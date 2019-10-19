package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Bullet extends Sprite {
    public World world;
    public Body b2body;

//    public Player player;

    private Fighter shooter;
    private Fighter target;
    private int damage;
    private boolean isFinished;

    public Bullet(PlayScreen screen,Fighter shooter, Fighter target) {
        this.world = screen.getWorld();
        this.shooter = shooter;
        this.target = target;

        define();
    }

    public void define() {
        damage = TaleOfOverlord.BULLET_DAMAGE;
        isFinished = false;

        BodyDef bdef = new BodyDef();
        bdef.position.set(shooter.getFrontPosition());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        b2body.setGravityScale(0);
        b2body.setLinearVelocity(new Vector2(4.0f * (shooter.checkIsRunningRight()? 1 : -1), 0));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isFinished = true;
            }
        }, 0.5f);
    }

    public void update() {
    }

    public Fighter getTarget() {
        return target;
    }

    public int getDamage() {
        return damage;
    }

    public void finish() {
        isFinished = true;
    }

    public boolean checkIsFinished() {
        return isFinished;
    }

}
