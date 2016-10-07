package net.binaryvibrance.schematicmetablocks.events;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

@Mod.EventBusSubscriber
public class TileEventGenerator {

    public static void OnChunkLoaded(ChunkDataEvent.Load event) {
        Map<BlockPos, TileEntity> tileEntityMap = event.getChunk().getTileEntityMap();
        for (TileEntity tileEntity : tileEntityMap.values()) {
            MinecraftForge.EVENT_BUS.post(new TileEntityEvent.Added(tileEntity));
        }
    }

    public static void OnChunkLoaded(ChunkDataEvent.Unload event) {
        Map<BlockPos, TileEntity> tileEntityMap = event.getChunk().getTileEntityMap();
        for (TileEntity tileEntity : tileEntityMap.values()) {
            MinecraftForge.EVENT_BUS.post(new TileEntityEvent.Removed(tileEntity));
        }
    }
}
