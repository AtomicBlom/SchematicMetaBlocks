package net.binaryvibrance.schematicmetablocks.proxy;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.minecraft.client.Minecraft;
import java.io.File;
import java.io.IOException;

public class ClientProxy extends CommonProxy
{
    @Override
    public File getDataDirectory() {
        final File file = Minecraft.getMinecraft().mcDataDir;
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            Logger.info("Could not canonize path!", e);
        }
        return file;
    }
}
