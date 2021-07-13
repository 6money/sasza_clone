package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants {
    // world/window
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
//    public static final int WORLD_WIDTH = 960;
    public static final int WORLD_WIDTH = 640;
//    public static final int WORLD_HEIGHT = 540;
    public static final int WORLD_HEIGHT = 360;
    public static final Color BG_COLOR = Color.SKY;
    public static final int CHASE_CAM_SPEED = 200;
    public static final String SKIN_PATH = "ui/uiskin.json";
    public static final String PREFERENCES_NAME = "sasza_clone";
    public static final String TEXTURE_ATLAS = "packaged/sasza_clone.pack.atlas";
    public static final String TEXTURE_ATLAS_PRIVATE = "packagedPrivate/sasza_clone_private.pack.atlas";
    public static final String VERSION = "0.0.1-alpha";

    // player
    public static final float PLAYER_SPEED = 200;
    public static final Vector2 PLAYER_CENTER = new Vector2(32, 32);

    // player sprites
    public static final String PLAYER = "tds-modern-pixel-game-kit/tds-modern-hero-weapons-and-props/Hero_Pistol/Shot/Hero_Rifle_Fire";
    public static final String PLAYER_PLACEHOLDER = "particle_pixel";

    // world sprites
    public static final String BOX_2 = "tds-modern-pixel-game-kit/tds-modern-tilesets-environment/PNG/Crates Barrels/TDS04_0013_Box-02";

    // HUD
    public static final float HUD_MARGIN = 23;
}
