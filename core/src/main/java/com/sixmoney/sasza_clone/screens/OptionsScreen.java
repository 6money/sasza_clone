package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.PreferenceManager;

public class OptionsScreen implements Screen {
    private static final String TAG = OptionsScreen.class.getName();
    private Sasza game;
    private Stage stage;
    private Skin skin;
    private Button buttonBack;
    private Window window;
    private CheckBox checkboxMusic;
    private CheckBox checkboxSounds;
    private CheckBox checkboxFPS;
    private CheckBox checkboxMobile;
    private Slider sliderMusic;
    private Slider sliderSounds;
    private Slider sliderDifficulty;
    private PreferenceManager preferenceManager;

    public OptionsScreen(Sasza saszaGame) {
        game = saszaGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = Assets.get_instance().skinAssets.skin;
        skin.getFont("font").getData().setScale(0.5f);

        buttonBack = new Button(skin);
        buttonBack.add(new Label("Back" ,skin));
        buttonBack.setWidth(stage.getWidth() / 8);
        buttonBack.setHeight(stage.getHeight() / 10);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.switchScreen("menu");
                dispose();
            }
        });

        Label labelVersion = new Label(Constants.VERSION, skin);
        labelVersion.setFontScale(0.5f);
        labelVersion.setAlignment(Align.left);
        labelVersion.setPosition(stage.getWidth() * 0.01f, stage.getHeight() * 0.01f);

        window = new Window("Options", skin);
        window.defaults().grow().pad(5);

        window.row();
        window.add(new Label("Music", skin));
        checkboxMusic = new CheckBox(null, skin);
        window.add(checkboxMusic);
        checkboxMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setMusic(checkboxMusic.isChecked());
//                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        window.row();
        window.add(new Label("Music Volume", skin));
        sliderMusic = new Slider(0, 100, 5, false, skin);
        window.add(sliderMusic);
        sliderMusic.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setMusicVolume(sliderMusic.getValue());
//                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        window.row();
        window.add(new Label("Sound Effects", skin));
        checkboxSounds = new CheckBox(null, skin);
        window.add(checkboxSounds);
        checkboxSounds.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setSound(checkboxSounds.isChecked());
//                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        window.row();
        window.add(new Label("Effects Volume", skin));
        sliderSounds = new Slider(0, 100, 5, false, skin);
        window.add(sliderSounds);
        sliderSounds.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setSoundVolume(sliderSounds.getValue());
//                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        window.row();
        window.add(new Label("Difficulty", skin));
        sliderDifficulty = new Slider(0, 100, 50, false, skin);
        window.add(sliderDifficulty);
        sliderDifficulty.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setDifficulty(sliderDifficulty.getValue());
            }
        });

        window.row();
        window.add(new Label("Show FPS", skin));
        checkboxFPS = new CheckBox(null, skin);
        window.add(checkboxFPS);
        checkboxFPS.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setShowFPS(checkboxFPS.isChecked());
            }
        });

        if (!game.mobileControls) {
            window.row();
            window.add(new Label("Enable Mobile Controls", skin));
            checkboxMobile = new CheckBox(null, skin);
            window.add(checkboxMobile);
            checkboxMobile.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    preferenceManager.setMobile(checkboxMobile.isChecked());
                }
            });
        }

        window.row();
        Button buttonResetData = new Button(skin);
        buttonResetData.add(new Label("Reset all data" ,skin));
        window.add(buttonResetData).height(stage.getWidth() / 20);
        buttonResetData.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.removeData();
                init_saved_settings();
            }
        });

        window.pack();
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);
        stage.addActor(window);
        stage.addActor(buttonBack);
        stage.addActor(labelVersion);

        preferenceManager = PreferenceManager.get_instance();
        init_saved_settings();

        Gdx.input.setInputProcessor(stage);
    }

    private void init_saved_settings() {
        boolean musicEnabled = preferenceManager.getMusic();
        boolean soundEnabled = preferenceManager.getSound();
        float musicVolume = preferenceManager.getMusicVolume();
        float soundVolume = preferenceManager.getSoundVolume();
        float difficulty = preferenceManager.getDifficulty();
        boolean showFPS = preferenceManager.getShowFPS();
        boolean showMobile = preferenceManager.getMobile();

        checkboxMusic.setChecked(musicEnabled);
        sliderMusic.setValue(musicVolume);
        checkboxSounds.setChecked(soundEnabled);
        sliderSounds.setValue(soundVolume);
        sliderDifficulty.setValue(difficulty);
        checkboxFPS.setChecked(showFPS);
        if (!game.mobileControls) {
            checkboxMobile.setChecked(showMobile);
        }

//        SoundManager.get_instance().updateSoundPreferences();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Constants.BG_COLOR.r,Constants.BG_COLOR.g,Constants.BG_COLOR.b,Constants.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
