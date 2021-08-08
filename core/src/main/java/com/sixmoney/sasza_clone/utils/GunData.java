package com.sixmoney.sasza_clone.utils;

public class GunData {

    public static GunRecord m4 = new GunRecord("m4", 15, 12, 1000, 250, 240, 30);
    public static GunRecord mp5 = new GunRecord("mp5", 8, 20, 800, 150, 240, 30);
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
