package com.sixmoney.sasza_clone.utils.InputHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.sixmoney.sasza_clone.utils.ChaseCam;

public class ControllerInputHandler implements ControllerListener {
    private static final String TAG = ControllerInputHandler.class.getName();
    private final float deadzone;
    private GameWorldScreen gameWorldScreen;
    private Level level;
    private ChaseCam camera;
    private Vector2 aimingVector;
    private Vector2 velocity;
    private Vector2 velocityNormal;
    private Array<Float> axisValues;

    public ControllerInputHandler(GameWorldScreen gameWorldScreen, ChaseCam cam) {
        this.gameWorldScreen = gameWorldScreen;
        level = gameWorldScreen.level;
        camera = cam;
        deadzone = 0.4f;
        aimingVector = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        velocityNormal = new Vector2(0, 0);
        axisValues = new Array<>(5);
        for (int i = 0; i < 5; i++){
            axisValues.add(0f);
        }

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
        if (value < -deadzone / 2 || value > deadzone / 2) {
            Gdx.app.log(TAG, "Moved stick: " + axisCode + " by: " + value);
        }

        if (axisCode == 0) {
            axisValues.set(0, value);
            if ((axisValues.get(1) > deadzone || axisValues.get(1) < -deadzone) || (value > deadzone || value < -deadzone)) {
                velocity.x = value;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            } else {
                velocity.x = 0;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            }
        }

        if (axisCode == 1) {
            axisValues.set(1, value);
            if ((axisValues.get(0) > deadzone || axisValues.get(0) < -deadzone) || (value > deadzone || value < -deadzone)) {
                velocity.y = -value;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            } else {
                velocity.y = 0;
                velocityNormal.set(velocity).nor();
                level.getPlayer().setVelocity(velocityNormal);
                return true;
            }
        }

        if (axisCode == 2) {
            axisValues.set(2, value);
            if ((axisValues.get(3) > deadzone || axisValues.get(3) < -deadzone) || (value > deadzone || value < -deadzone)) {
                aimingVector.x = value;
                aimingVector.nor();
                level.getPlayer().rotation = aimingVector.angleDeg() + 90;
                return true;
            }
        }

        if (axisCode == 3) {
            axisValues.set(3, value);
            if ((axisValues.get(2) > deadzone || axisValues.get(2) < -deadzone) || (value > deadzone || value < -deadzone)) {
                aimingVector.y = -value;
                aimingVector.nor();
                level.getPlayer().rotation = aimingVector.angleDeg() + 90;
                return true;
            }
        }

        if (axisCode == 5 && value > deadzone) {
            level.shooting = true;
            return true;
        } else if (axisCode == 5 && value < deadzone) {
            level.shooting = false;
            return true;
        }

        return false;
    }
}
