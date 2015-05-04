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
	public BitmapFont font;
	public ShapeRenderer sr;
	public Score score_player;
	public static boolean son_on = true;
	public static boolean vibre_on = true;
	public String escargot, trains, help, gameover, score_txt,
			bestscore_txt;
	public Screen mainMenuScreen, helpScreen, endScreen;
	public Texture bg0;
	public IActivityRequestHandler myRequestHandler;
	public boolean achievementList[] = { false, false, false, false, false };
	public BitmapFont fontVanilla;
	public BitmapFont fontNashville;
	public TextureAtlas atlas;
	public AssetManager manager;

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
		manager.load("pack.atlas", TextureAtlas.class);
		
		batch = new SpriteBatch();
		// Use LibGDX's default Arial font.
		font = new BitmapFont(Gdx.files.internal("magneto3.fnt"));
		font.setColor(Color.WHITE);
		// Use LibGDX's default Arial font.
		fontVanilla = new BitmapFont(Gdx.files.internal("vanilla.fnt"));
		fontVanilla.setColor(Color.WHITE);
		fontNashville = new BitmapFont(Gdx.files.internal("nashville.fnt"));
		fontNashville.setColor(Color.WHITE);
		sr = new ShapeRenderer();
		if (score_player == null)
			score_player = new Score();
		FileHandle baseFileHandle = Gdx.files.internal("i18n/MyBundle");
		I18NBundle myBundle = I18NBundle.createBundle(baseFileHandle);
		escargot = myBundle.get("snail");
		trains = myBundle.get("trains");
		help = myBundle.get("help");
		gameover = myBundle.get("gameover");
		score_txt = myBundle.get("score");
		bestscore_txt = myBundle.get("bestscore");
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		son_on = prefs.getBoolean("son_on", true);
		vibre_on = prefs.getBoolean("vibre_on", true);
		for (int i = 0; i < achievementList.length; i++) {
			achievementList[i] = prefs.getBoolean("a0", false);
		}
		score_player.resetScore();
		score_player.setScore(prefs.getFloat("max_score", 0.0f));
		score_player.resetScore();
		bg0 = new Texture(Gdx.files.internal("background_0.jpg"));
		atlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
		mainMenuScreen = new LoadingScreen(this);
		helpScreen = new HelpScreen(this);
		endScreen = new EndScreen(this, score_player);
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
		score_player.updateBestScore();
		prefs.putFloat("max_score", score_player.getMaxScoreValue());
		prefs.flush();
		mainMenuScreen.dispose();
		helpScreen.dispose();
		endScreen.dispose();
		bg0.dispose();
		atlas.dispose();
	}
}
