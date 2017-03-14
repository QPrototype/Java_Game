package com.mygdx.game;

import character_movement.CharacterMovement;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game implements ApplicationListener, InputProcessor {

    private SpriteBatch batch;
    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;
    private CharacterMovement sprite;

    //map thing - needs to be celaned up!
    private TiledMap map;
    private IsometricTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private Viewport viewport;

    //Box2d variables
    //private World world;
    //private Box2DDebugRenderer box2DDebugRenderer;


    //map thing - needs to be celaned up!
    //------------

    private float destinationX = -1;
    private float destinationY = -1;

    @Override
    public void create() {
        batch = new SpriteBatch();
        walkingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/walking.atlas"));
        lookingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/looking.atlas"));
        //map thing - needs to be cleaned up!

        camera = new OrthographicCamera(1600, 800);
        //viewport = new FillViewport(1600, 800, camera);

        //viewport.apply();


        map = new TmxMapLoader().load("core/assets/map/water_collision.tmx");
        mapRenderer = new IsometricTiledMapRenderer(map);
        Gdx.input.setInputProcessor(this);

        MapLayers layers = map.getLayers();

        TiledMapTileLayer layer0 = (TiledMapTileLayer) map.getLayers().get(0);
        Vector3 center = new Vector3(layer0.getWidth() * layer0.getTileWidth()
                / 2, layer0.getHeight() * layer0.getTileHeight() / 2, 0);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(center);

        //------------

        AtlasRegion region = walkingAtlas.findRegion("walking e0000");

        sprite = new CharacterMovement(walkingAtlas, lookingAtlas, region,(TiledMapTileLayer)layers.get(0));

        //sprite.setPosition(250, 100);
        sprite.scale(0.1f);


        Timer.schedule(new Task() {
                           @Override
                           public void run() {
                               sprite.walk(destinationX, destinationY);
                           }
                       }
                , 0, 1 / 30.0f);
    }

    @Override
    public void dispose() {
        batch.dispose();
        walkingAtlas.dispose();
    }

    @Override
    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            //projection to world coords
            Vector3 clickOnScreen = new Vector3();
            clickOnScreen.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(clickOnScreen);
            destinationX = clickOnScreen.x - sprite.getWidth() / 2;
            destinationY = clickOnScreen.y - 10;
            //System.out.println(clickOnScreen.x + " : " + clickOnScreen.y);
            //y = mapHeight - tilesize - y

            //-----------
        }
        //map stufff
        handleInput();
        mapRenderer.setView(camera);
        mapRenderer.render();

        //---------------
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    //map stuff - needs to be cleaned
    private void handleInput() {


        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-10, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(10, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -10, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 10, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.rotate(-0.5f, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(0.5f, 0, 0, 1);
        }
    }
    //----------------

    /*@Override
    public void resize(int width, int height) {
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 7, 0);
        //viewport.update(width, height);

    }*/

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
       /* Gdx.app.log("Mouse Event","Click at " + screenX + "," + screenY);
        Vector3 worldCoordinates = camera.unproject(new Vector3(screenX,screenY,0));
        Gdx.app.log("Mouse Event","Projected at " + worldCoordinates.x + "," + worldCoordinates.y);*/
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}