package net.binaryvibrance.schematicmetablocks.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.binaryvibrance.schematicmetablocks.TheMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class MetaBlock extends Block
{
    protected MetaBlock(Material material, boolean addToCreativeTab)
    {
        super(material);
        if (addToCreativeTab) setCreativeTab(TheMod.CREATIVE_TAB);
    }

    protected MetaBlock(Material material) { this(material, true); }

    @SuppressWarnings("WeakerAccess")
    protected static String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
    }

    @Override
    public String getUnlocalizedName()
    {
        //noinspection StringConcatenationMissingWhitespace
        return "tile." + TheMod.RESOURCE_PREFIX + getUnwrappedUnlocalizedName(super.getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(getUnwrappedUnlocalizedName(getUnlocalizedName()));
    }
}
