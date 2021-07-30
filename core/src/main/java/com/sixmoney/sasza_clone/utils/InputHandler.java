package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;

public class InputHandler extends InputAdapter {
    private GameWorldScreen gameWorldScreen;
    private Level level;
    private ChaseCam camera;
    private Vector2 velocity;
    private Vector2 velocityNormal;

    public InputHandler(GameWorldScreen gameWorldScreen, ChaseCam camera) {
        this.gameWorldScreen = gameWorldScreen;
        level = gameWorldScreen.level;
        this.camera = camera;
        velocity = new Vector2(0, 0);
        velocityNormal = new Vector2(0, 0);
    }


    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log("TAG", keycode + "");
        switch (keycode) {
            case Input.Keys.W:
                velocity.y += 1;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            case Input.Keys.S:
                velocity.y -= 1;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            case Input.Keys.A:
                velocity.x -= 1;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            case Input.Keys.D:
                velocity.x += 1;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            case Input.Keys.ESCAPE:
                gameWorldScreen.setPaused();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                velocity.y -= 1;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            case Input.Keys.S:
                velocity.y += 1;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            case Input.Keys.A:
                velocity.x += 1;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            case Input.Keys.D:
                velocity.x -= 1;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
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
        if (button == 0) {
            level.shooting = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 mouseWorldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        level.getPlayer().setRotation(new Vector2(mouseWorldCoords.x, mouseWorldCoords.y));
        if (button == 0) {
            level.shooting = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 mouseWorldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        level.getPlayer().setRotation(new Vector2(mouseWorldCoords.x, mouseWorldCoords.y));
        return true;
    }
}
