package net.binaryvibrance.schematicmetablocks.jobs;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;

@Mod.EventBusSubscriber
public class JobProcessor
{
    public static JobProcessor Instance = new JobProcessor();
    private final Thread _thread;
    private final Object jobContextSwitchLock = new Object();
    private IJob currentBackgroundJob = null;
    private BlockingQueue<IJob> scheduledBackgroundJobs = new LinkedBlockingDeque<>();
    private Queue<IJob> scheduledTickJobs = new ConcurrentLinkedQueue<>();

    private JobProcessor()
    {
        _thread = new Thread(this::ProcessBackgroundJobs);
        _thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(_thread::interrupt));
    }

    @SubscribeEvent
    //TODO: Move this to WorldListener
    public void OnWorldStopping(WorldEvent.Unload worldUnloadEvent)
    {
        synchronized (jobContextSwitchLock)
        {
            final Deque<IJob> jobsToRemove = new LinkedList<>();
            for (final IJob job : scheduledBackgroundJobs)
            {
                if (job instanceof IWorldJob)
                {
                    if (((IWorldJob) job).getWorld() == worldUnloadEvent.getWorld())
                    {
                        jobsToRemove.add(job);
                    }
                }
            }
            scheduledBackgroundJobs.removeAll(jobsToRemove);
            if (currentBackgroundJob instanceof IWorldJob)
            {
                if (((IWorldJob) currentBackgroundJob).getWorld() == worldUnloadEvent.getWorld())
                {
                    currentBackgroundJob.notifyUpdatedJob();
                }
            }
        }

    }

    private void ProcessBackgroundJobs()
    {
        IJob job;
        try
        {
            while (true)
            {
                job = scheduledBackgroundJobs.take();
                synchronized (jobContextSwitchLock)
                {
                    currentBackgroundJob = job;
                }
                if (job != null)
                {
                    Logger.info("Starting Background Job %s", job.getClass().getName());
                    job.start();
                }
            }
        } catch (InterruptedException e)
        {

        }
    }

    private void scheduleBackgroundJob(IJob jobToProcess)
    {
        synchronized (jobContextSwitchLock)
        {
            if (currentBackgroundJob != null && currentBackgroundJob.represents(jobToProcess))
            {
                currentBackgroundJob.notifyUpdatedJob();
            }

            for (final IJob job : scheduledBackgroundJobs)
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
        int jobQuota = 32;
        while (!scheduledTickJobs.isEmpty() && --jobQuota > 0)
        {
            final IJob job = scheduledTickJobs.poll();
            Logger.info("Starting World Tick Job %s", job.getClass().getName());
            job.start();
        }
    }

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

    public void unscheduleJobsIf(JobType jobType, Function<IJob, Boolean> predicate)
    {
        switch (jobType)
        {
            case WORLD_TICK:
                removeJobs(scheduledTickJobs, predicate);
                break;
            case BACKGROUND:
                removeJobs(scheduledBackgroundJobs, predicate);
                break;
        }
    }

    private void removeJobs(Collection<IJob> jobs, Function<IJob, Boolean> predicate) {
        LinkedList<IJob> jobsToRemove = new LinkedList<IJob>();
        for (final IJob iJob : jobs.toArray(new IJob[0]))
        {
            if (predicate.apply(iJob)) {
                jobsToRemove.add(iJob);
            }
        }

        for (final IJob job : jobs)
        {
            jobs.remove(job);
        }
    }
}

