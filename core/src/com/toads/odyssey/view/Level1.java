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
        loadCoins(world);
        loadFallZones();
        player = new Player(world, new Vector2(16 / ToadsOdyssey.PPM, 400 / ToadsOdyssey.PPM));
        Body playerBody = player.getBody();
    }

    @Override
    protected void setLevel() {
        LevelManager.instance.setLevelBase(this);
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
                float width = ((rectangle.width * 2) / PPM / 2); // divide by 2 to make the box smaller
                float height = ((rectangle.height * 2) / PPM / 2);

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