package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sixmoney.sasza_clone.entities.Player;

public class ChaseCam extends OrthographicCamera {
    public Player player;

    public ChaseCam(Player player) {
        super();
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
        position.set(player.getPosition().x + Constants.PLAYER_CENTER.x, player.getPosition().y + Constants.PLAYER_CENTER.x, 0);
        super.update();
    }
}
