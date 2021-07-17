package com.sixmoney.sasza_clone;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.ItemInfo;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.entities.Bullet;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.FloorTile;
import com.sixmoney.sasza_clone.entities.Player;
import com.sixmoney.sasza_clone.utils.ChaseCam;
import com.sixmoney.sasza_clone.utils.Constants;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Level {
    private World<Entity> world;
    private Viewport viewport;
    private ChaseCam camera;
    private Player player;

    private Array<FloorTile> grassTiles;
    private Array<FloorTile> dirtTiles;
    private Array<FloorTile> sandTiles;
    private Array<FloorTile> waterTiles;
    private Array<Entity> environmentEntities;
    private final DelayedRemovalArray<Bullet> bullets;

    public Level(Viewport viewport) {
        this.viewport = viewport;

        world = new World<>();
        world.setTileMode(false);

        grassTiles = new Array<>();
        dirtTiles = new Array<>();
        sandTiles = new Array<>();
        waterTiles = new Array<>();
        environmentEntities = new Array<>();
        bullets = new DelayedRemovalArray<>();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        world.add(player.item, player.bbox.x, player.bbox.y, player.bbox.width, player.bbox.height);
        camera = new ChaseCam(player);
        viewport.setCamera(camera);
    }

    public void setGrassTiles(Array<FloorTile> grassTiles) {
        this.grassTiles = grassTiles;
    }

    public void setDirtTiles(Array<FloorTile> dirtTiles) {
        this.dirtTiles = dirtTiles;
    }

    public void setSandTiles(Array<FloorTile> sandTiles) {
        this.sandTiles = sandTiles;
    }

    public void setWaterTiles(Array<FloorTile> waterTiles) {
        this.waterTiles = waterTiles;
        for (FloorTile waterTile: waterTiles) {
            waterTile.bbox.x += 55;
            waterTile.bbox.y += 55;
            waterTile.bbox.width -= 110;
            waterTile.bbox.height -= 110;
            world.add(waterTile.item, waterTile.bbox.x, waterTile.bbox.y, waterTile.bbox.width, waterTile.bbox.height);
        }
    }

    public void setEnvironmentEntities(Array<Entity> entities) {
        this.environmentEntities = entities;
        for (Entity entity: environmentEntities) {
            world.add(entity.item, entity.bbox.x, entity.bbox.y, entity.bbox.width, entity.bbox.height);
        }
    }

    public void update(float delta) {
        player.update(delta, world);

        bullets.begin();
        for (Bullet bullet: bullets) {
            bullet.update();
            if (bullet.getDead()) {
                bullets.removeValue(bullet, true);
            }
        }
        bullets.end();
    }

    public void render(Batch batch, ShapeDrawer drawer) {
        for (FloorTile tile: grassTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: dirtTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: sandTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: waterTiles) {
            tile.render(batch);
        }
        for (Entity entity: environmentEntities) {
            entity.render(batch);
        }

        player.render(batch, drawer);

        for (Bullet bullet: bullets) {
            bullet.render(batch);
        }
    }

    public void renderDebug(ShapeDrawer drawer) {
        for (FloorTile tile: waterTiles) {
            tile.renderDebug(drawer);
        }
        for (Entity entity: environmentEntities) {
            entity.renderDebug(drawer);
        }
        player.renderDebug(drawer);
    }

    public void shoot() {
        ArrayList<ItemInfo> items = new ArrayList<>();
        float rotation = player.rotation;
        Vector2 bulletOffsetTemp = new Vector2(player.getBulletOffset());
        bulletOffsetTemp.rotateDeg(rotation);
        Vector2 bulletVector = new Vector2(0, -1);
        bulletVector.rotateDeg(rotation);
        bulletVector.setLength(player.getGun().getRange());
        bulletVector.add(player.position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x, player.position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y);

        world.querySegmentWithCoords(
                player.position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x,
                player.position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y,
                bulletVector.x,
                bulletVector.y,
                CollisionFilter.defaultFilter,
                items
        );

        if (items.size() > 0) {
            bulletVector.set(items.get(0).x1, items.get(0).y1);
        }

        bullets.add(new Bullet(player.position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x, player.position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y, player.rotation, bulletVector.x, bulletVector.y, player.getGun().getProjectileSpeed()));
        player.shoot();
    }
}
