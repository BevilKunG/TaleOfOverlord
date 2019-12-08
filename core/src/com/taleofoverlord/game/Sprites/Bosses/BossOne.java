package com.taleofoverlord.game.Sprites.Bosses;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.Sprites.Animations.AnimationFactory;
import com.taleofoverlord.game.Sprites.Animations.AnimationPack;
import com.taleofoverlord.game.Sprites.Animations.AtlasFactory;
import com.taleofoverlord.game.Sprites.Animations.StandFactory;
import com.taleofoverlord.game.Sprites.Player;
import com.taleofoverlord.game.TaleOfOverlord;

public class BossOne extends Boss {
    private AnimationPack bossMelee, bossShoot, bossPrepareBlink,
            bossBlink, bossThrow, bossTransform,
            bossFinalBlink, bossFinalUltimate, bossDead;

    private boolean isBluffBlink;

    public int bigAttackCounter, bigAttackLeft;

    public BossOne(PlayScreen screen) {
        super(StandFactory.getFactory().getBossOneStand(),false);

        super.init(screen);
        bigAttackCounter = 1;
        bigAttackLeft = 0;

        define();
        createAnimationPacks();
    }

    @Override
    protected void createBossStand() {
        bossStand = StandFactory.getFactory().getBossOneStand();
        bossFinalStand = StandFactory.getFactory().getBossOneFinalStand();
    }

    protected void define() {
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

    private void createAnimationPacks() {
        bossMelee = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSMELEE);
        bossShoot = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSSHOOT);
        bossPrepareBlink = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSPREPAREBLINK);
        bossBlink = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSBLINK);
        bossThrow = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSTHROW);
        bossTransform = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSTRANSFORM);
        bossFinalBlink = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSFINALBLINK);
        bossFinalUltimate = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSFINALULTIMATE);
        bossDead = animationFactory.getBossOneAnimationPack(AnimationFactory.AnimationType.BOSSDEAD);
    }


    private State getTinyAttack() {
        int percent = randomPercent();
        if(percent<=60) return State.PREPAREBLINK;
        else if(percent<=90) return State.THROWING;
        else return State.BLINK;
    }

    private State getBigAttack() {
        return State.BLINK;
    }

    private State getBlinkAttack() {
        int percent = randomPercent();
        if(percent<=50) return State.PREPAREBLINK;
        else return State.BLINK;
    }

    private State getBluffBlink() {
        isBluffBlink = true;
        return getBlinkAttack();
    }

    private State getFinalAttack() {
        if(!isTransform) return State.TRANSFORMING;
        return State.FINALBLINK;
    }

    private State getPatternState() {
        State state = State.STANDING;
        if(getPercentHP()>60) {
            setWaitingTime(2f);
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
            setWaitingTime(1f);
            if(randomPercent()<=60) state = getBlinkAttack();
            else state = getBluffBlink();
        } else {
            setWaitingTime(1.5f);
            state = getFinalAttack();
        }
        return state;
    }

    private void runPattern(int healthPoint) {
        switch (getPatternState()) {
                case PREPAREBLINK: {
                    bossPrepareBlink.active();
                    if(!isBluffBlink) prepareBlinkAndMelee();
                    else bluffBlink();
                    break;
                }
                case BLINK: {
                    bossBlink.active();
                    if(!isBluffBlink) blinkAndShoot();
                    else bluffBlink();
                    break;
                }
                case FINALBLINK: {
                    bossFinalBlink.active();
                    finalBlinkAndUltimate();
                    break;
                }
                case THROWING: {
                    bossThrow.active();
                    throwBomb();
                    break;
                }
                case TRANSFORMING: {
                    isTransform = true;
                    bossTransform.active();
                    transform();
                    break;
                }
                case DEAD: bossDead.active();
                break;
            }
    }

    private TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region = getAnimationRegion(currentState);
        handleFlipingRegion(region);

        stateTimer = currentState == previousState ? stateTimer+delta : 0;
        previousState = currentState;
        return region;
    }

    private TextureRegion getAnimationRegion(State currentState) {
        switch (currentState) {
            case MELEE: return (TextureRegion) bossMelee.animation.getKeyFrame(stateTimer, true);
            case SHOOTING: return (TextureRegion) bossShoot.animation.getKeyFrame(stateTimer, true);
            case PREPAREBLINK: return (TextureRegion) bossPrepareBlink.animation.getKeyFrame(stateTimer, true);
            case BLINK: return (TextureRegion) bossBlink.animation.getKeyFrame(stateTimer, true);
            case THROWING: return (TextureRegion) bossThrow.animation.getKeyFrame(stateTimer, true);
            case TRANSFORMING: return (TextureRegion) bossTransform.animation.getKeyFrame(stateTimer, false);
            case FINALBLINK: return (TextureRegion) bossFinalBlink.animation.getKeyFrame(stateTimer, true);
            case FINALULTIMATE: return (TextureRegion) bossFinalUltimate.animation.getKeyFrame(stateTimer, true);
            case DEAD: return (TextureRegion) bossDead.animation.getKeyFrame(stateTimer, true);
            case STANDING:
            default: return !isTransform? bossStand : bossFinalStand;
        }
    }


    private State getState() {
        if(bossDead.isActive) {
            return State.DEAD;
        } else if(bossTransform.isActive) {
            return State.TRANSFORMING;
        } else if(bossPrepareBlink.isActive) {
            return State.PREPAREBLINK;
        } else if(bossBlink.isActive) {
            return State.BLINK;
        } else if(bossFinalBlink.isActive) {
            return State.FINALBLINK;
        } else if(bossFinalUltimate.isActive) {
            return State.FINALULTIMATE;
        } else if(bossMelee.isActive) {
            return State.MELEE;
        } else if(bossShoot.isActive) {
          return State.SHOOTING;
        } else if(bossThrow.isActive) {
            return  State.THROWING;
        } else {
            return State.STANDING;
        }
    }

    private Vector2 getBlinkPosition() {
        boolean isBlinkToFront = randomPercent()%2==0;
        boolean isSpace = bossBlink.isActive || bossThrow.isActive;
        if(isBlinkToFront) return new Vector2(player.getFrontPosition().x + ((player.checkIsRunningRight()?1f:-1f) * (isSpace?1:0)), player.getFrontPosition().y);
        else return new Vector2(player.getBackPosition().x + ((player.checkIsRunningRight()?-1f:1f) * (isSpace?1:0)), player.getBackPosition().y);
    }

    private void blink() {
        Vector2 blinkedPosition = getBlinkPosition();
        b2Body.setTransform(blinkedPosition, 0);
    }

    private void transform() {
        bossTransform.active();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                bossTransform.finish();
            }
        }, 5.5f);
    }

    private void bluffBlink() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                blink();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isBluffBlink = false;
                        bossPrepareBlink.finish();
                        bossBlink.finish();
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                waitAction();
                            }
                        },0.2f);
                    }
                }, 1f);
            }
        }, 1f);
    }

    private void prepareBlinkAndMelee() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                blink();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        bossPrepareBlink.finish();
                        bossMelee.active();
                        TaleOfOverlord.manager.get("audio/sounds/boss_melee.mp3", Sound.class).play();
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                bossMelee.finish();
                                waitAction();
                                setIsSwordCreated(false);
                            }
                        },0.2f);
                    }
                }, 1f);
            }
        }, 1f);
    }

    private void blinkAndShoot() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                blink();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        bossBlink.finish();
                        bossShoot.active();
                        TaleOfOverlord.manager.get("audio/sounds/boss_gunshot.wav", Sound.class).play();
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                bossShoot.finish();
                                waitAction();
                                setIsBulletCreated(false);
                            }
                        },0.8f);
                    }
                },1f);

            }
        }, 1f);
    }

    private void finalBlinkAndUltimate() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                blink();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        bossFinalBlink.finish();
                        bossFinalUltimate.active();
                        TaleOfOverlord.manager.get("audio/sounds/boss_melee.mp3", Sound.class).play();
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                bossFinalUltimate.finish();
                                waitAction();
                                setIsSwordCreated(false);
                            }
                        },0.6f);
                    }
                }, 1f);
            }
        }, 1f);
    }

    private void throwBomb() {
        blink();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                bossThrow.finish();
                waitAction();
            }
        },1.25f);
    }

    public void update(float delta) {
        if(!checkIsAction()) {
            runPattern(super.getHealthPoint());
        }
        setPosition((b2Body.getPosition().x - getWidth() / 2) + getOffset(), b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public void cancelAction() {
        if(!isWait) bigAttackCounter++;
//        bossShoot.finish();
//        bossMelee.finish();
//        bossThrow.finish();
//        bossPrepareBlink.finish();
//        bossBlink.finish();
//        isWait = true;
    }

    public boolean checkIsAction() {
        return isWait ||
                bossBlink.isActive ||
                bossPrepareBlink.isActive ||
                bossMelee.isActive ||
                bossShoot.isActive ||
                bossThrow.isActive ||
                bossTransform.isActive ||
                bossFinalBlink.isActive ||
                bossFinalUltimate.isActive;
    }

    @Override
    public boolean checkIsMelee() {
        return bossMelee.isActive || bossFinalUltimate.isActive;
    }

    @Override
    public boolean checkIsShooting() {
        return bossShoot.isActive;
    }
}
