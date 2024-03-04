package com.escargot.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.I18NBundle;
import com.escargot.game.screen.ScreenManager;
import com.escargot.game.screen.ScreenName;

public class EscargotGame extends Game implements ApplicationListener {
	public static Score score_player;
	public static boolean son_on = true;
	public static boolean vibre_on = true;
	public Screen mainScreen, tutoScreen, gameScreen, endScreen, scoreScreen;
	public static boolean achievementList[] = { false, false, false, false, false, false, false, false, false };
	public static int playTime = 3;
	private Preferences prefs;
	public I18NBundle bundle;
	public boolean pause = false;
	public static IActivityRequestHandler myRequestHandler;
	public EscargotGame(IActivityRequestHandler handler) {
		myRequestHandler = handler;
	}

	@Override
	public void create() {
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(ScreenName.LOADING);
		if (score_player == null)
			score_player = new Score();
		prefs = Gdx.app.getPreferences("Escargot prefs");
		son_on = prefs.getBoolean("son_on", true);
		vibre_on = prefs.getBoolean("vibre_on", true);
		for (int i = 0; i < achievementList.length; i++) {
			achievementList[i] = prefs.getBoolean("a"+i, false);
		}
		score_player.resetScore();
		score_player.setScore(prefs.getFloat("max_score", 0.0f));
		score_player.updateBestScore();
		score_player.resetScore();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		ScreenManager.getInstance().dispose();
		RessourcesManager.getInstance().dispose();
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", son_on);
		prefs.putBoolean("vibre_on", vibre_on);
		score_player.updateBestScore();
		prefs.putFloat("max_score", score_player.getMaxScoreValue());
		prefs.flush();
	}

	public static boolean isOnAndroidTV() {
		return Gdx.app.getType().equals(Application.ApplicationType.Android) &&
				!Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
	}
}
