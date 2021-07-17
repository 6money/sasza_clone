package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;

public class Bullet extends Entity {
    private Vector2 target;
    private float speed;
    private float remainingDistance;
    private boolean dead;

    public Bullet(float x, float y, float rotation, float targetX, float targetY, float speed) {
        super();
        position = new Vector2(x, y);
        this.rotation = rotation;
        target = new Vector2(targetX, targetY);
        textureRegion = Assets.get_instance().playerAssets.rifleProjectile;
        this.speed = speed;
        dead = false;
    }

    public boolean getDead() {
        return dead;
    }

    public void update() {
        Vector2 temp = new Vector2(target);
        temp.sub(position);
        remainingDistance = temp.len();
        temp.nor();

        if (remainingDistance < speed) {
            dead = true;
            return;
        }

        temp.setLength(speed);
        position.add(temp);
    }
}
