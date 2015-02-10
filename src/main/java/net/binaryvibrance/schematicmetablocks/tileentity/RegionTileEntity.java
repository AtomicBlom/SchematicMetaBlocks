package net.binaryvibrance.schematicmetablocks.tileentity;

import net.binaryvibrance.schematicmetablocks.schematic.WorldBlockCoord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class RegionTileEntity extends TileEntity
{
    private WorldBlockCoord oppositeLocation;

    public boolean isPaired()
    {
        return false;
    }

    public void setOpposite(RegionTileEntity opposite)
    {
        this.oppositeLocation = opposite.getWorldBlockLocation();
    }

    public WorldBlockCoord getOppositeLocation()
    {
        return oppositeLocation;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
    {
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (oppositeLocation != null )
        {
            nbt.setTag("opposite", oppositeLocation.toNBT());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.oppositeLocation = WorldBlockCoord.fromNBT(nbt.getCompoundTag("opposite"));

    }

    public WorldBlockCoord getWorldBlockLocation()
    {
        return new WorldBlockCoord(xCoord, yCoord, zCoord);
    }
}
