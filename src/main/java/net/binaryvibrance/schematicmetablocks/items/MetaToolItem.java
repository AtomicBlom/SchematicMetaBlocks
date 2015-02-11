package net.binaryvibrance.schematicmetablocks.items;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.TheMod;
import net.binaryvibrance.schematicmetablocks.gui.GUIs;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.schematic.WorldBlockCoord;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.List;

public class MetaToolItem extends SchematicMetaBlockItem
{
    public static final String NAME = "metaTool";

    public MetaToolItem()
    {
        setUnlocalizedName(NAME);
    }

    private NBTTagCompound getTagCompoundSafe(ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null)
        {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        return nbt;
    }

    private MetaToolMode getMetaToolMode(ItemStack stack)
    {
        NBTTagCompound nbt = getTagCompoundSafe(stack);
        final MetaToolMode[] modes = MetaToolMode.values();
        return modes[nbt.getInteger("Mode") % modes.length];
    }

    private void setMetaToolMode(ItemStack stack, MetaToolMode mode)
    {
        NBTTagCompound nbt = getTagCompoundSafe(stack);

        nbt.setInteger("Mode", mode.ordinal());
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (player.isSneaking()) return false;

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof RegionTileEntity)
        {
            MetaToolMode mode = getMetaToolMode(stack);
            final RegionTileEntity regionTileEntity = (RegionTileEntity) tileEntity;

            switch (mode)
            {
                case PULL:
                    return moveRegion(world, new WorldBlockCoord(x, y, z), side, regionTileEntity, false);
                case PUSH:
                    return moveRegion(world, new WorldBlockCoord(x, y, z), side, regionTileEntity, true);
                case SAVE_SCHEMATIC:
                    return saveSchematic(world, player, regionTileEntity);
                case SET_NAME:
                    return setName(world, player, regionTileEntity);
                case CLEAR_METATOOL:
                    return clearMetaTool(stack, player);
                case SET_REGION:
                    return linkRegions(stack, world, player, new WorldBlockCoord(x, y, z), regionTileEntity);
                case CLEAR_REGION:
                    return clearRegion(regionTileEntity);
            }
        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote && player.isSneaking())
        {
            MetaToolMode mode = getMetaToolMode(stack);

            final MetaToolMode[] modes = MetaToolMode.values();
            mode = modes[(mode.ordinal() + 1) % modes.length];
            setMetaToolMode(stack, mode);
            stack.setStackDisplayName(getItemStackDisplayName(stack));
        }
        return stack;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer p_77624_2_, List list, boolean p_77624_4_)
    {
        final NBTTagCompound coords = itemStack.getTagCompound();
        if (coords != null)
        {
            final WorldBlockCoord worldBlockCoord = WorldBlockCoord.fromNBT(coords);
            list.add(String.format("(%d, %d, %d)", worldBlockCoord.x, worldBlockCoord.y, worldBlockCoord.z));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        final NBTTagCompound nbt = stack.getTagCompound();

        final MetaToolMode mode = getMetaToolMode(stack);
        String extra = "";
        String modeName = mode.name().toLowerCase();
        if (mode == MetaToolMode.SET_REGION)
        {
            if (nbt.hasKey("Link"))
            {
                final WorldBlockCoord worldBlockCoord = WorldBlockCoord.fromNBT(nbt.getCompoundTag("Link"));
                extra = String.format(" - (%d, %d, %d)", worldBlockCoord.x, worldBlockCoord.y, worldBlockCoord.z);
            } else
            {
                modeName = "set_region_unset";
            }
        }

        String name = StatCollector.translateToLocal(
                String.format("%s.%s.name%s", this.getUnlocalizedNameInefficiently(stack), modeName, extra)
        );
        return name;
    }

    private boolean saveSchematic(World world, EntityPlayer player, RegionTileEntity regionTileEntity)
    {
        if (world.isRemote)
        {

            if (!regionTileEntity.isPaired())
            {
                player.addChatComponentMessage(new ChatComponentText("Cannot save schematic, not paired"));
                return false;
            }

            final String schematicName = regionTileEntity.getSchematicName();
            if (schematicName == null || schematicName.trim().isEmpty())
            {
                player.addChatComponentMessage(new ChatComponentText("Cannot save schematic, name not specified"));
                return false;
            }

            final WorldBlockCoord location = regionTileEntity.getWorldBlockLocation();
            final WorldBlockCoord oppositeLocation = regionTileEntity.getOppositeLocation();

            Minecraft.getMinecraft().thePlayer.sendChatMessage(
                    String.format("/schematicaSave %d %d %d %d %d %d %s",
                            location.x, location.y, location.z,
                            oppositeLocation.x, oppositeLocation.y, oppositeLocation.z,
                            schematicName)
            );
        }


        return true;
    }

    private boolean setName(World world, EntityPlayer player, RegionTileEntity tileEntity)
    {
        if (world.isRemote)
        {
            player.openGui(TheMod.instance, GUIs.SCHEMATIC_NAME.ordinal(), world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            return true;
        }
        return false;
    }

    private boolean clearMetaTool(ItemStack stack, EntityPlayer player)
    {

        final NBTTagCompound nbt = getTagCompoundSafe(stack);
        nbt.removeTag("Link");
        player.addChatComponentMessage(new ChatComponentText("MetaTool coordinates cleared"));
        return true;
    }

    private boolean clearRegion(RegionTileEntity tileEntity)
    {
        if (!tileEntity.isPaired())
        {
            return false;
        }

        tileEntity.setOppositeWithReverify(null);
        return true;
    }

    private boolean moveRegion(World world, WorldBlockCoord worldBlockCoord, int side, RegionTileEntity tileEntity, boolean push)
    {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        if (push)
        {
            direction = direction.getOpposite();
        }

        int newX = worldBlockCoord.x + direction.offsetX;
        int newY = worldBlockCoord.y + direction.offsetY;
        int newZ = worldBlockCoord.z + direction.offsetZ;

        if (!world.isAirBlock(newX, newY, newZ))
        {
            return false;
        }

        world.setBlock(newX, newY, newZ, ModBlock.blockRegion);
        final RegionTileEntity regionTileEntity = RegionTileEntity.tryGetTileEntity(world, newX, newY, newZ);
        if (regionTileEntity != null && tileEntity.isPaired())
        {
            final RegionTileEntity opposite = tileEntity.getOpposite();
            tileEntity.setOpposite(null);
            opposite.setOppositeWithReverify(regionTileEntity);
            regionTileEntity.setOppositeWithReverify(opposite);
        }
        world.setBlockToAir(worldBlockCoord.x, worldBlockCoord.y, worldBlockCoord.z);

        return true;
    }

    private boolean linkRegions(ItemStack stack, World world, EntityPlayer player, WorldBlockCoord clickedBlockCoord, RegionTileEntity selectedRegion)
    {
        if (world.isRemote)
        {
            return true;
        }

        if (selectedRegion.isPaired())
        {
            player.addChatComponentMessage(new ChatComponentText("Region already paired. Clear it first if you're sure you want to do this."));
            return false;
        }

        NBTTagCompound nbt = getTagCompoundSafe(stack);

        if (!nbt.hasKey("Link"))
        {
            Logger.info("Setting MetaTool's coordinates - %s", clickedBlockCoord);
            //Setting item nbt
            nbt.setTag("Link", clickedBlockCoord.toNBT());
            stack.setStackDisplayName(getItemStackDisplayName(stack));
        } else
        {
            //Applying nbt
            final WorldBlockCoord oppositeWorldCoords = WorldBlockCoord.fromNBT(nbt.getCompoundTag("Link"));

            if (oppositeWorldCoords.equals(clickedBlockCoord))
            {
                clearMetaTool(stack, player);
                return false;
            }

            Logger.info("Applying MetaTool Coordinates - %s", oppositeWorldCoords);

            TileEntity tileEntity = world.getTileEntity(oppositeWorldCoords.x, oppositeWorldCoords.y, oppositeWorldCoords.z);
            if (!(tileEntity instanceof RegionTileEntity))
            {
                player.addChatComponentMessage(new ChatComponentText("Linked Region Block no longer exists."));
                return false;
            }
            RegionTileEntity oppositeRegion = (RegionTileEntity) tileEntity;

            selectedRegion.setOppositeWithReverify(oppositeRegion);
            oppositeRegion.setOppositeWithReverify(selectedRegion);
            clearMetaTool(stack, player);
        }
        return true;
    }
}
