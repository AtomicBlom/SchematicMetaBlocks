package net.binaryvibrance.schematicmetablocks.items;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.schematic.SchematicLoader;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import javax.swing.plaf.synth.Region;

public class MetaToolItem extends SchematicMetaBlockItem
{
    public static final String NAME = "metaTool";

    public MetaToolItem() {
        setUnlocalizedName(NAME);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        Logger.info("OnItemRightClick - " + (p_77659_2_.isRemote ? "remote" : "local"));
        return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);

    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer p_77648_2_, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        Logger.info("onItemUse - " + (world.isRemote ? "remote" : "local"));
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof RegionTileEntity) {
            RegionTileEntity selectedRegion = (RegionTileEntity)tileEntity;
            NBTTagCompound coords = getCoords(itemStack);
            if (coords == null) {
                //Setting item coords
                setCoords(itemStack, x, y, z);
            } else {
                //Applying coords
                int altX = coords.getInteger("X");
                int altY = coords.getInteger("Y");
                int altZ = coords.getInteger("Z");

                tileEntity = world.getTileEntity(altX, altY, altZ);
                if (!(tileEntity instanceof RegionTileEntity)) {
                    //Message player. The tile entity was probably destroyed.
                    return false;
                }
                RegionTileEntity oppositeRegion = (RegionTileEntity)tileEntity;

                selectedRegion.setOpposite(oppositeRegion);
                oppositeRegion.setOpposite(selectedRegion);
            }
            return true;
        }

        return false;
    }



    private void setCoords(ItemStack itemStack, int x, int y, int z)
    {
        NBTTagCompound coords = itemStack.getTagCompound();
        if (coords == null) {
            coords = new NBTTagCompound();
            itemStack.setTagCompound(coords);
        }
        coords.setInteger("X", x);
        coords.setInteger("Y", y);
        coords.setInteger("Z", z);

    }

    private NBTTagCompound getCoords(ItemStack itemStack)
    {
        NBTTagCompound coords = itemStack.getTagCompound();
        if (coords == null) {
            return null;
        }
        return coords.getCompoundTag("Coord");
    }
}
