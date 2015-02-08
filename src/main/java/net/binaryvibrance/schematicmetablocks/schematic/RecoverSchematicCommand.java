package net.binaryvibrance.schematicmetablocks.schematic;

import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class RecoverSchematicCommand extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "smbRecoverSchematic";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "smbRecoverSchematic [SchematicName]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {

        if (sender instanceof EntityPlayerMP)
        {
            if (args.length < 1)
            {
                throw new CommandException("Could not execute command");
            }
            String filename = args[0];

            final List<BlockToExamine> blocksToExamine = new LinkedList<BlockToExamine>();

            final EntityPlayerMP player = (EntityPlayerMP) sender;
            SchematicLoader loader = new SchematicLoader();
            loader.addSetBlockEventListener(new SchematicLoader.IPreSetBlockEventListener()
            {
                @Override
                public void preBlockSet(SchematicLoader.PreSetBlockEvent event)
                {
                    if (event.getBlock() == Blocks.air)
                    {
                        SchematicLoader.SchematicWorld world = event.schematic;
                        for (final ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                        {
                            Block schematicBlock = event.schematic.getBlock(
                                    event.schematicCoord.getX() + direction.offsetX,
                                    event.schematicCoord.getY() + direction.offsetY,
                                    event.schematicCoord.getZ() + direction.offsetZ
                            );
                            if (schematicBlock == null || schematicBlock == ModBlock.blockNull)
                            {
                                event.replaceBlock(ModBlock.blockExplicitAir, 0);
                                return;
                            }
                        }
                        event.replaceBlock(ModBlock.blockImplicitAir, 0);
                        blocksToExamine.add(new BlockToExamine(event.worldCoord.getX(), event.worldCoord.getY(), event.worldCoord.getZ()));

                    } else if (event.getBlock() == ModBlock.blockNull)
                    {
                        event.replaceBlock(Blocks.air, 0);
                    }
                }
            });
            loader.addUnknownBlockEventListener(new SchematicLoader.IUnknownBlockEventListener()
            {
                @Override
                public void unknownBlock(SchematicLoader.UnknownBlockEvent event)
                {
                    if ("null".equals(event.name))
                    {
                        event.remap(ModBlock.blockNull);
                    }
                }
            });

            ResourceLocation schematicLocation = loader.loadSchematic(new File(Minecraft.getMinecraft().mcDataDir, "\\Schematics\\" + filename + ".schematic"));

            final World world = player.getEntityWorld();
            loader.renderSchematicInOneShot(schematicLocation, world, (int) player.posX, (int) player.posY, (int) player.posZ, ForgeDirection.NORTH, false);

            while (!blocksToExamine.isEmpty())
            {
                Stack<BlockToExamine> contiguousBlocks = new Stack<BlockToExamine>();
                List<BlockToExamine> blocksInGroup = new LinkedList<BlockToExamine>();
                BlockToExamine blockToExamine = blocksToExamine.get(0);
                contiguousBlocks.push(blockToExamine);
                blocksInGroup.add(blockToExamine);

                BlockToExamine candidateAirMarker = null;

                while (!contiguousBlocks.empty())
                {
                    blockToExamine = contiguousBlocks.pop();

                    for (final ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                    {
                        int blockX = blockToExamine.x + direction.offsetX;
                        int blockY = blockToExamine.y + direction.offsetY;
                        int blockZ = blockToExamine.z + direction.offsetZ;

                        BlockToExamine neighbourBlockToExamine = new BlockToExamine(blockX, blockY, blockZ);
                        Block b = world.getBlock(blockX, blockY, blockZ);

                        if (b == ModBlock.blockImplicitAir && blockX >> 4 == blockToExamine.x >> 4 && blockZ >> 4 == blockToExamine.z >> 4)
                        {
                            if (blocksToExamine.contains(neighbourBlockToExamine) && !blocksInGroup.contains(neighbourBlockToExamine))
                            {
                                contiguousBlocks.push(neighbourBlockToExamine);
                                blocksInGroup.add(neighbourBlockToExamine);
                            }
                        }
                    }

                    if (candidateAirMarker == null & blockToExamine.y > 0)
                    {
                        Block b = world.getBlock(blockToExamine.x, blockToExamine.y - 1, blockToExamine.z);
                        if (b != Blocks.air && b != ModBlock.blockExplicitAir && b != ModBlock.blockImplicitAir)
                        {
                            candidateAirMarker = blockToExamine;
                        }
                    }
                }

                if (candidateAirMarker == null)
                {
                    candidateAirMarker = blocksInGroup.get(0);
                }

                blocksToExamine.removeAll(blocksInGroup);
                world.setBlock(candidateAirMarker.x, candidateAirMarker.y, candidateAirMarker.z, ModBlock.blockInteriorAirMarker);
            }

            sender.addChatMessage(new ChatComponentText("Rendered schematic " + filename));
        }
    }

    private class BlockToExamine
    {
        private final int x;
        private final int y;
        private final int z;

        public BlockToExamine(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final BlockToExamine that = (BlockToExamine) o;

            if (x != that.x) return false;
            if (y != that.y) return false;
            if (z != that.z) return false;

            return true;
        }

        @Override
        public int hashCode()
        {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }
}
