package com.sixmoney.sasza_clone.utils;

import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.annotation.ConsoleDoc;

public class ConsoleCommandExecutor extends CommandExecutor {
    private GameWorldScreen gameWorld;

    public ConsoleCommandExecutor(GameWorldScreen gameWorld) {
        this.gameWorld = gameWorld;
    }


    @ConsoleDoc(description = "Teleports the player to the specified location") public final void teleport(float x, float y) {
        gameWorld.level.teleportPlayer(x, y);
    }

    @ConsoleDoc(description = "Sets player movement speed") public final void setPlayerSpeed(float speed) {
        gameWorld.level.getPlayer().playerSpeed = speed;
    }

    @ConsoleDoc(description = "Resets player movement speed to default") public final void resetPlayerSpeed() {
        gameWorld.level.getPlayer().playerSpeed = Constants.DEFAULT_PLAYER_SPEED;
    }
}
