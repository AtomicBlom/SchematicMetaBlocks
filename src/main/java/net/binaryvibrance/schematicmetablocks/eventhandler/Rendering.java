package net.binaryvibrance.schematicmetablocks.eventhandler;

import com.google.common.collect.Sets;
import net.binaryvibrance.schematicmetablocks.Logger;
import net.binaryvibrance.schematicmetablocks.events.TileEntityEvent;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import java.util.Set;

@Mod.EventBusSubscriber
public class Rendering
{

    private static final Set<TileEntity> renderables = Sets.newHashSet();

    @SubscribeEvent
    public static void onRenderWorld(RenderWorldLastEvent event) {

        final float partialTicks = event.getPartialTicks();
        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        final double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        final double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        final double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        GlStateManager.disableTexture2D();

        GL11.glPushMatrix();
        GL11.glTranslated(-x, -y, -z);
        GL11.glLineWidth(2);
        GlStateManager.enableBlend();
        for (final TileEntity renderable : renderables)
        {
            if (renderable instanceof RegionTileEntity) {
                final RegionTileEntity regionTileEntity = (RegionTileEntity) renderable;
                if (regionTileEntity.isRenderBlock()) {
                    renderTE(regionTileEntity);
                }
            }
        }
        GL11.glLineWidth(1);
        GL11.glPopMatrix();
        GlStateManager.enableTexture2D();
    }

    @SubscribeEvent
    public static void onTileEntityAdded(TileEntityEvent.Added event) {
        if (event.getTileEntity() instanceof RegionTileEntity) {
            event.setResult(Event.Result.ALLOW);
            renderables.add(event.getTileEntity());
        }
    }

    @SubscribeEvent
    public static void onTileEntityRemoved(TileEntityEvent.Removed event) {
        if (event.getTileEntity() instanceof RegionTileEntity) {
            renderables.add(event.getTileEntity());
        }
    }

    private static void renderTE(RegionTileEntity te) {
        final RegionTileEntity opposite = te.getLinkedTileEntity();

        final BlockPos primaryLocation = te.getPos();
        final BlockPos oppositeLocation = opposite.getPos();
        final int minX = Math.min(primaryLocation.getX(), oppositeLocation.getX());
        final int minY = Math.min(primaryLocation.getY(), oppositeLocation.getY());
        final int minZ = Math.min(primaryLocation.getZ(), oppositeLocation.getZ());
        final int maxX = Math.max(primaryLocation.getX(), oppositeLocation.getX());
        final int maxY = Math.max(primaryLocation.getY(), oppositeLocation.getY());
        final int maxZ = Math.max(primaryLocation.getZ(), oppositeLocation.getZ());

        final double e = 0.1 / 16.0;
        final AxisAlignedBB boundingBox = new AxisAlignedBB(minX + 1, minY + 1, minZ + 1, maxX, maxY, maxZ).expand(e, e, e);

        render(boundingBox);
    }

    private static void render(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();

        final float r = 0;
        final float g = 0.4352941176470588f;
        final float b = 0.6431372549019608f;
        final float a = 0.5f;

        vertexBuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(r, g, b, a).endVertex();
        tessellator.draw();

        vertexBuffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(r, g, b, a).endVertex();
        tessellator.draw();

        vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(r, g, b, a).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(r, g, b, a).endVertex();
        tessellator.draw();

        vertexBuffer.setTranslation(0, 0, 0);
    }
}
