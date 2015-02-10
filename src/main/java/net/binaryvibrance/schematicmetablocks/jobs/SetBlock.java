package net.binaryvibrance.schematicmetablocks.jobs;

import net.minecraft.block.Block;
import net.minecraft.world.World;

class SetBlock implements IJob
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
