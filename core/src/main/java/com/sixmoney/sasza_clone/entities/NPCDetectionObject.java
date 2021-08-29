package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Collision;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;

public class NPCDetectionObject extends Entity {
    private static final String TAG = NPCDetectionObject.class.getName();

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
        charaterCollidable = false;
        bulletCollidable = false;
    }

    public Vector2 update(World<Entity> world) {
        bbox.x = parent.getPosition().x - parent.getGun().getRange();
        bbox.y = parent.getPosition().y - parent.getGun().getRange();
        bbox.width = parent.getGun().getRange() * 2;
        bbox.height = parent.getGun().getRange() * 2;
        Response.Result result = world.move(item, bbox.x, bbox.y, new NPCDetectionFilter());
        Collisions collisions = result.projectedCollisions;

        if (collisions.size() > 0) {
            Collision collision = collisions.get(0);
            return new Vector2(collision.otherRect.x + (((Entity) collision.other.userData).bbox.width / 2), collision.otherRect.y + (((Entity) collision.other.userData).bbox.height / 2));
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
