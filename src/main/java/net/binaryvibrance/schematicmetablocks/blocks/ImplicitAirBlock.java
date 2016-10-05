package net.binaryvibrance.schematicmetablocks.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class ImplicitAirBlock extends Block
{
    //public static final String NAME = "blockImplicitAir";

    public ImplicitAirBlock()
    {
        super(Material.GLASS);
        //this.setBlockName(NAME);
    }

    /*@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }*/

    /*@Override
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

    @Nullable
    @Override
    @Deprecated
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return null;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return false;
    }

    /*@Override
    public int getRenderBlockPass()
    {
        return 1;
    }
    */

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    /*@Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass = pass;
        return pass != 0;
    }*/
}
