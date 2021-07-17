package com.sixmoney.sasza_clone;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.ItemInfo;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.entities.Bullet;
import com.sixmoney.sasza_clone.entities.Crate;
import com.sixmoney.sasza_clone.entities.Enemy;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.FloorTile;
import com.sixmoney.sasza_clone.entities.Player;
import com.sixmoney.sasza_clone.utils.ChaseCam;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.Utils;

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
    private Array<Enemy> enemyEntities;
    private final DelayedRemovalArray<Bullet> bullets;
    private BulletCollisionFilter bulletCollisionFilter;
    private long shootStartTime;

    public boolean shooting;

    public Level(Viewport viewport) {
        this.viewport = viewport;

        world = new World<>();
        world.setTileMode(false);

        grassTiles = new Array<>();
        dirtTiles = new Array<>();
        sandTiles = new Array<>();
        waterTiles = new Array<>();
        environmentEntities = new Array<>();
        enemyEntities = new Array<>();
        bullets = new DelayedRemovalArray<>();
        bulletCollisionFilter = new BulletCollisionFilter();
        shooting = false;
        shootStartTime = TimeUtils.nanoTime();


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

    public void setEnemyEntities(Array<Enemy> entities) {
        this.enemyEntities = entities;
        for (Entity entity: enemyEntities) {
            world.add(entity.item, entity.bbox.x, entity.bbox.y, entity.bbox.width, entity.bbox.height);
        }

        Arrive<Vector2> arrive = new Arrive<Vector2>(enemyEntities.get(0), player)
                .setTimeToTarget(0.1f)
                .setArrivalTolerance(2f)
                .setDecelerationRadius(5);
        enemyEntities.get(0).setBehavior(arrive);
    }

    public void update(float delta) {
        if (shooting && Utils.secondsSince(shootStartTime) > 1 / player.getGun().getFireRate()) {
            shootStartTime = TimeUtils.nanoTime();
            shoot();
        }

        for (Enemy enemy: enemyEntities) {
            enemy.update(delta, world);
        }

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
        for (Entity entity: enemyEntities) {
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
        for (Entity entity: enemyEntities) {
            entity.renderDebug(drawer);
        }
        player.renderDebug(drawer);
    }

    public void shoot() {
        if (player.getGun().getCurrentAmmo() == 0 && player.getGun().getCurrentMagazineAmmo() == 0) {
            return;
        }

        if (player.getGun().getCurrentMagazineAmmo() <= 0) {
            player.getGun().reload();
        }

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
                bulletCollisionFilter,
                items
        );

        if (items.size() > 0) {
            bulletVector.set(items.get(0).x1, items.get(0).y1);

            Item item = items.get(0).item;

            if (((Entity) item.userData).destructible) {
                ((Entity) item.userData).health -= player.getGun().getDamage();
                if (((Entity) item.userData).health <= 0) {
                    if (item.userData instanceof Enemy) {
                        enemyEntities.removeValue((Enemy) item.userData, true);
                    } else if (item.userData instanceof Crate) {
                        environmentEntities.removeValue((Entity) item.userData, true);
                    }
                    world.remove(item);
                }
            }
        }

        bullets.add(new Bullet(player.position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x, player.position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y, player.rotation, bulletVector.x, bulletVector.y, player.getGun().getProjectileSpeed()));
        player.getGun().decrementCurrentMagazineAmmo();
    }

    public static class BulletCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if (!(item.userData instanceof FloorTile)) return Response.touch;
            else return null;
        }
    }
}
