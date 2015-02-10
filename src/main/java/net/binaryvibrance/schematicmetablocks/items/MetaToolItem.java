package net.binaryvibrance.schematicmetablocks.items;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.schematic.SchematicLoader;
import net.binaryvibrance.schematicmetablocks.schematic.WorldBlockCoord;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import javax.swing.plaf.synth.Region;
import java.util.List;

public class MetaToolItem extends SchematicMetaBlockItem
{
    public static final String NAME = "metaTool";

    public MetaToolItem() {
        setUnlocalizedName(NAME);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer p_77648_2_, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {

        //Logger.info("onItemUse - " + (world.isRemote ? "remote" : "local"));
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof RegionTileEntity) {
            final WorldBlockCoord clickedWorldCoords = new WorldBlockCoord(x, y, z);
            if (world.isRemote) {
                return true;
            }

            RegionTileEntity selectedRegion = (RegionTileEntity)tileEntity;
            NBTTagCompound coords = itemStack.getTagCompound();
            if (coords == null) {

                Logger.info("Setting MetaTool's coordinates - %s", clickedWorldCoords);
                //Setting item coords

                itemStack.setTagCompound(clickedWorldCoords.toNBT());
            } else {
                //Applying coords
                final WorldBlockCoord oppositeWorldCoords = WorldBlockCoord.fromNBT(coords);

                if (oppositeWorldCoords.equals(clickedWorldCoords)) {
                    Logger.info("Clearing MetaTool");
                    itemStack.setTagCompound(null);
                    return false;
                }

                Logger.info("Applying MetaTool Coordinates - %s", oppositeWorldCoords);

                tileEntity = world.getTileEntity(oppositeWorldCoords.x, oppositeWorldCoords.y, oppositeWorldCoords.z);
                if (!(tileEntity instanceof RegionTileEntity)) {
                    //Message player. The tile entity was probably destroyed.
                    return false;
                }
                RegionTileEntity oppositeRegion = (RegionTileEntity)tileEntity;

                selectedRegion.setOpposite(oppositeRegion);
                oppositeRegion.setOpposite(selectedRegion);
                itemStack.setTagCompound(null);
            }
            return true;
        }

        return false;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_)
    {
        final NBTTagCompound coords = itemStack.getTagCompound();
        if (coords != null) {
            final WorldBlockCoord worldBlockCoord = WorldBlockCoord.fromNBT(coords);
            list.add(String.format("(%d, %d, %d)", worldBlockCoord.x, worldBlockCoord.y, worldBlockCoord.z));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        final NBTTagCompound coords = stack.getTagCompound();
        String coordText = "";
        if (coords != null) {
            final WorldBlockCoord worldBlockCoord = WorldBlockCoord.fromNBT(coords);
            coordText = String.format(" - (%d, %d, %d)", worldBlockCoord.x, worldBlockCoord.y, worldBlockCoord.z);
        }

        return super.getItemStackDisplayName(stack) + coordText;
    }
}
