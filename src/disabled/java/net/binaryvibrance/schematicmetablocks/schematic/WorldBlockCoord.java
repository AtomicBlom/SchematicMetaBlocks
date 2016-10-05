package net.binaryvibrance.schematicmetablocks.schematic;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldBlockCoord
{
    public final int x;
    public final int y;
    public final int z;
    public final int chunkX;
    public final int chunkZ;

    public WorldBlockCoord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.chunkX = x >> 4;
        this.chunkZ = z >> 4;
    }

    public static WorldBlockCoord fromNBT(NBTTagCompound nbt)
    {
        if (nbt == null)
        {
            return null;
        }
        int x = nbt.getInteger("X");
        int y = nbt.getInteger("Y");
        int z = nbt.getInteger("Z");
        return new WorldBlockCoord(x, y, z);
    }

    public WorldBlockCoord offset(int x, int y, int z) {
        return new WorldBlockCoord(this.x + x, this.y + y, this.z + z);
    }

    public WorldBlockCoord offset(WorldBlockCoord other) {
        return new WorldBlockCoord(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public WorldBlockCoord offset(ForgeDirection direction) {
        return new WorldBlockCoord(this.x + direction.offsetX, this.y + direction.offsetY, this.z + direction.offsetZ);
    }

    public Block getBlock(IBlockAccess blockAccess) {
        return blockAccess.getBlock(x, y, z);
    }

    public void setBlock(World world, Block block) {
        world.setBlock(x, y, z, block);
    }

    public TileEntity getTileEntity(IBlockAccess blockAccess) {
        return blockAccess.getTileEntity(x, y, z);
    }

    public NBTTagCompound toNBT()
    {
        NBTTagCompound origin = new NBTTagCompound();
        origin.setInteger("X", x);
        origin.setInteger("Y", y);
        origin.setInteger("Z", z);
        return origin;
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
    public String toString()
    {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
