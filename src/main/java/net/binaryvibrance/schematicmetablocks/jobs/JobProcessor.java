package net.binaryvibrance.schematicmetablocks.jobs;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.minecraftforge.event.world.WorldEvent;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Predicate;

public class JobProcessor
{
    public static JobProcessor Instance = new JobProcessor();
    private final Thread _thread;
    private final Object jobContextSwitchLock = new Object();
    IJob currentBackgroundJob;
    private BlockingQueue<IJob> scheduledBackgroundJobs = new LinkedBlockingDeque<IJob>();
    private ConcurrentLinkedQueue<IJob> scheduledTickJobs = new ConcurrentLinkedQueue<IJob>();

    private JobProcessor()
    {
        _thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ProcessBackgroundJobs();
            }
        });
        _thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                _thread.interrupt();
            }
        });
    }

    @SubscribeEvent
    //TODO: Move this to WorldListener
    public void OnWorldStopping(WorldEvent.Unload worldUnloadEvent)
    {
        synchronized (jobContextSwitchLock)
        {
            LinkedList<IJob> jobsToRemove = new LinkedList<IJob>();
            for (IJob job : scheduledBackgroundJobs)
            {
                if (job instanceof IWorldJob)
                {
                    if (((IWorldJob) job).getWorld() == worldUnloadEvent.world)
                    {
                        jobsToRemove.add(job);
                    }
                }
            }
            scheduledBackgroundJobs.removeAll(jobsToRemove);
            if (currentBackgroundJob instanceof IWorldJob)
            {
                if (((IWorldJob) currentBackgroundJob).getWorld() == worldUnloadEvent.world)
                {
                    currentBackgroundJob.notifyUpdatedJob();
                }
            }
        }

    }

    private void ProcessBackgroundJobs()
    {
        IJob chunkToProcess;
        try
        {
            while (true)
            {
                chunkToProcess = scheduledBackgroundJobs.take();
                synchronized (jobContextSwitchLock)
                {
                    currentBackgroundJob = chunkToProcess;
                }
                if (chunkToProcess != null)
                {
                    chunkToProcess.start();
                }
            }
        } catch (InterruptedException e)
        {

        }
    }
    //private Queue<IJob> unscheduledBackgroundJobs = new LinkedList<IJob>();

    private void scheduleBackgroundJob(IJob jobToProcess)
    {
        synchronized (jobContextSwitchLock)
        {
            if (currentBackgroundJob != null && currentBackgroundJob.represents(jobToProcess))
            {
                currentBackgroundJob.notifyUpdatedJob();
            }

            for (IJob job : scheduledBackgroundJobs)
            {
                if (jobToProcess.represents(job))
                {
                    //There's a job for this chunk already on the queue, don't add a new one.
                    return;
                }
            }

            try
            {
                scheduledBackgroundJobs.put(jobToProcess);
            } catch (InterruptedException e)
            {

            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent tickEvent)
    {
        /*for (IJob job : unscheduledBackgroundJobs)
        {
            scheduleBackgroundJob(job);
        }
        unscheduledBackgroundJobs.clear();*/

        int jobQuota = 32;
        while (!scheduledTickJobs.isEmpty() && --jobQuota > 0)
        {
            IJob job = scheduledTickJobs.poll();
            job.start();
        }
    }

    /*public void scheduleWorldTickJob(IJob job)
    {
        scheduledTickJobs.add(job);
    }*/

    public void scheduleJob(JobType jobType, IJob job)
    {
        if (!_thread.isAlive()) {
            Logger.info("Not allocating job, thread is not alive.");
            return;
        }

        switch (jobType)
        {
            case WORLD_TICK:
                scheduledTickJobs.add(job);
                break;
            case BACKGROUND:
                scheduleBackgroundJob(job);
                break;
        }
    }

    public void unscheduleJobsIf(JobType jobType, Predicate<IJob> jobIsCorrelated)
    {
        switch (jobType)
        {
            case WORLD_TICK:
                scheduledTickJobs.removeIf(jobIsCorrelated);
                break;
            case BACKGROUND:
                scheduledBackgroundJobs.removeIf(jobIsCorrelated);
                break;
        }
    }
}

