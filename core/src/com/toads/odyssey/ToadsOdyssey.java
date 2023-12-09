package com.toads.odyssey;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.view.StartScreen;

public class ToadsOdyssey extends Game {
    public static final float PPM = 100f;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public SpriteBatch batch;
    public AssetsLoader assetsLoader;
    @Override
    public void create() {
        batch = new SpriteBatch();
        assetsLoader = AssetsLoader.getInstance();
        setScreen(new StartScreen(this));
    }
    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
        batch.dispose();
        assetsLoader.dispose();
    }
}