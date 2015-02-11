package net.binaryvibrance.schematicmetablocks.jobs;

interface IJob
{
    void start();

    boolean represents(IJob otherJob);

    void notifyUpdatedJob();
}

