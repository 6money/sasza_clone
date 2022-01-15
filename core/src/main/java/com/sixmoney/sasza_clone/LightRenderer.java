package com.sixmoney.sasza_clone;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.entities.Bullet;
import com.sixmoney.sasza_clone.entities.Character;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Light;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class LightRenderer {
    private FrameBuffer lightBuffer;
    private Batch batch;
    private ShapeDrawer drawer;
    private Pool<Light> lightPool;
    private Array<Light> lights;
    private Light tempLight;
    private TextureRegion lightBufferTexture;

    private Color color1;
    private Color color2;

    public LightRenderer(Batch batch, ShapeDrawer drawer) {
        // lightBuffer is inited in resize, which is called automatically by libgdx from GameWorldScreen
        this.batch = batch;
        this.drawer = drawer;
        lights = new Array<>();
        color1 = new Color(0.768f, 0.674f, 0.329f, 1);
        color2 = new Color(1, 1, 1, 1);

        lightPool = new Pool<Light>(100, 500) {
            @Override
            protected Light newObject() {
                return new Light();
            }

            @Override
            public void free(Light light) {
                light.reset();
                super.free(light);
            }

            @Override
            public void freeAll(Array<Light> lights) {
                for (Light light: lights) {
                    light.reset();
                }
                super.freeAll(lights);
            }
        };
        lightPool.fill(100);
    }

    public void update(Level level) {
        lightPool.freeAll(lights);
        lights.clear();

        // player light
        tempLight = lightPool.obtain();
        lights.add(tempLight);
        tempLight.size = (600f / 100f) * 80;
        tempLight.x = level.getPlayer().getPosition().x - tempLight.size / 2f;
        tempLight.y = level.getPlayer().getPosition().y - tempLight.size / 2f;
        tempLight.lightTexture = Assets.get_instance().lightAssets.light2;
        tempLight.color = color1;

        // npc lights
        for (Character npc: level.characterEntities) {
            tempLight = lightPool.obtain();
            lights.add(tempLight);
            tempLight.size = (600f / 100f) * 40;
            tempLight.x = npc.getPosition().x - tempLight.size / 2f;
            tempLight.y = npc.getPosition().y - tempLight.size / 2f;
            tempLight.lightTexture = Assets.get_instance().lightAssets.light2;
            tempLight.color = color1;
        }

        // bullet lights
        for (Bullet bullet: level.bullets) {
            tempLight = lightPool.obtain();
            lights.add(tempLight);
            tempLight.size = (600f / 100f) * 2;
            tempLight.x = bullet.position.x - tempLight.size / 2f;
            tempLight.y = bullet.position.y - tempLight.size / 2f;
            tempLight.lightTexture = Assets.get_instance().lightAssets.light2;
            tempLight.color = color1;
        }


        for (Character npc: level.characterEntities) {
            if (npc.shooting) {
                tempLight = lightPool.obtain();
                lights.add(tempLight);
                tempLight.size = (80f / 100f) * 50;
                tempLight.x = npc.position.x + Constants.PLAYER_CENTER.x + npc.bulletOffsetReal.x;
                tempLight.y = npc.position.y + Constants.PLAYER_CENTER.y + npc.bulletOffsetReal.y - tempLight.size / 2f;
                tempLight.yOrigin = tempLight.size / 2;
                tempLight.rotation = npc.rotation;
                tempLight.lightTexture = Assets.get_instance().lightAssets.light4;
                tempLight.color = color2;
            }
        }

        if (level.getPlayer().shooting) {
            tempLight = lightPool.obtain();
            lights.add(tempLight);
            tempLight.reset();
            tempLight.size = (80f / 100f) * 50;
            tempLight.x = level.getPlayer().position.x + Constants.PLAYER_CENTER.x + level.getPlayer().bulletOffsetReal.x;
            tempLight.y = level.getPlayer().position.y + Constants.PLAYER_CENTER.y + level.getPlayer().bulletOffsetReal.y - tempLight.size / 2f;
            tempLight.yOrigin = tempLight.size / 2;
            tempLight.rotation = level.getPlayer().rotation;
            tempLight.lightTexture = Assets.get_instance().lightAssets.light4;
            tempLight.color = color2;
        }
    }

    public void render() {
        // RENDER TO FBO
        lightBuffer.begin();
        // set ambient light level
        ScreenUtils.clear(Constants.AMBIENT_LIGHTING);
        // set blending
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.begin();

        for (Light light: lights) {
            batch.setColor(light.color);
            batch.draw(light.lightTexture, light.x, light.y, light.xOrigin, light.yOrigin, light.size, light.size, 1, 1, light.rotation);
        }

        // reset batch color to white IMPORTANT
        batch.setColor(1, 1, 1, 1);
        batch.end();
        lightBuffer.end();

        // RENDER FBO TO BACK BUFFER
        batch.setProjectionMatrix(batch.getProjectionMatrix().idt());
        batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        batch.begin();
        lightBufferTexture.setRegion(lightBuffer.getColorBufferTexture());
        lightBufferTexture.flip(false, false);
        batch.draw(lightBufferTexture, -1, 1, 2, -2);
        batch.end();

        // RESET BLEND MODE
        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void resize(Viewport viewport) {
        int screenWidth = viewport.getScreenWidth();
        int screenHeight = viewport.getScreenHeight();

        if (lightBuffer != null) {
            lightBuffer.dispose();
        }
        lightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, screenWidth, screenHeight, false);
        lightBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        lightBufferTexture = new TextureRegion(lightBuffer.getColorBufferTexture(), screenWidth, screenHeight);
    }

    public void dispose() {
        lightBuffer.dispose();
    }
}
