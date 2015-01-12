package net.binaryvibrance.schematicmetablocks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.library.ModItem;
import net.binaryvibrance.schematicmetablocks.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import java.util.Map;

@Mod(modid = TheMod.MOD_ID, name = TheMod.MOD_NAME, version = TheMod.MOD_VERSION)
public class TheMod
{
	public static final String MOD_ID = "schematicmetablocks";
	public static final String MOD_NAME = "Schematica Meta-Blocks";
	public static final String MOD_VERSION = "@MOD_VERSION@";
	public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ':';

	@Mod.Instance(TheMod.MOD_ID)
	public static TheMod instance;

	@SidedProxy(clientSide="net.binaryvibrance.schematicmetablocks.proxy.ClientProxy", serverSide="net.binaryvibrance.schematicmetablocks.proxy.CommonProxy")
	public static CommonProxy proxy;

	@SuppressWarnings("AnonymousInnerClass")
	public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID.toLowerCase())
	{
		@Override
		public Item getTabIconItem()
		{
			return ModItem.fiddler;
		}
	};

	@Mod.EventHandler
	public void onFMLPreInitialization(FMLPreInitializationEvent event)
	{
		ModBlock.init();
	}

	@SuppressWarnings("UnusedParameters")
	@Mod.EventHandler
	public void onFMLInitialization(FMLInitializationEvent event)
	{

		ModBlock.registerTileEntities();
		proxy.setCustomRenderers();
		MinecraftForge.EVENT_BUS.register(InteriorProcessor.Instance);
		FMLCommonHandler.instance().bus().register(InteriorProcessor.Instance);
		MinecraftForge.EVENT_BUS.register(SchematicSaveListener.Instance);
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {

	}

	@NetworkCheckHandler
	public boolean checkRemoteVersions(Map<String, String> versions, Side side) {
		return true;
	}
}

