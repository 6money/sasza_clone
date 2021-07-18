package com.sixmoney.sasza_clone.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.ItemInfo;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.entities.Crate;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.FloorTile;

import java.util.ArrayList;

public class JBumpRaycastCollisionDetector implements RaycastCollisionDetector<Vector2> {
    private static final String TAG = JBumpRaycastCollisionDetector.class.getName();
    private World<Entity> world;
    private ArrayList<ItemInfo> items;

    public JBumpRaycastCollisionDetector(World<Entity> world) {
        super();
        this.world = world;
        items = new ArrayList<>();
    }

    @Override
    public boolean collides(Ray<Vector2> ray) {
        return findCollision(null, ray);
    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {
        boolean collided = false;

        items.clear();
        world.querySegmentWithCoords(
                inputRay.start.x,
                inputRay.start.y,
                inputRay.end.x,
                inputRay.end.y,
                new RayCastCollisionFilter(),
                items
        );

        if (items.size() > 0) {
            Vector2 collisionPoint = new Vector2(items.get(0).x1, items.get(0).y1);
            Vector2 collisionPointNor = new Vector2(items.get(0).x1, items.get(0).y1).nor();
            Gdx.app.log("POINT", collisionPoint.toString() + ", " + collisionPointNor.toString());
            outputCollision.point = collisionPoint;
            outputCollision.normal = collisionPointNor;
            Gdx.app.log(TAG, outputCollision.point.toString() + ", " + outputCollision.normal.toString());
            collided = true;
        }

        return collided;
    }

    public static class RayCastCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(item == null) return null;
            if (item.userData instanceof Crate) return Response.touch;
            if (item.userData instanceof FloorTile) return Response.touch;
            else return null;
        }
    }
}
