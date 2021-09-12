package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.ItemInfo;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.CollisionComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class NPCDetectionObject extends Entity implements Comparator<Integer> {
    private static final String TAG = NPCDetectionObject.class.getName();

    private Vector2 tempVector;
    private Vector2 tempLenVector;
    private Vector2 targetVector;
    private Vector2 targetLenVector;
    private Collisions collisions;
    private CollisionComparator collisionComparator;
    private ArrayList<ItemInfo> infos;
    private NPCDetectionFilter npcDetectionFilter;
    private NPCRayQueryFilter npcRayQueryFilter;

    public BaseNPC parent;

    public NPCDetectionObject(BaseNPC parent) {
        super();
        this.parent = parent;
        position = new Vector2(parent.getPosition().x - parent.getGun().getRange(), parent.getPosition().y - parent.getGun().getRange());
        bbox = new Rectangle(
                position.x,
                position.y,
                parent.getGun().getRange() * 2,
                parent.getGun().getRange() * 2
        );
        item = new Item<>(this);
        characterCollidable = false;
        bulletCollidable = false;
        tempVector = new Vector2();
        tempLenVector = new Vector2();
        targetVector = new Vector2();
        targetLenVector = new Vector2();
        collisionComparator = new CollisionComparator();
        npcDetectionFilter = new NPCDetectionFilter();
        npcRayQueryFilter = new NPCRayQueryFilter();
        infos = new ArrayList<>();
    }

    public Vector2 update(World<Entity> world) {
        if (parent.getGun().getRange() * 2 != bbox.getWidth()) {
            bbox.width = parent.getGun().getRange() * 2;
            bbox.height = parent.getGun().getRange() * 2;
            world.update(item, bbox.x, bbox.y, bbox.width, bbox.height);
        }
        bbox.x = parent.getPosition().x - parent.getGun().getRange();
        bbox.y = parent.getPosition().y - parent.getGun().getRange();
        Response.Result result = world.move(item, bbox.x, bbox.y, npcDetectionFilter);
        collisions = result.projectedCollisions;

        if (collisions.size() > 0) {

            collisionComparator.setCollisions(collisions);
            collisionComparator.setParentPosition(parent.getPosition());
            Integer[] collisionArray = new Integer[collisions.size()];
            for (int i = 0; i < collisions.size(); i++) {
                collisionArray[i] = i;
            }
            Arrays.sort(collisionArray, collisionComparator);

            for (Integer integer : collisionArray) {
                world.querySegmentWithCoords(
                        parent.getPosition().x,
                        parent.getPosition().y,
                        collisions.get(integer).otherRect.x + (collisions.get(integer).otherRect.w / 2),
                        collisions.get(integer).otherRect.y + (collisions.get(integer).otherRect.h / 2),
                        npcRayQueryFilter,
                        infos
                );

                if (infos.size() != 0 && infos.get(0).item.userData instanceof BaseEnemy) {
                    targetVector.set(
                            collisions.get(integer).otherRect.x + (collisions.get(integer).otherRect.w / 2),
                            collisions.get(integer).otherRect.y + (collisions.get(integer).otherRect.h / 2));
                    return targetVector;
                }
            }
        }



        return null;
    }

    @Override
    public int compare(Integer i1, Integer i2) {
        tempVector.set(
                collisions.get(i1).otherRect.x + (collisions.get(i1).otherRect.w / 2),
                collisions.get(i1).otherRect.y + (collisions.get(i1).otherRect.h / 2));
        tempVector.sub(parent.getPosition());
        float colDist1 = tempLenVector.len();

        tempVector.set(
                collisions.get(i2).otherRect.x + (collisions.get(i2).otherRect.w / 2),
                collisions.get(i2).otherRect.y + (collisions.get(i2).otherRect.h / 2));
        tempVector.sub(parent.getPosition());
        float colDist2 = tempLenVector.len();

        return (int) (colDist1 - colDist2);
    }


    public static class NPCDetectionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(other == null) return null;
            else if (other.userData instanceof BaseEnemy) return Response.cross;
            else return null;
        }
    }

    public static class NPCRayQueryFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(item == null) return null;
            else if (item.userData instanceof BaseNPC) return null;
            else if (item.userData instanceof Player) return null;
            else if (((Entity) item.userData).bulletCollidable) return Response.touch;
            else return null;
        }
    }
}
