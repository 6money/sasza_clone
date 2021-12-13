package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.dongbat.walkable.PathHelper;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.GunData;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class BaseSoldier extends BaseNPC {
    private static final String TAG = BaseSoldier.class.getName();

    public NPCDetectionObject detectionObject;
    public Vector2 targetLocation;


    public BaseSoldier(float x, float y) {
        super(x, y);
        position = new Vector2(x, y);
        entityTextureRegion = Assets.get_instance().npcAssets.sniperBase;
        destructible = true;
        health = 1000;
        maxHealth = 1000;
        entityAnimation = Assets.get_instance().npcAssets.npcWalkingAnimationS1;
        deathAnimation = Assets.get_instance().npcAssets.sniperDyingAnimation;
        characterShootingTexture = Assets.get_instance().npcAssets.sniperShooting;
        characterIdleLegTexture = Assets.get_instance().npcAssets.npcStandS1;
        bulletCollidable = false;
        bulletOffset = new Vector2( 19, -3);
        detectionObject = new NPCDetectionObject(this);
        prioritySteering.setEnabled(false);
        pathSteering.setEnabled(false);
        npcCollisionFilter = new NpcCollisionFilter();
        npcPlayerCollisionFilter = new NpcPlayerCollisionFilter();
        guns.set(0, new Gun(GunData.svd));
        setGun(0);
    }


    public void update(float delta, World<Entity> world, PathHelper pathHelper, Vector2 target) {
        super.update(delta, world, pathHelper, target);

        Response.Result result = world.move(item, bbox.x, bbox.y, npcCollisionFilter);
        Collisions projectedCollisions = result.projectedCollisions;
        for (int i = 0; i < projectedCollisions.size(); i++) {
            Collision collision = projectedCollisions.get(i);
            if (collision.type == Response.slide) {
                setPosition(collision.touch.x - (bbox.x - position.x), collision.touch.y - (bbox.y - position.y));
                break;
            }
        }

        updateBBox();

        if (velocity.len() < 5) {
            animationStartTime = 0;
        } else if (velocity.len() >= 5 && animationStartTime == 0) {
            animationStartTime = TimeUtils.nanoTime();
        }

        if (updateCounter == updateBracket) {
            targetLocation = detectionObject.update(world);
        }

        if (targetLocation != null) {
            shooting = true;
            rotation = new Vector2(targetLocation).sub(getPosition()).angleDeg();
        } else {
            shooting = false;
        }
    }

    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);
        drawer.rectangle(detectionObject.bbox, Color.ORANGE);

        if (pathSteering.isEnabled()) {
            LinePath<Vector2> path = (LinePath<Vector2>) ((FollowPath<Vector2, LinePath.LinePathParam>) steeringBehaviors.get(2)).getPath();

            for (LinePath.Segment<Vector2> segment : path.getSegments()) {
                drawer.line(segment.getBegin().x, segment.getBegin().y, segment.getEnd().x, segment.getEnd().y, Color.GOLD);
            }
        }
    }


    public static class NpcCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(other == null) return null;
            if (((Entity) other.userData).characterCollidable) {
                return Response.slide;
            } else {
                return null;
            }
        }
    }

    public static class NpcPlayerCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(item == null) return null;
            else if (item.userData instanceof BaseEnemy) return null;
            else if (item.userData instanceof BaseSoldier) return null;
            if (((Entity) item.userData).characterCollidable || ((Entity) item.userData).bulletCollidable) {
                return Response.touch;
            } else {
                return null;
            }
        }
    }
}
