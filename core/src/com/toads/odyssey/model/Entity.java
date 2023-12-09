package com.toads.odyssey.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
/**
 * Abstract class for moving game entities.
 *
 * @author Mika, Joanne
 * @version 2023
 */
public abstract class Entity extends Sprite {
    private final World world;
    private Vector2 spritePosition;
    /**
     * Constructor a game entity.
     * @param world the Box2D world
     * @param spritePosition the position of the sprite
     */
    public Entity(final World world, final Vector2 spritePosition) {
        this.world = world;
        this.spritePosition = spritePosition;
        define();
    }
    /**
     * Define the entity's physical properties.
     */
    public abstract void define();
    /**
     * Update the entity.
     * @param delta the time between frames
     */
    public abstract void update(float delta);
    /**
     * Returns the Box2D world.
     * @return the world
     */
    protected World getWorld() {
        return world;
    }

    /**
     * Returns the position of the sprite.
     * @return the position of the sprite
     */
    protected Vector2 getSpritePosition() {
        return spritePosition;
    }

    /**
     * Sets the position of the sprite.
     * @param spritePosition the position of the sprite
     */
    protected void setSpritePosition(final Vector2 spritePosition) {
        this.spritePosition = spritePosition;
    }

    /**
     * String representation of Entity.
     * @return the string representation of Entity.
     */
    @Override
    public String toString() {
        return "Entity{" + "world=" + world + ", spritePosition=" + spritePosition + '}';
    }
}
