package net.binaryvibrance.schematicmetablocks.events;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class TileEntityEvent extends Event {
    private TileEntity tileEntity;

    public TileEntityEvent(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    public static class Added extends TileEntityEvent {

        public Added(TileEntity tileEntity) {
            super(tileEntity);
        }
    }

    public static class Removed extends TileEntityEvent {

        public Removed(TileEntity tileEntity) {
            super(tileEntity);
        }
    }
}
