package com.sixmoney.sasza_clone.utils.InputHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.sixmoney.sasza_clone.overlays.MobileControlUI;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;

public class MobileInputHandler {
    public final static String TAG = MobileInputHandler.class.getName();

    private GameWorldScreen gameWorldScreen;
    private MobileControlUI mobileControlUI;

    private Vector2 aimingVector;
    private Vector2 velocity;
    private Vector2 velocityNormal;
    private Array<Float> axisValues;
    private int weaponIndex;

    public MobileInputHandler(GameWorldScreen gameWorldScreen, MobileControlUI mobileControlUI) {
        this.gameWorldScreen = gameWorldScreen;
        this.mobileControlUI = mobileControlUI;

        aimingVector = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        velocityNormal = new Vector2(0, 0);
        axisValues = new Array<>(5);
        for (int i = 0; i < 5; i++){
            axisValues.add(0f);
        }
        weaponIndex = 0;

        attachInputHandlers();
    }

    private void attachInputHandlers() {
        mobileControlUI.touchpadLeft.addListener(new DragListener() {
            @Override
            public void dragStart (InputEvent event, float x, float y, int pointer) {
                velocity.x = mobileControlUI.touchpadLeft.getKnobPercentX();
                velocity.y = mobileControlUI.touchpadLeft.getKnobPercentX();
                velocityNormal.set(velocity).nor();
                gameWorldScreen.level.getPlayer().setVelocity(velocityNormal);
            }

            @Override
            public void drag (InputEvent event, float x, float y, int pointer) {
                velocity.x = mobileControlUI.touchpadLeft.getKnobPercentX();
                velocity.y = mobileControlUI.touchpadLeft.getKnobPercentY();
                velocityNormal.set(velocity).nor();
                gameWorldScreen.level.getPlayer().setVelocity(velocityNormal);
            }

            @Override
            public void dragStop (InputEvent event, float x, float y, int pointer) {
                velocity.set(0, 0);
                velocityNormal.set(velocity).nor();
                gameWorldScreen.level.getPlayer().setVelocity(velocityNormal);
            }
        });

        mobileControlUI.touchpadRight.addListener(new DragListener() {
            @Override
            public void dragStart (InputEvent event, float x, float y, int pointer) {
                Gdx.app.log(TAG, "dragstart");
                gameWorldScreen.level.getPlayer().shooting = true;
            }

            @Override
            public void drag (InputEvent event, float x, float y, int pointer) {
                Gdx.app.log(TAG, "dragging");
                aimingVector.x = mobileControlUI.touchpadRight.getKnobPercentX();
                aimingVector.y = mobileControlUI.touchpadRight.getKnobPercentY();
                aimingVector.nor();
                gameWorldScreen.level.getPlayer().rotation = aimingVector.angleDeg();
            }

            @Override
            public void dragStop (InputEvent event, float x, float y, int pointer) {
                Gdx.app.log(TAG, "dragend");
                    gameWorldScreen.level.getPlayer().shooting = false;
            }
        });

        mobileControlUI.switchWeaponButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                gameWorldScreen.switchWeapon(nextWeaponIndex());
            }
        });

        mobileControlUI.reloadButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                gameWorldScreen.level.getPlayer().getGun().initReload();
            }
        });

        mobileControlUI.pauseButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                gameWorldScreen.setPaused();
            }
        });

        mobileControlUI.spawnWaveButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                try {
                    gameWorldScreen.level.spawnEnemyWave(20);
                } catch (ReflectionException ignored) {
                }
            }
        });
    }

    private int nextWeaponIndex() {
        weaponIndex += 1;
        if (weaponIndex >= 3) {
            weaponIndex = 0;
        }

        return weaponIndex;
    }
}
