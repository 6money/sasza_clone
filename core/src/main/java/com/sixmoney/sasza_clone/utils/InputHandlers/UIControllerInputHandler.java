package com.sixmoney.sasza_clone.utils.InputHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import de.golfgl.gdx.controllers.ControllerMenuStage;

public class UIControllerInputHandler implements ControllerListener {
    private static final String TAG = UIControllerInputHandler.class.getName();
    private ControllerMenuStage stage;
    private ControllerMapping mapping;

    public UIControllerInputHandler(ControllerMenuStage stage) {
        this.stage = stage;
        mapping = Controllers.getCurrent().getMapping();
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

        if (buttonCode == mapping.buttonA) {
            InputEvent inputEvent = new InputEvent();
            inputEvent.setType(InputEvent.Type.touchDown);
            stage.getFocusedActor().fire(inputEvent);
            return true;
        }

        if (buttonCode == mapping.buttonDpadDown) {
            if (stage.getFocusableActors().indexOf(stage.getFocusedActor(), true) == stage.getFocusableActors().size - 1) {
                return true;
            }

            Actor nextActor = stage.getFocusableActors().get(stage.getFocusableActors().indexOf(stage.getFocusedActor(), true) + 1);
            stage.setFocusedActor(nextActor);
            return true;
        }

        if (buttonCode == mapping.buttonDpadUp) {
            if (stage.getFocusableActors().indexOf(stage.getFocusedActor(), true) == 0) {
                return true;
            }

            Actor nextActor = stage.getFocusableActors().get(stage.getFocusableActors().indexOf(stage.getFocusedActor(), true) - 1);
            stage.setFocusedActor(nextActor);
            return true;
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        Gdx.app.log(TAG, "Released: " + buttonCode);

        if (buttonCode == mapping.buttonA) {
            InputEvent inputEvent = new InputEvent();
            inputEvent.setType(InputEvent.Type.touchUp);
            stage.getFocusedActor().fire(inputEvent);
            return true;
        }

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }
}
