package net.binaryvibrance.schematicmetablocks.jobs;

import com.google.common.base.Function;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.tileentity.InteriorAirMarkerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class ChunkToProcess implements IJob, IWorldJob
{
    private final World world;
    private final int _x;
    private final int _z;
    AtomicInteger idGenerator = new AtomicInteger();
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

    public void start()
    {
        final JobProcessor jobProcessor = JobProcessor.Instance;

        final int id = idGenerator.incrementAndGet();
        final Function<IJob, Boolean> jobIsCorrelated = new Function<IJob, Boolean>()
        {
            @Override
            public Boolean apply(IJob iJob)
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
                jobProcessor.unscheduleJobsIf(JobType.WORLD_TICK, jobIsCorrelated);
                return;
            }

            int blockToProcess = blocksToProcess.poll();
            int x = decodeX(blockToProcess);
            int y = decodeY(blockToProcess);
            int z = decodeZ(blockToProcess);


            Block currentBlock = chunk.getBlock(x, y, z);
            if (currentBlock instanceof BlockAir)
            {
                final SetBlock job = new SetBlock(id, world, chunkXStart + x, y, chunkZStart + z, ModBlock.blockImplicitAir, 0);
                jobProcessor.scheduleJob(JobType.WORLD_TICK, job);
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
            if (_jobObsolete)
            {
                Logger.info("Another job was requested, cancelling this one.");
                jobProcessor.unscheduleJobsIf(JobType.WORLD_TICK, jobIsCorrelated);
                return;
            }

            for (int x = 0; x < 16; ++x)
            {

                for (int z = 0; z < 16; ++z)
                {
                    if (processed[createIndex(x, y, z)] == 0 && chunk.getBlock(x, y, z) == ModBlock.blockImplicitAir)
                    {
                        final SetBlock setBlock = new SetBlock(id, world, chunkXStart + x, y, chunkZStart + z, Blocks.air, 0);
                        jobProcessor.scheduleJob(JobType.WORLD_TICK, setBlock);
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

    @Override
    public World getWorld()
    {
        return world;
    }
}
