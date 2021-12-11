package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.World;
import com.dongbat.walkable.PathHelper;
import com.dongbat.walkable.PathfinderException;

public abstract class BaseNPC extends Character {
    private static final String TAG = BaseNPC.class.getName();

    protected CollisionFilter npcCollisionFilter;
    protected CollisionFilter npcPlayerCollisionFilter;

    public BaseNPC(float x, float y) {
        super(x, y);
    }


    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBBox();
    }


    protected void applySteering(float delta) {
        if (!steerOutput.linear.isZero()) {
            acceleration = steerOutput.linear.scl(delta);
            if (acceleration.len() > maxLinearAcceleration) {
                acceleration.setLength(maxLinearAcceleration);
            }
            velocity.x += acceleration.x;
            velocity.y += acceleration.y;
            if (velocity.len() > maxLinearSpeed) {
                velocity.setLength(maxLinearSpeed);
            }
            position.mulAdd(velocity, delta);
        } else {
            velocity.set(0, 0);
        }
    }


    public void update(float delta, World<Entity> world, PathHelper pathHelper, Vector2 target) {
        world.querySegmentWithCoords(getPosition().x, getPosition().y, target.x, target.y, npcPlayerCollisionFilter, items);

        if (items.size() > 0 && items.get(0).item.userData instanceof Player && !prioritySteering.isEnabled()) {
            prioritySteering.setEnabled(true);
            pathSteering.setEnabled(false);
            path.clear();
            Gdx.app.log(TAG, "Toggled prioritySteering");
        } else if ((items.size() == 0 || !(items.get(0).item.userData instanceof Player)) && !pathSteering.isEnabled()) {
            prioritySteering.setEnabled(false);
            pathSteering.setEnabled(true);
            Gdx.app.log(TAG, "Toggled pathSteering");
        }

        if (prioritySteering.isEnabled()) {
            prioritySteering.calculateSteering(steerOutput);
        } else if (pathSteering.isEnabled()) {
            try {
                pathHelper.findPath(getPosition().x, getPosition().y, target.x, target.y, bbox.width * 1.1f, path);

            } catch (PathfinderException e) {
                Gdx.app.debug(TAG, "Pathfinding error: " + e.getMessage());
                Gdx.app.debug(TAG, "Location: " + position.toString());
                return;
            }

            if (path.size > 2 && path.size % 2 == 0) {
                Array<Vector2> waypoints = new Array<>();
                for (int i = 0; i < path.size; i += 2) {
                    waypoints.add(new Vector2(path.get(i), path.get(i + 1)));
                }

                LinePath<Vector2> newPath = new LinePath<>(waypoints, true);
                ((FollowPath<Vector2, LinePath.LinePathParam>) steeringBehaviors.get(2)).setPath(newPath);

                try {
                    pathSteering.calculateSteering(steerOutput);
                } catch (Exception e) {
                    Gdx.app.debug(TAG, "Path steering error: " + e.getMessage());
                }
            }
        }

        if (prioritySteering.isEnabled() || pathSteering.isEnabled()) {
            applySteering(delta);
            if (!velocity.isZero() && !shooting) {
                rotation = velocity.angleDeg();
            }
        }



        super.update(delta, world);
    }
}
