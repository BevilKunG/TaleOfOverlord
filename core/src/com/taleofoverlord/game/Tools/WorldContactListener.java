package com.taleofoverlord.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Sprites.Boss;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
//        Fixture fixA = contact.getFixtureA();
//        Fixture fixB = contact.getFixtureB();
//
//        if(fixA.getUserData() == "player" || fixB.getUserData() == "player") {
//            Fixture player = fixA.getUserData() == "player" ? fixA : fixB;
//            Fixture boss = player == fixA ? fixB : fixA;
//            if(Boss.class.isAssignableFrom((Class<?>) boss.getUserData())) {
//                ((Boss) boss.getUserData()).chout();
//            }
//        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
