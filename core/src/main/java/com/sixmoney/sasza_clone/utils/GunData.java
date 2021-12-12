package com.sixmoney.sasza_clone.utils;

import com.sixmoney.sasza_clone.utils.Utils.WeaponCategory;

public class GunData {

    public static GunRecord m4 = new GunRecord("m4", WeaponCategory.RIFLE, 15, 12, 1000, 250, 240, 30, 1500, 5);
    public static GunRecord mp5 = new GunRecord("mp5", WeaponCategory.SMG, 10, 20, 800, 150, 240, 30, 1000, 8);
    public static GunRecord svd = new GunRecord("svd", WeaponCategory.DMR, 100, 2, 2000, 500, 80, 10, 2500, 2);
    public static GunRecord pkm = new GunRecord("pkm", WeaponCategory.LMG, 12, 16, 1000, 300, 800, 100, 4000, 8);
    public static GunRecord vaporizer = new GunRecord("pkm", WeaponCategory.LMG, 5, 200, 100, 300, 8000, 2000, 4000, 50);

    public static class GunRecord {
        public String name;
        public WeaponCategory category;
        public float damage;
        public float fireRate;
        public float projectileSpeed;
        public float range;
        public int ammo;
        public int magSize;
        public int reloadTime;
        public float bloom;

        public GunRecord(String name, WeaponCategory category, float damage, float fireRate,
                         float projectileSpeed, float range, int ammo, int magSize, int reloadTime, float bloom) {
            this.name = name;
            this.category = category;
            this.damage = damage;
            this.fireRate = fireRate;
            this.projectileSpeed = projectileSpeed;
            this.range = range;
            this.ammo = ammo;
            this.magSize = magSize;
            this.reloadTime = reloadTime;
            this.bloom = bloom;
        }
    }
}
