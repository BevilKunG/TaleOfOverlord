package com.taleofoverlord.game.Sprites.Bosses;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

public class BossFactory {
    Array<Boss> bosses;

    public BossFactory() {
        createBoss();
    }

    private void createBoss() {
        bosses = new Array<Boss>();
        bosses.add((Boss)(new BossOne(null)));
        bosses.add((Boss)(new BossTwo(null)));
    }
}
