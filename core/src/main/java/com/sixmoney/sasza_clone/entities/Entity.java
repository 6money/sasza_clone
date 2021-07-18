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
    public Vector2 velocity;
    public Vector2 acceleration;
    public float rotation;
    public Item<Entity> item;
    public TextureRegion textureRegion;
    public float health;
    public boolean destructible;

    protected Entity() {
        rotation = 0;
        health = 0;
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        destructible = false;
        textureRegion = Assets.get_instance().playerAssets.playerPlaceholder;
    }

    public void render(Batch batch) {
        Utils.drawTextureRegion(batch, textureRegion, position.x, position.y, rotation);
    }

    public void renderDebug(ShapeDrawer drawer) {
        drawer.rectangle(bbox, Color.MAGENTA);
    }
}
