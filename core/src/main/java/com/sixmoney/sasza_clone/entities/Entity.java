package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;

import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Entity {
    public Vector2 position;
    public Rectangle bbox;
    public float rotation;
    public Item<Entity> item;
    public TextureRegion textureRegion;

    protected Entity() {
        rotation = 0;
        textureRegion = Assets.get_instance().playerAssets.playerPlaceholder;
    }

    public void render(Batch batch) {
        Utils.drawTextureRegion(batch, textureRegion, position.x, position.y, rotation);
    }

    public void renderDebug(ShapeDrawer drawer) {
        drawer.rectangle(bbox, Color.MAGENTA);
    }
}
