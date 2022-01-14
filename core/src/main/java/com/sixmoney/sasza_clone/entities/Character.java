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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.dongbat.jbump.ItemInfo;
import com.dongbat.jbump.World;
import com.ray3k.tenpatch.TenPatchDrawable;
import com.sixmoney.sasza_clone.ai.CentralRayWithWhiskersConfig;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.staticData.GunData;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Character extends Entity implements Steerable<Vector2> {
    private static final String TAG = Character.class.getName();

    protected boolean tagged;
    protected float zeroLinearSpeedThreshold;
    protected float maxLinearSpeed;
    protected float defaultMaxLinearSpeed;
    protected float maxLinearAcceleration;
    protected float maxAngularSpeed;
    protected float maxAngularAcceleration;
    protected float legsOffset;
    protected float legsRotation;
    protected RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidance;
    protected SteeringAcceleration<Vector2> steerOutput;
    protected PrioritySteering<Vector2> prioritySteering;
    protected PrioritySteering<Vector2> pathSteering;
    protected TextureRegion characterShootingTexture;
    protected TextureRegion characterIdleLegTexture;
    protected Vector2 oldVelocity;
    protected Vector2 bulletOffset;
    protected Gun currentGun;
    protected Array<Gun> guns;
    protected boolean muzzleFlash;
    protected boolean muzzleFlashReset;
    protected long muzzleFlashStartTime;
    protected boolean showHealthBar;
    protected TenPatchDrawable healthBar;
    protected TenPatchDrawable stunBar;
    protected FloatArray path;
    protected ArrayList<ItemInfo> items;
    protected float stunLimit;
    protected float stun;
    protected long stunTimer;
    protected long stunDecayTimer;

    public boolean shooting;
    public long shootStartTime;
    public long shootSpriteTime;
    public Animation<TextureRegion> deathAnimation;
    public Vector2 bulletOffsetReal;
    public Array<SteeringBehavior<Vector2>> steeringBehaviors;

    public Character(float x, float y) {
        super();
        bbox = new Rectangle(x + Constants.PLAYER_CENTER.x * 0.80f, y + Constants.PLAYER_CENTER.y * 0.80f, MathUtils.round(Constants.PLAYER_CENTER.x / 2.5f), MathUtils.round(Constants.PLAYER_CENTER.y / 2.5f));
        steerOutput = new SteeringAcceleration<>(new Vector2());
        maxLinearSpeed = 120;
        defaultMaxLinearSpeed = maxLinearSpeed;
        maxLinearAcceleration = 800f;
        maxAngularSpeed = 120f;
        maxAngularAcceleration = 200f;
        tagged = false;
        prioritySteering = new PrioritySteering<>(this, 0.0001f);
        pathSteering = new PrioritySteering<>(this, 0.0001f);
        oldVelocity = new Vector2(velocity);
        legsOffset = 0;
        legsRotation = 0;
        bulletOffset = new Vector2( 18, -3);
        bulletOffsetReal = new Vector2(bulletOffset);
        guns = new Array<>(true, 3);
        guns.add(new Gun(GunData.m4), new Gun(GunData.mp5), new Gun(GunData.pkm));
        setGun(0);
        shooting = false;
        shootStartTime = TimeUtils.nanoTime();
        shootSpriteTime = TimeUtils.nanoTime();
        muzzleFlash = false;
        muzzleFlashReset = false;
        muzzleFlashStartTime = 0;
        showHealthBar = true;
        healthBar = new TenPatchDrawable(new int[] {1, 1}, new int[] {1, 1}, false, Assets.get_instance().getPrivateAtlas().findRegion("health_bar"));
        stunBar = new TenPatchDrawable(new int[] {1, 1}, new int[] {1, 1}, false, Assets.get_instance().getPrivateAtlas().findRegion("stun_bar"));
        path = new FloatArray();
        items = new ArrayList<>();
        steeringBehaviors = new Array<>();
        stunLimit = 100;
        stun = 0;
        stunTimer = 0;
        stunDecayTimer = TimeUtils.nanoTime();
    }


    public Vector2 getBulletOffset() {
        return bulletOffset;
    }

    public Gun getGun() {
        return currentGun;
    }

    public void setGun(int index) {
        currentGun = guns.get(index);
        setMaxLinearSpeed(defaultMaxLinearSpeed);
    }

    public void setNewGun(String gunName) {
        Gun newGun = new Gun(GunData.gunRecords.get(gunName));
        int currentIndex = guns.indexOf(currentGun, true);
        guns.set(currentIndex, newGun);
        setGun(currentIndex);
    }

    public void triggerMuzzleFlash() {
        muzzleFlash = true;
        muzzleFlashReset = true;
        muzzleFlashStartTime = TimeUtils.nanoTime();
    }

    @Override
    public void update(float delta, World<Entity> world) {
        if (!velocity.isZero()) {
            legsRotation = velocity.angleDeg();
        }

        bulletOffsetReal.set(bulletOffset);
        bulletOffsetReal.rotateDeg(rotation);

        if (currentGun != null) {
            currentGun.checkReloadStatus();
        }

        if (stunTimer != 0 && Utils.secondsSince(stunTimer) >= 0.5f) {
            stunTimer = 0;
            stun = 0;
            maxLinearSpeed *= 2;
        }

        if (Utils.millisecondsSince(stunDecayTimer) >= 100 && stun > 0) {
            stunDecayTimer = TimeUtils.nanoTime();
            stun -= 2;
        }
    }

    protected void updateBBox() {
        bbox.x = position.x + Constants.PLAYER_CENTER.x * 0.80f;
        bbox.y = position.y + Constants.PLAYER_CENTER.y * 0.80f;
    }

    @Override
    public void render(Batch batch) {
        render(batch, 0, 0);
    }

    @Override
    public void render(Batch batch, float xOffset, float yOffset) {
        if (currentGun != null && characterShootingTexture != null && shooting && currentGun.getCurrentMagazineAmmo() > 0 && currentGun.checkReloadStatus() == 0) {
            if (Utils.secondsSince(shootSpriteTime) < Math.max(0.02, Math.min(0.1, 1 / currentGun.getFireRate()))) {
                batch.setColor(Math.max(0.6f, Constants.BACK_BUFFER_LIGHTING), Math.max(0.6f, Constants.BACK_BUFFER_LIGHTING), Math.max(0.6f, Constants.BACK_BUFFER_LIGHTING), 1);
                Utils.drawTextureRegion(batch, characterShootingTexture, position.x, position.y, rotation);
                batch.setColor(Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING, 1 );
            } else {
                super.render(batch, xOffset, yOffset);
            }

            float tempResetTime;

            if (currentGun.getFireRate() < 5) {
                tempResetTime = 1 / currentGun.getFireRate();
            } else {
                tempResetTime = 1 / currentGun.getFireRate() * 2;
            }

            if (Utils.secondsSince(shootSpriteTime) > tempResetTime) {
                shootSpriteTime = TimeUtils.nanoTime();
            }
        } else {
            super.render(batch, xOffset, yOffset);
        }

        float muzzleFlahsStartSeconds = Utils.secondsSince(muzzleFlashStartTime);

        if (muzzleFlash && !currentGun.getMuzzleFlashAnimation().isAnimationFinished(muzzleFlahsStartSeconds)) {
            currentGun.getMuzzleFlashOffsetReal().set(currentGun.getMuzzleFlashOffset());
            currentGun.getMuzzleFlashOffsetReal().set(currentGun.getMuzzleFlashOffsetReal().rotateDeg(rotation));
            batch.setColor(Math.max(0.8f, Constants.BACK_BUFFER_LIGHTING), Math.max(0.8f, Constants.BACK_BUFFER_LIGHTING), Math.max(0.8f, Constants.BACK_BUFFER_LIGHTING), 1);
            Utils.drawTextureRegion(
                    batch,
                    (TextureRegion) currentGun.getMuzzleFlashAnimation().getKeyFrame(muzzleFlahsStartSeconds),
                    position.x + Constants.PLAYER_CENTER.x + bulletOffsetReal.x - currentGun.getMuzzleFlashOffsetReal().x,
                    position.y + Constants.PLAYER_CENTER.y + bulletOffsetReal.y - currentGun.getMuzzleFlashOffsetReal().y,
                    rotation,
                    1,
                    0,
                    0
            );
            batch.setColor(Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING, Constants.BACK_BUFFER_LIGHTING, 1 );
        } else if (muzzleFlash && currentGun.getMuzzleFlashAnimation().isAnimationFinished(muzzleFlahsStartSeconds)) {
            muzzleFlashStartTime = 0;
            muzzleFlash = false;
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

    public void renderHealthBar(Batch batch) {
        if (health < maxHealth && showHealthBar) {
            float healthBarWidth = Constants.HEALTH_BAR_WIDTH * (health / maxHealth);
            healthBar.draw(batch, position.x + Constants.PLAYER_CENTER.x - (Constants.HEALTH_BAR_WIDTH / 2f), position.y + (Constants.PLAYER_CENTER.y * 1.5f), healthBarWidth, 3);
        }
    }

    public void renderStunBar(Batch batch) {
        if (stun != 0) {
            float stunBarWidth = Constants.HEALTH_BAR_WIDTH * (Math.min(stun, stunLimit) / stunLimit);
            stunBar.draw(batch, position.x + Constants.PLAYER_CENTER.x - (Constants.HEALTH_BAR_WIDTH / 2f), position.y + (Constants.PLAYER_CENTER.y * 1.5f) + 2, stunBarWidth, 3);
        }
    }

    @Override
    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);

        if (raycastObstacleAvoidance != null) {
            CentralRayWithWhiskersConfig config = (CentralRayWithWhiskersConfig) raycastObstacleAvoidance.getRayConfiguration();

            for (Ray<Vector2> ray : config.getRays()) {
                drawer.line(ray.start, ray.end, Color.CYAN);
            }
        }
    }

    public void addBehavior(SteeringBehavior<Vector2> behavior, int steeringIndex) {
        steeringBehaviors.add(behavior);

        if (steeringIndex == 0) {
            prioritySteering.add(behavior);
            pathSteering.add(behavior);
        } else if (steeringIndex == 1) {
            prioritySteering.add(behavior);
        } else if (steeringIndex == 2) {
            pathSteering.add(behavior);
        }

        if (behavior instanceof RaycastObstacleAvoidance) {
            raycastObstacleAvoidance = (RaycastObstacleAvoidance<Vector2>) behavior;
        }
    }

    public void incrementStun(float impact) {
        stun += impact;

        if (stun >= stunLimit) {
            if (stunTimer == 0) {
                maxLinearSpeed /= 2;
            }
            stunTimer = TimeUtils.nanoTime();
        }
    }

    @Override
    public String[] getData() {
        String[] data = super.getData();
        data[1] = data[1] + "\nstun current: " + stun;
        data[1] = data[1] + "\nstun limit: " + stunLimit;
        data[1] = data[1] + "\nmaxLinearSpeed: " + maxLinearSpeed;
        return data;
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
        defaultMaxLinearSpeed = maxLinearSpeed;
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
        return legsRotation;
    }

    @Override
    public void setOrientation(float orientation) {
        legsRotation = orientation;
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
