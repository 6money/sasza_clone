package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.GunData;

public class RiflemanSoldier extends BaseSoldier {
    public RiflemanSoldier(float x, float y) {
        super(x, y);

        entityTextureRegion = Assets.get_instance().npcAssets.riflemanBase;
        deathAnimation = Assets.get_instance().npcAssets.riflemanDyingAnimation;
        characterShootingTexture = Assets.get_instance().npcAssets.riflemanShooting;
        characterIdleLegTexture = Assets.get_instance().npcAssets.npcStandS2;
        entityAnimation = Assets.get_instance().npcAssets.npcWalkingAnimationS2;
        bulletOffset = new Vector2( 16, -3);
        guns.set(0, new Gun(GunData.mp5));
        setGun(0);
    }
}
