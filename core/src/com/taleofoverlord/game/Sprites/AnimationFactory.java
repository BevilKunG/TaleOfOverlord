package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.taleofoverlord.game.Screens.PlayScreen;

public class AnimationFactory {
    private PlayScreen screen;
    private Array<TextureRegion> frames;
    private static AnimationFactory factory;
    private static AnimationPack bossMelee, bossShoot, bossPrepareBlink,
            bossBlink, bossThrow, bossTransform,
            bossFinalBlink, bossFinalUltimate, bossDead;
    public enum AnimationType { BOSSMELEE, BOSSSHOOT, BOSSPREPAREBLINK,
        BOSSBLINK, BOSSTHROW, BOSSTRANSFORM,
        BOSSFINALBLINK, BOSSFINALULTIMATE, BOSSDEAD }

    private AnimationFactory(PlayScreen screen) {
        this.screen = screen;
        frames = new Array<TextureRegion>();
        createBossMelee();
        createBossShoot();
        createBossPrepareBlink();
        createBossBlink();
        createBossThrow();
        createBossTransform();
        createBossFinalBlink();
        createBossFinalUltimate();
        createBossDead();
    }

    public static AnimationFactory getFactory(PlayScreen screen) {
        if(factory == null) {
            factory = new AnimationFactory(screen);
        }
        return factory;
    }

    public AnimationPack getAnimationPack(AnimationType type) {
        switch (type) {
            case BOSSMELEE: return bossMelee;
            case BOSSSHOOT: return bossShoot;
            case BOSSPREPAREBLINK: return bossPrepareBlink;
            case BOSSBLINK: return bossBlink;
            case BOSSFINALULTIMATE: return  bossFinalUltimate;
            case BOSSTHROW: return bossThrow;
            case BOSSTRANSFORM: return bossTransform;
            case BOSSFINALBLINK: return bossFinalBlink;
            case BOSSDEAD: return bossDead;
        }
        return null;
    }

    private void createBossMelee() {
        for(int i=0; i<2; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_melee"), i * 128, 0, 128, 128));
        }
        bossMelee = new AnimationPack(new Animation(0.1f, frames));
        frames.clear();
    }

    private void createBossShoot() {
//        isBulletCreated = false;
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_shoot1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_shoot2"), i * 128, 0, 128, 128));
        }
        bossShoot = new AnimationPack(new Animation(0.1f, frames));
        frames.clear();
    }

    private void createBossPrepareBlink() {
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_prepareBlink"), i * 128, 0, 128, 128));
        }
        bossPrepareBlink = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();
    }

    private void createBossBlink() {
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_blink"), i * 128, 0, 128, 128));
        }
        bossBlink = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();
    }

    private void createBossThrow() {
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_throwing"), i * 128, 0, 128, 128));
        }
        bossThrow = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();
    }

    private void createBossTransform() {
        for(int i=1; i<6; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_transform1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<6; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_transform2"), i * 128, 0, 128, 128));
        }
        bossTransform = new AnimationPack(new Animation(0.5f, frames));
        frames.clear();
    }

    private void createBossFinalBlink() {
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_ulti1"), i * 128, 0, 128, 128));
        }
        bossFinalBlink = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();
    }

    private void createBossFinalUltimate() {
        for(int i=0; i<6; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_ulti2"), i * 128, 0, 128, 128));
        }
        bossFinalUltimate = new AnimationPack(new Animation(0.1f, frames));
        frames.clear();
    }

    private void createBossDead() {
        for(int i=1; i<6; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_died1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(screen.getBossAtlas().findRegion("boss_died2"), i * 128, 0, 128, 128));
        }
        bossDead = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();
    }
}
