package com.mygdx.game.desktop;

import Screens.MainScreen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Testing";
		config.width = 1280;
		config.height = 720;
//		config.fullscreen = true;

		new LwjglApplication(new MyGdxGame(), config);
	}
}
