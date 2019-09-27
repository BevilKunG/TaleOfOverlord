package com.taleofoverlord.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.taleofoverlord.game.Screens.PlayScreen;

public class TaleOfOverlord extends Game {
	public SpriteBatch batch;

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
