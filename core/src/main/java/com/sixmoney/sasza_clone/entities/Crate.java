package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;


public class Crate extends Entity {

    public Crate(float x, float y, String textureName) {
        super();
        enitiyTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        position = new Vector2(x, y);
        bbox = new Rectangle(
                position.x + Constants.BBOX_BUFFER,
                position.y + Constants.BBOX_BUFFER,
                enitiyTextureRegion.getRegionWidth() - Constants.BBOX_BUFFER * 2,
                enitiyTextureRegion.getRegionHeight() - Constants.BBOX_BUFFER * 2
        );
        health = 100;
        destructible = true;
        bulletCollisionSubObject = new BulletCollisionSubObject(this);
    }
}
