package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.overlays.HUD;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.ChaseCam;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.LevelLoader;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class GameWorldScreen extends InputAdapter implements Screen {
    private static final String TAG = GameWorldScreen.class.getName();

    private Sasza saszaGame;
    private Viewport viewport;
    private ChaseCam camera;
    private Batch batch;
    private ShapeDrawer drawer;
    private HUD hud;
    private Level level;

    public GameWorldScreen(Sasza game) {
        saszaGame = game;
    }

    @Override
    public void show() {
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        batch = new SpriteBatch();
        drawer = new ShapeDrawer(batch, Assets.get_instance().debugAssets.bboxOutline);
        hud = new HUD();
        level = LevelLoader.load("debug", viewport);
        camera = (ChaseCam) viewport.getCamera();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        level.update(delta);

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
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.viewport.update(width, height, true);
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
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                level.getPlayer().startMove("UP");
                return true;
            case Input.Keys.S:
                level.getPlayer().startMove("DOWN");
                return true;
            case Input.Keys.A:
                level.getPlayer().startMove("LEFT");
                return true;
            case Input.Keys.D:
                level.getPlayer().startMove("RIGHT");
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                level.getPlayer().stopMove("UP");
                return true;
            case Input.Keys.S:
                level.getPlayer().stopMove("DOWN");
                return true;
            case Input.Keys.A:
                level.getPlayer().stopMove("LEFT");
                return true;
            case Input.Keys.D:
                level.getPlayer().stopMove("RIGHT");
                return true;
        }
        return false;
    }

    @Override
    public boolean scrolled (float amountX, float amountY) {
        if (amountY > 0) {
            camera.zoomOut(0.1f);
            return true;
        } else if (amountY < 0) {
            camera.zoomIn(0.1f);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 mouseWorldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        level.getPlayer().setRotation(new Vector2(mouseWorldCoords.x, mouseWorldCoords.y));
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 mouseWorldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        level.getPlayer().setRotation(new Vector2(mouseWorldCoords.x, mouseWorldCoords.y));
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 mouseWorldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        level.getPlayer().setRotation(new Vector2(mouseWorldCoords.x, mouseWorldCoords.y));
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 mouseWorldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        level.getPlayer().setRotation(new Vector2(mouseWorldCoords.x, mouseWorldCoords.y));
        return true;
    }
}
