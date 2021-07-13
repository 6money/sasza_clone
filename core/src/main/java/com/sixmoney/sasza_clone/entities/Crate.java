package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.sixmoney.sasza_clone.utils.Assets;


public class Crate extends Entity {

    public Crate() {
        super();
        textureRegion = Assets.get_instance().envronmentAssets.box2;
        position = new Vector2(-80, 0);
        bbox = new Rectangle(position.x, position.y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
        item = new Item<>(this);
    }
}