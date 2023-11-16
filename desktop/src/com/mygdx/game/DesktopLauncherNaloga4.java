package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.Naloga22.Wheels;
import com.mygdx.game.Naloga4.SugarCubeGameUnits;

public class DesktopLauncherNaloga4 {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//config.setForegroundFPS(60);
		config.setTitle("Are you made of sugar?");	// window title
		//config.width = 1024;
		//config.height = 480;

		new Lwjgl3Application(new SugarCubeGameUnits(), config);
	}
}
