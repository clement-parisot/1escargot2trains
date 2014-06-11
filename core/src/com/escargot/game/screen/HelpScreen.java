package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.escargot.game.EscargotGame;

public class HelpScreen implements Screen {

	final EscargotGame game;
	private OrthographicCamera camera;
	private Texture retour;

	public HelpScreen(EscargotGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		retour = new Texture(Gdx.files.internal("back.png"));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(game.bg0, -512, 0, 1920, 1200);
		game.font.drawWrapped(game.batch, game.help, 20, 350, 620);
		game.batch.draw(game.tex_escargot, 300, 64, 161, 100);
		game.batch.draw(retour, 0, 0, 64, 64);
		game.batch.end();

		if (Gdx.input.justTouched()) {
			game.setScreen(game.mainMenuScreen);
		}
	}

	@Override
	public void resize(int width, int height) {
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
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		retour.dispose();
	}

}
