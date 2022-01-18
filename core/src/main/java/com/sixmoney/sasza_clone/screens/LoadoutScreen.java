package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.entities.Gun;
import com.sixmoney.sasza_clone.inputHandlers.UIControllerInputHandler;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Profile;

import de.golfgl.gdx.controllers.ControllerMenuStage;

public class LoadoutScreen implements Screen {
	private static final String TAG = LoadoutScreen.class.getName();

	private Sasza saszaGame;
	private Profile profile;
	private ControllerMenuStage stage;
	private Skin skin;
	private Table tableMenu;
	private UIControllerInputHandler controllerInputHandler;
	private Label labelProfileName;
	private Button buttonBack;
	private Label labelDamageValue;
	private Label labelFireRateValue;
	private Label labelRangeValue;
	private Label labelMagSizeValue;
	private Label labelReloadTimeValue;
	private Label labelGunName;

	private int selectedSlot;
	private Gun selectedGun;
	private Array<Image> gunEquippedImages;
	private Array<Label> gunEquippedLabels;
	private Table gunEquippedTable;

	public LoadoutScreen(Sasza game) {
		saszaGame = game;
		profile = saszaGame.profile;
	}

	@Override
	public void show() {
		stage = new ControllerMenuStage(new ScreenViewport());
		skin = Assets.get_instance().skinAssets.skin;
		stage.setDebugAll(saszaGame.debug);

		controllerInputHandler = new UIControllerInputHandler(stage);
		Controllers.addListener(controllerInputHandler);

		selectedSlot = 0;
		gunEquippedImages = new Array<>(true, 3);
		gunEquippedLabels = new Array<>(true, 3);

		tableMenu = new Table(skin);
		tableMenu.setFillParent(true);
		tableMenu.pad(5);
		tableMenu.defaults().space(5);
		tableMenu.setBackground(skin.getDrawable("bg-tile-ten"));

		// row start //
		Label title = new Label("Loadout", skin);
		title.setAlignment(Align.center);
		tableMenu.add(title).colspan(2).height(100).expandX();

		// row start //
		tableMenu.row();
		gunEquippedTable = new Table(skin);
		for (int i = 0; i <= 2; i++) {
			Gun gun = profile.getLoadout().get(i);
			Table table = new Table(skin);
			table.setTouchable(Touchable.enabled);
			table.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
			Label slotName = new Label("Slot " + (i + 1), skin);
			slotName.setAlignment(Align.center);
			table.add(slotName).fill().expandX();
			table.row();
			gunEquippedImages.add(new Image(new TextureRegionDrawable(gun.getWeaponSprite()), Scaling.fit));
			Container<Image> gunContainer = new Container<>(gunEquippedImages.get(i));
			gunContainer.width(gun.getWeaponSprite().getRegionWidth() * 4).height(gun.getWeaponSprite().getRegionHeight() * 4);
			table.add(gunContainer).width(200).height(75).align(Align.center);
			table.row();
			gunEquippedLabels.add(new Label(gun.getName(), skin));
			gunEquippedLabels.get(i).setAlignment(Align.center);
			table.add(gunEquippedLabels.get(i)).fill().expandX();
			table.addListener(new InputListener() {
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					super.enter(event, x, y, pointer, fromActor);
					table.background(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("reload_bar"), 1, 1, 1, 1)));
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					super.exit(event, x, y, pointer, toActor);
					table.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
				}
			});
			int finalI = i;
			table.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					selectedSlot = finalI;
					super.touchDown(event, x, y, pointer, button);

					return true;
				}
			});
			gunEquippedTable.add(table);
		}
		tableMenu.add(gunEquippedTable).colspan(2).height(120).expandX();
		tableMenu.defaults().space(5).grow();


		// row start //
		tableMenu.row();
		Table gunTable = new Table(skin);
		gunTable.defaults().growX();
		for (Gun gun: profile.getProfileGuns()) {
			gunTable.row();
			Table table = new Table(skin);
			table.setTouchable(Touchable.enabled);
			table.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
			Label gunName = new Label(gun.getName(), skin);
			gunName.setAlignment(Align.center);
			table.add(gunName).fill().expandX();
			Image gunSprite = new Image(new TextureRegionDrawable(gun.getWeaponSprite()), Scaling.fit);
			Container<Image> gunContainer = new Container<>(gunSprite);
			gunContainer.width(gun.getWeaponSprite().getRegionWidth() * 4).height(gun.getWeaponSprite().getRegionHeight() * 4);
			table.add(gunContainer).align(Align.center).width(200).height(75);
			table.addListener(new InputListener() {
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					super.enter(event, x, y, pointer, fromActor);
					table.background(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("reload_bar"), 1, 1, 1, 1)));
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					super.exit(event, x, y, pointer, toActor);
					table.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
				}
			});
			table.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					super.touchDown(event, x, y, pointer, button);
					selectedGun = gun;
					setWeaponDetails(gun);

					return true;
				}
			});
			gunTable.add(table);
		}
		Container<Table> container = new Container<Table>(gunTable);
		container.align(Align.top).fillX();
		ScrollPane scrollPane = new ScrollPane(container, skin);
		tableMenu.add(scrollPane);

		Gun slot0Gun = profile.getLoadout().get(selectedSlot);
		Table table2 = new Table(skin);
		table2.defaults().expand();
		labelGunName = new Label(slot0Gun.getName(), skin);
		labelGunName.setAlignment(Align.center);
		table2.add(labelGunName).colspan(2);

		table2.row();
		Label labelDamage = new Label("Damage", skin);
		labelDamageValue = new Label(String.valueOf(slot0Gun.getDamage()), skin);
		table2.add(labelDamage);
		table2.add(labelDamageValue);

		table2.row();
		Label labelFireRate = new Label("Fire rate", skin);
		labelFireRateValue = new Label(String.valueOf(slot0Gun.getFireRate()), skin);
		table2.add(labelFireRate);
		table2.add(labelFireRateValue);

		table2.row();
		Label labelRange = new Label("Range", skin);
		labelRangeValue = new Label(String.valueOf(slot0Gun.getRange()), skin);
		table2.add(labelRange);
		table2.add(labelRangeValue);

		table2.row();
		Label labelMagSize = new Label("Mag Size", skin);
		labelMagSizeValue = new Label(String.valueOf(slot0Gun.getMagazineSize()), skin);
		table2.add(labelMagSize);
		table2.add(labelMagSizeValue);

		table2.row();
		Label labelReloadTime = new Label("Reload Time", skin);
		labelReloadTimeValue = new Label(String.valueOf(slot0Gun.getReloadTime()), skin);
		table2.add(labelReloadTime);
		table2.add(labelReloadTimeValue);

		table2.row();
		TextButton buttonSwitchWeapon = new TextButton("Select Weapon", skin);
		buttonSwitchWeapon.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				profile.getLoadout().set(selectedSlot, selectedGun);
				profile.setLoadout(profile.getLoadout());
				gunEquippedLabels.get(selectedSlot).setText(selectedGun.getName());
				gunEquippedImages.get(selectedSlot).setDrawable(new TextureRegionDrawable(selectedGun.getWeaponSprite()));
				tableMenu.pack();
			}
		});
		table2.add(buttonSwitchWeapon).colspan(2);
		tableMenu.add(table2).width(stage.getWidth() / 3 * 2);


		tableMenu.pack();
		stage.addActor(tableMenu);

		buttonBack = new Button(skin);
		buttonBack.add(new Label("Back" ,skin));
		buttonBack.setWidth(stage.getWidth() / 8);
		buttonBack.setHeight(stage.getHeight() / 10);
		buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
		buttonBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saszaGame.switchScreen("menu");
				dispose();
			}
		});
		stage.addActor(buttonBack);

//		labelProfileName = new Label(null, skin);
//		if (!saszaGame.profile.getName().equals("default")) {
//			labelProfileName.setText(saszaGame.profile.getName() + "\nLevel: " + saszaGame.profile.getProfileLevel());
//		}
//		labelProfileName.setPosition(20, stage.getHeight() - 30);
//		stage.addActor(labelProfileName);

		Gdx.input.setInputProcessor(stage);
	}


	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
	}


	@Override
	public void render(float delta) {
		ScreenUtils.clear(Constants.BG_COLOR);

		stage.act(delta);
		stage.draw();
	}


	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		Controllers.removeListener(controllerInputHandler);
		stage.dispose();
	}

	private void setWeaponDetails(Gun gun) {
		labelGunName.setText(gun.getName());
		labelDamageValue.setText(String.valueOf(gun.getDamage()));
		labelFireRateValue.setText(String.valueOf(gun.getFireRate()));
		labelRangeValue.setText(String.valueOf(gun.getRange()));
		labelReloadTimeValue.setText(String.valueOf(gun.getReloadTime()));
		labelMagSizeValue.setText(String.valueOf(gun.getMagazineSize()));
	}
}