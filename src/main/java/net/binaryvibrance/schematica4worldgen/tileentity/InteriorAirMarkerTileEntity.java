package net.binaryvibrance.schematica4worldgen.tileentity;

import net.binaryvibrance.schematica4worldgen.InteriorProcessor;
import net.minecraft.tileentity.TileEntity;

public class InteriorAirMarkerTileEntity extends TileEntity
{
    @Override
    public void updateContainingBlockInfo()
    {
        if (!worldObj.isRemote)
        {
            InteriorProcessor.Instance.processChunk(this.worldObj, this.xCoord >> 4, this.zCoord >> 4);
        }
    }
}
