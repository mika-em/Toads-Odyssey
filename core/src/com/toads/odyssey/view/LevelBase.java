package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.model.Coin;
import com.toads.odyssey.model.Mushroom;
import com.toads.odyssey.model.Player;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.util.AssetsLoader.CoinAssets;
import com.toads.odyssey.util.CollisionDetection;
import com.toads.odyssey.util.LevelManager;
import static com.toads.odyssey.ToadsOdyssey.PPM;
import static com.toads.odyssey.ToadsOdyssey.SCREEN_HEIGHT;
import static com.toads.odyssey.ToadsOdyssey.SCREEN_WIDTH;

/**
 * LevelBase is the base class for all levels in the game.
 *
 * @author Joanne, Mika
 * @version 2023
 */
public abstract class LevelBase implements Screen {

    private static final float GRAVITY_Y = -20f;
    private static final float ORIGINAL_PLAYER_X = 100f / PPM;
    private static final float ORIGINAL_PLAYER_Y = 400f / PPM;
    private static final float RESPAWN_DELAY = 0.5f;
    private static final float COIN_TEXTURE_X = 10f;
    private static final float COIN_TEXTURE_Y = 10f;
    private static final float CLEAR_COLOR_R = 199f / 255f;
    private static final float CLEAR_COLOR_G = 219f / 255f;
    private static final float CLEAR_COLOR_B = 238f / 255f;
    private static final float CLEAR_COLOR_ALPHA = 1f;
    private static final float GREY_COLOR = 0.5f;
    private static final float GREY_COLOR_ALPHA = 0.3f;
    private static final int PLAYER_MAX_LIVES = 3;
    /**
     * The player.
     */
    protected Player player;
    /**
     * The map.
     */
    protected TiledMap map;
    /**
     * The debug renderer.
     */
    protected Box2DDebugRenderer debugRenderer;
    /**
     * The world.
     */
    protected World world;
    /**
     * The coins.
     */
    protected Array<Coin> coins;
    /**
     * The mushrooms.
     */
    protected Array<Mushroom> mushrooms;
    /**
     * The HUD.
     */
    protected Hud hud;
    private final ToadsOdyssey game;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer renderer;
    private final Viewport gamePort;
    private Vector2 originalPlayerPosition;
    private Texture grayTexture;
    private int coinCount = 0;
    private GameState gameState = GameState.RUNNING;
    private float respawnTimer = 0.0f;
    private boolean awaitingRespawn = false;
    private float stateTime = 0f;

    /**
     * Constructs a level.
     *
     * @param game the game
     */
    public LevelBase(final ToadsOdyssey game) {
        this.game = game;
        camera = new OrthographicCamera();
        gamePort = new StretchViewport(SCREEN_WIDTH / PPM,
                SCREEN_HEIGHT / PPM, camera);
        loadMap();
        hud = new Hud(AssetsLoader.getInstance(), game.getBatch());
        renderer = new OrthogonalTiledMapRenderer(map, 2 / PPM);
        world = new World(new Vector2(0, GRAVITY_Y), true);
        world.setContactListener(CollisionDetection.getInstance());
        debugRenderer = new Box2DDebugRenderer();
        coins = new Array<>();
        mushrooms = new Array<>();
        setCameraPosition();
        loadEntities();
        setLevel();
        setOriginalPlayerPosition();
        setUpPauseAssets();
    }

    /**
     * Sets up the assets for the pause overlay.
     */
    private void setUpPauseAssets() {
        Pixmap grayPixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        Color grayColor = new Color(GREY_COLOR, GREY_COLOR, GREY_COLOR, GREY_COLOR_ALPHA);
        grayPixmap.setColor(grayColor);
        grayPixmap.fill();
        grayTexture = new Texture(grayPixmap);
        grayPixmap.dispose();
    }

    /**
     * Sets the original position of the player.
     */
    private void setOriginalPlayerPosition() {
        originalPlayerPosition = new Vector2(ORIGINAL_PLAYER_X, ORIGINAL_PLAYER_Y);
    }

    /**
     * Sets the camera position.
     */
    private void setCameraPosition() {
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
    }

    /**
     * Loads the map.
     */
    protected abstract void loadMap();

    /**
     * Loads the entities.
     */
    protected abstract void loadEntities();

    /**
     * Sets the level.
     */
    protected abstract void setLevel();

    /**
     * Updates the game state.
     *
     * @param deltaTime the time between frames
     */
    private void update(final float deltaTime) {
        LevelManager.getInstance().update(deltaTime);
        camera.position.set(player.getPosition().x, gamePort.getWorldHeight() / 2, 0);
        float cameraX = Math.max(player.getPosition().x, gamePort.getWorldWidth() / 2);
        camera.position.set(cameraX, gamePort.getWorldHeight() / 2, 0);
        camera.update();
    }

    /**
     * Returns the frame of the player.
     */
    @Override
    public void show() {
    }

    /**
     * Renders the game.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(final float delta) {
        setStateTime(getStateTime() + delta);
        handlePauseToggle();
        renderBackground(delta);
        handlePlayerFallen();
        handleRespawnAndPlayerDeath(delta);
        renderEntities(delta);
        renderHUD();
    }

    /**
     * Handles the pause toggle so that the game can be paused and un-paused.
     */
    private void handlePauseToggle() {
        if (hud != null && hud.checkPausePressed()) {
            if (gameState == GameState.RUNNING) {
                gameState = GameState.PAUSED;
                player.setCanMove(false);
            } else {
                gameState = GameState.RUNNING;
                player.setCanMove(true);
            }
        }
    }

    /**
     * Renders the background.
     *
     * @param delta The time in seconds since the last render.
     */
    private void renderBackground(final float delta) {
        Gdx.gl.glClearColor(CLEAR_COLOR_R, CLEAR_COLOR_G, CLEAR_COLOR_B, CLEAR_COLOR_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();
        camera.update();
        if (gameState == GameState.RUNNING) {
            update(delta);
        }
    }

    /**
     * Handles the player falling from the map.
     */
    private void handlePlayerFallen() {
        if (CollisionDetection.getInstance().hasPlayerFallen() && !awaitingRespawn) {
            Player.loseLife();
            awaitingRespawn = true;
            respawnTimer = 0.0f;
            CollisionDetection.getInstance().resetPlayerFallen();
        }
    }

    /**
     * Handles the player's respawn and player death.
     *
     * @param delta The time in seconds since the last render.
     */
    private void handleRespawnAndPlayerDeath(final float delta) {
        if (awaitingRespawn) {
            respawnTimer += delta;
            if (respawnTimer >= RESPAWN_DELAY) {
                if (Player.isAlive()) {
                    respawnPlayer();
                }
                awaitingRespawn = false;
            }
        }
        if (!Player.isAlive()) {
            setGameOver();
        }
    }

    /**
     * Renders the entities coin, mushroom, and player.
     *
     * @param delta The time in seconds since the last render.
     */
    private void renderEntities(final float delta) {
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        renderMushroom(delta);
        renderCoins(delta);
        player.update(delta);
        player.draw(game.getBatch());
        renderPauseOverlay();
        checkIfGameWon();
        game.getBatch().end();
    }

    /**
     * Renders the mushroom.
     *
     * @param delta The time in seconds since the last render.
     */
    private void renderMushroom(final float delta) {
        for (int i = 0; i < mushrooms.size; i++) {
            Mushroom mushroom = mushrooms.get(i);
            if (gameState == GameState.RUNNING) {
                mushroom.update(delta);
            }
            mushroom.draw(game.getBatch());
        }
    }

    /**
     * Renders the coins.
     *
     * @param delta The time in seconds since the last render.
     */
    private void renderCoins(final float delta) {
        for (int i = coins.size - 1; i >= 0; i--) {
            Coin coin = coins.get(i);
            if (gameState == GameState.RUNNING) {
                coin.update(delta);
                if (coin.isCollision(player.getBody())) {
                    coinCount++;
                    coins.removeIndex(i);
                    continue;
                }
            }
            coin.draw(game.getBatch());
        }
        TextureRegion coinTexture = CoinAssets.getCoinTexture();
        game.getBatch().draw(coinTexture, COIN_TEXTURE_X, COIN_TEXTURE_Y);
    }


    /**
     * Renders the pause overlay.
     */
    private void renderPauseOverlay() {
        if (gameState == GameState.PAUSED) {
            game.getBatch().draw(grayTexture, 0, 0);
        }
    }

    /**
     * Checks if the player won the game by reaching the door at the end of the level.
     */
    private void checkIfGameWon() {
        if (CollisionDetection.getInstance().isDoorReached()) {
            setGameWon();
        }
    }

    /**
     * Renders the HUD.
     */
    private void renderHUD() {
        if (hud != null) {
            hud.updateCoinCount(coinCount);
            hud.render(player.getLives(), PLAYER_MAX_LIVES);
        }
    }

    /**
     * Resizes the game.
     *
     * @param width  the width of the game
     * @param height the height of the game
     */
    @Override
    public void resize(final int width, final int height) {
        gamePort.update(width, height);
    }

    /**
     * Pauses the game.
     */
    @Override
    public void pause() {
    }

    /**
     * Resumes the game.
     */
    @Override
    public void resume() {
    }

    /**
     * Hides the game.
     */
    @Override
    public void hide() {
    }

    /**
     * Returns the game.
     *
     * @return the game
     */
    public ToadsOdyssey getGame() {
        return game;
    }

    /**
     * Returns the world.
     *
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Resets the player's position.
     */
    private void respawnPlayer() {
        player.resetPosition(originalPlayerPosition);
    }

    /**
     * Sets the game over screen.
     */
    public void setGameOver() {
        game.setScreen(new GameOverScreen(game));
    }

    /**
     * Sets the game won screen.
     */
    public void setGameWon() {
        game.setScreen(new GameWonScreen(game));
    }

    /**
     * Returns the state time.
     *
     * @return the state time
     */
    public float getStateTime() {
        return stateTime;
    }

    /**
     * Sets the state time.
     *
     * @param stateTime the state time
     */
    public void setStateTime(final float stateTime) {
        this.stateTime = stateTime;
    }

    /**
     * Disposes the game when it is no longer needed.
     */
    @Override
    public void dispose() {
        for (int i = 0; i < coins.size; i++) {
            Coin coin = coins.get(i);
            coin.dispose();
        }
        grayTexture.dispose();
        map.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        hud.dispose();
    }
}
