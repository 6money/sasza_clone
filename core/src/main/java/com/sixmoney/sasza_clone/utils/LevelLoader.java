package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.entities.BaseEnemy;
import com.sixmoney.sasza_clone.entities.BaseNPC;
import com.sixmoney.sasza_clone.entities.Canopy;
import com.sixmoney.sasza_clone.entities.Crate;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.FloorTile;
import com.sixmoney.sasza_clone.entities.Player;
import com.sixmoney.sasza_clone.entities.Wall;

import java.io.File;

public class LevelLoader {
    public static final String TAG = LevelLoader.class.toString();


    public static Level load(String levelName, Viewport viewport) {
        Array<JsonValue> tiles = new Array<>();
        Array<JsonValue> characters = new Array<>();
        Array<JsonValue> enemies = new Array<>();
        Array<JsonValue> canopy = new Array<>();
        Array<JsonValue> environment = new Array<>();
        Array<JsonValue> walls = new Array<>();
        Level level = new Level(viewport);
        String path = Constants.LEVEL_DIR + File.separator + levelName + Constants.LEVEL_FILE_EXTENSION;


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
                        environment.add(jsonObject);
                        break;
                    case "Canopy":
                        canopy.add(jsonObject);
                        break;
                    case "Walls":
                        walls.add(jsonObject);
                        break;
                    default:
                        tiles.add(jsonObject);
                        break;
                }
            }

            loadTiles(tiles, level);
            loadCharacters(characters, level);
            loadEnemies(enemies, level);
            loadEnvironment(environment, level);
            loadCanopy(canopy, level);
            loadWalls(walls, level);
        } catch (Exception ex) {
            Gdx.app.error(TAG, ex.getMessage());
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
        Array<BaseNPC> enemyArray = new Array<>();

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
                case Constants.ENEMY:
                    BaseNPC baseNPC = new BaseNPC(x, y);
                    enemyArray.add(baseNPC);
                    Gdx.app.log(TAG, baseNPC.toString());
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

            BaseEnemy zom = new BaseEnemy(x, y);
            enemyArray.add(zom);
            Gdx.app.log(TAG, zom.toString());
        }

        level.setEnemyEntities(enemyArray);
    }

    private static void loadEnvironment(Array<JsonValue> objects, Level level) {
        Array<Entity> environmentArray = new Array<>();

        for (JsonValue object : objects) {
            final float x = object.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = object.getFloat(Constants.LEVEL_Y_KEY, 0);
            final String textureName = object.getString(Constants.LEVEL_IMAGENAME_KEY);

            Crate crate = new Crate(x, y, textureName);
            environmentArray.add(crate);
            Gdx.app.log(TAG, crate.toString());
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
}
