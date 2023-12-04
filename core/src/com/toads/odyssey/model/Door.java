package com.toads.odyssey.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Door extends Entity {
    private Animation<TextureAtlas.AtlasRegion> doorAnimation;
    private float stateTimer;
    private boolean isDoorOpen;
    public Door(World world, Vector2 position) {
        super(world, position);
        stateTimer = 1;
        isDoorOpen = false;
    }

    @Override
    public void define() {

    }

    @Override
    public void update(float delta) {

    }

    private TextureRegion getFrame(float delta) {
        TextureRegion region;
        region = doorAnimation.getKeyFrame(stateTimer);
        stateTimer = isDoorOpen ? stateTimer + delta : 1;
        return region;
    }
}
