package com.taleofoverlord.game.Sprites;

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
        super(screen.getBossAtlas().findRegion("boss_stand"),false);
        this.world = screen.getWorld();

        define();

        bossStand = new TextureRegion(getTexture(), 0, 0, 128, 128);
        setRegion(bossStand);
        setBounds(0, 0, 64 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM);
    }

    public void define() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(new Vector2(512 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 / TaleOfOverlord.PPM, 20 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        super.setHealthPoint(TaleOfOverlord.BOSS_MAX_HP);


    }

    public void update(float delta) {
//        Gdx.app.log("HP", " "+ super.getHealthPoint());
        setPosition((b2Body.getPosition().x - getWidth() / 2) + getOffset(), b2Body.getPosition().y - getHeight() / 2);
        setRegion(bossStand);
    }

    public float getOffset() {
        return 0.10f * (super.checkIsRunningRight() ? 1 : -1);
    }

}
