package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class RegionBlock extends Block
{
    public static final PropertyBool IS_VALID = PropertyBool.create("valid");

    public static final PropertyBool IS_PRIMARY_RENDERER = PropertyBool.create("is_primary_renderer");

    public RegionBlock()
    {
        super(Material.GLASS);

        this.setDefaultState(
                this.blockState.getBaseState()
                    .withProperty(IS_VALID, false)
                        .withProperty(IS_PRIMARY_RENDERER, false)
        );
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_PRIMARY_RENDERER, IS_VALID);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        RegionTileEntity regionTileEntity = RegionTileEntity.tryGetTileEntity(worldIn, pos);
        return state.withProperty(IS_VALID, regionTileEntity != null && regionTileEntity.isPaired())
                .withProperty(IS_PRIMARY_RENDERER, regionTileEntity != null && regionTileEntity.isRenderBlock());
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
    @Deprecated
    public boolean isFullCube(IBlockState state)
    {
        return false;
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
