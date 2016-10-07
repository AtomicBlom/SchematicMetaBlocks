package net.binaryvibrance.schematicmetablocks.eventhandler;

import com.github.lunatrius.schematica.api.ISchematic;
import com.github.lunatrius.schematica.api.event.DuplicateMappingException;
import com.github.lunatrius.schematica.api.event.PostSchematicCaptureEvent;
import com.github.lunatrius.schematica.api.event.PreSchematicSaveEvent;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.blocks.*;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.library.Mods;
import net.binaryvibrance.schematicmetablocks.utility.NBTUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SchematicSaveListener
{
    private static SchematicContext context;

    private SchematicSaveListener()
    {

    }

    @SubscribeEvent
    @Optional.Method(modid = Mods.Schematica)
    public static void OnSchematicCaptured(PostSchematicCaptureEvent event)
    {

        Logger.info("Schematic captured, changing weather pattern.");
        context = new SchematicContext();
        ISchematic schematic = event.schematic;
        int width = schematic.getWidth();
        int height = schematic.getHeight();
        int length = schematic.getLength();

        int interiorAir = 0;
        int exteriorAir = 0;

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int z = 0; z < length; ++z)
        {
            for (int x = 0; x < width; ++x)
            {
                for (int y = 0; y < height; ++y)
                {
                    mutableBlockPos.setPos(x, y, z);
                    final Block block = schematic.getBlockState(mutableBlockPos).getBlock();
                    if (block instanceof ExplicitAirBlock)
                    {
                        schematic.setBlockState(mutableBlockPos, Blocks.AIR.getDefaultState());
                        interiorAir++;
                        //TODO: Add Explicit Air to map of
                    } else if (block instanceof ImplicitAirBlock)
                    {
                        schematic.setBlockState(mutableBlockPos, Blocks.AIR.getDefaultState());
                        interiorAir++;
                    } else if (block instanceof InteriorAirMarker)
                    {
                        schematic.setBlockState(mutableBlockPos, Blocks.AIR.getDefaultState());
                        schematic.removeTileEntity(mutableBlockPos);
                        interiorAir++;
                    } else if (block instanceof BlockAir)
                    {
                        schematic.setBlockState(mutableBlockPos, ModBlock.blockNull.getDefaultState());
                        exteriorAir++;
                    } else if (block instanceof OriginBlock)
                    {
                        schematic.setBlockState(mutableBlockPos, Blocks.AIR.getDefaultState());
                        interiorAir++;
                        context.origin = new BlockPos(x, y, z);
                    }
                }
            }
        }

        Logger.info("Altered %d interior air blocks, and %d exterior air blocks", interiorAir, exteriorAir);
    }

    @SubscribeEvent
    @Optional.Method(modid = Mods.Schematica)
    public static void OnSchematicSaving(PreSchematicSaveEvent event)
    {

        final NBTTagCompound extendedMetadata = event.extendedMetadata;
        extendedMetadata.setString("SchematicName", "I dunno, I don't care");
        context.writeTo(extendedMetadata);

        final String nullBlockIdentifier = ModBlock.blockNull.getRegistryName().toString();
        try
        {
            event.replaceMapping(nullBlockIdentifier, "null");
        } catch (DuplicateMappingException ex)
        {
            Logger.warning("Got a duplicate mapping, something must not have been replaced correctly.");
        }
    }

    static class SchematicContext
    {
        BlockPos origin;

        public void writeTo(NBTTagCompound extendedMetadata)
        {
            if (origin != null)
            {
                extendedMetadata.setTag("Origin", NBTUtils.writeBlockPos(origin));
            }
        }
    }
}
