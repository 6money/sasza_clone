package com.sixmoney.sasza_clone.utils;

import com.sixmoney.sasza_clone.utils.Utils.WeaponCategory;

public class GunData {

    public static GunRecord m4 = new GunRecord("m4", WeaponCategory.RIFLE, 15, 12, 1000, 250, 240, 30);
    public static GunRecord mp5 = new GunRecord("mp5", WeaponCategory.SMG, 10, 20, 800, 150, 240, 30);
    public static GunRecord svd = new GunRecord("svd", WeaponCategory.DMR, 100, 2, 2000, 500, 80, 10);
    public static GunRecord pkm = new GunRecord("pkm", WeaponCategory.LMG, 12, 16, 1000, 300, 800, 100);

    public static class GunRecord {
        public String name;
        public WeaponCategory category;
        public float damage;
        public float fireRate;
        public float projectileSpeed;
        public float range;
        public int ammo;
        public int magSize;

        public GunRecord(String name, WeaponCategory category, float damage, float fireRate,
                         float projectileSpeed, float range, int ammo, int magSize) {
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
