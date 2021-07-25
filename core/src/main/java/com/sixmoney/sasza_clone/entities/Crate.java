package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;


public class Crate extends Entity {

    public Crate(float x, float y, String textureName) {
        super();
        enitiyTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        position = new Vector2(x, y);
        health = 100;
        destructible = true;
        if (MathUtils.random(0, 1) == 0) {
            rotation = 0;
            bbox.set(
                    position.x + Constants.BBOX_BUFFER,
                    position.y + Constants.BBOX_BUFFER,
                    enitiyTextureRegion.getRegionWidth() - Constants.BBOX_BUFFER * 2,
                    enitiyTextureRegion.getRegionHeight() - Constants.BBOX_BUFFER * 2
            );
        } else {
            rotation = 90;
            bbox.set(
                    position.x + Constants.BBOX_BUFFER,
                    position.y + Constants.BBOX_BUFFER,
                    enitiyTextureRegion.getRegionHeight() - Constants.BBOX_BUFFER * 2,
                    enitiyTextureRegion.getRegionWidth() - Constants.BBOX_BUFFER * 2
            );
        }
        bulletCollisionSubObject = new BulletCollisionSubObject(this);
    }
}
