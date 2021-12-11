package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.GunData;
import com.sixmoney.sasza_clone.utils.Utils;
import com.sixmoney.sasza_clone.utils.Utils.WeaponCategory;

public class Gun {
    private static final String TAG = Gun.class.getName();
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
    private TextureRegion weaponSprite;
    private Animation muzzleFlashAnimation;
    private Vector2 muzzleFlashOffset;
    private Vector2 muzzleFlashOffsetReal;
    private WeaponCategory weaponType;


    public Gun(GunData.GunRecord gunData) {
        name = gunData.name;
        magazineSize = gunData.magSize;
        fireRate = gunData.fireRate;
        range = gunData.range;
        maxAmmo = gunData.ammo;
        currentAmmo = maxAmmo;
        currentMagazineAmmo = magazineSize;
        projectileSpeed = gunData.projectileSpeed;
        damage = gunData.damage;
        reloadTime = gunData.reloadTime;
        weaponSprite = Assets.get_instance().getPrivateWeaponAtlas().findRegion(name + "_base");
        muzzleFlashOffset = new Vector2(5, 16);
        muzzleFlashOffsetReal = new Vector2(muzzleFlashOffset);
        weaponType = gunData.category;
        reloadTimer = 0;

        if (gunData.category == WeaponCategory.RIFLE || gunData.category == WeaponCategory.LMG) {
            muzzleFlashAnimation = Assets.get_instance().weaponAssets.rifleMuzzleFlashAnimation;
        } else if (gunData.category == WeaponCategory.DMR) {
            muzzleFlashAnimation = Assets.get_instance().weaponAssets.dmrMuzzleFlashAnimation;
        } else {
            muzzleFlashAnimation = Assets.get_instance().weaponAssets.pistolMuzzleFlashAnimation;
        }
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

    public void initReload() {
        if (currentMagazineAmmo < magazineSize && reloadTimer == 0) {
            reloadTimer = TimeUtils.nanoTime();
        }
    }

    public int getReloadTime() {
        return reloadTime;
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
}
