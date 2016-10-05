package net.binaryvibrance.schematicmetablocks.schematic;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.TheMod;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.binaryvibrance.schematicmetablocks.utility.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
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

                final List<BlockPos> blocksToExamine = new LinkedList<BlockPos>();

                final EntityPlayerMP player = (EntityPlayerMP) sender;

                SchematicLoader loader = new SchematicLoader();

                BlockPos playerPosition = new BlockPos((int)player.posX, (int)player.posY, (int)player.posZ);

                loader.addSetBlockEventListener(new SchematicLoader.IPreSetBlockEventListener()
                {
                    @Override
                    public void preBlockSet(SchematicLoader.PreSetBlockEvent event)
                    {
                        if (event.getBlockState().getBlock() == Blocks.AIR)
                        {
                            for (final EnumFacing direction : EnumFacing.VALUES)
                            {
                                final IBlockState blockState = event.schematic.getBlockState(event.schematicCoord.offset(direction));
                                Block schematicBlock = blockState.getBlock();
                                if (schematicBlock == null || schematicBlock == ModBlock.blockNull)
                                {
                                    event.replaceBlockState(ModBlock.blockExplicitAir.getDefaultState());
                                    return;
                                }
                            }
                            event.replaceBlockState(ModBlock.blockImplicitAir.getDefaultState());
                            blocksToExamine.add(new BlockPos(event.worldCoord.getX(), event.worldCoord.getY(), event.worldCoord.getZ()));

                        } else if (event.getBlockState().getBlock() == ModBlock.blockNull)
                        {
                            event.replaceBlockState(Blocks.AIR.getDefaultState());
                        }
                    }
                });
                loader.addUnknownBlockEventListener(event ->
                {
                    if ("null".equals(event.name))
                    {
                        event.remap(ModBlock.blockNull);
                    }
                });

                if (!filename.endsWith(".schematic")) {
                    filename += ".schematic";
                }

                ResourceLocation schematicLocation = loader.loadSchematic(new File(TheMod.proxy.getDataDirectory(), "/Schematics/" + filename));

                final SchematicLoader.ISchematicMetadata metadata = loader.getSchematicMetadata(schematicLocation);

                final World world = player.getEntityWorld();
                loader.renderSchematicInOneShot(schematicLocation, world, playerPosition, EnumFacing.NORTH, false);
                BlockPos originBlockCoord = null;
                final NBTTagCompound extendedMetadata = metadata.getExtendedMetadata();
                if (extendedMetadata.hasKey("Origin")) {
                    NBTTagCompound originNBT = extendedMetadata.getCompoundTag("Origin");
                    originBlockCoord = NBTUtils.readBlockPos(originNBT).add(playerPosition);

                    world.setBlockState(originBlockCoord, ModBlock.blockOrigin.getDefaultState());
                }

                while (!blocksToExamine.isEmpty())
                {
                    Stack<BlockPos> contiguousBlocks = new Stack<>();
                    List<BlockPos> blocksInGroup = new LinkedList<>();
                    BlockPos blockToExamine = blocksToExamine.get(0);

                    contiguousBlocks.push(blockToExamine);
                    blocksInGroup.add(blockToExamine);

                    BlockPos candidateAirMarker = null;

                    while (!contiguousBlocks.empty())
                    {
                        blockToExamine = contiguousBlocks.pop();
                        if (blockToExamine.equals(originBlockCoord)) {
                            continue;
                        }

                        for (final EnumFacing direction : EnumFacing.VALUES)
                        {
                            BlockPos neighbourBlockToExamine = blockToExamine.offset(direction);
                            final IBlockState blockState = world.getBlockState(neighbourBlockToExamine);
                            Block b = blockState.getBlock();

                            if (b == ModBlock.blockImplicitAir && (neighbourBlockToExamine.getX() >> 4) == (blockToExamine.getX() >> 4) && (neighbourBlockToExamine.getZ() >> 4) == (blockToExamine.getZ() >> 4))
                            {
                                if (blocksToExamine.contains(neighbourBlockToExamine) && !blocksInGroup.contains(neighbourBlockToExamine))
                                {
                                    contiguousBlocks.push(neighbourBlockToExamine);
                                    blocksInGroup.add(neighbourBlockToExamine);
                                }
                            }
                        }

                        if (candidateAirMarker == null & blockToExamine.getY() > 0)
                        {
                            final IBlockState blockStateBelow = world.getBlockState(blockToExamine.down());
                            Block blockBelow = blockStateBelow.getBlock();
                            final IBlockState blockState = world.getBlockState(blockToExamine);
                            Block block = blockState.getBlock();
                            if (block == Blocks.AIR && blockBelow != Blocks.AIR && blockBelow != ModBlock.blockExplicitAir && blockBelow != ModBlock.blockImplicitAir)
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
                        world.setBlockState(candidateAirMarker, ModBlock.blockInteriorAirMarker.getDefaultState());
                    }
                }

                final BlockPos cornerA = playerPosition.add(-1, -1, -1);
                final BlockPos cornerB = playerPosition.add(metadata.getWidth(), metadata.getHeight(), metadata.getLength());
                world.setBlockState(cornerA, ModBlock.blockRegion.getDefaultState());
                world.setBlockState(cornerB, ModBlock.blockRegion.getDefaultState());

                RegionTileEntity regionTileEntityA = RegionTileEntity.tryGetTileEntity(world, cornerA);
                RegionTileEntity regionTileEntityB = RegionTileEntity.tryGetTileEntity(world, cornerB);
                regionTileEntityA.setLinkedTileEntity(regionTileEntityB);
                regionTileEntityB.setLinkedTileEntity(regionTileEntityA);
                regionTileEntityA.setSchematicName(filename);
                regionTileEntityB.setSchematicName(filename);

                sender.addChatMessage(new TextComponentString("Rendered schematic " + filename));
            }
        } catch (Exception e) {
            Logger.info(e.toString());
        }
    }

}
