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

    @ConsoleDoc(description = "Display info about the higlighted entity") public final void entityInfo() {
        console.log(gameWorld.clickedEntity.toString());
    }

    // Player commands
    @ConsoleDoc(description = "Teleports the player to the specified location") public final void teleport(float x, float y) {
        gameWorld.level.teleportPlayer(x, y);
        console.log("Teleported player speed to " + gameWorld.level.getPlayer().getPosition().toString());
    }

    @ConsoleDoc(description = "Sets player movement speed") public final void setPlayerSpeed(float speed) {
        gameWorld.level.getPlayer().playerSpeed = speed;
        console.log("Set player movement speed to " + speed);
    }

    @ConsoleDoc(description = "Resets player movement speed to default") public final void resetPlayerSpeed() {
        gameWorld.level.getPlayer().playerSpeed = Constants.DEFAULT_PLAYER_SPEED;
        console.log("Reset player movement speed to " + Constants.DEFAULT_PLAYER_SPEED);
    }

    // Spawn commands
    @ConsoleDoc(description = "Spawns specified number of enemies at random areas around player") public final void spawnEnemy(float quantity) {
        gameWorld.level.spawnEnemy(quantity);
        console.log("Spawned " + quantity + " enemies");
    }

    @ConsoleDoc(description = "Spawns specified number of NPCs at random areas around player") public final void spawnNPC(float quantity) {
        gameWorld.level.spawnNPC(quantity);
        console.log("Spawned " + quantity + " NPCs");
    }


    // Weapon commands
    @ConsoleDoc(description = "Set current weapon's ammo for clicked entity") public final void setWeaponAmmo(int quantity) {
        gameWorld.getClickedEntity().getGun().setCurrentAmmo(quantity);
        console.log("Updated weapon ammo quantity for " + gameWorld.getClickedEntity().getClass().getSimpleName());
    }

    @ConsoleDoc(description = "Set current weapon's range for clicked entity") public final void setWeaponRange(float range) {
        gameWorld.getClickedEntity().getGun().setRange(range);
        console.log("Updated weapon range for " + gameWorld.getClickedEntity().getClass().getSimpleName());
    }

    @ConsoleDoc(description = "Set current weapon's damage for clicked entity") public final void setWeaponDamage(float damage) {
        gameWorld.getClickedEntity().getGun().setDamage(damage);
        console.log("Updated weapon damage for " + gameWorld.getClickedEntity().getClass().getSimpleName());
    }

    @ConsoleDoc(description = "Set current weapon's fire rate for clicked entity") public final void setWeaponFireRate(float fireRate) {
        gameWorld.getClickedEntity().getGun().setFireRate(fireRate);
        console.log("Updated weapon fire rate for " + gameWorld.getClickedEntity().getClass().getSimpleName());
    }

    @ConsoleDoc(description = "Set current weapon's magazine size for clicked entity") public final void setWeaponMagSize(int quantity) {
        gameWorld.getClickedEntity().getGun().setMagazineSize(quantity);
        console.log("Updated weapon magazine size for " + gameWorld.getClickedEntity().getClass().getSimpleName());
    }

    @ConsoleDoc(description = "Set current weapon's projectile speed for clicked entity") public final void setWeaponProjectileSpeed(float speed) {
        gameWorld.getClickedEntity().getGun().setProjectileSpeed(speed);
        console.log("Updated weapon projectile speed for " + gameWorld.getClickedEntity().getClass().getSimpleName());
    }
}
