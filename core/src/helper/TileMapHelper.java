package helper;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
public class TileMapHelper {

    public TiledMap tiledMap;
    public TileMapHelper() {
    }

    public OrthogonalTiledMapRenderer setupMap() {
        tiledMap = new TmxMapLoader().load("maps/map.tmx");
        return new OrthogonalTiledMapRenderer(tiledMap, 2);
    }
}
