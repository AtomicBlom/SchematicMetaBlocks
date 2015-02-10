package net.binaryvibrance.schematicmetablocks.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.binaryvibrance.schematicmetablocks.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class OriginBlockRenderer implements ISimpleBlockRenderingHandler
{

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID,
                                     RenderBlocks renderer)
    {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        //which render pass are we doing?
        /*if (ClientProxy.renderPass != 0)
        {
            return false;
        }*/

        double xPos = (double) x;
        double yPos = (double) y;
        double zPos = (double) z;
        IIcon renderIcon;
        //get the tessellator instance
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        //RenderHelper.disableStandardItemLighting();
        //tessellator.setBrightness(240);

        final double e = 0.0002;

        IBlockWithFloor blockWithFloor = (IBlockWithFloor)block;
        renderIcon = blockWithFloor.getFloorIcon();

        renderer.renderStandardBlock(block, x, y, z);

        //GL11.glDisable(GL11.GL_LIGHTING);
        double textureUMin = renderIcon.getMinU();
        double textureUMax = renderIcon.getMaxU();
        double textureVMin = renderIcon.getMinV();
        double textureVMax = renderIcon.getMaxV();
        //tessellator.setBrightness(240);
        tessellator.addVertexWithUV(xPos + 1 + 0.5, yPos + e, zPos - 0.5, textureUMin, textureVMax);
        tessellator.addVertexWithUV(xPos - 0.5, yPos + e, zPos- 0.5, textureUMin, textureVMin);
        tessellator.addVertexWithUV(xPos - 0.5, yPos + e, zPos + 1 + 0.5, textureUMax, textureVMin);
        tessellator.addVertexWithUV(xPos + 1 + 0.5, yPos + e, zPos + 1 + 0.5, textureUMax, textureVMax);
        //GL11.glPolygonOffset(0.0F, 0.0F);
        //GL11.glEnable(GL11.GL_LIGHTING);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.originBlockRendererId;
    }

}
