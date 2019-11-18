package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Boss extends Fighter {
    public World world;
    public Body b2Body;

    private TextureRegion bossStand;
    private Animation bossMelee, bossShoot, bossPrepareBlink, bossBlink, bossThrowing;

    private boolean isPrepareBlink;
    private boolean isMelee;
    private boolean isBlink;
    private boolean isShooting;
    private boolean isWait;
    private boolean isSwordCreated;
    private boolean isBulletCreated;
    private boolean isThrowing;
    private boolean isBluffBlink;

    private Vector2 currentPosition;


    private Player player;

    public enum State { STANDING, MELEE, SHOOTING, PREPAREBLINK, BLINK, THROWING };
    public State currentState;
    public State previousState;

    public float stateTimer;
    public int bigAttackCounter, bigAttackLeft;

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
        isBulletCreated = false;
        isShooting = false;
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

        // THROWING
        isThrowing = false;
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_throwing"), i * 128, 0, 128, 128));
        }
        bossThrowing = new Animation(0.25f, frames);
        frames.clear();

        //

        bigAttackCounter = 1;
        bigAttackLeft = 0;



        isWait = false;

        player = screen.getPlayer();

        isSwordCreated = false;

        isBluffBlink = false;
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
        if(!checkIsAction()) {
            runPattern(super.getHealthPoint());
        }
        setPosition((b2Body.getPosition().x - getWidth() / 2) + getOffset(), b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public int randomPercent() {
        return (int)(Math.random()*100);
    }

    public float getPercentHP() {
        return ((float)getHealthPoint()/TaleOfOverlord.BOSS_MAX_HP)*100;
    }

    public State getTinyAttack() {
        int percent = randomPercent();
        if(percent<=60) return State.PREPAREBLINK;
        else if(percent<=90) return State.THROWING;
        else return State.BLINK;
    }

    public State getBigAttack() {
        return State.BLINK;
    }

    public State getBlinkAttack() {
        int percent = randomPercent();
        if(percent<=50) return State.PREPAREBLINK;
        else return State.BLINK;
    }

    public State getBluffBlink() {
        isBluffBlink = true;
        return getBlinkAttack();
    }

    public State getPatternState() {
        State state = State.STANDING;
        if(getPercentHP()>60) {
            if(bigAttackCounter%10!=0) state = getTinyAttack();
            if(bigAttackCounter%10==0 && bigAttackLeft==0) {
                bigAttackLeft = 2;
                isWait = true;
                waitAction();
            }
            if(bigAttackLeft!=0 && !isWait) {
                state = getBigAttack();
                bigAttackLeft--;
            }
        } else if(getPercentHP()>40) {
            if(randomPercent()<=60) state = getBlinkAttack();
            else state = getBluffBlink();
        } else {

        }
        return state;
    }

    public void runPattern(int healthPoint) {
        switch (getPatternState()) {
                case PREPAREBLINK: {
                    isPrepareBlink = true;
                    if(!isBluffBlink) prepareBlinkAndMelee();
                    else bluffBlink();
                    break;
                }
                case BLINK: {
                    isBlink = true;
                    if(!isBluffBlink) blinkAndShoot();
                    else bluffBlink();
                    break;
                }
                case THROWING: {
                    isThrowing = true;
                    throwBomb();
                    break;
                }
            }
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
            case THROWING: region = (TextureRegion) bossThrowing.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            default: region = bossStand;
                break;
        }

        if(!checkInFrontOfPlayer()) {
            region.flip(true, false);
            super.setRunningRight(!checkIsRunningRight());
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
        } else if(isShooting) {
          return State.SHOOTING;
        } else if(isThrowing) {
            return  State.THROWING;
        } else {
            return State.STANDING;
        }
    }

    public Vector2 getBlinkPosition() {
        boolean isBlinkToFront = randomPercent()%2==0;
        boolean isSpace = isBlink || isThrowing;
        if(isBlinkToFront) return new Vector2(player.getFrontPosition().x + ((player.checkIsRunningRight()?1f:-1f) * (isSpace?1:0)), player.getFrontPosition().y);
        else return new Vector2(player.getBackPosition().x + ((player.checkIsRunningRight()?-1f:1f) * (isSpace?1:0)), player.getBackPosition().y);
    }


    @Override
    public Vector2 getFrontPosition() {
        return new Vector2(b2Body.getPosition().x + (0.24f * (super.checkIsRunningRight()? 1 : -1)), b2Body.getPosition().y);
    }

    public Vector2 getBackPosition() {
        return new Vector2(b2Body.getPosition().x + (0.24f * (super.checkIsRunningRight()? -1 : 1)), b2Body.getPosition().y);
    }

    @Override
    public void recoil() {
        b2Body.applyLinearImpulse(new Vector2(1f * (checkIsRunningRight()? -1:1), 0), b2Body.getWorldCenter(), true);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                stopMoving();
            }
        }, 0.2f);
    }

    public void recoil(Vector2 recoilFactor) {
        b2Body.applyLinearImpulse(new Vector2(1f * recoilFactor.x * (checkIsRunningRight()? -1:1), 1f * recoilFactor.y), b2Body.getWorldCenter(), true);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                stopMoving();
            }
        }, 0.2f);
    }

    public void stopMoving() {
        b2Body.setLinearVelocity(new Vector2(0, 0));
    }

    public void blink() {
        Vector2 blinkedPosition = getBlinkPosition();
        b2Body.setTransform(blinkedPosition, 0);
    }

    public void bluffBlink() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                blink();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isBluffBlink = false;
                        isPrepareBlink = false;
                        isBlink = false;
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                isWait = true;
                                waitAction();
                            }
                        },0.2f);
                    }
                }, 1f);
            }
        }, 1f);
    }

    public void prepareBlinkAndMelee() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                blink();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isPrepareBlink = false;
                        isMelee = true;
                        TaleOfOverlord.manager.get("audio/sounds/boss_melee.mp3", Sound.class).play();
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
        }, 1f);
    }

    public void blinkAndShoot() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                blink();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isBlink = false;
                        isShooting = true;
                        TaleOfOverlord.manager.get("audio/sounds/boss_gunshot.wav", Sound.class).play();
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                isShooting = false;
                                isWait = true;
                                waitAction();
                                setIsBulletCreated(false);
                            }
                        },0.8f);
                    }
                },1f);

            }
        }, 1f);
    }

    public void throwBomb() {
        blink();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isThrowing = false;
                isWait = true;
                waitAction();
            }
        },1.25f);
    }

    public void waitAction() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isWait = false;
            }
        },1f);
    }


    public void cancelAction() {
        if(!isWait) bigAttackCounter++;
//        isShooting = false;
//        isMelee = false;
//        isThrowing = false;
//        isPrepareBlink = false;
//        isBlink = false;
//        isWait = true;
    }

    public boolean checkInFrontOfPlayer() {
        return super.checkIsRunningRight()? player.b2Body.getPosition().x > b2Body.getPosition().x : player.b2Body.getPosition().x < b2Body.getPosition().x;
    }

    public boolean checkIsAction() {
        return isWait || isBlink || isPrepareBlink || isMelee || isShooting || isThrowing;
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

    public boolean checkIsShooting(){
        return isShooting;
    }

    public boolean checkIsBulletCreated(){
        return  isBulletCreated;
    }
    public void setIsBulletCreated(boolean isBulletCreated){
        this.isBulletCreated = isBulletCreated;
    }

}
