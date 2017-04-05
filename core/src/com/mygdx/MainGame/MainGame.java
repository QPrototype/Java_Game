package com.mygdx.MainGame;

import Screens.MainScreen;
import Screens.StartingScreen;
import com.badlogic.gdx.*;

public class MainGame extends Game {


    @Override
    public void create() {
        setScreen(new StartingScreen(this, new MainScreen(this)));

    }

    @Override
    public void render() {

        super.render();
    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}