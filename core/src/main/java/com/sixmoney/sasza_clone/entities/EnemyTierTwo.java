package com.sixmoney.sasza_clone.entities;

import com.sixmoney.sasza_clone.utils.Assets;

public class EnemyTierTwo extends BaseEnemy {
    public EnemyTierTwo(float x, float y) {
        super(x, y);

        entityTextureRegion = Assets.get_instance().enemyAssets.zom2;
        entityAnimation = Assets.get_instance().enemyAssets.enemyWalkingAnimation;
        deathAnimation = Assets.get_instance().enemyAssets.zom2DyingAnimation;
        characterIdleLegTexture = Assets.get_instance().enemyAssets.enemyStand;

        health = 200;
        maxHealth = 200;
        maxLinearSpeed = 190f;
        damage = 15;
    }
}
