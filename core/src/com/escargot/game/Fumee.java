package com.escargot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class Fumee extends ParticleEffect{

	private static String effect_fumee = "fumee_train";
	
	public Fumee(EscargotGame game){
		this.load(Gdx.files.internal(effect_fumee), game.atlas);
	}
}
