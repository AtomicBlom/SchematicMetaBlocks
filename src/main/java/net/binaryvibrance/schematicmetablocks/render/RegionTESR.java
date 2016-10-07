package net.binaryvibrance.schematicmetablocks.render;

import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class RegionTESR extends TileEntitySpecialRenderer<RegionTileEntity>
{
    @Override
    public void renderTileEntityAt(RegionTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        //if (te.isRenderBlock())
        if (false)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            //GlStateManager.disableCull();

            final RegionTileEntity opposite = te.getLinkedTileEntity();

            final BlockPos primaryLocation = te.getPos();
            final BlockPos oppositeLocation = opposite.getPos();
            final int minX = Math.min(primaryLocation.getX(), oppositeLocation.getX());
            final int minY = Math.min(primaryLocation.getY(), oppositeLocation.getY());
            final int minZ = Math.min(primaryLocation.getZ(), oppositeLocation.getZ());
            final int maxX = Math.max(primaryLocation.getX(), oppositeLocation.getX());
            final int maxY = Math.max(primaryLocation.getY(), oppositeLocation.getY());
            final int maxZ = Math.max(primaryLocation.getZ(), oppositeLocation.getZ());

            int xMin = minX == 0 ? 0 : minX / Math.abs(minX);
            int zMin = minZ == 0 ? 0 : minZ / Math.abs(minZ);

            int xMax = maxX == 0 ? 0 : maxX / Math.abs(maxX);
            int zMax = maxZ == 0 ? 0 : maxZ / Math.abs(maxZ);

            final double e = 0;// 1 / 16.0;
            final AxisAlignedBB boundingBox = new AxisAlignedBB(minX + 1, minY + 1, minZ + 1, maxX, maxY, maxZ).expand(e, e, e);

            Logger.info("(%d,%d)-(%d,%d) - %s", xMin, zMin, xMax, zMax, boundingBox);

            GL11.glLineWidth(3);
            //render(boundingBox);
            GL11.glLineWidth(1);

            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    private void render(AxisAlignedBB boundingBox) {

    Tessellator tessellator = Tessellator.getInstance();
    VertexBuffer vertexBuffer = tessellator.getBuffer();

    vertexBuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
    vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
    tessellator.draw();

    vertexBuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
    vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
    tessellator.draw();

    vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

    vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
    vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 255).endVertex();
    tessellator.draw();

    vertexBuffer.setTranslation(0, 0, 0);
}
}
