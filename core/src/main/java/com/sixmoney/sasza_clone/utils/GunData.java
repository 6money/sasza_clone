package com.sixmoney.sasza_clone.utils;

public class GunData {

    public static GunRecord mp5 = new GunRecord("mp5", 10, 12, 800, 200, 240, 30);
    public static GunRecord svd = new GunRecord("svd", 100, 2, 2000, 500, 10, 10);

    public static class GunRecord {
        public String name;
        public float damage;
        public float fireRate;
        public float projectileSpeed;
        public float range;
        public int ammo;
        public int magSize;

        public GunRecord(String name, float damage, float fireRate, float projectileSpeed,
                         float range, int ammo, int magSize) {
            this.name = name;
            this.damage = damage;
            this.fireRate = fireRate;
            this.projectileSpeed = projectileSpeed;
            this.range = range;
            this.ammo = ammo;
            this.magSize = magSize;
        }
    }
}
