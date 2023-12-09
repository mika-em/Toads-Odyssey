package com.toads.odyssey.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Loads all the assets used in the game.
 *
 * @author Mika, Joanne
 * @version 2023
 */
public final class AssetsLoader implements Disposable {
    /**
     * An AssetsLoader instance.
     */
    private static final AssetsLoader LOADER = new AssetsLoader();
    private final PlayerAssets playerAssets;
    private final CoinAssets coinAssets;
    private final IntroScreenAssets introScreenAssets;
    private final GameOverScreenAssets gameOverScreenAssets;
    private final GameWonScreenAssets gameWonScreenAssets;
    private final TextureAtlas numberAtlas;
    private final MushroomAssets mushroomAssets;
    private final AssetManager manager;
    private final Texture fullHeartTexture;
    private final Texture emptyHeartTexture;
    private final PlayerHurtAssets playerHurtAssets;

    /**
     * Constructs the AssetsLoader.
     */
    private AssetsLoader() {
        this.manager = new AssetManager();
        for (String path : Constants.ATLAS_PATHS) {
            manager.load(path, TextureAtlas.class);
        }
        manager.finishLoading();
        playerAssets = new PlayerAssets(manager.get(Constants.SPRITE_SHEET_PATH, TextureAtlas.class));
        coinAssets = new CoinAssets(manager.get(Constants.COIN_ATLAS_PATH, TextureAtlas.class));
        introScreenAssets = new IntroScreenAssets(manager.get(Constants.INTRO_SCREEN_ATLAS_PATH, TextureAtlas.class));
        gameOverScreenAssets = new GameOverScreenAssets(manager.get(Constants.GAME_OVER_SCREEN_ATLAS_PATH,
                TextureAtlas.class));
        gameWonScreenAssets = new GameWonScreenAssets(manager.get(Constants.GAME_WON_SCREEN_ATLAS_PATH,
                TextureAtlas.class));
        mushroomAssets = new MushroomAssets(manager.get(Constants.MUSHROOM_ATLAS_PATH, TextureAtlas.class));
        playerHurtAssets = new PlayerHurtAssets(manager.get(Constants.PLAYER_HURT_ATLAS_PATH, TextureAtlas.class));
        numberAtlas = manager.get(Constants.NUMBERS_ATLAS_PATH, TextureAtlas.class);
        fullHeartTexture = new Texture(Constants.FULL_HEART_TEXTURE_PATH);
        emptyHeartTexture = new Texture(Constants.EMPTY_HEART_TEXTURE_PATH);
    }

    /**
     * Returns the AssetsLoader instance.
     * @return the AssetsLoader instance
     */
    public static AssetsLoader getInstance() {
        return LOADER;
    }

    /**
     * Returns the CoinAssets.
     * @return the CoinAssets
     */
    public CoinAssets getCoinAssets() {
        TextureAtlas atlas = manager.get(Constants.SPRITE_SHEET_PATH, TextureAtlas.class);
        return new CoinAssets(atlas);
    }

    /**
     * Returns the TextureRegion for the number HUD.
     * @param number the number to get the TextureRegion for
     * @return the TextureRegion for the number
     */
    public Array<TextureRegion> getNumberTextures(final int number) {
        Array<TextureRegion> digits = new Array<>();
        String numberStr = String.valueOf(number);
        for (char digit : numberStr.toCharArray()) {
            String regionName = "numbers-" + digit;
            digits.add(numberAtlas.findRegion(regionName));
        }
        return digits;
    }

    /**
     * Returns the Texture for the Heart.
     * @param full whether the heart is full or not
     * @return the full or empty heart Texture
     */
    public Texture getHeartTexture(final boolean full) {
        if (full) {
            return fullHeartTexture;
        } else {
            return emptyHeartTexture;
        }
    }

    /**
     * Disposes the AssetManager.
     */
    @Override
    public void dispose() {
        manager.dispose();
        if (fullHeartTexture != null) {
            fullHeartTexture.dispose();
        }
        if (emptyHeartTexture != null) {
            emptyHeartTexture.dispose();
        }
    }
    /**
     * Hurt Assets for the Player.
     */
    public static class PlayerHurtAssets {
        /**
         * Hurt Animation for the Player.
         */
        public final Animation<TextureAtlas.AtlasRegion> hurtAnimation;

        /**
         * Constructs the PlayerHurtAssets.
         * @param atlas the TextureAtlas
         */
        public PlayerHurtAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> hurtFrames = new Array<>();
            for (int i = 1; i <= Constants.FOUR; i++) {
                TextureAtlas.AtlasRegion region = atlas.findRegion("frog_hurt" + i);
                    hurtFrames.add(region);
            }
            hurtAnimation = new Animation<>(Constants.SLOWER_FRAME_DURATION, hurtFrames, Animation.PlayMode.LOOP);
        }
    }

    /**
     * Assets for the Player.
     */
    public static class PlayerAssets {
        /**
         * Idle animation for the Player.
         */
        public final Animation<TextureAtlas.AtlasRegion> idleAnimation;
        /**
         * Movement animation for the Player.
         */
        public final Animation<TextureAtlas.AtlasRegion> moveAnimation;
        /**
         * Jump animation for the Player.
         */
        public final Animation<TextureAtlas.AtlasRegion> jumpAnimation;

        /**
         * Constructs the PlayerAssets.
         * @param atlas the TextureAtlas
         */
        public PlayerAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> idleFrames = new Array<>();
            for (int i = 1; i <= Constants.FOUR; i++) {
                idleFrames.add(atlas.findRegion("frog_idle", i));
            }
            idleAnimation = new Animation<>(Constants.MEDIUM_FRAME_DURATION, idleFrames, Animation.PlayMode.LOOP);

            Array<TextureAtlas.AtlasRegion> moveFrames = new Array<>();
            for (int i = 1; i <= Constants.SIX; i++) {
                moveFrames.add(atlas.findRegion("frog_walk", i));
            }
            moveAnimation = new Animation<>(Constants.MEDIUM_FRAME_DURATION, moveFrames, Animation.PlayMode.LOOP);

            Array<TextureAtlas.AtlasRegion> jumpFrames = new Array<>();
            for (int i = 1; i <= 2; i++) {
                jumpFrames.add(atlas.findRegion("frog_jump", i));
            }
            jumpAnimation = new Animation<>(Constants.MEDIUM_FRAME_DURATION, jumpFrames, Animation.PlayMode.LOOP);
        }
    }

    /**
     * Assets for the Coin.
     */
    public static class CoinAssets {
        /**
         * Animation for the Coin.
         */
        public static Animation<TextureAtlas.AtlasRegion> coinAnimation = null;

        /**
         * Constructs the CoinAssets.
         * @param atlas the TextureAtlas
         */
        public CoinAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> coinFrames = new Array<>();
            for (int i = 1; i <= Constants.FOUR; i++) {
                coinFrames.add(atlas.findRegion("coin_yellow", i));
            }
            coinAnimation = new Animation<>(Constants.FAST_FRAME_DURATION, coinFrames, Animation.PlayMode.LOOP);
        }

        /**
         * Returns the TextureRegion for the Coin.
         * @return the TextureRegion for the Coin
         */
        public static TextureRegion getCoinTexture() {
            return coinAnimation.getKeyFrame(0);
        }
    }

    /**
     * Returns the TextureRegion for the Coin.
     * @return the TextureRegion for the Coin
     */
    public Animation<TextureAtlas.AtlasRegion> getCoinAnimation() {
        return CoinAssets.coinAnimation;
    }

    /**
     * Assets for the Mushroom.
     */
    public static class MushroomAssets {
        /**
         * Animation for the Mushroom.
         */
        public static Animation<TextureAtlas.AtlasRegion> mushroomAnimation = null;

        /**
         * Constructs the MushroomAssets.
         * @param atlas the TextureAtlas
         */
        public MushroomAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> mushroomFrames = new Array<>();
            for (int i = 1; i <= Constants.ELEVEN; i++) {
                TextureAtlas.AtlasRegion region = atlas.findRegion("mushroom" + i);
                if (region != null) {
                    mushroomFrames.add(region);
                } else {
                    System.out.println("Mushroom frame " + i + " not found.");
                }
            }
            mushroomAnimation = new Animation<>(Constants.FAST_FRAME_DURATION, mushroomFrames, Animation.PlayMode.LOOP);
        }
    }

    /**
     * Assets for the IntroScreen.
     */
    public static class IntroScreenAssets {
        /**
         * Animation for the IntroScreen.
         */
        public final Animation<TextureAtlas.AtlasRegion> introAnimation;

        /**
         * Constructs the IntroScreenAssets.
         * @param atlas the TextureAtlas
         */
        public IntroScreenAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> introFrames = new Array<>();
            for (int i = 0; i <= Constants.EIGHTEEN; i++) {
                introFrames.add(atlas.findRegion("frame", i));
            }
            introAnimation = new Animation<>(Constants.MEDIUM_FRAME_DURATION, introFrames, Animation.PlayMode.LOOP);
        }
    }

    /**
     * Assets for the GameOverScreen.
     */
    public static class GameOverScreenAssets {
        /**
         * Animation for the GameOverScreen.
         */
        public final Animation<TextureAtlas.AtlasRegion> gameOverAnimation;

        /**
         * Constructs the GameOverScreenAssets.
         * @param atlas the TextureAtlas
         */
        public GameOverScreenAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> gameOverFrames = new Array<>();
            for (int i = 0; i <= Constants.FIFTEEN; i++) {
                gameOverFrames.add(atlas.findRegion("frame", i));
            }
            gameOverAnimation = new Animation<>(Constants.MEDIUM_FRAME_DURATION, gameOverFrames,
                    Animation.PlayMode.NORMAL);
        }
    }

    /**
     * Assets for the GameWonScreen.
     */
    public static class GameWonScreenAssets {
        /**
         * Animation for the GameWonScreen.
         */
        public final Animation<TextureAtlas.AtlasRegion> gameWonAnimation;

        /**
         * Constructs the GameWonScreenAssets.
         * @param atlas the TextureAtlas
         */
        public GameWonScreenAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> gameWonFrames = new Array<>();
            for (int i = 0; i <= Constants.EIGHTEEN; i++) {
                gameWonFrames.add(atlas.findRegion("frame", i));
            }
            gameWonAnimation = new Animation<>(Constants.MEDIUM_FRAME_DURATION, gameWonFrames,
                    Animation.PlayMode.NORMAL);
        }
    }

    /**
     * Returns the PlayerAssets.
     *
     * @return the PlayerAssets
     */
    public PlayerAssets getPlayerAssets() {
        return playerAssets;
    }

    /**
     * Returns the IntroScreenAssets.
     *
     * @return the IntroScreenAssets
     */
    public IntroScreenAssets getIntroScreenAssets() {
        return introScreenAssets;
    }

    /**
     * Returns the GameOverScreenAssets.
     *
     * @return the GameOverScreenAssets
     */
    public GameOverScreenAssets getGameOverScreenAssets() {
        return gameOverScreenAssets;
    }

    /**
     * Returns the GameWonScreenAssets.
     *
     * @return the GameWonScreenAssets
     */
    public GameWonScreenAssets getGameWonScreenAssets() {
        return gameWonScreenAssets;
    }

    /**
     * Returns the PlayerHurtAssets.
     *
     * @return the PlayerHurtAssets
     */
    public PlayerHurtAssets getPlayerHurtAssets() {
        return playerHurtAssets;
    }

    /**
     * Returns the string representation of the AssetsLoader.
     * @return a string
     */
    @Override
    public String toString() {
        return "AssetsLoader{" + "playerAssets=" + playerAssets + ", coinAssets=" + coinAssets + ", introScreenAssets="
                + introScreenAssets + ", gameOverScreenAssets=" + gameOverScreenAssets + ", gameWonScreenAssets="
                + gameWonScreenAssets + ", numberAtlas=" + numberAtlas + ", mushroomAssets=" + mushroomAssets
                + ", manager=" + manager + ", fullHeartTexture=" + fullHeartTexture + ", emptyHeartTexture="
                + emptyHeartTexture + ", playerHurtAssets=" + playerHurtAssets + '}';
    }
}
