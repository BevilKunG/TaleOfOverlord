package com.taleofoverlord.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Sprites.*;
import com.taleofoverlord.game.TaleOfOverlord;

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
                    if(checkCanAttack(target)) {
                        target.decreaseHealthPoint(bullet.getDamage());
                        target.cancelAction();
                        target.recoil();
                    }
                    bullet.finish();
                }
            }
        }

        if(Bullet.class.isAssignableFrom(fixA.getUserData().getClass()) && Bullet.class.isAssignableFrom(fixB.getUserData().getClass())) {
            Fixture bulletFixture1 = Bullet.class.isAssignableFrom(fixA.getUserData().getClass()) ? fixA : fixB;
            Fixture bulletFixture2 = fixA == bulletFixture1 ? fixB : fixA;

            Bullet bullet1 = (Bullet)bulletFixture1.getUserData();
            Bullet bullet2 = (Bullet)bulletFixture2.getUserData();
            if(bullet1.getTarget().hashCode() != bullet2.getTarget().hashCode()) {
                bullet1.finish();
                bullet2.finish();
            }
        }

        if(Bullet.class.isAssignableFrom(fixA.getUserData().getClass()) || Bullet.class.isAssignableFrom(fixB.getUserData().getClass())) {
            Fixture bulletFixture = Bullet.class.isAssignableFrom(fixA.getUserData().getClass()) ? fixA : fixB;
            Fixture slashedSwordFixture = fixA == bulletFixture ? fixB : fixA;
            if(slashedSwordFixture.getUserData() != null && SlashedSword.class.isAssignableFrom(slashedSwordFixture.getUserData().getClass())) {
                Bullet bullet = (Bullet) bulletFixture.getUserData();
                SlashedSword slashedSword = (SlashedSword) slashedSwordFixture.getUserData();
                bullet.finish();
                slashedSword.finish();
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
                    if(checkCanAttack(target)) {
                        target.decreaseHealthPoint(slashedSword.getDamage());
                        target.cancelAction();
                        target.recoil();
                    }
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
                    if(checkCanAttack(target)) {
                        target.decreaseHealthPoint(punch.getDamage());
                        target.cancelAction();
                        target.recoil();
                    }
                    punch.finish();
                }

            }
        }

        // Fighter Collision
        if(Fighter.class.isAssignableFrom(fixA.getUserData().getClass()) && Fighter.class.isAssignableFrom(fixB.getUserData().getClass())) {
            Fixture playerFixture = Player.class.isAssignableFrom(fixA.getUserData().getClass()) ? fixA : fixB;
            Fixture bossFixture = fixA == playerFixture ? fixB : fixA;
            if((playerFixture.getUserData() != null && Player.class.isAssignableFrom(playerFixture.getUserData().getClass())) && (bossFixture.getUserData() != null && Boss.class.isAssignableFrom(bossFixture.getUserData().getClass()))) {
                Player player = (Player) playerFixture.getUserData();
                Boss boss = (Boss) bossFixture.getUserData();
                player.decreaseHealthPoint(TaleOfOverlord.COLLISION_DAMAGE);
                player.cancelAction();
                if(player.b2Body.getPosition().y > boss.b2Body.getPosition().y) player.recoil(new Vector2(3f, 2f));
                else player.recoil();
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

    public boolean checkCanAttack(Fighter target) {
        return (Player.class.isAssignableFrom(target.getClass()) && !((Player)target).checkIsHurt()) || Boss.class.isAssignableFrom(target.getClass());
    }
}
