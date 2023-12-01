package com.toads.odyssey.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.util.AssetsLoader;

public class GameWonScreen implements Screen {

    private ToadsOdyssey game;
    private Viewport viewport;
    private TextureRegion region;
    private float stateTimer;

    public GameWonScreen(ToadsOdyssey game) {
        this.game = game;
        this.viewport = new StretchViewport(ToadsOdyssey.SCREEN_WIDTH / ToadsOdyssey.PPM, ToadsOdyssey.SCREEN_HEIGHT / ToadsOdyssey.PPM);
        viewport.apply();
        stateTimer = 0;
        region = AssetsLoader.instance.gameWonScreenAssets.gameWonAnimation.getKeyFrame(stateTimer, false);
    }

    private TextureRegion getFrame(float delta) {
        stateTimer += delta;
        return AssetsLoader.instance.gameWonScreenAssets.gameWonAnimation.getKeyFrame(stateTimer, false);
    }
    private void update(float deltaTime) {
        region = getFrame(deltaTime);
        handleKeyPressed();
        game.batch.begin();
        game.batch.draw(region, 0, 0, ToadsOdyssey.SCREEN_WIDTH, ToadsOdyssey.SCREEN_HEIGHT);
        game.batch.end();
    }
    private void handleKeyPressed() {
        boolean spacePressed = Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
        if (spacePressed) {
            Gdx.app.exit();
        }
    }
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
