package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class BaseNPC extends Character {
    private static final String TAG = BaseNPC.class.getName();

    public NPCDetectionObject detectionObject;
    public Vector2 targetLocation;
    public boolean shooting;

    public BaseNPC(float x, float y) {
        super(x, y);
        position = new Vector2(x, y);
        entityTextureRegion = Assets.get_instance().npcAssets.sniperBase;
        destructible = true;
        health = 200;
        entityAnimation = Assets.get_instance().npcAssets.sniperWalkingAnimation;
        deathAnimation = Assets.get_instance().npcAssets.sniperDyingAnimation;
        characterIdleLegTexture = Assets.get_instance().npcAssets.sniperStand;
        detectionObject = new NPCDetectionObject(this);
        shooting = false;
    }


    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBBox();
    }


    @Override
    public void update(float delta, World<Entity> world) {
        if (prioritySteering.isEnabled()) {
            prioritySteering.calculateSteering(steerOutput);
            applySteering(delta);
            if (!velocity.isZero() && !shooting) {
                rotation = velocity.angleDeg() + 90;
            }
        }

        Response.Result result = world.move(item, bbox.x, bbox.y, new BaseNPC.EnemyCollisionFilter());
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

        super.update(delta, world);

        targetLocation = detectionObject.update(world);

        if (targetLocation != null) {
            shooting = true;
            rotation = new Vector2(targetLocation).sub(getPosition()).angleDeg() + 90;
        } else {
            shooting = false;
        }
    }

    @Override
    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);
        drawer.rectangle(detectionObject.bbox, Color.ORANGE);
    }


    public static class EnemyCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(other == null) return null;
            else if (other.userData instanceof EnvironmentObject) return Response.slide;
            else if (other.userData instanceof FloorTile) return Response.slide;
            else if (other.userData instanceof Character) return Response.slide;
            else if (other.userData instanceof Wall) return Response.slide;
            else return null;
        }
    }
}
