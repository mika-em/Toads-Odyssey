package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.toads.odyssey.util.AssetsLoader;

public class Hud {
    private AssetsLoader assetsLoader;
    private SpriteBatch spriteBatch;
    private int coinCount;
    private final float numberScale = 1.3f;
    private final float digitSpacing = 2;
    private final float maxCoinCountWidth;
    private BitmapFont pauseFont;

    public Hud(AssetsLoader assetsLoader, SpriteBatch spriteBatch) {
        this.assetsLoader = assetsLoader;
        maxCoinCountWidth = calculateMaxWidth(999);
        if (spriteBatch == null) {
            Gdx.app.log("Hud", "SpriteBatch is null");
        }
        this.spriteBatch = spriteBatch;

        // Load the BitmapFont
//        Gdx.app.log("Hud", "Creating default BitmapFont");
        pauseFont = new BitmapFont(Gdx.files.internal("assets/font.fnt"));
        if (pauseFont == null) {
            Gdx.app.log("Hud", "pauseFont is null after instantiation");
        } else {
            Gdx.app.log("Hud", "pauseFont successfully created");
        }
        pauseFont.getData().setScale(0.3f);
        pauseFont.setColor(Color.DARK_GRAY);

    }

    public void updateCoinCount(int newCount) {
        coinCount = newCount;
    }

    private float calculateMaxWidth(int maxNumber) {
        Array<TextureRegion> maxDigits = assetsLoader.getNumberTextures(maxNumber);
        return getTotalWidth(maxDigits) * numberScale;
    }
    public void render() {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Array<TextureRegion> digitTextures = assetsLoader.getNumberTextures(coinCount);
        float totalWidth = getTotalWidth(digitTextures) * numberScale;
        float padding = 50;
        float x = Gdx.graphics.getWidth() - padding - maxCoinCountWidth;
        float y = padding;

        TextureRegion coinTexture = AssetsLoader.CoinAssets.getCoinTexture();
        float coinX = x - coinTexture.getRegionWidth() * numberScale - digitSpacing;
        float coinY = y - 1;

        spriteBatch.begin();
        spriteBatch.draw(coinTexture, coinX, coinY, coinTexture.getRegionWidth() * 1.1f, coinTexture.getRegionHeight() * 1.1f);
        pauseFont.draw(spriteBatch, "PAUSE", padding, Gdx.graphics.getHeight() - padding);
        for (TextureRegion digit : digitTextures) {
            float scaledWidth = digit.getRegionWidth() * numberScale;
            float scaledHeight = digit.getRegionHeight() * numberScale;
            spriteBatch.draw(digit, x, y, scaledWidth, scaledHeight);
            x += scaledWidth + digitSpacing;
        }

        spriteBatch.end();

        if (Gdx.gl.glGetError() != GL20.GL_NO_ERROR) {
            Gdx.app.log("Hud", "OpenGL error during rendering");
        }
    }

    private float getTotalWidth(Array<TextureRegion> digits) {
        float totalWidth = 0;
        for (TextureRegion digit : digits) {
            totalWidth += (digit.getRegionWidth() + digitSpacing) * numberScale;
        }
        return totalWidth - digitSpacing * numberScale;
    }
}
