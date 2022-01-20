package com.sixmoney.sasza_clone.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.sasza_clone.Sasza;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.PreferenceManager;

public class OptionsScreen implements Screen {
    private static final String TAG = OptionsScreen.class.getName();
    private Sasza saszaGame;
    private Stage stage;
    private Skin skin;
    private Button buttonBack;
    private Window window;
    private CheckBox checkboxMusic;
    private CheckBox checkboxSounds;
    private CheckBox checkboxFPS;
    private CheckBox checkboxCoords;
    private CheckBox checkboxMobile;
    private CheckBox checkboxScreenShake;
    private CheckBox checkBoxVSync;
    private Slider sliderMusic;
    private Slider sliderSounds;
    private Slider sliderDifficulty;
    private Slider sliderStatusBarTransparency;
    private Slider sliderPlayerStatusBarTransparency;
    private Slider sliderHitMarkerTransparency;
    private Slider sliderFps;
    private PreferenceManager preferenceManager;

    public OptionsScreen(Sasza saszaGame) {
        this.saszaGame = saszaGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = Assets.get_instance().skinAssets.skin;
        stage.setDebugAll(saszaGame.debug);

        buttonBack = new Button(skin);
        buttonBack.add(new Label("Back" ,skin));
        buttonBack.setWidth(stage.getWidth() / 8);
        buttonBack.setHeight(stage.getHeight() / 10);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saszaGame.switchScreen("menu");
                dispose();
            }
        });

        Label labelVersion = new Label(Constants.VERSION, skin);
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

        window.row();
        window.add(new Label("Show Coordinates", skin));
        checkboxCoords = new CheckBox(null, skin);
        window.add(checkboxCoords);
        checkboxCoords.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setShowCoords(checkboxCoords.isChecked());
            }
        });

        window.row();
        window.add(new Label("Enable Mobile Controls", skin));
        checkboxMobile = new CheckBox(null, skin);
        window.add(checkboxMobile);
        checkboxMobile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saszaGame.mobileControls = checkboxMobile.isChecked();
                preferenceManager.setMobile(checkboxMobile.isChecked());
            }
        });

        window.row();
        window.add(new Label("Player Status Bar Transparency", skin));
        sliderPlayerStatusBarTransparency = new Slider(0, 100, 5, false, skin);
        window.add(sliderPlayerStatusBarTransparency);
        sliderPlayerStatusBarTransparency.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setPStatusBarTransparency(sliderPlayerStatusBarTransparency.getValue());
            }
        });

        window.row();
        window.add(new Label("Status Bar Transparency", skin));
        sliderStatusBarTransparency = new Slider(0, 100, 5, false, skin);
        window.add(sliderStatusBarTransparency);
        sliderStatusBarTransparency.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setStatusBarTransparency(sliderStatusBarTransparency.getValue());
            }
        });

        window.row();
        window.add(new Label("Hit Marker Transparency", skin));
        sliderHitMarkerTransparency = new Slider(0, 100, 5, false, skin);
        window.add(sliderHitMarkerTransparency);
        sliderHitMarkerTransparency.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setHitMarkerTransparency(sliderHitMarkerTransparency.getValue());
            }
        });

        window.row();
        window.add(new Label("Enable Screen Shake", skin));
        checkboxScreenShake = new CheckBox(null, skin);
        window.add(checkboxScreenShake);
        checkboxScreenShake.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setScreenShake(checkboxScreenShake.isChecked());
            }
        });

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            window.row();
            window.add(new Label("Enable v-sync", skin));
            checkBoxVSync = new CheckBox(null, skin);
            window.add(checkBoxVSync);
            checkBoxVSync.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.graphics.setVSync(checkBoxVSync.isChecked());
                    preferenceManager.setVSync(checkBoxVSync.isChecked());
                }
            });

            window.row();
            window.add(new Label("Frame rate", skin));
            sliderFps = new Slider(30, 150, 5, false, skin);
            window.add(sliderFps);
            sliderFps.addListener(new DragListener() {
                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer) {
                    int fps = (int) sliderFps.getValue();
                    if (fps == 150) {
                        fps = 0;
                    }

                    Gdx.graphics.setForegroundFPS(fps);
                    preferenceManager.setFps((int) sliderFps.getValue());
                }
            });
        }

        window.row();
        Button buttonResetData = new Button(skin);
        buttonResetData.add(new Label("Reset Preferences data", skin));
        window.add(buttonResetData).height(stage.getWidth() / 20).uniform();
        buttonResetData.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.removePreferencesData();
                init_saved_settings();
            }
        });
        Button buttonResetProfile = new Button(skin);
        buttonResetProfile.add(new Label("Reset profile data", skin));
        window.add(buttonResetProfile).uniform();
        buttonResetProfile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.removeProfileData();
                saszaGame.profile = null;
                init_saved_settings();
            }
        });


//        window.setSize(stage.getWidth() / 1.5f, stage.getHeight() / 1.5f);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);
        window.pack();
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
        boolean showCoords = preferenceManager.getShowCoords();
        boolean showMobile = preferenceManager.getMobile();
        boolean screenShake = preferenceManager.getScreenShake();
        float statusBarTransparency = preferenceManager.getStatusBarTransparency();
        float playerStatusBarTransparency = preferenceManager.getPStatusBarTransparency();
        float hitMarkerTransparency = preferenceManager.getHitMarkerTransparency();
        boolean vSync = preferenceManager.getVSync();
        int fps = preferenceManager.getFps();

        checkboxMusic.setChecked(musicEnabled);
        sliderMusic.setValue(musicVolume);
        checkboxSounds.setChecked(soundEnabled);
        sliderSounds.setValue(soundVolume);
        sliderDifficulty.setValue(difficulty);
        checkboxFPS.setChecked(showFPS);
        checkboxCoords.setChecked(showCoords);
        checkboxScreenShake.setChecked(screenShake);
        checkBoxVSync.setChecked(vSync);
        checkboxMobile.setChecked(showMobile);
        sliderStatusBarTransparency.setValue(statusBarTransparency);
        sliderPlayerStatusBarTransparency.setValue(playerStatusBarTransparency);
        sliderHitMarkerTransparency.setValue(hitMarkerTransparency);
        sliderFps.setValue(fps);

//        SoundManager.get_instance().updateSoundPreferences();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Constants.BG_COLOR);

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
