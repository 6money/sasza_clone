package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.sixmoney.sasza_clone.entities.Gun;
import com.sixmoney.sasza_clone.staticData.GunData;

public class Profile {
    private static final String TAG = Profile.class.getName();
    private String name;
    private int profileLevel;
    private int profileXP;
    private int itemIdSeq;
    private Array<Float> profileScores;
    private Array<Gun> loadout;
    private Array<Gun> profileGuns;


    public Profile() {
        name = PreferenceManager.get_instance().getProfileName();
        profileLevel = PreferenceManager.get_instance().getProfileLevel();
        itemIdSeq = PreferenceManager.get_instance().getProfileItemIdSeq();
        loadout = new Array<>();
        profileGuns = new Array<>();

        if (name.equals("default")) {
            itemIdSeq = 0;
        }

        Gdx.app.log(TAG, name + "");
        Gdx.app.log(TAG, itemIdSeq + "");

        String gunDataString = PreferenceManager.get_instance().getProfileWeapons();
        if (!gunDataString.equals("new") && !name.equals("default")) {
            Json json = new Json();
            JsonReader jsonReader = new JsonReader();
            JsonValue gunsJson = jsonReader.parse(gunDataString);

            for (JsonValue gunJson : gunsJson) {
                Gun gun = json.fromJson(Gun.class, gunJson.toJson(JsonWriter.OutputType.json));
                gun.initGun();
                addProfileGun(gun);
            }
        } else {
            profileGuns = new Array<>();
            addProfileGun(new Gun(GunData.mp5));
            addProfileGun(new Gun(GunData.m4));
            addProfileGun(new Gun(GunData.svd));
            addProfileGun(new Gun(GunData.pkm));
            addProfileGun(new Gun(GunData.vaporizer));
            setProfileGuns(profileGuns);
        }

        gunDataString = PreferenceManager.get_instance().getProfileLoadout();
        if (!gunDataString.equals("new") && !name.equals("default")) {
            Json json = new Json();
            JsonReader jsonReader = new JsonReader();
            JsonValue gunsJson = jsonReader.parse(gunDataString);

            for (JsonValue gunJson : gunsJson) {
                if (!gunJson.toString().equals("null")) {Gun gun = json.fromJson(Gun.class, gunJson.toJson(JsonWriter.OutputType.json));
                    gun.initGun();
                    loadout.add(gun);
                } else {
                    loadout.add(null);
                }
            }
        } else {
            loadout.add(new Gun(GunData.mp5));
            addProfileGun(loadout.get(0));
        }

        if (loadout.size < 3) {
            for (int i = 0; i <= 3 - loadout.size; i++) {
                loadout.add(null);
            }
        }

        // if this is not a new profile, oh no :(
        // try find highest itemId from items to set seq
        if (itemIdSeq == -1) {

            int tempId = itemIdSeq;

            for (Gun gun: new Array.ArrayIterator<>(profileGuns)) {
                if (gun.getItemId() > tempId) {
                    tempId = gun.getItemId();
                }
            }

            itemIdSeq = tempId + 1;
            PreferenceManager.get_instance().setProfileItemIdSeq(itemIdSeq);
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        PreferenceManager.get_instance().setProfileName(name);
    }

    public int getProfileLevel() {
        return profileLevel;
    }

    public void setProfileLevel(int profileLevel) {
        this.profileLevel = profileLevel;
        PreferenceManager.get_instance().setProfileLevel(profileLevel);
    }

    public int getProfileXP() {
        return profileXP;
    }

    public void setProfileXP(int profileXP) {
        this.profileXP = profileXP;
    }

    public Array<Float> getProfileScores() {
        return profileScores;
    }

    public void setProfileScores(Array<Float> profileScores) {
        this.profileScores = profileScores;
    }

    public Array<Gun> getLoadout() {
        return loadout;
    }

    public void setLoadout(Array<Gun> loadout) {
        this.loadout = loadout;
        Json json = new Json();
        JsonValue gunsJson = new JsonValue(JsonValue.ValueType.array);

        for (Gun gun: new Array.ArrayIterator<>(loadout)) {
            String gunJson = json.toJson(gun);
            gunsJson.addChild(new JsonValue(gunJson));
        }

        PreferenceManager.get_instance().setProfileLoadout(gunsJson.toJson(JsonWriter.OutputType.json));
    }

    public Array<Gun> getProfileGuns() {
        return profileGuns;
    }

    public void addProfileGun(Gun gun) {
        if (gun.getItemId() == -1) {
            gun.setItemId(itemIdSeq);
            itemIdSeq++;
            PreferenceManager.get_instance().setProfileItemIdSeq(itemIdSeq);
        }
        profileGuns.add(gun);
    }

    public void setProfileGuns(Array<Gun> profileGuns) {
        this.profileGuns = profileGuns;
        Json json = new Json();
        JsonValue gunsJson = new JsonValue(JsonValue.ValueType.array);

        for (Gun gun: new Array.ArrayIterator<>(profileGuns)) {
            String gunJson = json.toJson(gun);
            gunsJson.addChild(new JsonValue(gunJson));
        }

        PreferenceManager.get_instance().setProfileWeapons(gunsJson.toJson(JsonWriter.OutputType.json));
    }
}
