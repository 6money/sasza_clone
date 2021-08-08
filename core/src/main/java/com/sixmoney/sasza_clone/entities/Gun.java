package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.GunData;

public class Gun {
    private String name;
    private int magazineSize;
    private float fireRate;
    private float range;
    private int maxAmmo;
    private int currentAmmo;
    private int currentMagazineAmmo;
    private float projectileSpeed;
    private float damage;
    private TextureRegion weaponSprite;

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
        weaponSprite = Assets.get_instance().getPrivateWeaponAtlas().findRegion(name + "_base");
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

    public void reload() {
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

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getDamage() {
        return damage;
    }

    public TextureRegion getWeaponSprite() {
        return weaponSprite;
    }
}
