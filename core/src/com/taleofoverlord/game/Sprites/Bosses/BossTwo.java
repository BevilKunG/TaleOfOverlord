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

public class BossTwo extends Boss {
    private AnimationPack bossBlink, bossShoot, bossTransform,
            bossFinalMelee, bossDead, bossFinalBlink;

    private int blinkCounter;

    public BossTwo(PlayScreen screen) {
        super(StandFactory.getFactory().getBossTwoStand(), false);

        super.init(screen);
        blinkCounter = 0;
        setIsIgnoreBullet(true);
        define();
        createAnimationPack();
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

    public void update(float delta) {
        if(!isDead && !checkIsAction()) {
            runPattern(super.getHealthPoint());
        }
        setPosition((b2Body.getPosition().x - getWidth() / 2) + getOffset(), b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    @Override
    protected void createBossStand() {
        bossStand = StandFactory.getFactory().getBossTwoStand();
        bossFinalStand = StandFactory.getFactory().getBossTwoFinalStand();
    }

    private void createAnimationPack() {
        bossBlink = animationFactory.getBossTwoAnimationPack(AnimationFactory.AnimationType.BOSSBLINK);
        bossShoot = animationFactory.getBossTwoAnimationPack(AnimationFactory.AnimationType.BOSSSHOOT);
        bossTransform = animationFactory.getBossTwoAnimationPack(AnimationFactory.AnimationType.BOSSTRANSFORM);
        bossFinalMelee = animationFactory.getBossTwoAnimationPack(AnimationFactory.AnimationType.BOSSFINALMELEE);
        bossDead = animationFactory.getBossTwoAnimationPack(AnimationFactory.AnimationType.BOSSDEAD);
        bossFinalBlink = animationFactory.getBossTwoAnimationPack(AnimationFactory.AnimationType.BOSSFINALBLINK);
    }



    private boolean checkIsAction() {
        return isWait ||
                bossBlink.isActive ||
                bossShoot.isActive ||
                bossTransform.isActive ||
                bossFinalMelee.isActive ||
                bossDead.isActive ||
                bossFinalBlink.isActive;
    }

    private State getFinalAttack() {
        if(!isTransform) return State.TRANSFORMING;
        return State.FINALBLINK;
    }

    private State getPatternState() {
        State state = State.STANDING;
        if(getPercentHP() <= 0) return State.DEAD;
        if(getPercentHP() > 60) {
            setWaitingTime(1f);
            state = blinkCounter>3 ? State.BLINK : State.SHOOTING;

        }  else {
            state = getFinalAttack();
        }

        return state;
    }

    private void runPattern(int healthPoint) {
        switch (getPatternState()) {
            case SHOOTING: {
                bossShoot.active();
                shoot();
                break;
            }
            case BLINK: {
                bossBlink.active();
                blinkAndShoot();
                blinkCounter = 0;
                break;
            }
            case FINALBLINK: {
                bossFinalBlink.active();
                if(getPercentHP() < 40) {
                    shoot();
                }
                finalBlinkAndFinalMelee();
                break;
            }
            case TRANSFORMING: {
                isTransform = true;
                bossTransform.active();
                transform();
                break;
            }
            case DEAD: {
                bossDead.active();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        isDead = true;
                    }
                }, 2.5f);
            }
                break;
        }
    }

    private State getState() {
        if(bossDead.isActive) {
            return State.DEAD;
        } else if(bossTransform.isActive) {
            return State.TRANSFORMING;
        } else if(bossBlink.isActive) {
            return State.BLINK;
        } else if(bossFinalBlink.isActive) {
            return State.FINALBLINK;
        } else if(bossFinalMelee.isActive) {
            return State.FINALMELEE;
        } else if(bossShoot.isActive) {
            return State.SHOOTING;
        } else {
            return State.STANDING;
        }
    }

    private TextureRegion getAnimationRegion(State currentState) {
        switch (currentState) {
            case BLINK: return (TextureRegion) bossBlink.animation.getKeyFrame(stateTimer, true);
            case SHOOTING: return (TextureRegion) bossShoot.animation.getKeyFrame(stateTimer, true);
            case TRANSFORMING: return (TextureRegion) bossTransform.animation.getKeyFrame(stateTimer, false);
            case FINALMELEE: return (TextureRegion) bossFinalMelee.animation.getKeyFrame(stateTimer, true);
            case DEAD: return (TextureRegion) bossDead.animation.getKeyFrame(stateTimer, false);
            case FINALBLINK: return (TextureRegion) bossFinalBlink.animation.getKeyFrame(stateTimer, true);
            case STANDING:
            default: return !isTransform ? bossStand : bossFinalStand;
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

    @Override
    public void cancelAction() {
        if(!isWait) blinkCounter++;
    }

    private Vector2 getBlinkPosition() {
        boolean isBlinkToFront = randomPercent()%2==0;
        boolean isSpace = bossBlink.isActive;
        if(isBlinkToFront) return new Vector2(player.getFrontPosition().x + ((player.checkIsRunningRight()?1f:-1f) * (isSpace?1:0)), player.getFrontPosition().y);
        else return new Vector2(player.getBackPosition().x + ((player.checkIsRunningRight()?-1f:1f) * (isSpace?1:0)), player.getBackPosition().y);
    }

    private void blink() {
        Vector2 blinkedPosition = getBlinkPosition();
        if(blinkedPosition.x < 1.25f) blinkedPosition = new Vector2(1.25f, blinkedPosition.y);
        else if(blinkedPosition.x > 6.65f) blinkedPosition = new Vector2(6.65f, blinkedPosition.y);
        b2Body.setTransform(blinkedPosition, 0);
    }

    private void shoot() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                bossBlink.finish();
                bossShoot.active();
                TaleOfOverlord.manager.get("audio/sounds/boss2_beforeShoot.mp3", Sound.class).play();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        bossShoot.finish();
                        waitAction();
                                setIsBulletCreated(false);
                        TaleOfOverlord.manager.get("audio/sounds/boss2_shoot.mp3", Sound.class).play();
                    }
                },0.8f);
            }
        },1f);
    }

    private void transform() {
        bossTransform.active();
        TaleOfOverlord.manager.get("audio/sounds/boss2_transform.mp3", Sound.class).play();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                bossTransform.finish();
            }
        }, 5.5f);
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
                        TaleOfOverlord.manager.get("audio/sounds/boss2_blink.mp3", Sound.class).play();
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

    private void finalBlinkAndFinalMelee() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                blink();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        bossFinalBlink.finish();
                        bossFinalMelee.active();
                        TaleOfOverlord.manager.get("audio/sounds/boss2_melee.mp3", Sound.class).play();
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                bossFinalMelee.finish();
                                waitAction();
                                setIsSwordCreated(false);
                            }
                        },0.6f);
                    }
                }, 1f);
            }
        }, 1f);
    }

    @Override
    public boolean checkIsMelee() {
        return bossFinalMelee.isActive;
    }

    @Override
    public boolean checkIsShooting() {
        return bossShoot.isActive;
    }
}
