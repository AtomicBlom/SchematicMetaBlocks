package net.binaryvibrance.schematicmetablocks.events;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.binaryvibrance.schematicmetablocks.config.Settings;
import net.binaryvibrance.schematicmetablocks.items.MetaToolItem;
import net.binaryvibrance.schematicmetablocks.utility.Localization;
import net.binaryvibrance.schematicmetablocks.utility.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Queue;

@Mod.EventBusSubscriber
public class ItemRegistration
{
    private static final Queue<Item> deferredItemRegistration = Queues.newArrayDeque();
    private static final List<Item> itemRenderingList = Lists.newArrayList();
    static void addFutureItemRegistration(Item item) {
        deferredItemRegistration.add(item);
    }

    @SubscribeEvent
    public static void onItemRegistration(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        registerItem(registry, configureItem(new MetaToolItem(), Reference.Items.MetaTool, true));

        deferredItemRegistration.forEach((value) -> registerItem(registry, value));
        //GameRegistry.register(configureItem(new MetaToolItem(), Reference.Items.MetaTool, true));
    }

    private static void registerItem(IForgeRegistry<Item> registry, Item item) {
        itemRenderingList.add(item);
        registry.register(item);
    }

    private static Item configureItem(Item item, ResourceLocation name, Boolean addToCreativeTab) {
        item.setRegistryName(name)
                .setUnlocalizedName(Localization.getUnlocalizedNameFor(item));

        if (Settings.creatorMode() && addToCreativeTab) {
            item.setCreativeTab(Reference.CreativeTab);
        }

        return item;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerRendering(RegisterRendering event) {
        itemRenderingList.forEach(item -> ModelLoader.setCustomModelResourceLocation(
            item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory")
        ));
        itemRenderingList.clear();
    }
}
