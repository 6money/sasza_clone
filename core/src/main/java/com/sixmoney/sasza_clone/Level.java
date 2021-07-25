package com.sixmoney.sasza_clone;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
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
import com.sixmoney.sasza_clone.entities.BulletCollisionSubObject;
import com.sixmoney.sasza_clone.entities.Canopy;
import com.sixmoney.sasza_clone.entities.Crate;
import com.sixmoney.sasza_clone.entities.Enemy;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.FloorTile;
import com.sixmoney.sasza_clone.entities.Player;
import com.sixmoney.sasza_clone.utils.CentralRayWithWhiskersConfig;
import com.sixmoney.sasza_clone.utils.ChaseCam;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.JBumpRaycastCollisionDetector;
import com.sixmoney.sasza_clone.utils.Utils;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Level {
    private static final String TAG = Level.class.getName();
    private World<Entity> world;
    private Viewport viewport;
    private ChaseCam camera;
    private Player player;

    private Array<FloorTile> grassTiles;
    private Array<FloorTile> dirtTiles;
    private Array<FloorTile> sandTiles;
    private Array<FloorTile> waterTiles;
    private Array<Entity> environmentEntities;
    private Array<Entity> canopyEntities;
    private Array<Enemy> enemyEntities;
    private final DelayedRemovalArray<Bullet> bullets;
    private BulletCollisionFilter bulletCollisionFilter;
    private long shootStartTime;

    public boolean shooting;

    public Level(Viewport viewport) {
        this.viewport = viewport;

        world = new World<>();
        world.setTileMode(true);

        grassTiles = new Array<>();
        dirtTiles = new Array<>();
        sandTiles = new Array<>();
        waterTiles = new Array<>();
        environmentEntities = new Array<>();
        canopyEntities = new Array<>();
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
            world.add(entity.bulletCollisionSubObject.item, entity.bulletCollisionSubObject.bbox.x, entity.bulletCollisionSubObject.bbox.y, entity.bulletCollisionSubObject.bbox.width, entity.bulletCollisionSubObject.bbox.height);
        }
    }

    public void setCanopyEntities(Array<Entity> entities) {
        this.canopyEntities = entities;
        for (Entity entity: canopyEntities) {
            world.add(entity.item, entity.bbox.x, entity.bbox.y, entity.bbox.width, entity.bbox.height);
//            world.add(entity.bulletCollisionSubObject.item, entity.bulletCollisionSubObject.bbox.x, entity.bulletCollisionSubObject.bbox.y, entity.bulletCollisionSubObject.bbox.width, entity.bulletCollisionSubObject.bbox.height);
        }
    }


    public void setEnemyEntities(Array<Enemy> entities) {
        this.enemyEntities = entities;
        for (Enemy enemy: enemyEntities) {
            world.add(enemy.item, enemy.bbox.x, enemy.bbox.y, enemy.bbox.width, enemy.bbox.height);

            RayConfiguration<Vector2> rayConfiguration = new CentralRayWithWhiskersConfig(enemy, 30, 12, 40);
            RaycastCollisionDetector<Vector2> raycastCollisionDetector = new JBumpRaycastCollisionDetector(world);
            RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<>(enemy, rayConfiguration, raycastCollisionDetector, 0);
            enemy.addBehavior(raycastObstacleAvoidance);

            Arrive<Vector2> arrive = new Arrive<>(enemy, player)
                    .setTimeToTarget(0.1f)
                    .setArrivalTolerance(50f)
                    .setDecelerationRadius(50);
            enemy.addBehavior(arrive);
        }
    }

    public void update(float delta) {
        if (shooting && Utils.secondsSince(shootStartTime) > 1 / player.getGun().getFireRate()) {
            shootStartTime = TimeUtils.nanoTime();
            shoot();
        }

        for (Enemy enemy: enemyEntities) {
            enemy.update(delta, world);
        }

        for (Entity entity: canopyEntities) {
            entity.update(delta, world);
        }

        player.update(delta, world);

        bullets.begin();
        for (Bullet bullet: bullets) {
            bullet.update(delta);
            if (bullet.getDead()) {
                bullets.removeValue(bullet, true);
            }
        }
        bullets.end();
    }

    public void render(Batch batch, ShapeDrawer drawer) {
        for (FloorTile tile: dirtTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: grassTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: sandTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: waterTiles) {
            tile.render(batch);
        }
        for (Entity entity: enemyEntities) {
            entity.renderSecondary(batch);
        }
        player.renderSecondary(batch);
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

        for (Entity entity: canopyEntities) {
            entity.render(batch);
        }
    }

    public void renderDebug(ShapeDrawer drawer) {
        for (FloorTile tile: waterTiles) {
            tile.renderDebug(drawer);
        }
        for (Entity entity: environmentEntities) {
            entity.renderDebug(drawer);

            if (entity.bulletCollisionSubObject != null) {
                entity.bulletCollisionSubObject.renderDebug(drawer);
            }
        }
        for (Entity entity: enemyEntities) {
            entity.renderDebug(drawer);
        }
        for (Entity entity: canopyEntities) {
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
                ((Entity) item.userData).decrementHealth(player.getGun().getDamage());
                if (((Entity) item.userData).getHealth() <= 0) {
                    if (item.userData instanceof Enemy) {
                        world.remove(item);
                        enemyEntities.removeValue((Enemy) item.userData, true);
                    } else if (item.userData instanceof Crate) {
                        world.remove(item);
                        environmentEntities.removeValue((Entity) item.userData, true);
                        if (((Crate) item.userData).bulletCollisionSubObject != null) {
                            world.remove(((Crate) item.userData).bulletCollisionSubObject.item);
                        }
                    } else if (item.userData instanceof BulletCollisionSubObject) {
                        world.remove(item);
                        world.remove(((BulletCollisionSubObject) item.userData).parent.item);
                        environmentEntities.removeValue(((BulletCollisionSubObject) item.userData).parent, true);
                    }
                }
            }
        }

        bullets.add(new Bullet(player.position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x, player.position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y, player.rotation, bulletVector.x, bulletVector.y, player.getGun().getProjectileSpeed()));
        player.getGun().decrementCurrentMagazineAmmo();
    }

    public static class BulletCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if ((item.userData instanceof FloorTile)) return null;
            if ((item.userData instanceof Canopy)) return null;
            else return Response.touch;
        }
    }
}
