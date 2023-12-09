package com.toads.odyssey.util;

import com.badlogic.gdx.physics.box2d.World;
import com.toads.odyssey.model.Player;
import com.toads.odyssey.view.LevelBase;

/**
 * Manages the game level and its objects.
 *
 * @author Mika, Joanne
 * @version 2023
 */
public final class LevelManager {
    private static LevelManager instance;
    private World world;
    private Player player;

    /**
     * Prevents instantiation.
     */
    private LevelManager() {
    }

    /**
     * Returns the instance of the LevelManager.
     * @return instance of the LevelManager
     */
    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    /**
     * Sets the level.
     * @param levelBase level to set
     */
    public void setLevelBase(final LevelBase levelBase) {
        this.world = levelBase.getWorld();
        this.player = levelBase.getPlayer();
    }

    /**
     * Updates the level.
     * @param deltaTime time between frames
     */
    public void update(final float deltaTime) {
        handleUserInput();
        world.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
        player.update(deltaTime);
    }

    /**
     * Handles user input for the player.
     */
    public void handleUserInput() {
        player.handleKeyPressed();
    }

    /**
     * Returns the string representation of LevelManager.
     * @return a string
     */
    @Override
    public String toString() {
        return "LevelManager{" + "world=" + world + ", player=" + player + '}';
    }
}
