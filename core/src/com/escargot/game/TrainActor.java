package com.escargot.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.*;

public class TrainActor extends Actor {
	TextureRegion texture;
	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	private float scale = 1.0f;
	private int direction = 1;
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 3892860159269540367L;
	private static int w = 626;
	private static int h = 141;
	private static Vector2 fumee = new Vector2(76, 140);
	private static int fum_x = 76;
	private static int fum_y = 140;
	
	public TrainActor() {
		setWidth(w); setHeight(h);setPosition(200, 140);
	}
	
	public TrainActor(int x, int y, float scale, int direction,
			int vitesse) {
		this();
		this.direction = direction;
		this.scale = scale;
		setWidth(w*scale); setHeight(h*scale);setPosition(x, y);
		addAction(Actions.scaleTo(direction,1.0f));
		addAction(Actions.forever(Actions.moveBy(direction*vitesse, 0)));
	}
    @Override
    public void draw (Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
    
	public float getFumX(){
        return localToParentCoordinates(fumee.cpy().scl(scale)).x;
	}

	public float getFumY(){
		return getY()+fum_y*scale;
	}
}
