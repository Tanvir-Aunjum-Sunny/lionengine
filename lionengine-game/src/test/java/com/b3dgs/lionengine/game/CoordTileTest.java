/*
 * Copyright (C) 2013-2018 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game;

import static com.b3dgs.lionengine.UtilAssert.assertEquals;

import org.junit.jupiter.api.Test;

import com.b3dgs.lionengine.game.feature.tile.map.pathfinding.CoordTile;

/**
 * Test {@link CoordTile}.
 */
public final class CoordTileTest
{
    /**
     * Test constructor.
     */
    @Test
    public void testConstructor()
    {
        final CoordTile coord = new CoordTile(1, 2);

        assertEquals(1, coord.getX());
        assertEquals(2, coord.getY());
    }
}
