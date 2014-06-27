package com.escargot.game;

import com.badlogic.gdx.graphics.Texture;

public class Escargot extends AbstractGameObject {
	private static final long serialVersionUID = 3892860159269540367L;

	public Escargot(int x, int y, int w, int h, Texture tex, int vitesse) {
		super(x, y, w, h, tex, vitesse);
		setDirection(1);
	}
}
