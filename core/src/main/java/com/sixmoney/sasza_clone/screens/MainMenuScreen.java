package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.inputHandlers.UIControllerInputHandler;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Profile;

import de.golfgl.gdx.controllers.ControllerMenuStage;

public class MainMenuScreen implements Screen {
	public static final String TAG = MainMenuScreen.class.getName();

	private Sasza saszaGame;
	private Profile profile;
	private ControllerMenuStage stage;
	private Skin skin;
	private Table tableMenu;
	private UIControllerInputHandler controllerInputHandler;
	private Label labelProfileName;

	public MainMenuScreen(Sasza game) {
		saszaGame = game;
		saszaGame.loadProfile();
		profile = saszaGame.profile;
	}

	@Override
	public void show() {
		stage = new ControllerMenuStage(new ScreenViewport());
		skin = Assets.get_instance().skinAssets.skin;

		controllerInputHandler = new UIControllerInputHandler(stage);
		Controllers.addListener(controllerInputHandler);

		tableMenu = new Table(skin);
		tableMenu.setFillParent(true);
		tableMenu.pad(5);
		tableMenu.defaults().space(5).padLeft(50).padRight(50).padTop(30);
		tableMenu.setBackground(skin.getDrawable("bg-tile-ten"));

		tableMenu.row();
		Label title = new Label("SAS: ZA Clone", skin);
		title.setAlignment(Align.center);
		tableMenu.add(title).height(114f);
		tableMenu.defaults().space(5).padLeft(50).padRight(50).padTop(30).grow();

		tableMenu.row().uniform().fill();

		tableMenu.row();
		TextButton buttonPlay = new TextButton("PLAY", skin);
		buttonPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saszaGame.switchScreen("gameplay");
				dispose();
			}
		});
		tableMenu.add(buttonPlay).minHeight(100f);

		tableMenu.row();
		TextButton buttonLoadout = new TextButton("LOADOUT", skin);
		buttonLoadout.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saszaGame.switchScreen("loadout");
				dispose();
			}
		});
		tableMenu.add(buttonLoadout).minHeight(100f);

		tableMenu.row();
		Table tableSubmenu = new Table(skin);
		tableSubmenu.defaults().grow();
		tableSubmenu.row();
		TextButton buttonStats = new TextButton("STATS", skin);
//		buttonStats.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				gigaGalGame.switchScreen("high_score");
//				dispose();
//			}
//		});
		tableSubmenu.add(buttonStats).uniform().spaceRight(5).minHeight(100f);
		TextButton buttonOptions = new TextButton("OPTIONS", skin);
		buttonOptions.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saszaGame.switchScreen("options");
				dispose();
			}
		});
		tableSubmenu.add(buttonOptions).uniform().spaceLeft(5).minHeight(100f);
		tableMenu.add(tableSubmenu).minHeight(100f);

		tableMenu.validate();
		stage.addActor(tableMenu);
		stage.addFocusableActor(buttonPlay);
		stage.addFocusableActor(buttonLoadout);
		stage.addFocusableActor(buttonOptions);
		stage.addFocusableActor(buttonStats);
		stage.setFocusedActor(buttonPlay);

		labelProfileName = new Label(null, skin);
		if (!profile.getName().equals("default")) {
			labelProfileName.setText(profile.getName() + "\nLevel: " + profile.getProfileLevel());
		}
		labelProfileName.setPosition(20, stage.getHeight() - 30);
		stage.addActor(labelProfileName);

		if (profile.getName().equals("default")) {
			buttonPlay.setDisabled(true);
			buttonLoadout.setDisabled(true);
			buttonStats.setDisabled(true);
			buttonOptions.setDisabled(true);

			Window window = new Window("Create Profile", skin);
			window.defaults().pad(10).grow();

			window.row();
			window.add(new Label("Enter name", skin));

			window.row();
			TextField inputProfileName = new TextField(null, skin);
			window.add(inputProfileName);

			window.row();
			TextButton buttonSetProfileName = new TextButton("CONTINUE", skin);
			buttonSetProfileName.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					profile.setName(inputProfileName.getText());
					profile.setProfileLevel(profile.getProfileLevel());
					profile.setGuns(profile.getGuns());
					labelProfileName.setText(profile.getName() + "\nLevel: " + profile.getProfileLevel());
					window.remove();

					buttonPlay.setDisabled(false);
					buttonLoadout.setDisabled(false);
					buttonStats.setDisabled(false);
					buttonOptions.setDisabled(false);
				}
			});
			window.add(buttonSetProfileName);

			window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);
			window.pack();
			stage.addActor(window);
		}

		Gdx.input.setInputProcessor(stage);
	}


	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		labelProfileName.setPosition(20, stage.getHeight() - 30);
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
}