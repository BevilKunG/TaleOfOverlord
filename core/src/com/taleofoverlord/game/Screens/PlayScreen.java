package com.taleofoverlord.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taleofoverlord.game.Scenes.Hud;
import com.taleofoverlord.game.Sprites.*;
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

    private TextureAtlas playerAtlas;
    private TextureAtlas bossAtlas;

   private Hud hud;

    public PlayScreen(TaleOfOverlord game) {
        this.game = game;

        playerAtlas = new TextureAtlas("player.pack");
        bossAtlas = new TextureAtlas("boss.pack");

        // Game Cam and Viewport
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(TaleOfOverlord.V_WIDTH / TaleOfOverlord.PPM, TaleOfOverlord.V_HEIGHT / TaleOfOverlord.PPM, gameCam);

        // Map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level2.tmx");
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
        boss = new Boss(this);
        bullets = new Array<Bullet>();
        slashedSwords = new Array<SlashedSword>();
        punches = new Array<Punch>();

//        hud = new Hud(this);
    }

    @Override
    public void show() {

    }

    public void handleInput(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.b2Body.getLinearVelocity().y == 0)
            player.b2Body.applyLinearImpulse(new Vector2(0, 4f), player.b2Body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2Body.getLinearVelocity().x <= 2)
            player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2Body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2Body.getLinearVelocity().x >= -2)
            player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2Body.getWorldCenter(), true);

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.shoot();
            bullets.add(new Bullet(this, player, boss));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            player.slash();
            slashedSwords.add(new SlashedSword(this, player, boss));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            player.punch();
            punches.add(new Punch(this, player, boss));
        }

    }

    public void handleBullet() {
        for(Bullet bullet:bullets) {
            if(bullet.checkIsFinished()) {
                bullets.removeValue(bullet, true);
                world.destroyBody(bullet.b2Body);
                bullet = null;
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

    public void handleBossBullet() {
        if(boss.currentState == Boss.State.BLINK) {
            bullets.add(new Bullet(this, boss, player));
        }
    }

    public void update(float delta) {
        handleInput(delta);
        handleBullet();
        handleSlashedSword();
        handlePunch();
//        handleBossBullet();
        handleBoss();
        world.step(1/60f, 6, 2);
        player.update(delta);
        boss.update(delta);
//        hud.update();
        gameCam.position.x = player.b2Body.getPosition().x;
        gameCam.update();
        mapRenderer.setView(gameCam);

        Gdx.app.log("HP","" + player.getHealthPoint());
    }

    private void handleBoss() {
        if(boss.checkIsMelee() == true && boss.checkIsSwordCreated() == false){
            slashedSwords.add(new SlashedSword(this, boss, player));
            boss.setIsSwordCreated(true);
        }
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
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        boss.draw(game.batch);
//        hud.draw(game.batch);
        game.batch.end();



    }

    public TextureAtlas getPlayerAtlas() {
        return playerAtlas;
    }
    public TextureAtlas getBossAtlas() {
        return bossAtlas;
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() { return player; }

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
