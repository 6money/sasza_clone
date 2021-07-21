package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.sixmoney.sasza_clone.utils.Assets;


public class Crate extends Entity {

    public Crate(float x, float y) {
        super();
        enitiyTextureRegion = Assets.get_instance().envronmentAssets.box2;
        position = new Vector2(x, y);
        bbox = new Rectangle(position.x, position.y, enitiyTextureRegion.getRegionWidth(), enitiyTextureRegion.getRegionHeight());
        item = new Item<>(this);
        health = 100;
        destructible = true;
    }
}
