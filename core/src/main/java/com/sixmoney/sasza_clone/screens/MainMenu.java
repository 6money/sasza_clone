package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.InputHandlers.UIControllerInputHandler;

import de.golfgl.gdx.controllers.ControllerMenuStage;

public class MainMenu implements Screen {
	public static final String TAG = MainMenu.class.getName();

	private Sasza saszaGame;
	private ControllerMenuStage stage;
	private Skin skin;
	private Table tableMenu;
	private UIControllerInputHandler controllerInputHandler;

	public MainMenu(Sasza game) {
		saszaGame = game;
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
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saszaGame.switchScreen("gameplay");
				dispose();
			}
		});
		tableMenu.add(buttonPlay).minHeight(100f);

		tableMenu.row();
		TextButton levelSelectPlay = new TextButton("LOADOUT", skin);
//		levelSelectPlay.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				gigaGalGame.switchScreen("level select");
//				dispose();
//			}
//		});
		tableMenu.add(levelSelectPlay).minHeight(100f);

		tableMenu.row();
		Table tableSubmenu = new Table(skin);
		tableSubmenu.defaults().grow();
		tableSubmenu.row();
		TextButton buttonHighScores = new TextButton("STATS", skin);
//		buttonHighScores.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				gigaGalGame.switchScreen("high_score");
//				dispose();
//			}
//		});
		tableSubmenu.add(buttonHighScores).uniform().spaceRight(5).minHeight(100f);
		TextButton buttonOptions = new TextButton("OPTIONS", skin);
		buttonOptions.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saszaGame.switchScreen("options");
				dispose();
			}
		});
		tableSubmenu.add(buttonOptions).uniform().spaceLeft(5).minHeight(100f);
		tableMenu.add(tableSubmenu).minHeight(100f);
		tableMenu.validate();

		stage.addActor(tableMenu);
		stage.addFocusableActor(buttonPlay);
		stage.addFocusableActor(levelSelectPlay);
		stage.addFocusableActor(buttonOptions);
		stage.addFocusableActor(buttonHighScores);
		stage.setFocusedActor(buttonPlay);
		Gdx.input.setInputProcessor(stage);
	}


	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(
				Constants.BG_COLOR.r,
				Constants.BG_COLOR.g,
				Constants.BG_COLOR.b,
				Constants.BG_COLOR.a
		);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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