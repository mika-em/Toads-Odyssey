package com.toads.odyssey.util;

import com.badlogic.gdx.physics.box2d.*;
import com.toads.odyssey.model.Door;
import com.toads.odyssey.model.Player;

public class CollisionDetection implements ContactListener {
    public static final CollisionDetection instance = new CollisionDetection();
    private int groundContacts = 0;
    private boolean playerHasFallen = false;
    private boolean playerHasReachedDoor = false;

    private CollisionDetection() {
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (isPlayerGroundContact(fa, fb)) {
            groundContacts++;
        }
        if (isPlayerDeathZoneContact(fa, fb)) {
            playerHasFallen = true;
        }
        if (isPlayerDoorContact(fa, fb)) {
            playerHasReachedDoor = true;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (isPlayerGroundContact(fa, fb)) {
            groundContacts--;
        }
    }

    public boolean isOnGround() {
        return groundContacts > 0;
    }

    private boolean isPlayerGroundContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Player && "Platform".equals(b.getUserData())) ||
                (b.getUserData() instanceof Player && "Platform".equals(a.getUserData()));
    }

    private boolean isPlayerDeathZoneContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Player && "DeathZone".equals(b.getUserData())) ||
                (b.getUserData() instanceof Player && "DeathZone".equals(a.getUserData()));
    }

    private boolean isPlayerDoorContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Player && b.getUserData() instanceof Door) ||
                (b.getUserData() instanceof Player && a.getUserData() instanceof Door);
    }


    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public boolean hasPlayerFallen() {
        return playerHasFallen;
    }

    public void resetPlayerFallen() {
        playerHasFallen = false;
    }
}

