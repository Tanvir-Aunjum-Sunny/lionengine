/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.game.map;

import java.util.Collection;

/**
 * Tile representation with the following data:
 * <ul>
 * <li><code>objects id</code> : current objects id located over the tile</li>
 * <li><code>blocking</code> : flag to know if tile can block path</li>
 * </ul>
 * <p>
 * This allows to know easily which objects are on tile.
 * </p>
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public interface TilePath
        extends TileFeature
{
    /**
     * Add an object id over this tile.
     * 
     * @param id The object id reference to add.
     */
    void addObjectId(Integer id);

    /**
     * Remove an object id from this tile.
     * 
     * @param id The object id reference to remove.
     */
    void removeObjectId(Integer id);

    /**
     * Set the tile path category.
     * 
     * @param category The category name.
     */
    void setCategory(String category);

    /**
     * Get the objects id over this tile.
     * 
     * @return The objects id over this tile.
     */
    Collection<Integer> getObjectsId();

    /**
     * Get the category name.
     * 
     * @return The category name.
     */
    String getCategory();
}