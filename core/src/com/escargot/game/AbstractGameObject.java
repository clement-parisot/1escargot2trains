package com.escargot.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class AbstractGameObject extends Rectangle {
	private static final long serialVersionUID = -6681616551560807046L;
	protected Texture texture;
	private int direction = 1;
	public int vitesse;

	protected AbstractGameObject(float x, float y, float w, float h, Texture t,
			int vitesse) {
		super(x, y, w, h);
		this.texture = t;
		this.vitesse = vitesse;
	}

	public void flip(int x) {
		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			this.setDirection(direction * -1);
		} else {
			if (x < 320)
				this.setDirection(1);
			else
				this.setDirection(-1);
		}
	}

	public void draw(SpriteBatch batch) {
		if (direction == 1)
			batch.draw(texture, x, y, width, height);
		else
			batch.draw(texture, x + width, y, -1 * width, height);
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
