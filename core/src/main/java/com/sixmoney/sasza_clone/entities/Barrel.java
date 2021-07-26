package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.sixmoney.sasza_clone.utils.Assets;

public class Barrel extends Entity {

    public Barrel(float x, float y, String textureName) {
        super();
        entityTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        position = new Vector2(x, y);
        int bufferX = entityTextureRegion.getRegionWidth() / 20;
        int bufferY = entityTextureRegion.getRegionHeight() / 20;
        bbox = new Rectangle(position.x + bufferX, position.y + bufferY, entityTextureRegion.getRegionWidth() - bufferX, entityTextureRegion.getRegionHeight() - bufferY);
        item = new Item<>(this);
        health = 100;
        destructible = true;
    }
}
