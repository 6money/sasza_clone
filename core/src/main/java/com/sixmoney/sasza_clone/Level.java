package com.sixmoney.sasza_clone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.dongbat.walkable.FloatArray;
import com.dongbat.walkable.PathHelper;
import com.dongbat.walkable.PathfinderException;
import com.sixmoney.sasza_clone.entities.BaseEnemy;
import com.sixmoney.sasza_clone.entities.BaseNPC;
import com.sixmoney.sasza_clone.entities.Bullet;
import com.sixmoney.sasza_clone.entities.BulletCollisionSubObject;
import com.sixmoney.sasza_clone.entities.Character;
import com.sixmoney.sasza_clone.entities.DeadEntity;
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

import hxDaedalus.data.Obstacle;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Level {
    private static final String TAG = Level.class.getName();
    private World<Entity> world;
    private Viewport viewport;
    private ChaseCam camera;
    private Player player;

    private Array<Entity> tiles;
    private DelayedRemovalArray<EnvironmentObject> environmentEntities;
    private Array<Entity> canopyEntities;
    private Array<Entity> wallEntities;
    private DelayedRemovalArray<BaseNPC> characterEntities;
    private DelayedRemovalArray<BaseEnemy> enemyEntities;
    private Array<Entity> deadEntities;
    private final DelayedRemovalArray<Bullet> bullets;

    public PathHelper pathHelper;
    public FloatArray path;
    public Vector2 clickedCoordinate;
    public boolean showPath;

    public Level(Viewport viewport, ChaseCam camera) {
        this.viewport = viewport;
        this.camera = camera;
        world = new World<>();
        world.setTileMode(true);
        path = new FloatArray();
        showPath = false;
        clickedCoordinate = new Vector2(0,0);

        tiles = new Array<>();
        environmentEntities = new DelayedRemovalArray<>();
        canopyEntities = new Array<>();
        characterEntities = new DelayedRemovalArray<>();
        enemyEntities = new DelayedRemovalArray<>();
        deadEntities = new Array<>();
        bullets = new DelayedRemovalArray<>();
    }

    public void initPathHelper(Vector2 outerCorner) {
        pathHelper = new PathHelper(outerCorner.x, outerCorner.y);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        world.add(player.item, player.bbox.x, player.bbox.y, player.bbox.width, player.bbox.height);
        camera.setPlayer(player);
    }

    public void setTiles(Array<Entity> tiles) {
        this.tiles = tiles;
        for (Entity tile: tiles) {
            if (tile.characterCollidable) {
                world.add(tile.item, tile.bbox.x, tile.bbox.y, tile.bbox.width, tile.bbox.height);
                tile.pathObstacle = pathHelper.addRect(tile.bbox.x, tile.bbox.y, tile.bbox.width, tile.bbox.height);
            }
        }
    }

    public void setEnvironmentEntities(Array<EnvironmentObject> entities) {
        this.environmentEntities = new DelayedRemovalArray<>(entities);
        for (EnvironmentObject environmentObject: environmentEntities) {
            world.add(environmentObject.item, environmentObject.bbox.x, environmentObject.bbox.y, environmentObject.bbox.width, environmentObject.bbox.height);
            world.add(
                    environmentObject.bulletCollisionSubObject.item,
                    environmentObject.bulletCollisionSubObject.bbox.x,
                    environmentObject.bulletCollisionSubObject.bbox.y,
                    environmentObject.bulletCollisionSubObject.bbox.width,
                    environmentObject.bulletCollisionSubObject.bbox.height
            );

            if (environmentObject.characterCollidable && !environmentObject.destructible) {
                environmentObject.pathObstacle = pathHelper.addRect(environmentObject.bbox.x, environmentObject.bbox.y, environmentObject.bbox.width, environmentObject.bbox.height);
            }

            for (EnvironmentObject compositeObject: environmentObject.compositeObjects) {
                world.add(compositeObject.item, compositeObject.bbox.x, compositeObject.bbox.y, compositeObject.bbox.width, compositeObject.bbox.height);
                world.add(
                        compositeObject.bulletCollisionSubObject.item,
                        compositeObject.bulletCollisionSubObject.bbox.x,
                        compositeObject.bulletCollisionSubObject.bbox.y,
                        compositeObject.bulletCollisionSubObject.bbox.width,
                        compositeObject.bulletCollisionSubObject.bbox.height
                );

                if (compositeObject.characterCollidable && !compositeObject.destructible) {
                    compositeObject.pathObstacle = pathHelper.addRect(compositeObject.bbox.x, compositeObject.bbox.y, compositeObject.bbox.width, compositeObject.bbox.height);
                }
            }
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
            if (entity.characterCollidable) {
                world.add(entity.item, entity.bbox.x, entity.bbox.y, entity.bbox.width, entity.bbox.height);
                world.add(entity.bulletCollisionSubObject.item, entity.bulletCollisionSubObject.bbox.x, entity.bulletCollisionSubObject.bbox.y, entity.bulletCollisionSubObject.bbox.width, entity.bulletCollisionSubObject.bbox.height);
                entity.pathObstacle = pathHelper.addRect(entity.bbox.x, entity.bbox.y, entity.bbox.width, entity.bbox.height);
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
            baseNPC.addBehavior(raycastObstacleAvoidance, 0);

            Arrive<Vector2> arrive = new Arrive<>(baseNPC, player)
                    .setTimeToTarget(0.1f)
                    .setArrivalTolerance(50f)
                    .setDecelerationRadius(50);
            baseNPC.addBehavior(arrive, 1);
        }
    }


    public void setEnemyEntities(Array<BaseEnemy> entities) {
        enemyEntities.addAll(entities);
        for (BaseEnemy enemy: enemyEntities) {
            world.add(enemy.item, enemy.bbox.x, enemy.bbox.y, enemy.bbox.width, enemy.bbox.height);

            RayConfiguration<Vector2> rayConfiguration = new CentralRayWithWhiskersConfig(enemy, 30, 12, 40);
            RaycastCollisionDetector<Vector2> raycastCollisionDetector = new JBumpRaycastCollisionDetector(world);
            RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<>(enemy, rayConfiguration, raycastCollisionDetector, 0);
            enemy.addBehavior(raycastObstacleAvoidance, 0);

            Seek<Vector2> seek = new Seek<>(enemy, player);
            enemy.addBehavior(seek, 1);

            Array<Vector2> waypoints = new Array<>();
            waypoints.add(new Vector2(0, 0));
            waypoints.add(new Vector2(0, 0));
            Path<Vector2, LinePath.LinePathParam> path = new LinePath<>(waypoints, true);
            FollowPath<Vector2, LinePath.LinePathParam> followPath = new FollowPath<>(enemy, path, 5);
            enemy.addBehavior(followPath, 2);
        }
    }

    public void update(float delta) {
        player.update(delta, world);

        if (player.shooting && Utils.secondsSince(player.shootStartTime) > 1 / player.getGun().getFireRate()) {
            player.shootStartTime = TimeUtils.nanoTime();
            shoot(player);
        }

        environmentEntities.begin();
        for (EnvironmentObject entity: environmentEntities) {
            if (entity.health <= 0) {
                world.remove(entity.item);
                if (entity.pathObstacle != null) {
                    pathHelper.removeObstacle(entity.pathObstacle);
                }
                if (entity.bulletCollisionSubObject != null) {
                    world.remove(entity.bulletCollisionSubObject.item);
                }
                environmentEntities.removeValue(entity, true);
            }
        }
        environmentEntities.end();
        enemyEntities.begin();
        for (BaseEnemy enemy: enemyEntities) {
            enemy.update(delta, world, pathHelper, player.getPosition());
            if (enemy.getHealth() <= 0) {
                world.remove(enemy.item);
                enemyEntities.removeValue(enemy, true);
                deadEntities.add(new DeadEntity(enemy.position.x, enemy.position.y, enemy.rotation, enemy.deathAnimation));
                if (deadEntities.size > Constants.MAX_DEAD_SPRITES) {
                    deadEntities.removeIndex(0);
                }
            }
        }
        enemyEntities.end();
        characterEntities.begin();
        for (BaseNPC baseNPC : characterEntities) {
            baseNPC.update(delta, world, pathHelper, player.getPosition());
            if (baseNPC.getHealth() <= 0) {
                world.remove(baseNPC.item);
                world.remove(baseNPC.detectionObject.item);
                characterEntities.removeValue(baseNPC, true);
                deadEntities.add(new DeadEntity(baseNPC.position.x, baseNPC.position.y, baseNPC.rotation, baseNPC.deathAnimation));
                if (deadEntities.size > Constants.MAX_DEAD_SPRITES) {
                    deadEntities.removeIndex(0);
                }
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
            bullet.update(delta, world);
            if (bullet.getDead()) {
                world.remove(bullet.item);
                bullets.removeValue(bullet, true);
            }
        }
        bullets.end();
    }

    public void render(Batch batch, ShapeDrawer drawer) {
        for (Entity tile: tiles) {
            tile.render(batch);
        }
        for (Entity entity: deadEntities) {
            entity.render(batch);
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
        for (BaseEnemy zom: enemyEntities) {
            zom.renderHealthBar(batch);
        }
        for (Character entity: characterEntities) {
            entity.renderHealthBar(batch);
        }
        for (Entity entity: wallEntities) {
            entity.renderSecondary(batch);
        }
        for (Entity entity: environmentEntities) {
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
        for (Entity entity: bullets) {
            entity.renderDebug(drawer);
        }
        player.renderDebug(drawer);

        if (showPath) {

            for (Obstacle obstacle: pathHelper.obstacles) {
                haxe.root.Array<Object> coords = obstacle._coordinates;
                Rectangle rectangle = new Rectangle((float) obstacle.get_x(), (float) obstacle.get_y(), ((float) coords.__get(2)), ((float) coords.__get(7)));
                drawer.rectangle(rectangle);
            }

            float pastNodeX = player.getPosition().x;
            float pastNodeY = player.getPosition().y;

            for (int i = 0; i < path.size; i += 2) {
                drawer.line(pastNodeX, pastNodeY, path.get(i), path.get(i + 1));
                pastNodeX = path.get(i);
                pastNodeY = path.get(i + 1);
            }
        }
    }

    public void genPath(Vector2 clickedCoords) {
        clickedCoordinate = clickedCoords;

        try {
            pathHelper.findPath(player.getPosition().x, player.getPosition().y, clickedCoordinate.x, clickedCoordinate.y, getPlayer().bbox.width * 1.1f, path);
        } catch (PathfinderException ignored) {
            Gdx.app.debug(TAG, "Pathfinding error");
        }
    }

    public void shoot(Character character) {
        if (character.getGun().getCurrentAmmo() == 0 && character.getGun().getCurrentMagazineAmmo() == 0) {
            return;
        }

        if (character.getGun().getCurrentMagazineAmmo() <= 0) {
            character.getGun().reload();
        }

        float rotation = character.rotation;
        Vector2 bulletVector = new Vector2(1, 0);
        bulletVector.rotateDeg(rotation);
        bulletVector.setLength(character.getGun().getRange());
        bulletVector.add(character.position.x + Constants.PLAYER_CENTER.x + character.bulletOffsetReal.x, character.position.y + Constants.PLAYER_CENTER.y + character.bulletOffsetReal.y);

        Bullet bullet = new Bullet(
                character.position.x + Constants.PLAYER_CENTER.x + character.bulletOffsetReal.x,
                character.position.y + Constants.PLAYER_CENTER.y + character.bulletOffsetReal.y,
                character.rotation,
                bulletVector.x,
                bulletVector.y,
                character.getGun().getProjectileSpeed(),
                character.getGun().getDamage(),
                character.getGun().getWeaponType()
        );

        bullets.add(bullet);
        world.add(bullet.item, bullet.bbox.x, bullet.bbox.y, bullet.bbox.width, bullet.bbox.height);
        character.getGun().decrementCurrentMagazineAmmo();
        character.triggerMuzzleFlash();
    }


    public void teleportPlayer(float x, float y) {
        x -= Constants.PLAYER_CENTER.x;
        y -= Constants.PLAYER_CENTER.y;
        world.update(player.item, x, y);
        player.setPosition(x, y);
        player.update(0, world);
    }


    public void spawnEnemy(float quantity) throws ReflectionException {
        spawnEnemy(quantity, "BaseEnemy");
    }


    public void spawnEnemy(float quantity, String type) throws ReflectionException {
        int randClose = 100;
        int randFar = 400;

        Class enemyClass = ClassReflection.forName("com.sixmoney.sasza_clone.entities." + type);
        Constructor constructor = ClassReflection.getConstructor(enemyClass, float.class, float.class);

        for (int i = 0; i < quantity; i++) {
            float x;
            float y;
            if (MathUtils.random(0, 1) == 1) {
                x = player.getPosition().x + MathUtils.random(randClose, randFar);
            } else {
                x = player.getPosition().x - MathUtils.random(randClose, randFar);
            }
            if (MathUtils.random(0, 1) == 1) {
                y = player.getPosition().y + MathUtils.random(randClose, randFar);
            } else {
                y = player.getPosition().y - MathUtils.random(randClose, randFar);
            }

            BaseEnemy enemy = (BaseEnemy) constructor.newInstance(x, y);

            world.add(enemy.item, enemy.bbox.x, enemy.bbox.y, enemy.bbox.width, enemy.bbox.height);

            RayConfiguration<Vector2> rayConfiguration = new CentralRayWithWhiskersConfig(enemy, 30, 12, 40);
            RaycastCollisionDetector<Vector2> raycastCollisionDetector = new JBumpRaycastCollisionDetector(world);
            RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<>(enemy, rayConfiguration, raycastCollisionDetector, 0);
            enemy.addBehavior(raycastObstacleAvoidance, 0);

            Seek<Vector2> seek = new Seek<>(enemy, player);
            enemy.addBehavior(seek, 1);

            Array<Vector2> waypoints = new Array<>();
            waypoints.add(new Vector2(0, 0));
            waypoints.add(new Vector2(0, 0));
            LinePath<Vector2> path = new LinePath<>(waypoints, true);
            FollowPath<Vector2, LinePath.LinePathParam> followPath = new FollowPath<>(enemy, path, 5);
            enemy.addBehavior(followPath, 2);

            enemyEntities.add(enemy);
        }
    }

    public void spawnNPC(float quantity) {
        int randClose = 100;
        int randFar = 400;

        for (int i = 0; i < quantity; i++) {
            float x;
            float y;
            if (MathUtils.random(0, 1) == 1) {
                x = player.getPosition().x + MathUtils.random(randClose, randFar);
            } else {
                x = player.getPosition().x - MathUtils.random(randClose, randFar);
            }
            if (MathUtils.random(0, 1) == 1) {
                y = player.getPosition().y + MathUtils.random(randClose, randFar);
            } else {
                y = player.getPosition().y - MathUtils.random(randClose, randFar);
            }

            BaseNPC npc = new BaseNPC(x, y);

            world.add(npc.item, npc.bbox.x, npc.bbox.y, npc.bbox.width, npc.bbox.height);
            world.add(npc.detectionObject.item, npc.detectionObject.bbox.x, npc.detectionObject.bbox.y, npc.detectionObject.bbox.width, npc.detectionObject.bbox.height);

            RayConfiguration<Vector2> rayConfiguration = new CentralRayWithWhiskersConfig(npc, 30, 12, 40);
            RaycastCollisionDetector<Vector2> raycastCollisionDetector = new JBumpRaycastCollisionDetector(world);
            RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance = new RaycastObstacleAvoidance<>(npc, rayConfiguration, raycastCollisionDetector, 0);
            npc.addBehavior(raycastObstacleAvoidance, 0);

            Arrive<Vector2> arrive = new Arrive<>(npc, player)
                    .setTimeToTarget(0.1f)
                    .setArrivalTolerance(50f)
                    .setDecelerationRadius(50);
            npc.addBehavior(arrive, 1);

            characterEntities.add(npc);
        }
    }


    public Entity queryPoint(Vector2 pointCoords) {
        ArrayList<Item> items = new ArrayList<>();
        world.queryPoint(pointCoords.x, pointCoords.y, new QueryCollisionFilter(), items);

        if (items.size() > 0) {
            return (Entity) items.get(0).userData;
        }

        return null;
    }


    public static class QueryCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if ((item.userData instanceof NPCDetectionObject)) return null;
            if ((item.userData instanceof BulletCollisionSubObject)) return null;
            if ((item.userData instanceof FloorTile)) return null;
            if ((item.userData instanceof Entity)) return Response.touch;
            else return null;
        }
    }
}
