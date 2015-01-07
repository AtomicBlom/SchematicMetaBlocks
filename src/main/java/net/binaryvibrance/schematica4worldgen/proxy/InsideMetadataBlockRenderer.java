package net.binaryvibrance.schematica4worldgen.proxy;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class InsideMetadataBlockRenderer implements ISimpleBlockRenderingHandler
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
        if (ClientProxy.renderPass == 0)
        {
            return false;
        }

        double xPos = (double) x;
        double yPos = (double) y;
        double zPos = (double) z;

        //get the tessellator instance
        Tessellator tessellator = Tessellator.instance;

        tessellator.setBrightness(240);

        final double e = 0.0001;
        IIcon renderIcon;
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


        if ((renderIcon = block.getIcon(world, x, y, z, ForgeDirection.DOWN.ordinal())) != null)
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

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return ClientProxy.insideMetadataBlockRendererId;
    }

}