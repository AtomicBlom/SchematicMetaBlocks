package net.binaryvibrance.schematicmetablocks.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.binaryvibrance.schematicmetablocks.jobs.ChunkToProcess;
import net.binaryvibrance.schematicmetablocks.jobs.JobProcessor;
import net.binaryvibrance.schematicmetablocks.jobs.JobType;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class WorldListener
{
    public static final WorldListener Instance = new WorldListener();

    private WorldListener() {}

    @SubscribeEvent
    public void onBlockChanged(BlockEvent.PlaceEvent blockPlacedEvent)
    {
        World world = blockPlacedEvent.blockSnapshot.world;
        ChunkToProcess chunkToProcess = new ChunkToProcess(world, blockPlacedEvent.x >> 4, blockPlacedEvent.z >> 4);
        JobProcessor.Instance.scheduleJob(JobType.BACKGROUND, chunkToProcess);
    }

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent blockBrokenEvent)
    {
        World world = blockBrokenEvent.world;
        ChunkToProcess chunkToProcess = new ChunkToProcess(world, blockBrokenEvent.x >> 4, blockBrokenEvent.z >> 4);
        JobProcessor.Instance.scheduleJob(JobType.BACKGROUND, chunkToProcess);
    }

}
