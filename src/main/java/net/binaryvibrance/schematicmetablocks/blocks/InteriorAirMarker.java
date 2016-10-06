package net.binaryvibrance.schematicmetablocks.blocks;

import net.binaryvibrance.schematicmetablocks.jobs.ChunkToProcess;
import net.binaryvibrance.schematicmetablocks.jobs.JobProcessor;
import net.binaryvibrance.schematicmetablocks.jobs.JobType;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.tileentity.InteriorAirMarkerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class InteriorAirMarker extends Block
{
    //public static final String NAME = "blockInteriorAirMarker";

    private static final AxisAlignedBB bounds = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.15F, 1.0F);

    public InteriorAirMarker()
    {
        super(Material.GLASS);
        //this.setBlockName(NAME);
    }


    /*@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }*/

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return bounds;
    }

    /*@Override
    public int getRenderType()
    {
        return ClientProxy.insideMetadataBlockRendererId;
    }*/

    /*@Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        Block b = world.getBlock(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
        if (b instanceof ImplicitAirBlock || b instanceof InteriorAirMarker)
        {
            return null;
        }

        return super.getIcon(world, x, y, z, side);
    }*/

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockState(pos, ModBlock.blockImplicitAir.getDefaultState(), 3);
    }

    /*@Override
    public int getRenderBlockPass()
    {
        return 1;
    }*/

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (!worldIn.isRemote)
        {
            ChunkToProcess chunkToProcess = new ChunkToProcess(worldIn, pos.getX() >> 4, pos.getZ() >> 4);
            JobProcessor.Instance.scheduleJob(JobType.BACKGROUND, chunkToProcess);
        }
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new InteriorAirMarkerTileEntity();
    }

    /*@Override
    public boolean canRenderInPass(int pass)
    {
        ClientProxy.renderPass = pass;
        return pass != 0;
    }*/


    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.SOLID;
    }
}
