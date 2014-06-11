package com.escargot.game;

import com.badlogic.gdx.graphics.Texture;

public class Train extends AbstractGameObject{
	private static final long serialVersionUID = 3892860159269540367L;
	
	public Train(int x, int y, int w, int h, Texture tex, int direction, int vitesse){
			super(x, y, w, h, tex, vitesse);
			this.setDirection(direction);
	}
}