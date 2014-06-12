package com.escargot.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.escargot.game.EscargotGame;
import com.escargot.game.IActivityRequestHandler;

public class DesktopLauncher implements IActivityRequestHandler {
	private static DesktopLauncher application;

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Escargot";
		config.width = 640;
		config.height = 480;
		if (application == null) {
			application = new DesktopLauncher();
		}
		new LwjglApplication(new EscargotGame(application), config);
	}

	@Override
	public void showAds(boolean show) {
	}
}
