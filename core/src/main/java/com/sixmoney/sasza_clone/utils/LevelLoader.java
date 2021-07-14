package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.entities.Crate;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.FloorTile;
import com.sixmoney.sasza_clone.entities.Player;

import java.io.File;

public class LevelLoader {
    public static final String TAG = LevelLoader.class.toString();


    public static Level load(String levelName, Viewport viewport) {
        Array<JsonValue> tiles = new Array<>();
        Array<JsonValue> other = new Array<>();
        Level level = new Level(viewport);
        String path = Constants.LEVEL_DIR + File.separator + levelName + Constants.LEVEL_FILE_EXTENSION;


        try {
            FileHandle fileHandle = Gdx.files.internal(path);
            JsonValue jsonRoot = new JsonReader().parse(fileHandle);
            JsonValue compositeObj = jsonRoot.get(Constants.LEVEL_COMPOSITE);
            JsonValue sImages = compositeObj.get(Constants.LEVEL_IMAGES);

            for (JsonValue jsonObject : sImages) {
                final String layer = jsonObject.getString(Constants.LEVEL_LAYER_NAME, "Default");

                if (layer.equals("Default")) {
                    other.add(jsonObject);
                } else {
                    tiles.add(jsonObject);
                }
            }

            loadImages(tiles, level);
            loadOthers(other, level);
        } catch (Exception ex) {
            Gdx.app.error(TAG, ex.getMessage());
            Gdx.app.log(TAG, Constants.LEVEL_ERROR_MESSAGE);
        }

        return level;
    }


    private static void loadImages(Array<JsonValue> tiles, Level level) {
        Array<FloorTile> grassTileArray = new Array<>();
        Array<FloorTile> dirtTileArray = new Array<>();
        Array<FloorTile> sandTileArray = new Array<>();
        Array<FloorTile> waterTileArray = new Array<>();

        for (JsonValue tileObjects : tiles) {
            final float x = tileObjects.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = tileObjects.getFloat(Constants.LEVEL_Y_KEY, 0);

            FloorTile floorTile;

            switch (tileObjects.getString(Constants.LEVEL_IMAGENAME_KEY)) {
                case Constants.GRASS:
                    floorTile = new FloorTile(x, y, Constants.GRASS);
                    grassTileArray.add(floorTile);
                    break;
                case Constants.DIRT:
                    floorTile = new FloorTile(x, y, Constants.DIRT);
                    dirtTileArray.add(floorTile);
                    break;
                case Constants.SAND:
                    floorTile = new FloorTile(x, y, Constants.SAND);
                    sandTileArray.add(floorTile);
                    break;
                case Constants.WATER:
                    floorTile = new FloorTile(x, y, Constants.WATER);
                    waterTileArray.add(floorTile);
                    break;
                default:
                    continue;
            }

            Gdx.app.log(TAG, floorTile.toString());
        }

        level.setGrassTiles(grassTileArray);
        level.setDirtTiles(dirtTileArray);
        level.setSandTiles(sandTileArray);
        level.setWaterTiles(waterTileArray);
    }

    private static void loadOthers(Array<JsonValue> objects, Level level) {
        Array<Entity> environmentArray = new Array<>();

        for (JsonValue object : objects) {
            final float x = object.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = object.getFloat(Constants.LEVEL_Y_KEY, 0);

            switch (object.getString(Constants.LEVEL_IMAGENAME_KEY)) {
                case Constants.PLAYER:
                    Player player = new Player(x, y);
                    level.setPlayer(player);
                    break;
                case Constants.BOX_2:
                    Crate crate = new Crate(x, y);
                    environmentArray.add(crate);
                    break;
            }
        }

        level.setEnvironmentEntities(environmentArray);
    }
}
