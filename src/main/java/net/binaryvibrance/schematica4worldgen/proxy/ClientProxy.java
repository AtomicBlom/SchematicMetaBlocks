package net.binaryvibrance.schematica4worldgen.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int insideMetadataBlockRendererId;
    public static int renderPass;

    @Override
    public void setCustomRenderers()
    {
        insideMetadataBlockRendererId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new InsideMetadataBlockRenderer());
    }

}
