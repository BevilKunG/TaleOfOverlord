package com.taleofoverlord.game.Scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taleofoverlord.game.TaleOfOverlord;

public class Hud extends Sprite{
//    public Stage stage;
//    private Viewport viewport;
//
    private Sprite playerHPBar;

    public Hud() {
//        viewport = new FitViewport(TaleOfOverlord.V_WIDTH, TaleOfOverlord.V_HEIGHT, new OrthographicCamera());
//        stage = new Stage(viewport, sb);
//
//        playerHPBar = new Sprite(new Texture("player_hp_bar.png"));
//        stage.addActor();
        super(new Texture("player_hp_bar.png"));
    }
}
