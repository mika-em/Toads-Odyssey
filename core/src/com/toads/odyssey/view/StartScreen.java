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
 * Represents the start screen.
 *
 * @author Mika, Joanne
 * @version 2023
 */
public class StartScreen implements Screen {
    private final ToadsOdyssey game;
    private final Viewport viewport;
    private TextureRegion region;
    private float stateTimer;

    /**
     * Constructs a start screen.
     * @param game the game instance
     */
    public StartScreen(final ToadsOdyssey game) {
        this.game = game;
        viewport = new StretchViewport(ToadsOdyssey.SCREEN_WIDTH / ToadsOdyssey.PPM,
                ToadsOdyssey.SCREEN_HEIGHT / ToadsOdyssey.PPM);
        viewport.apply();
        stateTimer = 0;
        region = AssetsLoader.instance.getIntroScreenAssets().introAnimation.getKeyFrame(stateTimer, true);
    }

    /**
     * Returns the current frame of the animation.
     * @param delta the time between frames
     * @return the current frame of the animation
     */
    private TextureRegion getFrame(final float delta) {
        stateTimer += delta;
        return AssetsLoader.instance.getIntroScreenAssets().introAnimation.getKeyFrame(stateTimer, true);
    }

    /**
     * Updates the start screen with the current frame and keypress event.
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
     * Handles the key pressed event to start the game.
     */
    private void handleKeyPressed() {
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        if (spacePressed) {
            game.setScreen(new Level1(game));
            dispose();
        }
    }

    @Override
    public void show() {
    }

    /**
     * Updates the start screen per unit of time.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(final float delta) {
        update(delta);
    }

    /**
     * Resizes the start screen.
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
     * Disposes the start screen.
     */
    @Override
    public void dispose() {
        region.getTexture().dispose();
    }
}
