package com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.scenery;

import java.io.IOException;

import com.b3dgs.lionengine.anim.AnimState;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.Context;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.Entity;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.TypeEntity;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.TypeEntityState;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.TypeState;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.patrol.Patrollable;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.patrol.PatrollerModel;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.patrol.TypePatrol;
import com.b3dgs.lionengine.file.FileReading;
import com.b3dgs.lionengine.file.FileWriting;
import com.b3dgs.lionengine.game.Force;
import com.b3dgs.lionengine.game.Movement;

/**
 * Beetle base implementation.
 */
public class EntityBeetle
        extends EntityScenery
        implements Patrollable
{
    /** Patrollable model. */
    protected final PatrollerModel patroller;
    /** Forces list used. */
    private final Force[] forces;
    /** Movement force. */
    private final Movement movement;
    /** Movement max speed. */
    private double movementSpeedMax;

    /**
     * Constructor.
     * 
     * @param context The context reference.
     * @param type The entity type.
     */
    EntityBeetle(Context context, TypeEntity type)
    {
        super(context, type);
        movement = new Movement();
        forces = new Force[]
        {
            movement.getForce()
        };
        patroller = new PatrollerModel(this);
    }

    /*
     * EntityScenery
     */

    @Override
    public void prepare()
    {
        super.prepare();
        patroller.prepare();
    }
    
    @Override
    public void hitThat(Entity entity)
    {
        if (!status.isState(TypeEntityState.TURN))
        {
            super.hitThat(entity);
        }
    }

    @Override
    public void save(FileWriting file) throws IOException
    {
        super.save(file);
        patroller.save(file);
    }

    @Override
    public void load(FileReading file) throws IOException
    {
        super.load(file);
        patroller.load(file);
    }

    @Override
    protected void handleActions(double extrp)
    {
        final TypeState state = status.getState();
        if (state == TypeEntityState.TURN)
        {
            movement.reset();
            if (getAnimState() == AnimState.FINISHED)
            {
                final int side = patroller.getSide();
                setSide(-side);
                mirror(side < 0);
                if (getPatrolType() == TypePatrol.HORIZONTAL)
                {
                    setMovementForce(movementSpeedMax * side, 0.0);
                    teleportX(getLocationIntX() + side);
                }
                else if (getPatrolType() == TypePatrol.VERTICAL)
                {
                    setMovementForce(0.0, movementSpeedMax * side);
                    teleportY(getLocationIntY() + side);
                }
            }
        }
        super.handleActions(extrp);
    }

    @Override
    protected void handleMovements(double extrp)
    {
        movement.update(extrp);
        super.handleMovements(extrp);
    }

    @Override
    protected Force[] getForces()
    {
        return forces;
    }

    @Override
    protected void onCollide(Entity entity)
    {
        // Nothing to do
    }

    @Override
    protected void onLostCollision()
    {
        // Nothing to do
    }

    /*
     * Patrollable
     */

    @Override
    public void setMovementForce(double fh, double fv)
    {
        movement.getForce().setForce(fh, fv);
        movement.setForceToReach(fh, fv);
    }

    @Override
    public void setMovementSpeedMax(double speed)
    {
        movementSpeedMax = speed;
    }

    @Override
    public double getMovementSpeedMax()
    {
        return movementSpeedMax;
    }

    @Override
    public double getForceHorizontal()
    {
        return movement.getForce().getForceHorizontal();
    }

    /*
     * Patroller
     */

    @Override
    public void enableMovement(TypePatrol type)
    {
        patroller.enableMovement(type);
    }

    @Override
    public void setSide(int side)
    {
        patroller.setSide(side);
    }

    @Override
    public void setPatrolType(TypePatrol movement)
    {
        patroller.setPatrolType(movement);
    }

    @Override
    public void setFirstMove(int firstMove)
    {
        patroller.setFirstMove(firstMove);
    }

    @Override
    public void setMoveSpeed(int speed)
    {
        patroller.setMoveSpeed(speed);
    }

    @Override
    public void setPatrolLeft(int left)
    {
        patroller.setPatrolLeft(left);
    }

    @Override
    public void setPatrolRight(int right)
    {
        patroller.setPatrolRight(right);
    }

    @Override
    public int getSide()
    {
        return patroller.getSide();
    }

    @Override
    public TypePatrol getPatrolType()
    {
        return patroller.getPatrolType();
    }

    @Override
    public int getFirstMove()
    {
        return patroller.getFirstMove();
    }

    @Override
    public int getMoveSpeed()
    {
        return patroller.getMoveSpeed();
    }

    @Override
    public int getPatrolLeft()
    {
        return patroller.getPatrolLeft();
    }

    @Override
    public int getPatrolRight()
    {
        return patroller.getPatrolRight();
    }

    @Override
    public int getPositionMin()
    {
        return patroller.getPositionMin();
    }

    @Override
    public int getPositionMax()
    {
        return patroller.getPositionMax();
    }

    @Override
    public boolean hasPatrol()
    {
        return patroller.hasPatrol();
    }

    @Override
    public boolean isPatrolEnabled()
    {
        return patroller.isPatrolEnabled();
    }

    @Override
    public boolean isPatrolEnabled(TypePatrol type)
    {
        return patroller.isPatrolEnabled(type);
    }
}
