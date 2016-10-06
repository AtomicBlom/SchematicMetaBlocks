package net.binaryvibrance.schematicmetablocks.tileentity;

import com.google.common.base.Objects;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.jobs.JobProcessor;
import net.binaryvibrance.schematicmetablocks.jobs.JobType;
import net.binaryvibrance.schematicmetablocks.jobs.VerifyOpposingRegionBlockJob;
import net.binaryvibrance.schematicmetablocks.utility.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import javax.annotation.Nullable;

public class RegionTileEntity extends TileEntity
{
    private BlockPos oppositeLocation;
    private String schematicName;

    public static RegionTileEntity tryGetTileEntity(IBlockAccess blockAccess, BlockPos pos)
    {
        TileEntity tileEntity = blockAccess.getTileEntity(pos);
        if (!(tileEntity instanceof RegionTileEntity)) return null;
        return (RegionTileEntity) tileEntity;
    }

    public boolean isPaired()
    {
        return getLinkedTileEntity() != null;
    }

    public void setLinkedTileEntityWithReverify(RegionTileEntity newOpposite)
    {
        RegionTileEntity currentOpposite = getLinkedTileEntity();

        this.oppositeLocation = newOpposite == null ? null : newOpposite.getPos();
        Logger.info("Setting Region Opposite: %s", this);
        if (currentOpposite != null)
        {
            Logger.info("Scheduling job to clear opposite %s", currentOpposite);
            JobProcessor.Instance.scheduleJob(JobType.WORLD_TICK, new VerifyOpposingRegionBlockJob(currentOpposite));
        }
        sendUpdate();
    }

    public BlockPos getLinkedLocation()
    {
        return oppositeLocation;
    }

    public RegionTileEntity getLinkedTileEntity()
    {
        if (oppositeLocation == null) return null;
        TileEntity tileEntity = worldObj.getTileEntity(oppositeLocation);
        if (!(tileEntity instanceof RegionTileEntity)) return null;
        return (RegionTileEntity) tileEntity;
    }

    public void setLinkedTileEntity(RegionTileEntity newOpposite)
    {
        this.oppositeLocation = newOpposite == null ? null : newOpposite.getPos();
        Logger.info("Setting Region Opposite: %s", this);
        if (worldObj != null && worldObj.isRemote)
        {
            sendUpdate();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        final NBTTagCompound opposite = nbt.getCompoundTag("Opposite");
        if (opposite != null)
        {
            this.oppositeLocation = NBTUtils.readBlockPos(opposite);
        }

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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        Logger.info("writing to NBT: %s", this);
        if (oppositeLocation != null)
        {
            nbt.setTag("Opposite", NBTUtils.writeBlockPos(oppositeLocation));
        }
        if (schematicName != null)
        {
            nbt.setString("SchematicName", schematicName);
        }
        return nbt;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        final NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new SPacketUpdateTileEntity(getPos(), 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }

    public boolean isRenderBlock()
    {
        final RegionTileEntity opposite = getLinkedTileEntity();
        if (opposite == null) return false;
        final BlockPos otherPos = getLinkedLocation();
        final BlockPos thisPos = this.getPos();
        int thisDistance = thisPos.getX() * thisPos.getX() + thisPos.getY() * thisPos.getY() + thisPos.getZ() * thisPos.getZ();
        int otherDistance = otherPos.getX() * otherPos.getX() + otherPos.getY() * otherPos.getY() + otherPos.getZ() * otherPos.getZ();

        //Just doing' it the deterministic way. Doesn't really matter at this point as long as it doesn't flicker.
        if (thisDistance < otherDistance) return true;
        if (Math.abs(thisPos.getX()) < Math.abs(otherPos.getX())) return true;
        if (Math.abs(thisPos.getZ()) < Math.abs(otherPos.getZ())) return true;
        if (Math.abs(thisPos.getY()) < Math.abs(otherPos.getY())) return true;
        return false;
    }

    private void sendUpdate()
    {
        markDirty();
        worldObj.notifyBlockOfStateChange(pos, getBlockType());
        worldObj.addBlockEvent(pos, getBlockType(), 1, isPaired() ? 1 : 0);
        //FIXME: Is this needed?
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("pos", getPos())
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
            getLinkedTileEntity().setSchematicNameInternal(schematicName);
        }

    }

    private void setSchematicNameInternal(String schematicName)
    {
        this.schematicName = schematicName;
        sendUpdate();
    }
}
