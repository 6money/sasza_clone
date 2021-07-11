package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Utils {
    public static void drawTextureRegion(Batch batch, TextureRegion region, float x, float y) {
        drawTextureRegion(batch, region, x, y, 0f);
    }

    public static void drawTextureRegion(Batch batch, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y);
    }

    public static void drawTextureRegion(Batch batch, TextureRegion region, float x, float y, float rotation) {
        drawTextureRegion(batch, region, x, y, rotation, 1f);
    }

    public static void drawTextureRegion(Batch batch, TextureRegion region, float x, float y, float rotation, float scale) {
        batch.draw(
                region.getTexture(),
                x,
                y,
                region.getRegionWidth() / 2f,
                region.getRegionHeight() / 2f,
                region.getRegionWidth(),
                region.getRegionHeight(),
                scale,
                scale,
                rotation,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
    }

    public static float secondsSince(long timeNanos) {
        return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos);
    }
}
