package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Player extends Sprite {

    public World world;
    public Body b2Body;

    private TextureRegion playerStand;

    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("72385462_2417083231953018_9201360505556434944_n"));
        this.world = world;
        definePlayer();

        playerStand = new TextureRegion(getTexture(),0,0,16,16);
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
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef);
    }

    public void update(float delta){
        setPosition(b2Body.getPosition().x - getWidth()/2, b2Body.getPosition().y - getHeight() /2);

    }
}
