package net.binaryvibrance.schematicmetablocks.jobs;

import com.google.common.base.Function;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.tileentity.InteriorAirMarkerTileEntity;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
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

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x1 = 0; x1 < 16; ++x1)
        {
            for (int z1 = 0; z1 < 16; ++z1)
            {
                for (int y1 = 0; y1 < world.getActualHeight(); ++y1)
                {
                    pos.setPos(x1, y1, z1);

                    final IBlockState block = chunk.getBlockState(pos);
                    processed[createIndex(x1, y1, z1)] = block != Blocks.AIR && block != ModBlock.blockImplicitAir && block != ModBlock.blockOrigin ? -1 : 0;
                }
            }
        }
        Queue<Integer> blocksToProcess = new LinkedList<Integer>();

        for (Object obj : chunk.getTileEntityMap().values())
        {
            TileEntity entity = (TileEntity) obj;
            if (entity instanceof InteriorAirMarkerTileEntity)
            {
                BlockPos entityPos = entity.getPos();

                blocksToProcess.add(createIndex(entityPos.getX() & 15, entityPos.getY(), entityPos.getZ() & 15));
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

            IBlockState currentBlock = chunk.getBlockState(x, y, z);
            if (currentBlock instanceof BlockAir)
            {
                final SetBlock job = new SetBlock(id, world, new BlockPos(chunkXStart + x, y, chunkZStart + z), ModBlock.blockImplicitAir.getDefaultState());
                jobProcessor.scheduleJob(JobType.WORLD_TICK, job);
            }

            for (EnumFacing d : EnumFacing.VALUES)
            {
                int neighbourX = x + d.getFrontOffsetX();
                int neighbourY = y + d.getFrontOffsetY();
                int neighbourZ = z + d.getFrontOffsetZ();

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
                    if (processed[createIndex(x, y, z)] == 0 && chunk.getBlockState(x, y, z) == ModBlock.blockImplicitAir)
                    {
                        final SetBlock setBlock = new SetBlock(id, world, new BlockPos(chunkXStart + x, y, chunkZStart + z), Blocks.AIR.getDefaultState());
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
