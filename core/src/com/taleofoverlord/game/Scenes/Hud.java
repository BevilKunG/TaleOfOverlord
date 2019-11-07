package com.taleofoverlord.game.Scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.TaleOfOverlord;

public class Hud extends Sprite{
    private Sprite playerHPBar;
    private Vector2 playerPosition;
    public Hud(PlayScreen screen) {
        super(new Texture("boss_hp_bar.png"));
        playerPosition = screen.getPlayer().b2Body.getPosition();
        setPosition(playerPosition.x, playerPosition.y);
    }

    public void update() {
        setPosition(playerPosition.x, playerPosition.y);
    }


}
