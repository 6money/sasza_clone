package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Collisions;

import java.util.Comparator;

public class CollisionComparator implements Comparator<Integer> {
    private Vector2 tempVector;
    private Collisions collisions;
    private Vector2 parentPosition;

    public CollisionComparator() {
        collisions = new Collisions();
        parentPosition = new Vector2();
        tempVector = new Vector2();
    }

    public CollisionComparator(Collisions collisions, Vector2 parentPosition) {
        this.collisions = collisions;
        this.parentPosition = parentPosition;
        tempVector = new Vector2();
    }

    public void setCollisions(Collisions collisions) {
        this.collisions = collisions;
    }

    public void setParentPosition(Vector2 parentPosition) {
        this.parentPosition = parentPosition;
    }

    @Override
    public int compare(Integer i1, Integer i2) {
        tempVector.set(
                collisions.get(i1).otherRect.x + (collisions.get(i1).otherRect.w / 2),
                collisions.get(i1).otherRect.y + (collisions.get(i1).otherRect.h / 2));
        tempVector.sub(parentPosition);
        float colDist1 = tempVector.len();

        tempVector.set(
                collisions.get(i2).otherRect.x + (collisions.get(i2).otherRect.w / 2),
                collisions.get(i2).otherRect.y + (collisions.get(i2).otherRect.h / 2));
        tempVector.sub(parentPosition);
        float colDist2 = tempVector.len();

        return (int) (colDist1 - colDist2);
    }
}
