package com.taleofoverlord.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.taleofoverlord.game.Screens.PlayScreen;

public class TaleOfOverlord extends Game {
	public SpriteBatch batch;

	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 200;
	public static final float PPM = 100; // Scaling Pixel
	public static final int PLAYER_MAX_HP = 100;
	public static final int BOSS_MAX_HP = 1000;
	public static final float FLIP_EPSILON = 0.01f;
	public static final int BULLET_DAMAGE = 10;

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
