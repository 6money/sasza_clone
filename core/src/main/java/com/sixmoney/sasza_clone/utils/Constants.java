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
    public static final int BACKGROUND_FPS = 30;
    public static final int FORGROUND_FPS = 144;
    public static final boolean V_SYNC = false;
    public static final String SKIN_PATH = "ui/uiskin.json";
    public static final String PREFERENCES_NAME = "sasza_clone";
    public static final String TEXTURE_ATLAS = "packaged/sasza_clone.pack.atlas";
    public static final String TEXTURE_ATLAS_PRIVATE = "packagedPrivate/sasza_clone_private.atlas";
    public static final String VERSION = "0.0.1-alpha";

    // debug sprites
    public static final String BBOX_OUTLINE = "bbox_pixel";

    // player
    public static final float PLAYER_SPEED = 200;
    public static final Vector2 PLAYER_CENTER = new Vector2(32, 32);

    // player sprites
    public static final String PLAYER = "Hero_Rifle";
    public static final String PLAYER_PLACEHOLDER = "particle_pixel";
    public static final String RIFLE_PROJECTILE = "GunnerBullet";

    // environment sprites
    public static final String BOX_2 = "TDS04_0013_Box-02";

    // tile sprites
    public static final String GRASS_TO_ROAD = "_0006_GrassToRoad";
    public static final String DIRT_TO_ROAD = "_0008_DirtToRoad";
    public static final String SAND_TO_ROAD = "_0007_SandToRoad";
    public static final String GRASS = "_0003_GrassTiles";
    public static final String DIRT = "_0001_DirtTiles";
    public static final String SAND = "_0002_SandTiles";
    public static final String WATER = "_0000_WTiles";

    // HUD
    public static final float HUD_MARGIN = 23;

    // level loading
    public static final String LEVEL_DIR = "levels";
    public static final String LEVEL_FILE_EXTENSION = ".dt";
    public static final String LEVEL_COMPOSITE = "composite";
    public static final String LEVEL_9PATCHES = "sImage9patchs";
    public static final String LEVEL_IMAGES = "sImages";
    public static final String LEVEL_ZINDEX = "zIndex";
    public static final String LEVEL_LAYER_NAME = "layerName";
    public static final String LEVEL_ERROR_MESSAGE = "There was a problem loading the level.";
    public static final String LEVEL_IMAGENAME_KEY = "imageName";
    public static final String LEVEL_X_KEY = "x";
    public static final String LEVEL_Y_KEY = "y";
    public static final String LEVEL_WIDTH_KEY = "width";
    public static final String LEVEL_HEIGHT_KEY = "height";
    public static final String LEVEL_IDENTIFIER_KEY = "itemIdentifier";
    public static final String LEVEL_CUSTOM_VARS = "customVars";

    // Overlays
    public static final String PAUSED_MESSAGE = "PAUSED";
    public static final String RESUME_MESSAGE = "RESUME";
    public static final String QUIT_MESSAGE = "QUIT";
}
