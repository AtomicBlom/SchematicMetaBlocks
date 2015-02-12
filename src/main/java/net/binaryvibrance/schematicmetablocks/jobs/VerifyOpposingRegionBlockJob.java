package net.binaryvibrance.schematicmetablocks.jobs;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.schematic.WorldBlockCoord;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.world.World;

public class VerifyOpposingRegionBlockJob implements IJob, IWorldJob
{
    private final RegionTileEntity regionTileEntity;

    public VerifyOpposingRegionBlockJob(RegionTileEntity regionTileEntity)
    {

        if (!regionTileEntity.hasWorldObj() || regionTileEntity.getWorldObj().isRemote)
        {
            throw new UnsupportedOperationException("Attempt to process a tile entity with no world object.");
        }
        this.regionTileEntity = regionTileEntity;
    }

    @Override
    public void start()
    {
        final RegionTileEntity opposite = regionTileEntity.getLinkedTileEntity();
        boolean isValid = true;
        final WorldBlockCoord worldBlockLocation = regionTileEntity.getWorldBlockLocation();
        if (opposite == null || !worldBlockLocation.equals(opposite.getLinkedLocation()))
        {
            isValid = false;
        }

        Logger.info("VerifyOpposingRegionBlockJob: %s - %s", worldBlockLocation, isValid);
        if (!isValid)
        {
            regionTileEntity.setLinkedTileEntityWithReverify(null);
        }
    }

    @Override
    public boolean represents(IJob otherJob)
    {
        if (otherJob instanceof VerifyOpposingRegionBlockJob)
        {
            VerifyOpposingRegionBlockJob job = (VerifyOpposingRegionBlockJob) otherJob;
            return regionTileEntity.getWorldBlockLocation().equals(job.regionTileEntity.getWorldBlockLocation());
        }
        return false;
    }

    @Override
    public void notifyUpdatedJob()
    {

    }

    @Override
    public World getWorld()
    {
        return regionTileEntity.getWorldObj();
    }
}
