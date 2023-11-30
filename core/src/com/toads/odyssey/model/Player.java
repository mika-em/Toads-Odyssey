package com.toads.odyssey.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.util.CollisionDetection;

public class Player extends Entity {
    private PlayerMode currentState;
    private PlayerMode previousState;
    private float stateTimer;
    private boolean moveRight;
    private float maxJumpHeight;
    private float startJumpY;
    public Body body;
    private boolean canMove = true;

    public Player(World world, Vector2 position) {
        super(world, position);
        currentState = PlayerMode.IDLE;
        previousState = PlayerMode.IDLE;
        stateTimer = 0;
        moveRight = true;
        setBounds(0, 0, 32 / ToadsOdyssey.PPM, 32 / ToadsOdyssey.PPM);
        setRegion(AssetsLoader.instance.playerAssets.idleAnimation.getKeyFrame(stateTimer, true));
    }
    @Override
    public void define() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spritePosition);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(17 / ToadsOdyssey.PPM);
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef).setUserData(this);
    }
    @Override
    public void update(float delta) {
        if (canMove) {

            this.spritePosition = body.getPosition();
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(delta));
            setJumpHeightLimit();
        }
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
    public Vector2 getPosition() {
        return body.getPosition();
    }
    public void handleKeyPressed() {
        if (!canMove) {
            return;
        }
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean isOnGround = CollisionDetection.instance.isOnGround();
        //System.out.println("isOnGround: " + isOnGround);
        if (upPressed && isOnGround) {
            startJumpY = body.getPosition().y;
            maxJumpHeight = startJumpY + 2;
            body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
        } else if (rightPressed && body.getLinearVelocity().x <= 2) {
            body.applyLinearImpulse(new Vector2(1.5f, 1), body.getWorldCenter(), true);
        } else if (leftPressed && body.getLinearVelocity().x >= -2) {
            body.applyLinearImpulse(new Vector2(-1.5f, 1), body.getWorldCenter(), true);
        }
        if (!leftPressed && !rightPressed) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }

    }
    public void draw(SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public Body getBody() {
        return body;
    }
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

}