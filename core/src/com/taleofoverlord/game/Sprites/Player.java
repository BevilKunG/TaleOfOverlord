package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Player extends Sprite {

    public World world;
    public Body b2Body;

    private static int healthPoint;

    private TextureRegion playerStand;
    private Animation playerRun;

    public enum State { STANDING, RUNNING }
    public State currentState;
    public State previousState;

    public float stateTimer;

    private boolean isRunningRight;


    public Player(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("player_stand"));
        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;
        //
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("player_run"), i * 128, 0, 128, 128));
        }
        playerRun = new Animation(0.1f, frames);
        frames.clear();
        //

        definePlayer();
        playerStand = new TextureRegion(getTexture(), 4 * 128, 0, 128, 128);
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
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(24 / TaleOfOverlord.PPM, 32 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef);

        //Player Health Point
        healthPoint = TaleOfOverlord.PLAYER_MAX_HP;


    }

    public void update(float delta) {
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case RUNNING: region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default: region = playerStand;
                break;
        }

        if((b2Body.getLinearVelocity().x < 0 || !isRunningRight) && !region.isFlipX() ) {
            region.flip(true, false);
            isRunningRight = false;
        } else if((b2Body.getLinearVelocity().x > 0 || isRunningRight) && region.isFlipX()) {
            region.flip(true, false);
            isRunningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer+delta : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if(b2Body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

}
