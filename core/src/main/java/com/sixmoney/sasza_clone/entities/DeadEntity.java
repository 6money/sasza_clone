package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.sasza_clone.utils.Utils;

public class DeadEntity extends Entity {
    private static final String TAG = DeadEntity.class.getName();

    public DeadEntity(float x, float y, float rotation, Animation<TextureRegion> deathAnimation) {
        position.set(x, y);
        entityAnimation = deathAnimation;
        this.rotation = rotation;
        animationStartTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(Batch batch) {
        float animationTime = Utils.secondsSince(animationStartTime);
        Utils.drawTextureRegion(batch, entityAnimation.getKeyFrame(animationTime), position.x, position.y, rotation);
    }
}
