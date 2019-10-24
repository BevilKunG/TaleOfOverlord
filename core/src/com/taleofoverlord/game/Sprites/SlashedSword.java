package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class SlashedSword {
    public World world;
    public Body b2Body;

    private Fighter slasher;
    private Fighter target;
    private int damage;
    private boolean isFinished;

    public SlashedSword(PlayScreen screen, Fighter slasher, Fighter target) {
        this.world = screen.getWorld();
        this.slasher = slasher;
        this.target = target;

        define();
    }

    public void define() {
        damage = TaleOfOverlord.SLASH_DAMAGE;
        isFinished = false;

        BodyDef bdef = new BodyDef();
        bdef.position.set(slasher.getFrontPosition());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        EdgeShape shape = new EdgeShape();
        shape.set(new Vector2(0, 0), new Vector2( TaleOfOverlord.SLASH_RANGE * (slasher.checkIsRunningRight()? 1:-1), 0));
        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        b2Body.setGravityScale(0);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isFinished = true;
            }
        }, 0.3f);
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
