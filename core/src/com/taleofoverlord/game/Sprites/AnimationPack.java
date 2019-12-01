package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationPack {
    public boolean isActive;
    public Animation animation;
    public AnimationPack(Animation animation) {
        isActive = false;
        this.animation = animation;
    }

    public void active() {
        isActive = true;
    }

    public void finish() {
        isActive = false;
    }
}
