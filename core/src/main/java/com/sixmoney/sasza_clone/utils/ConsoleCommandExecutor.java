package com.sixmoney.sasza_clone.utils;

import com.sixmoney.sasza_clone.screens.GameWorldScreen;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.annotation.ConsoleDoc;

public class ConsoleCommandExecutor extends CommandExecutor {
    private GameWorldScreen gameWorld;

    public ConsoleCommandExecutor(GameWorldScreen gameWorld) {
        this.gameWorld = gameWorld;
    }

    // Debug commands
    @ConsoleDoc(description = "Toggle debug mode on and off") public final void toggleDebugMode() {
        gameWorld.toggleDebug();
    }

    @ConsoleDoc(description = "Toggle mobile controls on and off") public final void toggleMobileControls() {
        gameWorld.toggleMobileControls();
    }

    // Player commands
    @ConsoleDoc(description = "Teleports the player to the specified location") public final void teleport(float x, float y) {
        gameWorld.level.teleportPlayer(x, y);
    }

    @ConsoleDoc(description = "Sets player movement speed") public final void setPlayerSpeed(float speed) {
        gameWorld.level.getPlayer().playerSpeed = speed;
    }

    @ConsoleDoc(description = "Resets player movement speed to default") public final void resetPlayerSpeed() {
        gameWorld.level.getPlayer().playerSpeed = Constants.DEFAULT_PLAYER_SPEED;
    }

    // Spawn commands
    @ConsoleDoc(description = "Spawns specified number of enemies at random areas around player") public final void spawnEnemy(float quantity) {
        gameWorld.level.spawnEnemy(quantity);
    }

    @ConsoleDoc(description = "Spawns specified number of NPCs at random areas around player") public final void spawnNPC(float quantity) {
        gameWorld.level.spawnNPC(quantity);
    }


    // Weapon commands
    @ConsoleDoc(description = "Set current weapon's ammo") public final void setWeaponAmmo(int quantity) {
        gameWorld.level.getPlayer().getGun().setCurrentAmmo(quantity);
    }

    @ConsoleDoc(description = "Set current weapon's range") public final void setWeaponRange(float range) {
        gameWorld.level.getPlayer().getGun().setRange(range);
    }

    @ConsoleDoc(description = "Set current weapon's damage") public final void setWeaponDamage(float damage) {
        gameWorld.level.getPlayer().getGun().setDamage(damage);
    }

    @ConsoleDoc(description = "Set current weapon's fire rate") public final void setWeaponFireRate(float fireRate) {
        gameWorld.level.getPlayer().getGun().setFireRate(fireRate);
    }

    @ConsoleDoc(description = "Set current weapon's magazine size") public final void setWeaponMagSize(int quantity) {
        gameWorld.level.getPlayer().getGun().setMagazineSize(quantity);
    }

    @ConsoleDoc(description = "Set current weapon's projectile speed") public final void setWeaponProjectileSpeed(float speed) {
        gameWorld.level.getPlayer().getGun().setProjectileSpeed(speed);
    }
}
