package com.toads.odyssey.util;

/**
 * Constants used throughout the game.
 *
 * @author Mika, Joanne
 * @version 2023
 */
public final class Constants {
    /**
     * The path to the sprite sheet atlas.
     */
    public static final String SPRITE_SHEET_PATH = "atlas_files/spriteSheet.atlas";
    /**
     * The path to the coin atlas.
     */
    public static final String COIN_ATLAS_PATH = "atlas_files/coin.atlas";
    /**
     * The path to the number atlas.
     */
    public static final String NUMBERS_ATLAS_PATH = "atlas_files/numbers.atlas";
    /**
     * The path to the start screen atlas.
     */
    public static final String INTRO_SCREEN_ATLAS_PATH = "screens/intro.atlas";
    /**
     * The path to the game over screen atlas.
     */
    public static final String GAME_OVER_SCREEN_ATLAS_PATH = "screens/gameover.atlas";
    /**
     * The path to the game won screen atlas.
     */
    public static final String GAME_WON_SCREEN_ATLAS_PATH = "screens/gamewon.atlas";
    /**
     * The path to the mushroom atlas.
     */
    public static final String MUSHROOM_ATLAS_PATH = "atlas_files/mushroom.atlas";
    /**
     * The path to the player hurt atlas.
     */
    public static final String PLAYER_HURT_ATLAS_PATH = "atlas_files/frog_hurt.atlas";
    /**
     * The array storing all the atlas paths to load by the asset manager.
     */
    public static final String[] ATLAS_PATHS = {
            SPRITE_SHEET_PATH,
            COIN_ATLAS_PATH,
            NUMBERS_ATLAS_PATH,
            INTRO_SCREEN_ATLAS_PATH,
            GAME_OVER_SCREEN_ATLAS_PATH,
            GAME_WON_SCREEN_ATLAS_PATH,
            MUSHROOM_ATLAS_PATH,
            PLAYER_HURT_ATLAS_PATH
    };
    /**
     * The path to the full_heart image.
     */
    public static final String FULL_HEART_TEXTURE_PATH = "tiles/full_heart.png";
    /**
     * The path to the empty_heart image.
     */
    public static final String EMPTY_HEART_TEXTURE_PATH = "tiles/empty_heart.png";
    /**
     * Frame duration of 0.15f.
     */
    public static final float FAST_FRAME_DURATION = 0.15f;
    /**
     * Frame duration of 0.4f, average speed.
     */
    public static final float MEDIUM_FRAME_DURATION = 0.4f;
    /**
     * Frame duration of 0.5f, a bit slower than the average.
     */
    public static final float SLOWER_FRAME_DURATION = 0.5f;
    /**
     * How fast player moves horizontally when external force is applied, the higher the faster.
     */
    public static final float HORIZONTAL_LINEAR_IMPULSE = 1.5f;
    /**
     * How fast player moves vertically when external force is applied, the higher the faster.
     */
    public static final float VERTICAL_LINEAR_IMPULSE = 4f;
    /**
     *The intensity of the knock back force applied to the player when hit with a mushroom.
     */
    public static final float KNOCK_BACK_INTENSITY = 9f;
    /**
     * Player has 3 lives by default.
     */
    public static final int DEFAULT_LIVES = 3;
    /**
     * An integer of the value 2.
     */
    public static final int TWO = 2;
    /**
     * An integer of the value 4.
     */
    public static final int FOUR = 4;
    /**
     * An integer of the value 6.
     */
    public static final int SIX = 6;
    /**
     * An integer of the value 11.
     */
    public static final int ELEVEN = 11;
    /**
     * An integer of the value 15.
     */
    public static final int FIFTEEN = 15;
    /**
     * An integer of the value 17.
     */
    public static final int SEVENTEEN = 17;
    /**
     * An integer of the value 18.
     */
    public static final int EIGHTEEN = 18;
    /**
     * An integer of the value 32.
     */
    public static final int THIRTY_TWO = 32;
    /**
     * The duration of each time step in the physical simulation.
     * Smaller value = more precise but higher computation load and vice versa.
     */
    public static final float TIME_STEP = 1 / 60f;
    /**
     * The number of velocity iterations in the physical simulation.
     */
    public static final int VELOCITY_ITERATIONS = 60;
    /**
     * The number of position iterations in the physical simulation.
     */
    public static final int POSITION_ITERATIONS = 2;

    /**
     * private constructor.
     */
    private Constants() {
    }

}
