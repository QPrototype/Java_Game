package com.mygdx.game;

import character_movement.CharacterMovement;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame implements ApplicationListener, InputProcessor {

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
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;


    //map thing - needs to be celaned up!
    //------------

    private float destinationX, destinationY;
    Vector3 worldCoordinates = new Vector3(destinationX, destinationY, 0);

    @Override
    public void create() {
        batch = new SpriteBatch();

        walkingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/walking.atlas"));
        lookingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/looking.atlas"));
        //map thing - needs to be cleaned up!

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewport = new FillViewport(1280, 1080, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight/ 2, 0);
        camera.update();

        map = new TmxMapLoader().load("core/assets/map/testingmap.tmx");
        mapRenderer = new IsometricTiledMapRenderer(map);
        Gdx.input.setInputProcessor(this);
        //box2d world
        world = new World(new Vector2(0, 0), false);
        box2DDebugRenderer = new Box2DDebugRenderer();
//
//      local variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
//
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(PolygonMapObject.class)){
            Polygon polygon = ((PolygonMapObject) object).getPolygon();

            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set(0, 0);

            body = world.createBody(bdef);

            shape.setAsBox(polygon.area() / 2, polygon.area() / 2);
            fdef.shape = shape;
            body.createFixture(fdef);


        }



        //------------

        AtlasRegion region = walkingAtlas.findRegion("walking e0000");

        sprite = new CharacterMovement(walkingAtlas, lookingAtlas, region);

        sprite.setPosition(120, 100);
        sprite.scale(0.1f);
        Timer.schedule(new Task() {
                           @Override
                           public void run() {
                               sprite.walk(worldCoordinates.x, worldCoordinates.y);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            worldCoordinates.x = Gdx.input.getX() - sprite.getWidth() / 2;
            worldCoordinates.y = Gdx.graphics.getHeight() - Gdx.input.getY() - sprite.getHeight() / 2;
        }

        handleInput();

        box2DDebugRenderer.render(world, camera.combined);
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);

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

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

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
        Gdx.app.log("Mouse Event","Click at " + screenX + "," + screenY);
        Vector3 worldCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
        Gdx.app.log("Mouse Event","Projected at " + worldCoordinates.x + "," + worldCoordinates.y);
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