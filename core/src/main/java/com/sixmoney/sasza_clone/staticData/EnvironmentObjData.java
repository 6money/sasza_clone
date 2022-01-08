package com.sixmoney.sasza_clone.staticData;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentObjData {
    public static EnvironmentObjData instance;

    private final String default_record;
    private final float default_buffer;
    private final float[] default_buffer_array;
    private Map<String, EnvironmentObjRecord> buffers;

    private EnvironmentObjRecord result;

    private EnvironmentObjData() {
        default_record = "default";
        default_buffer = Constants.BBOX_BUFFER_ENVIRONMENT;
        default_buffer_array = new float[]{default_buffer, default_buffer};
        buffers = new HashMap<>();

        buffers.put(default_record, new EnvironmentObjRecord(default_buffer_array, true, true, false, false));

        // base vehicles
        buffers.put("vehicles_helicopter_base", new EnvironmentObjRecord(new float[]{18, 10}, true, true, true, false));
        buffers.put("vehicles_helicopter_base_broken2", new EnvironmentObjRecord(new float[]{18, 10}, true, true, true, false));
        buffers.put("vehicles_bomber_mini", new EnvironmentObjRecord(new float[]{73, 12}, true, true, true, false));
        buffers.put("vehicles_bomber", new EnvironmentObjRecord(new float[]{108, 18}, true, true, true, false));
        buffers.put("vehicles_bomber_broken1", new EnvironmentObjRecord(new float[]{108, 18}, true, true, true, false));
        buffers.put("vehicles_bomber_broken2", new EnvironmentObjRecord(new float[]{108, 18}, true, true, true, false));
        buffers.put("vehicles_humvee_broken3", new EnvironmentObjRecord(new float[]{12, default_buffer}, true, true, false, false));
        buffers.put("vehicles_humvee_broken4", new EnvironmentObjRecord(new float[]{12, default_buffer}, true, true, false, false));
        buffers.put("vehicles_humvee_broken5", new EnvironmentObjRecord(new float[]{8, default_buffer}, true, true, false, false));
        buffers.put("vehicles_humvee_broken6sd", new EnvironmentObjRecord(new float[]{8, default_buffer}, true, true, false, false));

        // vehicle composites
        buffers.put("vehicles_helicopter_blades2", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));
        buffers.put("vehicles_helicopter_blades_broken", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));
        buffers.put("vehicles_panzer_tower", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));
        buffers.put("vehicles_panzer_tower_broken", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));
        buffers.put("vehicles_acsv_tower", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));
        buffers.put("vehicles_acsv_tower_broken1", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));
        buffers.put("vehicles_acsv_tower_broken2", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));
        buffers.put("vehicles_btr_tower", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));
        buffers.put("vehicles_btr_tower_broken", new EnvironmentObjRecord(default_buffer_array, false, false, true, false));

        // environment groupings
        buffers.put("bush", new EnvironmentObjRecord(default_buffer_array, true, false, false, false));
        buffers.put("box", new EnvironmentObjRecord(default_buffer_array, true, true, false, true));
        buffers.put("barrel", new EnvironmentObjRecord(default_buffer_array, true, true, false, true));
    }

    public static EnvironmentObjData get_instance() {
        if (instance == null) {
            instance = new EnvironmentObjData();
        }

        return instance;
    }

    public EnvironmentObjRecord getObjData(String name) {
        result = buffers.get(name);
        if (result == null) {
            result = buffers.get(default_record);
        }

        return result;
    }

    public static class EnvironmentObjRecord {
        public float[] bboxBuffer;
        public boolean characterCollidable;
        public boolean bulletCollidable;
        public boolean upper;
        public boolean destructible;

        public EnvironmentObjRecord(float[] bboxBuffer, boolean characterCollidable,
                                    boolean bulletCollidable, boolean upper, boolean destructible) {
            this.bboxBuffer = bboxBuffer;
            this.characterCollidable = characterCollidable;
            this.bulletCollidable = bulletCollidable;
            this.upper = upper;
            this.destructible = destructible;
        }
    }
}
