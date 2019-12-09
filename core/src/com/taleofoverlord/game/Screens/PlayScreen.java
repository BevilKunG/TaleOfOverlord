package com.taleofoverlord.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taleofoverlord.game.Scenes.Hud;
import com.taleofoverlord.game.Sprites.*;
import com.taleofoverlord.game.Sprites.Bosses.Boss;
import com.taleofoverlord.game.Sprites.Bosses.BossHandler;
import com.taleofoverlord.game.Sprites.Bosses.BossOne;
import com.taleofoverlord.game.Sprites.Bosses.BossTwo;
import com.taleofoverlord.game.Sprites.Weapons.Bullet;
import com.taleofoverlord.game.Sprites.Weapons.Punch;
import com.taleofoverlord.game.Sprites.Weapons.SlashedSword;
import com.taleofoverlord.game.Sprites.Weapons.Thorn;
import com.taleofoverlord.game.TaleOfOverlord;
import com.taleofoverlord.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    private TaleOfOverlord game;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Player player;
    private Boss boss;
    private Array<Bullet> bullets;
    private Array<SlashedSword> slashedSwords;
    private Array<Punch> punches;
    private Array<Thorn> thorns;

    private TextureAtlas playerAtlas;

    private BossHandler bossHandler;

    public boolean isGameOver;
    public Music music;

   private Hud hud;

    public PlayScreen(TaleOfOverlord game) {
        this.game = game;
        isGameOver = false;

        playerAtlas = new TextureAtlas("player.pack");

        // Game Cam and Viewport
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(TaleOfOverlord.V_WIDTH / TaleOfOverlord.PPM, TaleOfOverlord.V_HEIGHT / TaleOfOverlord.PPM, gameCam);

        // Map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level4.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / TaleOfOverlord.PPM);

        // Game Cam Position
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Box2D
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        world.setContactListener(new WorldContactListener());

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // ground
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / TaleOfOverlord.PPM, (rect.getY() + rect.getHeight() / 2) / TaleOfOverlord.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth() / 2 / TaleOfOverlord.PPM, rect.getHeight() / 2 / TaleOfOverlord.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("ground");
        }

        // Player and Boss
        player = new Player( this);
        bossHandler = BossHandler.getHandler(this);

        bullets = new Array<Bullet>();
        slashedSwords = new Array<SlashedSword>();
        punches = new Array<Punch>();
        thorns = new Array<Thorn>();

        // Hud
        hud = new Hud(game.batch);
    }

    public void reset() {
        destroy();
        if(music != null) music.stop();
        isGameOver = false;


        // Player and Boss
        player = new Player( this);
        bossHandler.reset();

        bullets = new Array<Bullet>();
        slashedSwords = new Array<SlashedSword>();
        punches = new Array<Punch>();
        thorns = new Array<Thorn>();
    }

    public void destroy() {
        world.destroyBody(player.b2Body);
        for(Bullet bullet: bullets) if(bullet != null) world.destroyBody(bullet.b2Body);
        for(Thorn thorn: thorns) if(thorn != null) world.destroyBody(thorn.b2Body);
        for(SlashedSword slashedSword: slashedSwords) if(slashedSword != null) world.destroyBody(slashedSword.b2Body);
        for(Punch punch: punches) if(punch != null) world.destroyBody(punch.b2Body);
    }

    @Override
    public void show() {

    }

    public void handleInput(float delta) {
//        if(!isGameOver) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                reset();
            }
//        }
        if(!isGameOver && !player.checkIsHurt()) {
            boolean canMove = !player.checkisPunching() && !player.checkisSlashing() && !player.checkisShooting();
            if(canMove) {
                if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.b2Body.getLinearVelocity().y == 0) {
                    player.b2Body.applyLinearImpulse(new Vector2(0, 4f), player.b2Body.getWorldCenter(), true);
                }

                if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2Body.getLinearVelocity().x <= 2) {
                    if(!player.checkIsRunningRight()) player.stopMoving();
                    player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2Body.getWorldCenter(), true);
                }

                if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2Body.getLinearVelocity().x >= -2) {
                    if(player.checkIsRunningRight()) player.stopMoving();
                    player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2Body.getWorldCenter(), true);
                }

            }

                if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !player.checkIsBulletCreated()) {
                    player.stopMoving();
                    player.shoot();
                    bullets.add(new Bullet(this, player, bossHandler.getBoss()));
                    player.setIsBulletCreated(true);
                }

                if(Gdx.input.isKeyJustPressed(Input.Keys.S) && !player.checkIsSwordCreated()) {
                    player.stopMoving();
                    player.slash();
                    slashedSwords.add(new SlashedSword(this, player, bossHandler.getBoss()));
                    player.setIsSwordCreated(true);
                }

                if(Gdx.input.isKeyJustPressed(Input.Keys.P) && !player.checkIsPunchCreated()) {
                    player.stopMoving();
                    player.punch();
                    punches.add(new Punch(this, player, bossHandler.getBoss()));
                    player.setIsPunchCreated(true);
                }
            }

    }

    public void handleBullet() {
        for(Bullet bullet:bullets) {
            bullet.update();
            if(bullet.checkIsFinished()) {
                bullets.removeValue(bullet, true);
                world.destroyBody(bullet.b2Body);
            }
        }
    }

    public void handleThorn() {
        for(Bullet thorn:thorns) {
            thorn.update();
            if(thorn.checkIsFinished()) {
                thorns.removeValue((Thorn) thorn, true);
                world.destroyBody(thorn.b2Body);
            }
        }
    }


    public void handlePunch() {
        for(Punch punch:punches) {
            if(punch.checkIsFinished()) {
                punches.removeValue(punch, true);
                world.destroyBody(punch.b2Body);
                punch = null;
            }
        }
    }

    public void handleSlashedSword() {
        for(SlashedSword slashedSword:slashedSwords) {
            if(slashedSword.checkIsFinished()) {
                slashedSwords.removeValue(slashedSword, true);
                world.destroyBody(slashedSword.b2Body);
                slashedSword = null;
            }
        }
    }

    public void handleHealth() {
        hud.playerHealthBar.setValue(((float)player.getHealthPoint() / TaleOfOverlord.PLAYER_MAX_HP));
        if(!isGameOver && player.getHealthPoint()<= 0) {
            isGameOver = true;
            player.dead();
            gameLose();
        }
    }

    public void update(float delta) {
        handleHealth();
        handleInput(delta);
        handleBullet();
        handleThorn();
        handleSlashedSword();
        handlePunch();

        world.step(1/60f, 6, 2);
        player.update(delta);
        bossHandler.update(delta);

        gameCam.position.x = player.b2Body.getPosition().x;
        gameCam.update();
        mapRenderer.setView(gameCam);

    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void addThorn(Thorn thorn) {
        thorns.add(thorn);
    }

    public void addSlashedSword(SlashedSword slashedSword) {
        slashedSwords.add(slashedSword);
    }

    @Override
    public void render(float delta) {
        update(delta);

        // Clear Screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Render Map
        mapRenderer.render();

        // render Box2d Debug
//        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        bossHandler.drawBoss(game.batch);


        for(Bullet bullet:bullets){
            if(!bullet.checkIsFinished())
                bullet.draw(game.batch);
        }

        for(Bullet thorn:thorns){
            if(!thorn.checkIsFinished())
                thorn.draw(game.batch);
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        hud.stage.act();


    }

    public void gameWin() {
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/victory_sound.mp3"));
        music.play();
    }

    public void gameLose() {
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/music/lose_sound.mp3"));
        music.play();
    }

    public TextureAtlas getPlayerAtlas() {
        return playerAtlas;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() { return player; }

    public Boss getBoss() {
        return boss;
    }

    public Hud getHud() {
        return hud;
    }

    public boolean checkIsGameOver() {
        return isGameOver;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
