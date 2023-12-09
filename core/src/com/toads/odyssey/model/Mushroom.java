package com.toads.odyssey.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * A mushroom that will damage the player.
 *
 * @author Joanne, Mika
 * @version 2023
 */

public class Mushroom {

    private static final float DENSITY = 1.0f;
    private static final boolean IS_SENSOR = true;
    private static final float FRICTION = 0.5f;
    private static final float RESTITUTION = 0.2f;
    private static final String USER_DATA = "Mushroom";

    private final Animation<TextureAtlas.AtlasRegion> animation;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private float stateTime = 0;

    /**
     * Constructs a mushroom.
     *
     * @param animation the animation of the mushroom
     * @param world     the Box2D world
     * @param x         the x coordinate of the mushroom
     * @param y         the y coordinate of the mushroom
     * @param width     the width of the mushroom
     * @param height    the height of the mushroom
     */
    public Mushroom(final Animation<TextureAtlas.AtlasRegion> animation, final World world,
                    final float x, final float y, final float width, final float height) {
        this.animation = animation;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        initializeMushroomBody(world);
    }

    /**
     * Initializes the physical properties of the mushroom's body.
     *
     * @param world the Box2D world
     */
    private void initializeMushroomBody(final World world) {
        BodyDef mushroomBodyDef = new BodyDef();
        mushroomBodyDef.type = BodyDef.BodyType.StaticBody;
        mushroomBodyDef.position.set(x + width / 2, y + height / 2);
        Body body = world.createBody(mushroomBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = DENSITY;
        fixtureDef.isSensor = IS_SENSOR;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;
        body.createFixture(fixtureDef).setUserData(USER_DATA);
        shape.dispose();
    }

    /**
     * Updates the mushroom's state time.
     *
     * @param deltaTime the time between frames
     */
    public void update(final float deltaTime) {
        stateTime += deltaTime;
    }

    /**
     * Draws the mushroom.
     *
     * @param batch the sprite batch
     */
    public void draw(final SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, width, height);
    }
}
