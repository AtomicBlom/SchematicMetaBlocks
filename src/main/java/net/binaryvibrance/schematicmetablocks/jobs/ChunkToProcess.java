package net.binaryvibrance.schematicmetablocks.jobs;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.tileentity.InteriorAirMarkerTileEntity;
import net.minecraft.block.Block;
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
import java.util.function.Function;

public class ChunkToProcess implements IJob, IWorldJob
{
    private final World world;
    private final int chunkX;
    private final int chunkZ;
    private AtomicInteger idGenerator = new AtomicInteger();
    private boolean _jobObsolete;

    public ChunkToProcess(World world, int chunkX, int chunkZ)
    {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
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
        return index >> 4 & 15;
    }

    private static int decodeY(int index)
    {
        return index >> 8;
    }

    public void start()
    {
        final JobProcessor jobProcessor = JobProcessor.Instance;

        final int id = idGenerator.incrementAndGet();
        final Function<IJob, Boolean> jobIsCorrelated = iJob ->
        {
            if (iJob instanceof SetBlock)
            {
                final SetBlock job = (SetBlock) iJob;
                if (job.correlationId == id)
                {
                    return true;
                }
            }

            return false;
        };

        Block blockToCheck;
        IBlockState blockStateToCheck;

        final int worldHeight = world.getActualHeight();
        final Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
        final int[] processed = new int[16 * 16 * worldHeight];

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x1 = 0; x1 < 16; ++x1)
        {
            for (int z1 = 0; z1 < 16; ++z1)
            {
                for (int y1 = 0; y1 < world.getActualHeight(); ++y1)
                {
                    pos.setPos(chunkX << 4 | x1, y1, chunkZ << 4 | z1);

                    blockStateToCheck = chunk.getBlockState(pos);
                    blockToCheck = blockStateToCheck.getBlock();
                    final int index = createIndex(x1, y1, z1);
                    final boolean isAir = blockToCheck != Blocks.AIR && blockToCheck != ModBlock.blockImplicitAir && blockToCheck != ModBlock.blockOrigin;
                    processed[index] = isAir ? -1 : 0;
                }
            }
        }
        final Queue<Integer> blocksToProcess = new LinkedList<>();

        for (final Object obj : chunk.getTileEntityMap().values())
        {
            final TileEntity entity = (TileEntity) obj;
            if (entity instanceof InteriorAirMarkerTileEntity)
            {
                final BlockPos entityPos = entity.getPos();

                final int index = createIndex(entityPos.getX() & 15, entityPos.getY(), entityPos.getZ() & 15);
                blocksToProcess.add(index);
            }
        }

        final int chunkZStart = chunk.zPosition * 16;
        final int chunkXStart = chunk.xPosition * 16;

        final BlockPos.MutableBlockPos thisBlockPos = new BlockPos.MutableBlockPos();

        while (!blocksToProcess.isEmpty())
        {
            if (_jobObsolete)
            {
                Logger.info("Another job was requested, cancelling this one.");
                jobProcessor.unscheduleJobsIf(JobType.WORLD_TICK, jobIsCorrelated);
                return;
            }

            final int blockToProcess = blocksToProcess.poll();
            final int x = decodeX(blockToProcess);
            final int y = decodeY(blockToProcess);
            final int z = decodeZ(blockToProcess);

            blockStateToCheck = chunk.getBlockState(x, y, z);
            blockToCheck = blockStateToCheck.getBlock();
            if (blockToCheck instanceof BlockAir)
            {
                final SetBlock job = new SetBlock(id, world, new BlockPos(chunkXStart + x, y, chunkZStart + z), ModBlock.blockImplicitAir.getDefaultState());
                jobProcessor.scheduleJob(JobType.WORLD_TICK, job);
            }

            thisBlockPos.setPos(x, y, z);

            for (final EnumFacing d : EnumFacing.VALUES)
            {
                final BlockPos neighbour = thisBlockPos.offset(d);
                final int neighbourX = neighbour.getX();
                final int neighbourY = neighbour.getY();
                final int neighbourZ = neighbour.getZ();

                if (neighbourX >= 0 && neighbourX <= 15 && neighbourY >= 0 && neighbourY < worldHeight && neighbourZ >= 0 && neighbourZ <= 15)
                {
                    final int neighbourIndex = createIndex(neighbourX, neighbourY, neighbourZ);
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
                    if (processed[createIndex(x, y, z)] == 0)
                    {
                        blockStateToCheck = chunk.getBlockState(x, y, z);
                        blockToCheck = blockStateToCheck.getBlock();
                        if (blockToCheck == ModBlock.blockImplicitAir)
                        {
                            final SetBlock setBlock = new SetBlock(id, world, new BlockPos(chunkXStart + x, y, chunkZStart + z), Blocks.AIR.getDefaultState());
                            jobProcessor.scheduleJob(JobType.WORLD_TICK, setBlock);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean represents(IJob otherJob)
    {
        if (!(otherJob instanceof ChunkToProcess)) return false;
        final ChunkToProcess otherChunkToProcess = (ChunkToProcess) otherJob;
        return chunkX == otherChunkToProcess.chunkX &&
                chunkZ == otherChunkToProcess.chunkZ &&
                world.equals(otherChunkToProcess.world);
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
