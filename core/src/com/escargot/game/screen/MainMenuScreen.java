package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.escargot.game.EscargotGame;

public class MainMenuScreen implements Screen {
	final EscargotGame game;
	Texture tex_son_on, tex_son_off, aide, vibre_on, vibre_off;

	OrthographicCamera camera;

	public MainMenuScreen(final EscargotGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);

		// Images
		tex_son_on = new Texture(Gdx.files.internal("son_on.png"));
		tex_son_off = new Texture(Gdx.files.internal("son_off.png"));
		vibre_on = new Texture(Gdx.files.internal("vibre_on.png"));
		vibre_off = new Texture(Gdx.files.internal("vibre_off.png"));
		aide = new Texture(Gdx.files.internal("aide.png"));
		
		//Pub
		game.myRequestHandler.showAds(true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(game.bg0, -512, 0, 1920, 1200);
		if (EscargotGame.son_on)
			game.batch.draw(tex_son_on, 0, 0, 64, 64);
		else
			game.batch.draw(tex_son_off, 0, 0, 64, 64);
		if (EscargotGame.vibre_on)
			game.batch.draw(vibre_on, 288, 0, 64, 64);
		else
			game.batch.draw(vibre_off, 288, 0, 64, 64);
		game.batch.draw(aide, 576, 0, 64, 64);
		game.font.drawWrapped(game.batch, game.mainscreen1, 120, 300, 400,
				HAlignment.CENTER);
		game.font.drawWrapped(game.batch, game.mainscreen2, 120, 250, 400,
				HAlignment.CENTER);
		game.batch.draw(game.tex_escargot, 300, 64, 161, 100);
		game.batch.end();

		if (Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			if (x < 64 && y > Gdx.graphics.getHeight() - 64) {
				EscargotGame.son_on = !EscargotGame.son_on;
			} else if (x > Gdx.graphics.getWidth() - 64
					&& y > Gdx.graphics.getHeight() - 64) {
				game.setScreen(new HelpScreen(game));
				dispose();
			} else if (x > Gdx.graphics.getWidth() / 2 - 32
					&& x < Gdx.graphics.getWidth() / 2 + 32
					&& y > Gdx.graphics.getHeight() - 64) {
				EscargotGame.vibre_on = !EscargotGame.vibre_on;
				if (EscargotGame.vibre_on)
					Gdx.input.vibrate(1000);
			} else {
				game.setScreen(new GameScreen(game));
				dispose();
			}
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
		tex_son_on.dispose();
		tex_son_off.dispose();
		aide.dispose();
		vibre_on.dispose();
		vibre_off.dispose();
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", EscargotGame.son_on);
		prefs.putBoolean("vibre_on", EscargotGame.vibre_on);
		prefs.flush();
	}
}
