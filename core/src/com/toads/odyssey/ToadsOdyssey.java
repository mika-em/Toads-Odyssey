package com.toads.odyssey;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.toads.odyssey.util.AssetsLoader;

public class ToadsOdyssey extends ApplicationAdapter {
	SpriteBatch batch;
    public AssetsLoader assetsLoader;
    private float elapsedTime = 0;
	Texture img;
	@Override
	public void create () {
		batch = new SpriteBatch();
        assetsLoader = new AssetsLoader();
		img = new Texture("forest.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		batch.draw(img, 0, 0, screenWidth, screenHeight);

        elapsedTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = assetsLoader.playerAssets.idleAnimation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame, 100, 100);
		batch.end();
	}
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
        assetsLoader.dispose();
	}
}
