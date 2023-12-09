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

import java.util.Objects;

/**
 * HUD for the game.
 *
 * @author Joanne and Mika
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
    private static final int DRAW_COINS_PADDING = 50;
    private static final float DRAW_COINS_OFFSET = 1.1f;
    private final AssetsLoader assetsLoader;
    private final SpriteBatch spriteBatch;
    private final float maxCoinCountWidth;
    private BitmapFont pauseFont;
    private Rectangle pauseTextBounds;
    private ShapeRenderer shapeRenderer;
    private int coinCount;
    private boolean isPaused = false;

    /**
     * An HUD displays the player's lives, coins, and pause button on the screen.
     *
     * @param assetsLoader the assets loader
     * @param spriteBatch  the sprite batch
     */
    public Hud(final AssetsLoader assetsLoader, final SpriteBatch spriteBatch) {
        this.assetsLoader = assetsLoader;
        maxCoinCountWidth = calculateMaxWidth();
        this.spriteBatch = Objects.requireNonNull(spriteBatch, "SpriteBatch is null");
        initializePauseFont();
        initializePauseTextBounds();
        initializeShapeRenderer();
    }

    /**
     * Renders the HUD.
     *
     * @param playerLives the player's lives
     * @param maxLives    the maximum number of lives
     */
    public void render(final int playerLives, final int maxLives) {
        spriteBatch.begin();
        drawPauseText();
        drawCoins();
        drawHearts(playerLives, maxLives);
        spriteBatch.end();
    }

    /**
     * Initializes the font for the pause button.
     */
    private void initializePauseFont() {
        pauseFont = new BitmapFont(Gdx.files.internal("assets/font.fnt"));
        pauseFont.getData().setScale(PAUSE_TEXT_SCALE);
        pauseFont.setColor(Color.DARK_GRAY);
    }

    /**
     * Initializes the bounds of the pause button.
     */
    private void initializePauseTextBounds() {
        GlyphLayout layout = new GlyphLayout(pauseFont, "PAUSE");
        float pauseTextWidth = layout.width;
        float pauseTextHeight = layout.height;
        float y = Gdx.graphics.getHeight() - PAUSE_TEXT_Y_OFFSET;
        pauseTextBounds = new Rectangle(PAUSE_TEXT_X_OFFSET + PAUSE_TEXT_X_OFFSET,
                y - pauseTextHeight - PAUSE_TEXT_HEIGHT_OFFSET, pauseTextWidth + PAUSE_TEXT_Y_OFFSET,
                pauseTextHeight + PAUSE_TEXT_X_OFFSET);
    }

    /**
     * Initializes the shape renderer.
     */
    private void initializeShapeRenderer() {
        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Draws the coins on the screen.
     */
    private void drawCoins() {
        Array<TextureRegion> digitTextures = assetsLoader.getNumberTextures(coinCount);
        float x = Gdx.graphics.getWidth() - DRAW_COINS_PADDING - maxCoinCountWidth;

        TextureRegion coinTexture = AssetsLoader.CoinAssets.getCoinTexture();
        float coinX = x - coinTexture.getRegionWidth() * NUMBER_SCALE - DIGIT_SPACING;
        float coinY = DRAW_COINS_PADDING - 1;
        spriteBatch.draw(coinTexture, coinX, coinY, coinTexture.getRegionWidth() * DRAW_COINS_OFFSET,
                coinTexture.getRegionHeight() * DRAW_COINS_OFFSET);

        for (int i = 0; i < digitTextures.size; i++) {
            TextureRegion digit = digitTextures.get(i);
            float scaledWidth = digit.getRegionWidth() * NUMBER_SCALE;
            float scaledHeight = digit.getRegionHeight() * NUMBER_SCALE;
            spriteBatch.draw(digit, x, DRAW_COINS_PADDING, scaledWidth, scaledHeight);
            x += scaledWidth + DIGIT_SPACING;
        }
    }

    /**
     * Draws the pause text on the screen.
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

        float textXOffset;
        if (pauseText.equals("PAUSE")) {
            textXOffset = 0;
        } else {
            textXOffset = (pauseTextWidth - layout.width) / 2;
        }
        float textX = PAUSE_TEXT_X_OFFSET + textXOffset;
        float textY = Gdx.graphics.getHeight() - PAUSE_TEXT_Y_OFFSET;

        pauseFont.draw(spriteBatch, pauseText, textX, textY);
        pauseTextBounds.set(textX, textY - layout.height, pauseTextWidth, layout.height);
    }


    /**
     * Draws the hearts on the screen.
     *
     * @param playerLives the player's lives
     * @param maxLives    the maximum number of lives
     */
    private void drawHearts(final int playerLives, final int maxLives) {
        float startX = Gdx.graphics.getWidth() - (HEART_SIZE + HEART_SPACING) * maxLives;

        for (int i = 0; i < maxLives; i++) {
            Texture heartTexture = assetsLoader.getHeartTexture(i < playerLives);
            spriteBatch.draw(new TextureRegion(heartTexture), startX + i * (HEART_SIZE + HEART_SPACING)
                            - HEART_X_OFFSET, Gdx.graphics.getHeight() - HEART_SIZE - HEART_Y_OFFSET, HEART_SIZE,
                    HEART_SIZE);
        }
    }

    /**
     * Checks if the pause button is pressed.
     *
     * @return true if the pause button is pressed, false otherwise
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
     * Returns the total width of the numbers.
     *
     * @param digits the digits in the HUD
     * @return the total width of the numbers
     */
    private float getTotalWidthOfNumbers(final Array<TextureRegion> digits) {
        float totalWidth = 0;
        for (int i = 0; i < digits.size; i++) {
            TextureRegion digit = digits.get(i);
            totalWidth += (digit.getRegionWidth() + DIGIT_SPACING) * NUMBER_SCALE;
        }
        return totalWidth - DIGIT_SPACING * NUMBER_SCALE;
    }

    /**
     * Returns if the game is paused.
     *
     * @return true if the game is paused, false otherwise
     */
    public boolean isPaused() {
        return isPaused;
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
     * Calculates the maximum width of the coins.
     *
     * @return the maximum width of the coins
     */
    private float calculateMaxWidth() {
        Array<TextureRegion> maxDigits = assetsLoader.getNumberTextures(MAX_COIN_COUNT);
        return getTotalWidthOfNumbers(maxDigits) * NUMBER_SCALE + COIN_PADDING
                + AssetsLoader.CoinAssets.getCoinTexture().getRegionWidth() * COIN_SIZE_SCALE;
    }

    /**
     * Disposes the HUD.
     */
    public void dispose() {
        pauseFont.dispose();
        shapeRenderer.dispose();
    }
}
