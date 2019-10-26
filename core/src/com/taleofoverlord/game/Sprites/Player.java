package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Player extends Fighter {

    public World world;
    public Body b2Body;


    private TextureRegion playerStand, playerJump;
    private Animation playerRun, playerShoot, playerSlash, playerPunch;

    public enum State { STANDING, RUNNING, JUMPING, SHOOTING, SLASHING, PUNCHING}
    public State currentState;
    public State previousState;

    public float stateTimer;

    private boolean isShooting;
    private boolean isSlashing;
    private boolean isPunching;


    public Player(PlayScreen screen) {
        super(screen.getPlayerAtlas().findRegion("player_stand"), true);
        this.world = screen.getWorld();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        //
        //Run
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(screen.getPlayerAtlas().findRegion("player_run"), i * 128, 0, 128, 128));
        }
        playerRun = new Animation(0.25f, frames);
        frames.clear();

        //Shoot
        isShooting = false;
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getPlayerAtlas().findRegion("player_shoot"), i * 128, 0, 128, 128));
        }
        playerShoot = new Animation(0.1f, frames);
        frames.clear();

        //Slash
        isSlashing = false;
        for (int i = 1; i < 3; i++) {
            frames.add(new TextureRegion(screen.getPlayerAtlas().findRegion("player_slash"), i * 128, 0, 128, 128));
        }
        playerSlash = new Animation(0.1f, frames);
        frames.clear();

        //Punch
        isPunching = false;
        for (int i = 1; i < 3; i++) {
            frames.add(new TextureRegion(screen.getPlayerAtlas().findRegion("player_punch"), i * 128, 0, 128, 128));
        }
        playerPunch = new Animation(0.1f, frames);
        frames.clear();
        //

        define();
        playerStand = new TextureRegion(getTexture(), 0, 0, 128, 128);
        playerJump = new TextureRegion(screen.getPlayerAtlas().findRegion("player_jump"), 128, 0, 128, 128);
        setBounds(0, 0, 64 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM);
        setRegion(playerStand);
    }

    public void define() {
        // Player Create body
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / TaleOfOverlord.PPM, 32 / TaleOfOverlord.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        // Player Create fixture
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 / TaleOfOverlord.PPM, 20 / TaleOfOverlord.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        //Player Health Point
        super.setHealthPoint(TaleOfOverlord.PLAYER_MAX_HP);

    }

    public void update(float delta) {
//        Gdx.app.log("HP"," "+super.getHealthPoint());
        setPosition((b2Body.getPosition().x - getWidth() / 2) + getOffset(), b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public float getOffset() {
        return 0.10f * (super.checkIsRunningRight() ? 1 : -1);
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case RUNNING: region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case JUMPING: region = playerJump;
                break;
            case SHOOTING: region = (TextureRegion) playerShoot.getKeyFrame(stateTimer, false);
                break;
            case SLASHING: region = (TextureRegion) playerSlash.getKeyFrame(stateTimer, false);
                break;
            case PUNCHING: region = (TextureRegion) playerPunch.getKeyFrame(stateTimer, false);
                break;
            case STANDING:
            default: region = playerStand;
                break;
        }

        if((b2Body.getLinearVelocity().x < -TaleOfOverlord.FLIP_EPSILON || !super.checkIsRunningRight()) && !region.isFlipX() ) {
            region.flip(true, false);
            super.setRunningRight(false);
        } else if((b2Body.getLinearVelocity().x > TaleOfOverlord.FLIP_EPSILON || super.checkIsRunningRight()) && region.isFlipX()) {
            region.flip(true, false);
            super.setRunningRight(true);
        }

        stateTimer = currentState == previousState ? stateTimer+delta : 0;
        previousState = currentState;
        return region;
    }


    public State getState() {
        if(isShooting) {
            return State.SHOOTING;
        } else if(isSlashing) {
            return State.SLASHING;
        }
        else if(isPunching) {
            return State.PUNCHING;
        } else if(b2Body.getLinearVelocity().y > TaleOfOverlord.JUMP_EPSILON){
            Gdx.app.log("OK", "JUMP");
            return State.JUMPING;
        } else if(b2Body.getLinearVelocity().x >= TaleOfOverlord.FLIP_EPSILON || b2Body.getLinearVelocity().x <= -TaleOfOverlord.FLIP_EPSILON) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }


    @Override
    public Vector2 getFrontPosition() {
        return new Vector2(b2Body.getPosition().x + (0.24f * (super.checkIsRunningRight()? 1 : -1)), b2Body.getPosition().y + 0.12f);
    }

    public void shoot() {
        isShooting = true;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isShooting = false;
            }
        }, 0.5f);
    }

    public void slash() {
        isSlashing = true;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isSlashing = false;
            }
        }, 0.3f);
    }

    public void punch() {
        isPunching = true;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isPunching = false;
            }
        }, 0.3f);
    }

    public boolean checkIsJumping() {
        return currentState == State.JUMPING;
    }
}
