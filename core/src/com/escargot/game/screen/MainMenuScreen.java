package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.escargot.game.EscargotGame;
import com.escargot.game.Score;

public class MainMenuScreen implements Screen {
	final EscargotGame game;
	Button but_son_on, but_son_off, but_aide, but_vibre_on, but_vibre_off,
			but_score;

	OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup table;

	public MainMenuScreen(final EscargotGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		stage = new Stage();
		table = new HorizontalGroup();
		table.setFillParent(true);
		table.align(Align.bottom + Align.right);
		stage.addActor(table);

		skin = new Skin();
		skin.add("son_on", new Texture(Gdx.files.internal("son_on.png")));
		skin.add("son_off", new Texture(Gdx.files.internal("son_off.png")));
		skin.add("vibre_on", new Texture(Gdx.files.internal("vibre_on.png")));
		skin.add("vibre_off", new Texture(Gdx.files.internal("vibre_off.png")));
		skin.add("aide", new Texture(Gdx.files.internal("aide.png")));
		skin.add("score", new Texture(Gdx.files.internal("score.png")));

		// Button
		but_son_on = new Button(skin.getDrawable("son_on"));
		but_son_on.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				EscargotGame.son_on = false;
			}
		});
		but_son_off = new Button(skin.getDrawable("son_off"));
		but_son_off.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				EscargotGame.son_on = true;
			}
		});
		but_vibre_on = new Button(skin.getDrawable("vibre_on"));
		but_vibre_on.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				EscargotGame.vibre_on = false;
			}
		});
		but_vibre_off = new Button(skin.getDrawable("vibre_off"));
		but_vibre_off.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				EscargotGame.vibre_on = true;
				Gdx.input.vibrate(1000);
			}
		});
		but_aide = new Button(skin.getDrawable("aide"));
		but_aide.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(game.helpScreen);
			}
		});
		but_score = new Button(skin.getDrawable("score"));
		but_score.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new ScoreScreen(game));
			}
		});
		// Add to stage
		table.addActor(but_son_on);
		table.addActor(but_son_off);
		table.addActor(but_vibre_on);
		table.addActor(but_vibre_off);
		table.addActor(but_score);
		table.addActor(but_aide);
		// Pub
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
		game.font.drawWrapped(game.batch, game.mainscreen1, 120, 300, 400,
				HAlignment.CENTER);
		game.font.drawWrapped(game.batch, game.mainscreen2, 120, 250, 400,
				HAlignment.CENTER);
		game.batch.draw(game.tex_escargot, 300, 64, 161, 100);
		game.batch.end();
		but_son_on.setVisible(EscargotGame.son_on);
		but_son_off.setVisible(!EscargotGame.son_on);
		but_vibre_on.setVisible(EscargotGame.vibre_on);
		but_vibre_off.setVisible(!EscargotGame.vibre_on);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		if (Gdx.input.justTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
					0);
			camera.unproject(touchPos);
			if (touchPos.y > 70) {
				Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
				if (prefs.getBoolean("firstGame", true)) {
					prefs.putBoolean("firstGame", false);
					prefs.flush();
					game.setScreen(new TutorialScreen(game, new Score()));
				} else {
					game.setScreen(new GameScreen(game));
				}
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		game.myRequestHandler.showAds(true);
		Gdx.input.setInputProcessor(stage);
		but_son_on.setVisible(EscargotGame.son_on);
		but_son_off.setVisible(!EscargotGame.son_on);
		but_vibre_on.setVisible(EscargotGame.vibre_on);
		but_vibre_off.setVisible(!EscargotGame.vibre_on);
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
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", EscargotGame.son_on);
		prefs.putBoolean("vibre_on", EscargotGame.vibre_on);
		prefs.flush();
		stage.dispose();
		skin.dispose();
	}
}
