package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sixmoney.sasza_clone.staticData.CompositeObjectData;
import com.sixmoney.sasza_clone.staticData.Constants;
import com.sixmoney.sasza_clone.staticData.EnvironmentObjData;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class EnvironmentObject extends Entity {

    private float[] rotationOrigin;

    public boolean upper;
    public Array<EnvironmentObject> compositeObjects;

    public EnvironmentObject(float x, float y, String textureName) {
        this(x, y, textureName, false, 0);
    }

    public EnvironmentObject(float x, float y, String textureName, boolean explicitRotate, float rotation) {
        this(x, y, textureName, explicitRotate, rotation, null);
    }

    public EnvironmentObject(float x, float y, String textureName, boolean explicitRotate, float rotation, float[] rotationOrigin) {
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

        if (rotationOrigin != null) {
            this.rotationOrigin = rotationOrigin;
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

        if (this.rotation != 0) {

            float[] p1 = rotatePoint(-(width / 2), -(height / 2));

            if (this.rotationOrigin == null) {
                p1[0] = p1[0] + (width / 2);
                p1[1] = p1[1] + (height / 2);
            }

            if (this.rotation == 90 || this.rotation == -270) {
                if (this.rotationOrigin == null) {
                    p1[0] -= height;
                } else {
                    p1[0] -= height * 1.5;
                    p1[1] += width / 2;
                }
            } else if (this.rotation == -90 || this.rotation == 270) {
                if (this.rotationOrigin == null) {
                    p1[1] -= width;
                } else {
                    p1[0] += height / 2;
                    p1[1] -= width * 1.5;
                }
            } else if (this.rotation == 180) {
                if (this.rotationOrigin == null) {
                    p1[0] -= width;
                    p1[1] -= height;
                } else {
                    p1[0] -= width * 1.5;
                    p1[1] -= height * 1.5;
                }
            }

            bboxX += p1[0];
            bboxY += p1[1];

            if (this.rotation == 90 || this.rotation == -90) {
                width = entityTextureRegion.getRegionHeight();
                height = entityTextureRegion.getRegionWidth();
            }

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
                compositeObjects.add(new EnvironmentObject(x + record.xOffset, y + record.yOffset, record.textureName, explicitRotate, this.rotation, record.rotateOrig));
            }
        } catch (Exception ignored) {
        }
    }

    private float[] rotatePoint(float tempX, float tempY) {
        if (this.rotationOrigin != null) {
            tempX -= this.rotationOrigin[0];
            tempY -= this.rotationOrigin[1];
        }

        if (this.rotation == 90 || this.rotation == -270) {
            float temp = tempX;
            tempX = -tempY;
            tempY = temp;
        } else if (this.rotation == -90 || this.rotation == 270) {
            float temp = tempX;
            tempX = tempY;
            tempY = -temp;
        } else if (this.rotation == 180) {
            tempX = -tempX;
            tempY = -tempY;
        }

        if (this.rotationOrigin != null) {
            tempX += this.rotationOrigin[0];
            tempY += this.rotationOrigin[1];
        }

        return new float[]{tempX, tempY};
    }

    @Override
    public void render(Batch batch) {
        if (!upper) {
            if (rotationOrigin == null) {
                Utils.drawTextureRegion(batch, entityTextureRegion, position.x, position.y, rotation);
            } else {
                Utils.drawTextureRegion(batch, entityTextureRegion, position.x, position.y, rotation, 1, rotationOrigin[0], rotationOrigin[1]);
            }
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
            if (rotationOrigin == null) {
                Utils.drawTextureRegion(batch, entityTextureRegion, position.x, position.y, rotation);
            } else {
                Utils.drawTextureRegion(batch, entityTextureRegion, position.x, position.y, rotation, 1, rotationOrigin[0], rotationOrigin[1]);
            }
        }

        for (EnvironmentObject environmentObject: compositeObjects) {
            if (environmentObject.upper) {
                environmentObject.renderSecondary(batch);
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
