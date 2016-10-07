package net.binaryvibrance.schematicmetablocks.schematic;

import net.binaryvibrance.schematicmetablocks.TheMod;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import java.io.File;

public class LoadSchematicCommand extends CommandBase
{

    @Override
    public String getCommandName()
    {
        return "smbLoadSchematic";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!(sender instanceof CommandBlockBaseLogic) && !sender.canCommandSenderUseCommand(3, this.getCommandName())) {
            throw new CommandException("Could not execute command: Permission Denied");
        }

        if (args.length < 1) {
            throw new CommandException("Could not execute command");
        }

        String filename = args[0];
        double x = sender.getPosition().getX();
        double y = sender.getPosition().getY();
        double z = sender.getPosition().getZ();
        if (args.length > 3) {
            x = parseCoordinate(x, args[1], false).getResult();
            y = parseCoordinate(y, args[2], false).getResult();
            z = parseCoordinate(z, args[3], false).getResult();
        }

        final SchematicLoader loader = new SchematicLoader();

        if (!filename.endsWith(".schematic")) {
            filename += ".schematic";
        }

        final ResourceLocation schematicLocation = loader.loadSchematic(new File(TheMod.proxy.getDataDirectory(), "/Schematics/" + filename));
        loader.renderSchematicInOneShot(schematicLocation, sender.getEntityWorld(), new BlockPos((int) x, (int) y, (int) z), EnumFacing.NORTH, false);
        sender.addChatMessage(new TextComponentString("Rendered schematic " + filename));
    }
}
