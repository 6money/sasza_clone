package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.sasza_clone.staticData.GunData;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;
import com.sixmoney.sasza_clone.utils.Utils.WeaponCategory;

public class Gun {
    private static String TAG = Gun.class.getName();
    private String name;
    private int magazineSize;
    private float fireRate;
    private float range;
    private int maxAmmo;
    private int currentAmmo;
    private int currentMagazineAmmo;
    private float projectileSpeed;
    private float damage;
    private int reloadTime;
    private long reloadTimer;
    private float bloom;
    private float impact;
    private int penetration;
    private float critChance;
    private float critDamage;
    private float movementPenalty;
    private Vector2 screenShake;
    private transient TextureRegion weaponSprite;
    private transient Animation muzzleFlashAnimation;
    private transient Vector2 muzzleFlashOffset;
    private transient Vector2 muzzleFlashOffsetReal;
    private WeaponCategory weaponType;

    public Gun() {
        muzzleFlashOffset = new Vector2(0, 0);
    }


    public Gun(GunData.GunRecord gunData) {
        name = gunData.textureName;
        magazineSize = gunData.magSize;
        fireRate = gunData.fireRate;
        range = gunData.range;
        maxAmmo = gunData.ammo;
        currentAmmo = maxAmmo;
        currentMagazineAmmo = magazineSize;
        projectileSpeed = gunData.projectileSpeed;
        damage = gunData.damage;
        reloadTime = gunData.reloadTime;
        bloom = gunData.bloom;
        impact = gunData.impact;
        penetration = gunData.penetration;
        critChance = gunData.critChance;
        critDamage = gunData.critDamage;
        movementPenalty = gunData.movementPenalty;
        screenShake = gunData.screenShake;
        weaponType = gunData.category;
        initGun();
    }

    // Needs to be called after gun instance created from serialized data
    public void initGun() {
        weaponSprite = Assets.get_instance().getPrivateWeaponAtlas().findRegion(name + "_base");
        GunData.GunRecord gunData = GunData.gunRecords.get(name);

        if (gunData.category == WeaponCategory.RIFLE) {
            muzzleFlashAnimation = Assets.get_instance().weaponAssets.rifleMuzzleFlashAnimation;
        } else if (gunData.category == WeaponCategory.LMG || gunData.category == WeaponCategory.SPECIAL) {
            muzzleFlashAnimation = Assets.get_instance().weaponAssets.lmgMuzzleFlashAnimation;
        } else if (gunData.category == WeaponCategory.DMR) {
            muzzleFlashAnimation = Assets.get_instance().weaponAssets.dmrMuzzleFlashAnimation;
        } else if (gunData.category == WeaponCategory.SMG) {
            muzzleFlashAnimation = Assets.get_instance().weaponAssets.smgMuzzleFlashAnimation;
        } else {
            muzzleFlashAnimation = Assets.get_instance().weaponAssets.pistolMuzzleFlashAnimation;
        }

        muzzleFlashOffset = new Vector2(-2, 12);
        muzzleFlashOffsetReal = new Vector2(muzzleFlashOffset);
        reloadTimer = 0;
    }

    public int getMagazineSize() {
        return magazineSize;
    }

    public float getFireRate() {
        return fireRate;
    }

    public float getRange() {
        return range;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public void setCurrentAmmo(int ammoQuantity) {
        this.currentAmmo = ammoQuantity;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public void setMagazineSize(int magazineSize) {
        this.magazineSize = magazineSize;
    }

    public void setProjectileSpeed(float projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    public void incrementCurrentAmmo(int ammoQuantity) {
        this.currentAmmo += ammoQuantity;
    }

    public int getCurrentMagazineAmmo() {
        return currentMagazineAmmo;
    }

    public void decrementCurrentMagazineAmmo() {
        this.currentMagazineAmmo -= 1;
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getDamage() {
        return damage;
    }

    public TextureRegion getWeaponSprite() {
        return weaponSprite;
    }

    public Animation getMuzzleFlashAnimation() {
        return muzzleFlashAnimation;
    }

    public Vector2 getMuzzleFlashOffset() {
        return muzzleFlashOffset;
    }

    public Vector2 getMuzzleFlashOffsetReal() {
        return muzzleFlashOffsetReal;
    }

    public WeaponCategory getWeaponType() {
        return weaponType;
    }

    public float getBloom() {
        return bloom;
    }

    public void setBloom(float bloom) {
        this.bloom = bloom;
    }

    public float getImpact() {
        return impact;
    }

    public void setImpact(float impact) {
        this.impact = impact;
    }

    public int getPenetration() {
        return penetration;
    }

    public void setPenetration(int penetration) {
        this.penetration = penetration;
    }

    public float getCritChance() {
        return critChance;
    }

    public void setCritChance(float critChance) {
        this.critChance = critChance;
    }

    public float getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(float critDamage) {
        this.critDamage = critDamage;
    }

    public float getMovementPenalty() {
        return movementPenalty;
    }

    public void setMovementPenalty(float movementPenalty) {
        this.movementPenalty = movementPenalty;
    }

    public Vector2 getScreenShake() {
        return screenShake;
    }

    public void setScreenShake(Vector2 screenShake) {
        this.screenShake = screenShake;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public void initReload() {
        if (currentMagazineAmmo < magazineSize && reloadTimer == 0) {
            reloadTimer = TimeUtils.nanoTime();
        }
    }

    public void resetReloadTimer() {
        this.reloadTimer = 0;
    }

    private void reload() {
        int remainingAmmo;
        if (currentAmmo >= magazineSize) {
            remainingAmmo = magazineSize;
            currentAmmo -= magazineSize;
        } else {
            remainingAmmo = currentAmmo;
            currentAmmo -= currentAmmo;
        }
        currentMagazineAmmo = remainingAmmo;
    }

    public float checkReloadStatus() {
        if (reloadTimer == 0) {
            return 0f;
        } else if (Utils.millisecondsSince(reloadTimer) >= reloadTime) {
            Gdx.app.log(TAG, "reloading");
            reloadTimer = 0;
            reload();
        }

        return Utils.millisecondsSince(reloadTimer);
    }

    public String serializeGun() {
        Json json = new Json();

        return json.toJson(this);
    }
}
