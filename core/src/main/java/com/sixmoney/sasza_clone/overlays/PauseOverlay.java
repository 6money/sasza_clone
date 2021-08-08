package com.sixmoney.sasza_clone.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
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
    private Vector2 mouseScreenCoords;

    public InputMultiplexer inputProcessor;

    public PauseOverlay(GameWorldScreen gameWorldScreen, Batch batch) {
        this.gameWorldScreen = gameWorldScreen;
        stage = new ControllerMenuStage(new ScreenViewport(), batch);
        inputProcessor = new InputMultiplexer(stage, this);
        skin = Assets.get_instance().skinAssets.skin;
        mouseScreenCoords = new Vector2(0, 0);

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
                Controllers.removeListener(controllerInputHandler);
                gameWorldScreen.setUnpaused();
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

    public void setInputProcessors() {
        Gdx.input.setInputProcessor(inputProcessor);
        Controllers.addListener(controllerInputHandler);
        gameWorldScreen.console.resetInputProcessing();
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
            Controllers.removeListener(controllerInputHandler);
            gameWorldScreen.setUnpaused();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        mouseScreenCoords.set(screenX, screenY);
        gameWorldScreen.getViewport().unproject(mouseScreenCoords);

        if (button == 0) {
            gameWorldScreen.setClickedEntity(mouseScreenCoords);
            return true;
        }
        return false;
    }

    public void dispose() {
        Controllers.removeListener(controllerInputHandler);
        stage.dispose();
    }
}
