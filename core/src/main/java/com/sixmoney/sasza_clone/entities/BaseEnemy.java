package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.dongbat.walkable.PathHelper;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class BaseEnemy extends BaseNPC {
    private static final String TAG = BaseEnemy.class.getName();

    protected float damage;

    private long attackDelayTimer;

    public BaseEnemy(float x, float y) {
        super(x, y);
        bbox = new Rectangle(x + Constants.PLAYER_CENTER.x * 0.80f, y + Constants.PLAYER_CENTER.y * 0.80f, MathUtils.round(Constants.PLAYER_CENTER.x / 3.5f), MathUtils.round(Constants.PLAYER_CENTER.y / 3.5f));
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
        npcCollisionFilter = new ZomCollisionFilter();
        npcPlayerCollisionFilter = new ZomPlayerCollisionFilter();
        prioritySteering.setEnabled(false);
        pathSteering.setEnabled(false);
        bulletCollisionSubObject = new BulletCollisionSubObject(this, Constants.BBOX_BUFFER_CHARACTERS);
        bulletCollisionSubObject.bulletCollidable = true;
    }


    @Override
    public void update(float delta, World<Entity> world, PathHelper pathHelper, Vector2 target) {
        super.update(delta, world, pathHelper, target);

        Response.Result result = world.move(item, bbox.x, bbox.y, npcCollisionFilter);
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
    }

    @Override
    protected void updateBBox() {
        bbox.x = position.x + Constants.PLAYER_CENTER.x * 0.85f;
        bbox.y = position.y + Constants.PLAYER_CENTER.y * 0.85f;

        if (bulletCollisionSubObject != null) {
            bulletCollisionSubObject.bbox.x = position.x + Constants.PLAYER_CENTER.x * 0.67f;
            bulletCollisionSubObject.bbox.y = position.y + Constants.PLAYER_CENTER.y * 0.67f;
        }
    }

    private void attack(Entity entity) {
        if (Utils.secondsSince(attackDelayTimer) >= Constants.ENEMY_ATTACK_SPEED || attackDelayTimer == 0) {
            attackDelayTimer = TimeUtils.nanoTime();
            entity.decrementHealth(damage);
        }
    }

    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);

        if (bulletCollisionSubObject != null && bulletCollidable) {
            drawer.rectangle(bulletCollisionSubObject.bbox, Color.ORANGE);
        }

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
            else if (item.userData instanceof BaseSoldier) return null;
            if (((Entity) item.userData).characterCollidable || ((Entity) item.userData).bulletCollidable) {
                return Response.touch;
            } else {
                return null;
            }
        }
    }
}
