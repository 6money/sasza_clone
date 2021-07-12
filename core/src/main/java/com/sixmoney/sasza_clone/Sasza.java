package com.sixmoney.sasza_clone;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.sixmoney.sasza_clone.screens.MainMenu;
import com.sixmoney.sasza_clone.screens.OptionsScreen;
import com.sixmoney.sasza_clone.utils.Assets;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Sasza extends Game {
	private static final String TAG = Sasza.class.getName();
	private final String[] args;
	private boolean debug;
	private Assets assets;

	public boolean mobileControls;

	public Sasza(String[] args) {
		this.args = args;
		debug = false;
		mobileControls = false;
	}

	@Override
	public void create() {
		if (args != null) {
			for (String arg : args) {
				if (arg.equals("debug")) {
					Gdx.app.setLogLevel(Application.LOG_DEBUG);
					debug = true;
				} else {
					Gdx.app.setLogLevel(Application.LOG_ERROR);
				}
				if (arg.equals("mobile_browser")) {
					mobileControls = true;
				}
			}
		}
		Gdx.app.log(TAG, ("debug mode: " + debug));
		Gdx.input.setCatchKey(Input.Keys.SPACE, true);

		if (Gdx.app.getType() == Application.ApplicationType.Android) {
			mobileControls = true;
		}

		assets = Assets.get_instance();

		setScreen(new MainMenu(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		screen.dispose();
		assets.dispose();
	}

	public void switchScreen(String screen_name) {
		Gdx.input.setInputProcessor(null);

		switch (screen_name) {
			case "gameplay":
				setScreen(new GameWorldScreen(this));
				break;
			case "options":
				setScreen(new OptionsScreen(this));
				break;
			default:
				setScreen(new MainMenu(this));
				break;
		}
	}
}