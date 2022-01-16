package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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

		tableMenu = new Table(skin);
		tableMenu.setFillParent(true);
		tableMenu.pad(5);
		tableMenu.defaults().space(5);
		tableMenu.setBackground(skin.getDrawable("bg-tile-ten"));

		// row start //
		Label title = new Label("Loadout", skin);
		title.setAlignment(Align.center);
		tableMenu.add(title).colspan(2).height(100).expandX();
		tableMenu.defaults().space(5).grow();

		tableMenu.row();
		// row start //
		Table gunTable = new Table(skin);
		gunTable.defaults().growX();
		for (Gun gun: profile.getGuns()) {
			gunTable.row();
			Table table = new Table(skin);
			Label gunName = new Label(gun.getName(), skin);
			gunName.setAlignment(Align.center);
			table.add(gunName).fill().expandX();
			Image gunSprite = new Image(gun.getWeaponSprite());
			table.add(gunSprite).width(gun.getWeaponSprite().getRegionWidth() * 4).height(gun.getWeaponSprite().getRegionHeight() * 4);
			table.addListener(new FocusListener(){
				@Override
				public boolean handle(Event event){
					if (event.toString().equals("mouseMoved")){
						table.background(new TextureRegionDrawable(Assets.get_instance().tileAssets.dirt));
						return false;
					}
					else if(event.toString().equals("exit")){
						table.setBackground((Drawable) null);
						return false;
					}
					return true;
				}
			});
			gunTable.add(table);
		}
		Container<Table> container = new Container<Table>(gunTable);
		container.align(Align.top).fillX();
		ScrollPane scrollPane = new ScrollPane(container, skin);
		tableMenu.add(scrollPane);


		Table table2 = new Table(skin);
		Label gun = new Label("Gun", skin);
		gun.setAlignment(Align.center);
		table2.add(gun);
		tableMenu.add(table2).width(stage.getWidth() / 3 * 2);



		tableMenu.validate();
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
}