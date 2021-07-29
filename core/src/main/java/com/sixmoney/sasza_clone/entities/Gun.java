package com.sixmoney.sasza_clone.entities;

public class Gun {
    private int magazineSize;
    private float fireRate;
    private float range;
    private int maxAmmo;
    private int currentAmmo;
    private int currentMagazineAmmo;
    private float projectileSpeed;
    private float damage;

    public Gun() {
        magazineSize = 30;
        fireRate = 12f;
        range = 200;
        maxAmmo = 240;
        currentAmmo = maxAmmo;
        currentMagazineAmmo = magazineSize;
        projectileSpeed = 800;
        damage = 10f;
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
}
