package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public abstract class Fighter extends Sprite {

    private int healthPoint;
    private boolean isRunningRight;

    public Fighter(TextureRegion region,boolean isRunningRight) {
        super(region);
        this.isRunningRight = isRunningRight;
    }


    public void setHealthPoint(int healthPoint) {
        this.healthPoint = healthPoint;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void decreaseHealthPoint(int damage) {
        healthPoint -= damage;
    }

    public void setRunningRight(boolean isRunningRight) {
        this.isRunningRight = isRunningRight;
    }

    public boolean checkIsRunningRight() {
        return isRunningRight;
    }

    public void ignoreBullet() {
        TaleOfOverlord.manager.get("audio/sounds/boss_barrier.mp3", Sound.class).play();
    }

    public abstract Vector2 getFrontPosition();
    public abstract Vector2 getBackPosition();
    public abstract void cancelAction();
    public abstract void recoil();
    public abstract void recoil(Vector2 recoilFactor);
}
