package com.toads.odyssey.util;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.toads.odyssey.model.Player;

public class CollisionDetection implements ContactListener {
    public static final CollisionDetection instance = new CollisionDetection();
    private int groundContacts = 0;
    private CollisionDetection() {
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (isPlayerGroundContact(fa, fb)) {
            groundContacts++;
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
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}

