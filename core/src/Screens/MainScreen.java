package Screens;

import Scenes.GameHud;
import character_movement.CharacterMovement;
import character_movement.MyInputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.MainGame.MainGame;

import java.util.ArrayList;
import java.util.List;

public class MainScreen implements Screen {

    // Reference to game.
    private MainGame game;
    private TextureAtlas atlas;

    // screen variables
    public static final int WORLD_WIDTH = 1600;
    public static final int WORLD_HEIGHT = 800;
    private static final int MINIMAP_X = -8000;
    private static final int MINIMAP_Y = 4830;
    private Viewport viewport;
    private OrthographicCamera camera;

    //map variables.
    private TiledMap map;
    private IsometricTiledMapRenderer mapRenderer;


    //Sprites
    private SpriteBatch hudBatch;
    private SpriteBatch batch;
    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;
    private TextureAtlas cuttingAtlas;
    private CharacterMovement sprite;
    private CharacterMovement sprite2;
    public List<CharacterMovement> allUnits = new ArrayList<CharacterMovement>();

    //unit selection
    private Rectangle dragging;
    private Vector3 dragStart;
    private Vector3 dragEnd;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    //movement
    private float destinationX = -1;
    private float destinationY = -1;

    // Minimap
    private IsometricTiledMapRenderer minimapRenderer;
    private OrthographicCamera minimapCamera;
    private SpriteBatch minimapSb;


    public GameHud gameHud;

    public MainScreen(MainGame game) {
        this.game = game;

        //SpriteBatch.
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        minimapSb = new SpriteBatch();

        walkingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/walking.atlas"));
        lookingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/looking.atlas"));
        cuttingAtlas = new TextureAtlas(Gdx.files.internal("core/assets/characters/cutting.atlas"));

        gameHud = new GameHud(hudBatch);

        //Camera
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.zoom = 2;
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // minimap camera
        minimapCamera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        minimapCamera.zoom = 14;
        minimapCamera.position.set(new Vector3(MINIMAP_X, MINIMAP_Y,0));

        //Map
        map = new TmxMapLoader().load("core/assets/map/Map_example1.tmx");
        //map = new TmxMapLoader().load("core/assets/map/water_collision.tmx");

        mapRenderer = new IsometricTiledMapRenderer(map);
        minimapRenderer = new IsometricTiledMapRenderer(map);

        TiledMapTileLayer layer0 = (TiledMapTileLayer) map.getLayers().get(0);

        Vector3 center = new Vector3(layer0.getWidth() * layer0.getTileWidth() / 5,
                layer0.getHeight() * layer0.getTileHeight() / 7, 0);
        MapLayers layers = map.getLayers();
        camera.position.set(center);

        TextureAtlas.AtlasRegion region = walkingAtlas.findRegion("walking e0000");
        sprite = new CharacterMovement("worker", walkingAtlas, lookingAtlas, cuttingAtlas, region, map);
        sprite2 = new CharacterMovement("fighter", walkingAtlas, lookingAtlas, cuttingAtlas, region, map);
        allUnits.add(sprite);
        allUnits.add(sprite2);
        sprite.setLocation(600, 5);
        sprite2.setLocation(700, 10);






        sprite.scale(0.1f);
        sprite2.scale(0.1f);
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               sprite.walk();
                               sprite2.walk();
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

    //Add unit to the list containing all units
    public void addCharacter(CharacterMovement character) {
        allUnits.add(character);
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
        shapeRenderer.setProjectionMatrix(camera.combined);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        if (dragging != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(5, 0.5F, 0.5F, 1);
//            shapeRenderer.line(dragging.x, dragging.y, dragging.getWidth(), dragging.getHeight());
            shapeRenderer.rect(200, 200, 100, 100);
            shapeRenderer.end();
        }



        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            MyInputProcessor inputProcessor = new MyInputProcessor();
            Gdx.input.setInputProcessor(inputProcessor);

            if (inputProcessor.touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, 0)) {
                dragStart.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(dragStart);
            }
            if (inputProcessor.touchUp(Gdx.input.getX(), Gdx.input.getY(), 0, 0)) {
                dragStart.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(dragEnd);
            }
            if (dragStart != dragEnd) {
                dragging = new Rectangle(dragStart.x, dragStart.y,
                        Math.abs(dragStart.x - dragEnd.x), Math.abs(dragStart.y - dragEnd.y));
                //Texture rect = new Texture(dragging)
            }
            //projection to world coords
            Vector3 clickOnScreen = new Vector3();
            clickOnScreen.set(Gdx.input.getX(), Gdx.input.getY(), 0);


            camera.unproject(clickOnScreen);

            // Selecting an unit
            for (CharacterMovement unit : allUnits) {
                float x = Math.abs(clickOnScreen.x - unit.getWidth() / 2 - unit.getCurrentX());
                float y = Math.abs(clickOnScreen.y - unit.getCurrentY() - 40);
                if (x < 20 && !(x < 0) && y < 20 && !(y < 0)) {
                    unit.select();
                }
            }


            // For detecting hud
            Vector3 projected = new Vector3();
            projected.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            if (projected.y < 943) {
                for (CharacterMovement unit : allUnits) {
                    if (unit.getSelected()) {
                        unit.setDestination(clickOnScreen.x - unit.getWidth() / 2, clickOnScreen.y - 10);
                    }
                }
            }

        } else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            for (CharacterMovement unit : allUnits) {
                unit.unSelect();
            }
        }

        int[] backgroundLayers = { 0 }; // don't allocate every frame!
        int[] foregroundLayers = { 1 };    // don't allocate every frame!
        handleInput();


        mapRenderer.render(backgroundLayers);
        mapRenderer.setView(camera);

        batch.begin();
        sprite.draw(batch);
        sprite2.draw(batch);
        //batch.draw(dragging, (float)dragStart.x, (float)dragStart.y, dragging.getWidth(), dragging.getHeight());
        batch.end();

        mapRenderer.render(foregroundLayers);

        //render & draw hud
        batch.setProjectionMatrix(gameHud.stage.getCamera().combined);
        gameHud.stage.draw();

        // minimap rendering
        minimapSb.setProjectionMatrix(minimapCamera.combined);
        minimapRenderer.setView(minimapCamera);
        minimapRenderer.render();
        minimapSb.begin();
        sprite.draw(minimapSb);
        sprite2.draw(minimapSb);
        minimapSb.end();

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