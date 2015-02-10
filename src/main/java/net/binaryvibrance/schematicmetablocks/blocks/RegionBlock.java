package net.binaryvibrance.schematicmetablocks.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.binaryvibrance.schematicmetablocks.TheMod;
import net.binaryvibrance.schematicmetablocks.tileentity.InteriorAirMarkerTileEntity;
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
    public boolean isOpaqueCube()
    {
        return false;
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

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
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
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (tileEntity instanceof RegionTileEntity) {
            RegionTileEntity regionTileEntity = (RegionTileEntity)tileEntity;

            if (!regionTileEntity.isPaired()) {
                return badIcon;
            }

        }
        return null;
    }
}
