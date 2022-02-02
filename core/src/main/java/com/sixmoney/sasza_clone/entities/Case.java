package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;

public class Case {
    private Vector2 position;
    private Vector2 direction;
    private TextureRegion caseTextureRegion;
    private float rotation;
    private long spawnTime;
    private float velocity;
    private Color color;

    public boolean dead;

    public Case(float x, float y, float rotation) {
        position = new Vector2(x, y);
        this.rotation = rotation + MathUtils.random(-15, 15);
        direction = new Vector2(1, 0);
        direction.rotateDeg(this.rotation - 90);
        caseTextureRegion = Assets.get_instance().weaponAssets.pistolProjectile;
        spawnTime = TimeUtils.nanoTime();
        dead = false;
        velocity = 30 + MathUtils.random(-5 , 5);
    }

    public void update(float delta) {
        if (Utils.millisecondsSince(spawnTime) >= 400) {
            velocity = 0;
            color = new Color(0.5f, 0.5f,0.5f, 0.75f);
        }

        if (Utils.secondsSince(spawnTime) >= 10) {
            dead = true;
            return;
        }

        position.add(direction.x * velocity * delta, direction.y * velocity * delta);
    }

    public void render(Batch batch) {
        if (color != null) {
            batch.setColor(color);
        }

        Utils.drawTextureRegion(batch, caseTextureRegion, position.x, position.y, rotation);

        if (color != null) {
            batch.setColor(Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING);
        }
    }
}
