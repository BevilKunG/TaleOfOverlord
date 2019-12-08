package com.taleofoverlord.game.Sprites.Animations;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AtlasFactory {
    private static AtlasFactory factory;
    private TextureAtlas playerAtlas, bossOneAtlas, bossTwoAtlas;

    private AtlasFactory() {
        createAtlas();
    }

    public static AtlasFactory getFactory() {
        if(factory == null) {
            factory = new AtlasFactory();
        }
        return factory;
    }

    private void createAtlas() {
        playerAtlas = new TextureAtlas("player.pack");
        bossOneAtlas = new TextureAtlas("boss_one.pack");
        bossTwoAtlas = new TextureAtlas("boss_two.pack");
    }

    public TextureAtlas getPlayerAtlas() {
        return playerAtlas;
    }

    public TextureAtlas getBossOneAtlas() {
        return bossOneAtlas;
    }

    public TextureAtlas getBossTwoAtlas() {
        return bossTwoAtlas;
    }

}
