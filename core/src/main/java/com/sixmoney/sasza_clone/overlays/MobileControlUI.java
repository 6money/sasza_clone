package com.sixmoney.sasza_clone.overlays;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.InputHandlers.MobileInputHandler;

public class MobileControlUI {
    public final static String TAG = MobileControlUI.class.getName();

    private Skin skin;
    private Table table;

    public Stage stage;
    public TextButton reloadButton;
    public TextButton switchWeaponButton;
    public Button pauseButton;
    public Button spawnWaveButton;
    public Touchpad touchpadLeft;
    public Touchpad touchpadRight;

    public MobileControlUI(GameWorldScreen gameWorldScreen, Batch batch) {
        stage = new Stage(new ScreenViewport(), batch);
//        stage.setDebugAll(true);
        skin = Assets.get_instance().skinAssets.skin;
        table = new Table();
        table.pad(50);
        table.setFillParent(true);
        table.setPosition(0, 0);
        table.defaults().expandX();

        switchWeaponButton = new TextButton("Switch", skin);
        reloadButton = new TextButton("Reload", skin);
        table.add(switchWeaponButton).expand().left().bottom();
        table.add(reloadButton).expand().right().bottom();

        table.row().space(50);
        touchpadLeft = new Touchpad(40, skin);
        touchpadLeft.setColor(1, 1, 1, 0.5f);
        touchpadRight = new Touchpad(20, skin);
        touchpadRight.setColor(1, 1, 1, 0.5f);
        table.add(touchpadLeft).bottom().left().width(250).height(250);
        table.add(touchpadRight).bottom().right().width(250).height(250);

        table.pack();
        stage.addActor(table);

        pauseButton = new Button(skin);
        pauseButton.setPosition(0, stage.getViewport().getScreenHeight() - 150);
        pauseButton.setSize(150, 150);
        pauseButton.setColor(1, 1, 1, 0);
        stage.addActor(pauseButton);

        spawnWaveButton = new Button(skin);
        spawnWaveButton.setPosition(stage.getViewport().getScreenWidth() - 150, stage.getViewport().getScreenHeight() - 150);
        spawnWaveButton.setSize(150, 150);
        spawnWaveButton.setColor(1, 1, 1, 0);
        stage.addActor(spawnWaveButton);

        new MobileInputHandler(gameWorldScreen, this);
    }

    public void render() {
        stage.act();
        stage.draw();
        stage.getBatch().setColor(Constants.AMBIENT_LIGHTING, Constants.AMBIENT_LIGHTING, Constants.AMBIENT_LIGHTING, 1);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        pauseButton.setPosition(0, stage.getViewport().getScreenHeight() - 150);
        spawnWaveButton.setPosition(stage.getViewport().getScreenWidth() - 150, stage.getViewport().getScreenHeight() - 150);
    }

    public void dispose() {
        stage.dispose();
    }
}
