package net.binaryvibrance.schematicmetablocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.List;

public class ExplicitAirBlock extends MetaBlock
{
    public static final String NAME = "blockExplicitAir";

    public ExplicitAirBlock()
    {
        super(Material.glass);
        this.setBlockName(NAME);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        Block b = world.getBlock(x, y, z);
        if (b instanceof ExplicitAirBlock)
        {
            return false;
        }
        return true;
    }

    @Override
    public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity entity)
    {
        if (!(entity instanceof EntityPlayer))
        {
            super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, entity);
        }

    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}
