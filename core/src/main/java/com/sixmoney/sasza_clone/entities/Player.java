package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

public class Player extends Entity {
    private static final String TAG = Player.class.getName();

    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;
    private Vector2 lazerVector;
    private Vector2 bulletOffset;
    private Gun gun;

    public Player(float x, float y) {
        super();
        position = new Vector2(x, y);
        bbox = new Rectangle(position.x + Constants.PLAYER_CENTER.x, position.y + Constants.PLAYER_CENTER.y, Constants.PLAYER_CENTER.x / 2, Constants.PLAYER_CENTER.y / 2);
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        item = new Item<>(this);
        rotation = 0;
        textureRegion = Assets.get_instance().playerAssets.player;
        lazerVector = new Vector2();
        bulletOffset = new Vector2( -3, -18);
        gun = new Gun();
    }

    public Vector2 getPosition() {
        return position;
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

    public void startMove(String direction) {
        switch (direction) {
            case "UP":
                moveUp = true;
                break;
            case "DOWN":
                moveDown = true;
                break;
            case "LEFT":
                moveLeft = true;
                break;
            case "RIGHT":
                moveRight = true;
                break;
            default:
                break;
        }
    }

    public void stopMove(String direction) {
        switch (direction) {
            case "UP":
                moveUp = false;
                break;
            case "DOWN":
                moveDown = false;
                break;
            case "LEFT":
                moveLeft = false;
                break;
            case "RIGHT":
                moveRight = false;
                break;
            default:
                break;
        }
    }

    public void shoot() {
        gun.decrementCurrentMagazineAmmo();
    }

    public void update(float delta, World<Entity> world) {
        if (moveUp) {
            position.y += delta * Constants.PLAYER_SPEED;
        }
        if (moveDown) {
            position.y -= delta * Constants.PLAYER_SPEED;
        }
        if (moveLeft) {
            position.x -= delta * Constants.PLAYER_SPEED;
        }
        if (moveRight) {
            position.x += delta * Constants.PLAYER_SPEED;
        }

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
                CollisionFilter.defaultFilter,
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
            if (other.userData instanceof Crate) return Response.slide;
            if (other.userData instanceof FloorTile) return Response.slide;
            else return null;
        }
    }
}


