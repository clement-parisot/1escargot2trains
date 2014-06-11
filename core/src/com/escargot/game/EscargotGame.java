package com.escargot.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.I18NBundle;
import com.escargot.game.screen.EndScreen;
import com.escargot.game.screen.GameScreen;
import com.escargot.game.screen.HelpScreen;
import com.escargot.game.screen.MainMenuScreen;

public class EscargotGame extends Game implements ApplicationListener {
	public SpriteBatch batch;
	public BitmapFont font;
	public ShapeRenderer sr;
	public Score max_score;
	public static boolean son_on = true;
	public static boolean vibre_on = true;
	public String mainscreen1, mainscreen2, help, gameover, score, bestscore;
	public Screen mainMenuScreen, gameScreen, helpScreen, endScreen;
	public Texture bg0, tex_escargot;
	public IActivityRequestHandler myRequestHandler;

	public EscargotGame(IActivityRequestHandler handler){
		myRequestHandler = handler;
	}
	@Override
	public void create() {
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont(Gdx.files.internal("magneto2.fnt"));
		//font = new BitmapFont();
		sr = new ShapeRenderer();
		if (max_score == null)
			max_score = new Score();
		FileHandle baseFileHandle = Gdx.files.internal("i18n/MyBundle");
		I18NBundle myBundle = I18NBundle.createBundle(baseFileHandle);
		mainscreen1 = myBundle.get("mainscreen1");
		mainscreen2 = myBundle.get("mainscreen2");
		help = myBundle.get("help");
		gameover = myBundle.get("gameover");
		score = myBundle.get("score");
		bestscore = myBundle.get("bestscore");
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		son_on = prefs.getBoolean("son_on", true);
		vibre_on = prefs.getBoolean("vibre_on", true);
		max_score.resetScore();
		max_score.setScore(prefs.getFloat("max_score", 0.0f));
		max_score.updateBestScore();
		bg0 = new Texture(Gdx.files.internal("background_0.jpg"));
		tex_escargot = new Texture(Gdx.files.internal("escargot_0.png"));
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		helpScreen = new HelpScreen(this);
		endScreen = new EndScreen(this, max_score);
		this.setScreen(mainMenuScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		sr.dispose();
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", son_on);
		prefs.putBoolean("vibre_on", vibre_on);
		max_score.updateBestScore();
		prefs.putFloat("max_score", max_score.getMaxScoreValue());
		prefs.flush();
		bg0.dispose();
		tex_escargot.dispose();
	}
}
