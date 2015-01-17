package net.binaryvibrance.schematicmetablocks.utility;

import net.minecraftforge.common.util.ForgeDirection;

public class BlockSideRotation
{
    //FIXME: This should be final
    private static ForgeDirection[][] ROTATION_MATRIX = {
            // Left, Right, Backwards, forwards, Bottom, Top
            //Down
            {ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UNKNOWN},
            //Up
            {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UNKNOWN},
            //North
            {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            //South
            {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            //West
            {ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
            //East
            {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
    };

    public static ForgeDirection forOrientation(TextureDirection direction, ForgeDirection orientation)
    {
        ForgeDirection[][] ROTATION_MATRIX = {
                // Left, Right, Backwards, forwards, Bottom, Top
                //Down
                {ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UNKNOWN},
                //Up
                {ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.UNKNOWN},
                //North
                {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
                //South
                {ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
                //West
                {ForgeDirection.SOUTH, ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
                //East
                {ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.DOWN, ForgeDirection.UP, ForgeDirection.UNKNOWN},
        };

        return ROTATION_MATRIX[orientation.ordinal()][direction.ordinal()];
    }

}
