package com.escargot.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.I18NBundle;

public final class RessourcesManager {
	private static RessourcesManager instance;
	private static AssetManager manager;
	
	private RessourcesManager() {
		manager = new AssetManager();
		manager.load("magneto3.fnt", BitmapFont.class);
		manager.load("vanilla.fnt", BitmapFont.class);
		manager.load("nashville.fnt", BitmapFont.class);
		manager.load("i18n/MyBundle", I18NBundle.class);
		manager.load("background_0.jpg", Texture.class);
		manager.load("rails_0.png", Texture.class);
		manager.load("pack.atlas", TextureAtlas.class);
		manager.load("sound0.mp3", Music.class);
		manager.load("sound2.wav", Sound.class);
		manager.load("sound1.wav", Sound.class);
    }
	
	public static RessourcesManager getInstance() {
        if (null == instance || null == manager) {
            instance = new RessourcesManager();
        }
        return instance;
    }

	public boolean update()
	{    
	    return manager.update();        
	}

	public void dispose()
	{
	       manager.dispose();
	       manager = null;
	}

	public float getProgress() {
		return manager.getProgress();
	}

	public TextureAtlas getAtlas(String fileName) {
		return manager.get(fileName, TextureAtlas.class);
	}

	public BitmapFont getFont(String fileName) {
		return manager.get(fileName, BitmapFont.class);
	}

	public I18NBundle getBundle() {
		return manager.get("i18n/MyBundle", I18NBundle.class);
	}

	public Texture getTexture(String fileName) {
		return manager.get(fileName,Texture.class);
	}

	public Music getMusic(String fileName) {
		return manager.get(fileName);
	}

	public Sound getSound(String fileName) {
		return manager.get(fileName);
	}

	public void finishLoad() {
		manager.finishLoading();
	}
}
