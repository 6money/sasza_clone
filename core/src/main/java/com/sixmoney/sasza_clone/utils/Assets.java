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
import com.sixmoney.sasza_clone.staticData.Constants;

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
    public LightAssets lightAssets;

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
        weaponAssets = new WeaponAssets(atlasPrivate, atlasPrivateWeapons);
        lightAssets = new LightAssets(atlasPrivate);
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
        public Animation<TextureRegion> playerWalkingAnimation;

        public PlayerAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            player = atlasPrivate.findRegion(Constants.PLAYER);
            playerShooting = atlasPrivate.findRegion(Constants.PLAYER_SHOOTING);
            playerStand = atlasPrivate.findRegion(Constants.PLAYER_STAND);
            playerPlaceholder = atlas.findRegion(Constants.PLAYER_PLACEHOLDER);
            playerWalkingAnimation = new Animation<>(0.10f, atlasPrivate.findRegions(Constants.PLAYER_WALK), Animation.PlayMode.LOOP);
        }
    }

    public class NPCAssets {
        public TextureRegion sniperBase;
        public TextureRegion sniperShooting;
        public Animation<TextureRegion> sniperDyingAnimation;

        public TextureRegion riflemanBase;
        public TextureRegion riflemanShooting;
        public Animation<TextureRegion> riflemanDyingAnimation;

        public TextureRegion gunnerBase;
        public TextureRegion gunnerShooting;
        public Animation<TextureRegion> gunnerDyingAnimation;

        public TextureRegion npcStandS1;
        public Animation<TextureRegion> npcWalkingAnimationS1;

        public TextureRegion npcStandS2;
        public Animation<TextureRegion> npcWalkingAnimationS2;


        public NPCAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            sniperBase = atlasPrivate.findRegion(Constants.SNIPER_BASE);
            sniperShooting = atlasPrivate.findRegion(Constants.SNIPER_SHOOTING);
            sniperDyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.SNIPER_DEAD), Animation.PlayMode.NORMAL);

            riflemanBase = atlasPrivate.findRegion(Constants.RIFLEMAN_BASE);
            riflemanShooting = atlasPrivate.findRegion(Constants.RIFLEMAN_SHOOTING);
            riflemanDyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.RIFLEMAN_DEAD), Animation.PlayMode.NORMAL);

            gunnerBase = atlasPrivate.findRegion(Constants.GUNNER_BASE);
            gunnerShooting = atlasPrivate.findRegion(Constants.GUNNER_SHOOTING);
            gunnerDyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.GUNNER_DEAD), Animation.PlayMode.NORMAL);

            npcStandS1 = atlasPrivate.findRegion(Constants.NPC_STAND_S1);
            npcWalkingAnimationS1 = new Animation<>(0.10f, atlasPrivate.findRegions(Constants.NPC_WALK_S1), Animation.PlayMode.LOOP);

            npcStandS2 = atlasPrivate.findRegion(Constants.NPC_STAND_S2);
            npcWalkingAnimationS2 = new Animation<>(0.10f, atlasPrivate.findRegions(Constants.NPC_WALK_S2), Animation.PlayMode.LOOP);
        }
    }

    public class EnemyAssets {
        public TextureRegion enemyStand;
        public Animation<TextureRegion> enemyWalkingAnimation;
        public Animation<TextureRegion> zom1DyingAnimation;
        public Animation<TextureRegion> zom2DyingAnimation;
        public Animation<TextureRegion> zom3DyingAnimation;
        public TextureRegion zom1;
        public TextureRegion zom2;
        public TextureRegion zom3;

        public EnemyAssets(TextureAtlas atlas, TextureAtlas atlasPrivate) {
            zom1 = atlasPrivate.findRegion(Constants.ZOM1);
            zom2 = atlasPrivate.findRegion(Constants.ZOM2);
            zom3 = atlasPrivate.findRegion(Constants.ZOM3);
            enemyStand = atlasPrivate.findRegion(Constants.NPC_STAND_S1);
            enemyWalkingAnimation = new Animation<>(0.10f, atlasPrivate.findRegions(Constants.NPC_WALK_S1), Animation.PlayMode.LOOP);
            zom1DyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.ZOM1_DEAD), Animation.PlayMode.NORMAL);
            zom2DyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.ZOM2_DEAD), Animation.PlayMode.NORMAL);
            zom3DyingAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.ZOM3_DEAD), Animation.PlayMode.NORMAL);
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
        public TextureRegion grass;
        public TextureRegion dirt;
        public TextureRegion sand;
        public TextureRegion water;

        public TileAssets(TextureAtlas atlasPrivate) {
            grass = atlasPrivate.findRegion(Constants.GRASS);
            dirt = atlasPrivate.findRegion(Constants.DIRT);
            sand = atlasPrivate.findRegion(Constants.SAND);
            water = atlasPrivate.findRegion(Constants.WATER);
        }
    }

    public class WeaponAssets {
        public TextureRegion mp5Base;
        public TextureRegion svdBase;

        public TextureRegion pistolProjectile;
        public TextureRegion rifleProjectile;
        public TextureRegion rifleProjectile2;
        public TextureRegion dmrProjectile;
        public TextureRegion lmgProjectile;

        public Animation<TextureRegion> rifleMuzzleFlashAnimation;
        public Animation<TextureRegion> pistolMuzzleFlashAnimation;
        public Animation<TextureRegion> smgMuzzleFlashAnimation;
        public Animation<TextureRegion> dmrMuzzleFlashAnimation;
        public Animation<TextureRegion> lmgMuzzleFlashAnimation;

        public WeaponAssets(TextureAtlas atlasPrivate, TextureAtlas atlasPrivateWeapons) {
            mp5Base = atlasPrivate.findRegion(Constants.MP5_BASE);
            svdBase = atlasPrivate.findRegion(Constants.SVD_BASE);

            pistolProjectile = atlasPrivate.findRegion(Constants.PISTOL_PROJECTILE);
            rifleProjectile = atlasPrivate.findRegion(Constants.RIFLE_PROJECTILE);
            dmrProjectile = atlasPrivate.findRegion(Constants.DMR_PROJECTILE);
            lmgProjectile = atlasPrivate.findRegion(Constants.LMG_PROJECTILE);

            rifleMuzzleFlashAnimation = new Animation<>(0.04f, atlasPrivate.findRegions(Constants.RIFLE_MUZZLE_FLASH), Animation.PlayMode.NORMAL);
            pistolMuzzleFlashAnimation = new Animation<>(0.035f, atlasPrivate.findRegions(Constants.PISTOL_MUZZLE_FLASH), Animation.PlayMode.NORMAL);
            smgMuzzleFlashAnimation = new Animation<>(0.035f, atlasPrivate.findRegions(Constants.SMG_MUZZLE_FLASH), Animation.PlayMode.NORMAL);
            dmrMuzzleFlashAnimation = new Animation<>(0.06f, atlasPrivate.findRegions(Constants.DMR_MUZZLE_FLASH), Animation.PlayMode.NORMAL);
            lmgMuzzleFlashAnimation = new Animation<>(0.04f, atlasPrivate.findRegions(Constants.LMG_MUZZLE_FLASH), Animation.PlayMode.NORMAL);
        }
    }

    public class LightAssets {
        public TextureRegion light1;
        public TextureRegion light2;
        public TextureRegion light3;
        public TextureRegion light4;
        public TextureRegion light5;

        public LightAssets(TextureAtlas atlasPrivate) {
            light1 = atlasPrivate.findRegion("light1");
            light2 = atlasPrivate.findRegion("light2");
            light3 = atlasPrivate.findRegion("light3");
            light4 = atlasPrivate.findRegion("light4");
            light5 = atlasPrivate.findRegion("light5");
        }
    }
}
