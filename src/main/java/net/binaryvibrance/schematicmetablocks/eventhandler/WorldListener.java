package net.binaryvibrance.schematicmetablocks.eventhandler;

import net.binaryvibrance.schematicmetablocks.jobs.ChunkToProcess;
import net.binaryvibrance.schematicmetablocks.jobs.JobProcessor;
import net.binaryvibrance.schematicmetablocks.jobs.JobType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldListener
{
    public static final WorldListener Instance = new WorldListener();

    private WorldListener() {}

    @SubscribeEvent
    public void onBlockChanged(BlockEvent.PlaceEvent blockPlacedEvent)
    {
        World world = blockPlacedEvent.getBlockSnapshot().getWorld();
        final BlockPos pos = blockPlacedEvent.getPos();
        ChunkToProcess chunkToProcess = new ChunkToProcess(world, pos.getX() >> 4, pos.getZ() >> 4);
        JobProcessor.Instance.scheduleJob(JobType.BACKGROUND, chunkToProcess);
    }

    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent blockBrokenEvent)
    {
        World world = blockBrokenEvent.getWorld();
        final BlockPos pos = blockBrokenEvent.getPos();
        ChunkToProcess chunkToProcess = new ChunkToProcess(world, pos.getX() >> 4, pos.getZ() >> 4);
        JobProcessor.Instance.scheduleJob(JobType.BACKGROUND, chunkToProcess);
    }

}
