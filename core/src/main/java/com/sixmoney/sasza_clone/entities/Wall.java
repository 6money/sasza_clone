package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.Utils;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Wall extends Entity {
    private TextureRegion upperTextureRegion;
    private boolean isDoor;

    public Wall(float x, float y, String textureName, boolean isDoor) {
        super();
        charaterCollidable = !isDoor;
        this.isDoor = isDoor;
        position = new Vector2(x, y);

        if (isDoor) {
            String lowerName = textureName.replace("upper", "lower");
            entityTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(lowerName);
            upperTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        } else {
            entityTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
            bbox.set(
                    position.x + Constants.BBOX_BUFFER_WALL,
                    position.y + Constants.BBOX_BUFFER_WALL,
                    entityTextureRegion.getRegionWidth() - Constants.BBOX_BUFFER_WALL * 2,
                    entityTextureRegion.getRegionHeight() - Constants.BBOX_BUFFER_WALL * 2
            );
            bulletCollisionSubObject = new BulletCollisionSubObject(this, Constants.BBOX_BUFFER_WALL);
        }
    }


    @Override
    public void renderSecondary(Batch batch) {
        if (isDoor) {
            Utils.drawTextureRegion(batch, upperTextureRegion, position.x, position.y, rotation);
        }
    }


    @Override
    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);
        if (bulletCollisionSubObject != null && bulletCollidable) {
            drawer.rectangle(bulletCollisionSubObject.bbox, Color.ORANGE);
        }
    }


    @Override
    public String toString() {
        if (isDoor) {
            return "Door loaded at x: " + position.x + ", y: " + position.y;
        }

        return this.getClass().getSimpleName() + " loaded at x: " + position.x + ", y: " + position.y;
    }
}
