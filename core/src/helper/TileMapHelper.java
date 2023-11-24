//package helper;
//
//import com.badlogic.gdx.maps.MapObject;
//import com.badlogic.gdx.maps.MapObjects;
//import com.badlogic.gdx.maps.objects.PolygonMapObject;
//import com.badlogic.gdx.maps.objects.RectangleMapObject;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TmxMapLoader;
//import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.PolygonShape;
//import com.badlogic.gdx.physics.box2d.Shape;
//import com.toads.odyssey.GameScreen;
//import objects.player.Player;
//
//public class TileMapHelper {
//
//    private GameScreen gameScreen;
//
//    public TiledMap tiledMap;
//
//    public TileMapHelper() {
//    }
//
//    public TileMapHelper(GameScreen gameScreen) {
//        this.gameScreen = gameScreen;
//    }
//
//    public OrthogonalTiledMapRenderer setupMap() {
//        tiledMap = new TmxMapLoader().load("maps/map.tmx");
//        parseMapObjects(tiledMap.getLayers().get("objects").getObjects());
//        return new OrthogonalTiledMapRenderer(tiledMap, 2);
//    }
//
//    private void parseMapObjects(MapObjects mapObjects) {
//        for (MapObject mapObject : mapObjects) {
//            if (mapObject instanceof PolygonMapObject) {
//                createStaticBody((PolygonMapObject) mapObject);
//            }
//
//            if (mapObject instanceof RectangleMapObject) {
//                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
//                String rectangleName = mapObject.getName();
//
//                if (rectangleName.equals("player")) {
//                    Body body = BodyHelper.createBody(
//                            rectangle.getX() + rectangle.getWidth() / 2,
//                            rectangle.getY() + rectangle.getHeight() / 2,
//                            rectangle.getWidth(),
//                            rectangle.getHeight(),
//                            false,
//                            gameScreen.getWorld()
//                    );
//                    gameScreen.setPlayer(new Player(rectangle.getWidth(), rectangle.getHeight(), body));
//                }
//            }
//
//        }
//        }
//
//
//    private void createStaticBody(PolygonMapObject polygonMapObject) {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.StaticBody;
//        Body body = gameScreen.getWorld().createBody(bodyDef);
//        Shape shape = createPolygonShape(polygonMapObject);
//        body.createFixture(shape, 1000);
//        shape.dispose();
//    }
//
//    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
//        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
//        Vector2[] worldVertices = new Vector2[vertices.length / 2];
//
//        for (int i = 0; i < vertices.length / 2; i++) {
//            Vector2 current = new Vector2(
//                    (vertices[i * 2] * 2) / Constants.PPM,
//                    (vertices[i * 2 + 1] * 2) / Constants.PPM);
//            worldVertices[i] = current;
//        }
//
//        PolygonShape polygonShape = new PolygonShape();
//        polygonShape.set(worldVertices);
//        return polygonShape;
//    }
//
//
////    private void parseMapObjects(MapObjects mapObjects) {
////        float unitScale = 2; // Define your unit scale
////
////        for (MapObject mapObject : mapObjects) {
////            if (mapObject instanceof PolygonMapObject) {
////                createStaticBody((PolygonMapObject) mapObject, unitScale);
////            }
////
////            if (mapObject instanceof RectangleMapObject) {
////                RectangleMapObject rectObject = (RectangleMapObject) mapObject;
////                String rectangleName = rectObject.getName();
////
////
////            if (rectangleName.equals("player")) {
////                Rectangle rect = rectangleMapObject.getRectangle();
////                BodyDef bodyDef = new BodyDef();
////                bodyDef.type = BodyDef.BodyType.DynamicBody;
////                bodyDef.position.set(
////                        (rect.x + rect.width * 0.5f) * unitScale / Constants.PPM,
////                        (rect.y + rect.height * 0.5f) * unitScale / Constants.PPM);
////
////                Body body = gameScreen.getWorld().createBody(bodyDef);
////
////                PolygonShape shape = new PolygonShape();
////
////                shape.setAsBox((rect.width * 0.5f) * unitScale / Constants.PPM,
////                        (rect.height * 0.5f) * unitScale / Constants.PPM);
////
////                FixtureDef fixtureDef = new FixtureDef();
////                fixtureDef.shape = shape;
////                body.createFixture(fixtureDef);
////                shape.dispose();
////                return body;
////            }
////
////
////        }
////    }
////
////        private void createStaticBody(PolygonMapObject polygonMapObject) {
////        BodyDef bodyDef = new BodyDef();
////        bodyDef.type = BodyDef.BodyType.StaticBody;
////        Body body = gameScreen.getWorld().createBody(bodyDef);
////        Shape shape = createPolygonShape(polygonMapObject);
////        body.createFixture(shape, 1000);
////        shape.dispose();
////        }
////
////        private Shape createPolygonShape(PolygonMapObject polygonMapObject){
////            float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
////            Vector2[] worldVertices = new Vector2[vertices.length / 2];
////
////            for (int i = 0; i < vertices.length / 2; i++) {
////                Vector2 current = new Vector2(
////                        (vertices[i * 2] * unitScale) / Constants.PPM,
////                        (vertices[i * 2 + 1] * unitScale) / Constants.PPM);
////                worldVertices[i] = current;
////
////
////            }
////        }
//
////    private void createStaticBody(PolygonMapObject polygonMapObject, float unitScale) {
////        BodyDef bodyDef = new BodyDef();
////        bodyDef.type = BodyDef.BodyType.StaticBody;
////        Body body = gameScreen.getWorld().createBody(bodyDef);
////        Shape shape = createPolygonShape(polygonMapObject, unitScale);
////        body.createFixture(shape, 1000);
////        shape.dispose();
////    }
////
////    private Shape createPolygonShape(PolygonMapObject polygonMapObject, float unitScale) {
////        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
////        Vector2[] worldVertices = new Vector2[vertices.length / 2];
////
////        for (int i = 0; i < vertices.length / 2; i++) {
////            Vector2 current = new Vector2(
////                    (vertices[i * 2] * unitScale) / Constants.PPM,
////                    (vertices[i * 2 + 1] * unitScale) / Constants.PPM);
////            worldVertices[i] = current;
////        }
////
////        PolygonShape polygonShape = new PolygonShape();
////        polygonShape.set(worldVertices);
////        return polygonShape;
////    }
////
////    private void createStaticBody(RectangleMapObject rectangleMapObject, float unitScale) {
////        Rectangle rect = rectangleMapObject.getRectangle();
////        BodyDef bodyDef = new BodyDef();
////        bodyDef.type = BodyDef.BodyType.StaticBody;
////        bodyDef.position.set(
////                (rect.x + rect.width * 0.5f) * unitScale / Constants.PPM,
////                (rect.y + rect.height * 0.5f) * unitScale / Constants.PPM);
////
////        Body body = gameScreen.getWorld().createBody(bodyDef);
////
////        PolygonShape shape = new PolygonShape();
////        shape.setAsBox((rect.width * 0.5f) * unitScale / Constants.PPM,
////                (rect.height * 0.5f) * unitScale / Constants.PPM);
////
////        body.createFixture(shape, 1000);
////        shape.dispose();
////    }
//}
