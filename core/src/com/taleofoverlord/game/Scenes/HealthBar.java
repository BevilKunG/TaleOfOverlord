package com.taleofoverlord.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.taleofoverlord.game.Sprites.Fighter;
import com.taleofoverlord.game.TaleOfOverlord;

public class HealthBar extends ProgressBar {
    private int width;
    private int height;
    public HealthBar(int width, int height, TaleOfOverlord.HealthBarStyle style) {
        super(0f, 1f, 0.01f, false , new ProgressBarStyle());

        this.width = width;
        this.height = height;

        setWidth(width);
        setHeight(height);

        switch (style) {
            case BOSS_STYLE: bossStyle();
                break;
            case PLAYER_STYLE:
            default:playerStyle();
                break;
        }
        setAnimateDuration(0.25f);
    }

    private TextureRegionDrawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion( new Texture(pixmap)));
        pixmap.dispose();
        return drawable;
    }

    private void playerStyle() {
        getStyle().background = getColoredDrawable(width, height, Color.BLACK);
        getStyle().knob = getColoredDrawable(0, height, Color.GREEN);
        getStyle().knobBefore = getColoredDrawable(width, height, Color.GREEN);

        setValue(1f);
    }

    private void bossStyle() {
        getStyle().background = getColoredDrawable(width, height, Color.YELLOW);
        getStyle().knob = getColoredDrawable(0, height, Color.BLACK);
        getStyle().knobBefore = getColoredDrawable(width, height, Color.BLACK);

        setValue(0f);
    }
}
