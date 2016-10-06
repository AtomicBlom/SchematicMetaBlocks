package net.binaryvibrance.schematicmetablocks.render;

import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
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
        Tessellator temp = Tessellator.getInstance();
        VertexBuffer tessellator = temp.getBuffer();

        final double e = 1 / 16;
        final RegionTileEntity primary = te;
        if (primary.isRenderBlock())
        {
            BlockPos blockpos = te.getPos();

            tessellator.setTranslation(x - blockpos.getX(), y - blockpos.getY(), z - blockpos.getZ());
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

            //tessellator.draw();
            GL11.glLineWidth(3);
            tessellator.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
            //tessellator.startDrawing(GL11.GL_LINE_LOOP);
            //tessellator.setColorOpaque(255, 0, 0);

            tessellator.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
            //tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
            //tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
            //tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
            //tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);

            tessellator.finishDrawing();
            //tessellator.draw();

            tessellator.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
            //tessellator.startDrawing(GL11.GL_LINE_LOOP);
            //tessellator.setColorOpaque(255, 0, 0);

            tessellator.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
            //tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
            //tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
            //tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
            //tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);

            tessellator.finishDrawing();
            //tessellator.draw();

            tessellator.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
            //tessellator.startDrawing(GL11.GL_LINES);
            //tessellator.setColorOpaque(255, 0, 0);

            tessellator.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();
            tessellator.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(255, 0, 0, 128).endVertex();

            //tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
            //tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
            //tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
            //tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
            //tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
            //tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
            //tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
            //tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);

            tessellator.finishDrawing();
            //tessellator.draw();

            GL11.glLineWidth(1);
            //tessellator.startDrawingQuads();
        }
    }
}
