package net.binaryvibrance.schematica4worldgen.proxy;

import net.binaryvibrance.schematica4worldgen.TheMod;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraftforge.common.util.ForgeDirection;

public class InsideMetadataBlockRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID,
                                     RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
                                    Block block, int modelId, RenderBlocks renderer) {

        //which render pass are we doing?
        if(ClientProxy.renderPass == 0)
        {
            return false;
            //we are on the solid block render pass, lets render the solid diamond block


        }

        //change the passed integer coordinates into double precision floats, and set the height on the y axis
        double xPos = (double)x;
        double yPos = (double)y;
        double zPos = (double)z;

        //this is the scale of the squares, in blocks
        float scale = 0.5F;

        //get the tessellator instance
        Tessellator tessellator = Tessellator.instance;

        IIcon diamondIcon = block.getBlockTextureFromSide(0);

        double textureUMin = diamondIcon.getInterpolatedU(0);
        double textureUMax = diamondIcon.getInterpolatedU(16);
        double textureVMin = diamondIcon.getInterpolatedV(0);
        double textureVMax = diamondIcon.getInterpolatedV(16);

        double e = 0.0001;

        if (block.getIcon(world, x, y, z, ForgeDirection.NORTH.ordinal()) != null)
        {
            tessellator.addVertexWithUV(xPos, yPos + 1, zPos + e, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos, yPos, zPos + e, textureUMin, textureVMax);
            tessellator.addVertexWithUV(xPos + 1, yPos, zPos + e, textureUMax, textureVMax);
            tessellator.addVertexWithUV(xPos + 1, yPos + 1, zPos + e, textureUMax, textureVMin);
        }

        if (block.getIcon(world, x, y, z, ForgeDirection.WEST.ordinal()) != null)
        {
            tessellator.addVertexWithUV(xPos + e, yPos, zPos, textureUMin, textureVMax);
            tessellator.addVertexWithUV(xPos + e, yPos + 1, zPos, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos + e, yPos + 1, zPos + 1, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos + e, yPos, zPos + 1, textureUMax, textureVMax);
        }


        if (block.getIcon(world, x, y, z, ForgeDirection.DOWN.ordinal()) != null)
        {
            tessellator.addVertexWithUV(xPos + 1, yPos + e, zPos, textureUMin, textureVMax);
            tessellator.addVertexWithUV(xPos, yPos + e, zPos, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos, yPos + e, zPos + 1, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos + 1, yPos + e, zPos + 1, textureUMax, textureVMax);
        }

        if (block.getIcon(world, x, y, z, ForgeDirection.SOUTH.ordinal()) != null)
        {
            tessellator.addVertexWithUV(xPos, yPos + 1, zPos + 1 - e, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos + 1, yPos + 1, zPos + 1 - e, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos + 1, yPos, zPos + 1 - e, textureUMax, textureVMax);
            tessellator.addVertexWithUV(xPos, yPos, zPos + 1 - e, textureUMin, textureVMax);
        }

        if (block.getIcon(world, x, y, z, ForgeDirection.EAST.ordinal()) != null) {
            tessellator.addVertexWithUV(xPos + 1 - e, yPos, zPos, textureUMin, textureVMax);
            tessellator.addVertexWithUV(xPos + 1 - e, yPos, zPos + 1, textureUMax, textureVMax);
            tessellator.addVertexWithUV(xPos + 1 - e, yPos + 1, zPos + 1, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos + 1 - e, yPos + 1, zPos, textureUMin, textureVMin);
        }

        if (block.getIcon(world, x, y, z, ForgeDirection.UP.ordinal()) != null)
        {
            tessellator.addVertexWithUV(xPos + 1, yPos + 1 - e, zPos + 1, textureUMax, textureVMax);
            tessellator.addVertexWithUV(xPos, yPos + 1 - e, zPos + 1, textureUMax, textureVMin);
            tessellator.addVertexWithUV(xPos, yPos + 1 - e, zPos, textureUMin, textureVMin);
            tessellator.addVertexWithUV(xPos + 1, yPos + 1 - e, zPos, textureUMin, textureVMax);
        }

        return true;
    }

    //Our custom renderer for the diamond, draws a pair of crossed squares in the centre of the block
    public void drawDiamond(Block par1Block, int x, int y, int z)
    {

    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return false;
    }

    @Override
    public int getRenderId() {

        return ClientProxy.insideMetadataBlockRendererId;
    }

}
