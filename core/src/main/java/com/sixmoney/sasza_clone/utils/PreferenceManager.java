package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.sixmoney.sasza_clone.staticData.Constants;

public class PreferenceManager {
    private final static String TAG = PreferenceManager.class.getName();

    private static Preferences preferences;
    private static PreferenceManager preferenceManager;

    private PreferenceManager() {
        preferences = Gdx.app.getPreferences(Constants.PREFERENCES_NAME);
    }

    public static PreferenceManager get_instance() {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager();
        }

        return preferenceManager;
    }

    public boolean getMusic() {
        return preferences.getBoolean("musicEnabled", true);
    }

    public void setMusic(boolean value) {
        preferences.putBoolean("musicEnabled", value);
        preferences.flush();
    }

    public float getMusicVolume() {
        return preferences.getFloat("musicVolume", 60f);
    }

    public void setMusicVolume(float value) {
        preferences.putFloat("musicVolume", value);
        preferences.flush();
    }

    public boolean getSound() {
        return preferences.getBoolean("soundEnabled", true);
    }

    public void setSound(boolean value) {
        preferences.putBoolean("soundEnabled", value);
        preferences.flush();
    }

    public float getSoundVolume() {
        return preferences.getFloat("soundVolume", 100f);
    }

    public void setSoundVolume(float value) {
        preferences.putFloat("soundVolume", value);
        preferences.flush();
    }

    public float getDifficulty() {
        return preferences.getFloat("difficulty", 0);
    }

    public void setDifficulty(float value) {
        preferences.putFloat("difficulty", value);
        preferences.flush();
    }

    public boolean getShowFPS() {
        return preferences.getBoolean("showFPS", false);
    }

    public void setShowFPS(boolean value) {
        preferences.putBoolean("showFPS", value);
        preferences.flush();
    }

    public boolean getShowCoords() {
        return preferences.getBoolean("showCoords", false);
    }

    public void setShowCoords(boolean value) {
        preferences.putBoolean("showCoords", value);
        preferences.flush();
    }

    public boolean getMobile() {
        return preferences.getBoolean("showMobile", false);
    }

    public void setMobile(boolean value) {
        preferences.putBoolean("showMobile", value);
        preferences.flush();
    }

    public float getStatusBarTransparency() {
        return preferences.getFloat("statusBarTransparency", 50);
    }

    public void setStatusBarTransparency(float value) {
        preferences.putFloat("statusBarTransparency", value);
        preferences.flush();
    }

    public float getPStatusBarTransparency() {
        return preferences.getFloat("pStatusBarTransparency", 50);
    }

    public void setPStatusBarTransparency(float value) {
        preferences.putFloat("pStatusBarTransparency", value);
        preferences.flush();
    }

    public float getHitMarkerTransparency() {
        return preferences.getFloat("hitMarkerTransparency", 60);
    }

    public void setHitMarkerTransparency(float value) {
        preferences.putFloat("hitMarkerTransparency", value);
        preferences.flush();
    }

    public boolean getScreenShake() {
        return preferences.getBoolean("screenShake", true);
    }

    public void setScreenShake(boolean value) {
        preferences.putBoolean("screenShake", value);
        preferences.flush();
    }

    public boolean getVSync() {
        return preferences.getBoolean("vSync", true);
    }

    public void setVSync(boolean value) {
        preferences.putBoolean("vSync", value);
        preferences.flush();
    }

    public int getFps() {
        return preferences.getInteger("fps", 60);
    }

    public void setFps(int value) {
        preferences.putInteger("fps", value);
        preferences.flush();
    }


    // Profile data

    public String getProfileName() {
        return preferences.getString("profileName", "default");
    }

    public void setProfileName(String value) {
        preferences.putString("profileName", value);
        preferences.flush();
    }

    public int getProfileLevel() {
        return preferences.getInteger("profileLevel", 1);
    }

    public void setProfileLevel(int value) {
        preferences.putInteger("profileLevel", value);
        preferences.flush();
    }

    public long getProfileItemIdSeq() {
        return preferences.getLong("profileItemIdSeq", -1);
    }

    public void setProfileItemIdSeq(long value) {
        preferences.putLong("profileItemIdSeq", value);
        preferences.flush();
    }

    public String getProfileLoadout() {
        return preferences.getString("profileLoadout", "new");
    }

    public void setProfileLoadout(String value) {
        preferences.putString("profileLoadout", value);
        preferences.flush();
    }

    public String getProfileWeapons() {
        return preferences.getString("profileWeapons", "new");
    }

    public void setProfileWeapons(String value) {
        preferences.putString("profileWeapons", value);
        preferences.flush();
    }


    public void removePreferencesData() {
        preferences.remove("musicEnabled");
        preferences.remove("musicVolume");
        preferences.remove("soundEnabled");
        preferences.remove("soundVolume");
        preferences.remove("difficulty");
        preferences.remove("showFPS");
        preferences.remove("showCoords");
        preferences.remove("showMobile");
        preferences.remove("statusBarTransparency");
        preferences.remove("pStatusBarTransparency");
        preferences.remove("hitMarkerTransparency");
        preferences.remove("screenShake");
        preferences.remove("vSync");
        preferences.remove("fps");
        preferences.flush();
    }

    public void removeProfileData() {
        preferences.remove("profileName");
        preferences.remove("profileItemIdSeq");
        preferences.remove("profileLevel");
        preferences.remove("profileLoadout");
        preferences.remove("profileWeapons");
        preferences.flush();
    }
}
