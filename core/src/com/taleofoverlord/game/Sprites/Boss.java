package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

import java.util.Random;

public class Boss extends Fighter {
    public World world;
    public Body b2Body;

    private TextureRegion bossStand;

    private boolean isShooting;
    private boolean isSlashing;
    private boolean isPunching;

    private Player player;

    public Random random;
    private int randomState;

    @Override
    public Vector2 getFrontPosition() {
        return null;
    }

    public enum State { STANDING };
    public State currentState;
    public State previousState;

    public float stateTimer;

    public Boss(PlayScreen screen) {
        super(screen.getBossAtlas().findRegion("boss_stand"),false);
        this.world = screen.getWorld();
        define();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        bossStand = new TextureRegion(getTexture(), 0, 0, 128, 128);
        setRegion(bossStand);
        setBounds(0, 0, 64 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM);

//        isPunching = false;
//        isShooting = false;
//        isSlashing = false;

        player = screen.getPlayer();

        random = new Random();
        randomState = random.nextInt(3);
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
//        if(!isShooting && !isSlashing && !isPunching) {
//            randomState = random.nextInt(100) % 4;
//            setStateRandom();
//        }
        setPosition((b2Body.getPosition().x - getWidth() / 2) + getOffset(), b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public float getOffset() {
        return 0.10f * (super.checkIsRunningRight() ? 1 : -1);
    }

    public void setStateRandom() {
//        switch (randomState) {
//            case 0: prepare();
//        }
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case STANDING:
            default: region = bossStand;
                break;
        }

//        if((b2Body.getLinearVelocity().x < -TaleOfOverlord.FLIP_EPSILON || !super.checkIsRunningRight()) && !region.isFlipX() ) {
//            region.flip(true, false);
//            super.setRunningRight(false);
//        } else if((b2Body.getLinearVelocity().x > TaleOfOverlord.FLIP_EPSILON || super.checkIsRunningRight()) && region.isFlipX()) {
//            region.flip(true, false);
//            super.setRunningRight(true);
//        }

        stateTimer = currentState == previousState ? stateTimer+delta : 0;
        previousState = currentState;
        return region;
    }


    public State getState() {
       return State.STANDING;
    }

}
