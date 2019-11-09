package com.taleofoverlord.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Sprites.*;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        // Shooting Event
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

        // Slashing Event
        if(SlashedSword.class.isAssignableFrom(fixA.getUserData().getClass()) || SlashedSword.class.isAssignableFrom(fixB.getUserData().getClass())) {
            Fixture slashedSwordFixture = SlashedSword.class.isAssignableFrom(fixA.getUserData().getClass()) ? fixA : fixB;
            Fixture targetFixture = fixA == slashedSwordFixture ? fixB : fixA;
            if(targetFixture.getUserData() != null && Fighter.class.isAssignableFrom(targetFixture.getUserData().getClass())) {
                SlashedSword slashedSword = (SlashedSword) slashedSwordFixture.getUserData();
                Fighter target = (Fighter) targetFixture.getUserData();
                if(slashedSword.getTarget() == target) {
                    target.decreaseHealthPoint(slashedSword.getDamage());
                    target.cancelAction();
                    target.recoil();
                    slashedSword.finish();
                }

            }
        }

        // Punching Event
        if(Punch.class.isAssignableFrom(fixA.getUserData().getClass()) || Punch.class.isAssignableFrom(fixB.getUserData().getClass())) {
            Fixture punchFixture = Punch.class.isAssignableFrom(fixA.getUserData().getClass()) ? fixA : fixB;
            Fixture targetFixture = fixA == punchFixture ? fixB : fixA;
            if(targetFixture.getUserData() != null && Fighter.class.isAssignableFrom(targetFixture.getUserData().getClass())) {
                Punch punch = (Punch) punchFixture.getUserData();
                Fighter target = (Fighter) targetFixture.getUserData();
                if(punch.getTarget() == target) {
                    target.decreaseHealthPoint(punch.getDamage());
                    punch.finish();
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
