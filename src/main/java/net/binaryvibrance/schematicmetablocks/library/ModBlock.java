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

package net.binaryvibrance.schematicmetablocks.library;

import cpw.mods.fml.common.registry.GameRegistry;
import net.binaryvibrance.schematicmetablocks.TheMod;
import net.binaryvibrance.schematicmetablocks.blocks.*;
import net.binaryvibrance.schematicmetablocks.tileentity.InteriorAirMarkerTileEntity;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModBlock
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    public static InteriorAirMarker blockInteriorAirMarker;
    public static ImplicitAirBlock blockImplicitAir;
    public static ExplicitAirBlock blockExplicitAir;
    public static OriginBlock blockOrigin;
    public static NullBlock blockNull;
    public static RegionBlock blockRegion;

    private ModBlock()
    {
        throw new AssertionError();
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(InteriorAirMarkerTileEntity.class, getTEName(blockInteriorAirMarker.NAME));
        GameRegistry.registerTileEntity(RegionTileEntity.class, getTEName(blockRegion.NAME));
    }

    private static String getTEName(String name) { return "tile." + name;}

    public static void init()
    {
        blockInteriorAirMarker = new InteriorAirMarker();
        blockImplicitAir = new ImplicitAirBlock();
        blockExplicitAir = new ExplicitAirBlock();
        blockOrigin = new OriginBlock();
        blockNull = new NullBlock();
        blockRegion = new RegionBlock();

        GameRegistry.registerBlock(blockInteriorAirMarker, blockInteriorAirMarker.NAME);
        GameRegistry.registerBlock(blockImplicitAir, blockImplicitAir.NAME);
        GameRegistry.registerBlock(blockExplicitAir, blockExplicitAir.NAME);
        GameRegistry.registerBlock(blockNull, blockNull.NAME);
        GameRegistry.registerBlock(blockOrigin, blockOrigin.NAME);
        GameRegistry.registerBlock(blockRegion, blockRegion.NAME);
    }
}
