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
package com.b3dgs.lionengine.game.feature;

import com.b3dgs.lionengine.game.Feature;

/**
 * Recyclable marker.
 * <p>
 * Allows to recycle a {@link Featurable} and all its {@link Feature} when using
 * {@link Factory#create(com.b3dgs.lionengine.Media)} or {@link Factory#create(com.b3dgs.lionengine.Media, Class)}.
 * </p>
 * <p>
 * Should be used if object creation is too much time consuming, and only if reuse can be intensive (such as effects or
 * bullets).
 * </p>
 */
public interface Recyclable
{
    /**
     * Recycle feature, to make it ready for reuse.
     */
    void recycle();
}
