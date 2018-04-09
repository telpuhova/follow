package com.boop442.follow.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.boop442.follow.Follow;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Follow";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new Follow(), config);
	}
}
