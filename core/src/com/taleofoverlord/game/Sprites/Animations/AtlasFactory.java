package com.taleofoverlord.game.Sprites.Animations;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AtlasFactory {
    private static AtlasFactory factory;
    private TextureAtlas playerAtlas, bossOneAtlas, bossTwoAtlas, bossThreeAtlas;

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
        bossThreeAtlas = new TextureAtlas("boss_three.pack");
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

    public TextureAtlas getBossThreeAtlas() {
        return bossThreeAtlas;
    }

}
