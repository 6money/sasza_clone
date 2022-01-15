package com.sixmoney.sasza_clone.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.staticData.Constants;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
	public static void main(String[] args) {
		createApplication(args);
	}

	private static LwjglApplication createApplication(String[] args) {
		return new LwjglApplication(new Sasza(args), getDefaultConfiguration());
	}

	private static LwjglApplicationConfiguration getDefaultConfiguration() {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "sasza_clone";
		configuration.width = Constants.WINDOW_WIDTH;
		configuration.height = Constants.WINDOW_HEIGHT;
		configuration.backgroundFPS = Constants.BACKGROUND_FPS;
		configuration.foregroundFPS = Constants.FOREGROUND_FPS;
		configuration.vSyncEnabled = Constants.V_SYNC;
		//// This prevents a confusing error that would appear after exiting normally.
		configuration.forceExit = false;
		
		for (int size : new int[] { 128, 64, 32, 16 }) {
			configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
		}
		return configuration;
	}
}