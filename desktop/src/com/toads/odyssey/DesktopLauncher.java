package com.toads.odyssey;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument

//Main class that starts the
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1280, 720);
		config.setDecorated(true); //window border and title bar
		config.setForegroundFPS(60); //frame rate
		config.useVsync(true);
		config.setTitle("Toad's Odyssey");
		new Lwjgl3Application(new Boot(), config);

//		config.setIdleFPS(60);
//		config.useVsync(true);
//		config.setWindowedMode(1280, 720);
//		new Lwjgl3Application(new Boot(), config);

	}
}
