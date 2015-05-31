package com.escargot.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.I18NBundle;
import com.escargot.game.screen.EndScreen;
import com.escargot.game.screen.HelpScreen;
import com.escargot.game.screen.LoadingScreen;

public class EscargotGame extends Game implements ApplicationListener {
	public SpriteBatch batch;
	public ShapeRenderer sr;
	public Score score_player;
	public static boolean son_on = true;
	public static boolean vibre_on = true;
	public Screen loadingScreen, helpScreen, endScreen;
	public IActivityRequestHandler myRequestHandler;
	public boolean achievementList[] = { false, false, false, false, false };
	public AssetManager manager;
	private Preferences prefs;
	public I18NBundle bundle;
	public boolean pause = false;

	public EscargotGame(IActivityRequestHandler handler) {
		myRequestHandler = handler;
	}

	@Override
	public void create() {
		manager = new AssetManager();
		manager.load("magneto3.fnt", BitmapFont.class);
		manager.load("vanilla.fnt", BitmapFont.class);
		manager.load("nashville.fnt", BitmapFont.class);
		manager.load("i18n/MyBundle", I18NBundle.class);
		manager.load("background_0.jpg", Texture.class);
		manager.load("rails_0.png", Texture.class);
		manager.load("pack.atlas", TextureAtlas.class);
		loadingScreen = new LoadingScreen(this);
		this.setScreen(loadingScreen);
		
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
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
		score_player.resetScore();

	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		sr.dispose();
		manager.dispose();
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", son_on);
		prefs.putBoolean("vibre_on", vibre_on);
		score_player.updateBestScore();
		prefs.putFloat("max_score", score_player.getMaxScoreValue());
		prefs.flush();
		loadingScreen.dispose();
		helpScreen.dispose();
		endScreen.dispose();
	}
}
