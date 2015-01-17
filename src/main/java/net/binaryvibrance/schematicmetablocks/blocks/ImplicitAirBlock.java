package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.TheMod;
import net.binaryvibrance.schematicmetablocks.proxy.ClientProxy;
import net.binaryvibrance.schematicmetablocks.utility.BlockSideRotation;
import net.binaryvibrance.schematicmetablocks.utility.TextureDirection;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplicitAirBlock extends MetaBlock
{
    public static final String NAME = "blockImplicitAir";
    private final Map<String, IIcon> connectedTextures = new HashMap<String, IIcon>();
    public ImplicitAirBlock()
    {
        super(Material.glass, false);
        this.setBlockName(NAME);
    }

    @Override
    public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity entity)
    {
        if (!(entity instanceof EntityPlayer)) {
            super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, entity);
        }

    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return ClientProxy.insideMetadataBlockRendererId;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass = pass;
        return pass != 0;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        ForgeDirection blockSide = ForgeDirection.getOrientation(side);

        ForgeDirection f = BlockSideRotation.forOrientation(TextureDirection.FORWARD, ForgeDirection.getOrientation(side));
        Block blockF = world.getBlock(x + f.offsetX, y + f.offsetY, z + f.offsetZ);
        if (isBlockValid(blockF)) {
            return null;
        }

        ForgeDirection t = BlockSideRotation.forOrientation(TextureDirection.ABOVE, ForgeDirection.getOrientation(side));
        ForgeDirection l = BlockSideRotation.forOrientation(TextureDirection.LEFT, ForgeDirection.getOrientation(side));
        ForgeDirection b = BlockSideRotation.forOrientation(TextureDirection.BELOW, ForgeDirection.getOrientation(side));
        ForgeDirection r = BlockSideRotation.forOrientation(TextureDirection.RIGHT, ForgeDirection.getOrientation(side));

        Block blockT = world.getBlock(x + t.offsetX, y + t.offsetY, z + t.offsetZ);
        Block blockL = world.getBlock(x + l.offsetX, y + l.offsetY, z + l.offsetZ);
        Block blockB = world.getBlock(x + b.offsetX, y + b.offsetY, z + b.offsetZ);
        Block blockR = world.getBlock(x + r.offsetX, y + r.offsetY, z + r.offsetZ);

        Block blockTL = world.getBlock(x + t.offsetX + l.offsetX, y + t.offsetY + l.offsetY, z + t.offsetZ + t.offsetZ);
        Block blockTR = world.getBlock(x + t.offsetX + r.offsetX, y + t.offsetY + r.offsetY, z + t.offsetZ + r.offsetZ);
        Block blockBL = world.getBlock(x + b.offsetX + l.offsetX, y + b.offsetY + l.offsetY, z + b.offsetZ + t.offsetZ);
        Block blockBR = world.getBlock(x + b.offsetX + r.offsetX, y + b.offsetY + r.offsetY, z + b.offsetZ + r.offsetZ);

        Boolean drawTSide = !isBlockValid(blockT);
        Boolean drawLSide = !isBlockValid(blockL);
        Boolean drawBSide = !isBlockValid(blockB);
        Boolean drawRSide = !isBlockValid(blockR);

        StringBuilder iconName = new StringBuilder();
        if (drawTSide) {
            iconName.append('T');
        }
        if (drawLSide) {
            iconName.append('L');
        }
        if (drawBSide) {
            iconName.append('B');
        }
        if (drawRSide) {
            iconName.append('R');
        }

        if (!drawTSide && !drawLSide && !isBlockValid(blockTL)) {
            iconName.append("_CTL");
        }
        if (!drawTSide && !drawRSide && !isBlockValid(blockTR)) {
            iconName.append("_CTR");
        }

        if (!drawBSide && !drawLSide && !isBlockValid(blockBL)) {
            iconName.append("_CBL");
        }
        if (!drawBSide && !drawRSide && !isBlockValid(blockBR)) {
            iconName.append("_CBR");
        }

        IIcon texture = null;
        if (iconName.length() > 0)
        {
            texture = connectedTextures.get(iconName.toString());
            if (texture == null)
            {
                texture = connectedTextures.get("DEAD");
            }
        }
        return texture;
    }

    private boolean isBlockValid(Block block)
    {
        return block instanceof ImplicitAirBlock || block instanceof  InteriorAirMarker;
    }

    public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_)
    {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        connectedTextures.clear();
        connectedTextures.put("DEAD", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_DEAD"));

        connectedTextures.put("T", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_T"));
        connectedTextures.put("L", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_L"));
        connectedTextures.put("B", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_B"));
        connectedTextures.put("R", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_R"));

        connectedTextures.put("TL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TL"));
        connectedTextures.put("TR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TR"));
        connectedTextures.put("LB", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_LB"));
        connectedTextures.put("BR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_BR"));

        connectedTextures.put("TLB", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TLB"));
        connectedTextures.put("TBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TBR"));
        connectedTextures.put("LBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_LBR"));
        connectedTextures.put("TLR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TLR"));

        connectedTextures.put("TLBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TLBR"));

        connectedTextures.put("TB", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TB"));
        connectedTextures.put("LR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_LR"));

        connectedTextures.put("B_CTL_CTR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_B_CTL_CTR"));
        connectedTextures.put("LB_CTR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_BL_CTR"));
        connectedTextures.put("BR_CTL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_BR_CTL"));

        connectedTextures.put("T_CBL_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_T_CBL_CBR"));
        connectedTextures.put("TL_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TL_CBR"));
        connectedTextures.put("TR_CBL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_TR_CBL"));

        connectedTextures.put("L_CTR_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_L_CTR_CBR"));
        connectedTextures.put("L_CTR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_L_CTR"));
        connectedTextures.put("L_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_L_CBR"));

        connectedTextures.put("R_CTL_CBL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_R_CTL_CBL"));
        connectedTextures.put("R_CTL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_R_CTL"));
        connectedTextures.put("R_CBL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_R_CBL"));

        connectedTextures.put("_CTL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTL"));
        connectedTextures.put("_CBL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CBL"));
        connectedTextures.put("_CTR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTR"));
        connectedTextures.put("_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CBR"));

        connectedTextures.put("_CTL_CTR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTL_CTR"));
        connectedTextures.put("_CTL_CBL", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTL_CBL"));
        connectedTextures.put("_CTL_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTL_CBR"));

        connectedTextures.put("_CTL_CTR_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTL_CTR_CBR"));
        connectedTextures.put("_CTL_CBL_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTL_CBL_CBR"));
        connectedTextures.put("_CTL_CBL_CTR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTL_CBL_CTR"));

        connectedTextures.put("_CTL_CBL_CTR_CBR", iconRegister.registerIcon(TheMod.MOD_ID + ":implicitAir/" + NAME + "_CTL_CBL_CTR_CBR"));





    }
}
