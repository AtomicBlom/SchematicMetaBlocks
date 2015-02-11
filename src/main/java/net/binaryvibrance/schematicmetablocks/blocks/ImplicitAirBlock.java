package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.List;

public class ImplicitAirBlock extends MetaBlock
{
    public static final String NAME = "blockImplicitAir";

    public ImplicitAirBlock()
    {
        super(Material.glass, false);
        this.setBlockName(NAME);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return ClientProxy.insideMetadataBlockRendererId;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        Block b = world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
        if (b instanceof ImplicitAirBlock || b instanceof InteriorAirMarker)
        {
            return null;
        }

        return super.getIcon(world, x, y, z, side);
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_)
    {
        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass = pass;
        return pass != 0;
    }
}
