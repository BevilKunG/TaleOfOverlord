package com.taleofoverlord.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Image playerHPBar;
    private Image bossHPBar;

    public Hud(SpriteBatch sb) {
        viewport = new FitViewport(TaleOfOverlord.V_WIDTH, TaleOfOverlord.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        playerHPBar = new Image(new Texture("player_hp_bar.png"));
        bossHPBar = new Image(new Texture("boss_hp_bar.png"));

//       Pixmap pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
//       pixmap.setColor(Color.RED);
//       pixmap.fill();
//
//       TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
//       pixmap.dispose();
//
//        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
//        style.background = drawable;
//
//        pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.GREEN);
//        pixmap.fill();
//        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
//        pixmap.dispose();
//
//        style.knob = drawable;
//
//        pixmap = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.GREEN);
//        pixmap.fill();
//        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
//        pixmap.dispose();
//
//        style.knobBefore = drawable;
//
//        bar = new ProgressBar(0f, 1f, .01f, false, style);
//        bar.setValue(1.0f);
//        bar.setAnimateDuration(0.25f);
//        bar.setBounds(10, 10, 100, 20);

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(playerHPBar);
        table.add(bossHPBar);
//        table.add(bar);
        stage.addActor(table);
    }

}
