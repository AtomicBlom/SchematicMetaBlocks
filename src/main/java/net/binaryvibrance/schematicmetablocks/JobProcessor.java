package net.binaryvibrance.schematicmetablocks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.tileentity.InteriorAirMarkerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class JobProcessor
{
    public static JobProcessor Instance = new JobProcessor();
    private final Thread _thread;
    private final Object jobContextSwitchLock = new Object();

    private JobProcessor()
    {
        _thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ProcessJobs();
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
    public void OnWorldStopping(WorldEvent.Unload worldUnloadEvent)
    {
        synchronized (jobContextSwitchLock)
        {
            LinkedList<IJob> jobsToRemove = new LinkedList<IJob>();
            for (IJob job : chunksToProcess)
            {
                if (job instanceof ChunkToProcess)
                {
                    if (((ChunkToProcess) job).world == worldUnloadEvent.world)
                    {
                        jobsToRemove.add(job);
                    }
                }
            }
            chunksToProcess.removeAll(jobsToRemove);
            if (currentJob instanceof ChunkToProcess)
            {
                if (((ChunkToProcess) currentJob).world == worldUnloadEvent.world)
                {
                    currentJob.notifyUpdatedJob();
                }
            }
        }

    }

    private void ProcessJobs()
    {
        IJob chunkToProcess;
        try
        {
            while (true)
            {
                chunkToProcess = chunksToProcess.take();
                synchronized (jobContextSwitchLock)
                {
                    currentJob = chunkToProcess;
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

    private void scheduleChunkToProcess(IJob chunkToProcess)
    {
        synchronized (jobContextSwitchLock)
        {
            if (currentJob != null && currentJob.represents(chunkToProcess))
            {
                currentJob.notifyUpdatedJob();
            }

            for (IJob job : chunksToProcess)
            {
                if (chunkToProcess.represents(job))
                {
                    //There's a job for this chunk already on the queue, don't add a new one.
                    return;
                }
            }

            try
            {
                chunksToProcess.put(chunkToProcess);
            } catch (InterruptedException e)
            {

            }
        }
    }

    BlockingQueue<IJob> chunksToProcess = new LinkedBlockingDeque<IJob>();
    ConcurrentLinkedQueue<IJob> tickJobs = new ConcurrentLinkedQueue<IJob>();
    Queue<IJob> scheduledChunksToProcess = new LinkedList<IJob>();

    IJob currentJob;

    @SubscribeEvent
    public void onBlockChanged(BlockEvent.PlaceEvent blockPlacedEvent)
    {
        World world = blockPlacedEvent.blockSnapshot.world;
        ChunkToProcess chunkToProcess = new ChunkToProcess(world, blockPlacedEvent.x >> 4, blockPlacedEvent.z >> 4);
        scheduledChunksToProcess.add(chunkToProcess);
    }

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent blockBrokenEvent)
    {
        World world = blockBrokenEvent.world;
        ChunkToProcess chunkToProcess = new ChunkToProcess(world, blockBrokenEvent.x >> 4, blockBrokenEvent.z >> 4);
        scheduledChunksToProcess.add(chunkToProcess);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent tickEvent)
    {
        for (IJob job : scheduledChunksToProcess)
        {
            scheduleChunkToProcess(job);
        }
        scheduledChunksToProcess.clear();

        int jobQuota = 32;
        while (!tickJobs.isEmpty() && --jobQuota > 0)
        {
            IJob job = tickJobs.poll();
            job.start();
        }
    }

    public void processChunk(World world, int chunkX, int chunkZ)
    {
        ChunkToProcess chunkToProcess = new ChunkToProcess(world, chunkX, chunkZ);
        scheduleChunkToProcess(chunkToProcess);
    }

    private static interface IJob
    {
        void start();

        boolean represents(IJob otherJob);

        void notifyUpdatedJob();
    }

    private static class SetBlock implements IJob
    {

        public final int correlationId;
        private final World world;
        private final int x;
        private final int y;
        private final int z;
        private final Block block;
        private final int metadata;

        public SetBlock(int correlationId, World world, int x, int y, int z, Block block, int metadata)
        {
            this.correlationId = correlationId;
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.block = block;
            this.metadata = metadata;
        }

        @Override
        public void start()
        {
            world.setBlock(x, y, z, block, metadata, 3);
        }

        @Override
        public boolean represents(IJob otherJob)
        {
            return false;
        }

        @Override
        public void notifyUpdatedJob()
        {

        }
    }

    private static class ChunkToProcess implements IJob
    {
        private final World world;
        private final int _x;
        private final int _z;
        private boolean _jobObsolete;

        public ChunkToProcess(World world, int x, int z)
        {
            this.world = world;
            this._x = x;
            this._z = z;
        }

        private static int createIndex(int x, int y, int z)
        {
            return y << 8 | z << 4 | x;
        }

        private static int decodeX(int index)
        {
            return index & 15;
        }

        private static int decodeZ(int index)
        {
            return (index >> 4) & 15;
        }

        private static int decodeY(int index)
        {
            return (index >> 8);
        }

        AtomicInteger idGenerator = new AtomicInteger();

        public void start()
        {
            final int id = idGenerator.incrementAndGet();
            final Predicate<IJob> jobIsCorrelated = new Predicate<IJob>()
            {
                @Override
                public boolean test(IJob iJob)
                {
                    if (iJob instanceof SetBlock)
                    {
                        SetBlock job = (SetBlock) iJob;
                        if (job.correlationId == id)
                        {
                            return true;
                        }
                    }

                    return false;
                }
            };

            int worldHeight = world.getActualHeight();
            Chunk chunk = world.getChunkFromChunkCoords(_x, _z);
            int[] processed = new int[16 * 16 * worldHeight];
            for (int x1 = 0; x1 < 16; ++x1)
            {
                for (int z1 = 0; z1 < 16; ++z1)
                {
                    for (int y1 = 0; y1 < world.getActualHeight(); ++y1)
                    {
                        final Block block = chunk.getBlock(x1, y1, z1);
                        processed[createIndex(x1, y1, z1)] = block != Blocks.air && block != ModBlock.blockImplicitAir && block != ModBlock.blockOrigin ? -1 : 0;
                    }
                }
            }
            Queue<Integer> blocksToProcess = new LinkedList<Integer>();

            for (Object obj : chunk.chunkTileEntityMap.values())
            {
                TileEntity entity = (TileEntity) obj;
                if (entity instanceof InteriorAirMarkerTileEntity)
                {
                    blocksToProcess.add(createIndex(entity.xCoord & 15, entity.yCoord, entity.zCoord & 15));
                }
            }

            final int chunkZStart = chunk.zPosition * 16;
            final int chunkXStart = chunk.xPosition * 16;

            while (!blocksToProcess.isEmpty())
            {
                if (_jobObsolete)
                {
                    Logger.info("Another job was requested, cancelling this one.");
                    JobProcessor.Instance.tickJobs.removeIf(jobIsCorrelated);
                    return;
                }

                int blockToProcess = blocksToProcess.poll();
                int x = decodeX(blockToProcess);
                int y = decodeY(blockToProcess);
                int z = decodeZ(blockToProcess);


                Block currentBlock = chunk.getBlock(x, y, z);
                if (currentBlock instanceof BlockAir)
                {
                    JobProcessor.Instance.tickJobs.add(new SetBlock(id, world, chunkXStart + x, y, chunkZStart + z, ModBlock.blockImplicitAir, 0));
                }

                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
                {
                    int neighbourX = x + d.offsetX;
                    int neighbourY = y + d.offsetY;
                    int neighbourZ = z + d.offsetZ;

                    if (neighbourX >= 0 && neighbourX <= 15 && neighbourY >= 0 && neighbourY < worldHeight && neighbourZ >= 0 && neighbourZ <= 15)
                    {
                        int neighbourIndex = createIndex(neighbourX, neighbourY, neighbourZ);
                        if (processed[neighbourIndex] == 0)
                        {
                            processed[neighbourIndex] = 1;
                            blocksToProcess.add(neighbourIndex);
                        }
                    }
                }
            }

            for (int y = 0; y < world.getActualHeight(); ++y)
            {
                for (int x = 0; x < 16; ++x)
                {
                    for (int z = 0; z < 16; ++z)
                    {
                        if (_jobObsolete)
                        {
                            Logger.info("Another job was requested, cancelling this one.");
                            JobProcessor.Instance.tickJobs.removeIf(jobIsCorrelated);
                            return;
                        }

                        if (processed[createIndex(x, y, z)] == 0 && chunk.getBlock(x, y, z) == ModBlock.blockImplicitAir)
                        {
                            JobProcessor.Instance.tickJobs.add(new SetBlock(id, world, chunkXStart + x, y, chunkZStart + z, Blocks.air, 0));
                        }
                    }
                }
            }
        }

        @Override
        public boolean represents(IJob otherJob)
        {
            if (!(otherJob instanceof ChunkToProcess)) return false;
            ChunkToProcess otherChunkToProcess = (ChunkToProcess) otherJob;
            return this._x == otherChunkToProcess._x &&
                    this._z == otherChunkToProcess._z &&
                    this.world.equals(otherChunkToProcess.world);
        }

        @Override
        public void notifyUpdatedJob()
        {
            _jobObsolete = true;
        }
    }
}
