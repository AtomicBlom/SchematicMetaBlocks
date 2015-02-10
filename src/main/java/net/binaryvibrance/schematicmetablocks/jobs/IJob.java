package net.binaryvibrance.schematicmetablocks.jobs;

import net.minecraft.world.World;

interface IJob
{
    void start();

    boolean represents(IJob otherJob);

    void notifyUpdatedJob();
}

