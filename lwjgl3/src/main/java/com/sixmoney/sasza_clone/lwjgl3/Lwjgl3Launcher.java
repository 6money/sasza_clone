package com.sixmoney.sasza_clone.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.utils.Constants;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
	public static void main(String[] args) {
		createApplication(args);
	}

	private static Lwjgl3Application createApplication(String[] args) {
		return new Lwjgl3Application(new Sasza(args), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("sasza_clone");
		configuration.setWindowedMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		configuration.setIdleFPS(Constants.BACKGROUND_FPS);
		configuration.setForegroundFPS(Constants.FORGROUND_FPS);
		configuration.useVsync(Constants.V_SYNC);
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		return configuration;
	}
}