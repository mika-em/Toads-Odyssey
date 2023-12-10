package com.toads.odyssey.model;

import static com.toads.odyssey.ToadsOdyssey.PPM;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Represents a coin in the game, which the player can collect.
 *
 * @author Joanne, Mika
 * @version 2023
 */
public final class Coin {
    private static final float COIN_SIZE = 18;
    private static final float DENSITY = 1.0f;
    private static final float FRICTION = 0.5f;
    private static final float RESTITUTION = 0.2f;
    private final Animation<TextureAtlas.AtlasRegion> animation;
    private final float width;
    private final float height;
    private final float x;
    private final float y;
    private float stateTime = 0;
    private boolean collected = false;
    private int coinCount = 0;
    private int collectedCoins = 0;
    private Body body;

    /**
     * Constructs a coin.
     *
     * @param animation the animation of the coin
     * @param world     the Box2D world
     * @param x         the x coordinate of the coin
     * @param y         the y coordinate of the coin
     */
    public Coin(final Animation<TextureAtlas.AtlasRegion> animation, final World world,
                final float x, final float y) {
        this.animation = animation;
        this.x = x;
        this.y = y;
        this.width = COIN_SIZE / PPM;
        this.height = COIN_SIZE / PPM;
        BodyDef coinBodyDef = new BodyDef();
        coinBodyDef.type = BodyDef.BodyType.StaticBody;
        coinBodyDef.position.set(x + width / 2, y + height / 2);
        body = world.createBody(coinBodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    /**
     * Checks if the player has collided with the coin.
     *
     * @param playerBody the player's body
     * @return true if the player has collided with the coin, false otherwise
     */
    public boolean isCollision(final Body playerBody) {
        if (playerBody != null && !collected) {
            Array<Fixture> fixtures = playerBody.getFixtureList();
            for (int i = 0; i < fixtures.size; i++) {
                Fixture fixture = fixtures.get(i);
                if (fixture.testPoint(x + width / 2, y + height / 2)) {
                    collected = true;
                    setCoinCount(getCoinCount() + 1);
                    remove();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes the coin from the world.
     */
    public void remove() {
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
            setCollectedCoins(getCollectedCoins() + 1);
        }
    }

    /**
     * Updates the coin.
     *
     * @param deltaTime the time between frames
     */
    public void update(final float deltaTime) {
        stateTime += deltaTime;
    }

    /**
     * Draws the coin if it has not been collected.
     *
     * @param batch the sprite batch
     */
    public void draw(final SpriteBatch batch) {
        if (!collected) {
            batch.draw(animation.getKeyFrame(stateTime, true), x, y, width, height);
        }
    }

    /**
     * Returns the number of coins collected.
     *
     * @return the number of coins collected
     */
    public int getCollectedCoins() {
        return collectedCoins;
    }

    /**
     * Sets the number of coins collected.
     *
     * @param collectedCoins the number of coins collected
     */
    public void setCollectedCoins(final int collectedCoins) {
        this.collectedCoins = collectedCoins;
    }

    /**
     * Returns the number of coins in the level.
     *
     * @return the number of coins in the level
     */
    public int getCoinCount() {
        return coinCount;
    }

    /**
     * Sets the number of coins in the level.
     *
     * @param coinCount the number of coins in the level
     */
    public void setCoinCount(final int coinCount) {
        this.coinCount = coinCount;
    }

    /**
     * Disposes the coin.
     */
    public void dispose() {
    }

    /**
     * A string representation of the coin.
     *
     * @return a string representation of the coin
     */
    @Override
    public String toString() {
        return "Coin{"
                + "animation=" + animation + ", width=" + width + ", height=" + height + ", x=" + x + ", y=" + y
                + ", stateTime=" + stateTime + ", collected=" + collected + ", coinCount=" + coinCount
                + ", collectedCoins=" + collectedCoins + ", body=" + body + '}';
    }
}
