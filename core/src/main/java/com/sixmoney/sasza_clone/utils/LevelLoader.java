package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.entities.BaseEnemy;
import com.sixmoney.sasza_clone.entities.BaseSoldier;
import com.sixmoney.sasza_clone.entities.Canopy;
import com.sixmoney.sasza_clone.entities.EnemyTierThree;
import com.sixmoney.sasza_clone.entities.EnemyTierTwo;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.EnvironmentObject;
import com.sixmoney.sasza_clone.entities.FloorTile;
import com.sixmoney.sasza_clone.entities.GunnerSoldier;
import com.sixmoney.sasza_clone.entities.Player;
import com.sixmoney.sasza_clone.entities.RiflemanSoldier;
import com.sixmoney.sasza_clone.entities.Wall;
import com.sixmoney.sasza_clone.staticData.Constants;

import java.io.File;

public class LevelLoader {
    public static final String TAG = LevelLoader.class.toString();


    public static Level load(String levelName, Viewport viewport, ChaseCam camera) {
        Array<JsonValue> tiles = new Array<>();
        Array<JsonValue> characters = new Array<>();
        Array<JsonValue> enemies = new Array<>();
        Array<JsonValue> canopy = new Array<>();
        Array<JsonValue> environment = new Array<>();
        Array<JsonValue> vehicles = new Array<>();
        Array<JsonValue> walls = new Array<>();
        Array<JsonValue> spawns = new Array<>();
        Level level = new Level(levelName, viewport, camera);
        String path = Constants.LEVEL_DIR + File.separator + levelName + Constants.LEVEL_FILE_EXTENSION;
        Vector2 outerCorner = null;


        try {
            FileHandle fileHandle = Gdx.files.internal(path);
            JsonValue jsonRoot = new JsonReader().parse(fileHandle);
            JsonValue compositeObj = jsonRoot.get(Constants.LEVEL_COMPOSITE);
            JsonValue sImages = compositeObj.get(Constants.LEVEL_IMAGES);

            for (JsonValue jsonObject : sImages) {
                final String layer = jsonObject.getString(Constants.LEVEL_LAYER_NAME, "Default");
                switch (layer) {
                    case "Characters":
                        characters.add(jsonObject);
                        break;
                    case "Enemies":
                        enemies.add(jsonObject);
                        break;
                    case "Environment":
                    case "Vehicles":
                        environment.add(jsonObject);
                        break;
                    case "Canopy":
                        canopy.add(jsonObject);
                        break;
                    case "Walls":
                        walls.add(jsonObject);
                        break;
                    case "Spawns":
                        spawns.add(jsonObject);
                        break;
                    case "GroundFloor":
                    case "GroundRoad":
                    case "GroundWater":
                    case "GroundSand":
                    case "GroundGrass":
                    case "GroundDirt":
                        tiles.add(jsonObject);
                    default:
                        JsonValue tags = jsonObject.get(Constants.LEVEL_TAGS_KEY);
                        for (JsonValue tag : tags) {
                            if (tag.asString().equals(Constants.LEVEL_OUTERCORNER_KEY)) {
                                outerCorner = new Vector2(jsonObject.getFloat(Constants.LEVEL_X_KEY, 0), jsonObject.getFloat(Constants.LEVEL_Y_KEY, 0));
                                level.initPathHelpers(outerCorner);
                                Gdx.app.log(TAG, "outerCorner:" + outerCorner.toString());
                            }
                        }
                        break;
                }
            }

            if (outerCorner == null) {
                Gdx.app.log(TAG, "NO OUTERCORNER FOUND IN LEVEL");
            }

            loadTiles(tiles, level);
            loadCharacters(characters, level);
            loadEnemies(enemies, level);
            loadEnvironment(environment, level);
            loadCanopy(canopy, level);
            loadWalls(walls, level);
            loadSpawns(spawns, level);
        } catch (Exception ex) {
            Gdx.app.log(TAG, ex.getMessage());
            Gdx.app.log(TAG, Constants.LEVEL_ERROR_MESSAGE);
        }

        return level;
    }


    private static void loadTiles(Array<JsonValue> tiles, Level level) {
        Array<Entity> tileArray = new Array<>();

        for (JsonValue tileObjects : tiles) {
            final float x = tileObjects.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = tileObjects.getFloat(Constants.LEVEL_Y_KEY, 0);
            String textureName = tileObjects.getString(Constants.LEVEL_IMAGENAME_KEY);

            FloorTile floorTile;

            if (Constants.WATER.equals(textureName)) {
                floorTile = new FloorTile(x, y, textureName , true);
            } else {
                floorTile = new FloorTile(x, y, textureName, false);
            }

            tileArray.add(floorTile);
            Gdx.app.log(TAG, floorTile.toString());
        }

        level.setTiles(tileArray);
    }

    private static void loadCharacters(Array<JsonValue> objects, Level level) {
        Array<BaseSoldier> enemyArray = new Array<>();

        for (JsonValue object : objects) {
            final float x = object.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = object.getFloat(Constants.LEVEL_Y_KEY, 0);
            final String textureName = object.getString(Constants.LEVEL_IMAGENAME_KEY);

            switch (textureName) {
                case Constants.PLAYER:
                    Player player = new Player(x, y);
                    level.setPlayer(player);
                    Gdx.app.log(TAG, player.toString());
                    break;
                case Constants.SNIPER_BASE:
                    BaseSoldier sniperNPC = new BaseSoldier(x, y);
                    enemyArray.add(sniperNPC);
                    Gdx.app.log(TAG, sniperNPC.toString());
                    break;
                case Constants.RIFLEMAN_BASE:
                    BaseSoldier riflemanNPC = new RiflemanSoldier(x, y);
                    enemyArray.add(riflemanNPC);
                    Gdx.app.log(TAG, riflemanNPC.toString());
                    break;
                case Constants.GUNNER_BASE:
                    BaseSoldier gunnerNPC = new GunnerSoldier(x, y);
                    enemyArray.add(gunnerNPC);
                    Gdx.app.log(TAG, gunnerNPC.toString());
                    break;
            }
        }

        level.setCharacterEntities(enemyArray);
    }

    private static void loadEnemies(Array<JsonValue> objects, Level level) {
        Array<BaseEnemy> enemyArray = new Array<>();

        for (JsonValue object : objects) {
            final float x = object.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = object.getFloat(Constants.LEVEL_Y_KEY, 0);
            final String textureName = object.getString(Constants.LEVEL_IMAGENAME_KEY);

            BaseEnemy zom;
            if (textureName.equals(Constants.ZOM2)) {
                zom = new EnemyTierTwo(x, y);
            } else if (textureName.equals(Constants.ZOM3)) {
                zom = new EnemyTierThree(x, y);
            } else {
                zom = new BaseEnemy(x, y);
            }
            enemyArray.add(zom);
            Gdx.app.log(TAG, zom.toString());
        }

        level.setEnemyEntities(enemyArray);
    }

    private static void loadEnvironment(Array<JsonValue> objects, Level level) {
        Array<EnvironmentObject> environmentArray = new Array<>();

        for (JsonValue object : objects) {
            final float x = object.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = object.getFloat(Constants.LEVEL_Y_KEY, 0);
            final String textureName = object.getString(Constants.LEVEL_IMAGENAME_KEY);
            float rotation = 0;
            boolean explicitRotate = false;
            try {
                if (textureName.contains("vehicle")) {
                    explicitRotate = true;
                } else {
                    JsonValue tags = object.get(Constants.LEVEL_TAGS_KEY);
                    for (JsonValue tag : tags) {
                        if (tag.asString().equals(Constants.LEVEL_EXPLICIT_ROTATION_KEY)) {
                            explicitRotate = true;
                        }
                    }
                }
                rotation = object.getFloat(Constants.LEVEL_ROTATION_KEY);
            } catch (IllegalArgumentException e) {
                rotation = 0;
            }

            EnvironmentObject environmentObject;

            if (explicitRotate) {
                environmentObject = new EnvironmentObject(x, y, textureName, true, rotation);
            } else {
                environmentObject = new EnvironmentObject(x, y, textureName);
            }

            environmentArray.add(environmentObject);
            Gdx.app.log(TAG, environmentObject.toString());
        }

        level.setEnvironmentEntities(environmentArray);
    }

    private static void loadCanopy(Array<JsonValue> objects, Level level) {
        Array<Entity> canopyArray = new Array<>();

        for (JsonValue object : objects) {
            final float x = object.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = object.getFloat(Constants.LEVEL_Y_KEY, 0);
            final String textureName = object.getString(Constants.LEVEL_IMAGENAME_KEY);

            Canopy canopy = new Canopy(x, y, textureName);
            canopyArray.add(canopy);
            Gdx.app.log(TAG, canopy.toString());
        }

        level.setCanopyEntities(canopyArray);
    }

    private static void loadWalls(Array<JsonValue> objects, Level level) {
        Array<Entity> wallArray = new Array<>();

        for (JsonValue object : objects) {
            final float x = object.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = object.getFloat(Constants.LEVEL_Y_KEY, 0);
            final String textureName = object.getString(Constants.LEVEL_IMAGENAME_KEY);
            boolean isDoor = false;

            if (textureName.contains("door")) {
                isDoor = true;
            }

            Wall wall = new Wall(x, y, textureName, isDoor);
            wallArray.add(wall);
            Gdx.app.log(TAG, wall.toString());
        }

        level.setWallEntities(wallArray);
    }

    private static void loadSpawns(Array<JsonValue> objects, Level level) {
        Array<Vector2> spawnArray = new Array<>();

        for (JsonValue object : objects) {
            final float x = object.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = object.getFloat(Constants.LEVEL_Y_KEY, 0);

            spawnArray.add(new Vector2(x, y));
            Gdx.app.log(TAG, "Spawn point at " + x + ", " + y);
        }

        level.setSpawnPoints(spawnArray);
    }
}
