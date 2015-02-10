package net.binaryvibrance.schematicmetablocks.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.binaryvibrance.schematicmetablocks.blocks.InteriorAirMarker;
import net.binaryvibrance.schematicmetablocks.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class InvertedMetaBlockRenderer implements ISimpleBlockRenderingHandler
{

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID,
                                     RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        renderThing(0, 0, 0, tessellator);

        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        //which render pass are we doing?
        if (ClientProxy.renderPass == 0)
        {
            return false;
        }

        double xPos = (double) x;
        double yPos = (double) y;
        double zPos = (double) z;
        IIcon renderIcon;
        //get the tessellator instance
        Tessellator tessellator = Tessellator.instance;

        if (block instanceof InteriorAirMarker) {
            renderThing(xPos, yPos, zPos, tessellator);
        }

        tessellator.setBrightness(240);

        final double e = 0.0001;

        if ((renderIcon = block.getIcon(world, x, y, z, ForgeDirection.NORTH.ordinal())) != null)
        {
            double textureUMin = renderIcon.getInterpolatedU(0);
            double textureUMax = renderIcon.getInterpolatedU(16);
            double textureVMin = renderIcon.getInterpolatedV(0);
            double textureVMax = renderIcon.getInterpolatedV(16);

            tessellator.addVertexWithUV(xPos, yPos + 1, zPos + e, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos, yPos, zPos + e, textureUMin, textureVMax);
            tessellator.addVertexWithUV(xPos + 1, yPos, zPos + e, textureUMax, textureVMax);
            tessellator.addVertexWithUV(xPos + 1, yPos + 1, zPos + e, textureUMax, textureVMin);
        }

        if ((renderIcon = block.getIcon(world, x, y, z, ForgeDirection.WEST.ordinal())) != null)
        {
            double textureUMin = renderIcon.getInterpolatedU(0);
            double textureUMax = renderIcon.getInterpolatedU(16);
            double textureVMin = renderIcon.getInterpolatedV(0);
            double textureVMax = renderIcon.getInterpolatedV(16);

            tessellator.addVertexWithUV(xPos + e, yPos, zPos, textureUMin, textureVMax);
            tessellator.addVertexWithUV(xPos + e, yPos + 1, zPos, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos + e, yPos + 1, zPos + 1, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos + e, yPos, zPos + 1, textureUMax, textureVMax);
        }


        if (!(block instanceof InteriorAirMarker) && (renderIcon = block.getIcon(world, x, y, z, ForgeDirection.DOWN.ordinal())) != null)
        {
            double textureUMin = renderIcon.getInterpolatedU(0);
            double textureUMax = renderIcon.getInterpolatedU(16);
            double textureVMin = renderIcon.getInterpolatedV(0);
            double textureVMax = renderIcon.getInterpolatedV(16);

            tessellator.addVertexWithUV(xPos + 1, yPos + e, zPos, textureUMin, textureVMax);
            tessellator.addVertexWithUV(xPos, yPos + e, zPos, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos, yPos + e, zPos + 1, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos + 1, yPos + e, zPos + 1, textureUMax, textureVMax);
        }

        if ((renderIcon = block.getIcon(world, x, y, z, ForgeDirection.SOUTH.ordinal())) != null)
        {
            double textureUMin = renderIcon.getInterpolatedU(0);
            double textureUMax = renderIcon.getInterpolatedU(16);
            double textureVMin = renderIcon.getInterpolatedV(0);
            double textureVMax = renderIcon.getInterpolatedV(16);

            tessellator.addVertexWithUV(xPos, yPos + 1, zPos + 1 - e, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos + 1, yPos + 1, zPos + 1 - e, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos + 1, yPos, zPos + 1 - e, textureUMax, textureVMax);
            tessellator.addVertexWithUV(xPos, yPos, zPos + 1 - e, textureUMin, textureVMax);
        }

        if ((renderIcon = block.getIcon(world, x, y, z, ForgeDirection.EAST.ordinal())) != null)
        {
            double textureUMin = renderIcon.getInterpolatedU(0);
            double textureUMax = renderIcon.getInterpolatedU(16);
            double textureVMin = renderIcon.getInterpolatedV(0);
            double textureVMax = renderIcon.getInterpolatedV(16);

            tessellator.addVertexWithUV(xPos + 1 - e, yPos, zPos, textureUMin, textureVMax);
            tessellator.addVertexWithUV(xPos + 1 - e, yPos, zPos + 1, textureUMax, textureVMax);
            tessellator.addVertexWithUV(xPos + 1 - e, yPos + 1, zPos + 1, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos + 1 - e, yPos + 1, zPos, textureUMin, textureVMin);
        }

        if ((renderIcon = block.getIcon(world, x, y, z, ForgeDirection.UP.ordinal())) != null)
        {
            double textureUMin = renderIcon.getInterpolatedU(0);
            double textureUMax = renderIcon.getInterpolatedU(16);
            double textureVMin = renderIcon.getInterpolatedV(0);
            double textureVMax = renderIcon.getInterpolatedV(16);

            tessellator.addVertexWithUV(xPos + 1, yPos + 1 - e, zPos + 1, textureUMax, textureVMax);
            tessellator.addVertexWithUV(xPos, yPos + 1 - e, zPos + 1, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos, yPos + 1 - e, zPos, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos + 1, yPos + 1 - e, zPos, textureUMin, textureVMax);
        }

        return true;
    }

    private void renderThing(double xPos, double yPos, double zPos, Tessellator tessellator)
    {
        final IIcon renderIcon;
        double pedestalHeight = 0.25;

        final Block blockFromName = Block.getBlockFromName("minecraft:stone");
        renderIcon = blockFromName.getIcon(0, 0);

        tessellator.addVertexWithUV(
                xPos, yPos, zPos,
                renderIcon.getInterpolatedU(0), renderIcon.getInterpolatedV(0));
        tessellator.addVertexWithUV(
                xPos + pedestalHeight, yPos + pedestalHeight, zPos + pedestalHeight,
                renderIcon.getInterpolatedU(4), renderIcon.getInterpolatedV(4) );
        tessellator.addVertexWithUV(
                xPos + 1 - pedestalHeight, yPos + pedestalHeight, zPos + pedestalHeight,
                renderIcon.getInterpolatedU(12), renderIcon.getInterpolatedV(4));
        tessellator.addVertexWithUV(
                xPos + 1, yPos, zPos,
                renderIcon.getInterpolatedU(16), renderIcon.getInterpolatedV(0));

        tessellator.addVertexWithUV(
                xPos, yPos, zPos,
                renderIcon.getInterpolatedU(0), renderIcon.getInterpolatedV(0));
        tessellator.addVertexWithUV(
                xPos, yPos, zPos + 1,
                renderIcon.getInterpolatedU(0), renderIcon.getInterpolatedV(16));
        tessellator.addVertexWithUV(
                xPos + pedestalHeight, yPos + pedestalHeight, zPos + 1 - pedestalHeight,
                renderIcon.getInterpolatedU(4), renderIcon.getInterpolatedV(12));
        tessellator.addVertexWithUV(
                xPos + pedestalHeight, yPos + pedestalHeight, zPos + pedestalHeight,
                renderIcon.getInterpolatedU(4), renderIcon.getInterpolatedV(4) );

        tessellator.addVertexWithUV(
                xPos, yPos, zPos + 1,
                renderIcon.getInterpolatedU(0), renderIcon.getInterpolatedV(16));
        tessellator.addVertexWithUV(
                xPos + 1, yPos, zPos + 1,
                renderIcon.getInterpolatedU(16), renderIcon.getInterpolatedV(16));
        tessellator.addVertexWithUV(
                xPos + 1 - pedestalHeight, yPos + pedestalHeight, zPos + 1 - pedestalHeight,
                renderIcon.getInterpolatedU(12), renderIcon.getInterpolatedV(12));
        tessellator.addVertexWithUV(
                xPos + pedestalHeight, yPos + pedestalHeight, zPos + 1 - pedestalHeight,
                renderIcon.getInterpolatedU(4), renderIcon.getInterpolatedV(12) );

        tessellator.addVertexWithUV(
                xPos + 1, yPos, zPos,
                renderIcon.getInterpolatedU(16), renderIcon.getInterpolatedV(0));
        tessellator.addVertexWithUV(
                xPos + 1 - pedestalHeight, yPos + pedestalHeight, zPos + pedestalHeight,
                renderIcon.getInterpolatedU(12), renderIcon.getInterpolatedV(4) );
        tessellator.addVertexWithUV(
                xPos + 1 - pedestalHeight, yPos + pedestalHeight, zPos + 1 - pedestalHeight,
                renderIcon.getInterpolatedU(12), renderIcon.getInterpolatedV(12));
        tessellator.addVertexWithUV(
                xPos + 1, yPos, zPos + 1,
                renderIcon.getInterpolatedU(16), renderIcon.getInterpolatedV(16));

        tessellator.addVertexWithUV(
                xPos + pedestalHeight, yPos + pedestalHeight, zPos + pedestalHeight,
                renderIcon.getInterpolatedU(4), renderIcon.getInterpolatedV(4));
        tessellator.addVertexWithUV(
                xPos + pedestalHeight, yPos + pedestalHeight, zPos + 1 - pedestalHeight,
                renderIcon.getInterpolatedU(4), renderIcon.getInterpolatedV(12) );

        tessellator.addVertexWithUV(
                xPos + 1 - pedestalHeight, yPos + pedestalHeight, zPos + 1 - pedestalHeight,
                renderIcon.getInterpolatedU(12), renderIcon.getInterpolatedV(12));
        tessellator.addVertexWithUV(
                xPos + 1 - pedestalHeight, yPos + pedestalHeight, zPos + pedestalHeight,
                renderIcon.getInterpolatedU(12), renderIcon.getInterpolatedV(4));
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.insideMetadataBlockRendererId;
    }

}
