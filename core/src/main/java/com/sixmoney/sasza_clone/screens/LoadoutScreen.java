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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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
	private Table tableLoadoutBase;
	private UIControllerInputHandler controllerInputHandler;
	private Label labelProfileName;
	private Button buttonBack;
	private Label labelGunName;
	private Label labelDamageValue;
	private Label labelFireRateValue;
	private Label labelCritChanceValue;
	private Label labelCritDamageValue;
	private Label labelMagSizeValue;
	private Label labelReloadTimeValue;
	private Label labelRangeValue;
	private Label labelBloomValue;
	private Label labelPenetrationValue;
	private Label labelMovementPenaltyValue;

	private int selectedSlot;
	private Gun selectedGun;
	private Array<Image> loadoutImages;
	private Array<Label> loadoutLabels;
	private Table tableLoadout;
	private Table tableProfileWeapons;
	private Array<Container<Image>> loadoutContainers;

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
		loadoutImages = new Array<>(true, 3);
		loadoutContainers = new Array<>(true, 3);
		loadoutLabels = new Array<>(true, 3);

		tableLoadoutBase = new Table(skin);
		tableLoadoutBase.setFillParent(true);
		tableLoadoutBase.pad(5);
		tableLoadoutBase.defaults().space(5);
		tableLoadoutBase.setBackground(skin.getDrawable("bg-tile-ten"));

		// row start //
		Label title = new Label("Loadout", skin);
		title.setAlignment(Align.center);
		tableLoadoutBase.add(title).colspan(2).height(100).expandX();

		tableLoadout = new Table(skin); // table for currently equipped items
		tableProfileWeapons = new Table(skin); // table for all items in account
		tableProfileWeapons.defaults().growX();

		// row start //
		tableLoadoutBase.row();
		for (int i = 0; i < 3; i++) {
			Table tableLoadoutInner = new Table(skin);
			tableLoadoutInner.setTouchable(Touchable.enabled);
			if (i == 0) {
				tableLoadoutInner.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("health_bar"), 1, 1, 1, 1)));
			} else {
				tableLoadoutInner.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
			}
			Label slotName = new Label("Slot " + (i + 1), skin);
			slotName.setAlignment(Align.center);
			tableLoadoutInner.add(slotName).fill().expandX();
			tableLoadoutInner.row();
			loadoutImages.add(new Image(null, Scaling.fit));
			loadoutContainers.add(new Container<>(loadoutImages.get(i)));
			tableLoadoutInner.add(loadoutContainers.get(i)).width(200).height(75).align(Align.center);
			loadoutLabels.add(new Label("", skin));
			loadoutLabels.get(i).setAlignment(Align.center);
			if (profile.getLoadout().get(i) != null) {
				Gun gun = profile.getLoadout().get(i);
				loadoutImages.get(i).setDrawable(new TextureRegionDrawable(gun.getWeaponSprite()));
				loadoutImages.get(i).setDrawable(new TextureRegionDrawable(gun.getWeaponSprite()));
				loadoutContainers.get(i).width(gun.getWeaponSprite().getRegionWidth() * 4).height(gun.getWeaponSprite().getRegionHeight() * 4);
				loadoutLabels.get(i).setText(gun.getName());
			}
			tableLoadoutInner.row();
			tableLoadoutInner.add(loadoutLabels.get(i)).fill().expandX();
			int finalI = i;
			tableLoadoutInner.addListener(new InputListener() {
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					super.enter(event, x, y, pointer, fromActor);
					if (selectedSlot != finalI) {
						tableLoadoutInner.background(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("reload_bar"), 1, 1, 1, 1)));
					}
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					super.exit(event, x, y, pointer, toActor);
					if (selectedSlot != finalI) {
						tableLoadoutInner.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
					}
				}
			});
			tableLoadoutInner.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					super.touchDown(event, x, y, pointer, button);
					selectedSlot = finalI;
					if (profile.getLoadout().get(selectedSlot) != null) {
						setWeaponDetails(profile.getLoadout().get(selectedSlot));
					}
					selectedGun = null;

					for (Cell<Table> cell: tableLoadout.getCells()) {
						cell.getActor().setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
					}
					for (Cell<Table> cell: tableProfileWeapons.getCells()) {
						cell.getActor().setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
					}

					tableLoadoutInner.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("health_bar"), 1, 1, 1, 1)));

					return true;
				}
			});
			tableLoadout.add(tableLoadoutInner);
		}
		tableLoadoutBase.add(tableLoadout).colspan(2).height(120).expandX();
		tableLoadoutBase.defaults().space(5).grow();


		// row start //
		tableLoadoutBase.row();
		for (Gun gun: profile.getProfileGuns()) {
			tableProfileWeapons.row();
			Table tableProfileWeaponsInner = new Table(skin);
			tableProfileWeaponsInner.setTouchable(Touchable.enabled);
			tableProfileWeaponsInner.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
			Label gunName = new Label(gun.getName(), skin);
			gunName.setAlignment(Align.center);
			tableProfileWeaponsInner.add(gunName).fill().expandX();
			Image gunSprite = new Image(new TextureRegionDrawable(gun.getWeaponSprite()), Scaling.fit);
			Container<Image> gunContainer = new Container<>(gunSprite);
			gunContainer.width(gun.getWeaponSprite().getRegionWidth() * 4).height(gun.getWeaponSprite().getRegionHeight() * 4);
			tableProfileWeaponsInner.add(gunContainer).align(Align.center).width(200).height(75);
			tableProfileWeaponsInner.addListener(new InputListener() {
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					super.enter(event, x, y, pointer, fromActor);
					if (selectedGun != gun) {
						tableProfileWeaponsInner.background(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("reload_bar"), 1, 1, 1, 1)));
					}
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					super.exit(event, x, y, pointer, toActor);
					if (selectedGun != gun) {
						tableProfileWeaponsInner.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
					}
				}
			});
			tableProfileWeaponsInner.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					super.touchDown(event, x, y, pointer, button);
					selectedGun = gun;
					setWeaponDetails(gun);

					for (Cell<Table> cell: tableProfileWeapons.getCells()) {
						cell.getActor().setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
					}

					tableProfileWeaponsInner.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("health_bar"), 1, 1, 1, 1)));

					return true;
				}
			});
			tableProfileWeapons.add(tableProfileWeaponsInner);
		}
		Container<Table> containerProfileWeapons = new Container<Table>(tableProfileWeapons);
		containerProfileWeapons.align(Align.top).fillX();
		ScrollPane scrollPaneProfileWeapons = new ScrollPane(containerProfileWeapons, skin);
		scrollPaneProfileWeapons.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				stage.setScrollFocus(scrollPaneProfileWeapons);
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				stage.setScrollFocus(null);
			}
		});
		tableLoadoutBase.add(scrollPaneProfileWeapons);

		Table tableWeaponInfo = new Table(skin);
		tableWeaponInfo.setBackground(new NinePatchDrawable(new NinePatch(Assets.get_instance().getPrivateAtlas().findRegion("background_bar"), 1, 1, 1, 1)));
		tableWeaponInfo.defaults().expand();
		labelGunName = new Label("", skin);
		labelGunName.setAlignment(Align.center);
		tableWeaponInfo.add(labelGunName).colspan(2);

		tableWeaponInfo.row();
		Label labelDamage = new Label("Damage", skin);
		labelDamageValue = new Label("", skin);
		tableWeaponInfo.add(labelDamage);
		tableWeaponInfo.add(labelDamageValue);

		tableWeaponInfo.row();
		Label labelFireRate = new Label("Fire rate", skin);
		labelFireRateValue = new Label("", skin);
		tableWeaponInfo.add(labelFireRate);
		tableWeaponInfo.add(labelFireRateValue);

		tableWeaponInfo.row();
		Label labelCritChance = new Label("Crit Chance", skin);
		labelCritChanceValue = new Label("", skin);
		tableWeaponInfo.add(labelCritChance);
		tableWeaponInfo.add(labelCritChanceValue);

		tableWeaponInfo.row();
		Label labelCritDamage = new Label("Crit Damage", skin);
		labelCritDamageValue = new Label("", skin);
		tableWeaponInfo.add(labelCritDamage);
		tableWeaponInfo.add(labelCritDamageValue);

		tableWeaponInfo.row();
		Label labelMagSize = new Label("Mag Size", skin);
		labelMagSizeValue = new Label("", skin);
		tableWeaponInfo.add(labelMagSize);
		tableWeaponInfo.add(labelMagSizeValue);

		tableWeaponInfo.row();
		Label labelReloadTime = new Label("Reload Time", skin);
		labelReloadTimeValue = new Label("", skin);
		tableWeaponInfo.add(labelReloadTime);
		tableWeaponInfo.add(labelReloadTimeValue);

		tableWeaponInfo.row();
		Label labelRange = new Label("Range", skin);
		labelRangeValue = new Label("", skin);
		tableWeaponInfo.add(labelRange);
		tableWeaponInfo.add(labelRangeValue);

		tableWeaponInfo.row();
		Label labelBloom = new Label("Bloom", skin);
		labelBloomValue = new Label("", skin);
		tableWeaponInfo.add(labelBloom);
		tableWeaponInfo.add(labelBloomValue);

		tableWeaponInfo.row();
		Label labelPenetration = new Label("Penetration", skin);
		labelPenetrationValue = new Label("", skin);
		tableWeaponInfo.add(labelPenetration);
		tableWeaponInfo.add(labelPenetrationValue);

		tableWeaponInfo.row();
		Label labelMovementPenalty = new Label("Movement Penalty", skin);
		labelMovementPenaltyValue = new Label("", skin);
		tableWeaponInfo.add(labelMovementPenalty);
		tableWeaponInfo.add(labelMovementPenaltyValue);

		if (profile.getLoadout().get(selectedSlot) != null) {
			setWeaponDetails(profile.getLoadout().get(selectedSlot));
		}

		tableWeaponInfo.row();
		TextButton buttonSwitchWeapon = new TextButton("Select Weapon", skin);
		buttonSwitchWeapon.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (selectedGun == null) {
					return;
				}

				for (Gun gun: profile.getLoadout()) {
					if (gun != null && gun.getItemId() == selectedGun.getItemId()) {
						int slot = profile.getLoadout().indexOf(gun, true);
						loadoutLabels.get(slot).setText("");
						loadoutImages.get(slot).setDrawable(null);
						profile.getLoadout().set(slot, null);
						break;
					}
				}

				profile.getLoadout().set(selectedSlot, selectedGun);
				profile.setLoadout(profile.getLoadout());
				loadoutLabels.get(selectedSlot).setText(selectedGun.getName());
				loadoutImages.get(selectedSlot).setDrawable(new TextureRegionDrawable(selectedGun.getWeaponSprite()));
				loadoutContainers.get(selectedSlot).width(selectedGun.getWeaponSprite().getRegionWidth() * 4).height(selectedGun.getWeaponSprite().getRegionHeight() * 4);
			}
		});
		tableWeaponInfo.add(buttonSwitchWeapon);
		TextButton buttonRemoveWeapon = new TextButton("Remove Weapon", skin);
		buttonRemoveWeapon.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				loadoutLabels.get(selectedSlot).setText("");
				loadoutImages.get(selectedSlot).setDrawable(null);
				profile.getLoadout().set(selectedSlot, null);
				profile.setLoadout(profile.getLoadout());
			}
		});
		tableWeaponInfo.add(buttonRemoveWeapon);
		tableLoadoutBase.add(tableWeaponInfo).width(stage.getWidth() / 3 * 2);


		tableLoadoutBase.pack();
		stage.addActor(tableLoadoutBase);

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
		labelCritChanceValue.setText(String.valueOf(gun.getCritChance()));
		labelCritDamageValue.setText(String.valueOf(gun.getCritDamage()));
		labelMagSizeValue.setText(String.valueOf(gun.getMagazineSize()));
		labelReloadTimeValue.setText(String.valueOf(gun.getReloadTime()));
		labelRangeValue.setText(String.valueOf(gun.getRange()));
		labelBloomValue.setText(String.valueOf(gun.getBloom()));
		labelPenetrationValue.setText(String.valueOf(gun.getPenetration()));
		labelMovementPenaltyValue.setText(String.valueOf(gun.getMovementPenalty()));
	}
}