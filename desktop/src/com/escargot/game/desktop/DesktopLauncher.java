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

	@Override
	public boolean isSignedIn() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void beginUserSignIn() {
		System.out.println("Signin...");
	}

	@Override
	public void signOutUser() {
		System.out.println("Signout");
	}

	@Override
	public void rateApp() {
		System.out.println("rate");
	}

	@Override
	public void unlock(int id) {
		
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@Override
	public void showAchievments() {
		System.out.println("achiev");
	}

	@Override
	public void envoyerScore(int score) {
		
	}

	@Override
	public void classement() {
		System.out.println("classement");
	}
}
