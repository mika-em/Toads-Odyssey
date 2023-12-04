package com.toads.odyssey.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class AssetsLoader implements Disposable {
    public AssetManager manager;
    public final PlayerAssets playerAssets;
    public final CoinAssets coinAssets;
    public final IntroScreenAssets introScreenAssets;
    public final GameOverScreenAssets gameOverScreenAssets;
    public final GameWonScreenAssets gameWonScreenAssets;
    public final DoorAssets doorAssets;
    public final TextureAtlas numberAtlas;;
    private Texture fullHeartTexture;
    private Texture emptyHeartTexture;
    public static final AssetsLoader instance = new AssetsLoader();
    public AssetsLoader() {
        this.manager = new AssetManager();
        manager.load("atlas_files/spriteSheet.atlas", TextureAtlas.class);
        manager.load("atlas_files/coin.atlas", TextureAtlas.class);
        manager.load("atlas_files/numbers.atlas", TextureAtlas.class);
        manager.load("screens/intro.atlas", TextureAtlas.class);
        manager.load("screens/gameover.atlas", TextureAtlas.class);
        manager.load("screens/gamewon.atlas", TextureAtlas.class);
        manager.load("atlas_files/door.atlas", TextureAtlas.class);
        manager.finishLoading();
        TextureAtlas atlas = manager.get("atlas_files/spriteSheet.atlas", TextureAtlas.class);
        TextureAtlas coinAtlas = manager.get("atlas_files/coin.atlas", TextureAtlas.class);
		TextureAtlas introAtlas = manager.get("screens/intro.atlas", TextureAtlas.class);
        TextureAtlas gameOverAtlas = manager.get("screens/gameover.atlas", TextureAtlas.class);
        TextureAtlas gameWonAtlas = manager.get("screens/gamewon.atlas", TextureAtlas.class);
        TextureAtlas doorAtlas = manager.get("atlas_files/door.atlas", TextureAtlas.class);
        playerAssets = new PlayerAssets(atlas);
        coinAssets = new CoinAssets(coinAtlas);
        introScreenAssets = new IntroScreenAssets(introAtlas);
        gameOverScreenAssets = new GameOverScreenAssets(gameOverAtlas);
        gameWonScreenAssets = new GameWonScreenAssets(gameWonAtlas);
        doorAssets = new DoorAssets(doorAtlas);
        numberAtlas = manager.get("atlas_files/numbers.atlas", TextureAtlas.class);
        fullHeartTexture = new Texture("assets/tiles/full_heart.png");
        emptyHeartTexture = new Texture("assets/tiles/empty_heart.png");
    }
    public static class PlayerAssets {
        public final Animation<TextureAtlas.AtlasRegion> idleAnimation;
        public final Animation<TextureAtlas.AtlasRegion> moveAnimation;
        public final Animation<TextureAtlas.AtlasRegion> jumpAnimation;
        public PlayerAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> idleFrames = new Array<>();
            for (int i = 1; i <= 4; i++) {
                idleFrames.add(atlas.findRegion("frog_idle", i));
            }
            idleAnimation = new Animation<>(0.1f, idleFrames, Animation.PlayMode.LOOP);

            Array<TextureAtlas.AtlasRegion> moveFrames = new Array<>();
            for (int i = 1; i <= 6; i++) {
                moveFrames.add(atlas.findRegion("frog_walk", i));
            }
            moveAnimation = new Animation<>(0.1f, moveFrames, Animation.PlayMode.LOOP);

            Array<TextureAtlas.AtlasRegion> jumpFrames = new Array<>();
            for (int i = 1; i <= 2; i++) {
                jumpFrames.add(atlas.findRegion("frog_jump", i));
            }
            jumpAnimation = new Animation<>(0.1f, jumpFrames, Animation.PlayMode.LOOP);
        }
    }

    public static class CoinAssets {
        public static Animation<TextureAtlas.AtlasRegion> coinAnimation = null;
        public CoinAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> coinFrames = new Array<>();
            for (int i = 1; i <= 4; i++) {
                coinFrames.add(atlas.findRegion("coin_yellow", i));
            }
            coinAnimation = new Animation<>(0.15f, coinFrames, Animation.PlayMode.LOOP);
        }

        public static TextureRegion getCoinTexture() {
            return coinAnimation.getKeyFrame(0);
        }

    }

    public CoinAssets getCoinAssets() {
        TextureAtlas atlas = manager.get("atlas_files/spriteSheet.atlas", TextureAtlas.class);
        return new CoinAssets(atlas);
    }

    public static class IntroScreenAssets {
        public final Animation<TextureAtlas.AtlasRegion> introAnimation;
        public IntroScreenAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> introFrames = new Array<>();
            for (int i = 0; i <= 18; i++) {
                introFrames.add(atlas.findRegion("frame", i));
            }
            introAnimation = new Animation<>(0.3f, introFrames, Animation.PlayMode.LOOP);
        }
    }

    public static class GameOverScreenAssets {
        public final Animation<TextureAtlas.AtlasRegion> gameOverAnimation;
        public GameOverScreenAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> gameOverFrames = new Array<>();
            for (int i = 0; i <= 15; i++) {
                gameOverFrames.add(atlas.findRegion("frame", i));
            }
            gameOverAnimation = new Animation<>(0.3f, gameOverFrames, Animation.PlayMode.NORMAL);
        }
    }

    public static class GameWonScreenAssets {
        public final Animation<TextureAtlas.AtlasRegion> gameWonAnimation;
        public GameWonScreenAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> gameWonFrames = new Array<>();
            for (int i = 0; i <= 18; i++) {
                gameWonFrames.add(atlas.findRegion("frame", i));
            }
            gameWonAnimation = new Animation<>(0.3f, gameWonFrames, Animation.PlayMode.NORMAL);
        }
    }

    public static class DoorAssets {
        public final Animation<TextureAtlas.AtlasRegion> doorAnimation;
        public DoorAssets(final TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> doorFrames = new Array<>();
            for (int i = 1; i <= 3; i++) {
                doorFrames.add(atlas.findRegion("door_open", i));
            }
            doorAnimation = new Animation<>(0.3f, doorFrames, Animation.PlayMode.NORMAL);
        }
    }

    public Array<TextureRegion> getNumberTextures(int number) {
        Array<TextureRegion> digits = new Array<>();
        String numberStr = String.valueOf(number);
        for (char digit : numberStr.toCharArray()) {
            String regionName = "numbers-" + digit;
            digits.add(numberAtlas.findRegion(regionName));
        }
        return digits;
    }
    public Texture getHeartTexture(boolean full) {
        return full ? fullHeartTexture : emptyHeartTexture;
    }

    @Override
    public void dispose() {
        manager.dispose();
        if (fullHeartTexture != null) fullHeartTexture.dispose();
        if (emptyHeartTexture != null) emptyHeartTexture.dispose();
    }

}