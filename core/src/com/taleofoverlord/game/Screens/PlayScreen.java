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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taleofoverlord.game.Sprites.Boss;
import com.taleofoverlord.game.Sprites.Player;
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

    private TextureAtlas atlas;

    public PlayScreen(TaleOfOverlord game) {
        this.game = game;

        atlas = new TextureAtlas("player_action.pack");

        // Game Cam and Viewport
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(TaleOfOverlord.V_WIDTH / TaleOfOverlord.PPM, TaleOfOverlord.V_HEIGHT / TaleOfOverlord.PPM, gameCam);

        // Map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
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
            body.createFixture(fdef);
        }

        // Player and Boss
        player = new Player( this);
        boss = new Boss(this);

    }

    @Override
    public void show() {

    }

    public void handleInput(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2Body.applyLinearImpulse(new Vector2(0, 4f), player.b2Body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2Body.getLinearVelocity().x <= 2)
            player.b2Body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2Body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2Body.getLinearVelocity().x >= -2)
            player.b2Body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2Body.getWorldCenter(), true);
    }

    public void update(float delta) {
        handleInput(delta);
        world.step(1/60f, 6, 2);
        player.update(delta);
        gameCam.position.x = player.b2Body.getPosition().x;
        gameCam.update();
        mapRenderer.setView(gameCam);
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
        game.batch.end();

        game.batch.setProjectionMatrix(gameCam.combined);




    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public World getWorld() {
        return world;
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
