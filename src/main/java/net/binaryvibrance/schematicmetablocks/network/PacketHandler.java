package net.binaryvibrance.schematicmetablocks.network;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.TheMod;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import java.io.IOException;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<FMLProxyPacket>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket packet) throws Exception
    {
        if (packet.channel().equals(TheMod.RESOURCE_PREFIX + "Network"))
        {
            ByteBuf payload = packet.payload();
            final PacketBuffer packetBuffer = new PacketBuffer(payload);
            final PacketType[] values = PacketType.values();
            final int packetTypeValue = packetBuffer.readInt();
            if (packetTypeValue >= values.length)
            {
                Logger.warning("Unknown Packet type: %d", packetTypeValue);
                return;
            }

            PacketType packetType = values[packetTypeValue];
            switch (packetType)
            {
                case SET_SCHEMATIC_NAME:
                    setSchematicName(packetBuffer);
                    break;
            }
        }
    }

    private void setSchematicName(PacketBuffer packetBuffer) throws IOException
    {
        int x = packetBuffer.readInt();
        int y = packetBuffer.readInt();
        int z = packetBuffer.readInt();
        int dimension = packetBuffer.readInt();
        String newName = packetBuffer.readStringFromBuffer(128);

        final WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dimension);
        RegionTileEntity te = RegionTileEntity.tryGetTileEntity(worldServer, x, y, z);
        if (te != null)
        {
            te.setSchematicName(newName);
        }

    }
}