package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.math.Vector2;

public class CentralRayWithWhiskersConfig extends CentralRayWithWhiskersConfiguration<Vector2> {
    private float rayLength;
    private float whiskerLength;
    private float whiskerAngle;

    public CentralRayWithWhiskersConfig(Steerable<Vector2> owner, float rayLength, float whiskerLength, float whiskerAngle) {
        super(owner, rayLength, whiskerLength, whiskerAngle);
        this.rayLength = rayLength;
        this.whiskerLength = whiskerLength;
        this.whiskerAngle = whiskerAngle;
    }

    @Override
    public Ray<Vector2>[] updateRays () {
        Vector2 ownerPosition = owner.getPosition();

        // Update central ray
        rays[0].start.set(ownerPosition);
        rays[0].end.set(0, -1).setAngleDeg(getOwner().getOrientation() - 90).setLength(rayLength).add(ownerPosition);

        // Update left ray
        rays[1].start.set(ownerPosition);
        rays[1].end.set(0, -1).setAngleDeg(getOwner().getOrientation() - whiskerAngle - 90).setLength(whiskerLength).add(ownerPosition);

        // Update right ray
        rays[2].start.set(ownerPosition);
        rays[2].end.set(0, -1).setAngleDeg(getOwner().getOrientation() + whiskerAngle - 90).setLength(whiskerLength).add(ownerPosition);

        return rays;
    }
}
