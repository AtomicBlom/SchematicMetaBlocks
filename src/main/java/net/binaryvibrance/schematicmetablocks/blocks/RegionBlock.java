package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import java.util.List;

public class RegionBlock extends Block
{
    //public static final String NAME = "blockRegion";
    //private IIcon badIcon;

    public RegionBlock()
    {
        super(Material.REDSTONE_LIGHT);
        //this.setBlockName(NAME);
    }

    /*@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        badIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":" + NAME + "-bad");
    }*/

    /*
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
*/
    /*@Override
    public int getRenderType()
    {
        return ClientProxy.regionBlockRendererId;
    }*/

    /*@Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        IIcon icon;
        RegionTileEntity tileEntity = RegionTileEntity.tryGetTileEntity(blockAccess, x, y, z);
        if (!tileEntity.isPaired())
        {
            icon = badIcon;
        } else
        {
            icon = blockIcon;
        }

        return icon;
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

    /*@Override
    public int getRenderBlockPass()
    {
        return 1;
    }*/

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

    /*@Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass = pass;
        return pass < 2;
    }*/
}
