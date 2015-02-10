package net.binaryvibrance.schematicmetablocks.schematic;

import net.minecraft.nbt.NBTTagCompound;

public class WorldBlockCoord
{
    public final int x;
    public final int y;
    public final int z;

    public WorldBlockCoord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound origin = new NBTTagCompound();
        origin.setInteger("X", x);
        origin.setInteger("Y", y);
        origin.setInteger("Z", z);
        return origin;
    }

    public static WorldBlockCoord fromNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return null;
        }
        int x = nbt.getInteger("X");
        int y = nbt.getInteger("Y");
        int z = nbt.getInteger("Z");
        return new WorldBlockCoord(x, y, z);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final WorldBlockCoord that = (WorldBlockCoord) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public String toString()
    {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
