package net.binaryvibrance.schematica4worldgen.blocks;

import net.minecraft.block.material.Material;

public class InsideBlock extends S4WGBlock
{
    public static final String NAME = "Inside";

    public InsideBlock()
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
}
