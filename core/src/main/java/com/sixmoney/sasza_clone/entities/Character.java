package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.CentralRayWithWhiskersConfig;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.Utils;

import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Character extends Entity implements Steerable<Vector2> {
    private static final String TAG = Character.class.getName();

    protected boolean tagged;
    protected float zeroLinearSpeedThreshold;
    protected float maxLinearSpeed;
    protected float maxLinearAcceleration;
    protected float maxAngularSpeed;
    protected float maxAngularAcceleration;
    protected float legsOffset;
    protected float legsRotation;
    protected RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance;
    protected SteeringAcceleration<Vector2> steerOutput;
    protected PrioritySteering<Vector2> prioritySteering;
    protected TextureRegion characterShootingTexture;
    protected TextureRegion characterIdleLegTexture;
    protected Vector2 oldVelocity;
    protected Vector2 bulletOffset;
    protected Gun gun;

    public boolean shooting;
    public long shootStartTime;
    public long shootSpriteTime;
    public Animation<TextureRegion> deathAnimation;

    public Character(float x, float y) {
        super();
        bbox = new Rectangle(x + Constants.PLAYER_CENTER.x * 0.80f, y + Constants.PLAYER_CENTER.y * 0.80f, MathUtils.round(Constants.PLAYER_CENTER.x / 2.5f), MathUtils.round(Constants.PLAYER_CENTER.y / 2.5f));
        steerOutput = new SteeringAcceleration<>(new Vector2());
        maxLinearSpeed = 120;
        maxLinearAcceleration = 200f;
        maxAngularSpeed = 120f;
        maxAngularAcceleration = 200f;
        tagged = false;
        prioritySteering = new PrioritySteering<>(this, 0.0001f);
        oldVelocity = new Vector2(velocity);
        legsOffset = 0;
        legsRotation = 0;
        bulletOffset = new Vector2( 18, -3);
        gun = new Gun();
        shooting = false;
        shootStartTime = TimeUtils.nanoTime();
        shootSpriteTime = TimeUtils.nanoTime();
    }


    public Vector2 getBulletOffset() {
        return bulletOffset;
    }

    public Gun getGun() {
        return gun;
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
        } else {
            velocity.set(0, 0);
        }
    }

    @Override
    public void update(float delta, World<Entity> world) {
        if (!velocity.isZero()) {
            legsRotation = velocity.angleDeg();
        }
    }

    protected void updateBBox() {
        bbox.x = position.x + Constants.PLAYER_CENTER.x * 0.80f;
        bbox.y = position.y + Constants.PLAYER_CENTER.y * 0.80f;
    }

    @Override
    public void render(Batch batch) {
        if (gun != null && characterShootingTexture != null && shooting && gun.getCurrentMagazineAmmo() > 0) {
            if (Utils.secondsSince(shootSpriteTime) < Math.max(0.02, Math.min(0.1, 1 / gun.getFireRate()))) {
                Utils.drawTextureRegion(batch, characterShootingTexture, position.x, position.y, rotation);
            } else {
                super.render(batch);
            }

            if (Utils.secondsSince(shootSpriteTime) > 1 / gun.getFireRate() * 2) {
                shootSpriteTime = TimeUtils.nanoTime();
            }
        } else {
            super.render(batch);
        }
    }

    @Override
    public void renderSecondary(Batch batch) {
        if (entityAnimation != null) {
            if (animationStartTime == 0) {
                Utils.drawTextureRegion(batch, characterIdleLegTexture, position.x - legsOffset, position.y - legsOffset, legsRotation);
            } else {
                float animationTime = Utils.secondsSince(animationStartTime);
                Utils.drawTextureRegion(batch, entityAnimation.getKeyFrame(animationTime), position.x - legsOffset, position.y - legsOffset, legsRotation);
            }
        }
    }

    @Override
    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);

        if (raycastObstacleAvoidance != null) {
            CentralRayWithWhiskersConfig config = (CentralRayWithWhiskersConfig) raycastObstacleAvoidance.getRayConfiguration();

            for (Ray<Vector2> ray : config.getRays())
                drawer.line(ray.start, ray.end, Color.CYAN);
        }
    }

    public void addBehavior(SteeringBehavior<Vector2> behavior) {
        prioritySteering.add(behavior);

        if (behavior instanceof RaycastObstacleAvoidance) {
            raycastObstacleAvoidance = (RaycastObstacleAvoidance<Vector2>) behavior;
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
        return Constants.PLAYER_CENTER.x;
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

    /*
    Returns the center point of the character, not the bottom left which is what the position
    attribute defines
    */
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
