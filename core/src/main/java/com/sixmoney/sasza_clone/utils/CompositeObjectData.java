package com.sixmoney.sasza_clone.utils;


import java.util.HashMap;
import java.util.Map;

public class CompositeObjectData {
    public static CompositeObjectData instance;

    public Map<String, CompositeObjectRecord[]> compositeObjectRecordMap;

    private CompositeObjectData() {
        CompositeObjectRecord[] helicopter = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_helicopter_wings_left", -12, 43, true, true, false),
                new CompositeObjectRecord("vehicles_helicopter_wings_right", 50, 43, true, true, false),
                new CompositeObjectRecord("vehicles_helicopter_blades2", -88.5f, -38, false, false, true)
        };

        CompositeObjectRecord[] helicopterBroken = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_helicopter_blades_broken", -75.5f, -38, false, false, true)
        };

        CompositeObjectRecord[] panzer = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_panzer_tower", 6.5f, -41, false, false, true)
        };

        CompositeObjectRecord[] panzerBroken = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_panzer_tower_broken", 9.5f, -36, false, false, true)
        };

        CompositeObjectRecord[] acsv = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_acsv_tower", 10.5f, -35, false, false, true)
        };

        CompositeObjectRecord[] acsvBroken1 = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_acsv_tower_broken1", 10.5f, -35, false, false, true)
        };

        CompositeObjectRecord[] acsvBroken2 = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_acsv_tower_broken2", 15.5f, -30, false, false, true)
        };

        CompositeObjectRecord[] btr = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_btr_tower", 4.5f, -13, false, false, true)
        };

        CompositeObjectRecord[] btrBroken = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_btr_tower_broken", 8.5f, -11, false, false, true)
        };

        compositeObjectRecordMap = new HashMap<>();
        compositeObjectRecordMap.put("vehicles_helicopter_base", helicopter);
        compositeObjectRecordMap.put("vehicles_helicopter_base_broken1", helicopterBroken);
        compositeObjectRecordMap.put("vehicles_helicopter_base_broken2", helicopterBroken);
        compositeObjectRecordMap.put("vehicles_panzer_base", panzer);
        compositeObjectRecordMap.put("vehicles_panzer_base_broken1", panzerBroken);
        compositeObjectRecordMap.put("vehicles_panzer_base_broken2", panzerBroken);
        compositeObjectRecordMap.put("vehicles_acsv_base", acsv);
        compositeObjectRecordMap.put("vehicles_acsv_base_broken1", acsvBroken1);
        compositeObjectRecordMap.put("vehicles_acsv_base_broken2", acsvBroken2);
        compositeObjectRecordMap.put("vehicles_btr_base", btr);
        compositeObjectRecordMap.put("vehicles_btr_base_broken", btrBroken);
    }

    public static CompositeObjectData get_instance() {
        if (instance == null) {
            instance = new CompositeObjectData();
        }

        return instance;
    }

    public static class CompositeObjectRecord {
        public String textureName;
        public float xOffset;
        public float yOffset;
        public boolean characterCollidable;
        public boolean bulletCollidable;
        public boolean upper;

        public CompositeObjectRecord(String textureName, float xOffset, float yOffset,
                         boolean characterCollidable, boolean bulletCollidable, boolean upper) {
            this.textureName = textureName;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.characterCollidable = characterCollidable;
            this.bulletCollidable = bulletCollidable;
            this.upper = upper;
        }
    }
}
