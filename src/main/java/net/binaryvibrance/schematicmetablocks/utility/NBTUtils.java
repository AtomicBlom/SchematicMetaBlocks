package net.binaryvibrance.schematicmetablocks.utility;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public final class NBTUtils
{
    public static NBTTagCompound writeBlockPos(NBTTagCompound compound, BlockPos pos) {
        compound.setInteger("x", pos.getX());
        compound.setInteger("y", pos.getY());
        compound.setInteger("z", pos.getZ());
        return compound;
    }

    public static NBTTagCompound writeBlockPos(BlockPos pos) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("x", pos.getX());
        compound.setInteger("y", pos.getY());
        compound.setInteger("z", pos.getZ());
        return compound;
    }

    public static BlockPos readBlockPos(NBTTagCompound compound) {
        if (!compound.hasKey("x") || !compound.hasKey("y") || !compound.hasKey("z") ) {
            return null;
        }

        return new BlockPos(
                compound.getInteger("x"),
                compound.getInteger("y"),
                compound.getInteger("z")
        );
    }

    private NBTUtils() {}
}
