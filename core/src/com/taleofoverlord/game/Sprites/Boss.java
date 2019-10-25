package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Boss extends Fighter {
    public World world;
    public Body b2Body;

    private TextureRegion bossStand;

    @Override
    public Vector2 getFrontPosition() {
        return null;
    }

    public enum State { STANDING };

    public Boss(PlayScreen screen) {
        super(screen.getAtlas().findRegion("player_stand"),false);
//        super(screen.getAtlas().findRegion(""));
        this.world = screen.getWorld();
        defineBoss();
    }

    public void defineBoss() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(new Vector2(512 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(24 / TaleOfOverlord.PPM, 32 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        super.setHealthPoint(TaleOfOverlord.BOSS_MAX_HP);
    }

    public void update() {
//        Gdx.app.log("HP", " "+ super.getHealthPoint());
    }

}
