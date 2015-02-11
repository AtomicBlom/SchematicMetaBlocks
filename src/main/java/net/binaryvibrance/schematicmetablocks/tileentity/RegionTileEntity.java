package net.binaryvibrance.schematicmetablocks.tileentity;

import com.google.common.base.Objects;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.jobs.JobProcessor;
import net.binaryvibrance.schematicmetablocks.jobs.JobType;
import net.binaryvibrance.schematicmetablocks.jobs.VerifyOpposingRegionBlockJob;
import net.binaryvibrance.schematicmetablocks.schematic.WorldBlockCoord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class RegionTileEntity extends TileEntity
{
    private WorldBlockCoord oppositeLocation;
    private String schematicName;

    public static RegionTileEntity tryGetTileEntity(IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntity tileEntity = blockAccess.getTileEntity(x, y, z);
        if (!(tileEntity instanceof RegionTileEntity)) return null;
        return (RegionTileEntity) tileEntity;
    }

    public boolean isPaired()
    {
        return oppositeLocation != null;
    }

    public void setOppositeWithReverify(RegionTileEntity newOpposite)
    {
        Logger.info("Setting Region Opposite: %s", this);
        RegionTileEntity currentOpposite = getOpposite();


        this.oppositeLocation = newOpposite == null ? null : newOpposite.getWorldBlockLocation();
        if (currentOpposite != null)
        {
            Logger.info("Scheduling job to clear opposite %s", currentOpposite);
            JobProcessor.Instance.scheduleJob(JobType.WORLD_TICK, new VerifyOpposingRegionBlockJob(currentOpposite));
        }
        sendUpdate();
    }

    public WorldBlockCoord getOppositeLocation()
    {
        return oppositeLocation;
    }

    public RegionTileEntity getOpposite()
    {
        if (oppositeLocation == null) return null;
        TileEntity tileEntity = worldObj.getTileEntity(oppositeLocation.x, oppositeLocation.y, oppositeLocation.z);
        if (!(tileEntity instanceof RegionTileEntity)) return null;
        return (RegionTileEntity) tileEntity;
    }

    public void setOpposite(RegionTileEntity newOpposite)
    {
        Logger.info("Setting Region Opposite: %s", this);
        this.oppositeLocation = newOpposite == null ? null : newOpposite.getWorldBlockLocation();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.oppositeLocation = WorldBlockCoord.fromNBT(nbt.getCompoundTag("Opposite"));
        this.schematicName = nbt.hasKey("SchematicName") ? nbt.getString("SchematicName") : null;
        Logger.info("Reading from NBT: %s", this);
        if (this.hasWorldObj() && !worldObj.isRemote)
        {
            JobProcessor.Instance.scheduleJob(JobType.WORLD_TICK, new VerifyOpposingRegionBlockJob(this));
        }
        if (worldObj != null && worldObj.isRemote)
        {
            sendUpdate();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        Logger.info("writing to NBT: %s", this);
        if (oppositeLocation != null)
        {
            nbt.setTag("Opposite", oppositeLocation.toNBT());
        }
        if (schematicName != null)
        {
            nbt.setString("SchematicName", schematicName);
        }
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

    public boolean isPrimaryBlock()
    {
        final RegionTileEntity opposite = getOpposite();
        if (opposite == null) return false;
        final WorldBlockCoord other = getOppositeLocation();
        int thisDistance = xCoord * xCoord + yCoord * yCoord + zCoord * zCoord;
        int otherDistance = other.x * other.x + other.y * other.y + other.z * other.z;

        //Just doing' it the deterministic way. Doesn't really matter at this point as long as it doesn't flicker.
        if (thisDistance < otherDistance) return true;
        if (Math.abs(xCoord) < Math.abs(other.x)) return true;
        if (Math.abs(zCoord) < Math.abs(other.z)) return true;
        if (Math.abs(yCoord) < Math.abs(other.y)) return true;
        return false;
    }

    public WorldBlockCoord getWorldBlockLocation()
    {
        if (worldObj == null) return null;
        return new WorldBlockCoord(xCoord, yCoord, zCoord);
    }

    private void sendUpdate()
    {
        markDirty();
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, getBlockType(), 1, isPaired() ? 1 : 0);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("xCoord", xCoord)
                .add("yCoord", yCoord)
                .add("zCoord", zCoord)
                .add("oppositeLocation", oppositeLocation)
                .add("isRemote", worldObj == null ? "unknown" : worldObj.isRemote)
                .add("schematicName", schematicName)
                .toString();
    }

    public String getSchematicName()
    {
        return schematicName;
    }

    public void setSchematicName(String schematicName)
    {
        setSchematicNameInternal(schematicName);
        if (isPaired())
        {
            getOpposite().setSchematicNameInternal(schematicName);
        }

    }

    private void setSchematicNameInternal(String schematicName)
    {
        this.schematicName = schematicName;
        sendUpdate();
    }
}
