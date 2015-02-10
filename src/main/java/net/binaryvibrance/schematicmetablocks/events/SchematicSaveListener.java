package net.binaryvibrance.schematicmetablocks.events;

import com.github.lunatrius.schematica.api.ISchematic;
import com.github.lunatrius.schematica.api.event.DuplicateMappingException;
import com.github.lunatrius.schematica.api.event.PostSchematicCaptureEvent;
import com.github.lunatrius.schematica.api.event.PreSchematicSaveEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.blocks.*;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.schematic.WorldBlockCoord;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

public class SchematicSaveListener
{
    public static final SchematicSaveListener Instance = new SchematicSaveListener();

    private SchematicSaveListener() {

    }

    class SchematicContext {
        WorldBlockCoord origin;

        public void writeTo(NBTTagCompound extendedMetadata)
        {
            if (origin != null) {
                extendedMetadata.setTag("Origin", origin.toNBT());
            }
        }
    }

    private SchematicContext context;

    @SubscribeEvent
    public void OnSchematicCaptured(PostSchematicCaptureEvent event) {

        Logger.info("Schematic captured, changing weather pattern.");
        context = new SchematicContext();
        ISchematic schematic = event.schematic;
        int width = schematic.getWidth();
        int height = schematic.getHeight();
        int length = schematic.getLength();

        int interiorAir = 0;
        int exteriorAir = 0;

        for (int z = 0; z < length; ++z) {
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    final Block block = schematic.getBlock(x, y, z);
                    if (block instanceof ExplicitAirBlock) {
                        schematic.setBlock(x, y, z, Blocks.air, 0);
                        interiorAir++;
                        //TODO: Add Explicit Air to map of
                    } else if (block instanceof ImplicitAirBlock)
                    {
                        schematic.setBlock(x, y, z, Blocks.air, 0);
                        interiorAir++;
                    } else if (block instanceof InteriorAirMarker) {
                        schematic.setBlock(x, y, z, Blocks.air, 0);
                        schematic.removeTileEntity(x, y, z);
                        interiorAir++;
                    } else if (block instanceof BlockAir) {
                        schematic.setBlock(x, y, z, ModBlock.blockNull, 0);
                        exteriorAir++;
                    } else if (block instanceof OriginBlock) {
                        schematic.setBlock(x, y, z, Blocks.air, 0);
                        interiorAir++;
                        context.origin = new WorldBlockCoord(x, y, z);
                    }
                }
            }
        }

        Logger.info("Altered %d interior air blocks, and %d exterior air blocks", interiorAir, exteriorAir);
    }


    @SubscribeEvent
    public void OnSchematicSaving(PreSchematicSaveEvent event) {

        final NBTTagCompound extendedMetadata = event.extendedMetadata;
        extendedMetadata.setString("SchematicName", "I dunno, I don't care");
        context.writeTo(extendedMetadata);

        final String nullBlockIdentifier = MetaBlock.getUnwrappedUnlocalizedName(ModBlock.blockNull.getUnlocalizedName());
        try
        {
            event.replaceMapping(nullBlockIdentifier, "null");
        } catch (DuplicateMappingException ex) {
            Logger.warning("Got a duplicate mapping, something must not have been replaced correctly.");
        }
    }
}
