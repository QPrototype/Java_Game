package Scenes;

import Screens.MainScreen;

import character_movement.CharacterMovement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import javafx.scene.paint.ImagePattern;

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
    private Table menu;

    private Table wood;
    private Table food;
    private Table gold;

    private Skin unitSkin;
    private Skin resourceSkin;
    private Skin timeSkin;
    private Skin woodSkin;
    private Skin foodSkin;
    private Skin goldSkin;
    private Skin menuSkin;

    private Drawable drawable;
    private Drawable drawable2;
    private Drawable timeBG;

    private final Button menuButton;

    public  Image unit;
    private Image fighterIcon;


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

        fighterIcon = new Image(new Texture("core/assets/screens/gamehud/fighter_icon.bmp"));
        fighterIcon.setScale(0.5f, 0.5f);


        //time----->
        timeSkin = new Skin();
        timeSkin.add("timeBackground", new Texture("core/assets/screens/gamehud/timeBackground.png"));
        timeBG = timeSkin.getDrawable("timeBackground");

        //define labels using the String, and a Label style consisting of a font and color

        //resources
        wood = new Table();
        woodSkin = new Skin();
        woodSkin.add("wood", new Texture("core/assets/screens/gamehud/wood.png"));
        wood.setBackground(woodSkin.getDrawable("wood"));

        food = new Table();
        foodSkin = new Skin();
        foodSkin.add("food", new Texture("core/assets/screens/gamehud/food.png"));
        food.setBackground(foodSkin.getDrawable("food"));

        gold = new Table();
        goldSkin = new Skin();
        goldSkin.add("gold", new Texture("core/assets/screens/gamehud/gold.png"));
        gold.setBackground(goldSkin.getDrawable("gold"));

        woodLabel = new Label(("    " + woodCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        foodLabel = new Label(("    " + foodCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        goldLabel = new Label(("    " + goldCount), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        woodLabel.setFontScale(2);
        foodLabel.setFontScale(2);
        goldLabel.setFontScale(2);


        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        unitActivitiesLabel = new Label("Selected Unit Activites", new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        unitActivitiesLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        countdownLabel.setFontScale(2);

        // In game menu button
        menuSkin = new Skin(Gdx.files.internal("core/assets/screens/startingscreen/uiskin.json"));
        menuButton = new TextButton("MENU", menuSkin, "default");
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Restart clicked!");
            }
            //TODO: button location is somewhere lost :p. Need fix!
        });

        //Menu skin
        menu = new Table();
//        menu.setFillParent(true);
        menu.setPosition(1480, 770);
        menu.setSize(120, 30);
        menu.add(menuButton).size(120, 30);


        // Game time table configuration;
        timeTable = new Table();
        timeTable.center();
        timeTable.setBackground(timeBG);
        timeTable.setPosition(750, 700);
        timeTable.setSize(100, 100);
        timeTable.add(countdownLabel);

        //Configuring unit picture table.
        unitImgTable = new Table();
        unitImgTable.setBackground(drawable2);
        unitImgTable.add(unit).expandX().pad(5);

        //Configuring resource table.
        resourceTable = new Table();
        resourceTable.left();
        resourceTable.add(wood).size(40, 30).pad(2);
        resourceTable.add(woodLabel);
        resourceTable.row();
        resourceTable.add(food).size(40, 30).pad(2);
        resourceTable.add(foodLabel);
        resourceTable.row();
        resourceTable.add(gold).size(40, 30).pad(2);
        resourceTable.add(goldLabel);

        // Configuring Selected unit Activities
        unitActivitiesTable = new Table();
        //unitActivitiesTable.add(unitActivitiesLabel).expandX().align(10);
        unitActivitiesTable.add(fighterIcon);

        //Map table
        map = new Table();

        // Confiqure root table.
        root = new Table();
        root.bottom();
        root.setSize(MainScreen.WORLD_WIDTH, MainScreen.WORLD_HEIGHT / 7);
        root.setBackground(drawable);


        // Add sub tables to root table.
        root.add(unitImgTable).width(150).height(MainScreen.WORLD_HEIGHT / 7);
        root.add(resourceTable).width(200).expandY();
        root.add(unitActivitiesTable).width(300).expandY();
        root.add(map).expandX().expandY();

        //add table to the stage
        stage.addActor(root);
        stage.addActor(timeTable);
        stage.addActor(menu);

        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt) {
//        System.out.println(menuButton.getX() + " " + menuButton.getY());

        timeCount += dt;
        woodLabel.setText(String.format("%d", woodCount));
        foodLabel.setText(String.format("%d", foodCount));
        goldLabel.setText(String.format("%d", goldCount));
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


    public static void addWood(int amount) {
        woodCount += amount;
    }
}