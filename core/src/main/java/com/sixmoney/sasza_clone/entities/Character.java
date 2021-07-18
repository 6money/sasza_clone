package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Constants;

public abstract class Character extends Entity implements Steerable<Vector2> {
    private static final String TAG = Character.class.getName();

    protected boolean tagged;
    protected float zeroLinearSpeedThreshold;
    protected float maxLinearSpeed;
    protected float maxLinearAcceleration;
    protected float maxAngularSpeed;
    protected float maxAngularAcceleration;
    protected SteeringBehavior<Vector2> behavior;
    protected SteeringAcceleration<Vector2> steerOutput;
    protected PrioritySteering<Vector2> prioritySteering;

    public Character() {
        super();
        steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
        maxLinearSpeed = 120;
        maxLinearAcceleration = 5000f;
        maxAngularSpeed = 10f;
        maxAngularAcceleration = 5f;
        tagged = false;
        prioritySteering = new PrioritySteering<>(this, 0.0001f);
    }

    protected void applySteering(float delta) {
        if (!steerOutput.linear.isZero()) {
            acceleration = steerOutput.linear.scl(delta);
            if (acceleration.len() > maxLinearAcceleration) {
                acceleration.setLength(maxLinearAcceleration);
            }
            velocity.x += acceleration.x;
            velocity.y += acceleration.y;
            if (velocity.len() > maxLinearSpeed) {
                velocity.setLength(maxLinearSpeed);
            }
            position.mulAdd(velocity, delta);
        }

        if (!velocity.isZero()) {
            rotation = velocity.angleDeg() + 90;
        }
    }

    public void addBehavior(SteeringBehavior<Vector2> behavior) {
        prioritySteering.add(behavior);
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
        return new Vector2(bbox.x + bbox.width / 2, bbox.y + bbox.height / 2);
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
        return vector.angleDeg();
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.setAngleDeg(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return this;
    }
}
