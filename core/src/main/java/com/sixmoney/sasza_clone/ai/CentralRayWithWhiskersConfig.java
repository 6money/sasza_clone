package com.sixmoney.sasza_clone.ai;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector2;

public class CentralRayWithWhiskersConfig extends RayConfigurationBase<Vector2> {
    private float rayLength;
    private float whiskerLength;
    private float whiskerAngle;

    public CentralRayWithWhiskersConfig(Steerable<Vector2> owner, float rayLength, float whiskerLength, float whiskerAngle) {
        super(owner, 5);
        this.rayLength = rayLength;
        this.whiskerLength = whiskerLength;
        this.whiskerAngle = whiskerAngle;
    }

    @Override
    public Ray<Vector2>[] updateRays () {
        Vector2 ownerPosition = owner.getPosition();

        // Update central ray
        rays[0].start.set(ownerPosition);
        rays[0].end.set(0, -1).setAngleDeg(getOwner().getOrientation()).setLength(rayLength).add(ownerPosition);

        // Update left rays
        rays[1].start.set(ownerPosition);
        rays[1].end.set(0, -1).setAngleDeg(getOwner().getOrientation() - whiskerAngle).setLength(whiskerLength).add(ownerPosition);
        rays[2].start.set(ownerPosition);
        rays[2].end.set(0, -1).setAngleDeg(getOwner().getOrientation() - (whiskerAngle * 2)).setLength(whiskerLength).add(ownerPosition);

        // Update right rays
        rays[3].start.set(ownerPosition);
        rays[3].end.set(0, -1).setAngleDeg(getOwner().getOrientation() + whiskerAngle).setLength(whiskerLength).add(ownerPosition);
        rays[4].start.set(ownerPosition);
        rays[4].end.set(0, -1).setAngleDeg(getOwner().getOrientation() + (whiskerAngle * 2)).setLength(whiskerLength).add(ownerPosition);

        return rays;
    }

    public Ray<Vector2>[] getRays() {
        return rays;
    }

    /** Returns the length of the central ray. */
    public float getRayLength () {
        return rayLength;
    }

    /** Sets the length of the central ray. */
    public void setRayLength (float rayLength) {
        this.rayLength = rayLength;
    }

    /** Returns the length of the two whiskers. */
    public float getWhiskerLength () {
        return whiskerLength;
    }

    /** Sets the length of the two whiskers. */
    public void setWhiskerLength (float whiskerLength) {
        this.whiskerLength = whiskerLength;
    }

    /** Returns the angle in radians of the whiskers from the central ray. */
    public float getWhiskerAngle () {
        return whiskerAngle;
    }

    /** Sets the angle in radians of the whiskers from the central ray. */
    public void setWhiskerAngle (float whiskerAngle) {
        this.whiskerAngle = whiskerAngle;
    }
}
