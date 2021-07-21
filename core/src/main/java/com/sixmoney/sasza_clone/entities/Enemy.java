package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;

public class Enemy extends Character {
    private static final String TAG = Enemy.class.getName();

    public Enemy(float x, float y) {
        super();
        position = new Vector2(x, y);
        bbox = new Rectangle(position.x + Constants.PLAYER_CENTER.x * 1.25f, position.y + Constants.PLAYER_CENTER.y * 1.25f, Constants.PLAYER_CENTER.x / 2, Constants.PLAYER_CENTER.y / 2);
        item = new Item<>(this);
        enitiyTextureRegion = Assets.get_instance().enemyAssets.enemy;
        destructible = true;
        health = 200;
        entityAnimation = Assets.get_instance().enemyAssets.enemyWalkingAnimation;
        characterIdleLegTexture = Assets.get_instance().enemyAssets.enemyStand;
    }


    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBBox();
    }

    public void update(float delta, World<Entity> world) {
        if (prioritySteering.isEnabled()) {
            prioritySteering.calculateSteering(steerOutput);
            applySteering(delta);
        }

        Response.Result result = world.move(item, bbox.x, bbox.y, new Enemy.EnemyCollisionFilter());
        Collisions projectedCollisions = result.projectedCollisions;
        for (int i = 0; i < projectedCollisions.size(); i++) {
            Collision collision = projectedCollisions.get(i);
            if (collision.type == Response.slide) {
                setPosition(collision.touch.x - (bbox.x - position.x), collision.touch.y - (bbox.y - position.y));
            }
        }

        updateBBox();

        Gdx.app.log(TAG, velocity.len() + "");
        if (velocity.len() < 5) {
            animationStartTime = 0;
        } else if (velocity.len() >= 5 && animationStartTime == 0) {
            animationStartTime = TimeUtils.nanoTime();
        }
    }

    private void updateBBox() {
        bbox.x = position.x + Constants.PLAYER_CENTER.x * 1.25f;
        bbox.y = position.y + Constants.PLAYER_CENTER.y * 1.25f;
    }

    public static class EnemyCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(other == null) return null;
            else if (other.userData instanceof Crate) return Response.slide;
            else if (other.userData instanceof FloorTile) return Response.slide;
            else if (other.userData instanceof Player) return Response.slide;
            else if (other.userData instanceof Enemy) return Response.slide;
            else return null;
        }
    }


}
