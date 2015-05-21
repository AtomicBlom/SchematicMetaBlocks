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

package net.binaryvibrance.schematicmetablocks;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.binaryvibrance.schematicmetablocks.library.ModBlock;
import net.binaryvibrance.schematicmetablocks.library.ModItem;
import net.minecraft.item.ItemStack;

@SuppressWarnings("WeakerAccess")
public class NEISMBConfig implements IConfigureNEI
{

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    @Override
    public void loadConfig()
    {
        API.hideItem(new ItemStack(ModBlock.blockNull));
        API.hideItem(new ItemStack(ModBlock.blockImplicitAir));

        if (!TheMod.creatorMode) {
            API.hideItem(new ItemStack(ModBlock.blockExplicitAir));
            API.hideItem(new ItemStack(ModBlock.blockInteriorAirMarker));
            API.hideItem(new ItemStack(ModBlock.blockOrigin));
            API.hideItem(new ItemStack(ModBlock.blockRegion));
            API.hideItem(new ItemStack(ModItem.metaToolItem));
        }
    }

    @Override
    public String getName()
    {
        return TheMod.MOD_NAME;
    }

    @Override
    public String getVersion()
    {
        return TheMod.MOD_VERSION;
    }
}