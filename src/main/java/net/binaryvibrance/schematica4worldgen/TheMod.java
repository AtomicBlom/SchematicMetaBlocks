package net.binaryvibrance.schematica4worldgen;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import net.binaryvibrance.schematica4worldgen.library.ModBlock;
import net.binaryvibrance.schematica4worldgen.library.ModItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import java.util.Map;

@Mod(modid = TheMod.MOD_ID, name = TheMod.MOD_NAME, version = TheMod.MOD_VERSION)
public class TheMod
{
	public static final String MOD_ID = "schematica4worldgen";
	public static final String MOD_NAME = "Schematica WorldGen Extensions";
	public static final String MOD_VERSION = "@MOD_VERSION@";
	public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ':';

	@Mod.Instance(TheMod.MOD_ID)
	public static TheMod instance;

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
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {

	}

	@NetworkCheckHandler
	public boolean checkRemoteVersions(Map<String, String> versions, Side side) {
		return true;
	}
}

