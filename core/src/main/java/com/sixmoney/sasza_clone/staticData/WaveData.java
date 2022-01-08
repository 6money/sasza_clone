package com.sixmoney.sasza_clone.staticData;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class WaveData {

    public static Array<WaveRecord> l1w1 = new Array<>(new WaveRecord[]{
            new WaveRecord("BaseEnemy", 100, Constants.WAVE_BREAK, 0.1f)
    });
    public static Array<WaveRecord> l1w2 = new Array<>(new WaveRecord[]{
            new WaveRecord("BaseEnemy", 60, Constants.WAVE_BREAK, 0.1f),
            new WaveRecord("EnemyTierTwo", 20, Constants.WAVE_BREAK, 0.2f)
    });
    public static Array<WaveRecord> l1w3 = new Array<>(new WaveRecord[]{
            new WaveRecord("BaseEnemy", 60, Constants.WAVE_BREAK, 0.1f),
            new WaveRecord("EnemyTierTwo", 20, Constants.WAVE_BREAK, 0.2f),
            new WaveRecord("EnemyTierThree", 5, Constants.WAVE_BREAK, 1)
    });

    public static Array<WaveRecord> dw1 = new Array<>(new WaveRecord[]{
            new WaveRecord("BaseEnemy", 5, 10, 1f)
    });

    public static final Map<String, Array<WaveRecord>[]> waveRecords = createWaveRecordMap();
    public static Map<String, Array<WaveRecord>[]> createWaveRecordMap() {
        Map<String, Array<WaveRecord>[]> map = new HashMap<>();
        map.put("level1", new Array[]{l1w1, l1w2, l1w3});
        map.put("debug", new Array[]{dw1, dw1, dw1, dw1, dw1, dw1, dw1, dw1, dw1, dw1, dw1, dw1, dw1});

        return map;
    }


    public static class WaveRecord {
        public String enemyType;
        public int waveAmount;
        public float waveDelay;
        public float spawnDelay;

        public WaveRecord(String enemyType, int waveAmount, float waveDelay, float spawnDelay) {
            this.enemyType = enemyType;
            this.waveAmount = waveAmount;
            this.waveDelay = waveDelay;
            this.spawnDelay = spawnDelay;
        }
    }
}
