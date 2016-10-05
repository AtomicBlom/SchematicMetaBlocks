package net.binaryvibrance.schematicmetablocks.network;

import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetSchematicNameMessageHandler implements IMessageHandler<SetSchematicNameMessage, IMessage>
{
    @Override
    public IMessage onMessage(SetSchematicNameMessage message, MessageContext ctx)
    {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        final WorldServer worldServer = server.worldServerForDimension(message.getDimensionId());
        final RegionTileEntity te = RegionTileEntity.tryGetTileEntity(worldServer, message.getPos());
        if (te != null)
        {
            te.setSchematicName(message.getSchematicName());
        }
        return null;
    }
}
