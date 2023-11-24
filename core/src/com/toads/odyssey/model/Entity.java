package com.toads.odyssey.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Entity extends Sprite {
    public World world;
    public Body body;
    public Vector2 spritePosition;


    public Entity(World world, Vector2 spritePosition) {
        this.world = world;
        this.spritePosition = spritePosition;
        define();
    }
    public abstract void define();
    public abstract void update(float delta);

}