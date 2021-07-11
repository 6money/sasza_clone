package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;

public class Player {
    private Vector2 position;

    public Player() {
        position = new Vector2(0, 0);
    }

    public void move(int distance, String direction) {
        switch (direction) {
            case "UP":
                position.y += distance;
                break;
            case "DOWN":
                position.y -= distance;
                break;
            case "LEFT":
                position.x -= distance;
                break;
            case "RIGHT":
                position.x += distance;
                break;
            default:
                break;
        }
    }

    public void update() {

    }

    public void render(Batch batch) {
        batch.draw(Assets.get_instance().playerAssets.playerPlaceholder, position.x, position.y);
    }
}
