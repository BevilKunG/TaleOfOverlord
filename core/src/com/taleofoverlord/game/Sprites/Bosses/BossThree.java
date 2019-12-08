package com.taleofoverlord.game.Sprites.Bosses;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.Sprites.Animations.AnimationFactory;
import com.taleofoverlord.game.Sprites.Animations.AnimationPack;
import com.taleofoverlord.game.Sprites.Animations.StandFactory;
import com.taleofoverlord.game.TaleOfOverlord;

public class BossThree extends Boss {
    private AnimationPack bossBlink, bossShoot, bossTransform,
            bossFinalMelee, bossDead, bossFinalBlink;

    public BossThree(PlayScreen screen) {
        super(StandFactory.getFactory().getBossThreeStand(), false);

        super.init(screen);
        setIsIgnoreBullet(false);
        define();
        createAnimationPack();
    }

    private void createAnimationPack() {
        bossShoot = animationFactory.getBossThreeAnimationPack(AnimationFactory.AnimationType.BOSSSHOOT);
        bossTransform = animationFactory.getBossThreeAnimationPack(AnimationFactory.AnimationType.BOSSTRANSFORM);
        bossDead = animationFactory.getBossThreeAnimationPack(AnimationFactory.AnimationType.BOSSDEAD);
    }

    @Override
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
        bossStand = StandFactory.getFactory().getBossThreeStand();
        bossFinalStand = StandFactory.getFactory().getBossThreeFinalStand();
    }

    private State getFinalAttack() {
        if(!isTransform) return State.TRANSFORMING;
        return randomPercent()<50?State.SHOOTING : State.STANDING;
    }

    private State getPatternState() {
        State state = State.STANDING;
        if(getPercentHP() <= 0) return State.DEAD;
        if(getPercentHP() > 60) {
            setWaitingTime(1f);
            state = State.SHOOTING;

        }  else {
            state = getFinalAttack();
        }

        return state;
    }

    private void runPattern(int healthPoint) {
        switch (getPatternState()) {
            case SHOOTING: {
                if(isTransform) bossShoot.active();
                shoot();
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
        } else if(bossShoot.isActive) {
            return State.SHOOTING;
        } else {
            return State.STANDING;
        }
    }

    private TextureRegion getAnimationRegion(State currentState) {
        switch (currentState) {
            case SHOOTING: return (TextureRegion) bossShoot.animation.getKeyFrame(stateTimer, true);
            case TRANSFORMING: return (TextureRegion) bossTransform.animation.getKeyFrame(stateTimer, false);
            case DEAD: return (TextureRegion) bossDead.animation.getKeyFrame(stateTimer, false);
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

    private void shoot() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
//                bossBlink.finish();
                bossShoot.active();
//                TaleOfOverlord.manager.get("audio/sounds/boss2_beforeShoot.mp3", Sound.class).play();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        bossShoot.finish();
                        waitAction();
                        setIsBulletCreated(false);
//                        TaleOfOverlord.manager.get("audio/sounds/boss2_shoot.mp3", Sound.class).play();
                    }
                },0.8f);
            }
        },1f);
    }

    private void transform() {
        bossTransform.active();
//        TaleOfOverlord.manager.get("audio/sounds/boss2_transform.mp3", Sound.class).play();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                bossTransform.finish();
            }
        }, 5.5f);
    }

    private boolean checkIsAction() {
        return isWait ||
                bossShoot.isActive ||
                bossTransform.isActive ||
                bossDead.isActive;
    }

    @Override
    public boolean checkIsMelee() {
        return false;
    }

    @Override
    public boolean checkIsShooting() {
        return bossShoot.isActive;
    }

    @Override
    public void cancelAction() {

    }
}
