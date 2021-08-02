package com.sixmoney.sasza_clone.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.InputHandlers.UIControllerInputHandler;

import de.golfgl.gdx.controllers.ControllerMenuStage;

public class PauseOverlay extends InputAdapter {
    public final static String TAG = PauseOverlay.class.getName();

    private UIControllerInputHandler controllerInputHandler;
    private GameWorldScreen gameWorldScreen;
    private Skin skin;
    private Table table;
    private ControllerMenuStage stage;
    private boolean show;

    public InputMultiplexer inputProcessor;

    public PauseOverlay(GameWorldScreen gameWorldScreen, Batch batch) {
        this.gameWorldScreen = gameWorldScreen;
        show = false;
        stage = new ControllerMenuStage(new ScreenViewport(), batch);
        inputProcessor = new InputMultiplexer(stage, this);
        skin = Assets.get_instance().skinAssets.skin;

        controllerInputHandler = new UIControllerInputHandler(stage);

        table = new Table();
        table.setFillParent(true);
        table.setPosition(0, 0);
        table.defaults().growX().center();

        Label labelPaused = new Label(Constants.PAUSED_MESSAGE, skin);
        labelPaused.setAlignment(Align.center);
        table.add(labelPaused).padTop(100f).padBottom(40f).padLeft(200f).padRight(200f).height(120f);
        table.row();
        TextButton buttonResume = new TextButton(Constants.RESUME_MESSAGE, skin);
        buttonResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        table.add(buttonResume).padLeft(300f).padRight(300f).height(100f);
        table.row();
        TextButton buttonQuit = new TextButton(Constants.QUIT_MESSAGE, skin);
        buttonQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameWorldScreen.quit();
            }
        });
        table.add(buttonQuit).padLeft(300f).padRight(300f).height(100f);
        table.pack();
        table.validate();

        stage.addActor(table);
        stage.addFocusableActor(buttonResume);
        stage.addFocusableActor(buttonQuit);
        stage.setFocusedActor(buttonResume);
    }

    public void show() {
        if (!show) {
            Controllers.addListener(controllerInputHandler);
        }
        show = true;
    }

    public void hide() {
        show = false;
        Controllers.removeListener(controllerInputHandler);
        gameWorldScreen.paused = false;
        gameWorldScreen.setInputProcessors();
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            gameWorldScreen.paused = false;
            Gdx.input.setInputProcessor(gameWorldScreen.keyboardInputHandler);
            return true;
        }
        return false;
    }

    public void dispose() {
        Controllers.removeListener(controllerInputHandler);
        stage.dispose();
    }
}
