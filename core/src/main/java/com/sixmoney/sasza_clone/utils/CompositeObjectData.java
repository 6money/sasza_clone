package com.sixmoney.sasza_clone.utils;


import java.util.HashMap;
import java.util.Map;

public class CompositeObjectData {

    public static CompositeObjectData instance;
    private CompositeObjectRecord[] helicopter;
    public Map<String, CompositeObjectRecord[]> compositeObjectRecordMap;

    private CompositeObjectData() {
        helicopter = new CompositeObjectRecord[]{
                new CompositeObjectRecord("vehicles_helicopter_wings_left", -12, 43, true, true),
                new CompositeObjectRecord("vehicles_helicopter_wings_right", 50, 43, true, true),
                new CompositeObjectRecord("vehicles_helicopter_blades2", -88.5f, -39, false, false)
        };

        compositeObjectRecordMap = new HashMap<>();
        compositeObjectRecordMap.put("vehicles_helicopter_base", helicopter);
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

        public CompositeObjectRecord(String textureName, float xOffset, float yOffset,
                         boolean characterCollidable, boolean bulletCollidable) {
            this.textureName = textureName;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.characterCollidable = characterCollidable;
            this.bulletCollidable = bulletCollidable;
        }
    }
}
