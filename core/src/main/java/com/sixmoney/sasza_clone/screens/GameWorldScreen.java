package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.LightRenderer;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.entities.Character;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.inputHandlers.ControllerInputHandler;
import com.sixmoney.sasza_clone.inputHandlers.KeyboardInputHandler;
import com.sixmoney.sasza_clone.overlays.HUD;
import com.sixmoney.sasza_clone.overlays.MobileControlUI;
import com.sixmoney.sasza_clone.overlays.PauseOverlay;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.ChaseCam;
import com.sixmoney.sasza_clone.utils.ConsoleCommandExecutor;
import com.sixmoney.sasza_clone.utils.LevelLoader;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class GameWorldScreen implements Screen {
    private static final String TAG = GameWorldScreen.class.getName();

    private Sasza saszaGame;
    private Viewport viewport;
    private ChaseCam camera;
    private Batch batch;
    private ShapeDrawer drawer;
    private HUD hud;
    private PauseOverlay pauseOverlay;
    private MobileControlUI mobileControlUI;
    private ControllerInputHandler controllerInputHandler;
    private LightRenderer lightRenderer;

    public Console console;
    public Level level;
    public KeyboardInputHandler keyboardInputHandler;
    public boolean paused;
    public Entity clickedEntity;

    public GameWorldScreen(Sasza game) {
        saszaGame = game;
    }

    @Override
    public void show() {
        camera = new ChaseCam(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        batch = new SpriteBatch();
        drawer = new ShapeDrawer(batch, Assets.get_instance().debugAssets.bboxOutline);
        level = LevelLoader.load("debug", viewport, camera);
        level.getPlayer().setWeapons(saszaGame.profile.getGuns());
        lightRenderer = new LightRenderer(batch, drawer);
        hud = new HUD(level, batch, saszaGame.debug);
        pauseOverlay = new PauseOverlay(this, batch);
        mobileControlUI = new MobileControlUI(this, batch);

        keyboardInputHandler = new KeyboardInputHandler(this, viewport);
        paused = false;

        controllerInputHandler = new ControllerInputHandler(this, camera);

        console = new GUIConsole(Assets.get_instance().skinAssets.skinConsole);
        console.setCommandExecutor(new ConsoleCommandExecutor(this));
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            console.setDisplayKeyID(Input.Keys.BACKSLASH);
        } else {
            console.setDisplayKeyID(Input.Keys.GRAVE);
        }
        console.setNoHoverAlpha(0.7f);
        console.setHoverAlpha(0.7f);
        console.setPosition(0, 0);

        setInputProcessors();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setInputProcessors() {
        Controllers.addListener(controllerInputHandler);
        if (saszaGame.mobileControls) {
            Gdx.input.setInputProcessor(mobileControlUI.stage);
        } else {
            Gdx.input.setInputProcessor(keyboardInputHandler);
        }
        console.resetInputProcessing();
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            GdxAI.getTimepiece().update(delta);
            level.update(delta);
            lightRenderer.update(level);
        }

        viewport.apply(); // viewport.apply() will call camera.update()
        ScreenUtils.clear(Constants.BG_COLOR);

        batch.setProjectionMatrix(camera.combined);
        batch.setColor(Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING, 1);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);

        level.render(batch, drawer);
        lightRenderer.render();
        level.renderCanopy(batch, drawer);

        batch.begin();
        if (saszaGame.debug) {
            level.renderDebug(drawer);
            if (clickedEntity != null) {
                drawer.rectangle(clickedEntity.bbox, Color.GOLD);
            }
        }
        batch.end();

        hud.render();

        if (saszaGame.mobileControls) {
            mobileControlUI.render();
        }

        if (paused) {
            pauseOverlay.render();
        }

        console.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
        pauseOverlay.resize(width, height);
        console.refresh();
        lightRenderer.resize(viewport);


        if (saszaGame.mobileControls) {
            mobileControlUI.resize(width, height);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        console.dispose();
        batch.dispose();
        pauseOverlay.dispose();
        hud.dispose();
        mobileControlUI.dispose();
        lightRenderer.dispose();
    }

    public void setPaused() {
        paused = true;
        Controllers.removeListener(controllerInputHandler);
        pauseOverlay.setInputProcessors();
        resetVelocity();
        level.pauseWave();
    }

    public void setUnpaused() {
        paused = false;
        setInputProcessors();
        level.resumeWave();
    }

    public void quit() {
        Controllers.removeListener(controllerInputHandler);
        dispose();
        saszaGame.switchScreen("menu");
    }

    public void toggleDebug() {
        saszaGame.debug = !saszaGame.debug;
    }

    public void toggleMobileControls() {
        saszaGame.mobileControls = !saszaGame.mobileControls;
        setInputProcessors();
    }

    public Character getClickedEntity() {
        if (clickedEntity != null && clickedEntity instanceof Character) {
            return (Character) clickedEntity;
        }

        return level.getPlayer();
    }

    public void setClickedEntity(Vector2 pointCoords) {
        clickedEntity = level.queryPoint(pointCoords);
        if (clickedEntity != null) {
            console.log(clickedEntity.toString());
        }
    }

    public void resetVelocity() {
        keyboardInputHandler.resetVelocity();
        level.getPlayer().resetVelocity();
    }

    public void switchWeapon(int index) {
        level.getPlayer().getGun().resetReloadTimer();
        level.getPlayer().setGun(index);
        hud.updateWeaponSprite();
    }
}
