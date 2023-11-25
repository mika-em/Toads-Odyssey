package objects.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import static helper.Constants.PPM;

public class Player extends GameEntity {

    private int jumpCounter;

    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 8f;
        this.jumpCounter = 0;
    }

    @Override
    public void update() {
        // Scale the position conversion
        x = body.getPosition().x * PPM; //*2 ?
        y = body.getPosition().y * PPM;

        checkUserInput();
    }

    @Override
    public void render(SpriteBatch batch) {
        // Rendering code (if any)

    }

    private void checkUserInput() {
        velocityX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocityX = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocityX = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && jumpCounter < 2) {
            float force = body.getMass() * 18 ;
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            jumpCounter++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            float force = body.getMass() * -2 ;
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
        }

        // reset jump counter if player is on the ground
        if (body.getLinearVelocity().y == 0) {
            jumpCounter = 0;
        }

        body.setLinearVelocity(velocityX * speed, body.getLinearVelocity().y);
    }

    public Body getBody() {
        return body;
    }



}

