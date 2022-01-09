package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.FloatArray;
import com.sixmoney.sasza_clone.entities.Player;

import java.util.Random;
import java.util.stream.IntStream;

public class ChaseCam extends OrthographicCamera {
    private FloatArray samples;
    private float timer = 0f;
    private float duration = 0f;
    private int amplitude = 0;
    private int frequency = 0;
    private boolean isFading = true;
    private boolean shake = false;

    public Player player;

    public ChaseCam(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void zoomIn(float zoomAmount) {
        if (!(zoom < 0.5)) {
            zoom -= zoomAmount;
        }
    }

    public void zoomOut(float zoomAmount) {
        if (!(zoom > 2)) {
            zoom += zoomAmount;
        }
    }

    public void shake() {
        shake(4, 90);
    }

    public void shake(int amplitude, int frequency) {
        shake(0.2f, amplitude, frequency, true);
    }

    public void shake(float time, int amp, int freq, boolean fade) {
        shake = true;
        timer = 0f;
        duration = time;
        amplitude = amp;
        frequency = freq;
        isFading = fade;
        samples = new FloatArray(frequency);
        Random random = new Random();
        IntStream.range(0, frequency).forEach(i -> samples.add(random.nextFloat() * 2f - 1f));
    }

    public void update() {
        if (player != null) {
            position.set(player.getPosition().x, player.getPosition().y, 0);
        }

        if (shake && PreferenceManager.get_instance().getScreenShake()) {
            if (timer > duration) {
                shake = false;
            }

            float dt = Gdx.graphics.getDeltaTime();
            timer += dt;
            if (duration > 0) {
                duration -= dt;
                float shakeTime = timer * frequency;
                int first = (int) shakeTime;
                int second = (first + 1) % frequency;
                int third = (first + 2) % frequency;
                float deltaT = shakeTime - (int) shakeTime;
                float deltaX = samples.get(first) * deltaT + samples.get(second) * (1f - deltaT);
                float deltaY = samples.get(second) * deltaT + samples.get(third) * (1f - deltaT);

                if (isFading) {
                    position.x += deltaX * amplitude * Math.min(duration, 1f);
                    position.y += deltaY * amplitude * Math.min(duration, 1f);
                } else {
                    position.x += deltaX * amplitude * 1f;
                    position.y += deltaY * amplitude * 1f;
                }
            }
        }

        super.update();
    }
}
