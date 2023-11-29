package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.toads.odyssey.util.AssetsLoader;

public class Hud {
    private AssetsLoader assetsLoader;
    private SpriteBatch spriteBatch;
    private int coinCount;
    private final float numberScale = 1.3f;
    private final float padding = 50;
    private final float digitSpacing = 2;
    private final float maxCoinCountWidth;

    public Hud(AssetsLoader assetsLoader, SpriteBatch spriteBatch) {
        this.assetsLoader = assetsLoader;
        this.spriteBatch = spriteBatch;
        maxCoinCountWidth = calculateMaxWidth(999);
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
        float x = Gdx.graphics.getWidth() - padding - maxCoinCountWidth;
        float y = padding;

        TextureRegion coinTexture = AssetsLoader.CoinAssets.getCoinTexture();
        float coinX = x - coinTexture.getRegionWidth() * numberScale - digitSpacing;
        float coinY = y - 1;

        spriteBatch.begin();
        spriteBatch.draw(coinTexture, coinX, coinY, coinTexture.getRegionWidth() * 1.1f, coinTexture.getRegionHeight() * 1.1f);

        for (TextureRegion digit : digitTextures) {
            float scaledWidth = digit.getRegionWidth() * numberScale;
            float scaledHeight = digit.getRegionHeight() * numberScale;
            spriteBatch.draw(digit, x, y, scaledWidth, scaledHeight);
            x += scaledWidth + digitSpacing;
        }

        spriteBatch.end();
    }

    private float getTotalWidth(Array<TextureRegion> digits) {
        float totalWidth = 0;
        for (TextureRegion digit : digits) {
            totalWidth += (digit.getRegionWidth() + digitSpacing) * numberScale;
        }
        return totalWidth - digitSpacing * numberScale;
    }
}
