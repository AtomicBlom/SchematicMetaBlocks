package net.binaryvibrance.schematicmetablocks.proxy;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.io.IOException;

public class CommonProxy
{
    public void setCustomRenderers()
    {

    }

    public File getDataDirectory() {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        final File file = server.getFile(".");
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            Logger.info("Could not canonize path!", e);
        }
        return file;
    }
}
