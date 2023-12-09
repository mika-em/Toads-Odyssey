package com.toads.odyssey.view;


import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.model.Coin;
import com.toads.odyssey.model.Mushroom;
import com.toads.odyssey.model.Player;
import com.toads.odyssey.util.AssetsLoader;
import com.toads.odyssey.util.LevelManager;

import static com.toads.odyssey.ToadsOdyssey.PPM;

public class Level1 extends LevelBase {

    private GameState gameState = GameState.RUNNING;


    public Level1(final ToadsOdyssey game) {
        super(game);
    }

    @Override
    protected void loadMap() {
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/map.tmx");
    }

    @Override
    protected void loadEntities() {
        loadPlatform();
        loadDoor();
        loadMushrooms(world);
        loadCoins(world);
        loadFallZones();
        player = new Player(world, new Vector2(42 / ToadsOdyssey.PPM, 400 / ToadsOdyssey.PPM));
        Body playerBody = player.getBody();
    }

    @Override
    protected void setLevel() {
        LevelManager.getInstance().setLevelBase(this);
    }

    private void loadMushrooms(World world) {
        AssetsLoader assetsLoader = AssetsLoader.instance;
        AssetsLoader.MushroomAssets mushroomAssets = assetsLoader.getMushroomAssets();
        MapLayer mushroomLayer = map.getLayers().get("mushroom");

        if (mushroomLayer == null || mushroomLayer.getObjects().getCount() == 0) return;

        for (MapObject object : mushroomLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle rectangle = rectangleObject.getRectangle();

                // Adjusting for unit scale and converting pixels to world units
                float x = ((rectangle.x) * 2/ PPM); // Multiply by the unit scale
                float y = ((rectangle.y) / PPM) * 2; // Multiply by the unit scale
                float originalWidth = rectangle.width / PPM;
                float originalHeight = rectangle.height / PPM;
                float width = originalWidth * 2; // Make mushroom twice as wide
                float height = originalHeight * 2; // Make mushroom twice as tall

                Mushroom mushroom = new Mushroom(mushroomAssets.mushroomAnimation, world, x, y, width, height);
                mushrooms.add(mushroom);
            }
        }
    }

    private void loadCoins(World world) {
        AssetsLoader assetsLoader = AssetsLoader.instance;
        AssetsLoader.CoinAssets coinAssets = assetsLoader.getCoinAssets();
        MapLayer coinLayer = map.getLayers().get("coins");

        if (coinLayer == null || coinLayer.getObjects().getCount() == 0) return;

        if (coins == null) coins = new Array<Coin>();

        for (MapObject object : coinLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectangleObject = (RectangleMapObject) object;
                Rectangle rectangle = rectangleObject.getRectangle();

                float x = ((rectangle.x + 4) * 2) / PPM; // add 4 to center the coin
                float y = ((rectangle.y + 5) * 2) / PPM; // add 5 to be slightly above the ground
                float width = ((rectangle.width * 2) / PPM ); // divide by 2 to make the box smaller
                float height = ((rectangle.height * 2) / PPM);

                Coin coin = new Coin(coinAssets.coinAnimation, world, x, y);
                coins.add(coin);

                if (coins.isEmpty()) {
                    System.out.println("coins is empty");
                }
            }
        }
    }

    private void loadPlatform() {
        BodyDef platformBodyDef = new BodyDef();
        platformBodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef platformFixtureDef = new FixtureDef();
        MapLayer platformLayer = map.getLayers().get("objects");

        for (MapObject object : platformLayer.getObjects()) {
            if (object instanceof PolygonMapObject) {
                PolygonMapObject polygonObject = (PolygonMapObject) object;
                Polygon polygon = polygonObject.getPolygon();
                platformBodyDef.position.set((polygon.getX() * 2) / PPM, (polygon.getY() * 2) / PPM);
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

    private void loadFallZones() {
        MapLayer fallZoneLayer = map.getLayers().get("death_zones");

        if (fallZoneLayer == null) return;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;

        for (MapObject object : fallZoneLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                float x = (rect.x + rect.width * 0.5f) * 2 / PPM;
                float y = (rect.y + rect.height * 0.5f) * 2 / PPM;
                bodyDef.position.set(x, y);
                Body sensorBody = world.createBody(bodyDef);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(rect.width * 0.5f * 2 / PPM, rect.height * 0.5f * 2 / PPM);
                fixtureDef.shape = shape;
                sensorBody.createFixture(fixtureDef).setUserData("DeathZone");
                shape.dispose();
            }
        }
    }


}