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
package com.b3dgs.lionengine.game.platform.entity;

import java.util.HashMap;

import com.b3dgs.lionengine.anim.AnimState;
import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.anim.Animator;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.SpriteAnimated;
import com.b3dgs.lionengine.game.CameraGame;
import com.b3dgs.lionengine.game.CoordTile;
import com.b3dgs.lionengine.game.EntityGame;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.SetupSurfaceGame;
import com.b3dgs.lionengine.game.configurable.Configurable;
import com.b3dgs.lionengine.game.configurable.FramesData;
import com.b3dgs.lionengine.game.configurable.SizeData;
import com.b3dgs.lionengine.game.map.CollisionTile;
import com.b3dgs.lionengine.game.map.CollisionTileCategory;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.TileGame;
import com.b3dgs.lionengine.game.purview.Body;
import com.b3dgs.lionengine.game.purview.model.BodyModel;

/**
 * Abstract and standard entity used for platform games. It already supports gravity, animation and collisions.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public abstract class EntityPlatform
        extends EntityGame
        implements Animator, Body
{
    /** Animation surface. */
    protected final SpriteAnimated sprite;
    /** Body object reference. */
    private final Body body;
    /** List of declared tile collision point. */
    private final HashMap<CollisionTileCategory, CoordTile> tileCollisions;
    /** Collisions special offsets x. */
    private int collOffX;
    /** Collisions special offsets y. */
    private int collOffY;
    /** Frame offsets x. */
    private int frameOffsetX;
    /** Frame offsets y. */
    private int frameOffsetY;
    /** Old collision y. */
    private double locationBeforeCollisionOldY;
    /** Last collision y. */
    private double locationBeforeCollisionY;

    /**
     * Constructor.
     * <p>
     * It needs in its config file the frame description:
     * </p>
     * 
     * <pre>
     * {@code
     * <entity>
     *     <frames horizontal="" vertical=""/>
     *     <size width="" height=""/>
     * </entity>
     * }
     * </pre>
     * 
     * @param setup The entity setup.
     */
    public EntityPlatform(SetupSurfaceGame setup)
    {
        super(setup);
        tileCollisions = new HashMap<>(1);
        body = new BodyModel(this);
        final Configurable configurable = setup.getConfigurable();
        final FramesData framesData = configurable.getFrames();
        final SizeData sizeData = configurable.getSize();
        sprite = Drawable.loadSpriteAnimated(setup.surface, framesData.getHorizontal(), framesData.getVertical());
        frameOffsetX = 0;
        frameOffsetY = 0;
        setSize(sizeData.getWidth(), sizeData.getHeight());
    }

    /**
     * Update actions, such as movements and attacks.
     * 
     * @param extrp The extrapolation value.
     */
    protected abstract void handleActions(final double extrp);

    /**
     * Update movement, depending of actions.
     * 
     * @param extrp The extrapolation value.
     */
    protected abstract void handleMovements(final double extrp);

    /**
     * Update collisions, after movements. Should be used to call
     * {@link #getCollisionTile(MapTile, CollisionTileCategory)} for each collision test.
     * <p>
     * Example:
     * </p>
     * 
     * <pre>
     * &#064;Override
     * protected void handleCollisions(double extrp)
     * {
     *     // Check something here
     *     // ...
     * 
     *     // Horizontal collision
     *     if (getDiffHorizontal() &lt; 0)
     *     {
     *         checkHorizontal(EntityCollisionTileCategory.KNEE_LEFT);
     *     }
     *     else if (getDiffHorizontal() &gt; 0)
     *     {
     *         checkHorizontal(EntityCollisionTileCategory.KNEE_RIGHT);
     *     }
     * 
     *     // Vertical collision
     *     if (getDiffVertical() &lt; 0 || isOnGround())
     *     {
     *         checkVertical(EntityCollisionTileCategory.LEG_LEFT);
     *         checkVertical(EntityCollisionTileCategory.LEG_RIGHT);
     *         checkVertical(EntityCollisionTileCategory.GROUND_CENTER);
     *     }
     * }
     * </pre>
     * 
     * @param extrp The extrapolation value.
     */
    protected abstract void handleCollisions(final double extrp);

    /**
     * Update animations, corresponding to a movement.
     * 
     * @param extrp The extrapolation value.
     */
    protected abstract void handleAnimations(final double extrp);

    /**
     * Set frame offsets (offsets on rendering).
     * 
     * @param frameOffsetX The horizontal offset.
     * @param frameOffsetY The vertical offset.
     */
    public void setFrameOffsets(int frameOffsetX, int frameOffsetY)
    {
        this.frameOffsetX = frameOffsetX;
        this.frameOffsetY = frameOffsetY;
    }

    /**
     * Get real horizontal speed (calculated on differential location x).
     * 
     * @return The real speed.
     */
    public double getDiffHorizontal()
    {
        return getLocationX() - getLocationOldX();
    }

    /**
     * Get real vertical speed (calculated on differential location y).
     * 
     * @return The real speed.
     */
    public double getDiffVertical()
    {
        return getLocationY() - getLocationOldY();
    }

    /**
     * Check if entity is going up.
     * 
     * @return <code>true</code> if going up, <code>false</code> else.
     */
    public boolean isGoingUp()
    {
        return locationBeforeCollisionY > locationBeforeCollisionOldY;
    }

    /**
     * Check if entity is going down.
     * 
     * @return <code>true</code> if going down, <code>false</code> else.
     */
    public boolean isGoingDown()
    {
        return locationBeforeCollisionY < locationBeforeCollisionOldY;
    }

    /**
     * Apply an horizontal collision using the specified blocking x value.
     * 
     * @param x The blocking x value.
     * @return <code>true</code> if collision where applied.
     */
    public boolean applyHorizontalCollision(Double x)
    {
        if (x != null)
        {
            teleportX(x.doubleValue());
            return true;
        }
        return false;
    }

    /**
     * Apply a vertical collision using the specified blocking y value.
     * 
     * @param y The blocking y value.
     * @return <code>true</code> if collision where applied.
     */
    public boolean applyVerticalCollision(Double y)
    {
        if (y != null)
        {
            locationBeforeCollisionOldY = locationBeforeCollisionY;
            locationBeforeCollisionY = getLocationY();
            teleportY(y.doubleValue());
            return true;
        }
        return false;
    }

    /**
     * Render an animated sprite from the entity location, following camera view point.
     * 
     * @param g The graphics output.
     * @param sprite The sprite to render.
     * @param camera The camera reference.
     */
    public void renderAnim(Graphic g, SpriteAnimated sprite, CameraGame camera)
    {
        renderAnim(g, sprite, camera, 0, 0);
    }

    /**
     * Render an animated sprite from the entity location, following camera view point.
     * 
     * @param g The graphics output.
     * @param sprite The sprite to render.
     * @param camera The camera reference.
     * @param rx The horizontal rendering offset.
     * @param ry The vertical rendering offset.
     */
    public void renderAnim(Graphic g, SpriteAnimated sprite, CameraGame camera, int rx, int ry)
    {
        final int x = camera.getViewpointX(getLocationIntX() - sprite.getFrameWidth() / 2 - frameOffsetX);
        final int y = camera.getViewpointY(getLocationIntY() + sprite.getFrameHeight() + frameOffsetY);
        sprite.render(g, x + rx, y + ry);
    }

    /**
     * Define a tile collision at a specific offset from the entity referential.
     * 
     * @param <C> The collision type used.
     * @param type The collision tile type.
     * @param offsetX The horizontal offset value.
     * @param offsetY The vertical offset value.
     */
    protected <C extends Enum<C> & CollisionTile> void addCollisionTile(CollisionTileCategory type, int offsetX,
            int offsetY)
    {
        tileCollisions.put(type, new CoordTile(offsetX, offsetY));
    }

    /**
     * Get the collision offset.
     * 
     * @param <C> The collision type used.
     * @param type The collision category.
     * @return The collision offset.
     */
    protected <C extends Enum<C> & CollisionTile> CoordTile getCollisionTileOffset(CollisionTileCategory type)
    {
        return tileCollisions.get(type);
    }

    /**
     * Get the first tile hit for the specified collision tile category matching the collision list.
     * 
     * @param <T> The tile type used.
     * @param <M> The map tile platform used.
     * @param map The map reference.
     * @param category The collision tile category.
     * @return The first tile hit, <code>null</code> if none.
     */
    public <T extends TileGame, M extends MapTile<T>> T getCollisionTile(M map, CollisionTileCategory category)
    {
        final CoordTile offsets = tileCollisions.get(category);
        if (offsets != null)
        {
            collOffX = offsets.getX();
            collOffY = offsets.getY();

            final T tile = map.getFirstTileHit(this, category.getCollisions(), true);
            return tile;
        }
        return null;
    }

    /*
     * EntityGame
     */

    /**
     * Main update routine. By default it calls theses functions in this order:
     * <ul>
     * <li>{@link #handleActions(double extrp)}</li>
     * <li>{@link #handleMovements(double extrp)}</li>
     * <li>{@link #handleCollisions(double extrp)}</li>
     * <li>{@link #handleAnimations(double extrp)}</li>
     * </ul>
     * 
     * @param extrp The extrapolation value.
     */
    @Override
    public void update(double extrp)
    {
        handleActions(extrp);
        handleMovements(extrp);
        handleCollisions(extrp);
        collOffX = 0;
        collOffY = 0;
        updateCollision();
        handleAnimations(extrp);
    }

    @Override
    public void render(Graphic g, CameraGame camera)
    {
        renderAnim(g, sprite, camera);
    }

    @Override
    public void updateMirror()
    {
        super.updateMirror();
        sprite.setMirror(getMirror());
    }

    @Override
    public double getLocationX()
    {
        return super.getLocationX() + collOffX;
    }

    @Override
    public double getLocationY()
    {
        return super.getLocationY() + collOffY;
    }

    @Override
    public double getLocationOldX()
    {
        return super.getLocationOldX() + collOffX;
    }

    @Override
    public double getLocationOldY()
    {
        return super.getLocationOldY() + collOffY;
    }

    @Override
    public int getLocationIntX()
    {
        return super.getLocationIntX() + collOffX;
    }

    @Override
    public int getLocationIntY()
    {
        return super.getLocationIntY() + collOffY;
    }

    @Override
    public void teleport(double x, double y)
    {
        super.teleport(x - collOffX, y - collOffY);
    }

    @Override
    public void teleportX(double x)
    {
        super.teleportX(x - collOffX);
    }

    @Override
    public void teleportY(double y)
    {
        super.teleportY(y - collOffY);
    }

    /*
     * Animator
     */

    @Override
    public void play(Animation anim)
    {
        sprite.play(anim);
    }

    @Override
    public void setAnimSpeed(double speed)
    {
        sprite.setAnimSpeed(speed);
    }

    @Override
    public void updateAnimation(double extrp)
    {
        sprite.updateAnimation(extrp);
    }

    @Override
    public int getFrame()
    {
        return sprite.getFrame();
    }

    @Override
    public int getFrameAnim()
    {
        return sprite.getFrameAnim();
    }

    @Override
    public void stopAnimation()
    {
        sprite.stopAnimation();
    }

    @Override
    public AnimState getAnimState()
    {
        return sprite.getAnimState();
    }

    @Override
    public void setFrame(int frame)
    {
        sprite.setFrame(frame);
    }

    /*
     * Body
     */

    @Override
    public void updateGravity(double extrp, int desiredFps, Force... forces)
    {
        body.updateGravity(extrp, desiredFps, forces);
    }

    @Override
    public void resetGravity()
    {
        body.resetGravity();
    }

    @Override
    public void invertAxisY(boolean state)
    {
        body.invertAxisY(state);
    }

    @Override
    public void setGravityMax(double max)
    {
        body.setGravityMax(max);
    }

    @Override
    public void setMass(double mass)
    {
        body.setMass(mass);
    }

    @Override
    public double getMass()
    {
        return body.getMass();
    }

    @Override
    public double getWeight()
    {
        return body.getWeight();
    }
}
