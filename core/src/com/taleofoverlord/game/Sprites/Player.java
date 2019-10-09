package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Player extends Sprite {

    public World world;
    public Body b2Body;

    private int healthPoint;

    private TextureRegion playerStand;


    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("player_stand"));
        this.world = world;
        definePlayer();
        playerStand = new TextureRegion(getTexture(), 0, 0, 128, 128);
        setBounds(0, 0, 64 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM);
        setRegion(playerStand);
    }

    public void definePlayer() {
        // Player Create body
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / TaleOfOverlord.PPM, 32 / TaleOfOverlord.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        // Player Create fixture
        FixtureDef fdef = new FixtureDef();
//        CircleShape shape = new CircleShape();
//        shape.setRadius(5 / TaleOfOverlord.PPM);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32 / TaleOfOverlord.PPM, 32 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef);

        //Player Health Point
        healthPoint = 100;


    }

    public void update(float delta) {
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
    }

}
