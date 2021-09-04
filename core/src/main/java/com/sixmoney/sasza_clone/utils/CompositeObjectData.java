package com.sixmoney.sasza_clone.utils;


import java.util.HashMap;
import java.util.Map;

public class CompositeObjectData {
    public static CompositeObjectData instance;

    public Map<String, CompositeObjectRecord[]> compositeObjectRecordMap;

    private CompositeObjectData() {
        CompositeObjectRecord[] helicopter = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_helicopter_wings_left", -12, 43, new float[]{45f, 58}),
                new CompositeObjectRecord("vehicles_helicopter_wings_right", 50, 43, new float[]{-17f, 58}),
                new CompositeObjectRecord("vehicles_helicopter_blades2", -88.5f, -38, new float[]{121.5f, 139})
        };

        CompositeObjectRecord[] helicopterBroken = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_helicopter_wings_left_broken", -12, 43),
                new CompositeObjectRecord("vehicles_helicopter_wings_right_broken", 50, 43),
                new CompositeObjectRecord("vehicles_helicopter_blades_broken", -88.5f, -38)
        };

        CompositeObjectRecord[] helicopterDestroyed = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_helicopter_blades_broken", -73.5f, -40)
        };

        CompositeObjectRecord[] panzer = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_panzer_tower", 6.5f, -41, new float[]{27.5f, 88})
        };

        CompositeObjectRecord[] panzerBroken = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_panzer_tower_broken", 9.5f, -36, new float[]{23.5f, 83})
        };

        CompositeObjectRecord[] acsv = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_acsv_tower", 10.5f, -35, new float[]{23f, 78.5f})
        };

        CompositeObjectRecord[] acsvBroken1 = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_acsv_tower_broken1", 10.5f, -35, new float[]{23f, 78.5f})
        };

        CompositeObjectRecord[] acsvBroken2 = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_acsv_tower_broken2", 15.5f, -30, new float[]{22.5f, 76.5f})
        };

        CompositeObjectRecord[] btr = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_btr_tower", 4.5f, -13, new float[]{22f, 60})
        };

        CompositeObjectRecord[] btrBroken = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_btr_tower_broken", 8.5f, -11, new float[]{19f, 59})
        };

        compositeObjectRecordMap = new HashMap<>();
        compositeObjectRecordMap.put("vehicles_helicopter_base", helicopter);
        compositeObjectRecordMap.put("vehicles_helicopter_base_broken1", helicopterDestroyed);
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
        public float[] rotateOrig;

        public CompositeObjectRecord(String textureName, float xOffset, float yOffset) {
            this.textureName = textureName;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

        public CompositeObjectRecord(String textureName, float xOffset, float yOffset, float[] rotateOrig) {
            this.textureName = textureName;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.rotateOrig = rotateOrig;
        }
    }
}
