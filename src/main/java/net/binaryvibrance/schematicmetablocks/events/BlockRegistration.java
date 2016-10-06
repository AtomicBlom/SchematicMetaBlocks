package net.binaryvibrance.schematicmetablocks.events;

import com.google.common.collect.Queues;
import net.binaryvibrance.schematicmetablocks.blocks.*;
import net.binaryvibrance.schematicmetablocks.config.Settings;
import net.binaryvibrance.schematicmetablocks.utility.Localization;
import net.binaryvibrance.schematicmetablocks.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opencl.CL;

import java.util.Queue;

@Mod.EventBusSubscriber
public class BlockRegistration
{
    @SubscribeEvent
    public static void onBlockRegistration(RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();

        registerBlockAndItem(registry, new ExplicitAirBlock(), Reference.Blocks.ExplicitAir);
        registerBlockAndItem(registry, new ImplicitAirBlock(), Reference.Blocks.ImplicitAir, false);
        registerBlockAndItem(registry, new InteriorAirMarker(), Reference.Blocks.InteriorAirMarker);
        registerBlockAndItem(registry, new NullBlock(), Reference.Blocks.Null, false);
        registerBlockAndItem(registry, new OriginBlock(), Reference.Blocks.Origin);
        registerBlockAndItem(registry, new RegionBlock(), Reference.Blocks.Region);

        //GameRegistry.registerTileEntity(InteriorAirMarkerTileEntity.class, getTEName(blockInteriorAirMarker.NAME));
        //GameRegistry.registerTileEntity(RegionTileEntity.class, getTEName(blockRegion.NAME));
    }

    private static void registerBlockAndItem(IForgeRegistry<Block> registry, Block block, ResourceLocation registryName) {
        registerBlockAndItem(registry, block, registryName, true);
    }

    private static void registerBlockAndItem(IForgeRegistry<Block> registry, Block block, ResourceLocation registryName, Boolean addToCreativeTab)
    {
        registry.register(configureBlock(block, registryName, addToCreativeTab));
        ItemRegistration.addFutureItemRegistration(configureItemBlock(new ItemBlock(block), addToCreativeTab));
        //GameRegistry.register(configureBlock(block, registryName, addToCreativeTab));
        //GameRegistry.register(configureItemBlock(new ItemBlock(block)));
    }

    private static Block configureBlock(Block block, ResourceLocation name, Boolean addToCreativeTab) {
        block.setRegistryName(name)
                .setUnlocalizedName(Localization.getUnlocalizedNameFor(block));

        if (Settings.creatorMode() && addToCreativeTab) {
            block.setCreativeTab(Reference.CreativeTab);
        }

        return block;
    }

    private static Item configureItemBlock(ItemBlock itemBlock, Boolean addToCreativeTab)
    {
        itemBlock
                .setRegistryName(itemBlock.block.getRegistryName())
                .setUnlocalizedName(itemBlock.block.getUnlocalizedName());

        if (Settings.creatorMode() && addToCreativeTab) {
            itemBlock.setCreativeTab(Reference.CreativeTab);
        }

        return itemBlock;
    }
}
