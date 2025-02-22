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

/**
 * The HUD is the Heads-Up Display that shows the player's score, lives, and the pause button.
 *
 * @author Joanne, Mika
 * @version 2023
 */
public final class Hud {
    private static final float NUMBER_SCALE = 1.3f;
    private static final float DIGIT_SPACING = 2;
    private static final float PAUSE_TEXT_SCALE = 0.3f;
    private static final float COIN_PADDING = 50;
    private static final float HEART_SIZE = 45;
    private static final float HEART_SPACING = 10;
    private static final float PAUSE_TEXT_X_OFFSET = 20;
    private static final float PAUSE_TEXT_Y_OFFSET = 20;
    private static final float PAUSE_TEXT_HEIGHT_OFFSET = 40;
    private static final float COIN_SIZE_SCALE = 1.1f;
    private static final float HEART_X_OFFSET = 30;
    private static final float HEART_Y_OFFSET = 35;
    private static final int MAX_COIN_COUNT = 999;
    private final AssetsLoader assetsLoader;
    private final SpriteBatch spriteBatch;
    private final float maxCoinCountWidth;
    private final ShapeRenderer shapeRenderer;
    private BitmapFont pauseFont;
    private Rectangle pauseTextBounds;
    private int coinCount;
    private boolean isPaused = false;

    /**
     * Constructs a HUD.
     *
     * @param assetsLoader the assets loader
     * @param spriteBatch  the sprite batch
     */
    public Hud(final AssetsLoader assetsLoader, final SpriteBatch spriteBatch) {
        this.assetsLoader = assetsLoader;
        this.spriteBatch = spriteBatch;
        shapeRenderer = new ShapeRenderer();
        initializePauseFont();
        maxCoinCountWidth = calculateMaxCoinCountWidth();
    }

    private void initializePauseFont() {
        pauseFont = new BitmapFont(Gdx.files.internal("font.fnt"));
        pauseFont.getData().setScale(PAUSE_TEXT_SCALE);
        pauseFont.setColor(Color.DARK_GRAY);
        GlyphLayout layout = new GlyphLayout(pauseFont, "PAUSE");
        float pauseTextWidth = layout.width;
        float pauseTextHeight = layout.height;

        float y = Gdx.graphics.getHeight() - PAUSE_TEXT_Y_OFFSET;
        pauseTextBounds = new Rectangle(PAUSE_TEXT_X_OFFSET + PAUSE_TEXT_X_OFFSET,
                y - pauseTextHeight - PAUSE_TEXT_HEIGHT_OFFSET, pauseTextWidth + PAUSE_TEXT_X_OFFSET,
                pauseTextHeight + PAUSE_TEXT_X_OFFSET);
    }

    /**
     * Calculates the maximum width of the coin count.
     *
     * @return the maximum width of the coin count
     */
    private float calculateMaxCoinCountWidth() {
        Array<TextureRegion> maxDigits = assetsLoader.getNumberTextures(MAX_COIN_COUNT);
        return getTotalWidth(maxDigits) * NUMBER_SCALE;
    }

    /**
     * Updates the coin count.
     *
     * @param newCount the new coin count
     */
    public void updateCoinCount(final int newCount) {
        coinCount = newCount;
    }

    /**
     * Renders the HUD.
     *
     * @param playerLives the player's lives
     * @param maxLives    the maximum number of lives
     */
    public void render(final int playerLives, final int maxLives) {
        spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.begin();
        drawPauseText();
        drawCoinCount();
        drawLives(playerLives, maxLives);
        spriteBatch.end();
    }

    /**
     * Draws the pause or unpause text.
     */
    private void drawPauseText() {
        String pauseText;
        if (isPaused()) {
            pauseText = "UNPAUSE";
        } else {
            pauseText = "PAUSE";
        }

        GlyphLayout layout = new GlyphLayout(pauseFont, pauseText);
        float pauseTextWidth = layout.width;

        float textX;
        if (pauseText.equals("PAUSE")) {
            textX = COIN_PADDING;
        } else {
            textX = COIN_PADDING + (pauseTextWidth - layout.width) / DIGIT_SPACING;
        }

        float textY = Gdx.graphics.getHeight() - COIN_PADDING;
        pauseFont.draw(spriteBatch, pauseText, textX, textY);
        pauseTextBounds.set(textX, textY - layout.height, pauseTextWidth, layout.height);
    }

    /**
     * Draws the coin count.
     */
    private void drawCoinCount() {
        Array<TextureRegion> digitTextures = assetsLoader.getNumberTextures(coinCount);
        float x = Gdx.graphics.getWidth() - COIN_PADDING - maxCoinCountWidth;
        TextureRegion coinTexture = AssetsLoader.CoinAssets.getCoinTexture();
        float coinX = x - coinTexture.getRegionWidth() * NUMBER_SCALE - DIGIT_SPACING;
        float coinY = COIN_PADDING - 1;

        spriteBatch.draw(coinTexture, coinX, coinY, coinTexture.getRegionWidth() * COIN_SIZE_SCALE,
                coinTexture.getRegionHeight() * COIN_SIZE_SCALE);
        for (int i = 0; i < digitTextures.size; i++) {
            TextureRegion digit = digitTextures.get(i);
            float scaledWidth = digit.getRegionWidth() * NUMBER_SCALE;
            float scaledHeight = digit.getRegionHeight() * NUMBER_SCALE;
            spriteBatch.draw(digit, x, COIN_PADDING, scaledWidth, scaledHeight);
            x += scaledWidth + DIGIT_SPACING;
        }
    }

    /**
     * Draws the lives as hearts.
     *
     * @param playerLives the player's lives
     * @param maxLives    the maximum number of lives
     */
    private void drawLives(final int playerLives, final int maxLives) {
        float startX = Gdx.graphics.getWidth() - (HEART_SIZE + HEART_SPACING) * maxLives;
        for (int i = 0; i < maxLives; i++) {
            Texture heartTexture = assetsLoader.getHeartTexture(i < playerLives);
            spriteBatch.draw(new TextureRegion(heartTexture), startX + i * (HEART_SIZE + HEART_SPACING)
                            - HEART_X_OFFSET, Gdx.graphics.getHeight() - HEART_SIZE - HEART_Y_OFFSET,
                    HEART_SIZE, HEART_SIZE);
        }
    }


    /**
     * Returns the total width of the digits.
     *
     * @param digits the digits
     * @return the total width of the digits
     */
    private float getTotalWidth(final Array<TextureRegion> digits) {
        float totalWidth = 0;
        for (int i = 0; i < digits.size; i++) {
            TextureRegion digit = digits.get(i);
            totalWidth += (digit.getRegionWidth() + DIGIT_SPACING) * NUMBER_SCALE;
        }
        return totalWidth - DIGIT_SPACING * NUMBER_SCALE;
    }

    /**
     * Checks if the pause button is pressed.
     *
     * @return true if the pause button is pressed
     */

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

    /**
     * Returns if the game is paused.
     *
     * @return true if the game is paused
     */

    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Disposes the HUD.
     */
    public void dispose() {
        pauseFont.dispose();
        shapeRenderer.dispose();
    }

    /**
     * Returns the string representation of the HUD.
     *
     * @return a string
     */
    @Override
    public String toString() {
        return "Hud{"
                + "assetsLoader=" + assetsLoader + ", spriteBatch=" + spriteBatch + ", maxCoinCountWidth="
                + maxCoinCountWidth + ", shapeRenderer=" + shapeRenderer + ", pauseFont=" + pauseFont
                + ", pauseTextBounds=" + pauseTextBounds + ", coinCount=" + coinCount + ", isPaused=" + isPaused + '}';
    }
}
