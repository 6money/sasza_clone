package com.sixmoney.sasza_clone;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.sixmoney.sasza_clone.screens.MainMenu;
import com.sixmoney.sasza_clone.screens.OptionsScreen;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Profile;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Sasza extends Game {
	private static final String TAG = Sasza.class.getName();
	private final String[] args;
	private Assets assets;

	public boolean debug;
	public boolean mobileControls;
	public Profile profile;

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

//		Gdx.app.setLogLevel(Application.LOG_INFO);
		Gdx.app.log(TAG, ("debug mode: " + debug));
		Gdx.input.setCatchKey(Input.Keys.SPACE, true);
		Gdx.input.setCatchKey(Input.Keys.TAB, true);

		if (Gdx.app.getType() == Application.ApplicationType.Android) {
			mobileControls = true;
		}

		assets = Assets.get_instance();
		loadProfile();
		setCursor();
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

	private void setCursor() {
		Pixmap cursorPixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
		cursorPixmap.setColor(1, 0, 0, 0.4f);
		// center
		cursorPixmap.drawPixel(16, 16);
		cursorPixmap.drawPixel(16, 17);
		cursorPixmap.drawPixel(17, 16);
		cursorPixmap.drawPixel(17, 17);
		// left
		cursorPixmap.drawLine(0, 16, 12, 16);
		cursorPixmap.drawLine(0, 17, 12, 17);
		// right
		cursorPixmap.drawLine(21, 16, 32, 16);
		cursorPixmap.drawLine(21, 17, 32, 17);
		// bottom
		cursorPixmap.drawLine(16, 0, 16, 12);
		cursorPixmap.drawLine(17, 0, 17, 12);
		// top
		cursorPixmap.drawLine(16, 21, 16, 32);
		cursorPixmap.drawLine(17, 21, 17, 32);

		Cursor cursor = Gdx.graphics.newCursor(cursorPixmap, 16, 16);
		Gdx.graphics.setCursor(cursor);
		cursorPixmap.dispose();
	}

	public void loadProfile() {
		profile = new Profile();
	}
}