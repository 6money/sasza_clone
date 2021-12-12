package com.sixmoney.sasza_clone.entities;

import com.sixmoney.sasza_clone.utils.Assets;

public class EnemyTierThree extends BaseEnemy {
    public EnemyTierThree(float x, float y) {
        super(x, y);

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
}
