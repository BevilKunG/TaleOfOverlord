package com.taleofoverlord.game.Sprites.Bosses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.Sprites.Player;
import com.taleofoverlord.game.Sprites.Weapons.Bullet;
import com.taleofoverlord.game.Sprites.Weapons.SlashedSword;
import com.taleofoverlord.game.Sprites.Weapons.Thorn;
import com.taleofoverlord.game.TaleOfOverlord;

public class BossHandler {
    private static BossHandler handler;
    private PlayScreen screen;
    private World world;
    private Player player;

    private static Boss boss;
    private static int bossType;
    private boolean isReborn;

    private BossHandler(PlayScreen screen) {
        this.screen = screen;
        world = screen.getWorld();
        player = screen.getPlayer();

        bossType = 1;
        isReborn = false;

        createBoss();
    }

    public static BossHandler getHandler(PlayScreen screen) {
        if(handler == null) {
            handler = new BossHandler(screen);
        }
        return handler;
    }

    public static Boss getBoss() {
        return boss;
    }

    private void createBoss() {
        if(boss != null && boss.checkIsDead()) destroyBoss();
        switch (bossType) {
            case 1: boss = new BossOne(screen); break;
            case 2: boss = new BossTwo(screen); break;
            default: boss = new BossOne(screen);
        }
    }

    private void destroyBoss() {
        world.destroyBody(boss.getB2Body());
        boss = null;
    }

    public void drawBoss(SpriteBatch batch) {
        boss.draw(batch);
    }

    public void update(float delta) {
        handleBossBullet();
        handleBossSword();
        handleHealth();
        boss.update(delta);
    }

    private void handleBossBullet() {
        if(boss.checkIsShooting() && !boss.checkIsBulletCreated()) {
            if(BossOne.class.isAssignableFrom(boss.getClass())) {
                screen.addBullet(new Bullet(screen, boss, player, 0));
                screen.addBullet(new Bullet(screen, boss, player, 30));
                screen.addBullet(new Bullet(screen, boss, player, 45));
                screen.addBullet(new Bullet(screen, boss, player, 60));

            } else if(BossTwo.class.isAssignableFrom(boss.getClass())) {
                screen.addThorn(new Thorn(screen, boss, player));
            }
            boss.setIsBulletCreated(true);
        }
    }

    private void handleBossSword() {
        if(boss.checkIsMelee() && !boss.checkIsSwordCreated()){
            screen.addSlashedSword(new SlashedSword(screen, boss, player));
            boss.setIsSwordCreated(true);
        }
    }

    private void handleHealth() {
        if(boss.checkIsDead() && !isReborn) {
            isReborn = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    bossType++;
                    createBoss();
                    boss.setIsDead(false);
                    isReborn = false;
                }
            },3.5f);
        }
        screen.getHud().bossHealthBar.setValue(1f-((float)boss.getHealthPoint() / TaleOfOverlord.BOSS_MAX_HP));
    }
}
