package com.sixmoney.sasza_clone.utils;

import com.sixmoney.sasza_clone.utils.Utils.WeaponCategory;

import java.util.HashMap;
import java.util.Map;

public class GunData {

    public static GunRecord m4 = new GunRecord("m4", WeaponCategory.RIFLE, 15, 12, 1000, 250, 240, 30, 1500, 5, 25, 2, 0.05f, 1.25f);
    public static GunRecord mp5 = new GunRecord("mp5", WeaponCategory.SMG, 10, 20, 800, 150, 240, 30, 1000, 8, 20, 2, 0.05f, 1.25f);
    public static GunRecord svd = new GunRecord("svd", WeaponCategory.DMR, 100, 2, 2000, 500, 80, 10, 2500, 2, 100, 5, 0.05f, 1.25f);
    public static GunRecord pkm = new GunRecord("pkm", WeaponCategory.LMG, 12, 16, 1000, 300, 800, 100, 4000, 8, 34, 3, 0.05f, 1.25f);
    public static GunRecord vaporizer = new GunRecord("vaporizer", "pkm", WeaponCategory.SPECIAL, 5, 200, 100, 300, 8000, 2000, 4000, 50, 10, 2, 0.05f, 1.25f);

    public static final Map<String, GunRecord> gunRecords = createGunRecordMap();
    public static Map<String, GunRecord> createGunRecordMap() {
        Map<String, GunRecord> map = new HashMap<>();
        map.put(m4.name, m4);
        map.put(mp5.name, mp5);
        map.put(svd.name, svd);
        map.put(pkm.name, pkm);
        map.put(vaporizer.name, vaporizer);

        return map;
    }


    public static class GunRecord {
        public String name;
        public String textureName;
        public WeaponCategory category;
        public float damage;
        public float fireRate;
        public float projectileSpeed;
        public float range;
        public int ammo;
        public int magSize;
        public int reloadTime;
        public float bloom;
        public float impact;
        public int penetration;
        public float critChance;
        public float critDamage;

        public GunRecord(String name, WeaponCategory category, float damage, float fireRate,
                         float projectileSpeed, float range, int ammo, int magSize, int reloadTime,
                         float bloom, float impact, int penetration, float critChance, float critDamage) {
            this(name, name, category, damage, fireRate, projectileSpeed, range, ammo,
                    magSize, reloadTime, bloom, impact, penetration, critChance, critDamage);
        }

        public GunRecord(String name, String textureName, WeaponCategory category, float damage, float fireRate,
                         float projectileSpeed, float range, int ammo, int magSize, int reloadTime,
                         float bloom, float impact, int penetration, float critChance, float critDamage) {
            this.name = name;
            this.textureName = textureName;
            this.category = category;
            this.damage = damage;
            this.fireRate = fireRate;
            this.projectileSpeed = projectileSpeed;
            this.range = range;
            this.ammo = ammo;
            this.magSize = magSize;
            this.reloadTime = reloadTime;
            this.bloom = bloom;
            this.impact = impact;
            this.penetration = penetration;
            this.critChance = critChance;
            this.critDamage = critDamage;
        }
    }
}
