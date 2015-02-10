package net.binaryvibrance.schematicmetablocks.jobs;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.schematic.WorldBlockCoord;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class VerifyOpposingRegionBlockJob implements IJob, IWorldJob
{
    private final RegionTileEntity regionTileEntity;

    public VerifyOpposingRegionBlockJob(RegionTileEntity regionTileEntity) {

        this.regionTileEntity = regionTileEntity;
    }

    @Override
    public void start()
    {
        final RegionTileEntity opposite = regionTileEntity.getOpposite();
        boolean isValid = true;
        final WorldBlockCoord worldBlockLocation = regionTileEntity.getWorldBlockLocation();
        if (opposite == null || !worldBlockLocation.equals(opposite.getOppositeLocation())) {
            isValid = false;
        }

        /*final WorldBlockCoord location = ;
        if (location == null) {
            return;
        }
        final TileEntity tileEntity = regionTileEntity.getWorldObj().getTileEntity(location.x, location.y, location.z);
        boolean isValid = false;
        if (tileEntity instanceof RegionTileEntity) {
            RegionTileEntity regionTileEntity = (RegionTileEntity)tileEntity;
            if (regionTileEntity.getWorldBlockLocation().equals(this.regionTileEntity.getWorldBlockLocation())) {
                isValid = true;
            }
        }*/

        Logger.info("VerifyOpposingRegionBlockJob: %s - %s", worldBlockLocation, isValid);
        if (!isValid) {
            regionTileEntity.setOpposite(null);
        }
    }

    @Override
    public boolean represents(IJob otherJob)
    {
        if (otherJob instanceof VerifyOpposingRegionBlockJob) {
            VerifyOpposingRegionBlockJob job = (VerifyOpposingRegionBlockJob)otherJob;
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
