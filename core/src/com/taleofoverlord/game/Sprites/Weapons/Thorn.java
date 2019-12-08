package com.taleofoverlord.game.Sprites.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.taleofoverlord.game.Screens.PlayScreen;
import com.taleofoverlord.game.Sprites.Animations.AtlasFactory;
import com.taleofoverlord.game.Sprites.Bosses.Boss;
import com.taleofoverlord.game.Sprites.Fighter;
import com.taleofoverlord.game.Sprites.Player;

public class Thorn extends Bullet {
    private TextureRegion thornRegion;
    private boolean isRight, isShooted, isStop;
    private Player player;
    private Boss boss;
    public Thorn(PlayScreen screen, Fighter shooter, Fighter target) {
        super(screen, shooter, target);

        thornRegion = AtlasFactory.getFactory().getBossTwoAtlas().findRegion("boss2_bullet");
        setRegion(thornRegion);

        isRight = true;
        isShooted = false;
        isStop = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isShooted = true;
            }
        }, 2f );
        player = screen.getPlayer();
        boss = screen.getBoss();
        setStartPosition(new Vector2(player.getFrontPosition().x + 0.25f, player.getFrontPosition().y + 0.5f));
    }

    @Override
    public void update() {
        if(isStop && !isShooted) handleFlipingRegion(thornRegion);
        else move();
        setPosition((b2Body.getPosition().x - getWidth() / 2) + getOffset().x , (b2Body.getPosition().y - getHeight() / 2) + getOffset().y);
        setRegion(thornRegion);
    }

    @Override
    protected void move() {
        if(isShooted) {
            b2Body.setLinearVelocity(new Vector2(2.0f * (b2Body.getPosition().x < player.b2Body.getPosition().x? 1 : -1), 2.0f * (b2Body.getPosition().y < player.b2Body.getPosition().y? 1 : -1)));
            isShooted = false;
            isStop = false;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    setIsFinished(true);
                }
            }, 0.8f);
        }
    }

    private void setStartPosition(Vector2 position) {
        b2Body.setTransform(position, 0);
    }

    public boolean checkInFrontOfPlayer() {
        return checkIsRight()? player.b2Body.getPosition().x > b2Body.getPosition().x : player.b2Body.getPosition().x < b2Body.getPosition().x;
    }

    protected void handleFlipingRegion(TextureRegion region) {
        if(!checkInFrontOfPlayer()) {
            region.flip(true, false);
            setRight(!checkIsRight());
        }
        if(checkIsRight() != region.isFlipX()) {
            region.flip(true, false);
        }
    }

    private boolean checkIsRight() {
        return isRight;
    }

    private void setRight(boolean isRight) {
        this.isRight = isRight;
    }
}
