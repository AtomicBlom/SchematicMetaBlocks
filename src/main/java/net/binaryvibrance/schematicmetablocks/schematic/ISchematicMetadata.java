package net.binaryvibrance.schematicmetablocks.schematic;

import net.minecraft.nbt.NBTTagCompound;

public interface ISchematicMetadata
{
    int getWidth();

    int getLength();

    int getHeight();

    NBTTagCompound getExtendedMetadata();
}
