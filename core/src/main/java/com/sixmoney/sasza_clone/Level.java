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
import com.sixmoney.sasza_clone.entities.BaseEnemy;
import com.sixmoney.sasza_clone.entities.BaseNPC;
import com.sixmoney.sasza_clone.entities.Bullet;
import com.sixmoney.sasza_clone.entities.BulletCollisionSubObject;
import com.sixmoney.sasza_clone.entities.Canopy;
import com.sixmoney.sasza_clone.entities.Character;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.EnvironmentObject;
import com.sixmoney.sasza_clone.entities.FloorTile;
import com.sixmoney.sasza_clone.entities.NPCDetectionObject;
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

    private Array<Entity> tiles;
    private Array<Entity> environmentEntities;
    private Array<Entity> canopyEntities;
    private Array<Entity> wallEntities;
    private DelayedRemovalArray<BaseNPC> characterEntities;
    private Array<BaseEnemy> enemyEntities;
    private final DelayedRemovalArray<Bullet> bullets;
    private BulletCollisionFilter bulletCollisionFilter;

    public long shootStartTime;
    public boolean shooting;

    public Level(Viewport viewport) {
        this.viewport = viewport;

        world = new World<>();
        world.setTileMode(true);

        tiles = new Array<>();
        environmentEntities = new Array<>();
        canopyEntities = new Array<>();
        characterEntities = new DelayedRemovalArray<>();
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


    public void setCharacterEntities(Array<BaseNPC> entities) {
        this.characterEntities = new DelayedRemovalArray<>(entities);
        for (BaseNPC baseNPC : characterEntities) {
            world.add(baseNPC.item, baseNPC.bbox.x, baseNPC.bbox.y, baseNPC.bbox.width, baseNPC.bbox.height);
            world.add(baseNPC.detectionObject.item, baseNPC.detectionObject.bbox.x, baseNPC.detectionObject.bbox.y, baseNPC.detectionObject.bbox.width, baseNPC.detectionObject.bbox.height);

            RayConfiguration<Vector2> rayConfiguration = new CentralRayWithWhiskersConfig(baseNPC, 30, 12, 40);
            RaycastCollisionDetector<Vector2> raycastCollisionDetector = new JBumpRaycastCollisionDetector(world);
            RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<>(baseNPC, rayConfiguration, raycastCollisionDetector, 0);
            baseNPC.addBehavior(raycastObstacleAvoidance);

            Arrive<Vector2> arrive = new Arrive<>(baseNPC, player)
                    .setTimeToTarget(0.1f)
                    .setArrivalTolerance(50f)
                    .setDecelerationRadius(50);
            baseNPC.addBehavior(arrive);
        }
    }


    public void setEnemyEntities(Array<BaseEnemy> entities) {
        this.enemyEntities = entities;
        for (BaseEnemy zom: enemyEntities) {
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
        player.update(delta, world);

        if (shooting && Utils.secondsSince(shootStartTime) > 1 / player.getGun().getFireRate()) {
            shootStartTime = TimeUtils.nanoTime();
            shoot(player);
        }

        for (BaseEnemy zom: enemyEntities) {
            zom.update(delta, world);
        }
        characterEntities.begin();
        for (BaseNPC baseNPC : characterEntities) {
            baseNPC.update(delta, world);
            if (baseNPC.getHealth() <= 0) {
                world.remove(baseNPC.item);
                world.remove(baseNPC.detectionObject.item);
                characterEntities.removeValue(baseNPC, true);
            }
            if (baseNPC.shooting && Utils.secondsSince(baseNPC.shootStartTime) > 1 / baseNPC.getGun().getFireRate()) {
                baseNPC.shootStartTime = TimeUtils.nanoTime();
                shoot(baseNPC);
            }
        }
        characterEntities.end();
        for (Entity entity: canopyEntities) {
            entity.update(delta, world);
        }

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
        for (BaseEnemy zom: enemyEntities) {
            zom.renderSecondary(batch);
        }
        for (Entity entity: characterEntities) {
            entity.renderSecondary(batch);
        }
        player.renderSecondary(batch);
        for (Entity entity: environmentEntities) {
            entity.render(batch);
        }
        for (BaseEnemy zom: enemyEntities) {
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
        }
        for (BaseEnemy zom: enemyEntities) {
            zom.renderDebug(drawer);
        }
        for (Entity entity: characterEntities) {
            entity.renderDebug(drawer);
        }
        for (Entity entity: wallEntities) {
            entity.renderDebug(drawer);
        }
        for (Entity entity: canopyEntities) {
            entity.renderDebug(drawer);
        }
        player.renderDebug(drawer);
    }

    public void shoot(Character character) {
        if (character.getGun().getCurrentAmmo() == 0 && character.getGun().getCurrentMagazineAmmo() == 0) {
            return;
        }

        if (character.getGun().getCurrentMagazineAmmo() <= 0) {
            character.getGun().reload();
        }

        ArrayList<ItemInfo> items = new ArrayList<>();
        float rotation = character.rotation;
        Vector2 bulletOffsetTemp = new Vector2(character.getBulletOffset());
        bulletOffsetTemp.rotateDeg(rotation);
        Vector2 bulletVector = new Vector2(0, -1);
        bulletVector.rotateDeg(rotation);
        bulletVector.setLength(character.getGun().getRange());
        bulletVector.add(character.position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x, character.position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y);

        world.querySegmentWithCoords(
                character.position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x,
                character.position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y,
                bulletVector.x,
                bulletVector.y,
                bulletCollisionFilter,
                items
        );

        if (items.size() > 0) {
            bulletVector.set(items.get(0).x1, items.get(0).y1);

            Item item = items.get(0).item;

            if (((Entity) item.userData).destructible) {
                ((Entity) item.userData).decrementHealth(character.getGun().getDamage());
                if (((Entity) item.userData).getHealth() <= 0) {
                    if (item.userData instanceof BaseNPC) {
                        world.remove(item);
                        world.remove(((BaseNPC) item.userData).detectionObject.item);
                        characterEntities.removeValue((BaseNPC) item.userData, true);
                    } else if (item.userData instanceof BaseEnemy) {
                        world.remove(item);
                        enemyEntities.removeValue((BaseEnemy) item.userData, true);
                    } else if (item.userData instanceof EnvironmentObject) {
                        world.remove(item);
                        environmentEntities.removeValue((Entity) item.userData, true);
                        if (((EnvironmentObject) item.userData).bulletCollisionSubObject != null) {
                            world.remove(((EnvironmentObject) item.userData).bulletCollisionSubObject.item);
                        }
                    } else if (item.userData instanceof BulletCollisionSubObject) {
                        world.remove(item);
                        world.remove(((BulletCollisionSubObject) item.userData).parent.item);
                        environmentEntities.removeValue(((BulletCollisionSubObject) item.userData).parent, true);
                    }
                }
            }
        }

        bullets.add(new Bullet(character.position.x + Constants.PLAYER_CENTER.x + bulletOffsetTemp.x, character.position.y + Constants.PLAYER_CENTER.y + bulletOffsetTemp.y, character.rotation, bulletVector.x, bulletVector.y, character.getGun().getProjectileSpeed()));
        character.getGun().decrementCurrentMagazineAmmo();
    }


    public void teleportPlayer(float x, float y) {
        world.update(player.item, x, y);
        player.setPosition(x, y);
    }

    public static class BulletCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if ((item.userData instanceof FloorTile)) return null;
            if ((item.userData instanceof Canopy)) return null;
            if ((item.userData instanceof NPCDetectionObject)) return null;
            if ((item.userData instanceof BaseNPC)) return null;
            else return Response.touch;
        }
    }
}
