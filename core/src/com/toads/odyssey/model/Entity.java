package com.toads.odyssey.model;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Entity extends Sprite {
    public abstract void define();
    public abstract void update(float delta);
}
