package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sixmoney.sasza_clone.Level;

public class InputHandler extends InputAdapter {
    Level level;
    ChaseCam camera;

    public InputHandler(Level level, ChaseCam camera) {
        this.level = level;
        this.camera = camera;
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
