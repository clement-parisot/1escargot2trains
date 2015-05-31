package com.escargot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.utils.Align;

public class EscargotActor extends Actor {
	TextureRegion texture;
	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 3892860159269540367L;
	private static int w = 529;
	private static int h = 187;
	private int direction = 1;
	private int vitesse;
	
	public EscargotActor() {
		setWidth(w); setHeight(h);setOrigin(Align.center);setPosition(320, 140);
	}
	
	public EscargotActor(int x, int y, float scale, int vitesse) {
		this();
		this.vitesse = vitesse;
		setWidth(w*scale); setHeight(h*scale);setOrigin(Align.center);setPosition(x, y);
		addAction(Actions.forever(
				Actions.parallel(
						Actions.scaleTo(-direction, 1.0f),
						Actions.moveBy(vitesse*direction, 0))
				)
		);
	}
	
    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        
    }
    
	public void flip(int x) {
		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			direction *= -1;
		} else {
			if (x < 320)
				direction = 1;
			else
				direction =-1;
		}
		clearActions();
		this.addAction(Actions.forever(
				Actions.parallel(
						Actions.scaleTo(-direction, 1.0f),
						Actions.moveBy(vitesse*direction, 0))
				)
		);
	}
}
