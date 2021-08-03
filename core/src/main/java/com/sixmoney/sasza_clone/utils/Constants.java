package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants {
    // world/window
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
//    public static final int WORLD_WIDTH_2 = 960;
    public static final int WORLD_WIDTH = 640;
//    public static final int WORLD_HEIGHT_2 = 540;
    public static final int WORLD_HEIGHT = 360;
    public static final Color BG_COLOR = Color.SKY;
    public static final int BACKGROUND_FPS = 30;
    public static final int FORGROUND_FPS = 144;
    public static final boolean V_SYNC = false;
    public static final int BBOX_BUFFER_ENVIRONMENT = 5;
    public static final int BBOX_BUFFER_WALL = 2;
    public static final String SKIN_PATH = "ui/shimmer-ui.json";
    public static final String SKIN_CONSOLE_PATH = "ui/console/uiskin.json";
    public static final String PREFERENCES_NAME = "sasza_clone";
    public static final String TEXTURE_ATLAS = "packaged/sasza_clone.pack.atlas";
    public static final String TEXTURE_ATLAS_PRIVATE = "packagedPrivate/sasza_clone_private.atlas";
    public static final String VERSION = "0.0.1-alpha";


    // debug sprites
    public static final String BBOX_OUTLINE = "bbox_pixel";

    // player
    public static final float DEFAULT_PLAYER_SPEED = 200;
    public static final Vector2 PLAYER_CENTER = new Vector2(32, 32);

    // enemy
    public static final float ENEMY_ATTACK_SPEED = 0.5f;

    // player sprites
    public static final String PLAYER = "player_rifle";
    public static final String PLAYER_SHOOTING = "player_rifle_fire";
    public static final String PLAYER_WALK = "player_walk";
    public static final String PLAYER_STAND = "player_stand";
    public static final String PLAYER_PLACEHOLDER = "particle_pixel";
    public static final String RIFLE_PROJECTILE = "player_bullet";

    // enemy sprites
    public static final String SNIPER_BASE = "sniper_base";
    public static final String SNIPER_WALK = "sniper_walk";
    public static final String SNIPER_DEAD = "sniper_die";
    public static final String SNIPER_STAND = "sniper_stand";
    public static final String SNIPER_SHOOTING = "sniper_shot";
    public static final String ZOM1 = "zom1_base";
    public static final String ZOM1_DEAD = "zom1_die";

    // environment sprites
    public static final String BOX_1 = "environment_box1";
    public static final String BOX_2 = "environment_box2";
    public static final String BOX_1_MINI = "environment_box1_mini";
    public static final String BOX_2_MINI = "environment_box2_mini";
    public static final String BARREL = "environment_barrel";
    public static final String BARREL_OIL = "environment_barrel_oil";
    public static final String ROCK_1 = "environment_rock1";
    public static final String ROCK_2 = "environment_rock2";
    public static final String ROCK_3 = "environment_rock3";
    public static final String STUMP_1 = "environment_tree5";
    public static final String STUMP_2 = "environment_tree6";
    public static final String STUMP_3 = "environment_tree7";

    // canopy sprites
    public static final String TREE_1 = "canopy_tree1";
    public static final String TREE_2 = "canopy_tree2";
    public static final String TREE_3 = "canopy_tree3";
    public static final String TREE_4 = "canopy_tree4";

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
