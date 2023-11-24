package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.model.Player;
import com.toads.odyssey.util.CollisionDetection;
import com.toads.odyssey.util.LevelManager;

import static helper.Constants.PPM;

/**
 * LevelBase is an abstract class that is used to create the levels.
 * It includes the camera, renderer, world, and player.
 */
public abstract class LevelBase implements Screen {
    private ToadsOdyssey game;
    protected Player player;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    protected TiledMap map;
    protected Box2DDebugRenderer debugRenderer;
    protected World world;
    private Viewport gamePort;
    public LevelBase(ToadsOdyssey game) {
        this.game = game;
        camera = new OrthographicCamera();
        gamePort = new StretchViewport(ToadsOdyssey.SCREEN_WIDTH / ToadsOdyssey.PPM, ToadsOdyssey.SCREEN_HEIGHT / ToadsOdyssey.PPM, camera);
        loadMap();
        renderer = new OrthogonalTiledMapRenderer(map, 2 / ToadsOdyssey.PPM); //change to 2 to make the map bigger
        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -20), true); //y is gravity
        world.setContactListener(CollisionDetection.instance);
        debugRenderer = new Box2DDebugRenderer();
        loadEntities();
        setLevel();
    }
    protected abstract void loadMap();
    protected abstract void loadEntities();
    protected abstract void setLevel();

    private void update(float deltaTime) {
        LevelManager.instance.update(deltaTime);
        Vector2 playerPosition = player.getBody().getPosition();
        camera.position.set(playerPosition.x, gamePort.getWorldHeight()/2, 0);
        camera.update();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit(); // Exit the game if escape is pressed
        }
    }

    @Override
    public void show() {
    }
    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor((199/255f), (219/255f), (238/255f), 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        //Gdx.app.log("camera position " + camera.position);
        renderer.setView(camera);
        renderer.render();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();

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
}
