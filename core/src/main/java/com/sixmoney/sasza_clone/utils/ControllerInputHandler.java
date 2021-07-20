package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;

public class ControllerInputHandler implements ControllerListener {
    private static final String TAG = ControllerInputHandler.class.getName();

    private GameWorldScreen gameWorldScreen;
    private Level level;
    private ChaseCam camera;
    private Vector2 aimingVector;

    public ControllerInputHandler(GameWorldScreen gameWorldScreen, ChaseCam cam) {
        this.gameWorldScreen = gameWorldScreen;
        level = gameWorldScreen.level;
        camera = cam;
        aimingVector = new Vector2(0, 0);
    }

    @Override
    public void connected(Controller controller) {
        Gdx.app.log(TAG, "Connected: " + controller.getName());
    }

    @Override
    public void disconnected(Controller controller) {
        Gdx.app.log(TAG, "Disconnected: " + controller.getName());
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        Gdx.app.log(TAG, "Pressed: " + buttonCode);

        if (buttonCode == 6) {
            if (!gameWorldScreen.paused) {
                gameWorldScreen.setPaused();
            } else {
                gameWorldScreen.setUnpaused();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        Gdx.app.log(TAG, "Released: " + buttonCode);
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if (value < -0.1 || value > 0.1) {
            Gdx.app.log(TAG, "Moved stick: " + axisCode + " by: " + value);
        }

        if (axisCode == 0) {
            if (value > 0.2) {
                level.getPlayer().startMove("RIGHT");
            } else if (value < 0.2) {
                level.getPlayer().stopMove("RIGHT");
            }

            if (value < -0.2) {
                level.getPlayer().startMove("LEFT");
            } else if (value > -0.2) {
                level.getPlayer().stopMove("LEFT");
            }

            return true;
        }

        if (axisCode == 1) {
            if (value > 0.2) {
                level.getPlayer().startMove("DOWN");
            } else if (value < 0.2) {
                level.getPlayer().stopMove("DOWN");
            }

            if (value < -0.2) {
                level.getPlayer().startMove("UP");
            } else if (value > -0.2) {
                level.getPlayer().stopMove("UP");
            }

            return true;
        }

        if (axisCode == 2 && (value < -0.2 || value > 0.2)) {
            aimingVector.x = value;
            aimingVector.nor();
            level.getPlayer().rotation = aimingVector.angleDeg() + 90;
            return true;
        }

        if (axisCode == 3 && (value < -0.2 || value > 0.2)) {
            aimingVector.y = -value;
            aimingVector.nor();
            level.getPlayer().rotation = aimingVector.angleDeg() + 90;
            return true;
        }

        if (axisCode == 5 && value > 0.2) {
            level.shooting = true;
            return true;
        } else if (axisCode == 5 && value < 0.2) {
            level.shooting = false;
            return true;
        }

        return false;
    }
}
