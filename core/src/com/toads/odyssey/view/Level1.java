package com.toads.odyssey.view;


import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
        map = mapLoader.load("map/level1.tmx");
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
        PolygonShape platformShape = new PolygonShape();
        FixtureDef platformFixtureDef = new FixtureDef();
        platformFixtureDef.shape = platformShape;
        MapLayer platformLayer = map.getLayers().get("platform");
        for (MapObject object : platformLayer.getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            platformBodyDef.position.set((rect.getX() + rect.getWidth() / 2) / ToadsOdyssey.PPM, (rect.getY()
                    + rect.getHeight() / 2) / ToadsOdyssey.PPM);
            Body platform = world.createBody(platformBodyDef);
            platformShape.setAsBox(rect.getWidth() / 2 / ToadsOdyssey.PPM, rect.getHeight() / 2 / ToadsOdyssey.PPM);
            platform.createFixture(platformFixtureDef).setUserData("Platform");
        }
        platformShape.dispose();
    }

}
