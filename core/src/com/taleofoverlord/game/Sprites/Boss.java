package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

import java.util.Random;

public class Boss extends Fighter {
    public World world;
    public Body b2Body;

    private TextureRegion bossStand;
    private Animation bossMelee, bossShoot, bossPrepareBlink, bossBlink;

    private boolean isPrepareBlink;
    private boolean isMelee;
    private boolean isBlink;
    private boolean isShoot;
    private boolean isWait;
    private boolean isSwordCreated;

    private Vector2 currentPosition;


    private Player player;

    @Override
    public Vector2 getFrontPosition() {
        return new Vector2(b2Body.getPosition().x + (0.24f * (super.checkIsRunningRight()? 1 : -1)), b2Body.getPosition().y - 0.05f);
    }

    public enum State { STANDING, MELEE, SHOOTING, PREPAREBLINK, BLINK };
    public State currentState;
    public State previousState;
    public Array<State> actionStates;

    public float stateTimer;

    public Boss(PlayScreen screen) {
        super(screen.getBossAtlas().findRegion("boss_stand"),false);
        this.world = screen.getWorld();
        currentPosition = new Vector2(512 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM);
        define();

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        bossStand = new TextureRegion(screen.getBossAtlas().findRegion("boss_stand"), 0, 0, 128, 128);
        setRegion(bossStand);
        setBounds(0, 0, 64 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM);

        Array<TextureRegion> frames = new Array<TextureRegion>();
        // Melee
        isMelee = false;
        for(int i=0; i<2; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_melee"), i * 128, 0, 128, 128));
        }
        bossMelee = new Animation(0.1f, frames);
        frames.clear();

        // Shoot
        isShoot = false;
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_shoot1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_shoot2"), i * 128, 0, 128, 128));
        }
        bossShoot = new Animation(0.1f, frames);
        frames.clear();

        // Prepare blink
        isPrepareBlink = false;
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_prepareBlink"), i * 128, 0, 128, 128));
        }
        bossPrepareBlink = new Animation(0.25f, frames);
        frames.clear();

        // Blink
        isBlink = false;
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_blink"), i * 128, 0, 128, 128));
        }
        bossBlink = new Animation(0.25f, frames);
        frames.clear();

        actionStates = new Array<State> ();
        actionStates.add(State.BLINK);
        actionStates.add(State.PREPAREBLINK);

        isWait = false;

        player = screen.getPlayer();

        isSwordCreated = false;
    }

    public void define() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(new Vector2(currentPosition.x, currentPosition.y));
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
        if(!isWait && !isMelee && !isShoot && !isBlink && !isPrepareBlink) {
            isPrepareBlink = actionStates.random() == State.PREPAREBLINK;
            isBlink = !isPrepareBlink;
            if(isPrepareBlink) melee();
            else shoot();
        }
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
            case MELEE: region = (TextureRegion) bossMelee.getKeyFrame(stateTimer, true);
                break;
            case SHOOTING: region = (TextureRegion) bossShoot.getKeyFrame(stateTimer, true);
                break;
            case PREPAREBLINK: region = (TextureRegion) bossPrepareBlink.getKeyFrame(stateTimer, true);
                break;
            case BLINK: region = (TextureRegion) bossBlink.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default: region = bossStand;
                break;
        }

        if(!checkInFrontOfPlayer()) {
            if(player.checkIsRunningRight() && this.checkIsRunningRight()) {
                region.flip(true, false);
                super.setRunningRight(false);
            } else if(!player.checkIsRunningRight() && !this.checkIsRunningRight()) {
                region.flip(true, false);
                super.setRunningRight(true);
            }
        }
        if(this.checkIsRunningRight() != region.isFlipX()) {
            region.flip(true, false);
        }


        stateTimer = currentState == previousState ? stateTimer+delta : 0;
        previousState = currentState;
        return region;
    }


    public State getState() {
        if(isPrepareBlink) {
            return State.PREPAREBLINK;
        } else if(isBlink) {
            return State.BLINK;
        } else if(isMelee) {
            return State.MELEE;
        } else if(isShoot) {
          return State.SHOOTING;
        } else {
            return State.STANDING;
        }
    }

    public void melee() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isPrepareBlink = false;
                isMelee = true;
                b2Body.setTransform(player.getFrontPosition(), 0);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isMelee = false;
                        isWait = true;
                        waitAction();
                        setIsSwordCreated(false);
                    }
                },0.2f);
            }
        }, 1f);
    }

    public void shoot() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isBlink = false;
                isShoot = true;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isShoot = false;
                        isWait = true;
                        waitAction();
                    }
                },0.8f);
            }
        }, 1f);
    }

    public void waitAction() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isWait = false;
            }
        },1f);
    }

    public boolean checkInFrontOfPlayer() {
        return super.checkIsRunningRight()? player.b2Body.getPosition().x > b2Body.getPosition().x : player.b2Body.getPosition().x < b2Body.getPosition().x;
    }

    public boolean checkIsMelee(){
        return  isMelee;
    }

    public boolean checkIsSwordCreated(){
        return isSwordCreated;
    }
    public void setIsSwordCreated(boolean isSwordCreated){
        this.isSwordCreated = isSwordCreated;
    }
}
