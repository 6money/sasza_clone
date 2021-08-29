package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;

import java.util.ArrayList;

public class Canopy extends Entity {
    private static final String TAG = Canopy.class.getName();

    private ArrayList<Item> collisionItems;

    public boolean transparent;


    public Canopy(float x, float y, String textureName) {
        super();
        position.set(x, y);
        entityTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        bbox.set(x, y, entityTextureRegion.getRegionWidth(), entityTextureRegion.getRegionHeight());
        rotation = MathUtils.random(0, 360);
        transparent = false;
        collisionItems = new ArrayList<>();
        charaterCollidable = false;
        bulletCollidable = false;
    }


    @Override
    public void update(float delta, World<Entity> world) {
        world.queryRect(bbox.x, bbox.y, bbox.width, bbox.height, new CanopyCollisionFilter(), collisionItems);
        transparent = collisionItems.size() != 0;
    }

    @Override
    public void render(Batch batch) {
        if (transparent) {
            batch.setColor(1, 1, 1, 0.5f);
        }

        Utils.drawTextureRegion(batch, entityTextureRegion, position.x, position.y, rotation);

        if (transparent) {
            batch.setColor(1, 1, 1, 1);
        }
    }

    public static class CanopyCollisionFilter implements CollisionFilter {
        @Override
        public Response filter(Item item, Item other) {
            if (item.userData instanceof Player) return Response.cross;
            else return null;
        }
    }
}
