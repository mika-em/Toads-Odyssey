package com.toads.odyssey.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Represents a mushroom.
 *
 * @author Joanne, Mika
 * @version 2023
 */
public final class Mushroom {
    private static final float DENSITY = 1.0f;
    private static final float FRICTION = 0.5f;
    private static final float RESTITUTION = 0.2f;
    private final Animation<TextureAtlas.AtlasRegion> animation;
    private final float width;
    private final float height;
    private final float x;
    private final float y;
    private final World world;
    private Body body;
    private float stateTime = 0;

    /**
     * Constructs a mushroom.
     * @param animation the animation of the mushroom
     * @param world the Box2D world
     * @param x the x coordinate of the mushroom
     * @param y the y coordinate of the mushroom
     * @param width the width of the mushroom
     * @param height the height of the mushroom
     */
    public Mushroom(final Animation<TextureAtlas.AtlasRegion> animation, final World world, final float x,
                    final float y, final float width, final float height) {
        this.animation = animation;
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createMushroomBody();
        createMushroomFixture();

    }

    /**
     * Creates the mushroom body.
     */
    public void createMushroomBody() {
        BodyDef mushroomBodyDef = new BodyDef();
        mushroomBodyDef.type = BodyDef.BodyType.StaticBody;
        mushroomBodyDef.position.set(x + width / 2, y + height / 2);
        body = world.createBody(mushroomBodyDef);
    }

    /**
     * Creates the mushroom fixture.
     */
    private void createMushroomFixture() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.isSensor = true;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;
        body.createFixture(fixtureDef).setUserData("Mushroom");
        shape.dispose();
    }

    /**
     * Updates the mushroom per unit of time.
     * @param deltaTime the time between frames
     */
    public void update(final float deltaTime) {
        stateTime += deltaTime;
    }

    /**
     * Draws the mushroom.
     * @param batch the sprite batch
     */
    public void draw(final SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, width, height);
    }
}
