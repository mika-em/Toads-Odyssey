package com.toads.odyssey;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;


public class Boot extends Game {

    public static Boot INSTANCE;
    public int widthScreen, heightScreen;
    public OrthographicCamera camera;

    public Boot() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        this.widthScreen = Gdx.graphics.getWidth();
        this.heightScreen = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.widthScreen, this.heightScreen);
        setScreen(new GameScreen(camera));
    }
}
