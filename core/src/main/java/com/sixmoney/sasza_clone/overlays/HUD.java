package com.sixmoney.sasza_clone.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.utils.Constants;

public class HUD {
    public Viewport viewport;

    private BitmapFont font;

    public HUD() {
        viewport = new ScreenViewport();
        font = new BitmapFont(Gdx.files.internal("fonts/arial-15.fnt"));
        font.getData().setScale(2);
    }

    public void render(Batch batch) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        font.draw(batch, String.valueOf(Gdx.graphics.getFramesPerSecond()), viewport.getCamera().viewportWidth - Constants.HUD_MARGIN * 3, viewport.getCamera().viewportHeight - Constants.HUD_MARGIN * 2);
        batch.end();

    }

    public void dispose() {
        font.dispose();
    }
}