package com.toads.odyssey.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.util.CollisionDetection;

import javax.print.attribute.standard.PagesPerMinute;

public class Player extends Entity {
    private PlayerMode currentState;
    private PlayerMode previousState;
    private float stateTimer;
    private boolean moveRight;
    private float startJumpY;
    private Body body;
    private final float centerX;
    private final float centerY;
    private int jumpCount = 0;           // Keep track of the number of jumps
    private int maxJumpCount = 5;        // Maximum number of jumps allowed
    private float jumpHeight = 5f;     // Initial jump height
    private float maxJumpHeight = 0;     // Maximum jump height reached
    private float reducedJumpHeight = 1.0f; // Reduced jump height after a certain duration
    private float jumpDurationThreshold = 2f; // Threshold for reducing jump height
    private float jumpTimer = 0;         // Timer for tracking jump duration



    public Player(World world, Vector2 position) {
        super(world, position);

        jumpCount = 0;
        jumpTimer = 0;

        float centerXInPixels = Gdx.graphics.getWidth();
        float centerYInPixels = Gdx.graphics.getHeight();


        float centerX = centerXInPixels / (2 * ToadsOdyssey.PPM) ;
        float centerY = centerYInPixels / (2*ToadsOdyssey.PPM);

        this.centerX = centerX;
        this.centerY = centerY;

        currentState = PlayerMode.IDLE;
        previousState = PlayerMode.IDLE;
        stateTimer = 0;
        moveRight = true;
        setBounds(0, 0, 32 / ToadsOdyssey.PPM, 32 / ToadsOdyssey.PPM);
        setRegion(AssetsLoader.instance.playerAssets.idleAnimation.getKeyFrame(stateTimer, true));

        spritePosition.set(centerX, centerY);
        define();
    }
    @Override
    public void define() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spritePosition.x, spritePosition.y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(14 / ToadsOdyssey.PPM, 14 / ToadsOdyssey.PPM); //changed from 16 so that the player can fit through the gaps

        fixtureDef.shape = shape;
        fixtureDef.friction = 1f; // friction with other objects
        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
    }

    @Override
    public void update(float delta) {
        this.spritePosition.set(body.getPosition().x, body.getPosition().y);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
        setJumpHeightLimit();
    }


    private void setJumpHeightLimit() {
        if (body.getPosition().y >= maxJumpHeight) {
            if (body.getLinearVelocity().y > 0) {
                body.setLinearVelocity(body.getLinearVelocity().x, 0);
            }
        }
    }
    private TextureRegion getFrame(float delta) {
        currentState = getState();
        //System.out.println(currentState);
        TextureRegion region;
        switch (currentState) {
            case MOVE:
                region = AssetsLoader.instance.playerAssets.moveAnimation.getKeyFrame(stateTimer, true);
                break;
            case JUMP:
                region = AssetsLoader.instance.playerAssets.jumpAnimation.getKeyFrame(stateTimer, true);
                break;
            case IDLE:
            default:
                region = AssetsLoader.instance.playerAssets.idleAnimation.getKeyFrame(stateTimer, true);
                break;
        }
        if ((body.getLinearVelocity().x > 0 || moveRight) && !region.isFlipX()) {
            region.flip(true, false);
            moveRight = true;
        } else if ((body.getLinearVelocity().x < 0 || !moveRight) && region.isFlipX()) {
            region.flip(true, false);
            moveRight = false;
        }
        if (currentState == previousState) {
            stateTimer += delta;
        } else {
            stateTimer = 0;
        }
        previousState = currentState;
        return region;
    }
    private PlayerMode getState() {
        if (body.getLinearVelocity().y > 0) {
            return PlayerMode.JUMP;
        } else if (body.getLinearVelocity().x != 0) {
            return PlayerMode.MOVE;
        } else {
            return PlayerMode.IDLE;
        }
    }
    public void handleKeyPressed() {
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.UP);

        float currentY = body.getPosition().y;

        float maxJumpHeight = Gdx.graphics.getHeight() / (ToadsOdyssey.PPM);
        if (upPressed && currentY < maxJumpHeight) {
            body.applyLinearImpulse(new Vector2(0, 5), body.getWorldCenter(), true); //y is the jump height
        }
        float desiredVelocityX = 0;
        if (leftPressed) {
            desiredVelocityX = -2; //leftward velocity
        } else if (rightPressed) {
            desiredVelocityX = 2; // rightward velocity
        }
        float impulseX = desiredVelocityX - body.getLinearVelocity().x;
        body.applyLinearImpulse(new Vector2(impulseX, 0), body.getWorldCenter(), true);

        // limits the speed of the player
        float maxVelocityX = 2; // max velocity in x direction
        float clampedVelocityX = MathUtils.clamp(body.getLinearVelocity().x, -maxVelocityX, maxVelocityX);
        body.setLinearVelocity(clampedVelocityX, body.getLinearVelocity().y);

        if (body.getLinearVelocity().y > 0 && !upPressed) {
            body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y);
        }


        if (!leftPressed && !rightPressed) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }

        if (upPressed && rightPressed) {
            body.applyLinearImpulse(new Vector2(0f, 0f), body.getWorldCenter(), true);
        } else if (upPressed && leftPressed) {
            body.applyLinearImpulse(new Vector2(-0f, 0f), body.getWorldCenter(), true);
        }
    }


    public void draw(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public Vector2 getSpritePosition() {
        return this.spritePosition;
    }

    public Body getBody() {
        return this.body;
    }


}