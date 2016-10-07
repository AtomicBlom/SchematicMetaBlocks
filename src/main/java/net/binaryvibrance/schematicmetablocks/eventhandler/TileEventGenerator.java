package net.binaryvibrance.schematicmetablocks.events;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Queue;

@Mod.EventBusSubscriber
public class TileEventGenerator {

    private static final Map<BlockPos, TileEntity> trackedEntities = Maps.newHashMap();

    @SubscribeEvent
    public static void OnChunkLoaded(ChunkDataEvent.Load event) {
        Map<BlockPos, TileEntity> tileEntityMap = event.getChunk().getTileEntityMap();
        for (TileEntity tileEntity : tileEntityMap.values()) {
            final boolean trackEntity = MinecraftForge.EVENT_BUS.post(new TileEntityEvent.Added(tileEntity));
            if (trackEntity) {

            }
        }
    }

    @SubscribeEvent
    public static void OnChunkLoaded(ChunkDataEvent.Unload event) {
        Queue<BlockPos> removedTileEntities = Queues.newArrayDeque();

        for (final Map.Entry<BlockPos, TileEntity> blockPosTileEntityEntry : trackedEntities.entrySet())
        {
            final BlockPos pos = blockPosTileEntityEntry.getKey();
            if (event.getChunk().isAtLocation(pos.getX() >> 4, pos.getZ() >> 4)) {
                removedTileEntities.add(pos);
                MinecraftForge.EVENT_BUS.post(new TileEntityEvent.Removed(blockPosTileEntityEntry.getValue()));
            }
        }
    }
}
