package net.binaryvibrance.schematicmetablocks.jobs;

import net.binaryvibrance.schematicmetablocks.schematic.SchematicLoader;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Steven on 22/05/2015.
 */
public class RenderWorldJob implements IJob {
    private final SchematicLoader schematicLoader;
    private final ResourceLocation resource;
    private final World world;
    private final BlockPos blockPos;
    private final int chunkX;
    private final int chunkZ;
    private final EnumFacing rotation;
    private final boolean flip;

    public RenderWorldJob(SchematicLoader schematicLoader, ResourceLocation resource, World world, BlockPos blockPos, int chunkX, int chunkZ, EnumFacing rotation, boolean flip) {
        this.schematicLoader = schematicLoader;
        this.resource = resource;
        this.world = world;
        this.blockPos = blockPos;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.rotation = rotation;
        this.flip = flip;
    }

    @Override
    public void start() {
        schematicLoader.renderSchematicToSingleChunk(resource, world, blockPos, chunkX, chunkZ, rotation, flip);
    }

    @Override
    public boolean represents(IJob otherJob) {
        if (otherJob instanceof RenderWorldJob) {
            RenderWorldJob otherRenderJob = (RenderWorldJob) otherJob;
            return otherRenderJob.chunkX == this.chunkX &&
                    otherRenderJob.chunkZ == this.chunkZ &&
                    otherRenderJob.blockPos.getX() == this.blockPos.getX() &&
                    otherRenderJob.blockPos.getY() == this.blockPos.getY() &&
                    otherRenderJob.blockPos.getZ() == this.blockPos.getZ() &&
                    otherRenderJob.world == this.world;
        }
        return false;
    }

    @Override
    public void notifyUpdatedJob() {

    }
}
