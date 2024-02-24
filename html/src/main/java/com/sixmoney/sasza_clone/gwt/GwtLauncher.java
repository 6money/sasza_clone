package com.sixmoney.sasza_clone.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.sixmoney.sasza_clone.Sasza;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
		@Override
		public GwtApplicationConfiguration getConfig () {
			// Resizable application, uses available space in browser
			GwtApplicationConfiguration config = new GwtApplicationConfiguration(true);
			config.useGL30 = true;

			return config;
			// Fixed size application:
			//return new GwtApplicationConfiguration(480, 320);
		}

		@Override
		public ApplicationListener createApplicationListener () {
			String[] args;

			if (GwtApplication.isMobileDevice()) {
				args = new String[]{"mobile_browser"};
			} else {
				args = new String[]{};
			}

			return new Sasza(args);
		}
}
