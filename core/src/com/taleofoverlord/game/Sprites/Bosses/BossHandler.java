package com.taleofoverlord.game.Sprites.Bosses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
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
    private static Music music;

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
//            case 3: boss = new BossThree(screen); break;
            default: {
                boss = null;
                screen.gameWin();
            }
        }
        playMusic(bossType);
    }

    private void destroyBoss() {
        world.destroyBody(boss.getB2Body());
        boss = null;
    }

    public void drawBoss(SpriteBatch batch) {
        if(boss == null) return;
        boss.draw(batch);
    }

    public void update(float delta) {
        if(boss == null || screen.checkIsGameOver()) {
            music.stop();
            return;
        }
        handleBossBullet();
        handleBossSword();
        handleHealth();
        boss.update(delta);
    }

    private void playMusic(int type) {
        if(music != null) music.stop();
        switch (bossType) {
            case 1: music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/bg_sound.mp3")); break;
            case 2: music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/bg_sound2.mp3")); break;
//            case 3: music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/bg_sound.mp3")); break;
            default: {
                boss = null;
                if(music != null) music.stop();
                screen.gameWin();
            }
        }
        if(boss != null)music.play();
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
            } else if(BossThree.class.isAssignableFrom(boss.getClass())) {
                Bullet bullet = new Bullet(screen, boss, player);
                bullet.setTexture(new Texture("bullet2.png"));
                screen.addBullet(bullet);
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

    public void reset() {
        bossType = 1;
        isReborn = false;
        if(boss != null) world.destroyBody(boss.getB2Body());
        createBoss();
    }
}
