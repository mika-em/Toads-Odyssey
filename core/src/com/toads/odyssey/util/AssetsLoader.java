package com.toads.odyssey.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class AssetsLoader implements Disposable {
    private AssetManager manager;
    public final PlayerAssets playerAssets;
    public static final AssetsLoader instance = new AssetsLoader();
    public AssetsLoader() {
        this.manager = new AssetManager();
        manager.load("spriteSheet.atlas", TextureAtlas.class);
        manager.finishLoading();
        TextureAtlas atlas = manager.get("spriteSheet.atlas", TextureAtlas.class);
        playerAssets = new PlayerAssets(atlas);
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
    @Override
    public void dispose() {
        manager.dispose();
    }

}
