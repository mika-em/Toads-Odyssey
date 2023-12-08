package com.toads.odyssey.util;

import com.badlogic.gdx.physics.box2d.*;
import com.toads.odyssey.model.Player;

public class CollisionDetection implements ContactListener {
    public static final CollisionDetection instance = new CollisionDetection();
    private int groundContacts = 0;
    private boolean playerHasFallen = false;
    private boolean isDoorReached = false;

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
        if (isPlayerMushroomContact(fa, fb)) {
            Player player = getPlayerFromFixture(fa, fb);
            if (player != null) {
                player.hitByMushroom(true);
            }
        }
        if (isPlayerDoorContact(fa, fb)) {
            isDoorReached = true;
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

    private boolean isPlayerDoorContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Player && "Door".equals(b.getUserData())) ||
                (b.getUserData() instanceof Player && "Door".equals(a.getUserData()));
    }

    private boolean isPlayerDeathZoneContact(Fixture a, Fixture b) {
        return (a.getUserData() instanceof Player && "DeathZone".equals(b.getUserData())) ||
                (b.getUserData() instanceof Player && "DeathZone".equals(a.getUserData()));
    }

    private boolean isPlayerMushroomContact(Fixture a, Fixture b) {
        boolean aIsPlayer = a.getUserData() instanceof Player;
        boolean bIsPlayer = b.getUserData() instanceof Player;
        boolean aIsMushroom = "Mushroom".equals(a.getUserData());
        boolean bIsMushroom = "Mushroom".equals(b.getUserData());

        return (aIsPlayer && bIsMushroom) || (bIsPlayer && aIsMushroom);
    }

    private Player getPlayerFromFixture(Fixture a, Fixture b) {
        if (a.getUserData() instanceof Player) {
            return (Player) a.getUserData();
        } else if (b.getUserData() instanceof Player) {
            return (Player) b.getUserData();
        }
        return null;
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

    public boolean isDoorReached() {
        return isDoorReached;
    }
}

