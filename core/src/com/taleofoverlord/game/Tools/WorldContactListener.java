package com.taleofoverlord.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Sprites.Boss;
import com.taleofoverlord.game.Sprites.Bullet;
import com.taleofoverlord.game.Sprites.Fighter;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if(Bullet.class.isAssignableFrom(fixA.getUserData().getClass()) || Bullet.class.isAssignableFrom(fixB.getUserData().getClass())) {
            Fixture bulletFixture = Bullet.class.isAssignableFrom(fixA.getUserData().getClass()) ? fixA : fixB;
            Fixture targetFixture = fixA == bulletFixture ? fixB : fixA;
            if(targetFixture.getUserData() != null && Fighter.class.isAssignableFrom(targetFixture.getUserData().getClass())) {
                Bullet bullet = (Bullet) bulletFixture.getUserData();
                Fighter target = (Fighter) targetFixture.getUserData();
                if(bullet.getTarget() == target)  {
                    target.decreaseHealthPoint(bullet.getDamage());
                    bullet.finish();
                }
            }
        }
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
