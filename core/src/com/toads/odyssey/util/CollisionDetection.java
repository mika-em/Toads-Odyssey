package com.toads.odyssey.util;

import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.toads.odyssey.model.Player;

/**
 * Detects collisions between objects in the game world.
 *
 * @author Mika, Joanne
 * @version 2023
 */
public final class CollisionDetection implements ContactListener {
    /**
     * A singleton instance of CollisionDetection.
     */
    private static final CollisionDetection INSTANCE = new CollisionDetection();
    private int groundContacts = 0;
    private boolean playerHasFallen = false;
    private boolean isDoorReached = false;

    /**
     * Prevents instantiation from other classes.
     */
    private CollisionDetection() {
    }

    /**
     * Returns CollisionDetection singleton.
     * @return the CollisionDetection singleton
     */
    public static CollisionDetection getInstance() {
        return INSTANCE;
    }

    /**
     * Called when two fixtures collides with each other.
     * @param contact the contact object
     */
    @Override
    public void beginContact(final Contact contact) {
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

    /**
     * Called when two fixtures no longer collide with each other.
     * @param contact the contact object
     */
    @Override
    public void endContact(final Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (isPlayerGroundContact(fa, fb)) {
            groundContacts--;
        }
    }

    /**
     * Returns true if the player is on the ground, false otherwise.
     * @return true if the player is on the ground, false otherwise
     */
    public boolean isOnGround() {
        return groundContacts > 0;
    }

    /**
     * Returns true if the player has collided with the ground, false otherwise.
     * @param a the first fixture
     * @param b the second fixture
     * @return true if the player has collided with the ground, false otherwise
     */
    private boolean isPlayerGroundContact(final Fixture a, final Fixture b) {
        return (a.getUserData() instanceof Player && "Platform".equals(b.getUserData()))
                || (b.getUserData() instanceof Player && "Platform".equals(a.getUserData()));
    }

    /**
     * Returns true if the player has collided with the door, false otherwise.
     * @param a the first fixture
     * @param b the second fixture
     * @return true if the player has collided with the door, false otherwise
     */
    private boolean isPlayerDoorContact(final Fixture a, final Fixture b) {
        return (a.getUserData() instanceof Player && "Door".equals(b.getUserData()))
                || (b.getUserData() instanceof Player && "Door".equals(a.getUserData()));
    }

    /**
     * Returns true if the player has collided with the death zone, false otherwise.
     * @param a the first fixture
     * @param b the second fixture
     * @return true if the player has collided with the death zone, false otherwise
     */
    private boolean isPlayerDeathZoneContact(final Fixture a, final Fixture b) {
        return (a.getUserData() instanceof Player && "DeathZone".equals(b.getUserData()))
                || (b.getUserData() instanceof Player && "DeathZone".equals(a.getUserData()));
    }

    /**
     * Returns true if the player has collided with a mushroom, false otherwise.
     * @param a the first fixture
     * @param b the second fixture
     * @return true if the player has collided with a mushroom, false otherwise
     */
    private boolean isPlayerMushroomContact(final Fixture a, final Fixture b) {
        boolean aIsPlayer = a.getUserData() instanceof Player;
        boolean bIsPlayer = b.getUserData() instanceof Player;
        boolean aIsMushroom = "Mushroom".equals(a.getUserData());
        boolean bIsMushroom = "Mushroom".equals(b.getUserData());
        return (aIsPlayer && bIsMushroom) || (bIsPlayer && aIsMushroom);
    }

    /**
     * Returns the player object from the provided fixtures, or null if not found.
     * @param a the first fixture
     * @param b the second fixture
     * @return the player object if found in either fixture's user data, or null if not found.
     */
    private Player getPlayerFromFixture(final Fixture a, final Fixture b) {
        if (a.getUserData() instanceof Player) {
            return (Player) a.getUserData();
        } else if (b.getUserData() instanceof Player) {
            return (Player) b.getUserData();
        }
        return null;
    }

    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold) {
    }


    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {
    }

    /**
     * Returns true if the player has fallen, false otherwise.
     * @return true if the player has fallen, false otherwise
     */
    public boolean hasPlayerFallen() {
        return playerHasFallen;
    }

    /**
     * Resets the playerHasFallen boolean to false.
     */
    public void resetPlayerFallen() {
        playerHasFallen = false;
    }

    /**
     * Returns true if the player has reached the door, false otherwise.
     * @return true if the player has reached the door, false otherwise
     */
    public boolean isDoorReached() {
        return isDoorReached;
    }
}

