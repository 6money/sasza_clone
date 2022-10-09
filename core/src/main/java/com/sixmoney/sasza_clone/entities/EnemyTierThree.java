package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;

public class EnemyTierThree extends BaseEnemy {
    public EnemyTierThree(float x, float y) {
        super(x, y);
        bbox = new Rectangle(x + 38, y + 38, 18, 18);
        bulletCollisionSubObject.updateSize(Constants.BBOX_BUFFER_CHARACTERS);
        entityTextureRegion = Assets.get_instance().enemyAssets.zom3;
        entityAnimation = Assets.get_instance().enemyAssets.enemyWalkingAnimation;
        deathAnimation = Assets.get_instance().enemyAssets.zom3DyingAnimation;
        characterIdleLegTexture = Assets.get_instance().enemyAssets.enemyStand;

        health = 1000;
        maxHealth = 1000;
        maxLinearSpeed = 10f;
        damage = 40;
        stunLimit = 300;
    }

    @Override
    protected void updateBBox() {
        bbox.x = position.x + 23;
        bbox.y = position.y + 23;

        if (bulletCollisionSubObject != null) {
            bulletCollisionSubObject.bbox.x = position.x + Constants.PLAYER_CENTER.x * 0.53f;
            bulletCollisionSubObject.bbox.y = position.y + Constants.PLAYER_CENTER.y * 0.53f;
        }
    }

    @Override
    public void render(Batch batch) {
        super.render(batch, -16, -16);
    }
}
