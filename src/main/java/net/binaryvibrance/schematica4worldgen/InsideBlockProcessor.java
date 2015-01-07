package net.binaryvibrance.schematica4worldgen;

import cpw.mods.fml.common.Mod;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import java.util.concurrent.BlockingQueue;

public class InsideBlockProcessor
{
    public static InsideBlockProcessor Instance = new InsideBlockProcessor();
    private InsideBlockProcessor() {

    }

    //BlockingQueue<ChunkToProcess>

    @Mod.EventHandler
    public void onChunkLoaded(ChunkEvent.Load chunkLoadEvent) {

    }

    @Mod.EventHandler
    public void onBlockChanged(BlockEvent.PlaceEvent blockPlacedEvent) {
        World world = blockPlacedEvent.blockSnapshot.world;
        Chunk c = world.getChunkFromBlockCoords(blockPlacedEvent.x, blockPlacedEvent.z);
    }

    private static class ChunkToProcess {
        private final int x;
        private final int z;

        public ChunkToProcess(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }
}
