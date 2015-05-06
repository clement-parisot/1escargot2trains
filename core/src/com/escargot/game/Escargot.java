package com.escargot.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Escargot extends AbstractGameObject {
	private static final long serialVersionUID = 3892860159269540367L;
	private static int w = 529;
	private static int h = 187;
	
	public Escargot(int x, int y, float scale, int vitesse, Sprite sprite) {
		super(x,
			  y,
			  w*scale,
			  h*scale,
			  sprite,
			  vitesse);
		setDirection(-1);
	}
}
