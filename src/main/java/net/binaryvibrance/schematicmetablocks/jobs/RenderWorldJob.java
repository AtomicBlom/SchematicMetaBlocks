package net.binaryvibrance.schematicmetablocks.jobs;

import net.binaryvibrance.schematicmetablocks.jobs.IJob;
import net.binaryvibrance.schematicmetablocks.schematic.SchematicLoader;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Steven on 22/05/2015.
 */
public class RenderWorldJob implements IJob {
    private final SchematicLoader schematicLoader;
    private final ResourceLocation resource;
    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private final int chunkX;
    private final int chunkZ;
    private final ForgeDirection rotation;
    private final boolean flip;

    public RenderWorldJob(SchematicLoader schematicLoader, ResourceLocation resource, World world, int x, int y, int z, int chunkX, int chunkZ, ForgeDirection rotation, boolean flip) {
        this.schematicLoader = schematicLoader;
        this.resource = resource;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.rotation = rotation;
        this.flip = flip;
    }

    @Override
    public void start() {
        schematicLoader.renderSchematicToSingleChunk(resource, world, x, y, z, chunkX, chunkZ, rotation, flip);
    }

    @Override
    public boolean represents(IJob otherJob) {
        if (otherJob instanceof RenderWorldJob) {
            RenderWorldJob otherRenderJob = (RenderWorldJob) otherJob;
            return otherRenderJob.chunkX == this.chunkX &&
                    otherRenderJob.chunkZ == this.chunkZ &&
                    otherRenderJob.x == this.x &&
                    otherRenderJob.y == this.y &&
                    otherRenderJob.z == this.z &&
                    otherRenderJob.world == this.world;
        }
        return false;
    }

    @Override
    public void notifyUpdatedJob() {

    }
}
