package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;

public class Enemy extends Entity implements Steerable<Vector2> {

    private Vector2 acceleration;

    private boolean tagged;
    private float zeroLinearSpeedThreshold;
    private float maxLinearSpeed;
    private float maxLinearAcceleration;
    private float maxAngularSpeed;
    private float maxAngularAcceleration;
    private SteeringBehavior<Vector2> behavior;
    private SteeringAcceleration<Vector2> steerOutput;

    public Enemy(float x, float y) {
        super();
        position = new Vector2(x, y);
        bbox = new Rectangle(position.x + Constants.PLAYER_CENTER.x * 1.25f, position.y + Constants.PLAYER_CENTER.y * 1.25f, Constants.PLAYER_CENTER.x / 2, Constants.PLAYER_CENTER.y / 2);
        item = new Item<>(this);
        textureRegion = Assets.get_instance().enemyAssets.enemy;
        destructible = true;
        health = 200;
        acceleration = new Vector2(0, 0);
        steerOutput = new SteeringAcceleration<Vector2>(new Vector2());

        maxLinearSpeed = 100f;
        maxLinearAcceleration = 5000f;
        maxAngularSpeed = 10f;
        maxAngularAcceleration = 5f;
        tagged = false;
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior) {
        this.behavior = behavior;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBBox();
    }

    public void update(float delta, World<Entity> world) {
        if (behavior != null) {
            behavior.calculateSteering(steerOutput);
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
    }

    private void applySteering(float delta) {
        boolean anyAcclerations = false;

        if (!steerOutput.linear.isZero()) {
            acceleration = steerOutput.linear.scl(delta);
            Gdx.app.log("acceleration", acceleration.toString());
            velocity.x += acceleration.x;
            velocity.y += acceleration.y;
            Gdx.app.log("velocity", velocity.toString());

            position.mulAdd(velocity, delta);
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
            if (other.userData instanceof Crate) return Response.slide;
            if (other.userData instanceof FloorTile) return Response.slide;
            if (other.userData instanceof Player) return Response.slide;
            else return null;
        }
    }

    @Override
    public Vector2 getLinearVelocity() {
        return velocity;
    }

    @Override
    public float getAngularVelocity() {
        return 0;
    }

    @Override
    public float getBoundingRadius() {
        return Constants.PLAYER_CENTER.x / 2;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        zeroLinearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return rotation;
    }

    @Override
    public void setOrientation(float orientation) {
        rotation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return 90;
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return new Vector2(1, 0);
    }

    @Override
    public Location<Vector2> newLocation() {
        return this;
    }
}
