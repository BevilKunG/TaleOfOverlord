package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Boss extends Sprite {
    public World world;
    public Body b2body;

    private static int healthPoint;

    private TextureRegion bossStand;
    public enum State { STANDING };

    public Boss(PlayScreen screen) {
//        super(screen.getAtlas().findRegion(""));
        this.world = screen.getWorld();
        defineBoss();
    }

    public void defineBoss() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(new Vector2(1024 / TaleOfOverlord.PPM, 32 / TaleOfOverlord.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(24 / TaleOfOverlord.PPM, 32 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData("boss");

        healthPoint = TaleOfOverlord.BOSS_MAX_HP;
    }

}
