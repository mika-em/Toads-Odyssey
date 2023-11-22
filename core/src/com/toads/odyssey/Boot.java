package com.toads.odyssey;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class Boot extends Game {

    public static Boot INSTANCE;

    public Boot() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        int widthScreen = Gdx.graphics.getWidth();
        int heightscreen = Gdx.graphics.getHeight();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, widthScreen, heightscreen);
        setScreen(new GameScreen(camera));
    }
}
