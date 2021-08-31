package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.GunData;

public class GunnerNPC extends BaseNPC {
    public GunnerNPC(float x, float y) {
        super(x, y);

        entityTextureRegion = Assets.get_instance().npcAssets.gunnerBase;
        deathAnimation = Assets.get_instance().npcAssets.gunnerDyingAnimation;
        characterShootingTexture = Assets.get_instance().npcAssets.gunnerShooting;
        currentGun = new Gun(GunData.pkm);
        bulletOffset = new Vector2( 16, -3);
    }
}
