package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;

import java.util.ArrayList;

public class Bullet extends Entity {
    private static final String TAG = Bullet.class.getName();

    private Vector2 target;
    private float speed;
    private float damage;
    private float remainingDistance;
    private boolean dead;
    private boolean firstUpdate;
    private ArrayList<Item> items;
    private BulletCollisionFilter bulletCollisionFilter;

    public Bullet(float x, float y, float rotation, float targetX, float targetY, float speed, float damage) {
        super();
        position = new Vector2(x, y);
        bbox.set(x, y, 2, 2);
        this.rotation = rotation;
        target = new Vector2(targetX, targetY);
        entityTextureRegion = Assets.get_instance().playerAssets.rifleProjectile;
        this.speed = speed;
        this.damage = damage;
        dead = false;
        firstUpdate = true;
        items = new ArrayList<>();
        bulletCollisionFilter = new BulletCollisionFilter();
    }

    public boolean getDead() {
        return dead;
    }

    public void update(float delta, World<Entity> world) {
        if (firstUpdate) {
            firstUpdate = false;
            return;
        }

        Vector2 temp = new Vector2(target);
        temp.sub(position);
        remainingDistance = temp.len();
        temp.nor();

        if (remainingDistance == 0) {
            dead = true;
            return;
        }

        temp.setLength(Math.min(remainingDistance, speed * delta));
        position.add(temp);
        bbox.x = position.x;
        bbox.y = position.y;

        Response.Result result = world.move(item, bbox.x, bbox.y, bulletCollisionFilter);

        Collisions collisions = result.projectedCollisions;

        if (collisions.size() > 0) {
            Item item = collisions.get(0).other;

            Gdx.app.log(TAG, item.userData.getClass().getSimpleName());

            if (((Entity) item.userData).destructible) {
                ((Entity) item.userData).decrementHealth(damage);
            }
            dead = true;
            return;
        }
    }

    public static class BulletCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if ((other.userData instanceof FloorTile)) return null;
            if ((other.userData instanceof Canopy)) return null;
            if ((other.userData instanceof NPCDetectionObject)) return null;
            if ((other.userData instanceof BaseNPC)) return null;
            if ((other.userData instanceof Player)) return null;
            else return Response.touch;
        }
    }
}
