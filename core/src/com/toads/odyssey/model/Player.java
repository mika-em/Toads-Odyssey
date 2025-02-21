package com.toads.odyssey.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.util.CollisionDetection;
import com.toads.odyssey.util.Constants;

/**
 * Represents the player.
 *
 * @author Mika, Joane
 * @version 2023
 */
public final class Player extends Entity {
    /**
     * Default number of lives of the player.
     */
    private static int lives = Constants.DEFAULT_LIVES;
    private Body body;
    private PlayerMode currentState;
    private PlayerMode previousState;
    private float stateTimer;
    private boolean moveRight;
    private float maxJumpHeight;
    private boolean canMove = true;
    private boolean isHit;

    /**
     * Constructs a player entity.
     *
     * @param world a Box2D world
     * @param position the initial position of the player
     */
    public Player(final World world, final Vector2 position) {
        super(world, position);
        currentState = PlayerMode.IDLE;
        previousState = PlayerMode.IDLE;
        stateTimer = 0;
        moveRight = true;
        setBounds(0, 0, Constants.THIRTY_TWO / ToadsOdyssey.PPM, Constants.THIRTY_TWO / ToadsOdyssey.PPM);
        setRegion(AssetsLoader.getInstance().getPlayerAssets().idleAnimation.getKeyFrame(stateTimer, true));
        isHit = false;
    }

    /**
     * Decrement player's lives by 1.
     */
    public static void loseLife() {
        lives--;
        System.out.println("Lives: " + lives);
        if (lives <= 0) {
            System.out.println("Game Over");

        }
    }

    /**
     * Checks if the player is alive.
     *
     * @return true if the player is alive, false otherwise.
     */
    public static boolean isAlive() {
        return lives > 0;
    }

    /**
     * Defines the physical properties of the player's body.
     */
    @Override
    public void define() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getSpritePosition());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = getWorld().createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(Constants.SEVENTEEN / ToadsOdyssey.PPM);
        fixtureDef.shape = shape;
        fixtureDef.friction = 0f;
        body.createFixture(fixtureDef).setUserData(this);
    }

    /**
     * Updates player's state and position based on the Box2D physics simulation.
     *
     * @param delta the time between frames
     */
    @Override
    public void update(final float delta) {
        if (canMove) {
            setSpritePosition(body.getPosition());
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(delta));
            setJumpHeightLimit();
        }
    }

    /**
     * Sets the limit of the maximum jump height.
     */
    private void setJumpHeightLimit() {
        if (body.getPosition().y >= maxJumpHeight) {
            if (body.getLinearVelocity().y > 0) {
                body.setLinearVelocity(body.getLinearVelocity().x, 0);
            }
        }
    }

    /**
     * Returns the animation frame of the player per unit time.
     * @param delta the time between frames
     * @return the animation frame of the player.
     */
    private TextureRegion getFrame(final float delta) {
        currentState = getState();
        TextureRegion region = getCurrentAnimationFrame();
        flipPlayerTextureOnDirection(region);
        if (currentState == previousState) {
            stateTimer += delta;
        } else {
            stateTimer = 0;
        }
        previousState = currentState;
        return region;
    }

    /**
     * Returns the current frame of the player based on player's movement.
     * @return the current animation frame of the player as a TextureRegion.
     */
    private TextureRegion getCurrentAnimationFrame() {
        switch (currentState) {
            case HIT:
                return handleHitState();
            case MOVE:
                return AssetsLoader.getInstance().getPlayerAssets().moveAnimation.getKeyFrame(stateTimer, true);
            case JUMP:
                return AssetsLoader.getInstance().getPlayerAssets().jumpAnimation.getKeyFrame(stateTimer, true);
            case IDLE:
            default:
                return AssetsLoader.getInstance().getPlayerAssets().idleAnimation.getKeyFrame(stateTimer, true);
        }
    }

    /**
     * Handles the player's state when hit by a mushroom.
     * @return the animation frame of the player when hit by a mushroom.
     */
    private TextureRegion handleHitState() {
        TextureRegion region = AssetsLoader.getInstance().getPlayerHurtAssets().hurtAnimation.getKeyFrame(stateTimer,
                false);
        if (AssetsLoader.getInstance().getPlayerHurtAssets().hurtAnimation.isAnimationFinished(stateTimer)) {
            isHit = false;
            currentState = PlayerMode.IDLE;
        }
        return region;
    }

    /**
     * Flips the player's texture if it is not facing the same direction that it is moving.
     * @param region the texture of the player.
     */
    private void flipPlayerTextureOnDirection(final TextureRegion region) {
        if ((body.getLinearVelocity().x > 0 || moveRight) && !region.isFlipX()) {
            region.flip(true, false);
            moveRight = true;
        } else if ((body.getLinearVelocity().x < 0 || !moveRight) && region.isFlipX()) {
            region.flip(true, false);
            moveRight = false;
        }
    }

    /**
     * Returns the state of the player movement.
     *
     * @return the state of the player movement.
     */
    private PlayerMode getState() {
        if (body.getLinearVelocity().y > 0) {
            return PlayerMode.JUMP;
        } else if (body.getLinearVelocity().x != 0) {
            return PlayerMode.MOVE;
        }
        if (isHit) {
            return PlayerMode.HIT;
        } else {
            return PlayerMode.IDLE;
        }
    }

    /**
     * Returns the position of the player.
     *
     * @return the position of the player as a Vector2.
     */
    public Vector2 getPosition() {
        return body.getPosition();
    }

    /**
     * Handles the key pressed event.
     */
    public void handleKeyPressed() {
        if (!canMove) {
            return;
        }
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean isOnGround = CollisionDetection.getInstance().isOnGround();
        if (upPressed && isOnGround) {
            float startJumpY = body.getPosition().y;
            maxJumpHeight = startJumpY + 2;
            body.applyLinearImpulse(new Vector2(0, Constants.VERTICAL_LINEAR_IMPULSE), body.getWorldCenter(), true);
        } else if (rightPressed && body.getLinearVelocity().x <= 2) {
            body.applyLinearImpulse(new Vector2(Constants.HORIZONTAL_LINEAR_IMPULSE, 0), body.getWorldCenter(), true);
        } else if (leftPressed && body.getLinearVelocity().x >= -Constants.TWO) {
            body.applyLinearImpulse(new Vector2(-Constants.HORIZONTAL_LINEAR_IMPULSE, 0), body.getWorldCenter(), true);
        }
        if (!leftPressed && !rightPressed) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }

    }

    /**
     * Draws the player.
     *
     * @param batch the batch to draw the player.
     */
    public void draw(final SpriteBatch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    /**
     * Returns the body of the player.
     *
     * @return the body of the player.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Sets the canMove variable.
     *
     * @param canMove true if the player can move, false otherwise.
     */
    public void setCanMove(final boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * Resets the position of the player.
     *
     * @param newPosition the new position of the player.
     */
    public void resetPosition(final Vector2 newPosition) {
        body.setTransform(newPosition, 0);
        body.setLinearVelocity(0, 0);
    }

    /**
     * Returns the number of lives of the player.
     *
     * @return the number of lives of the player.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Checks if the player is hit by a mushroom.
     *
     * @param hit true if the player is hit by a mushroom, false otherwise.
     */
    public void hitByMushroom(final boolean hit) {
        if (hit) {
            isHit = true;
            currentState = PlayerMode.HIT;
            applyKnockback();
            loseLife();
        }
    }

    /**
     * Applies knock back force to the player.
     */
    private void applyKnockback() {
        Vector2 knockbackDirection = new Vector2(-(Constants.TWO), Constants.TWO);
        body.applyLinearImpulse(knockbackDirection.scl(Constants.KNOCK_BACK_INTENSITY), body.getWorldCenter(), true);
    }

    /**
     * Returns the string representation of the player's state.
     * @return a string
     */
    @Override
    public String toString() {
        return "Player{" + "body=" + body + ", currentState=" + currentState + ", previousState=" + previousState
                + ", stateTimer=" + stateTimer + ", moveRight=" + moveRight + ", maxJumpHeight=" + maxJumpHeight
                + ", canMove=" + canMove + ", isHit=" + isHit + '}';
    }
}
