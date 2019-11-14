package com.taleofoverlord.game.Scenes;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.Group;
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

    private Image playerHealthFrame;
    private Image bossHealthFrame;

    public HealthBar playerHealthBar, bossHealthBar;

    public Hud(SpriteBatch sb) {
        viewport = new FitViewport(TaleOfOverlord.V_WIDTH, TaleOfOverlord.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        playerHealthFrame = new Image(new Texture("player_hp_bar.png"));

        bossHealthFrame = new Image(new Texture("boss_hp_bar.png"));

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(playerHealthFrame).padRight(10);
        table.add(bossHealthFrame);
        stage.addActor(table);

        table = new Table();
        table.top();
        table.setFillParent(true);
        playerHealthBar = new HealthBar(450, 14, Color.GREEN);

        bossHealthBar = new HealthBar(450, 14, Color.YELLOW);

        table.add(playerHealthBar).padTop(16).padRight(60);
        table.add(bossHealthBar).padTop(16);

        stage.addActor(table);
    }

}
