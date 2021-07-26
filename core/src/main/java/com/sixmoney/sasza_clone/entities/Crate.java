package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;


public class Crate extends Entity {

    public Crate(float x, float y, String textureName) {
        super();
        entityTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        position = new Vector2(x, y);
        health = 100;
        destructible = true;
        if (MathUtils.random(0, 1) == 0) {
            rotation = 0;
            bbox.set(
                    position.x + Constants.BBOX_BUFFER_ENVIRONMENT,
                    position.y + Constants.BBOX_BUFFER_ENVIRONMENT,
                    entityTextureRegion.getRegionWidth() - Constants.BBOX_BUFFER_ENVIRONMENT * 2,
                    entityTextureRegion.getRegionHeight() - Constants.BBOX_BUFFER_ENVIRONMENT * 2
            );
        } else {
            rotation = 90;
            bbox.set(
                    position.x + Constants.BBOX_BUFFER_ENVIRONMENT,
                    position.y + Constants.BBOX_BUFFER_ENVIRONMENT,
                    entityTextureRegion.getRegionHeight() - Constants.BBOX_BUFFER_ENVIRONMENT * 2,
                    entityTextureRegion.getRegionWidth() - Constants.BBOX_BUFFER_ENVIRONMENT * 2
            );
        }
        bulletCollisionSubObject = new BulletCollisionSubObject(this, true, Constants.BBOX_BUFFER_ENVIRONMENT);
    }
}
