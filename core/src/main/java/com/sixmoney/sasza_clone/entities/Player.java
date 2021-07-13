package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.Utils;

public class Player extends Entity {
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    public Player() {
        position = new Vector2(0, 0);
        bbox = new Rectangle(position.x, position.y, 64, 64);
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        item = new Item<>(this);
        rotation = 0;
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
        bbox.x = position.x + (Constants.PLAYER_CENTER.x) / 2;
        bbox.y = position.y + (Constants.PLAYER_CENTER.y) / 2;
    }

    public void setRotation(Vector2 pointerPosition) {
        Vector2 playerCenter = new Vector2(position.x + Constants.PLAYER_CENTER.x, position.y + Constants.PLAYER_CENTER.y);
        Vector2 vec = pointerPosition.sub(playerCenter);
        vec.nor();
        rotation = vec.angleDeg() + 90;
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

    public void update(float delta) {
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
    }

    public void render(Batch batch) {
        Utils.drawTextureRegion(batch, Assets.get_instance().playerAssets.player, position.x, position.y, rotation);
    }

    public static class PlayerCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if (other.userData instanceof Crate) return Response.slide;
            else return null;
        }
    }
}


