package com.toads.odyssey.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

public class Mushroom {

    private final Animation<TextureAtlas.AtlasRegion> animation;
    private final Body body;
    private float stateTime = 0;
    private final float width;
    private final float height;
    private final float x;
    private final float y;
    private final boolean isAlive = true;
    private final World world;

    private boolean isColliding = false;
    public Mushroom(Animation<TextureAtlas.AtlasRegion> animation, World world, float x, float y, float width, float height) {
        this.animation = animation;
        this.world = world;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        BodyDef mushroomBodyDef = new BodyDef();
        mushroomBodyDef.type = BodyDef.BodyType.StaticBody;
        mushroomBodyDef.position.set(x + width / 2, y + height / 2);
        body = world.createBody(mushroomBodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = true;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.2f;
        body.createFixture(fixtureDef).setUserData("Mushroom");
        shape.dispose();

    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, width, height);
    }

}