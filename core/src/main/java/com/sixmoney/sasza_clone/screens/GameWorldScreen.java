package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.overlays.HUD;
import com.sixmoney.sasza_clone.overlays.PauseOverlay;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.ChaseCam;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.InputHandler;
import com.sixmoney.sasza_clone.utils.LevelLoader;

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

    public Level level;
    public InputHandler inputHandler;
    public boolean paused;

    public GameWorldScreen(Sasza game) {
        saszaGame = game;
    }

    @Override
    public void show() {
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        batch = new SpriteBatch();
        drawer = new ShapeDrawer(batch, Assets.get_instance().debugAssets.bboxOutline);
        level = LevelLoader.load("debug", viewport);
        hud = new HUD(level);
        pauseOverlay = new PauseOverlay(this, batch);
        camera = (ChaseCam) viewport.getCamera();
        inputHandler = new InputHandler(this, camera);
        paused = false;

        Gdx.input.setInputProcessor(inputHandler);
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            GdxAI.getTimepiece().update(delta);
            level.update(delta);
        }

        viewport.apply(); // viewport.apply() will call camera.update()
        Gdx.gl.glClearColor(Constants.BG_COLOR.r,Constants.BG_COLOR.g,Constants.BG_COLOR.b,Constants.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        level.render(batch, drawer);

        if (saszaGame.debug) {
            level.renderDebug(drawer);
        }

        batch.end();

        hud.render(batch);

        if (paused) {
            pauseOverlay.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
        pauseOverlay.resize(width, height);
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
        batch.dispose();
        pauseOverlay.dispose();
        hud.dispose();
    }

    public void setPaused() {
        paused = true;
        Gdx.input.setInputProcessor(pauseOverlay.inputProcessor);
    }

    public void quit() {
        Gdx.app.postRunnable(() -> {
            saszaGame.switchScreen("menu");
            dispose();
        });
    }
}
