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
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.entities.Crate;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.Player;
import com.sixmoney.sasza_clone.overlays.HUD;
import com.sixmoney.sasza_clone.utils.ChaseCam;
import com.sixmoney.sasza_clone.utils.Constants;

public class GameWorldScreen extends InputAdapter implements Screen {
    public static final String TAG = GameWorldScreen.class.getName();

    private Sasza saszaGame;
    private Viewport viewport;
    private ChaseCam camera;
    private Batch batch;
    private Player player;
    private HUD hud;
    private World<Entity> world;
    private Crate crate;

    public GameWorldScreen(Sasza game) {
        saszaGame = game;
    }

    @Override
    public void show() {
        world = new World<>();
        world.setTileMode(false);
        player = new Player();
        world.add(player.item, player.bbox.x, player.bbox.y, player.bbox.width, player.bbox.height);
        camera = new ChaseCam(player);
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        batch = new SpriteBatch();
        hud = new HUD();
        crate = new Crate();
        world.add(crate.item, crate.bbox.x, crate.bbox.y, crate.bbox.width, crate.bbox.height);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        player.update(delta);
        Response.Result result = world.move(player.item, player.position.x, player.position.y, CollisionFilter.defaultFilter);
        Collisions projectedCollisions = result.projectedCollisions;
        for (int i = 0; i < projectedCollisions.size(); i++) {
            Collision collision = projectedCollisions.get(i);
            if (collision.type == null || collision.type == Response.slide) {
                player.setPosition(collision.touch.x, collision.touch.y);
            }
        }

        viewport.apply(); // viewport.apply() will call camera.update()

        Gdx.gl.glClearColor(Constants.BG_COLOR.r,Constants.BG_COLOR.g,Constants.BG_COLOR.b,Constants.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        crate.render(batch);
        player.render(batch);
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
    public boolean keyUp(int keycode) {
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
        player.setRotation(new Vector2(mouseWorldCoords.x, mouseWorldCoords.y));
        return true;
    }
}
