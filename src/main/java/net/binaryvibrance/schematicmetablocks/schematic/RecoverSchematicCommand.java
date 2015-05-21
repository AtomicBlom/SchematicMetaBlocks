package net.binaryvibrance.schematicmetablocks.schematic;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
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

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        try
        {
            if (sender instanceof EntityPlayerMP)
            {
                if (args.length < 1)
                {
                    throw new CommandException("Could not execute command");
                }
                String filename = args[0];

                final List<WorldBlockCoord> blocksToExamine = new LinkedList<WorldBlockCoord>();

                final EntityPlayerMP player = (EntityPlayerMP) sender;

                SchematicLoader loader = new SchematicLoader();

                WorldBlockCoord playerPosition = new WorldBlockCoord((int)player.posX, (int)player.posY, (int)player.posZ);

                loader.addSetBlockEventListener(new SchematicLoader.IPreSetBlockEventListener()
                {
                    @Override
                    public void preBlockSet(SchematicLoader.PreSetBlockEvent event)
                    {
                        if (event.getBlock() == Blocks.air)
                        {
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
                            blocksToExamine.add(new WorldBlockCoord(event.worldCoord.getX(), event.worldCoord.getY(), event.worldCoord.getZ()));

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

                ResourceLocation schematicLocation = loader.loadSchematic(new File(Minecraft.getMinecraft().mcDataDir, "/Schematics/" + filename + ".schematic"));
                final SchematicLoader.ISchematicMetadata metadata = loader.getSchematicMetadata(schematicLocation);

                final World world = player.getEntityWorld();
                loader.renderSchematicInOneShot(schematicLocation, world, (int) player.posX, (int) player.posY, (int) player.posZ, ForgeDirection.NORTH, false);
                WorldBlockCoord originBlockCoord = null;
                final NBTTagCompound extendedMetadata = metadata.getExtendedMetadata();
                if (extendedMetadata.hasKey("Origin")) {
                    NBTTagCompound originNBT = extendedMetadata.getCompoundTag("Origin");
                    originBlockCoord = WorldBlockCoord.fromNBT(originNBT).offset(playerPosition);

                    originBlockCoord.setBlock(world, ModBlock.blockOrigin);
                }

                while (!blocksToExamine.isEmpty())
                {
                    Stack<WorldBlockCoord> contiguousBlocks = new Stack<WorldBlockCoord>();
                    List<WorldBlockCoord> blocksInGroup = new LinkedList<WorldBlockCoord>();
                    WorldBlockCoord blockToExamine = blocksToExamine.get(0);

                    contiguousBlocks.push(blockToExamine);
                    blocksInGroup.add(blockToExamine);

                    WorldBlockCoord candidateAirMarker = null;

                    while (!contiguousBlocks.empty())
                    {
                        blockToExamine = contiguousBlocks.pop();
                        if (blockToExamine.equals(originBlockCoord)) {
                            continue;
                        }

                        for (final ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                        {
                            WorldBlockCoord neighbourBlockToExamine = blockToExamine.offset(direction);
                            Block b = neighbourBlockToExamine.getBlock(world);

                            if (b == ModBlock.blockImplicitAir && neighbourBlockToExamine.chunkX == blockToExamine.chunkX && neighbourBlockToExamine.chunkZ == blockToExamine.chunkZ)
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
                            Block blockBelow = blockToExamine.offset(0, -1, 0).getBlock(world);
                            Block block = blockToExamine.getBlock(world);
                            if (block == Blocks.air && blockBelow != Blocks.air && blockBelow != ModBlock.blockExplicitAir && blockBelow != ModBlock.blockImplicitAir)
                            {
                                candidateAirMarker = blockToExamine;
                            }
                        }
                    }

                    if (candidateAirMarker == null && !blocksInGroup.get(0).equals(originBlockCoord))
                    {
                        candidateAirMarker = blocksInGroup.get(0);
                    }

                    blocksToExamine.removeAll(blocksInGroup);
                    if (candidateAirMarker != null)
                    {
                        candidateAirMarker.setBlock(world, ModBlock.blockInteriorAirMarker);
                    }
                }

                final WorldBlockCoord cornerA = playerPosition.offset(-1, -1, -1);
                final WorldBlockCoord cornerB = playerPosition.offset(metadata.getWidth(), metadata.getHeight(), metadata.getLength());
                cornerA.setBlock(world, ModBlock.blockRegion);
                cornerB.setBlock(world, ModBlock.blockRegion);

                RegionTileEntity regionTileEntityA = RegionTileEntity.tryGetTileEntity(world, cornerA.x, cornerA.y, cornerA.z);
                RegionTileEntity regionTileEntityB = RegionTileEntity.tryGetTileEntity(world, cornerB.x, cornerB.y, cornerB.z);
                regionTileEntityA.setLinkedTileEntity(regionTileEntityB);
                regionTileEntityB.setLinkedTileEntity(regionTileEntityA);
                regionTileEntityA.setSchematicName(filename);
                regionTileEntityB.setSchematicName(filename);

                sender.addChatMessage(new ChatComponentText("Rendered schematic " + filename));
            }
        } catch (Exception e) {
            Logger.info(e.toString());
        }
    }

}
