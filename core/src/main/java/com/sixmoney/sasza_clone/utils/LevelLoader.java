package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.sasza_clone.Level;
import com.sixmoney.sasza_clone.entities.FloorTile;

import java.io.File;

public class LevelLoader {
    public static final String TAG = LevelLoader.class.toString();


    public static Level load(String levelName, Viewport viewport) {

        Level level = new Level(viewport);
        String path = Constants.LEVEL_DIR + File.separator + levelName + Constants.LEVEL_FILE_EXTENSION;


        try {
            FileHandle fileHandle = Gdx.files.internal(path);
            JsonValue jsonRoot = new JsonReader().parse(fileHandle);
            JsonValue compositeObj = jsonRoot.get(Constants.LEVEL_COMPOSITE);
            JsonValue sImages = compositeObj.get(Constants.LEVEL_IMAGES);
            loadImages(sImages, level);
        } catch (Exception ex) {
            Gdx.app.error(TAG, ex.getMessage());
            Gdx.app.log(TAG, Constants.LEVEL_ERROR_MESSAGE);
        }

        return level;
    }


    private static void loadImages(JsonValue array, Level level) {
        Array<FloorTile> grassTileArray = new Array<>();
        Array<FloorTile> dirtTileArray = new Array<>();
        Array<FloorTile> sandTileArray = new Array<>();
        Array<FloorTile> waterTileArray = new Array<>();

        for (JsonValue platformObject : array) {
            final float x = platformObject.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = platformObject.getFloat(Constants.LEVEL_Y_KEY, 0);
            final float width = platformObject.getFloat(Constants.LEVEL_WIDTH_KEY, 0);
            final float height = platformObject.getFloat(Constants.LEVEL_HEIGHT_KEY, 0);
            final String layer = platformObject.getString(Constants.LEVEL_LAYER_NAME, "");

            FloorTile floorTile;


            if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.GRASS)) {
                floorTile = new FloorTile(x, y, Constants.GRASS);
                grassTileArray.add(floorTile);
            } else if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.DIRT)) {
                floorTile = new FloorTile(x, y, Constants.DIRT);
                dirtTileArray.add(floorTile);
            } else if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SAND)) {
                floorTile = new FloorTile(x, y, Constants.SAND);
                sandTileArray.add(floorTile);
            } else if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.WATER)) {
                floorTile = new FloorTile(x, y, Constants.WATER);
                waterTileArray.add(floorTile);
            } else {
                continue;
            }

            Gdx.app.log(TAG, floorTile.toString());
        }

        level.setGrassTiles(grassTileArray);
        level.setDirtTiles(dirtTileArray);
        level.setSandTiles(sandTileArray);
        level.setWaterTiles(waterTileArray);
    }
}
