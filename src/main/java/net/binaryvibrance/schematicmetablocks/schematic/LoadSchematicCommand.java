package net.binaryvibrance.schematicmetablocks.schematic;

import akka.io.Tcp;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.Side;
import net.binaryvibrance.schematicmetablocks.TheMod;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import java.io.File;

public class LoadSchematicCommand extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "smbLoadSchematic";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "/smbLoadSchematic <schematic> [x] [y] [z]";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (!(sender instanceof CommandBlockLogic) && !sender.canCommandSenderUseCommand(3, this.getCommandName())) {
            throw new CommandException("Could not execute command: Permission Denied");
        }

        if (args.length < 1) {
            throw new CommandException("Could not execute command");
        }

        String filename = args[0];
        double x = sender.getPlayerCoordinates().posX;
        double y = sender.getPlayerCoordinates().posY;
        double z = sender.getPlayerCoordinates().posZ;
        if (args.length > 3) {
            x = func_110666_a(sender, x, args[1]);
            y = func_110666_a(sender, y, args[2]);
            z = func_110666_a(sender, z, args[3]);
        }

        SchematicLoader loader = new SchematicLoader();

        if (!filename.endsWith(".schematic")) {
            filename += ".schematic";
        }

        ResourceLocation schematicLocation = loader.loadSchematic(new File(TheMod.proxy.getDataDirectory(), "/Schematics/" + filename));
        loader.renderSchematicInOneShot(schematicLocation, sender.getEntityWorld(), (int) x, (int) y, (int) z, ForgeDirection.NORTH, false);
        sender.addChatMessage(new ChatComponentText("Rendered schematic " + filename));
    }
}
