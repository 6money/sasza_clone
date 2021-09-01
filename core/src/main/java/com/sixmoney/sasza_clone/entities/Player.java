package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.ItemInfo;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Player extends Character {
    private static final String TAG = Player.class.getName();

    private Vector2 lazerVector;

    public float playerSpeed = 200;


    public Player(float x, float y) {
        super(x, y);
        position = new Vector2(x, y);
        entityTextureRegion = Assets.get_instance().playerAssets.player;
        entityAnimation = Assets.get_instance().playerAssets.playerWalkingAnimation;
        characterShootingTexture = Assets.get_instance().playerAssets.playerShooting;
        characterIdleLegTexture = Assets.get_instance().playerAssets.playerStand;
        lazerVector = new Vector2();
        health = 2000;
        maxHealth = 2000;
        destructible = true;
        showHealthBar = false;
    }


    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBBox();
    }


    public void setRotation(Vector2 pointerPosition) {
        Vector2 playerCenter = new Vector2(position.x + Constants.PLAYER_CENTER.x, position.y + Constants.PLAYER_CENTER.y);
        Vector2 vec = pointerPosition.sub(playerCenter);
        vec.nor();
        rotation = vec.angleDeg();
    }


    public void setVelocity(Vector2 newVelocityNormal) {
        oldVelocity.set(velocity);
        velocity.set(newVelocityNormal);
        velocity.setLength(playerSpeed);

        if (velocity.isZero() && velocity != oldVelocity) {
            animationStartTime = 0;
        } else if (oldVelocity.isZero() && !velocity.isZero()) {
            animationStartTime = TimeUtils.nanoTime();
        }
    }

    public void resetVelocity() {
        velocity.set(0, 0);
        animationStartTime = 0;
    }

    @Override
    public void update(float delta, World<Entity> world) {
        float rotationLower = rotation - Constants.PLAYER_BACK_PEDAL_ANGLE;
        float rotationUpper = rotation + Constants.PLAYER_BACK_PEDAL_ANGLE;

        if (rotation > 365 - Constants.PLAYER_BACK_PEDAL_ANGLE
                && velocity.angleDeg() < Constants.PLAYER_BACK_PEDAL_ANGLE) {
            rotationUpper = Constants.PLAYER_BACK_PEDAL_ANGLE - rotation;
            if (
                    (velocity.angleDeg() >= 0 && velocity.angleDeg() < rotationUpper)
                    || (velocity.angleDeg() <= 365 && velocity.angleDeg() < rotationLower )
            ) {
                velocity.setLength(playerSpeed);
            } else {
                velocity.setLength(playerSpeed - Constants.PLAYER_BACK_PEDAL_PENALTY);
            }
        } else {
            if (velocity.angleDeg() > rotationLower && velocity.angleDeg() < rotationUpper) {
                velocity.setLength(playerSpeed);
            } else {
                velocity.setLength(playerSpeed - Constants.PLAYER_BACK_PEDAL_PENALTY);
            }
        }

        position.mulAdd(velocity, delta);
        updateBBox();

        Response.Result result = world.move(item, bbox.x, bbox.y, new PlayerCollisionFilter());
        Collisions projectedCollisions = result.projectedCollisions;
        for (int i = 0; i < projectedCollisions.size(); i++) {
            Collision collision = projectedCollisions.get(i);
            if (collision.type == Response.slide) {
                Rect newPos = world.getRect(this.item);
                setPosition(newPos.x - (bbox.x - position.x), newPos.y - (bbox.y - position.y));
                break;
            }
        }

        ArrayList<ItemInfo> items = new ArrayList<>();
        lazerVector.set(1, 0);
        lazerVector.rotateDeg(rotation);
        lazerVector.setLength(currentGun.getRange());
        lazerVector.add(position.x + Constants.PLAYER_CENTER.x + bulletOffsetReal.x, position.y + Constants.PLAYER_CENTER.y + bulletOffsetReal.y);

        world.querySegmentWithCoords(
                position.x + Constants.PLAYER_CENTER.x + bulletOffsetReal.x,
                position.y + Constants.PLAYER_CENTER.y + bulletOffsetReal.y,
                lazerVector.x,
                lazerVector.y,
                new LazerCollisionFilter(),
                items
        );

        if (items.size() > 0) {
            lazerVector.set(items.get(0).x1, items.get(0).y1);
        }

        super.update(delta, world);
    }


    public void render(Batch batch, ShapeDrawer drawer) {
        drawer.line(position.x + Constants.PLAYER_CENTER.x + bulletOffsetReal.x, position.y + Constants.PLAYER_CENTER.y + bulletOffsetReal.y, lazerVector.x, lazerVector.y, new Color(1, 0, 0, 0.2f));
        super.render(batch);
    }


    public static class PlayerCollisionFilter implements CollisionFilter {
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

    public static class LazerCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if (((Entity) item.userData).bulletCollidable) {
                return Response.touch;
            } else {
                return null;
            }
        }
    }
}


