package com.escargot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Train extends AbstractGameObject {
	private static final long serialVersionUID = 3892860159269540367L;
	private static int w = 626;
	private static int h = 140;
	private static int fum_x = 76;
	private static int fum_y = 140;
	private static String tex_train = "train_0.png";
	private float scale;
	
	public Train(int x, int y, float scale, int direction,
			int vitesse) {
		super(x,
			  y,
			  w*scale,
			  h*scale,
			  new Texture(Gdx.files.internal(tex_train)),
			  vitesse);
		this.setScale(scale);
		this.setDirection(direction);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getFumX(){
		if(getDirection() > 0)
			return x+fum_x*scale*getDirection();
		else
			return x+fum_x*scale*getDirection() + w;
	}
	
	public float getFumY(){
		return y+fum_y*scale;
	}
}