package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;

public class BulletCollisionSubObject extends Entity {
    public Entity parent;

    public BulletCollisionSubObject(Entity parent, boolean destructible, float buffer) {
        super();
        this.parent = parent;
        position = new Vector2(parent.bbox.x - buffer, parent.bbox.y - buffer);
        bbox = new Rectangle(
                position.x,
                position.y,
                parent.bbox.width + buffer * 2,
                parent.bbox.height + buffer * 2
        );
        item = new Item<>(this);
        this.destructible = destructible;
    }

    @Override
    public float getHealth() {
        return parent.getHealth();
    }

    @Override
    public void decrementHealth(float value) {
        parent.decrementHealth(value);
    }
}
