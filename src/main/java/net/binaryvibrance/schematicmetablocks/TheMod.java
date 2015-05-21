package net.binaryvibrance.schematicmetablocks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.binaryvibrance.schematicmetablocks.events.SchematicSaveListener;
import net.binaryvibrance.schematicmetablocks.events.WorldListener;
import net.binaryvibrance.schematicmetablocks.gui.GuiHandler;
import net.binaryvibrance.schematicmetablocks.jobs.JobProcessor;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.library.ModItem;
import net.binaryvibrance.schematicmetablocks.network.PacketHandler;
import net.binaryvibrance.schematicmetablocks.proxy.CommonProxy;
import net.binaryvibrance.schematicmetablocks.schematic.LoadSchematicCommand;
import net.binaryvibrance.schematicmetablocks.schematic.RecoverSchematicCommand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.util.Map;

@Mod(modid = TheMod.MOD_ID, name = TheMod.MOD_NAME, version = TheMod.MOD_VERSION)
public class TheMod
{
    public static final String MOD_ID = "schematicmetablocks";
    public static final String MOD_NAME = "Schematic Meta-Blocks";
    public static final String MOD_VERSION = "@MOD_VERSION@";
    public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ':';
    @SuppressWarnings("AnonymousInnerClass")
    public static CreativeTabs CREATIVE_TAB;
    @Mod.Instance(TheMod.MOD_ID)
    public static TheMod instance;
    @SidedProxy(clientSide = "net.binaryvibrance.schematicmetablocks.proxy.ClientProxy", serverSide = "net.binaryvibrance.schematicmetablocks.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static Configuration configFile;
    public static boolean creatorMode = true;

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event)
    {
        configFile = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
        if (creatorMode) {
            CREATIVE_TAB = new CreativeTabs(MOD_ID.toLowerCase())
            {
                @Override
                public Item getTabIconItem()
                {
                    return Item.getItemFromBlock(ModBlock.blockExplicitAir);
                }
            };
        }
        ModBlock.init();
        ModItem.init();
    }

    @SuppressWarnings("UnusedParameters")
    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event)
    {

        ModBlock.registerTileEntities();
        proxy.setCustomRenderers();
        FMLCommonHandler.instance().bus().register(JobProcessor.Instance);
        MinecraftForge.EVENT_BUS.register(JobProcessor.Instance);
        MinecraftForge.EVENT_BUS.register(WorldListener.Instance);
        MinecraftForge.EVENT_BUS.register(SchematicSaveListener.Instance);
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        NetworkRegistry.INSTANCE.newChannel(RESOURCE_PREFIX + "Network", new PacketHandler());
        event.registerServerCommand(new LoadSchematicCommand());
        event.registerServerCommand(new RecoverSchematicCommand());
    }

    @NetworkCheckHandler
    public boolean checkRemoteVersions(Map<String, String> versions, Side side)
    {
        return true;
    }

    public static void syncConfig() {
        creatorMode = configFile.getBoolean("creatorMode", Configuration.CATEGORY_GENERAL, creatorMode, "Turning off Creator mode disables all the blocks and hides the creative tab");

        if(configFile.hasChanged())
            configFile.save();
    }
}

