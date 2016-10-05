package net.binaryvibrance.schematicmetablocks.utility;

import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public final class Reference
{
    public static CreativeTabs CreativeTab = new CreativeTabs(Reference.MOD_ID)
    {
        @Override
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(ModBlock.blockExplicitAir);
        }
    };

    public static final String MOD_ID = "schematicmetablocks";
    public static final String MOD_NAME = "Schematic Meta-Blocks";
    public static final String MOD_VERSION = "@MOD_VERSION@";

    public static final class SettingsHelp {
        public static final String CreatorMode = "Turning off Creator mode disables all the blocks and hides the creative tab";

        private SettingsHelp() {}
    }

    public static final class Blocks {
        //public static final String NORMAL_VARIANT = "normal";

        public static final ResourceLocation ExplicitAir = new ResourceLocation(MOD_ID, "blockExplicitAir");
        public static final ResourceLocation ImplicitAir = new ResourceLocation(MOD_ID, "blockImplicitAir");
        public static final ResourceLocation InteriorAirMarker = new ResourceLocation(MOD_ID, "blockInteriorAirMarker");
        public static final ResourceLocation Null = new ResourceLocation(MOD_ID, "blockNull");
        public static final ResourceLocation Origin = new ResourceLocation(MOD_ID, "blockOrigin");
        public static final ResourceLocation Region = new ResourceLocation(MOD_ID, "blockRegion");

        private Blocks() {}
    }

    public static final class Items {
        public static final ResourceLocation MetaTool = new ResourceLocation(MOD_ID, "itemMetaTool");

        private Items() {}
    }

    private Reference() {}
}
