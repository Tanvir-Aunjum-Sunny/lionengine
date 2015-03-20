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
package com.b3dgs.lionengine.example.game.effect;

import com.b3dgs.lionengine.Origin;
import com.b3dgs.lionengine.UtilRandom;
import com.b3dgs.lionengine.Viewer;
import com.b3dgs.lionengine.anim.AnimState;
import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.core.Core;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.core.Renderable;
import com.b3dgs.lionengine.core.Updatable;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.SpriteAnimated;
import com.b3dgs.lionengine.game.configurer.ConfigAnimations;
import com.b3dgs.lionengine.game.configurer.ConfigFrames;
import com.b3dgs.lionengine.game.object.ObjectGame;
import com.b3dgs.lionengine.game.object.Services;
import com.b3dgs.lionengine.game.object.SetupSurface;
import com.b3dgs.lionengine.game.trait.transformable.Transformable;
import com.b3dgs.lionengine.game.trait.transformable.TransformableModel;

/**
 * Effect base implementation.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
class Effect
        extends ObjectGame
        implements Updatable, Renderable
{
    /** Explode media. */
    public static final Media EXPLODE = Core.MEDIA.create("Explode.xml");

    /** Surface. */
    private final SpriteAnimated surface;
    /** Explode animation. */
    private final Animation animExplode;
    /** The viewer reference. */
    private final Viewer viewer;
    /** Transformable model. */
    private Transformable transformable;

    /**
     * Constructor.
     * 
     * @param setup the setup reference.
     * @param context The context reference.
     */
    public Effect(SetupSurface setup, Services context)
    {
        super(setup, context);

        final ConfigFrames configFrames = ConfigFrames.create(setup.getConfigurer());
        final int scale = UtilRandom.getRandomInteger(75) + 50;
        surface = Drawable.loadSpriteAnimated(setup.surface, configFrames.getHorizontal(), configFrames.getVertical());
        surface.stretch(scale, scale);
        surface.setOrigin(Origin.MIDDLE);

        final ConfigAnimations configAnimations = ConfigAnimations.create(setup.getConfigurer());
        animExplode = configAnimations.getAnimation("explode");

        viewer = context.get(Viewer.class);

        addTrait(TransformableModel.class);
    }

    @Override
    protected void prepareTraits()
    {
        transformable = getTrait(Transformable.class);
        transformable.setSize(surface.getFrameWidth(), surface.getFrameHeight());
    }

    /**
     * Start the effect at the specified location.
     * 
     * @param x The horizontal location.
     * @param y The vertical location.
     */
    public void start(double x, double y)
    {
        transformable.teleport(x, y);
        surface.setLocation(viewer.getViewpointX(transformable.getX()), viewer.getViewpointY(transformable.getY()));
        surface.play(animExplode);
    }

    @Override
    public void update(double extrp)
    {
        surface.update(extrp);
        if (surface.getAnimState() == AnimState.FINISHED)
        {
            destroy();
        }
    }

    @Override
    public void render(Graphic g)
    {
        if (viewer.isViewable(surface, 0, 0))
        {
            surface.render(g);
        }
    }
}
