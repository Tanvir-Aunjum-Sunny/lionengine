/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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

import java.util.HashMap;
import java.util.Map;

import com.b3dgs.lionengine.game.purview.Fabricable;

/**
 * It performs a list of {@link SetupGame} considering an input enumeration. This way it is possible to create new
 * instances of object related to their type by sharing the same data.
 * <p>
 * Sample implementation:
 * </p>
 * 
 * <pre>
 * public class Factory
 *         extends FactoryGame&lt;SetupGame, ObjectGame&gt;
 * {
 *     public Factory()
 *     {
 *         super();
 *     }
 * 
 *     &#064;Override
 *     protected SetupGame createSetup(Class&lt;? extends ObjectGame&gt; type)
 *     {
 *         return new SetupGame(Core.MEDIA.create(type.getSimpleName() + &quot;.xml&quot;));
 *     }
 * }
 * </pre>
 * 
 * @param <S> The setup type used.
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public abstract class FactoryGame<S extends SetupGame>
{
    /** Setups list. */
    private final Map<Class<? extends Fabricable>, S> setups;

    /**
     * Constructor.
     */
    public FactoryGame()
    {
        setups = new HashMap<>(4);
    }

    /**
     * Get setup instance from the type.
     * 
     * @param type The enum type.
     * @return The setup instance.
     */
    protected abstract S createSetup(Class<? extends Fabricable> type);

    /**
     * Clear all loaded setup and their configuration.
     */
    public void clear()
    {
        for (final S setup : setups.values())
        {
            setup.configurable.clear();
        }
        setups.clear();
    }

    /**
     * Get a setup reference from its type.
     * 
     * @param type The reference type.
     * @return The setup reference.
     */
    public S getSetup(Class<? extends Fabricable> type)
    {
        if (!setups.containsKey(type))
        {
            addSetup(type, createSetup(type));
        }
        return setups.get(type);
    }

    /**
     * Add a setup reference for the specified type.
     * 
     * @param type The class type.
     * @param setup The setup reference.
     */
    protected void addSetup(Class<? extends Fabricable> type, S setup)
    {
        setups.put(type, setup);
    }
}
