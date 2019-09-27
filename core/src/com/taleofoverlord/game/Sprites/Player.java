package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Sprite {

    public World world;
    public Body b2Body;

    public Player(World world) {
        this.world = world;
        definePlayer();
    }

    public void definePlayer() {
        // Player Create body
        BodyDef bdef = new BodyDef();
        bdef.position.set(32, 32);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        // Player Create fixture
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);
        fdef.shape = shape;
        b2Body.createFixture(fdef);
    }
}
