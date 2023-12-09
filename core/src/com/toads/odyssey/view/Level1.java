package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.model.Coin;
import com.toads.odyssey.model.Mushroom;
import com.toads.odyssey.model.Player;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.util.LevelManager;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import static com.toads.odyssey.ToadsOdyssey.PPM;

/**
 * Represents the first level of the game.
 *
 * @author Joanne, Mika
 * @version 2023
 */
public class Level1 extends LevelBase {
    private static final String MAP_PATH = "maps/map.tmx";
    private static final float COIN_X_OFFSET = 4f;
    private static final float COIN_Y_OFFSET = 5f;
    private static final float COIN_SCALE = 2f;
    private static final float PLATFORM_SCALE = 2f;
    private static final float DOOR_SCALE = 2f;
    private static final float FALL_ZONE_SCALE = 2f;
    private static final float FALL_ZONE_OFFSET = 0.5f;
    private static final float PLAYER_START_X = 42f / PPM;
    private static final float PLAYER_START_Y = 400f / PPM;

    /**
     * Constructs a level.
     *
     * @param game the game
     */
    public Level1(final ToadsOdyssey game) {
        super(game);
    }

    /**
     * Gets the mushroom from the map and creates a mushroom object.
     *
     * @param world           the Box2D world
     * @param rectangleObject the rectangle object
     * @return the mushroom
     */
    private static Mushroom getMushroom(final World world, final RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        float x = ((rectangle.x) * 2 / PPM);
        float y = ((rectangle.y) / PPM) * 2;
        float originalWidth = rectangle.width / PPM;
        float originalHeight = rectangle.height / PPM;
        float width = originalWidth * 2;
        float height = originalHeight * 2;
        return new Mushroom(AssetsLoader.MushroomAssets.mushroomAnimation, world, x, y, width, height);
    }

    /**
     * Loads the map.
     */
    @Override
    protected void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(MAP_PATH);
    }

    /**
     * Loads the entities coin, mushroom, platform, door, and fall zone.
     */
    @Override
    protected void loadEntities() {
        loadPlatform();
        loadDoor();
        loadMushrooms(world);
        loadCoins(world);
        loadFallZones();
        player = new Player(world, new Vector2(PLAYER_START_X, PLAYER_START_Y));
    }

    /**
     * Sets the level.
     */
    @Override
    protected void setLevel() {
        LevelManager.getInstance().setLevelBase(this);
    }

    /**
     * Loads the mushrooms.
     *
     * @param world the Box2D world
     */
    private void loadMushrooms(final World world) {
        MapLayer mushroomLayer = map.getLayers().get("mushroom");
        for (MapObject object : mushroomLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Mushroom mushroom = getMushroom(world, rectangleObject);
                mushrooms.add(mushroom);
            }
        }
    }

    /**
     * Loads the coins.
     *
     * @param world the Box2D world
     */
    private void loadCoins(final World world) {
        AssetsLoader assetsLoader = AssetsLoader.getInstance();
        AssetsLoader.CoinAssets coinAssets = assetsLoader.getCoinAssets();
        Gdx.app.log("LoadCoins", "Loading coins with assets: " + coinAssets);
        MapLayer coinLayer = map.getLayers().get("coins");
        if (coins == null) {
            coins = new Array<>();
        }
        for (MapObject object : coinLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle rectangle = rectangleObject.getRectangle();
                float x = ((rectangle.x + COIN_X_OFFSET) * COIN_SCALE) / PPM;
                float y = ((rectangle.y + COIN_Y_OFFSET) * COIN_SCALE) / PPM;
                Coin coin = new Coin(AssetsLoader.getInstance().getCoinAnimation(), world, x, y);
                coins.add(coin);
                if (coins.isEmpty()) {
                    System.out.println("coins is empty");
                }
            }
        }
    }

    /**
     * Loads the platforms.
     */
    private void loadPlatform() {
        BodyDef platformBodyDef = new BodyDef();
        platformBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef platformFixtureDef = new FixtureDef();
        MapLayer platformLayer = map.getLayers().get("objects");

        for (MapObject object : platformLayer.getObjects()) {
            if (object instanceof PolygonMapObject) {
                PolygonMapObject polygonObject = (PolygonMapObject) object;
                Polygon polygon = polygonObject.getPolygon();
                platformBodyDef.position.set((polygon.getX() * PLATFORM_SCALE) / PPM,
                        (polygon.getY() * PLATFORM_SCALE) / PPM);
                Body platform = world.createBody(platformBodyDef);
                float[] vertices = polygon.getVertices();
                for (int i = 0; i < vertices.length; i += 2) {
                    vertices[i] = (vertices[i] * 2) / PPM;
                    vertices[i + 1] = (vertices[i + 1] * 2) / PPM;
                }
                ChainShape platformShape = new ChainShape();
                platformShape.createChain(vertices);
                platformFixtureDef.shape = platformShape;
                platform.createFixture(platformFixtureDef).setUserData("Platform");
                platformShape.dispose();
            }
        }
    }

    /**
     * Loads the door at the end of the level.
     */
    private void loadDoor() {
        MapLayer doorLayer = map.getLayers().get("door");
        for (MapObject object : doorLayer.getObjects()) {
            if (object instanceof PolygonMapObject) {
                PolygonMapObject polygonObject = (PolygonMapObject) object;
                Polygon polygon = polygonObject.getPolygon();
                float[] vertices = polygon.getTransformedVertices();
                Vector2[] worldVertices = new Vector2[vertices.length / 2];
                for (int i = 0; i < vertices.length / 2; ++i) {
                    worldVertices[i] = new Vector2(vertices[i * 2] / PPM, vertices[i * 2 + 1] / PPM);
                }
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(polygon.getX() / PPM, polygon.getY() / PPM);
                Body doorBody = world.createBody(bodyDef);
                PolygonShape shape = new PolygonShape();
                shape.set(worldVertices);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                Fixture fixture = doorBody.createFixture(fixtureDef);
                fixture.setUserData("Door");
                shape.dispose();
            }
        }
    }

    /**
     * Loads the fall zones.
     */
    private void loadFallZones() {
        MapLayer fallZoneLayer = map.getLayers().get("death_zones");
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;

        for (MapObject object : fallZoneLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                float x = (rect.x + rect.width * FALL_ZONE_OFFSET) * DOOR_SCALE / PPM;
                float y = (rect.y + rect.height * FALL_ZONE_OFFSET) * DOOR_SCALE / PPM;
                bodyDef.position.set(x, y);
                Body sensorBody = world.createBody(bodyDef);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rect.width * FALL_ZONE_OFFSET * FALL_ZONE_SCALE / PPM,
                        rect.height * FALL_ZONE_OFFSET * FALL_ZONE_SCALE / PPM);
                fixtureDef.shape = shape;
                sensorBody.createFixture(fixtureDef).setUserData("DeathZone");
                shape.dispose();
            }
        }
    }
}
