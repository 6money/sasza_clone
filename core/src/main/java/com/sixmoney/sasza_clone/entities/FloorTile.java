package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.sasza_clone.utils.Assets;

public class FloorTile extends Entity {

    public FloorTile(float x, float y, String textureName) {
        super();
        entityTextureRegion = Assets.get_instance().getPrivateAtlas().findRegion(textureName);
        position = new Vector2(x, y);
        bbox = new Rectangle(position.x, position.y, entityTextureRegion.getRegionWidth(), entityTextureRegion.getRegionHeight());
    }

    @Override
    public String toString() {
        return "tile located at x: " + position.x + ", y: " + position.y + ", width: " + entityTextureRegion.getRegionWidth() + ", height: " + entityTextureRegion.getRegionHeight();
    }
}
