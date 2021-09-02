package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.CompositeObjectData;
import com.sixmoney.sasza_clone.utils.Constants;
import com.sixmoney.sasza_clone.utils.EnvironmentObjData;
import com.sixmoney.sasza_clone.utils.Utils;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class EnvironmentObject extends Entity {

    public boolean upper;
    public Array<EnvironmentObject> compositeObjects;

    public EnvironmentObject(float x, float y, String textureName) {
        this(x, y, textureName, false, 0);
    }

    public EnvironmentObject(float x, float y, String textureName, boolean explicitRotate, float rotation) {
        super();
        entityTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        position = new Vector2(x, y);
        health = 100;
        maxHealth = 100;

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

        String objKey;

        if (textureName.contains("box") || textureName.contains("barrel") || textureName.contains("bush")) {
            objKey = textureName.split("_")[1];
        } else {
            objKey = textureName;
        }

        EnvironmentObjData.EnvironmentObjRecord envObjRecord = EnvironmentObjData.get_instance().getObjData(objKey);

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

            bbox.set(
                    bboxX + envObjRecord.bboxBuffer[1],
                    bboxY + envObjRecord.bboxBuffer[0],
                    width - envObjRecord.bboxBuffer[1] * 2,
                    height - envObjRecord.bboxBuffer[0] * 2
            );

        } else {
            bbox.set(
                    bboxX + envObjRecord.bboxBuffer[0],
                    bboxY + envObjRecord.bboxBuffer[1],
                    width - envObjRecord.bboxBuffer[0] * 2,
                    height - envObjRecord.bboxBuffer[1] * 2
            );
        }

        this.characterCollidable = envObjRecord.characterCollidable;
        this.bulletCollidable = envObjRecord.bulletCollidable;
        this.upper = envObjRecord.upper;
        this.destructible = envObjRecord.destructible;

        bulletCollisionSubObject = new BulletCollisionSubObject(this, Constants.BBOX_BUFFER_ENVIRONMENT);
        compositeObjects = new Array<>();

        try {
            CompositeObjectData.CompositeObjectRecord[] test = CompositeObjectData.get_instance().compositeObjectRecordMap.get(textureName);

            for (CompositeObjectData.CompositeObjectRecord record: test) {
                compositeObjects.add(new EnvironmentObject(x + record.xOffset, y + record.yOffset, record.textureName, explicitRotate, this.rotation));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void render(Batch batch) {
        if (!upper) {
            super.render(batch);
        }

        for (EnvironmentObject environmentObject: compositeObjects) {
            if (!environmentObject.upper) {
                environmentObject.render(batch);
            }
        }
    }

    @Override
    public void renderSecondary(Batch batch) {
        if (upper) {
            Utils.drawTextureRegion(batch, entityTextureRegion, position.x, position.y, rotation);
        }

        for (EnvironmentObject environmentObject: compositeObjects) {
            if (environmentObject.upper) {
                Utils.drawTextureRegion(
                        batch,
                        environmentObject.entityTextureRegion,
                        environmentObject.position.x,
                        environmentObject.position.y,
                        environmentObject.rotation
                );
            }
        }
    }

    @Override
    public void renderDebug(ShapeDrawer drawer) {
        super.renderDebug(drawer);
        if (bulletCollisionSubObject != null && bulletCollidable) {
            drawer.rectangle(bulletCollisionSubObject.bbox, Color.ORANGE);
        }

        for (EnvironmentObject environmentObject: compositeObjects) {
            environmentObject.renderDebug(drawer);
        }
    }
}
