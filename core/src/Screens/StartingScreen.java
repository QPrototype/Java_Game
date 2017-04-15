package Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
//import com.sun.tools.javadoc.Start;


public class StartingScreen implements Screen {
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    // UI background
    private Table tableBG;
    private Skin mainBG;
    private Drawable bgDrawable;

    // UI tableContent
    private Table tableContent;
    private Skin  test;
    private Drawable testDra;

    public StartingScreen(final Game game, final Screen screen) {

        stage = new Stage();

        // Starting screen buttons design.
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("core/assets/screens/startingscreen/uiskin.json"));
        stage = new Stage();

        // Constructing buttons
        final TextButton startButton = new TextButton("Start Game", skin, "default");
        final TextButton exitButton = new TextButton("Exit Game", skin, "default");
        final TextButton settingButton = new TextButton("Settings", skin, "default");
        final TextButton scoreButton = new TextButton("Highcores", skin, "default");

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(screen);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });
        // TODO: Settings and highscore button listeners.


        // Starting screen background.
        tableBG = new Table();
        tableBG.setFillParent(true);
        mainBG = new Skin();
        mainBG.add("startingBackground", new Texture("core/assets/screens/startingscreen/startingBackground.jpg"));
        bgDrawable = mainBG.getDrawable("startingBackground");
        tableBG.setBackground(bgDrawable);

        // Content layout on starting screen
        tableContent = new Table();
        test = new Skin();
        tableContent.add(startButton).size(200, 45).pad(3, 10, 3, 10);
        tableContent.row();
        tableContent.add(settingButton).size(200, 45).pad(3, 10, 3, 10);
        tableContent.row();
        tableContent.add(scoreButton).size(200, 45).pad(3, 10, 3, 10);
        tableContent.row();
        tableContent.add(exitButton).size(200, 45).pad(3, 10, 3, 10);

        tableBG.add(tableContent);
        stage.addActor(tableBG);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        stage.draw();
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
