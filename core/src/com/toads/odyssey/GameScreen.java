package com.toads.odyssey;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.math.Vector2;
import helper.TileMapHelper;
import objects.player.Player;
import static helper.Constants.PPM;

public class GameScreen extends ScreenAdapter {

    private final OrthographicCamera camera;
    private final SpriteBatch batch; //will render the sprites
    private final World world; //will hold the game objects, ie. box2D bodies
    private final Box2DDebugRenderer debugRenderer; //will render the box2D bodies for debugging purposes
    private final OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Player player;


    public GameScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, -20), false);
        this.debugRenderer = new Box2DDebugRenderer();
        TileMapHelper tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();
    }

    private void update() {
        //update objects
        //update the world 1/60 since FPS is 60
        world.step(1 / 60f, 6, 2);

        //camera update
        cameraUpdate();

        batch.setProjectionMatrix(camera.combined); //set the projection matrix to the camera's matrix
        orthogonalTiledMapRenderer.setView(camera);

        player.update();


        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit(); //exit the game if escape is pressed
        }
    }

    private void cameraUpdate() {
        float cameraX = Math.round(player.getBody().getPosition().x * PPM); //round the player's x position
        float cameraY = camera.viewportHeight / 2;
        camera.position.set(cameraX, cameraY, 0); //set the camera's position to the center of the screen
        camera.update();
    }
    @Override
    public void render(float delta) {
        this.update();

        //clear screen to remove previous frame
        Gdx.gl.glClearColor((199/255f), (219/255f), (238/255f), 1); //black screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //clear the screen

        orthogonalTiledMapRenderer.render();

        batch.begin(); //start rendering all the objects


        batch.end();
        debugRenderer.render(world, camera.combined.scl(PPM)); //render the box2D bodies
    }

    public World getWorld() {
        return world;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
