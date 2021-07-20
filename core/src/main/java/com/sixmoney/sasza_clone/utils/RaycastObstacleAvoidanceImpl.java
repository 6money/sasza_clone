package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;

public class RaycastObstacleAvoidanceImpl extends RaycastObstacleAvoidance<Vector2> {
    private Collision<Vector2> outputCollision;
    private Collision<Vector2> minOutputCollision;

    public RaycastObstacleAvoidanceImpl(Steerable<Vector2> owner, RayConfiguration<Vector2> rayConfiguration,
                                        RaycastCollisionDetector<Vector2> raycastCollisionDetector, float distanceFromBoundary) {
        super(owner, rayConfiguration, raycastCollisionDetector, distanceFromBoundary);
        this.outputCollision = new Collision<>(newVector(owner), newVector(owner));
        this.minOutputCollision = new Collision<>(newVector(owner), newVector(owner));
    }

    @Override
    protected SteeringAcceleration<Vector2> calculateRealSteering (SteeringAcceleration<Vector2> steering) {
        Vector2 ownerPosition = owner.getPosition();
        float minDistanceSquare = Float.POSITIVE_INFINITY;

        // Get the updated rays
        Ray<Vector2>[] inputRays = rayConfiguration.updateRays();

        // Process rays
        for (int i = 0; i < inputRays.length; i++) {
            // Find the collision with current ray
            boolean collided = raycastCollisionDetector.findCollision(outputCollision, inputRays[i]);

            if (collided) {
                float distanceSquare = ownerPosition.dst2(outputCollision.point);
                if (distanceSquare < minDistanceSquare) {
                    minDistanceSquare = distanceSquare;
                    // Swap collisions
                    Collision<Vector2> tmpCollision = outputCollision;
                    outputCollision = minOutputCollision;
                    minOutputCollision = tmpCollision;
                }
            }
        }

        // Return zero steering if no collision has occurred
        if (minDistanceSquare == Float.POSITIVE_INFINITY) return steering.setZero();

        // Calculate and seek the target position
        steering.linear.set(minOutputCollision.point)
                .mulAdd(minOutputCollision.normal, owner.getBoundingRadius() + distanceFromBoundary).sub(owner.getPosition()).nor()
                .scl(getActualLimiter().getMaxLinearAcceleration());

        // No angular acceleration
        steering.angular = 0;

        // Output steering acceleration
        return steering;
    }
}
