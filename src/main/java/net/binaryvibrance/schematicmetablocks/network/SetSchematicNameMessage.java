package net.binaryvibrance.schematicmetablocks.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SetSchematicNameMessage implements IMessage
{
    private int dimensionId;
    private BlockPos pos;
    private String schematicName;

    public SetSchematicNameMessage(int dimensionId, BlockPos pos, String schematicName) {

        this.dimensionId = dimensionId;
        this.pos = pos;
        this.schematicName = schematicName;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        dimensionId = buf.readInt();
        pos = new BlockPos(
                buf.readInt(),
                buf.readInt(),
                buf.readInt()
        );
        schematicName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(dimensionId);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        ByteBufUtils.writeUTF8String(buf, schematicName);
    }

    public int getDimensionId()
    {
        return dimensionId;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public String getSchematicName()
    {
        return schematicName;
    }
}
