package com.sixmoney.sasza_clone.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.PreferenceManager;

public class HUD {
    private Level level;
    private Skin skin;
    private Table table;
    private Stage stage;
    private Boolean debug;

    private Label labelHealth;
    private Label labelAmmo;
    private Label labelMagAmmo;
    private Label labelFPS;
    private Label labelPosition;
    private Label labelWaveTimer;
    private Image gunSprite;

    public HUD(Level level, Batch batch, Boolean debug) {
        this.level = level;
        this.debug = debug;
        stage = new Stage(new ScreenViewport(), batch);
        skin = Assets.get_instance().skinAssets.skin;

        table = new Table();
        table.setFillParent(true);
        table.pad(20).top();
        table.defaults().growX();

        labelHealth = new Label("", skin);
        labelHealth.setAlignment(Align.left);
        labelFPS = new Label("", skin);
        labelFPS.setAlignment(Align.right);
        table.add(labelHealth).align(Align.left);
        table.add(labelFPS).right();

        table.row();
        labelAmmo = new Label("", skin);
        labelAmmo.setAlignment(Align.left);
        labelPosition = new Label("", skin);
        labelPosition.setAlignment(Align.right);
        table.add(labelAmmo).left();
        table.add(labelPosition).align(Align.right);

        table.row();
        labelMagAmmo = new Label("", skin);
        labelMagAmmo.setAlignment(Align.left);
        labelWaveTimer = new Label("", skin);
        labelWaveTimer.setAlignment(Align.right);
        table.add(labelMagAmmo).align(Align.left);
        table.add(labelWaveTimer).align(Align.right);

        table.row();
        gunSprite = new Image(level.getPlayer().getGun().getWeaponSprite());
        gunSprite.scaleBy(3);
        gunSprite.setOriginY(gunSprite.getHeight());
        table.defaults().reset();
        table.add(gunSprite).align(Align.bottomLeft);

        table.pack();
        table.validate();
        stage.addActor(table);
    }

    public void render() {
        labelHealth.setText("Health: " + ((int) level.getPlayer().getHealth()));
        labelAmmo.setText(String.valueOf(level.getPlayer().getGun().getCurrentAmmo()));
        labelMagAmmo.setText(String.valueOf(level.getPlayer().getGun().getCurrentMagazineAmmo()));
        labelWaveTimer.setText("Wave starts in " + level.waveCountdown);
        if (debug || PreferenceManager.get_instance().getShowFPS()) {
            labelFPS.setText(String.valueOf(Gdx.graphics.getFramesPerSecond()));
        }
        if (debug || PreferenceManager.get_instance().getShowCoords()) {
            labelPosition.setText(MathUtils.round(level.getPlayer().getPosition().x) + "," + MathUtils.round(level.getPlayer().getPosition().y));
        }

        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }

    public void updateWeaponSprite() {
        gunSprite.setDrawable(new TextureRegionDrawable(level.getPlayer().getGun().getWeaponSprite()));
    }
}
