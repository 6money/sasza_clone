package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.dongbat.walkable.PathHelper;
import com.dongbat.walkable.PathfinderException;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.Utils;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class BaseEnemy extends Character {
    private static final String TAG = BaseEnemy.class.getName();

    protected float damage;

    private long attackDelayTimer;
    private ZomCollisionFilter zomCollisionFilter;
    private ZomPlayerCollisionFilter zomPlayerCollisionFilter;

    public BaseEnemy(float x, float y) {
        super(x, y);
        position = new Vector2(x, y);
        entityTextureRegion = Assets.get_instance().enemyAssets.zom1;
        destructible = true;
        health = 100;
        maxHealth = 100;
        entityAnimation = Assets.get_instance().enemyAssets.enemyWalkingAnimation;
        deathAnimation = Assets.get_instance().enemyAssets.zom1DyingAnimation;
        characterIdleLegTexture = Assets.get_instance().enemyAssets.enemyStand;
        maxLinearAcceleration = 4000f;
        maxLinearSpeed = 150f;
        damage = 10;
        attackDelayTimer = 0;
        zomCollisionFilter = new ZomCollisionFilter();
        zomPlayerCollisionFilter = new ZomPlayerCollisionFilter();
        prioritySteering.setEnabled(false);
        pathSteering.setEnabled(false);
    }


    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBBox();
    }


    public void update(float delta, World<Entity> world, PathHelper pathHelper, Vector2 target) {
        world.querySegmentWithCoords(getPosition().x, getPosition().y, target.x, target.y, zomPlayerCollisionFilter, items);

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

                } catch (PathfinderException ignored) {
                    Gdx.app.debug(TAG, "Pathfinding error");
                    Gdx.app.debug(TAG, "Location: " + position.toString());
                    return;
                }

                if (path.size > 0) {
                    Array<Vector2> waypoints = new Array<>();
                    for (int i = 0; i < path.size; i += 2) {
                        waypoints.add(new Vector2(path.get(i), path.get(i + 1)));
                    }

                    LinePath<Vector2> newPath = new LinePath<>(waypoints, true);
                    ((FollowPath<Vector2, LinePath.LinePathParam>) steeringBehaviors.get(2)).setPath(newPath);
                }

            pathSteering.calculateSteering(steerOutput);
        }

        if (prioritySteering.isEnabled() || pathSteering.isEnabled()) {
            applySteering(delta);
            if (!velocity.isZero()) {
                rotation = velocity.angleDeg();
            }
        }

        Response.Result result = world.move(item, bbox.x, bbox.y, zomCollisionFilter);
        Collisions projectedCollisions = result.projectedCollisions;
        for (int i = 0; i < projectedCollisions.size(); i++) {
            Collision collision = projectedCollisions.get(i);
            if (collision.type == Response.slide) {
                setPosition(collision.touch.x - (bbox.x - position.x), collision.touch.y - (bbox.y - position.y));
            }

            if (((Entity) collision.other.userData).destructible && !(collision.other.userData instanceof BaseEnemy)) {
                attack((Entity) collision.other.userData);
            }
        }

        updateBBox();

        if (velocity.len() < 5) {
            animationStartTime = 0;
        } else if (velocity.len() >= 5 && animationStartTime == 0) {
            animationStartTime = TimeUtils.nanoTime();
        }

        super.update(delta, world);
    }

    private void attack(Entity entity) {
        if (Utils.secondsSince(attackDelayTimer) >= Constants.ENEMY_ATTACK_SPEED || attackDelayTimer == 0) {
            attackDelayTimer = TimeUtils.nanoTime();
            entity.decrementHealth(damage);
        }
    }

    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);

        if (pathSteering.isEnabled()) {
            LinePath<Vector2> path = (LinePath<Vector2>) ((FollowPath<Vector2, LinePath.LinePathParam>) steeringBehaviors.get(2)).getPath();

            for (LinePath.Segment<Vector2> segment : path.getSegments()) {
                drawer.line(segment.getBegin().x, segment.getBegin().y, segment.getEnd().x, segment.getEnd().y, Color.GOLD);
            }
        }
    }

    public static class ZomCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(other == null) return null;
            if (((Entity) other.userData).characterCollidable) {
                return Response.slide;
            } else {
                return null;
            }
        }
    }

    public static class ZomPlayerCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(item == null) return null;
            else if (item.userData instanceof BaseEnemy) return null;
            else if (item.userData instanceof BaseNPC) return null;
            if (((Entity) item.userData).characterCollidable || ((Entity) item.userData).bulletCollidable) {
                return Response.touch;
            } else {
                return null;
            }
        }
    }
}
