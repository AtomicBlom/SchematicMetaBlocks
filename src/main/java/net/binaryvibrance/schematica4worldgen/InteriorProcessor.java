package net.binaryvibrance.schematica4worldgen;

import cpw.mods.fml.common.Mod;
import net.binaryvibrance.schematica4worldgen.blocks.ImplicitAirBlock;
import net.binaryvibrance.schematica4worldgen.library.ModBlock;
import net.binaryvibrance.schematica4worldgen.tileentity.InteriorAirMarkerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import java.util.LinkedList;
import java.util.Queue;

public class InteriorProcessor
{
    public static InteriorProcessor Instance = new InteriorProcessor();
    private InteriorProcessor() {

    }

    @Mod.EventHandler
    public void onChunkLoaded(ChunkEvent.Load chunkLoadEvent) {

    }

    @Mod.EventHandler
    public void onBlockChanged(BlockEvent.PlaceEvent blockPlacedEvent) {
        World world = blockPlacedEvent.blockSnapshot.world;
        Chunk c = world.getChunkFromBlockCoords(blockPlacedEvent.x, blockPlacedEvent.z);
    }

    public void processChunk(World world, int chunkX, int chunkZ) {
        ChunkToProcess chunkToProcess = new ChunkToProcess(world, chunkX, chunkZ);
        processChunk(chunkToProcess);
    }

    private void processChunk(ChunkToProcess chunkToProcess)
    {
        World world = chunkToProcess.world;
        int worldHeight = world.getActualHeight();
        Chunk chunk = chunkToProcess.world.getChunkFromChunkCoords(chunkToProcess.x, chunkToProcess.z);
        boolean[] processed = new boolean[16 * 16 * worldHeight];
        Queue<Integer> blocksToProcess = new LinkedList<Integer>();

        for(Object obj : chunk.chunkTileEntityMap.values())
        {
            TileEntity entity = (TileEntity)obj;
            if (entity instanceof InteriorAirMarkerTileEntity)
            {
                blocksToProcess.add(createIndex(entity.xCoord & 15, entity.yCoord, entity.zCoord & 15));
            }
        }

        int processedBlocks = 0;
        while (!blocksToProcess.isEmpty()) {
            processedBlocks++;
            int blockToProcess = blocksToProcess.poll();
            int x = decodeX(blockToProcess);
            int y = decodeY(blockToProcess);
            int z = decodeZ(blockToProcess);

            final int chunkZStart = chunk.zPosition * 16;
            final int chunkXStart = chunk.xPosition * 16;
            Block currentBlock = chunk.getBlock(x, y, z);
            if (currentBlock instanceof BlockAir)
            {
                world.setBlock(chunkXStart + x, y, chunkZStart + z, ModBlock.blockImplicitAir, 0, 3);
            }

            Logger.info("Processing block %d,%d,%d (%d/%d)", chunkXStart + x, y, chunkZStart + z, processedBlocks, processed.length);

            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                int neighbourX = x + d.offsetX;
                int neighbourY = y + d.offsetY;
                int neighbourZ = z + d.offsetZ;

                if (neighbourX >= 0 && neighbourX <= 15 && neighbourY >= 0 && neighbourY < worldHeight && neighbourZ >= 0 && neighbourZ <= 15)
                {
                    int neighbourIndex = createIndex(neighbourX, neighbourY, neighbourZ);
                    Block b = world.getBlock(chunkXStart + neighbourX, neighbourY, chunkZStart + neighbourZ);
                    if (b instanceof BlockAir || b instanceof ImplicitAirBlock)
                    {
                        if (!processed[neighbourIndex])
                        {
                            processed[neighbourIndex] = true;
                            Logger.info("adding block %d,%d,%d to list", chunkXStart + neighbourX, neighbourY, chunkZStart + neighbourZ);
                            blocksToProcess.add(neighbourIndex);
                        }
                    }
                }
            }
        }
    }

    private static int createIndex(int x, int y, int z) {

        //yyyyyyyyzzzzxxxx
        return y << 8 | z << 4 | x;
    }

    private static int decodeX(int index) {
        return index & 15;
    }

    private static int decodeZ(int index) {
        return (index >> 4) & 15;
    }

    private static int decodeY(int index) {
        return (index >> 8);
    }

    private static class ChunkToProcess {
        private final World world;
        private final int x;
        private final int z;

        public ChunkToProcess(World world, int x, int z) {
            this.world = world;
            this.x = x;
            this.z = z;
        }
    }
}
