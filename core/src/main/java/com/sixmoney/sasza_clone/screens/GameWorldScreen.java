package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.entities.Player;
import com.sixmoney.sasza_clone.utils.Constants;

public class GameWorldScreen extends InputAdapter implements Screen {
    public static final String TAG = GameWorldScreen.class.getName();

    private Sasza saszaGame;
    private Viewport viewport;
    private Batch batch;
    private Player player;

    public GameWorldScreen(Sasza game) {
        saszaGame = game;
    }

    @Override
    public void show() {
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        batch = new SpriteBatch();
        player = new Player();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        player.update();
        viewport.getCamera().position.set(player.getPosition().x, player.getPosition().y, 0);
        viewport.apply();

        Gdx.gl.glClearColor(Constants.BG_COLOR.r,Constants.BG_COLOR.g,Constants.BG_COLOR.b,Constants.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
    public boolean keyDown (int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                player.startMove("UP");
                return true;
            case Input.Keys.S:
                player.startMove("DOWN");
                return true;
            case Input.Keys.A:
                player.startMove("LEFT");
                return true;
            case Input.Keys.D:
                player.startMove("RIGHT");
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                player.stopMove("UP");
                return true;
            case Input.Keys.S:
                player.stopMove("DOWN");
                return true;
            case Input.Keys.A:
                player.stopMove("LEFT");
                return true;
            case Input.Keys.D:
                player.stopMove("RIGHT");
                return true;
        }
        return false;
    }
}
