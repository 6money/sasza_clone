package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static Assets instance;

    private AssetManager assetManager;
    private TextureAtlas atlas;
    private TextureAtlas atlasPrivate;

    public DebugAssets debugAssets;
    public PlayerAssets playerAssets;
    public EnemyAssets enemyAssets;
    public EnvronmentAssets envronmentAssets;
    public TileAssets tileAssets;
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

        atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        atlasPrivate = assetManager.get(Constants.TEXTURE_ATLAS_PRIVATE);

        skinAssets = new SkinAssets(assetManager);
        debugAssets = new DebugAssets(atlas);
        playerAssets = new PlayerAssets(atlas, atlasPrivate);
        enemyAssets = new EnemyAssets(atlas, atlasPrivate);
        envronmentAssets = new EnvronmentAssets(atlas, atlasPrivate);
        tileAssets = new TileAssets(atlasPrivate);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TextureAtlas getPrivateAtlas() {
        return atlasPrivate;
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

    public class DebugAssets {
        public TextureRegion bboxOutline;

        public DebugAssets(TextureAtlas atlas) {
            bboxOutline = atlas.findRegion(Constants.BBOX_OUTLINE);
        }
    }

    public class PlayerAssets {
        public TextureRegion player;
        public TextureRegion playerShooting;
        public TextureRegion playerStand;
        public TextureRegion playerPlaceholder;
        public TextureRegion rifleProjectile;
        public Animation<TextureRegion> playerWalkingAnimation;

        public PlayerAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            player = atlasPrivate.findRegion(Constants.PLAYER);
            playerShooting = atlasPrivate.findRegion(Constants.PLAYER_SHOOTING);
            playerStand = atlasPrivate.findRegion(Constants.PLAYER_STAND);
            playerPlaceholder = atlas.findRegion(Constants.PLAYER_PLACEHOLDER);
            rifleProjectile = atlasPrivate.findRegion(Constants.RIFLE_PROJECTILE);
            playerWalkingAnimation = new Animation<>(0.10f, atlasPrivate.findRegions(Constants.PLAYER_WALK), Animation.PlayMode.LOOP);
        }
    }

    public class EnemyAssets {
        public TextureRegion enemy;
        public TextureRegion enemyShooting;
        public TextureRegion enemyStand;
        public TextureRegion rifleProjectile;
        public Animation<TextureRegion> enemyWalkingAnimation;
        public Animation<TextureRegion> enemyDyingAnimation;
        public TextureRegion zom1;

        public EnemyAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            enemy = atlasPrivate.findRegion(Constants.ENEMY);
            enemyShooting = atlasPrivate.findRegion(Constants.ENEMY_SHOOTING);
            enemyStand = atlasPrivate.findRegion(Constants.ENEMY_STAND);
            rifleProjectile = atlasPrivate.findRegion(Constants.RIFLE_PROJECTILE);
            enemyWalkingAnimation = new Animation<>(0.10f, atlasPrivate.findRegions(Constants.ENEMY_WALK), Animation.PlayMode.LOOP);
            enemyDyingAnimation = new Animation<>(0.20f, atlasPrivate.findRegions(Constants.ENEMY_DEAD), Animation.PlayMode.NORMAL);
            zom1 = atlasPrivate.findRegion(Constants.ZOM1);
        }
    }

    public class EnvronmentAssets {
        public TextureRegion box1;
        public TextureRegion box2;
        public TextureRegion box1Mini;
        public TextureRegion box2Mini;
        public TextureRegion barrel;
        public TextureRegion barrelOil;

        public EnvronmentAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            box1 = atlasPrivate.findRegion(Constants.BOX_1);
            box2 = atlasPrivate.findRegion(Constants.BOX_2);
            box1Mini = atlasPrivate.findRegion(Constants.BOX_1_MINI);
            box2Mini = atlasPrivate.findRegion(Constants.BOX_2_MINI);
            barrel = atlasPrivate.findRegion(Constants.BARREL);
            barrelOil = atlasPrivate.findRegion(Constants.BARREL_OIL);
        }
    }

    public class TileAssets {
        public TextureRegion dirtToRoad;
        public TextureRegion sandToRoad;
        public TextureRegion grassToRoad;
        public TextureRegion grass;
        public TextureRegion dirt;
        public TextureRegion sand;
        public TextureRegion water;

        public TileAssets(TextureAtlas atlasPrivate) {
            dirtToRoad = atlasPrivate.findRegion(Constants.DIRT_TO_ROAD);
            sandToRoad = atlasPrivate.findRegion(Constants.SAND_TO_ROAD);
            grassToRoad = atlasPrivate.findRegion(Constants.GRASS_TO_ROAD);
            grass = atlasPrivate.findRegion(Constants.GRASS);
            dirt = atlasPrivate.findRegion(Constants.DIRT);
            sand = atlasPrivate.findRegion(Constants.SAND);
            water = atlasPrivate.findRegion(Constants.WATER);
        }
    }
}
