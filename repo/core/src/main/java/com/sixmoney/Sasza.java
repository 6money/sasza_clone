package com.sixmoney;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Sasza extends Game {
	@Override
	public void create() {
		setScreen(new FirstScreen());
	}
}