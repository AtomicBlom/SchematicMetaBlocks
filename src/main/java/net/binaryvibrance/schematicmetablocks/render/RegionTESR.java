package net.binaryvibrance.schematicmetablocks.render;

import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

/**
 * Created by codew on 6/10/2016.
 */
public class RegionTESR extends TileEntitySpecialRenderer<RegionTileEntity>
{
    @Override
    public void renderTileEntityAt(RegionTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();

        final double e = 1 / 16;
        final RegionTileEntity primary = te;
        if (primary.isRenderBlock())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            //vertexBuffer.setTranslation(x - te.getPos().getX(), y - te.getPos().getY(), z - te.getPos().getZ());

            final RegionTileEntity opposite = primary.getLinkedTileEntity();

            final BlockPos primaryLocation = primary.getPos();
            final BlockPos oppositeLocation = opposite.getPos();
            int minX = Math.min(primaryLocation.getX(), oppositeLocation.getX());
            int minY = Math.min(primaryLocation.getY(), oppositeLocation.getY());
            int minZ = Math.min(primaryLocation.getZ(), oppositeLocation.getZ());
            int maxX = Math.max(primaryLocation.getX(), oppositeLocation.getX());
            int maxY = Math.max(primaryLocation.getY(), oppositeLocation.getY());
            int maxZ = Math.max(primaryLocation.getZ(), oppositeLocation.getZ());
            AxisAlignedBB boundingBox = new AxisAlignedBB(minX + 1, minY + 1, minZ + 1, maxX, maxY, maxZ).expand(e, e, e);

            render(tessellator, vertexBuffer, primaryLocation, boundingBox);

            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    private void render(Tessellator tessellator, VertexBuffer vertexBuffer, BlockPos primaryLocation, AxisAlignedBB boundingBox) {
        //vertexBuffer.setTranslation(primaryLocation.getX(), primaryLocation.getY(), primaryLocation.getZ());

        //final VertexFormat originalVertexFormat = vertexBuffer.getVertexFormat();
        //final int originalDrawMode = vertexBuffer.getDrawMode();

        //vertexBuffer.draw();
        GL11.glLineWidth(3);
        vertexBuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        //vertexBuffer.startDrawing(GL11.GL_LINE_LOOP);
        //vertexBuffer.setColorOpaque(255, 0, 0);
        //vertexBuffer.noColor();

        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
        //vertexBuffer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        //vertexBuffer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        //vertexBuffer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        //vertexBuffer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);

        //vertexBuffer.finishDrawing();
        tessellator.draw();
        //vertexBuffer.draw();

        vertexBuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        //vertexBuffer.startDrawing(GL11.GL_LINE_LOOP);
        //vertexBuffer.setColorOpaque(255, 0, 0);

        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
        //vertexBuffer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        //vertexBuffer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        //vertexBuffer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        //vertexBuffer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);

        //vertexBuffer.finishDrawing();
        tessellator.draw();
        //vertexBuffer.draw();

        vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        //vertexBuffer.startDrawing(GL11.GL_LINES);
        //vertexBuffer.setColorOpaque(255, 0, 0);

        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();

        //vertexBuffer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        //vertexBuffer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        //vertexBuffer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        //vertexBuffer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        //vertexBuffer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        //vertexBuffer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        //vertexBuffer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        //vertexBuffer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);

        //vertexBuffer.finishDrawing();
        tessellator.draw();
        //vertexBuffer.draw();

        vertexBuffer.setTranslation(0, 0, 0);
        GL11.glLineWidth(1);
        //vertexBuffer.begin(originalDrawMode, originalVertexFormat);
    }
}
