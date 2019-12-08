package com.taleofoverlord.game.Sprites.Animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationFactory {
    private Array<TextureRegion> frames;
    private static AnimationFactory factory;
    private static AnimationPack bossOneMelee, bossOneShoot, bossOnePrepareBlink,
            bossOneBlink, bossOneThrow, bossOneTransform,
            bossOneFinalBlink, bossOneFinalUltimate, bossOneDead;

    private static AnimationPack bossTwoBlink, bossTwoShoot, bossTwoTransform, bossTwoFinalMelee,
            bossTwoDead, bossTwoFinalBlink;

    private static AnimationPack bossThreeTransform, bossThreeShoot, bossThreeDead;

    public enum AnimationType { BOSSMELEE, BOSSSHOOT, BOSSPREPAREBLINK,
        BOSSBLINK, BOSSTHROW, BOSSTRANSFORM,
        BOSSFINALBLINK, BOSSFINALULTIMATE, BOSSDEAD,
        BOSSFINALMELEE}
    
    private AtlasFactory atlasFactory;
    private TextureAtlas bossOneAtlas, bossTwoAtlas, bossThreeAtlas;


    private AnimationFactory() {
        defineAtlas();
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
        createBossFinalMelee();
    }

    public static AnimationFactory getFactory() {
        if(factory == null) {
            factory = new AnimationFactory();
        }
        return factory;
    }


    public AnimationPack getBossOneAnimationPack(AnimationType type) {
        switch (type) {
            case BOSSMELEE: return bossOneMelee;
            case BOSSSHOOT: return bossOneShoot;
            case BOSSPREPAREBLINK: return bossOnePrepareBlink;
            case BOSSBLINK: return bossOneBlink;
            case BOSSFINALULTIMATE: return  bossOneFinalUltimate;
            case BOSSTHROW: return bossOneThrow;
            case BOSSTRANSFORM: return bossOneTransform;
            case BOSSFINALBLINK: return bossOneFinalBlink;
            case BOSSDEAD: return bossOneDead;
        }
        return null;
    }

    public AnimationPack getBossTwoAnimationPack(AnimationType type) {
        switch (type) {
            case BOSSBLINK: return bossTwoBlink;
            case BOSSSHOOT: return bossTwoShoot;
            case BOSSTRANSFORM: return bossTwoTransform;
            case BOSSFINALMELEE: return bossTwoFinalMelee;
            case BOSSDEAD: return bossTwoDead;
            case BOSSFINALBLINK: return bossTwoFinalBlink;
        }
        return null;
    }

    public AnimationPack getBossThreeAnimationPack(AnimationType type) {
        switch (type) {
//            case BOSSBLINK: return bossTwoBlink;
            case BOSSSHOOT: return bossThreeShoot;
            case BOSSTRANSFORM: return bossThreeTransform;
//            case BOSSFINALMELEE: return bossTwoFinalMelee;
            case BOSSDEAD: return bossThreeDead;
//            case BOSSFINALBLINK: return bossTwoFinalBlink;
        }
        return null;
    }

    private void defineAtlas() {
         atlasFactory = AtlasFactory.getFactory();
         bossOneAtlas = atlasFactory.getBossOneAtlas();
         bossTwoAtlas = atlasFactory.getBossTwoAtlas();
         bossThreeAtlas = atlasFactory.getBossThreeAtlas();
    }
    
    private void createBossMelee() {
        // one
        for(int i=0; i<2; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_melee"), i * 128, 0, 128, 128));
        }
        bossOneMelee = new AnimationPack(new Animation(0.1f, frames));
        frames.clear();
    }

    private void createBossShoot() {
        // one
//        isBulletCreated = false;
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_shoot1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_shoot2"), i * 128, 0, 128, 128));
        }
        bossOneShoot = new AnimationPack(new Animation(0.1f, frames));
        frames.clear();

        // two
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_stand"), i * 128, 0, 128, 128));
        }
        bossTwoShoot = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();

        // three
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossThreeAtlas.findRegion("boss3_wing"), i * 128, 0, 128, 128));
        }
        bossThreeShoot = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();


    }

    private void createBossPrepareBlink() {
        // one
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_prepareBlink"), i * 128, 0, 128, 128));
        }
        bossOnePrepareBlink = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();
    }

    private void createBossBlink() {
        //one

        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_blink"), i * 128, 0, 128, 128));
        }
        bossOneBlink = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();

        // two
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_blink1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_blink2"), i * 128, 0, 128, 128));
        }
        bossTwoBlink = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();

    }

    private void createBossThrow() {
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_throwing"), i * 128, 0, 128, 128));
        }
        bossOneThrow = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();
    }

    private void createBossTransform() {
        // one

        for(int i=1; i<6; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_transform1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<6; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_transform2"), i * 128, 0, 128, 128));
        }
        bossOneTransform = new AnimationPack(new Animation(0.5f, frames));
        frames.clear();

        // two

        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_transform1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_transform2"), i * 128, 0, 128, 128));
        }
        bossTwoTransform = new AnimationPack(new Animation(0.5f, frames));
        frames.clear();

        // three
        for(int i=1; i<5; i++) {
            frames.add(new TextureRegion(bossThreeAtlas.findRegion("boss3_transform1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossThreeAtlas.findRegion("boss3_transform2"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossThreeAtlas.findRegion("boss3_transform3"), i * 128, 0, 128, 128));
        }
        bossThreeTransform = new AnimationPack(new Animation(0.5f, frames));
        frames.clear();

    }

    private void createBossFinalBlink() {
        // one
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_ulti1"), i * 128, 0, 128, 128));
        }
        bossOneFinalBlink = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();

        // two
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_prepareTeleport"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_teleport"), i * 128, 0, 128, 128));
        }
        bossTwoFinalBlink = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();
    }

    private void createBossFinalUltimate() {
        for(int i=0; i<6; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_ulti2"), i * 128, 0, 128, 128));
        }
        bossOneFinalUltimate = new AnimationPack(new Animation(0.1f, frames));
        frames.clear();
    }

    private void createBossDead() {
        //one
        for(int i=1; i<6; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_died1"), i * 128, 0, 128, 128));
        }
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(bossOneAtlas.findRegion("boss_died2"), i * 128, 0, 128, 128));
        }
        bossOneDead = new AnimationPack(new Animation(0.25f, frames));
        frames.clear();

        // two
        for(int i=0; i<6; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_died"), i * 128, 0, 128, 128));
        }
        bossTwoDead = new AnimationPack(new Animation(0.5f, frames));
        frames.clear();

        // three
        for(int i=0; i<5; i++) {
            frames.add(new TextureRegion(bossThreeAtlas.findRegion("boss3_died"), i * 128, 0, 128, 128));
        }
        bossThreeDead = new AnimationPack(new Animation(0.5f, frames));
        frames.clear();
    }

    private void createBossFinalMelee() {
        // two
        for(int i=0; i<4; i++) {
            frames.add(new TextureRegion(bossTwoAtlas.findRegion("boss2_melee"), i * 128, 0, 128, 128));
        }
        bossTwoFinalMelee = new AnimationPack(new Animation(0.1f, frames));
        frames.clear();
    }
}
