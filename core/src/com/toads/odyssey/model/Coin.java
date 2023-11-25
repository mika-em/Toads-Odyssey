package com.toads.odyssey.model;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import com.toads.odyssey.ToadsOdyssey;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Coin {
    private Animation<TextureAtlas.AtlasRegion> animation;
    private float stateTime = 0;
    private float width, height; // size of the coin
    private float x, y; // position of the coin
    private boolean collected = false;
    private int coinCount = 0;
    private int collectedCoins = 0;
    private Body body;

    public Coin(Animation<TextureAtlas.AtlasRegion> animation, World world, float x, float y) {
        this.animation = animation;
        this.x = x;
        this.y = y;
        this.width = 18 / ToadsOdyssey.PPM;
        this.height = 18 / ToadsOdyssey.PPM;

        //create box2D body for the coin:
        BodyDef coinBodyDef = new BodyDef();
        coinBodyDef.type = BodyDef.BodyType.StaticBody;
        coinBodyDef.position.set(x + width / 2, y + height / 2);
        body = world.createBody(coinBodyDef);
        //create the shape of the coin from the tiledmap rectangle:
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        //create the fixture for the coin:
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = true;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.2f;
        body.createFixture(fixtureDef);

        shape.dispose();
    }

    public boolean isCollision(Body playerBody) {
        if (playerBody != null && !collected) {
            for (Fixture fixture : playerBody.getFixtureList()) {
                if (fixture.testPoint(x + width / 2, y + height / 2)) {
                    collected = true;
                    coinCount++;
                    remove();
                    return true;
                }
            }
        }
        return false;
    }


    public void remove() {
        if (body != null) {
            body.getWorld().destroyBody(body);
            body = null;
            collectedCoins++; //collected coins stored as an int for the HUD
        }
    }

    public Rectangle getBoundingRectangle() {
        return new Rectangle(x, y, width, height);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        if (!collected) {
            batch.draw(animation.getKeyFrame(stateTime, true), x, y, width, height);
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public void dispose() {
    }
}
