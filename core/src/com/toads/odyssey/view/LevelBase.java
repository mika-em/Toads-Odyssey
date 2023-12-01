package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
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
import com.toads.odyssey.model.Player;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.util.AssetsLoader.CoinAssets;
import com.toads.odyssey.util.CollisionDetection;
import com.toads.odyssey.util.LevelManager;

import java.util.Iterator;

/**
 * LevelBase is an abstract class that is used to create the levels.
 * It includes the camera, renderer, world, and player.
 */
public abstract class LevelBase implements Screen {
    private final ToadsOdyssey game;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer renderer;
    private final Viewport gamePort;
    public Vector2 originalPlayerPosition;
    protected Player player;
    protected TiledMap map;
    protected Box2DDebugRenderer debugRenderer;
    protected World world;
    protected Array<Coin> coins;
    protected Hud hud;
    private int coinCount = 0;
    private final boolean isPaused = false;
    private final Texture grayTexture;
    private GameState gameState = GameState.RUNNING;
    private float respawnTimer = 0.0f;
    private boolean awaitingRespawn = false;


    public LevelBase(ToadsOdyssey game) {
        this.game = game;
        camera = new OrthographicCamera();
        gamePort = new StretchViewport(ToadsOdyssey.SCREEN_WIDTH / ToadsOdyssey.PPM, ToadsOdyssey.SCREEN_HEIGHT / ToadsOdyssey.PPM, camera);
        loadMap();
        hud = new Hud(AssetsLoader.instance, game.batch);
        renderer = new OrthogonalTiledMapRenderer(map, 2 / ToadsOdyssey.PPM); //change to 2 to make the map bigger
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -20), true); //y is gravity
        world.setContactListener(CollisionDetection.instance);
        debugRenderer = new Box2DDebugRenderer();
        coins = new Array<>();
        loadEntities();
        setLevel();
        originalPlayerPosition = new Vector2(42 / ToadsOdyssey.PPM, 400 / ToadsOdyssey.PPM);
        Pixmap grayPixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        Color grayColor = new Color(0.5f, 0.5f, 0.5f, 0.3f);
        grayPixmap.setColor(grayColor);
        grayPixmap.fill();
        grayTexture = new Texture(grayPixmap);
        grayPixmap.dispose();
    }

    protected abstract void loadMap();

    protected abstract void loadEntities();

    protected abstract void setLevel();

    private void update(float deltaTime) {
        LevelManager.instance.update(deltaTime);
        camera.position.set(player.getPosition().x, gamePort.getWorldHeight() / 2, 0);
        float cameraX = Math.max(player.getPosition().x, gamePort.getWorldWidth() / 2);
        camera.position.set(cameraX, gamePort.getWorldHeight() / 2, 0);
        camera.update();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if (hud != null && hud.checkPausePressed()) {
            if (gameState == GameState.RUNNING) {
                gameState = GameState.PAUSED;
                player.setCanMove(false);
            } else {
                gameState = GameState.RUNNING;
                player.setCanMove(true);
            }
        }

        Gdx.gl.glClearColor((199 / 255f), (219 / 255f), (238 / 255f), 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();
        camera.update();
        if (gameState == GameState.RUNNING) {
            update(delta);
        }

        if (CollisionDetection.instance.hasPlayerFallen() && !awaitingRespawn) {
            player.loseLife();
            awaitingRespawn = true;
            respawnTimer = 0.0f;
            CollisionDetection.instance.resetPlayerFallen();
        }

        if (awaitingRespawn) {
            respawnTimer += delta;
            float respawnDelay = 0.3f;
            if (respawnTimer >= respawnDelay) {
                if (player.isAlive()) {
                    respawnPlayer();
                } else {
                    endGame();
                }
                awaitingRespawn = false;
            }
        }

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        Iterator<Coin> coinIterator = coins.iterator();
        while (coinIterator.hasNext()) {
            Coin coin = coinIterator.next();
            if (gameState == GameState.RUNNING) {
                coin.update(delta);
                if (coin.isCollision(player.getBody())) {
                    coinCount++;
                    coinIterator.remove();
                }
            }
            coin.draw(game.batch);
            if (hud != null) {
                hud.updateCoinCount(coinCount);
            }
        }
        player.draw(game.batch);
        TextureRegion coinTexture = CoinAssets.getCoinTexture();
        game.batch.draw(coinTexture, 10, 10);

        if (gameState == GameState.PAUSED) {
            game.batch.draw(grayTexture, 0, 0);
        }

        game.batch.end();

        if (hud != null) {
            hud.render(player.getLives(), 3);
        }

        debugRenderer.render(world, camera.combined);
    }

    @Override
    public void resize(final int width, final int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        for (int i = 0; i < coins.size; i++) {
            Coin coin = coins.get(i);
            coin.dispose();
        }
        grayTexture.dispose();
    }

    public ToadsOdyssey getGame() {
        return game;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    private void respawnPlayer() {
        player.resetPosition(originalPlayerPosition);
    }

    private void endGame() {
        game.setScreen(new GameOverScreen(game));
    }

}