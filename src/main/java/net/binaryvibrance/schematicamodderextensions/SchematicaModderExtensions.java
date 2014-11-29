package net.binaryvibrance.schematicamodderextensions;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.Map;

@Mod(modid = SchematicaModderExtensions.MOD_ID, name = SchematicaModderExtensions.MOD_NAME, version = SchematicaModderExtensions.MOD_VERSION)
public class SchematicaModderExtensions {
	public static final String MOD_ID = "schematicamodderextensions";
	public static final String MOD_NAME = "Schematica Modder Extensions";
	public static final String MOD_VERSION = "@MOD_VERSION@";

	@Mod.Instance(SchematicaModderExtensions.MOD_ID)
	public static SchematicaModderExtensions instance;

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {

	}

	@NetworkCheckHandler
	public boolean checkRemoteVersions(Map<String, String> versions, Side side) {
		return true;
	}
}

