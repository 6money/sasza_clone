package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.sixmoney.sasza_clone.entities.Gun;
import com.sixmoney.sasza_clone.staticData.GunData;

public class Profile {
    private String name;
    private int profileLevel;
    private int profileXP;
    private Array<Float> profileScores;
    private Array<Gun> loadout;
    private Array<Gun> profileGuns;


    public Profile() {
        name = PreferenceManager.get_instance().getProfileName();
        profileLevel = PreferenceManager.get_instance().getProfileLevel();
        loadout = new Array<>();
        profileGuns = new Array<>();

        String gunDataString = PreferenceManager.get_instance().getProfileLoadout();
        if (!gunDataString.equals("new") && !name.equals("default")) {
            Json json = new Json();
            JsonReader jsonReader = new JsonReader();
            JsonValue gunsJson = jsonReader.parse(gunDataString);

            for (JsonValue gunJson : gunsJson) {
                Gun gun = json.fromJson(Gun.class, gunJson.toJson(JsonWriter.OutputType.json));
                gun.initGun();
                loadout.add(gun);
            }
        }

        gunDataString = PreferenceManager.get_instance().getProfileWeapons();
        if (!gunDataString.equals("new") && !name.equals("default")) {
            Json json = new Json();
            JsonReader jsonReader = new JsonReader();
            JsonValue gunsJson = jsonReader.parse(gunDataString);

            for (JsonValue gunJson : gunsJson) {
                Gun gun = json.fromJson(Gun.class, gunJson.toJson(JsonWriter.OutputType.json));
                gun.initGun();
                profileGuns.add(gun);
            }
        } else {
            loadout.add(new Gun(GunData.mp5));
            loadout.add(new Gun(GunData.svd));
            loadout.add(new Gun(GunData.vaporizer));

            profileGuns = new Array<>(loadout);
            profileGuns.add(new Gun(GunData.mp5));
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

        for (Gun gun: loadout) {
            String gunJson = json.toJson(gun);
            gunsJson.addChild(new JsonValue(gunJson));
        }

        PreferenceManager.get_instance().setProfileLoadout(gunsJson.toJson(JsonWriter.OutputType.json));
    }

    public Array<Gun> getProfileGuns() {
        return profileGuns;
    }

    public void setProfileGuns(Array<Gun> profileGuns) {
        this.profileGuns = profileGuns;
        Json json = new Json();
        JsonValue gunsJson = new JsonValue(JsonValue.ValueType.array);

        for (Gun gun: profileGuns) {
            String gunJson = json.toJson(gun);
            gunsJson.addChild(new JsonValue(gunJson));
        }

        PreferenceManager.get_instance().setProfileWeapons(gunsJson.toJson(JsonWriter.OutputType.json));
    }
}
