package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.escargot.game.EscargotGame;

public class ScoreScreen implements Screen {
	
	final EscargotGame game;
	private OrthographicCamera camera;
	private Texture retour, signIn, signOut;
	private boolean isSignedIn = false;
	
	public ScoreScreen(EscargotGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		retour = new Texture(Gdx.files.internal("back.png"));
		signIn = new Texture(Gdx.files.internal("signIn.png"));
		signOut = new Texture(Gdx.files.internal("signOut.png"));
	}

	@Override
	public void render(float delta) {
		isSignedIn = game.myRequestHandler.isSignedIn();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(game.bg0, -512, 0, 1920, 1200);
		game.batch.draw(retour, 0, 0, 64, 64);
		if(!isSignedIn){
			game.font.drawWrapped(game.batch, "Pas connecté !",	320, 240, 240);
			game.batch.draw(signIn, 576, 0, 64, 64);
		}else{
			game.font.drawWrapped(game.batch, "Connecté !",	320, 240, 240);
			game.batch.draw(signOut, 576, 0, 64, 64);
		}
		game.batch.end();

		if (Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();

			if (x < 64 && y > Gdx.graphics.getHeight() - 64) {
				// back
				game.setScreen(game.mainMenuScreen);
				dispose();
			} else if (x > Gdx.graphics.getWidth() - 64
					&& y > Gdx.graphics.getHeight() - 64) {
				//sign in ou out
				if(isSignedIn){
					game.myRequestHandler.signOutUser();
				}else{
					game.myRequestHandler.beginUserSignIn();
				}
			}
			
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		game.myRequestHandler.showAds(true);
	}

	@Override
	public void hide() {
		game.myRequestHandler.showAds(false);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		retour.dispose();
	}

}
