package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.utils.Assets;
import com.sixmoney.sasza_clone.utils.Utils;

import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Entity {
    public Vector2 position;
    public Rectangle bbox;
    public BulletCollisionSubObject bulletCollisionSubObject;
    public Vector2 velocity;
    public Vector2 acceleration;
    public float rotation;
    public Item<Entity> item;
    public TextureRegion entityTextureRegion;
    public Animation<TextureRegion> entityAnimation;
    public long animationStartTime;
    public float health;
    public boolean destructible;
    public boolean collidable;

    protected Entity() {
        rotation = 0;
        health = 0;
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 0);
        bbox = new Rectangle(0, 0, 0, 0);
        destructible = false;
        rotation = 0;
        collidable = true;
        entityTextureRegion = Assets.get_instance().playerAssets.playerPlaceholder;
        animationStartTime = 0;
        item = new Item<>(this);
    }

    public void update(float delta, World<Entity> world) {
    }

    public void render(Batch batch) {
        Utils.drawTextureRegion(batch, entityTextureRegion, position.x, position.y, rotation);
    }

    public void renderSecondary(Batch batch) {
    }

    public void renderDebug(ShapeDrawer drawer) {
        drawer.rectangle(bbox, Color.MAGENTA);
    }

    public float getHealth() {
        return health;
    }

    public void decrementHealth(float value) {
        health -= value;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " loaded at x: " + position.x + ", y: " + position.y;
    }
}
