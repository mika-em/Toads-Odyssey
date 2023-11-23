package com.toads.odyssey.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.toads.odyssey.ToadsOdyssey;
import com.toads.odyssey.model.Player;
import com.toads.odyssey.view.LevelBase;

public class LevelManager {
    public static final LevelManager instance = new LevelManager();
    private ToadsOdyssey game;
    private LevelBase levelBase;
    private World world;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private LevelManager() {
    }
    public void setLevelBase(LevelBase levelBase) {
        instance.levelBase = levelBase;
        instance.game = levelBase.getGame();
        instance.world = levelBase.getWorld();
        instance.renderer = levelBase.getRenderer();
        instance.camera = levelBase.getCamera();
        instance.player = levelBase.getPlayer();
    }
    public void update(float deltaTime) {
        handleUserInput(deltaTime);
        world.step(1 / 60f, 60, 2);
        player.update(deltaTime);
    }
    public void handleUserInput(float deltaTime) {
        player.handleKeyPressed();
    }

}
