package com.escargot.game;

import sun.java2d.loops.DrawRect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.utils.Align;

public class EscargotActor extends Actor {
	TextureRegion texture;
	private static final long serialVersionUID = 3892860159269540367L;
	private static int w = 529;
	private static int h = 187;
	private static String tex_escargot = "escargot_0.png";
	private int direction = 1;
	
	public EscargotActor() {
		texture = new TextureRegion(new Texture(Gdx.files.internal(tex_escargot)));
		setWidth(w); setHeight(h);setOrigin(Align.center);setPosition(320, 140);
	}
	
	public EscargotActor(int x, int y, float scale, int vitesse) {
		this();
		setWidth(w*scale); setHeight(h*scale);setOrigin(Align.center);setPosition(x, y);
		addAction(Actions.forever(Actions.moveBy(vitesse*direction, 0)));
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
		this.addAction(Actions.forever(Actions.moveBy(direction, 0)));
	}
}
