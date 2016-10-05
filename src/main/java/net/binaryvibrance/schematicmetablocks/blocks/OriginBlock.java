package net.binaryvibrance.schematicmetablocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class OriginBlock extends Block //implements IBlockWithFloor
{
    public static final String NAME = "blockOrigin";
    //private IIcon floorIcon;

    public OriginBlock()
    {
        super(Material.GLASS);
        //this.setBlockName(NAME);
    }

    /*
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
    */
    /*@Override
    public int getRenderType()
    {
        return ClientProxy.originBlockRendererId;
    }*/


    @Override
    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
    {
        if (!(entityIn instanceof EntityPlayer))
        {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn);
        }
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
