package com.escargot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Escargot extends AbstractGameObject {
	private static final long serialVersionUID = 3892860159269540367L;
	private static int w = 529;
	private static int h = 187;
	private static String tex_escargot = "escargot_0.png";
	
	public Escargot(int x, int y, float scale, int vitesse) {
		super(x,
			  y,
			  w*scale,
			  h*scale,
			  new Texture(Gdx.files.internal(tex_escargot)),
			  vitesse);
		setDirection(-1);
	}
}
