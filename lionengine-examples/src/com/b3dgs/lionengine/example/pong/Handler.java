/*
 * Copyright (C) 2013 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.example.pong;

import java.util.Set;

import com.b3dgs.lionengine.Keyboard;
import com.b3dgs.lionengine.UtilityRandom;
import com.b3dgs.lionengine.core.UtilityMath;
import com.b3dgs.lionengine.game.Force;

/**
 * Handle the game, count score, and reset the ball in case of goal.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
final class Handler
{
    /** Screen width. */
    private final int screenWidth;
    /** Screen height. */
    private final int screenHeight;
    /** Rackets. */
    private final Set<Racket> rackets;
    /** Ball. */
    private final Ball ball;
    /** Score left. */
    private int scoreLeft;
    /** Score right. */
    private int scoreRight;

    /**
     * Constructor.
     * 
     * @param screenWidth The screen width.
     * @param screenHeight The screen height.
     * @param rackets The rackets reference.
     * @param ball The ball reference.
     */
    Handler(int screenWidth, int screenHeight, Set<Racket> rackets, Ball ball)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.rackets = rackets;
        this.ball = ball;
        scoreLeft = 0;
        scoreRight = 0;
    }

    /**
     * Set the ball speed.
     * 
     * @param speed The ball speed.
     */
    public void setBallSpeed(double speed)
    {
        ball.setSpeed(speed);
    }

    /**
     * Set the racket speed.
     * 
     * @param speed The racket speed.
     */
    public void setRacketSpeed(double speed)
    {
        for (final Racket racket : rackets)
        {
            racket.setSpeed(speed);
        }
    }

    /**
     * Start the game.
     */
    public void engage()
    {
        final int side = UtilityRandom.getRandomBoolean() ? 1 : -1;
        ball.setSpeed(ball.getSpeedInit());
        ball.setForces(ball.getSpeed() * side, 0.0);
        ball.setLocation(screenWidth / 2.0, screenHeight / 2.0);
    }

    /**
     * Update the ball and control the racket.
     * 
     * @param extrp The extrapolation value.
     * @param keyboard The keyboard reference.
     */
    public void update(double extrp, Keyboard keyboard)
    {
        // Update ball
        ball.update(extrp);

        // Update rackets
        for (final Racket racket : rackets)
        {
            racket.update(extrp, keyboard, ball);

            if (ball.collide(racket))
            {
                // Apply collision
                final Force force = ball.getForce();
                int side = 1;

                if (ball.getLocationX() < ball.getLocationOldX())
                {
                    ball.teleportX(racket.getLocationX() + racket.getWidth() + 1);
                }
                if (ball.getLocationX() > ball.getLocationOldX())
                {
                    ball.teleportX(racket.getLocationX() - racket.getWidth() - 1);
                    side = -1;
                }

                final double diff = (ball.getLocationY() - racket.getLocationY()) / racket.getHeight() / 2;
                final int angle = (int) Math.round(diff * 180);

                if (ball.getSpeed() < 5.0)
                {
                    ball.setSpeed(ball.getSpeed() + 0.3);
                }

                force.setForce(ball.getSpeed() * UtilityMath.cos(angle) * side,
                        ball.getSpeed() * UtilityMath.sin(angle));
            }
        }

        // Border collisions
        final int left = ball.getWidth() / 2;
        if (ball.getLocationX() < left)
        {
            scoreRight++;
            engage();
        }

        final int right = screenWidth - ball.getWidth() / 2;
        if (ball.getLocationX() > right)
        {
            scoreLeft++;
            engage();
        }

        final int top = ball.getHeight() / 2;
        if (ball.getLocationY() < top)
        {
            ball.setLocationY(top);
            ball.setForces(ball.getForce().getForceHorizontal(), -ball.getForce().getForceVertical());
        }

        final int bottom = screenHeight - ball.getHeight() / 2;
        if (ball.getLocationY() > bottom)
        {
            ball.setLocationY(bottom);
            ball.setForces(ball.getForce().getForceHorizontal(), -ball.getForce().getForceVertical());
        }
    }

    /**
     * Get the left score.
     * 
     * @return The left score.
     */
    public int getScoreLeft()
    {
        return scoreLeft;
    }

    /**
     * Get the left score.
     * 
     * @return The left score.
     */
    public int getScoreRight()
    {
        return scoreRight;
    }
}
