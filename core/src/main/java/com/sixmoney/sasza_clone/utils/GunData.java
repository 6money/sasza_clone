package com.sixmoney.sasza_clone.utils;

import com.sixmoney.sasza_clone.utils.Utils.weaponCategory;

public class GunData {

    public static GunRecord m4 = new GunRecord("m4", weaponCategory.RIFLE, 15, 12, 1000, 250, 240, 30);
    public static GunRecord mp5 = new GunRecord("mp5", weaponCategory.RIFLE, 8, 20, 800, 150, 240, 30);
    public static GunRecord svd = new GunRecord("svd", weaponCategory.DRM, 100, 2, 2000, 500, 80, 10);

    public static class GunRecord {
        public String name;
        public weaponCategory category;
        public float damage;
        public float fireRate;
        public float projectileSpeed;
        public float range;
        public int ammo;
        public int magSize;

        public GunRecord(String name, weaponCategory category, float damage, float fireRate, float projectileSpeed,
                         float range, int ammo, int magSize) {
            this.name = name;
            this.category = category;
            this.damage = damage;
            this.fireRate = fireRate;
            this.projectileSpeed = projectileSpeed;
            this.range = range;
            this.ammo = ammo;
            this.magSize = magSize;
        }
    }
}
