package Screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.sun.tools.javadoc.Start;


public class StartingScreen implements Screen {
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

   public StartingScreen(final Game game, final Screen screen) {
        super();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("core/assets/screens/startingscreen/glassy-ui.json"));
        stage = new Stage();

        final TextButton button = new TextButton("Start Game", skin, "default");

        button.setWidth(200f);
        button.setHeight(60f);
        button.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 10f);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(screen);
            }
        });

        stage.addActor(button);
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
        batch.dispose();
    }
}
