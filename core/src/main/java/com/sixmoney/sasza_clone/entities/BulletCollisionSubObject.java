package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.sixmoney.sasza_clone.utils.Constants;

public class BulletCollisionSubObject extends Entity {
    public Entity parent;

    public BulletCollisionSubObject(Entity parent) {
        super();
        this.parent = parent;
        position = new Vector2(parent.bbox.x - Constants.BBOX_BUFFER, parent.bbox.y - Constants.BBOX_BUFFER);
        bbox = new Rectangle(
                position.x,
                position.y,
                parent.bbox.width + Constants.BBOX_BUFFER * 2,
                parent.bbox.height + Constants.BBOX_BUFFER * 2
        );
        item = new Item<>(this);
        destructible = true;
    }

    @Override
    public float getHealth() {
        return parent.getHealth();
    }

    @Override
    public void decrementHealth(float value) {
        Gdx.app.log("TEAST", parent.health + "");
        parent.decrementHealth(value);
    }
}
