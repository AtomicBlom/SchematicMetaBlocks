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

import net.binaryvibrance.schematicmetablocks.utility.Reference;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(Reference.MOD_ID)
public final class ModBlock
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *
    public static final Block blockInteriorAirMarker;
    public static final Block blockImplicitAir;
    public static final Block blockExplicitAir;
    public static final Block blockOrigin;
    public static final Block blockNull;
    public static final Block blockRegion;

    private ModBlock() { }

    static {
        blockInteriorAirMarker = null;
        blockImplicitAir = null;
        blockExplicitAir = null;
        blockOrigin = null;
        blockNull = null;
        blockRegion = null;
    }

    public static void registerTileEntities()
    {
        //GameRegistry.registerTileEntity(InteriorAirMarkerTileEntity.class, getTEName(blockInteriorAirMarker.NAME));
        //GameRegistry.registerTileEntity(RegionTileEntity.class, getTEName(blockRegion.NAME));
    }



    public static void init()
    {
        /*blockInteriorAirMarker = new InteriorAirMarker();
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
        GameRegistry.registerBlock(blockRegion, blockRegion.NAME);*/
    }
}
