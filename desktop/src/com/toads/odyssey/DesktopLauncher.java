package com.toads.odyssey;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Runs the game on desktop.
 *
 * @author Joanne, Mika
 * @version 2023
 */
public final class DesktopLauncher {
    /**
     * The width of the screen.
     */
    public static final int SCREEN_WIDTH = 1280;
    /**
     * The height of the screen.
     */
    public static final int SCREEN_HEIGHT = 720;

    /**
     * Private constructor to prevent instantiation.
     */
    private DesktopLauncher() {
    }

    /**
     * Drives the game on desktop.
     *
     * @param arg the arguments
     */
    public static void main(final String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
        config.setTitle("Toad's Odyssey");
        new Lwjgl3Application(new ToadsOdyssey(), config);
    }
}
