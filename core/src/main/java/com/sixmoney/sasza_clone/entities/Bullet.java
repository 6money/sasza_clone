package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;
import com.sixmoney.sasza_clone.utils.Utils.WeaponCategory;

public class Bullet extends Entity {
    private static final String TAG = Bullet.class.getName();

    private Vector2 target;
    private float speed;
    private float damage;
    private float remainingDistance;
    private boolean dead;
    private boolean firstUpdate;
    private float impact;
    private BulletCollisionFilter bulletCollisionFilter;
    private int penetration;
    private int penetrationCount;
    private float critChance;
    private float critDamage;
    private Array<Entity> hitEntities;

    public Bullet(float x, float y, float rotation, float targetX, float targetY, float speed, float damage, WeaponCategory projectileType, float impact, int penetration, float critChange, float critDamage) {
        super();
        position = new Vector2(x, y);
        bbox.set(x, y, 2, 2);
        this.rotation = rotation;
        this.impact = impact;
        this.penetration = penetration;
        penetrationCount = 0;
        hitEntities = new Array<>();
        this.critChance = critChange;
        this.critDamage = critDamage;
        target = new Vector2(targetX, targetY);
        if (projectileType == WeaponCategory.DMR) {
            entityTextureRegion = Assets.get_instance().weaponAssets.dmrProjectile;
        } else if (projectileType == WeaponCategory.RIFLE) {
            entityTextureRegion = Assets.get_instance().weaponAssets.rifleProjectile;
        } else if (projectileType == WeaponCategory.LMG || projectileType == WeaponCategory.SPECIAL) {
            entityTextureRegion = Assets.get_instance().weaponAssets.lmgProjectile;
        } else if (projectileType == WeaponCategory.SMG) {
            entityTextureRegion = Assets.get_instance().weaponAssets.pistolProjectile;
        } else {
            entityTextureRegion = Assets.get_instance().weaponAssets.pistolProjectile;
        }

        this.speed = speed;
        this.damage = damage;
        dead = false;
        firstUpdate = true;
        bulletCollisionFilter = new BulletCollisionFilter();
        characterCollidable = false;
        bulletCollidable = false;
    }

    public boolean getDead() {
        return dead;
    }


    public void update(float delta, World<Entity> world, DelayedRemovalArray<Utils.HitRecord> hitLocations) {
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
            for (Item item: collisions.others) {
                if (!hitEntities.contains(((Entity) item.userData), true)) {
                    boolean isCrit = MathUtils.randomBoolean(critChance);
                    float realDamage = (!isCrit) ? damage : damage * critDamage;

                    if (((Entity) item.userData).destructible) {
                        ((Entity) item.userData).decrementHealth(realDamage);

                        if (item.userData instanceof BaseEnemy) {
                            ((BaseEnemy) item.userData).incrementStun(impact);
                            penetrationCount += 1;
                            hitEntities.add(((Entity) item.userData));
                            hitLocations.add(new Utils.HitRecord(collisions.get(collisions.others.indexOf(item)).touch.x, collisions.get(collisions.others.indexOf(item)).touch.y, realDamage, isCrit));
                        } else {
                            dead = true;
                            return;
                        }
                    } else if (((Entity) item.userData).bulletCollidable) {
                        dead = true;
                        return;
                    }

                    if (penetrationCount >= penetration) {
                        dead = true;
                        return;
                    }
                }
            }
        }
    }


    public static class BulletCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(other == null) return null;
            if (((Entity) other.userData).bulletCollidable) {
                if (other.userData instanceof Character) {
                    return Response.cross;
                } else {
                    return Response.touch;
                }
            } else {
                return null;
            }
        }
    }
}
