package Scenes;

import Screens.MainScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    public Stage stage;
    private Viewport viewport;

    //score && time tracking variables

    private Integer worldTimer;
    private float timeCount;
    private static Integer score;
    private boolean timeUp;

    private Label countdownLabel, timeLabel, linkLabel;
    private static Label scoreLabel;

    final Texture img = new Texture(Gdx.files.internal("core/assets/hud/testHud.png"));
    final Drawable splashDrawable = new TextureRegionDrawable(new TextureRegion(img));


    public Hud(SpriteBatch sb) {
        //define tracking variables
        worldTimer = 1500;
        timeCount = 0;
        score = 0;

        //setup the HUD viewport using a new camera seperate from gamecam
        //define stage using that viewport and games spritebatch
        viewport = new FitViewport(1280, 720, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        timeLabel = new Label("LEFTOVER TIME", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        linkLabel = new Label("POINTS", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        linkLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        countdownLabel.setFontScale(2);

        //define a table used to organize hud's labels
        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        //add labels to table, padding the top, and giving them all equal width with expandX
        table.add(linkLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(countdownLabel).expandX();

        //add table to the stage
        stage.addActor(table);

    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }


    public boolean isTimeUp() {
        return timeUp;
    }


    public static Label getScoreLabel() {
        return scoreLabel;
    }

    public static Integer getScore() {
        return score;
    }

}
