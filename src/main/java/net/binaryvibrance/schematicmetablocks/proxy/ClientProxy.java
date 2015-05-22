package net.binaryvibrance.schematicmetablocks.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.client.renderer.InvertedMetaBlockRenderer;
import net.binaryvibrance.schematicmetablocks.client.renderer.OriginBlockRenderer;
import net.binaryvibrance.schematicmetablocks.client.renderer.RegionBlockRenderer;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;

public class ClientProxy extends CommonProxy
{
    public static int insideMetadataBlockRendererId;
    public static int renderPass;
    public static int originBlockRendererId;
    public static int regionBlockRendererId;

    @Override
    public void setCustomRenderers()
    {
        insideMetadataBlockRendererId = RenderingRegistry.getNextAvailableRenderId();
        originBlockRendererId = RenderingRegistry.getNextAvailableRenderId();
        regionBlockRendererId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new InvertedMetaBlockRenderer());
        RenderingRegistry.registerBlockHandler(new OriginBlockRenderer());
        RenderingRegistry.registerBlockHandler(new RegionBlockRenderer());
    }

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
