package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;

public class NPCDetectionObject extends Entity {
    private static final String TAG = NPCDetectionObject.class.getName();

    private Vector2 tempVector;
    private Vector2 tempLenVector;
    private Vector2 targetVector;
    private Vector2 targetLenVector;

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
    }

    public Vector2 update(World<Entity> world) {
        if (parent.getGun().getRange() * 2 != bbox.getWidth()) {
            bbox.width = parent.getGun().getRange() * 2;
            bbox.height = parent.getGun().getRange() * 2;
            world.update(item, bbox.x, bbox.y, bbox.width, bbox.height);
        }
        bbox.x = parent.getPosition().x - parent.getGun().getRange();
        bbox.y = parent.getPosition().y - parent.getGun().getRange();
        Response.Result result = world.move(item, bbox.x, bbox.y, new NPCDetectionFilter());
        Collisions collisions = result.projectedCollisions;

        if (collisions.size() > 0) {
            targetLenVector.set(Integer.MAX_VALUE, 0);
            targetLenVector.sub(parent.getPosition());
            for (int i = 0; i < collisions.size(); i++) {
                tempVector.set(
                        collisions.get(i).otherRect.x + (((Entity) collisions.get(i).other.userData).bbox.width / 2),
                        collisions.get(i).otherRect.y + (((Entity) collisions.get(i).other.userData).bbox.height / 2));
                tempLenVector.set(tempVector);
                tempLenVector.sub(parent.getPosition());
                if (tempLenVector.len() < targetLenVector.len()) {
                    targetVector.set(tempVector);
                    targetLenVector.set(targetVector);
                    targetLenVector.sub(parent.getPosition());
                }
            }

            return targetVector;
        }

        return null;
    }


    public static class NPCDetectionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if(other == null) return null;
            else if (other.userData instanceof BaseEnemy) return Response.cross;
            else return null;
        }
    }
}
