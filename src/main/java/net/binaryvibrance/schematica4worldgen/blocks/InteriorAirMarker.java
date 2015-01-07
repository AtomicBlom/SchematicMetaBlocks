package net.binaryvibrance.schematica4worldgen.blocks;

import net.binaryvibrance.schematica4worldgen.tileentity.InteriorAirMarkerTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class InteriorAirMarker extends S4WGBlock
{
    public static final String NAME = "blockInteriorAirMarker";

    public InteriorAirMarker()
    {
        super(Material.glass);
        this.setBlockName(NAME);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.01F, 1.0F);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new InteriorAirMarkerTileEntity();
    }
}
