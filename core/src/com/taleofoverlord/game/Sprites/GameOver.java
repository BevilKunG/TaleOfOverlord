package com.taleofoverlord.game.Sprites;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.taleofoverlord.game.Screens.PlayScreen;

public class GameOver extends Sprite {
    private PlayScreen screen;

    public GameOver(PlayScreen screen){
        super(new Texture("game_over.png"));
        this.screen = screen;
        setSize(4,2);
        setPosition(screen.getGameCam().position.x - getWidth() / 2,screen.getGameCam().position.y - getHeight() / 2);
    }

    public void update(){
        setPosition(screen.getGameCam().position.x - getWidth() / 2,screen.getGameCam().position.y - getHeight() / 2);
    }
}
