package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.InteriorProcessor;
import net.binaryvibrance.schematicmetablocks.tileentity.InteriorAirMarkerTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class InteriorAirMarker extends MetaBlock
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

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float tu, float tv, float tw, int metadata)
    {
        if (!world.isRemote) {
            InteriorProcessor.Instance.processChunk(world, x >> 4, z >> 4);
        }
        return metadata;
    }
}
