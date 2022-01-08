package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.sixmoney.sasza_clone.entities.Gun;

public class Profile {
    private String name;
    private int profileLevel;
    private int profileXP;
    private Array<Float> profileScores;
    private Array<Gun> guns;


    public Profile() {
        name = PreferenceManager.get_instance().getProfileName();
        profileLevel = PreferenceManager.get_instance().getProfileLevel();
        guns = new Array<>();

        String gunDataString = PreferenceManager.get_instance().getProfileWeapons();
        if (!gunDataString.equals("new") && !name.equals("default")) {
            Json json = new Json();
            JsonReader jsonReader = new JsonReader();
            JsonValue gunsJson = jsonReader.parse(gunDataString);

            for (JsonValue gunJson : gunsJson) {
                Gun gun = json.fromJson(Gun.class, gunJson.toJson(JsonWriter.OutputType.json));
                gun.setTextures();
                guns.add(gun);
            }
        } else {
            guns.add(new Gun(GunData.mp5));
            guns.add(new Gun(GunData.svd));
            guns.add(new Gun(GunData.m4));
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

    public Array<Gun> getGuns() {
        return guns;
    }

    public void setGuns(Array<Gun> guns) {
        this.guns = guns;
        Json json = new Json();
        JsonValue gunsJson = new JsonValue(JsonValue.ValueType.array);

        for (Gun gun: guns) {
            String gunJson = json.toJson(gun);
            gunsJson.addChild(new JsonValue(gunJson));
        }

        PreferenceManager.get_instance().setProfileWeapons(gunsJson.toJson(JsonWriter.OutputType.json));
    }
}
