package net.binaryvibrance.schematicmetablocks.events;

import net.binaryvibrance.schematicmetablocks.config.Settings;
import net.binaryvibrance.schematicmetablocks.items.MetaToolItem;
import net.binaryvibrance.schematicmetablocks.utility.Localization;
import net.binaryvibrance.schematicmetablocks.utility.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber
public class ItemRegistration
{
    @SubscribeEvent
    public static void onItemRegistration(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(configureItem(new MetaToolItem(), Reference.Items.MetaTool, true));
    }

    private static Item configureItem(Item item, ResourceLocation name, Boolean addToCreativeTab) {
        item.setRegistryName(name)
                .setUnlocalizedName(Localization.getUnlocalizedNameFor(item));

        if (Settings.creatorMode() && addToCreativeTab) {
            item.setCreativeTab(Reference.CreativeTab);
        }

        return item;
    }
}
