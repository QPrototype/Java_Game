package Screens;

import Scenes.GameHud;
import character_movement.CharacterMovement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.MainGame.MainGame;

public class MainScreen implements Screen {

    //Comment
    private MainGame game;
    private TextureAtlas atlas;

    // screen variables
    public static final int WORLD_WIDTH = 1600;
    public static final int WORLD_HEIGHT = 800;
    private Viewport viewport;
    private OrthographicCamera camera;

    //map variables.
    private TiledMap map;
    private IsometricTiledMapRenderer mapRenderer;

    // minimap stuff
    private OrthographicCamera minimapCamera;
    private IsometricTiledMapRenderer minimapRenderer;
    private SpriteBatch minimapBatch;


    //Sprites
    private SpriteBatch hudBatch;
    private SpriteBatch batch;
    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;
    private TextureAtlas cuttingAtlas;
    private CharacterMovement sprite;

    //movement
    private float destinationX = -1;
    private float destinationY = -1;


    public GameHud gameHud;

    public MainScreen(MainGame game) {
        this.game = game;

        //SpriteBatch.
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        minimapBatch = new SpriteBatch();

        walkingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/walking.atlas"));
        lookingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/looking.atlas"));
        cuttingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/cutting.atlas"));

        gameHud = new GameHud(hudBatch);

        //Camera
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //minimap
        minimapCamera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        minimapCamera.zoom = 14;
        minimapCamera.position.set(-WORLD_WIDTH * 5, 4850, 0);

        //Map
        map = new TmxMapLoader().load("core/assets/map/Map_example1.tmx");

        mapRenderer = new IsometricTiledMapRenderer(map);
        minimapRenderer = new IsometricTiledMapRenderer(map);

        TiledMapTileLayer layer0 = (TiledMapTileLayer) map.getLayers().get(0);

        Vector3 center = new Vector3(layer0.getWidth() * layer0.getTileWidth() / 5,
                layer0.getHeight() * layer0.getTileHeight() / 7, 0);
        MapLayers layers = map.getLayers();
        camera.position.set(center);

        TextureAtlas.AtlasRegion region = walkingAtlas.findRegion("walking e0000");
        sprite = new CharacterMovement("worker", walkingAtlas, lookingAtlas, cuttingAtlas, region, map);

        sprite.scale(0.1f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               sprite.walk(destinationX, destinationY);
                           }
                       }
                , 0, 1 / 30.0f);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public TiledMap getMap(){
        return map;
    }


    @Override
    public void show() {

    }

    private void handleInput() {
        //controlling camera

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
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
        }

    }

    @Override
    public void render(float delta) {

        camera.update();
        minimapCamera.update();
        gameHud.update(delta);

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
        }

        int[] backgroundLayers = { 0 }; // don't allocate every frame!
        int[] foregroundLayers = { 1 };    // don't allocate every frame!
        handleInput();

        mapRenderer.render(backgroundLayers);
        mapRenderer.setView(camera);


        batch.begin();
        sprite.draw(batch);
        batch.end();

        mapRenderer.render(foregroundLayers);

        //render & draw hud
        batch.setProjectionMatrix(gameHud.stage.getCamera().combined);
        gameHud.stage.draw();

        //Render minimap
        minimapBatch.setProjectionMatrix(minimapCamera.combined);

        minimapRenderer.render(backgroundLayers);
        minimapRenderer.setView(minimapCamera);

        minimapBatch.begin();
        sprite.draw(minimapBatch);
        minimapBatch.end();

        minimapRenderer.render(foregroundLayers);
    }

    @Override
    public void resize(int width, int height) {

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
        map.dispose();
        mapRenderer.dispose();

    }
}