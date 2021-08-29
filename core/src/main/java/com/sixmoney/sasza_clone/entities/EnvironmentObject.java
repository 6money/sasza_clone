package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Constants;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class EnvironmentObject extends Entity {

    public EnvironmentObject(float x, float y, String textureName) {
        this(x, y, textureName, false, 0);
    }

    public EnvironmentObject(float x, float y, String textureName, boolean explicitRotate, float rotation) {
        super();
        entityTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        position = new Vector2(x, y);
        health = 100;
        maxHealth = 100;
        if (textureName.contains("box") || textureName.contains("barrel")) {
            destructible = true;
        }

        if (!explicitRotate) {
            int randRotation = MathUtils.random(0, 3);

            switch (randRotation) {
                case 0:
                    this.rotation = 0;
                    break;
                case 1:
                    this.rotation = 90;
                    break;
                case 2:
                    this.rotation = 180;
                    break;
                case 3:
                    this.rotation = 270;
                    break;
            }
        } else {
            this.rotation = rotation;
        }

        float width = entityTextureRegion.getRegionWidth();
        float height = entityTextureRegion.getRegionHeight();
        float bboxX = x;
        float bboxY = y;

        if (this.rotation == 90 || this.rotation == 270 ||
                this.rotation == -90 || this.rotation == -270) {
            bboxX += width / 2 - height / 2;
            bboxY -= width / 2 - height / 2;
            width = entityTextureRegion.getRegionHeight();
            height = entityTextureRegion.getRegionWidth();
        }

        bbox.set(
                bboxX + Constants.BBOX_BUFFER_ENVIRONMENT,
                bboxY + Constants.BBOX_BUFFER_ENVIRONMENT,
                width - Constants.BBOX_BUFFER_ENVIRONMENT * 2,
                height - Constants.BBOX_BUFFER_ENVIRONMENT * 2
        );

        if (textureName.contains("bush")) {
            bulletCollidable = false;
        }

        bulletCollisionSubObject = new BulletCollisionSubObject(this, Constants.BBOX_BUFFER_ENVIRONMENT);
    }

    @Override
    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);
        if (bulletCollisionSubObject != null && bulletCollidable) {
            drawer.rectangle(bulletCollisionSubObject.bbox, Color.ORANGE);
        }
    }
}
