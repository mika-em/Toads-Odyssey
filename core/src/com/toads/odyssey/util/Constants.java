package com.toads.odyssey.util;

/**
 * Constants used throughout the game.
 *
 * @author Mika, Joanne
 * @version 2023
 */
public final class Constants {
    /**
     * private constructor to prevent instantiation.
     */
    private Constants() {
    }

    public static final String SPRITE_SHEET_PATH = "atlas_files/spriteSheet.atlas";
    public static final String COIN_ATLAS_PATH = "atlas_files/coin.atlas";
    public static final String NUMBERS_ATLAS_PATH = "atlas_files/numbers.atlas";
    public static final String INTRO_SCREEN_ATLAS_PATH = "screens/intro.atlas";
    public static final String GAME_OVER_SCREEN_ATLAS_PATH = "screens/gameover.atlas";
    public static final String GAME_WON_SCREEN_ATLAS_PATH = "screens/gamewon.atlas";
    public static final String MUSHROOM_ATLAS_PATH = "atlas_files/mushroom.atlas";
    public static final String PLAYER_HURT_ATLAS_PATH = "atlas_files/frog_hurt.atlas";
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
    public static final String FULL_HEART_TEXTURE_PATH = "assets/tiles/full_heart.png";
    public static final String EMPTY_HEART_TEXTURE_PATH = "assets/tiles/empty_heart.png";
    public static final float FAST_FRAME_DURATION = 0.15f;
    public static final float MEDIUM_FRAME_DURATION = 0.4f;
    public static final float SLOWER_FRAME_DURATION = 0.5f;
    public static final float HORIZONTAL_LINEAR_IMPULSE = 1.5f;
    public static final float VERTICAL_LINEAR_IMPULSE = 4f;
    public static final float KNOCK_BACK_INTENSITY = 7f;
    public static final int DEFAULT_LIVES = 3;
    public static final int TWO = 2;
    public static final int FOUR = 4;
    public static final int SIX = 6;
    public static final int ELEVEN = 11;
    public static final int FIFTEEN = 15;
    public static final int SEVENTEEN = 17;
    public static final int EIGHTEEN = 18;
    public static final int THIRTY_TWO = 32;



}
