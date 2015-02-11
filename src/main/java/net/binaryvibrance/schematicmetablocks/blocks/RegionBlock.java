package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.TheMod;
import net.binaryvibrance.schematicmetablocks.proxy.ClientProxy;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.List;

public class RegionBlock extends MetaBlock
{
    public static final String NAME = "blockRegion";
    private IIcon badIcon;
    private IIcon opposingIcon;

    public RegionBlock()
    {
        super(Material.redstoneLight);
        this.setBlockName(NAME);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity entity)
    {
        if (!(entity instanceof EntityPlayer)) {
            super.addCollisionBoxesToList(world, x, y, z, p_149743_5_, p_149743_6_, entity);
        }
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new RegionTileEntity();
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        badIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":" + NAME + "-bad");
        opposingIcon = iconRegister.registerIcon(TheMod.MOD_ID + ":" + NAME + "-alt");
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        IIcon icon;
        RegionTileEntity tileEntity = RegionTileEntity.tryGetTileEntity(blockAccess, x, y, z);
        if (!tileEntity.isPaired()) {
            icon = badIcon;
        } else
        {
            if (tileEntity.isPrimaryBlock())
            {
                icon = blockIcon;
            } else
            {
                icon = opposingIcon;
            }
        }

        return icon;
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int p_149725_5_)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof RegionTileEntity)
        {
            RegionTileEntity regionTileEntity = (RegionTileEntity) tileEntity;
            final RegionTileEntity opposite = regionTileEntity.getOpposite();
            if (opposite != null)
            {
                opposite.setOppositeWithReverify(null);
            }
        }
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return ClientProxy.regionBlockRendererId;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass = pass;
        return pass < 2;
    }
}
