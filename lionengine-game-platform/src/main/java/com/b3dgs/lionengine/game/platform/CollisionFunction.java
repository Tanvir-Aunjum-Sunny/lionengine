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
package com.b3dgs.lionengine.game.platform;

import com.b3dgs.lionengine.game.Range;

/**
 * Describe the collision function used.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class CollisionFunction
{
    /** Value range. */
    private final Range range;
    /** The name. */
    private String name;
    /** The axis used. */
    private CollisionRefential axis;
    /** The input used. */
    private CollisionRefential input;
    /** Value. */
    private double value;
    /** Offset value. */
    private int offset;

    /**
     * Constructor.
     */
    public CollisionFunction()
    {
        range = new Range();
        axis = CollisionRefential.X;
        input = CollisionRefential.X;
        value = 0;
        offset = 0;
    }

    /**
     * Get the collision value by compute the current value with the collision function.
     * 
     * @param current The current value to use as input.
     * @return The collision function result.
     */
    public double computeCollision(int current)
    {
        return current * value + offset;
    }

    /**
     * Set the name.
     * 
     * @param name The name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Set the input type used.
     * 
     * @param axis The axis to set
     */
    public void setAxis(CollisionRefential axis)
    {
        this.axis = axis;
    }

    /**
     * Set the input type used.
     * 
     * @param input The input to set
     */
    public void setInput(CollisionRefential input)
    {
        this.input = input;
    }

    /**
     * Set the value.
     * 
     * @param value The value to set
     */
    public void setValue(double value)
    {
        this.value = value;
    }

    /**
     * Set the offset value.
     * 
     * @param offset The offset to set
     */
    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    /**
     * Set the function working range.
     * 
     * @param min The minimum value.
     * @param max The maximum value.
     */
    public void setRange(int min, int max)
    {
        range.setMin(min);
        range.setMax(max);
    }

    /**
     * Get the name.
     * 
     * @return The name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the axis.
     * 
     * @return the axis
     */
    public CollisionRefential getAxis()
    {
        return axis;
    }

    /**
     * Get the input.
     * 
     * @return the input
     */
    public CollisionRefential getInput()
    {
        return input;
    }

    /**
     * Get the value.
     * 
     * @return the value
     */
    public double getValue()
    {
        return value;
    }

    /**
     * Get the offset.
     * 
     * @return the offset
     */
    public int getOffset()
    {
        return offset;
    }

    /**
     * Get the working range.
     * 
     * @return The working range.
     */
    public Range getRange()
    {
        return range;
    }
}
