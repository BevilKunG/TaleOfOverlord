package com.taleofoverlord.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HealthBar extends ProgressBar {
    public HealthBar(int width, int height, Color color) {
        super(0f, 1f, 0.01f, false , new ProgressBarStyle());
        getStyle().background = getColoredDrawable(width, height, Color.BLACK);
        getStyle().knob = getColoredDrawable(0, height, color);
        getStyle().knobBefore = getColoredDrawable(width, height, color);

        setWidth(width);
        setHeight(height);

        setAnimateDuration(0.0f);
        setValue(1f);

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
}
