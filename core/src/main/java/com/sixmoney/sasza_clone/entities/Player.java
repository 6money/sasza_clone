package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.ItemInfo;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Player extends Character {
    private static final String TAG = Player.class.getName();

    private Vector2 lazerVector;
    private Vector2 bulletOffset;
    private Gun gun;


    public Player(float x, float y) {
        super();
        position = new Vector2(x, y);
        bbox = new Rectangle(position.x + Constants.PLAYER_CENTER.x * 3 / 4, position.y + Constants.PLAYER_CENTER.y * 3 / 4, Constants.PLAYER_CENTER.x / 2, Constants.PLAYER_CENTER.y / 2);
        item = new Item<>(this);
        enitiyTextureRegion = Assets.get_instance().playerAssets.player;
        entityAnimation = Assets.get_instance().playerAssets.playerWalkingAnimation;
        characterIdleLegTexture = Assets.get_instance().playerAssets.playerStand;
        lazerVector = new Vector2();
        bulletOffset = new Vector2( -3, -18);
        gun = new Gun();
        health = 200f;
        destructible = true;
        legsOffset = Constants.PLAYER_CENTER.x / 2;
    }


    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBBox();
    }


    private void updateBBox() {
        bbox.x = position.x + Constants.PLAYER_CENTER.x * 3 / 4;
        bbox.y = position.y + Constants.PLAYER_CENTER.y * 3 / 4;
    }


    public void setRotation(Vector2 pointerPosition) {
        Vector2 playerCenter = new Vector2(position.x + Constants.PLAYER_CENTER.x, position.y + Constants.PLAYER_CENTER.y);
        Vector2 vec = pointerPosition.sub(playerCenter);
        vec.nor();
        rotation = vec.angleDeg() + 90;
    }


    public Vector2 getBulletOffset() {
        return bulletOffset;
    }


    public Gun getGun() {
        return gun;
    }


    public void setVelocity(Vector2 newVelocityNormal) {
        oldVelocity.set(velocity);
        velocity.set(newVelocityNormal);
        velocity.setLength(Constants.PLAYER_SPEED);

        if (velocity.isZero() && velocity != oldVelocity) {
            animationStartTime = 0;
        } else if (oldVelocity.isZero() && !velocity.isZero()) {
            animationStartTime = TimeUtils.nanoTime();
        }
    }


    public void update(float delta, World<Entity> world) {
        position.mulAdd(velocity, delta);
        updateBBox();

        Response.Result result = world.move(item, bbox.x, bbox.y, new PlayerCollisionFilter());
        Collisions projectedCollisions = result.projectedCollisions;
        for (int i = 0; i < projectedCollisions.size(); i++) {
            Collision collision = projectedCollisions.get(i);
            if (collision.type == Response.slide) {
                setPosition(collision.touch.x - (bbox.x - position.x), collision.touch.y - (bbox.y - position.y));
            }
        }


        ArrayList<ItemInfo> items = new ArrayList<>();
        Vector2 bulletOffsetTemp = new Vector2(bulletOffset);
        bulletOffsetTemp.rotateDeg(rotation);
        lazerVector.set(0, -1);
        lazerVector.rotateDeg(rotation);
        lazerVector.setLength(gun.getRange());
        lazerVector.add(position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x, position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y);

        world.querySegmentWithCoords(
                position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x,
                position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y,
                lazerVector.x,
                lazerVector.y,
                new LazerCollisionFilter(),
                items
        );

        if (items.size() > 0) {
            lazerVector.set(items.get(0).x1, items.get(0).y1);
        }
    }


    public void render(Batch batch, ShapeDrawer drawer) {


        super.render(batch);


        Vector2 bulletOffsetTemp = new Vector2(bulletOffset);
        bulletOffsetTemp.rotateDeg(rotation);

        drawer.line(position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x, position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y, lazerVector.x, lazerVector.y, new Color(1, 0, 0, 0.2f));
    }


    public static class PlayerCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(other == null) return null;
            else if (other.userData instanceof Crate) return Response.slide;
            else if (other.userData instanceof FloorTile) return Response.slide;
            else if (other.userData instanceof Enemy) return Response.slide;
            else return null;
        }
    }

    public static class LazerCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if (!(item.userData instanceof FloorTile)) return Response.touch;
            else return null;
        }
    }
}


