package net.binaryvibrance.schematicmetablocks.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.binaryvibrance.schematicmetablocks.proxy.ClientProxy;
import net.binaryvibrance.schematicmetablocks.schematic.WorldBlockCoord;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RegionBlockRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        if (ClientProxy.renderPass == 0)
        {
            final double e = 1/16;
            final RegionTileEntity primary = RegionTileEntity.tryGetTileEntity(world, x, y, z);
            if (primary.isPrimaryBlock()) {
                final RegionTileEntity opposite = primary.getOpposite();

                final WorldBlockCoord primaryLocation = primary.getWorldBlockLocation();
                final WorldBlockCoord oppositeLocation = opposite.getWorldBlockLocation();
                int minX = Math.min(primaryLocation.x, oppositeLocation.x);
                int minY = Math.min(primaryLocation.y, oppositeLocation.y);
                int minZ = Math.min(primaryLocation.z, oppositeLocation.z);
                int maxX = Math.max(primaryLocation.x, oppositeLocation.x);
                int maxY = Math.max(primaryLocation.y, oppositeLocation.y);
                int maxZ = Math.max(primaryLocation.z, oppositeLocation.z);
                AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(minX + 1, minY + 1, minZ + 1, maxX, maxY, maxZ).expand(e, e, e);


                //RenderGlobal.drawOutlinedBoundingBox(boundingBox, 0xFF00FF);
                tessellator.draw();
                GL11.glLineWidth(3);
                tessellator.startDrawing(GL11.GL_LINE_LOOP);
                tessellator.setColorOpaque(255, 0, 0);

                tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
                tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
                tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
                tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
                tessellator.draw();

                tessellator.startDrawing(GL11.GL_LINE_LOOP);
                tessellator.setColorOpaque(255, 0, 0);

                tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
                tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
                tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
                tessellator.draw();

                tessellator.startDrawing(GL11.GL_LINES);
                tessellator.setColorOpaque(255, 0, 0);

                tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
                tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
                tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
                tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
                tessellator.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
                tessellator.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
                tessellator.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
                tessellator.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
                tessellator.draw();

                GL11.glLineWidth(1);
                tessellator.startDrawingQuads();

                return true;
            }

        } else if (ClientProxy.renderPass == 1) {

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            renderer.renderStandardBlock(block, x, y, z);
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.regionBlockRendererId;
    }
}
