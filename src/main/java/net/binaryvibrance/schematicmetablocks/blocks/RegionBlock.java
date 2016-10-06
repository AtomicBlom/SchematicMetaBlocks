package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class RegionBlock extends Block
{
    public RegionBlock()
    {
        super(Material.REDSTONE_LIGHT);
    }

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

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof RegionTileEntity)
        {
            final RegionTileEntity regionTileEntity = (RegionTileEntity) tileEntity;
            final RegionTileEntity opposite = regionTileEntity.getLinkedTileEntity();
            if (opposite != null)
            {
                opposite.setLinkedTileEntityWithReverify(null);
            }
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new RegionTileEntity();
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.TRANSLUCENT;
    }
}
