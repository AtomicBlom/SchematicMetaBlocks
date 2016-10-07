package net.binaryvibrance.schematicmetablocks;

import net.binaryvibrance.schematicmetablocks.config.Settings;
import net.binaryvibrance.schematicmetablocks.eventhandler.SchematicSaveListener;
import net.binaryvibrance.schematicmetablocks.eventhandler.WorldListener;
import net.binaryvibrance.schematicmetablocks.events.*;
import net.binaryvibrance.schematicmetablocks.gui.GuiHandler;
import net.binaryvibrance.schematicmetablocks.jobs.JobProcessor;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.network.SetSchematicNameMessage;
import net.binaryvibrance.schematicmetablocks.network.SetSchematicNameMessageHandler;
import net.binaryvibrance.schematicmetablocks.proxy.CommonProxy;
import net.binaryvibrance.schematicmetablocks.schematic.LoadSchematicCommand;
import net.binaryvibrance.schematicmetablocks.schematic.RecoverSchematicCommand;
import net.binaryvibrance.schematicmetablocks.utility.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class TheMod
{
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
    @Mod.Instance(Reference.MOD_ID)
    public static TheMod instance;

    @SidedProxy(clientSide = "net.binaryvibrance.schematicmetablocks.proxy.ClientProxy", serverSide = "net.binaryvibrance.schematicmetablocks.proxy.CommonProxy")
    public static CommonProxy proxy;
    //public static Configuration configFile;
    //public static boolean creatorMode = true;

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event)
    {
        CHANNEL.registerMessage(SetSchematicNameMessageHandler.class, SetSchematicNameMessage.class, 0, Side.SERVER);

        Settings.syncConfig(new Configuration(event.getSuggestedConfigurationFile()));
        //syncConfig();
        //ModBlock.init();
        //ModItem.init();

        //BlockRegistration.onBlockRegister(null);
        //ItemRegistration.onItemRegistration(null);
        MinecraftForge.EVENT_BUS.post(new RegisterRenderingEvent());
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
        event.registerServerCommand(new LoadSchematicCommand());
        event.registerServerCommand(new RecoverSchematicCommand());
    }

    @NetworkCheckHandler
    public boolean checkRemoteVersions(Map<String, String> versions, Side side)
    {
        return true;
    }

    /*public static void syncConfig() {
        creatorMode = configFile.getBoolean("creatorMode", Configuration.CATEGORY_GENERAL, creatorMode, "Turning off Creator mode disables all the blocks and hides the creative tab");

        if(configFile.hasChanged())
            configFile.save();
    }*/
}

