package com.taleofoverlord.game.Sprites.Bosses;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.Sprites.Animations.AnimationFactory;
import com.taleofoverlord.game.Sprites.Animations.StandFactory;
import com.taleofoverlord.game.Sprites.Fighter;
import com.taleofoverlord.game.Sprites.Player;
import com.taleofoverlord.game.TaleOfOverlord;

public abstract class Boss extends Fighter {
    protected Body b2Body;

    protected World world;
    protected Player player;

    protected Vector2 currentPosition;

    public enum State { STANDING, SHOOTING, BLINK,
        TRANSFORMING, FINALBLINK, DEAD,
        FINALMELEE, MELEE, PREPAREBLINK,
        FINALULTIMATE, THROWING};
    public State currentState, previousState;
    public float stateTimer;

    protected boolean isWait, isTransform;
    protected boolean isSwordCreated, isBulletCreated;
    protected boolean isIgnoreBullet;
    protected float waitingTime;

    protected AnimationFactory animationFactory;
    protected TextureRegion bossStand, bossFinalStand;

    public Boss(TextureRegion region, boolean isRunningRight) {
        super(region, isRunningRight);
    }

    protected void init(PlayScreen screen) {
        world = screen.getWorld();
        player = screen.getPlayer();

        currentPosition = new Vector2(512 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM);

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;

        animationFactory = AnimationFactory.getFactory();

        createBossStand();
        setRegion(bossStand);
        setBounds(0, 0, 64 / TaleOfOverlord.PPM, 64 / TaleOfOverlord.PPM);

        isWait = false;
        isTransform = false;
        waitingTime = 3f;

        isSwordCreated = false;
        isBulletCreated = false;
        isIgnoreBullet = false;
    }

    protected abstract void define();
    public Body getB2Body() {
        return b2Body;
    };

    protected abstract void createBossStand();
    public abstract void update(float delta);

    protected void handleFlipingRegion(TextureRegion region) {
        if(!checkInFrontOfPlayer()) {
            region.flip(true, false);
            super.setRunningRight(!checkIsRunningRight());
        }
        if(this.checkIsRunningRight() != region.isFlipX()) {
            region.flip(true, false);
        }
    }

    protected int randomPercent() {
        return (int)(Math.random()*100);
    }

    public boolean checkInFrontOfPlayer() {
        return super.checkIsRunningRight()? player.b2Body.getPosition().x > b2Body.getPosition().x : player.b2Body.getPosition().x < b2Body.getPosition().x;
    }

    @Override
    public Vector2 getFrontPosition() {
        return new Vector2(b2Body.getPosition().x + (0.24f * (super.checkIsRunningRight()? 1 : -1)), b2Body.getPosition().y);
    }

    @Override
    public Vector2 getBackPosition() {
        return new Vector2(b2Body.getPosition().x + (0.24f * (super.checkIsRunningRight()? -1 : 1)), b2Body.getPosition().y);
    }

    public float getPercentHP() {
        return ((float)getHealthPoint()/TaleOfOverlord.BOSS_MAX_HP)*100;
    }

    protected float getOffset() {
        return 0.10f * (super.checkIsRunningRight() ? 1 : -1);
    }

    protected void setWaitingTime(float waitingTime) {
        this.waitingTime = waitingTime;
    }

    protected void waitAction() {
        // speed up
        isWait = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isWait = false;
            }
        }, waitingTime);
    }

    public void stopMoving() {
        b2Body.setLinearVelocity(new Vector2(0, 0));
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

    @Override
    public void recoil(Vector2 recoilFactor) {
        b2Body.applyLinearImpulse(new Vector2(1f * recoilFactor.x * (checkIsRunningRight()? -1:1), 1f * recoilFactor.y), b2Body.getWorldCenter(), true);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                stopMoving();
            }
        }, 0.2f);
    }

//    public abstract  void ignoreDamage();

    public abstract boolean checkIsMelee();
    public abstract boolean checkIsShooting();

    public boolean checkIsSwordCreated(){
        return isSwordCreated;
    }
    public void setIsSwordCreated(boolean isSwordCreated){
        this.isSwordCreated = isSwordCreated;
    }

    public boolean checkIsBulletCreated(){
        return  isBulletCreated;
    }
    public void setIsBulletCreated(boolean isBulletCreated){
        this.isBulletCreated = isBulletCreated;
    }

    public boolean checkIsIgnoreBullet() {
        return isIgnoreBullet;
    }
    protected void setIsIgnoreBullet(boolean isIgnoreBullet) {
        this.isIgnoreBullet = isIgnoreBullet;
    }
}
