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
    private TextureAtlas atlasPrivateWeapons;

    public DebugAssets debugAssets;
    public PlayerAssets playerAssets;
    public NPCAssets npcAssets;
    public EnemyAssets enemyAssets;
    public EnvronmentAssets envronmentAssets;
    public TileAssets tileAssets;
    public SkinAssets skinAssets;
    public WeaponAssets weaponAssets;

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
        assetManager.load(Constants.TEXTURE_ATLAS_PRIVATE_WEAPONS, TextureAtlas.class);
        assetManager.load(Constants.SKIN_PATH, Skin.class);
        assetManager.load(Constants.SKIN_CONSOLE_PATH, Skin.class);
        assetManager.finishLoading();
        Gdx.app.log(TAG, "Assets Loaded in " + Utils.secondsSince(startLoad) + " seconds");
        Gdx.app.log(TAG, assetManager.getAssetNames().toString());

        atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        atlasPrivate = assetManager.get(Constants.TEXTURE_ATLAS_PRIVATE);
        atlasPrivateWeapons = assetManager.get(Constants.TEXTURE_ATLAS_PRIVATE_WEAPONS);

        skinAssets = new SkinAssets(assetManager);
        debugAssets = new DebugAssets(atlas);
        playerAssets = new PlayerAssets(atlas, atlasPrivate);
        npcAssets = new NPCAssets(atlas, atlasPrivate);
        enemyAssets = new EnemyAssets(atlas, atlasPrivate);
        envronmentAssets = new EnvronmentAssets(atlas, atlasPrivate);
        tileAssets = new TileAssets(atlasPrivate);
        weaponAssets = new WeaponAssets(atlasPrivateWeapons);
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

    public TextureAtlas getPrivateWeaponAtlas() {
        return atlasPrivateWeapons;
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
        public Skin skinConsole;

        public SkinAssets(AssetManager assetManager) {
            skin = assetManager.get(Constants.SKIN_PATH);
            skinConsole = assetManager.get(Constants.SKIN_CONSOLE_PATH);
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

    public class NPCAssets {
        public TextureRegion sniperBase;
        public TextureRegion sniperShooting;
        public TextureRegion sniperStand;
        public TextureRegion rifleProjectile;
        public Animation<TextureRegion> sniperWalkingAnimation;
        public Animation<TextureRegion> sniperDyingAnimation;

        public NPCAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            sniperBase = atlasPrivate.findRegion(Constants.SNIPER_BASE);
            sniperShooting = atlasPrivate.findRegion(Constants.SNIPER_SHOOTING);
            sniperStand = atlasPrivate.findRegion(Constants.SNIPER_STAND);
            rifleProjectile = atlasPrivate.findRegion(Constants.RIFLE_PROJECTILE);
            sniperWalkingAnimation = new Animation<>(0.10f, atlasPrivate.findRegions(Constants.SNIPER_WALK), Animation.PlayMode.LOOP);
            sniperDyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.SNIPER_DEAD), Animation.PlayMode.NORMAL);
        }
    }

    public class EnemyAssets {
        public TextureRegion enemyStand;
        public Animation<TextureRegion> enemyWalkingAnimation;
        public Animation<TextureRegion> zom1DyingAnimation;
        public Animation<TextureRegion> zom2DyingAnimation;
        public TextureRegion zom1;
        public TextureRegion zom2;

        public EnemyAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            zom1 = atlasPrivate.findRegion(Constants.ZOM1);
            zom2 = atlasPrivate.findRegion(Constants.ZOM2);
            enemyStand = atlasPrivate.findRegion(Constants.SNIPER_STAND);
            enemyWalkingAnimation = new Animation<>(0.10f, atlasPrivate.findRegions(Constants.SNIPER_WALK), Animation.PlayMode.LOOP);
            zom1DyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.ZOM1_DEAD), Animation.PlayMode.NORMAL);
            zom2DyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.ZOM2_DEAD), Animation.PlayMode.NORMAL);
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

    public class WeaponAssets {
        public TextureRegion mp5Base;
        public TextureRegion svdBase;

        public Animation<TextureRegion> rifleMuzzleFlashAnimation;
        public Animation<TextureRegion> pistolMuzzleFlashAnimation;
        public Animation<TextureRegion> drmMuzzleFlashAnimation;

        public WeaponAssets(TextureAtlas atlasPrivateWeapons) {
            mp5Base = atlasPrivate.findRegion(Constants.MP5_BASE);
            svdBase = atlasPrivate.findRegion(Constants.SVD_BASE);

            rifleMuzzleFlashAnimation = new Animation<>(0.04f, atlasPrivate.findRegions(Constants.RIFLE_MUZZLE_FLASH), Animation.PlayMode.NORMAL);
            pistolMuzzleFlashAnimation = new Animation<>(0.035f, atlasPrivate.findRegions(Constants.PISTOL_MUZZLE_FLASH), Animation.PlayMode.NORMAL);
            drmMuzzleFlashAnimation = new Animation<>(0.05f, atlasPrivate.findRegions(Constants.DRM_MUZZLE_FLASH), Animation.PlayMode.NORMAL);
        }
    }
}
