package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sixmoney.sasza_clone.entities.Player;

public class ChaseCam extends OrthographicCamera {
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

    public void update() {
        super.update();
        if (player != null) {
            position.set(player.getPosition().x, player.getPosition().y, 0);
        }
    }
}
