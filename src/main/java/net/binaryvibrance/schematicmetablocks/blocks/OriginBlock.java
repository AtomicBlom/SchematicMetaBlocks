package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.TheMod;
import net.binaryvibrance.schematicmetablocks.proxy.ClientProxy;
import net.binaryvibrance.schematicmetablocks.proxy.IBlockWithFloor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.List;

public class OriginBlock extends MetaBlock implements IBlockWithFloor
{
    public static final String NAME = "blockOrigin";
    private IIcon floorIcon;

    public OriginBlock()
    {
        super(Material.glass);
        this.setBlockName(NAME);
    }

    @Override
    public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity entity)
    {
        if (!(entity instanceof EntityPlayer)) {
            super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, entity);
        }

    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public IIcon getFloorIcon()
    {
        return floorIcon;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        floorIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":originFloor");
    }

    @Override
    public int getRenderType()
    {
        return ClientProxy.originBlockRendererId;
    }
}
