package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Light {
    public float x;
    public float y;
    public float xOrigin;
    public float yOrigin;
    public float rotation;
    public float size;
    public Color color;
    public TextureRegion lightTexture;

    public Light() {
        x = 0;
        y = 0;
        xOrigin = 0;
        yOrigin = 0;
        rotation = 0;
        size = 0;
        color = Color.WHITE;
        lightTexture = Assets.get_instance().lightAssets.light1;
    }

    public Light(float x, float y, float xOrigin, float yOrigin, float size, float rotation, Color color, TextureRegion lightTexture) {
        this.x = x;
        this.y = y;
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.rotation = rotation;
        this.size = size;
        this.color = color;
        this.lightTexture = lightTexture;
    }

    public void reset() {
        x = 0;
        y = 0;
        xOrigin = 0;
        yOrigin = 0;
        rotation = 0;
        size = 0;
        color = Color.WHITE;
        lightTexture = Assets.get_instance().lightAssets.light1;
    }
}
