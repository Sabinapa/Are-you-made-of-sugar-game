package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.Naloga2.SugarCubeGame1;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncherNaloga2 {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Are you made of sugar?");	// window title
		//config.width = 800;		// window width in pixels
		//config.height = 600;	// window height in pixels
		//config.forceExit = false;

		new Lwjgl3Application(new SugarCubeGame1(), config);
	}
}
