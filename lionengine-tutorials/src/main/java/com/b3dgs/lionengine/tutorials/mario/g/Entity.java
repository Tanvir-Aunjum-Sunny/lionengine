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
package com.b3dgs.lionengine.tutorials.mario.g;

import java.util.EnumMap;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.game.EntityGame;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.Movement;
import com.b3dgs.lionengine.game.configurable.Configurable;
import com.b3dgs.lionengine.game.map.TileGame;
import com.b3dgs.lionengine.game.platform.entity.EntityPlatform;

/**
 * Abstract entity base implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
abstract class Entity
        extends EntityPlatform
{
    /** Map reference. */
    protected final Map map;
    /** Desired fps value. */
    protected final int desiredFps;
    /** Movement force. */
    protected final Movement movement;
    /** Movement jump force. */
    protected final Force jumpForce;
    /** Animations list. */
    private final EnumMap<EntityState, Animation> animations;
    /** Jump force. */
    protected double jumpForceValue;
    /** Movement max speed. */
    protected double movementSpeedValue;
    /** Key right state. */
    protected boolean right;
    /** Key left state. */
    protected boolean left;
    /** Key up state. */
    protected boolean up;
    /** Entity state. */
    protected EntityState state;
    /** Old state. */
    protected EntityState stateOld;
    /** Collision state. */
    protected EntityCollision coll;
    /** Dead flag. */
    protected boolean dead;

    /**
     * Constructor.
     * 
     * @param setup The setup reference.
     */
    protected Entity(SetupEntity setup)
    {
        super(setup);
        final ContextEntity context = setup.getContext(ContextEntity.class);
        map = context.map;
        desiredFps = context.desiredFps;
        animations = new EnumMap<>(EntityState.class);
        final Configurable configurable = setup.getConfigurable();
        jumpForceValue = configurable.getDouble("jumpSpeed", "data");
        movementSpeedValue = configurable.getDouble("movementSpeed", "data");
        movement = new Movement();
        jumpForce = new Force();
        state = EntityState.IDLE;
        setMass(configurable.getDouble("mass", "data"));
        setFrameOffsets(0, 1);
        setCollision(configurable.getCollision("default"));
        loadAnimations(configurable);
        addCollisionTile(EntityCollisionTileCategory.GROUND_CENTER, 0, 0);
        addCollisionTile(EntityCollisionTileCategory.KNEE_LEFT, -5, 9);
        addCollisionTile(EntityCollisionTileCategory.KNEE_RIGHT, 5, 9);
    }

    /**
     * Called when hit this entity.
     * 
     * @param entity The entity hit.
     */
    public abstract void onHitThat(Entity entity);

    /**
     * Called when get hurt.
     * 
     * @param entity Entity hitting this.
     */
    protected abstract void onHurtBy(EntityGame entity);

    /**
     * Check if hero can jump.
     * 
     * @return <code>true</code> if can jump, <code>false</code> else.
     */
    public boolean canJump()
    {
        return isOnGround();
    }

    /**
     * Check if hero is jumping.
     * 
     * @return <code>true</code> if jumping, <code>false</code> else.
     */
    public boolean isJumping()
    {
        return getLocationY() > getLocationOldY();
    }

    /**
     * Check if hero is falling.
     * 
     * @return <code>true</code> if falling, <code>false</code> else.
     */
    public boolean isFalling()
    {
        return getLocationY() < getLocationOldY();
    }

    /**
     * Check if entity is on ground.
     * 
     * @return <code>true</code> if on ground, <code>false</code> else.
     */
    public boolean isOnGround()
    {
        return coll == EntityCollision.GROUND;
    }

    /**
     * Check if entity is dead.
     * 
     * @return <code>true</code> if dead, <code>false</code> else.
     */
    public boolean isDead()
    {
        return dead;
    }

    /**
     * Called when horizontal collision occurred.
     */
    protected void onHorizontalCollision()
    {
        // Nothing by default
    }

    /**
     * Load all existing animations defined in the xml file.
     * 
     * @param configurable The configurable reference.
     */
    private void loadAnimations(Configurable configurable)
    {
        for (final EntityState state : EntityState.values())
        {
            try
            {
                animations.put(state, configurable.getAnimation(state.getAnimationName()));
            }
            catch (final LionEngineException exception)
            {
                continue;
            }
        }
    }

    /**
     * Check the map limit and apply collision if necessary.
     */
    private void checkMapLimit()
    {
        final int limitLeft = 0;
        if (getLocationX() < limitLeft)
        {
            setLocationX(limitLeft);
            movement.reset();
        }
        final int limitRight = map.getWidthInTile() * map.getTileWidth();
        if (getLocationX() > limitRight)
        {
            setLocationX(limitRight);
            movement.reset();
        }
    }

    /**
     * Update the forces depending of the pressed key.
     */
    private void updateForces()
    {
        movement.setForceToReach(Force.ZERO);
        final double speed;
        if (right && !left)
        {
            speed = movementSpeedValue;
        }
        else if (left && !right)
        {
            speed = -movementSpeedValue;
        }
        else
        {
            speed = 0.0;
        }
        movement.setForceToReach(speed, 0.0);

        if (up && canJump())
        {
            jumpForce.setForce(0.0, jumpForceValue);
            resetGravity();
            coll = EntityCollision.NONE;
            Sfx.JUMP.play();
        }
    }

    /**
     * Update entity states.
     */
    private void updateStates()
    {
        final double diffHorizontal = getDiffHorizontal();
        stateOld = state;

        if (diffHorizontal != 0.0)
        {
            mirror(diffHorizontal < 0.0);
        }

        final boolean mirror = getMirror();
        if (!isOnGround())
        {
            state = EntityState.JUMP;
        }
        else if (mirror && right && diffHorizontal < 0.0)
        {
            state = EntityState.TURN;
        }
        else if (!mirror && left && diffHorizontal > 0.0)
        {
            state = EntityState.TURN;
        }
        else if (diffHorizontal != 0.0)
        {
            state = EntityState.WALK;
        }
        else
        {
            state = EntityState.IDLE;
        }
        if (dead)
        {
            state = EntityState.DEAD;
        }
    }

    /**
     * Check the horizontal collision.
     * 
     * @param category The collision category.
     */
    private void checkHorizontal(EntityCollisionTileCategory category)
    {
        final TileGame tile = getCollisionTile(map, category);
        if (tile != null)
        {
            final Double x = tile.getCollisionX(this);
            if (applyHorizontalCollision(x))
            {
                movement.reset();
                onHorizontalCollision();
            }
        }
    }

    /**
     * Check the vertical collision.
     * 
     * @param category The collision category.
     */
    protected void checkVertical(EntityCollisionTileCategory category)
    {
        final TileGame tile = getCollisionTile(map, category);
        if (tile != null)
        {
            final Double y = tile.getCollisionY(this);
            if (applyVerticalCollision(y))
            {
                jumpForce.setForce(Force.ZERO);
                resetGravity();
                coll = EntityCollision.GROUND;
            }
            else
            {
                coll = EntityCollision.NONE;
            }
        }
    }

    /*
     * EntityPlatform
     */

    @Override
    protected void handleActions(double extrp)
    {
        if (!dead)
        {
            updateForces();
        }
        updateStates();
    }

    @Override
    protected void handleMovements(double extrp)
    {
        movement.update(extrp);
        updateGravity(extrp, desiredFps, jumpForce, movement.getForce());
        updateMirror();
    }

    @Override
    protected void handleCollisions(double extrp)
    {
        checkMapLimit();
        coll = EntityCollision.NONE;

        // Horizontal collision
        if (getDiffHorizontal() < 0)
        {
            checkHorizontal(EntityCollisionTileCategory.KNEE_LEFT);
        }
        else if (getDiffHorizontal() > 0)
        {
            checkHorizontal(EntityCollisionTileCategory.KNEE_RIGHT);
        }

        // Vertical collision
        if (getDiffVertical() < 0 || isOnGround())
        {
            checkVertical(EntityCollisionTileCategory.GROUND_CENTER);
        }
    }

    @Override
    protected void handleAnimations(double extrp)
    {
        // Assign an animation for each state
        if (state == EntityState.WALK)
        {
            setAnimSpeed(Math.abs(movement.getForce().getForceHorizontal()) / 12.0);
        }
        // Play the assigned animation
        if (stateOld != state)
        {
            play(animations.get(state));
        }
        updateAnimation(extrp);
    }
}
