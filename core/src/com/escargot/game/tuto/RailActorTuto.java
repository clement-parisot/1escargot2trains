package com.escargot.game.tuto;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class RailActorTuto extends Actor {
	Texture texture;
	private static int w = 1920;
	private static int h = 30;
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public RailActorTuto() {
		setOrigin(Align.topLeft);setWidth(w); setHeight(h);setPosition(0, 115);
	}
	
    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        texture.setWrap(TextureWrap.MirroredRepeat, TextureWrap.ClampToEdge);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        
    }
}
