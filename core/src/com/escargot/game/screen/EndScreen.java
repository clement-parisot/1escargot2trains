package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.escargot.game.EscargotGame;
import com.escargot.game.Score;

public class EndScreen implements Screen {

	final EscargotGame game;
	private OrthographicCamera camera;
	private Button retour, noter;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup table;

	public EndScreen(final EscargotGame game, final Score score) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		table = new HorizontalGroup();
		table.setFillParent(true);
		stage.addActor(table);
		skin = new Skin();
		skin.add("retour", new Texture(Gdx.files.internal("back.png")));
		skin.add("noter", new Texture(Gdx.files.internal("star.png")));

		retour = new Button(skin.getDrawable("retour"));
		retour.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(game.mainMenuScreen);
			}
		});

		noter = new Button(skin.getDrawable("noter"));
		noter.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.myRequestHandler.rateApp();
			}
		});
		table.addActor(retour);
		table.addActor(noter);

		table.bottom();
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
		game.font.draw(game.batch, game.gameover, 250, 350);
		game.font
				.draw(game.batch, game.score_txt + game.score_player, 200, 300);
		game.font.draw(game.batch,
				game.bestscore_txt + game.score_player.getMaxScore(), 200, 275);
		game.batch.draw(game.tex_escargot, 200, 20, 312, 198);
		game.batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if (Gdx.input.justTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
					0);
			camera.unproject(touchPos);
			if (touchPos.y > 70)
				game.setScreen(new GameScreen(game));
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		game.myRequestHandler.showAds(true);
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", EscargotGame.son_on);
		prefs.putBoolean("vibre_on", EscargotGame.vibre_on);
		prefs.putFloat("max_score", game.score_player.getMaxScoreValue());
		prefs.flush();
		int score = game.score_player.getScore();
		game.myRequestHandler.envoyerScore(score);
		if (score >= 500 && !game.achievementList[0]) {
			game.myRequestHandler.unlock(0);
			game.achievementList[0] = true;
			prefs.putBoolean("a0", true);
		}
		if (score >= 1000 && !game.achievementList[1]) {
			game.myRequestHandler.unlock(1);
			game.achievementList[1] = true;
			prefs.putBoolean("a1", true);
		}
		if (score >= 2000 && !game.achievementList[2]) {
			game.myRequestHandler.unlock(2);
			game.achievementList[2] = true;
			prefs.putBoolean("a2", true);
		}
		if (score >= 3000 && !game.achievementList[3]) {
			game.myRequestHandler.unlock(3);
			game.achievementList[3] = true;
			prefs.putBoolean("a3", true);
		}
		if (score >= 4000 && !game.achievementList[4]) {
			game.myRequestHandler.unlock(4);
			game.achievementList[4] = true;
			prefs.putBoolean("a4", true);
		}
		prefs.flush();
		Gdx.input.setInputProcessor(stage);
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
		stage.dispose();
		skin.dispose();
	}

}
