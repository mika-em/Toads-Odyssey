package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.toads.odyssey.util.AssetsLoader;

public class Hud {
    private final AssetsLoader assetsLoader;
    private final SpriteBatch spriteBatch;
    private final float numberScale = 1.3f;
    private final float digitSpacing = 2;
    private final float maxCoinCountWidth;
    private final BitmapFont pauseFont;
    private final Rectangle pauseTextBounds;
    private final ShapeRenderer shapeRenderer;
    private int coinCount;
    private boolean isPaused = false;
    public Hud(AssetsLoader assetsLoader, SpriteBatch spriteBatch) {
        this.assetsLoader = assetsLoader;
        maxCoinCountWidth = calculateMaxWidth();
        if (spriteBatch == null) {
            Gdx.app.log("Hud", "SpriteBatch is null");
        }
        this.spriteBatch = spriteBatch;
        shapeRenderer = new ShapeRenderer();
        pauseFont = new BitmapFont(Gdx.files.internal("assets/font.fnt"));
        pauseFont.getData().setScale(0.3f);
        pauseFont.setColor(Color.DARK_GRAY);
        GlyphLayout layout = new GlyphLayout(pauseFont, "PAUSE");
        float pauseTextWidth = layout.width;
        float pauseTextHeight = layout.height;

        float x = 20;
        float y = Gdx.graphics.getHeight() - 20;
        pauseTextBounds = new Rectangle(x + 20, y - pauseTextHeight - 40, pauseTextWidth + 20, pauseTextHeight + 20);
    }

    public void updateCoinCount(int newCount) {
        coinCount = newCount;
    }

    private float calculateMaxWidth() {
        Array<TextureRegion> maxDigits = assetsLoader.getNumberTextures(999);
        return getTotalWidth(maxDigits) * numberScale;
    }

    public void render(int playerLives, int maxLives) {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Array<TextureRegion> digitTextures = assetsLoader.getNumberTextures(coinCount);
        float padding = 50;
        float x = Gdx.graphics.getWidth() - padding - maxCoinCountWidth;

        TextureRegion coinTexture = AssetsLoader.CoinAssets.getCoinTexture();
        TextureRegion mushroomTexture = AssetsLoader.MushroomAssets.getMushroomTexture();
        float coinX = x - coinTexture.getRegionWidth() * numberScale - digitSpacing;
        float coinY = padding - 1;
        spriteBatch.begin();
        String pauseText = isPaused() ? "UNPAUSE" : "PAUSE";
        GlyphLayout layout = new GlyphLayout(pauseFont, pauseText);
        float pauseTextWidth = layout.width;
        float textX = padding + (pauseText.equals("PAUSE") ? 0 : (pauseTextWidth - layout.width) / 2);
        float textY = Gdx.graphics.getHeight() - padding;
        pauseFont.draw(spriteBatch, pauseText, textX, textY);
        spriteBatch.draw(coinTexture, coinX, coinY, coinTexture.getRegionWidth() * 1.1f, coinTexture.getRegionHeight() * 1.1f);


        for (TextureRegion digit : digitTextures) {
            float scaledWidth = digit.getRegionWidth() * numberScale;
            float scaledHeight = digit.getRegionHeight() * numberScale;
            spriteBatch.draw(digit, x, padding, scaledWidth, scaledHeight);
            x += scaledWidth + digitSpacing;
        }

        float heartSize = 45;
        float heartSpacing = 10;
        float startX = Gdx.graphics.getWidth() - (heartSize + heartSpacing) * maxLives;

        for (int i = 0; i < maxLives; i++) {
            Texture heartTexture = assetsLoader.getHeartTexture(i < playerLives);
            spriteBatch.draw(new TextureRegion(heartTexture), startX + i * (heartSize + heartSpacing) - 30, Gdx.graphics.getHeight() - heartSize - 35, heartSize, heartSize);
        }


        spriteBatch.end();
        pauseTextBounds.set(textX, textY - layout.height, pauseTextWidth, layout.height);
        shapeRenderer.end();
    }


    private float getTotalWidth(Array<TextureRegion> digits) {
        float totalWidth = 0;
        for (TextureRegion digit : digits) {
            totalWidth += (digit.getRegionWidth() + digitSpacing) * numberScale;
        }
        return totalWidth - digitSpacing * numberScale;
    }

    public boolean checkPausePressed() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            if (pauseTextBounds.contains(touchPos.x, Gdx.graphics.getHeight() - touchPos.y)) {
                isPaused = !isPaused;
                return true;
            }
        }

        return false;
    }


    public boolean isPaused() {
        return isPaused;
    }

    public void dispose() {
        pauseFont.dispose();
        shapeRenderer.dispose();
    }
}
