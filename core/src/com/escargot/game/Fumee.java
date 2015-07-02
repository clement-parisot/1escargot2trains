package com.escargot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class Fumee extends ParticleEffect{

	private static String effect_fumee = "fumee_train";
	
	public Fumee(){
		this.load(Gdx.files.internal(effect_fumee), RessourcesManager.getInstance().getAtlas("pack.atlas"));
	}
}
