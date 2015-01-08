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
import net.binaryvibrance.schematicmetablocks.items.ItemFiddler;

@SuppressWarnings({"UtilityClass", "WeakerAccess"})
@GameRegistry.ObjectHolder(TheMod.MOD_ID)
public final class ModItem
{
    // *******
    // * NOTE: @GameRegistry.ObjectHolder requires these fields to have the same name as the unlocalized name of the
    // *       object.
    // *

    public static final ItemFiddler fiddler = new ItemFiddler();

    private ModItem()
    {
        throw new AssertionError();
    }

    public static void init()
    {
        registerArtifacts();
    }


    private static void registerArtifacts()
    {
        GameRegistry.registerItem(fiddler, Names.FIDDLER);
    }

    public enum Names
    {
        INSTANCE;

        // Artifacts
        public static final String FIDDLER = "fiddler";
    }
}
