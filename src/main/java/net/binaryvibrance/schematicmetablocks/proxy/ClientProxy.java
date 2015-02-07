package net.binaryvibrance.schematicmetablocks.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int insideMetadataBlockRendererId;
    public static int renderPass;
    public static int originBlockRendererId;

    @Override
    public void setCustomRenderers()
    {
        insideMetadataBlockRendererId = RenderingRegistry.getNextAvailableRenderId();
        originBlockRendererId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new InvertedMetaBlockRenderer());
        RenderingRegistry.registerBlockHandler(new OriginBlockRenderer());
    }

}
