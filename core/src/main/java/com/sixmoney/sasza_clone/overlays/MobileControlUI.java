package com.sixmoney.sasza_clone.overlays;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.InputHandlers.MobileInputHandler;

public class MobileControlUI {
    public final static String TAG = MobileControlUI.class.getName();

    private Skin skin;
    private Table table;

    public Stage stage;
    public Touchpad touchpadLeft;
    public Touchpad touchpadRight;

    public MobileControlUI(GameWorldScreen gameWorldScreen, Batch batch) {
        stage = new Stage(new ScreenViewport(), batch);
        skin = Assets.get_instance().skinAssets.skin;
        table = new Table();
        table.pad(50);
        table.setFillParent(true);
        table.setPosition(0, 0);
        table.defaults().expand().space(50);

        touchpadLeft = new Touchpad(20, skin);
        touchpadRight = new Touchpad(20, skin);
        table.add(touchpadLeft).fill(0.2f, 0.2f).bottom().left();
        table.add(touchpadRight).fill(0.2f, 0.2f).bottom().right();

        stage.addActor(table);

        new MobileInputHandler(gameWorldScreen, this);
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}
