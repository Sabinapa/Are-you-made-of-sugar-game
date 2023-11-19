package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.Naloga22.Wheels;
import com.mygdx.game.Naloga4.SugarCubeGameUnits;

public class DesktopLauncherNaloga4 {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Are you made of sugar?");	// window title
		config.setWindowedMode(640, 480);	// window size

		new Lwjgl3Application(new SugarCubeGameUnits(), config);
	}
}
