package com.escargot.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class RailActor extends Actor {
	TextureRegion texture;
	private static int w = 2048*640/2048;
	private static int h = 97*640/2048;
	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}
	
	public RailActor() {
		setOrigin(Align.topLeft);setWidth(w); setHeight(h);setPosition(0, 115);
	}
	
    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        
    }
}
