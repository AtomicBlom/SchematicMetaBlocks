/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package net.binaryvibrance.schematica4worldgen.library;

import cpw.mods.fml.common.registry.GameRegistry;
import net.binaryvibrance.schematica4worldgen.TheMod;
import net.binaryvibrance.schematica4worldgen.blocks.InsideBlock;
import net.binaryvibrance.schematica4worldgen.blocks.S4WGBlock;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModBlock
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    public static final InsideBlock blockInside = new InsideBlock();

    private ModBlock()
    {
        throw new AssertionError();
    }

    public static void registerTileEntities()
    {
        /*GameRegistry.registerTileEntity(CupolaTE.class, getTEName(CupolaBlock.NAME));
        GameRegistry.registerTileEntity(PlotoniumChestTE.class, getTEName(PlotoniumChest.NAME));*/
    }

    private static String getTEName(String name) { return "tile." + name;}

    public static void init()
    {
        GameRegistry.registerBlock(blockInside, blockInside.NAME);
    }
}
