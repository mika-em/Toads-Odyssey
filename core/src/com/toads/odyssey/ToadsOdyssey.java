package com.toads.odyssey;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.view.StartScreen;

/**
 * The main class of the game.
 *
 * @author Joanne, Mika
 * @version 2023
 */

public final class ToadsOdyssey extends Game {
    /**
     * The pixels per meter ratio.
     */
    public static final float PPM = 100f;
    /**
     * The width of the screen.
     */
    public static final int SCREEN_WIDTH = 1280;
    /**
     * The height of the screen.
     */
    public static final int SCREEN_HEIGHT = 720;
    /**
     * The batch of sprites.
     */
    private SpriteBatch batch;
    /**
     * The assets' loader.
     */
    private AssetsLoader assetsLoader;

    /**
     * Creates the game.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        assetsLoader = AssetsLoader.getInstance();
        setScreen(new StartScreen(this));
    }

    /**
     * Renders the game.
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Disposes the game.
     */
    @Override
    public void dispose() {
        batch.dispose();
        assetsLoader.dispose();
    }

    /**
     * Gets the batch of sprites.
     *
     * @return the batch of sprites
     */
    public SpriteBatch getBatch() {
        return batch;
    }
}
