package com.escargot.game.tuto;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class EscargotActorMenu extends Actor {
	Animation texture;
	private float stateTime = 0;
	public void setTexture(Animation texture) {
		this.texture = texture;
		this.texture.setPlayMode(PlayMode.LOOP_PINGPONG);
	}

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 3892860159269540367L;
	private static int w = 529;
	private static int h = 187;
	private int direction = 1;
	public int getDirection() {
		return direction;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
	    stateTime += delta;
	}

	public void reset()
	{
	    stateTime = 0;
	}
	private double vitesse;

	public double getVitesse() {
		return vitesse;
	}

	public void setVitesse(double vitesse) {
		if(this.vitesse != vitesse){
			this.vitesse = vitesse;
		}
	}

	public EscargotActorMenu() {
		setWidth(w); setHeight(h);setOrigin(Align.center);setPosition(320, 140);
	}

	public EscargotActorMenu(int x, int y, float scale, double vitesse) {
		this();
		this.vitesse = vitesse;
		setWidth(w*scale); setHeight(h*scale);setOrigin(Align.center);
		setPosition(x, y, Align.center+Align.bottom);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		this.texture.setFrameDuration((float) (1/(getVitesse()*30f)));
		batch.draw((TextureRegion) this.texture.getKeyFrame(this.stateTime, true), getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
}
