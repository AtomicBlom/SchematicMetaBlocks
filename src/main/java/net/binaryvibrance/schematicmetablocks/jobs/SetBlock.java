package net.binaryvibrance.schematicmetablocks.jobs;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

class SetBlock implements IJob
{

    public final int correlationId;
    private final World world;
    private final BlockPos pos;
    private final IBlockState state;

    public SetBlock(int correlationId, World world, BlockPos pos, IBlockState blockState)
    {
        this.correlationId = correlationId;
        this.world = world;
        this.pos = pos;
        state = blockState;
    }

    @Override
    public void start()
    {
        world.setBlockState(pos, state, 3);
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
