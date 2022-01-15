package com.sixmoney.sasza_clone;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.sixmoney.sasza_clone.screens.LoadoutScreen;
import com.sixmoney.sasza_clone.screens.MainMenuScreen;
import com.sixmoney.sasza_clone.screens.OptionsScreen;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.PreferenceManager;
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
		// set app log level
		Gdx.app.setLogLevel(Application.LOG_ERROR);

		// update config based on args
		if (args != null) {
			for (String arg : args) {
				if (arg.equals("debug")) {
					Gdx.app.setLogLevel(Application.LOG_DEBUG);
					debug = true;
				} else if (arg.equals("info")) {
					Gdx.app.setLogLevel(Application.LOG_INFO);
				} else {
					Gdx.app.setLogLevel(Application.LOG_ERROR);
				}
				if (arg.equals("mobile_browser")) {
					mobileControls = true;
				}
			}
		}

		// set catch keys for html build
		Gdx.input.setCatchKey(Input.Keys.SPACE, true);
		Gdx.input.setCatchKey(Input.Keys.TAB, true);

		// set vsync and target fps
		boolean vsync = Constants.V_SYNC;
		int fps = Constants.FORGROUND_FPS;
		if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
			vsync = PreferenceManager.get_instance().getVSync();
			fps = PreferenceManager.get_instance().getFps();
			if (fps == 150) {
				fps = 0;
			}
		}
		Gdx.graphics.setVSync(vsync);
		Gdx.graphics.setForegroundFPS(fps);

		// load assets and user profile
		assets = Assets.get_instance();
		loadProfile();

		// does what is says
		setCursor();

		// enable mobile controls and gui scaling if on android
		if (Gdx.app.getType() == Application.ApplicationType.Android) {
			mobileControls = true;
			Assets.get_instance().skinAssets.skin.getFont("font").getData().scale(1.2f);
		}

		// set mobile controls based on user pref, if not yet set from args
		if (!mobileControls) {
			mobileControls = PreferenceManager.get_instance().getMobile();
		}

		Gdx.app.log(TAG, "debug mode: " + debug);
		Gdx.app.log(TAG, "Log Level: " + Gdx.app.getLogLevel());
		Gdx.app.log(TAG, "VSync enabled: " + vsync);
		Gdx.app.log(TAG, "target fps: " + fps);
		Gdx.app.log(TAG, "Mobile Controls: " + mobileControls);


		setScreen(new MainMenuScreen(this));
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
			case "loadout":
				setScreen(new LoadoutScreen(this));
				break;
			default:
				setScreen(new MainMenuScreen(this));
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