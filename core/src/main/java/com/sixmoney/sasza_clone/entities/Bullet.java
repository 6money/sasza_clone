package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;

public class Bullet extends Entity {
    private Vector2 target;
    private float speed;
    private float remainingDistance;
    private boolean dead;
    private boolean firstUpdate;

    public Bullet(float x, float y, float rotation, float targetX, float targetY, float speed) {
        super();
        position = new Vector2(x, y);
        this.rotation = rotation;
        target = new Vector2(targetX, targetY);
        entityTextureRegion = Assets.get_instance().playerAssets.rifleProjectile;
        this.speed = speed;
        dead = false;
        firstUpdate = true;
    }

    public boolean getDead() {
        return dead;
    }

    public void update(float delta) {
        if (firstUpdate) {
            firstUpdate = false;
            return;
        }

        Vector2 temp = new Vector2(target);
        temp.sub(position);
        remainingDistance = temp.len();
        temp.nor();

        if (remainingDistance == 0) {
            dead = true;
            return;
        }

        temp.setLength(Math.min(remainingDistance, speed * delta));
        position.add(temp);
    }
}
