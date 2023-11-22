package com.toads.odyssey.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class AssetsLoader implements Disposable {
    private AssetManager manager;
    public final PlayerAssets playerAssets;
    public AssetsLoader() {
        this.manager = new AssetManager();
        manager.load("spriteSheet.atlas", TextureAtlas.class);
        manager.finishLoading();
        TextureAtlas atlas = manager.get("spriteSheet.atlas", TextureAtlas.class);
        playerAssets = new PlayerAssets(atlas);
    }

    public class PlayerAssets {
        public final Animation<TextureAtlas.AtlasRegion> idleAnimation;
        public PlayerAssets(TextureAtlas atlas) {
            Array<TextureAtlas.AtlasRegion> idleFrames = new Array<>();
            for (int i = 1; i <= 4; i++) {
                idleFrames.add(atlas.findRegion("frog_idle", i));
            }
            idleAnimation = new Animation<>(0.1f, idleFrames, Animation.PlayMode.LOOP);
        }
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
