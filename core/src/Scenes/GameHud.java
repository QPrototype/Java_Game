package Scenes;

import Screens.MainScreen;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameHud {

    public Stage stage;
    private Camera camera;
    private Viewport viewport;


    //score && time tracking variables
    public static int worldTimer;
    private float timeCount;
    private static Integer score;
    private boolean timeUp;
    private Label countdownLabel, timeLabel, linkLabel, unitActivitiesLabel, mapLabel;
    private Label woodLabel, foodLabel, goldLabel;

    public static int woodCount, foodCount, goldCount;

    private static Label scoreLabel;

    //Tables
    private Table timeTable;
    private Table root;
    private Table unitImgTable;
    private Table resourceTable;
    private Table unitActivitiesTable;
    private Table map;

    private Skin unitSkin;
    private Skin resourceSkin;
    private Skin timeSkin;

    private Drawable drawable;
    private Drawable drawable2;
    private Drawable timeBG;

    private Image unit;

    public GameHud(SpriteBatch spriteBatch) {

        //define tracking variables
        worldTimer = 1500;
        timeCount = 0;
        score = 0;

        //setup the HUD viewport using a new camera seperate from gamecam
        //define stage using that viewport and games spritebatch
        camera = new OrthographicCamera(MainScreen.WORLD_WIDTH, MainScreen.WORLD_HEIGHT);
        viewport = new FillViewport(MainScreen.WORLD_WIDTH, MainScreen.WORLD_HEIGHT, camera);
        stage = new Stage(viewport, spriteBatch);

        //Skin for game hud background.
        unitSkin = new Skin();
        unitSkin.add("hud", new Texture("core/assets/screens/gamehud/hud.jpg"));
        drawable = unitSkin.getDrawable("hud");

        resourceSkin = new Skin();
        resourceSkin.add("bg", new Texture("core/assets/screens/gamehud/bg.jpg"));
        drawable2 = resourceSkin.getDrawable("bg");
        unit = new Image(new Texture("core/assets/screens/gamehud/testunit.png"));
        unit.setScaling(Scaling.stretch);

        //time----->
        timeSkin = new Skin();
        timeSkin.add("timeBackground", new Texture("core/assets/screens/gamehud/timeBackground.png"));
        timeBG = timeSkin.getDrawable("timeBackground");

        //define labels using the String, and a Label style consisting of a font and color

        //resources
        woodLabel = new Label(("Wood: " + woodCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        foodLabel = new Label(("Food: " + foodCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        goldLabel = new Label(("Gold: " + goldCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        woodLabel.setFontScale(2);
        foodLabel.setFontScale(2);
        goldLabel.setFontScale(2);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        mapLabel = new Label("Minimap goes here, mby ?", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        unitActivitiesLabel = new Label("Selected Unit Activites", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        unitActivitiesLabel.setFontScale(2);
        mapLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        countdownLabel.setFontScale(2);

        // Game time table configuration;
        timeTable = new Table();
        timeTable.setBackground(timeBG);
        timeTable.setPosition(1500, 700);
        timeTable.setSize(100, 100);
        timeTable.add(countdownLabel);

        //Configuring unit picture table.
        unitImgTable = new Table();
        unitImgTable.setBackground(drawable2);
        unitImgTable.add(unit).expandX().pad(5);

        //Configuring resource table.
        resourceTable = new Table();
        resourceTable.add(woodLabel);
        resourceTable.row();
        resourceTable.add(foodLabel);
        resourceTable.row();
        resourceTable.add(goldLabel);

        // Configuring Selected unit Activities
        unitActivitiesTable = new Table();
        unitActivitiesTable.add(unitActivitiesLabel).expandX().align(10);

        //Map table
        map = new Table();
        map.add(mapLabel);

        // Confiqure root table.
        root = new Table();
        root.bottom();
        root.setSize(MainScreen.WORLD_WIDTH, MainScreen.WORLD_HEIGHT / 8);
        root.setBackground(drawable);


        // Add sub tables to root table.
        root.add(unitImgTable).width(150).height(MainScreen.WORLD_HEIGHT / 8);
        root.add(resourceTable).width(200).expandY();
        root.add(unitActivitiesTable).width(300).expandY();
        root.add(map).expandX().expandY();

        //add table to the stage
        stage.addActor(root);
        stage.addActor(timeTable);
    }

    public void update(float dt) {
        timeCount += dt;
        woodLabel.setText(String.format("Wood: %d", woodCount));
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

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public static void addScore(int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public static int getTime() {
        return worldTimer;
    }

    public static Label getScoreLabel() {
        return scoreLabel;
    }

    public static Integer getScore() {
        return score;
    }

    public static void addWood(int amount) {
        woodCount += amount;
    }
}