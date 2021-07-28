package com.sixmoney.sasza_clone;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
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
import com.sixmoney.sasza_clone.entities.Zom1;
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

    private Array<Entity> tiles;
    private Array<Entity> environmentEntities;
    private Array<Entity> canopyEntities;
    private Array<Entity> wallEntities;
    private Array<Enemy> characterEntities;
    private Array<Zom1> enemyEntities;
    private final DelayedRemovalArray<Bullet> bullets;
    private BulletCollisionFilter bulletCollisionFilter;
    private long shootStartTime;

    public boolean shooting;

    public Level(Viewport viewport) {
        this.viewport = viewport;

        world = new World<>();
        world.setTileMode(true);

        tiles = new Array<>();
        environmentEntities = new Array<>();
        canopyEntities = new Array<>();
        characterEntities = new Array<>();
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

    public void setTiles(Array<Entity> tiles) {
        this.tiles = tiles;
        for (Entity tile: tiles) {
            if (tile.collidable) {
                world.add(tile.item, tile.bbox.x, tile.bbox.y, tile.bbox.width, tile.bbox.height);
            }
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
        }
    }

    public void setWallEntities(Array<Entity> entities) {
        this.wallEntities = entities;
        for (Entity entity: wallEntities) {
            if (entity.collidable) {
                world.add(entity.item, entity.bbox.x, entity.bbox.y, entity.bbox.width, entity.bbox.height);
                world.add(entity.bulletCollisionSubObject.item, entity.bulletCollisionSubObject.bbox.x, entity.bulletCollisionSubObject.bbox.y, entity.bulletCollisionSubObject.bbox.width, entity.bulletCollisionSubObject.bbox.height);
            }
        }
    }


    public void setCharacterEntities(Array<Enemy> entities) {
        this.characterEntities = entities;
        for (Enemy enemy: characterEntities) {
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


    public void setEnemyEntities(Array<Zom1> entities) {
        this.enemyEntities = entities;
        for (Zom1 zom: enemyEntities) {
            world.add(zom.item, zom.bbox.x, zom.bbox.y, zom.bbox.width, zom.bbox.height);

            RayConfiguration<Vector2> rayConfiguration = new CentralRayWithWhiskersConfig(zom, 30, 12, 40);
            RaycastCollisionDetector<Vector2> raycastCollisionDetector = new JBumpRaycastCollisionDetector(world);
            RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<>(zom, rayConfiguration, raycastCollisionDetector, 0);
            zom.addBehavior(raycastObstacleAvoidance);

            Seek<Vector2> seek = new Seek<>(zom, player);
            zom.addBehavior(seek);
        }
    }

    public void update(float delta) {
        if (shooting && Utils.secondsSince(shootStartTime) > 1 / player.getGun().getFireRate()) {
            shootStartTime = TimeUtils.nanoTime();
            shoot();
        }

        for (Zom1 zom: enemyEntities) {
            zom.update(delta, world);
        }
        for (Enemy enemy: characterEntities) {
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
        for (Entity tile: tiles) {
            tile.render(batch);
        }
        for (Entity entity: wallEntities) {
            entity.render(batch);
        }
        for (Zom1 zom: enemyEntities) {
            zom.renderSecondary(batch);
        }
        for (Entity entity: characterEntities) {
            entity.renderSecondary(batch);
        }
        player.renderSecondary(batch);
        for (Entity entity: environmentEntities) {
            entity.render(batch);
        }
        for (Zom1 zom: enemyEntities) {
            zom.render(batch);
        }
        for (Entity entity: characterEntities) {
            entity.render(batch);
        }

        player.render(batch, drawer);

        for (Bullet bullet: bullets) {
            bullet.render(batch);
        }
        for (Entity entity: wallEntities) {
            entity.renderSecondary(batch);
        }
        for (Entity entity: canopyEntities) {
            entity.render(batch);
        }
    }

    public void renderDebug(ShapeDrawer drawer) {
        for (Entity tile: tiles) {
            tile.renderDebug(drawer);
        }
        for (Entity entity: environmentEntities) {
            entity.renderDebug(drawer);

            if (entity.bulletCollisionSubObject != null) {
                entity.bulletCollisionSubObject.renderDebug(drawer);
            }
        }
        for (Zom1 zom: enemyEntities) {
            zom.renderDebug(drawer);
        }
        for (Entity entity: characterEntities) {
            entity.renderDebug(drawer);
        }
        for (Entity entity: wallEntities) {
            entity.renderDebug(drawer);

            if (entity.bulletCollisionSubObject != null) {
                entity.bulletCollisionSubObject.renderDebug(drawer);
            }
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
                        characterEntities.removeValue((Enemy) item.userData, true);
                    } else if (item.userData instanceof Zom1) {
                        world.remove(item);
                        enemyEntities.removeValue((Zom1) item.userData, true);
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
