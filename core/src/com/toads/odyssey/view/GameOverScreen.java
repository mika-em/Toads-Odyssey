package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.util.AssetsLoader;

/**
 * Represents the game over screen.
 *
 * @author Mika, Joanne
 * @version 2023
 */
public class GameOverScreen implements Screen {
    private final ToadsOdyssey game;
    private final Viewport viewport;
    private TextureRegion region;
    private float stateTimer;

    /**
     * Constructs a game over screen.
     * @param game the game instance
     */
    public GameOverScreen(final ToadsOdyssey game) {
        this.game = game;
        viewport = new StretchViewport(ToadsOdyssey.SCREEN_WIDTH / ToadsOdyssey.PPM,
                ToadsOdyssey.SCREEN_HEIGHT / ToadsOdyssey.PPM);
        viewport.apply();
        stateTimer = 0;
        region = AssetsLoader.getInstance().getGameOverScreenAssets().gameOverAnimation.getKeyFrame(stateTimer, false);
    }

    /**
     * Returns the current frame of the animation.
     * @param delta the time between frames
     * @return the current frame of the animation
     */
    private TextureRegion getFrame(final float delta) {
        stateTimer += delta;
        return AssetsLoader.getInstance().getGameOverScreenAssets().gameOverAnimation.getKeyFrame(stateTimer, false);
    }

    /**
     * Updates the game over screen with the current frame and keypress event.
     * @param deltaTime the time between frames
     */
    private void update(final float deltaTime) {
        region = getFrame(deltaTime);
        handleKeyPressed();
        game.batch.begin();
        game.batch.draw(region, 0, 0, ToadsOdyssey.SCREEN_WIDTH, ToadsOdyssey.SCREEN_HEIGHT);
        game.batch.end();
    }

    /**
     * Handles the key pressed event to exit the game.
     */
    private void handleKeyPressed() {
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
        if (spacePressed) {
            Gdx.app.exit();
        }
    }
    @Override
    public void show() {
    }

    /**
     * Renders the game over screen.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(final float delta) {
        update(delta);
    }

    /**
     * Resizes the game over screen.
     * @param width the width of the screen
     * @param height the height of the screen
     */
    @Override
    public void resize(final int width, final int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    /**
     * Disposes the game over screen.
     */
    @Override
    public void dispose() {
        region.getTexture().dispose();
    }
}
