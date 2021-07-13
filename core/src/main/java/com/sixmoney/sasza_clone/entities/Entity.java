package com.sixmoney.sasza_clone.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;

public abstract class Entity {
    public Vector2 position;
    public Rectangle bbox;
    public float rotation;
    public Item<Entity> item;
}
