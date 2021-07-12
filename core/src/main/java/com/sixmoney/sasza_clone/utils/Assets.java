package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static Assets instance;

    private AssetManager assetManager;

    public PlayerAssets playerAssets;
    public SkinAssets skinAssets;

    private Assets() {
        init();
    }

    public static Assets get_instance() {
        if (instance == null) {
            instance = new Assets();
        }

        return instance;
    }

    private void init() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        long startLoad = TimeUtils.nanoTime();
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.load(Constants.TEXTURE_ATLAS_PRIVATE, TextureAtlas.class);
        assetManager.load(Constants.SKIN_PATH, Skin.class);
        assetManager.finishLoading();
        Gdx.app.log(TAG, "Assets Loaded in " + Utils.secondsSince(startLoad) + " seconds");
        Gdx.app.log(TAG, assetManager.getAssetNames().toString());

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        TextureAtlas atlasPrivate = assetManager.get(Constants.TEXTURE_ATLAS_PRIVATE);

        skinAssets = new SkinAssets(assetManager);
        playerAssets = new PlayerAssets(atlas, atlasPrivate);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public class SkinAssets {
        public Skin skin;

        public SkinAssets(AssetManager assetManager) {
            skin = assetManager.get(Constants.SKIN_PATH);
        }
    }

    public class PlayerAssets {
        public TextureRegion player;
        public TextureRegion playerPlaceholder;

        public PlayerAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            player = atlasPrivate.findRegion(Constants.PLAYER);
            playerPlaceholder = atlas.findRegion(Constants.PLAYER_PLACEHOLDER);
        }
    }
}
