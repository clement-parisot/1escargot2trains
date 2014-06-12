package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.escargot.game.EscargotGame;
import com.escargot.game.Score;

public class EndScreen implements Screen {

	final EscargotGame game;
	private OrthographicCamera camera;
	private Texture retour;
	
	public EndScreen(final EscargotGame game, final Score score) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		retour = new Texture(Gdx.files.internal("back.png"));
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", EscargotGame.son_on);
		prefs.putBoolean("vibre_on", EscargotGame.vibre_on);
		prefs.putFloat("max_score", score.getMaxScoreValue());
		prefs.flush();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(game.bg0, -512, 0, 1920, 1200);
		game.font.draw(game.batch, game.gameover, 250, 350);
		game.font.draw(game.batch, game.score_txt + game.score_player, 200, 300);
		game.font.draw(game.batch, game.bestscore_txt + game.score_player.getMaxScore(),
				200, 275);
		game.batch.draw(game.tex_escargot, 200, 20, 312, 198);
		game.batch.draw(retour, 0, 0, 64, 64);
		game.batch.end();

		if (Gdx.input.justTouched()) {
			if(Gdx.input.getX() < 64 && Gdx.input.getY() > Gdx.graphics.getHeight() - 64)
				game.setScreen(game.mainMenuScreen);
			else
				game.setScreen(new GameScreen(game));
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
