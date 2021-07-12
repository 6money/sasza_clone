package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;

public class Player {
    private Vector2 position;
    private int distance;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    public Player() {
        position = new Vector2(0, 0);
        distance = 10;
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public void update() {
        if (moveUp) {
            position.y += distance;
        }
        if (moveDown) {
            position.y -= distance;
        }
        if (moveLeft) {
            position.x -= distance;
        }
        if (moveRight) {
            position.x += distance;
        }
    }

    public void render(Batch batch) {
        batch.draw(Assets.get_instance().playerAssets.player, position.x, position.y);
    }
}
