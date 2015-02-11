package net.binaryvibrance.schematicmetablocks.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.binaryvibrance.schematicmetablocks.client.renderer.InvertedMetaBlockRenderer;
import net.binaryvibrance.schematicmetablocks.client.renderer.OriginBlockRenderer;
import net.binaryvibrance.schematicmetablocks.client.renderer.RegionBlockRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

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
}
