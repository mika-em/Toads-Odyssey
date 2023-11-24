package com.toads.odyssey.view;


import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.model.Player;
import com.toads.odyssey.util.LevelManager;

public class Level1 extends LevelBase {
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
        player = new Player(world, new Vector2(32 / ToadsOdyssey.PPM, 400 / ToadsOdyssey.PPM));
    }
    @Override
    protected void setLevel() {
        LevelManager.instance.setLevelBase(this);
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
                platformBodyDef.position.set(polygon.getX() / ToadsOdyssey.PPM, polygon.getY() / ToadsOdyssey.PPM);
                Body platform = world.createBody(platformBodyDef);
                float[] vertices = polygon.getVertices();
                for (int i = 0; i < vertices.length; i += 2) {
                    vertices[i] /= ToadsOdyssey.PPM;
                    vertices[i + 1] /= ToadsOdyssey.PPM;
                }
                ChainShape platformShape = new ChainShape();
                platformShape.createChain(vertices);
                platformFixtureDef.shape = platformShape;
                platform.createFixture(platformFixtureDef).setUserData("Platform");
                platformShape.dispose();
            }
        }
    }


}
