package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Player extends Sprite {

    public World world;
    public Body b2Body;

    private int healthPoint;

    private TextureRegion playerStand;


    public Player(World world, PlayScreen screen) {
//        super(screen.getPlayerAtlas().findRegion("player_stand"));
        this.world = world;
        definePlayer();
//        playerStand = new TextureRegion(getTexture(),0,0,128,128);
//        setBounds(0,0,128/TaleOfOverlord.PPM, 128/TaleOfOverlord.PPM);
//        setRegion(playerStand);

    }

    public void definePlayer() {
        // Player Create body
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / TaleOfOverlord.PPM, 32 / TaleOfOverlord.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        // Player Create fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef);

        //Player Health Point
        healthPoint = 100;


    }

}
