package com.taleofoverlord.game.Sprites.Animations;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StandFactory {
    private static StandFactory factory;
    private TextureRegion playerStand,
            bossOneStand, bossOneFinalStand,
            bossTwoStand, bossTwoFinalStand,
            bossThreeStand, bossThreeFinalStand;
    private AtlasFactory atlasFactory;
    private TextureAtlas playerAtlas, bossOneAtlas, bossTwoAtlas, bossThreeAtlas;

    private StandFactory() {
        defineAtlas();
        createStand();
    }

    public static StandFactory getFactory() {
        if(factory == null) {
            factory = new StandFactory();
        }
        return factory;
    }

    private void defineAtlas() {
        atlasFactory = AtlasFactory.getFactory();
        playerAtlas = atlasFactory.getPlayerAtlas();
        bossOneAtlas = atlasFactory.getBossOneAtlas();
        bossTwoAtlas = atlasFactory.getBossTwoAtlas();
        bossThreeAtlas = atlasFactory.getBossThreeAtlas();
    }

    private void createStand() {
        playerStand = playerAtlas.findRegion("player_stand");
        bossOneStand = bossOneAtlas.findRegion("boss_stand");
        bossOneFinalStand = new TextureRegion(bossOneAtlas.findRegion("boss_transform2"), 128 * 5, 0, 128, 128);
        bossTwoStand = new TextureRegion(bossTwoAtlas.findRegion("boss2_stand"), 0, 0, 128, 128);
        bossTwoFinalStand = new TextureRegion(bossTwoAtlas.findRegion("boss2_transform2"), 128 * 3, 0, 128, 128);
        bossThreeStand = new TextureRegion(bossThreeAtlas.findRegion("boss3_transform1"), 0, 0, 128, 128);
        bossThreeFinalStand = new TextureRegion(bossThreeAtlas.findRegion("boss3_transform3"), 128 * 3, 0, 128, 128);
    }

    public TextureRegion getPlayerStand() {
        return playerStand;
    }

    public TextureRegion getBossOneStand() {
        return bossOneStand;
    }

    public TextureRegion getBossOneFinalStand() {
        return bossOneFinalStand;
    }

    public TextureRegion getBossTwoStand() {
        return bossTwoStand;
    }

    public TextureRegion getBossTwoFinalStand() {
        return bossTwoFinalStand;
    }

    public TextureRegion getBossThreeStand() {
        return bossThreeStand;
    }

    public TextureRegion getBossThreeFinalStand() {
        return bossThreeFinalStand;
    }

}
