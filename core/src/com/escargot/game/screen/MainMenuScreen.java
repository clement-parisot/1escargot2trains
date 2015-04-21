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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.escargot.game.EscargotActor;
import com.escargot.game.EscargotGame;
import com.escargot.game.Score;

public class MainMenuScreen implements Screen {
	final EscargotGame game;
	Button but_play, but_score, but_quit;
	Button but_son_on, but_son_off, but_aide, but_vibre_on, but_vibre_off;

	OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup main_buttons;

	public MainMenuScreen(final EscargotGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		stage = new Stage(new StretchViewport(640, 480));
		main_buttons = new HorizontalGroup();
		main_buttons.align(Align.center);
		
		stage.addActor(main_buttons);

		skin = new Skin();
		// Main buttons
		skin.add("play", new Texture(Gdx.files.internal("play.png")));
		skin.add("score", new Texture(Gdx.files.internal("score.png")));
		skin.add("quit", new Texture(Gdx.files.internal("signOut.png")));
		
		// Parameters
		skin.add("param", new Texture(Gdx.files.internal("param.png")));
		skin.add("son_on", new Texture(Gdx.files.internal("son_on.png")));
		skin.add("son_off", new Texture(Gdx.files.internal("son_off.png")));
		skin.add("vibre_on", new Texture(Gdx.files.internal("vibre_on.png")));
		skin.add("vibre_off", new Texture(Gdx.files.internal("vibre_off.png")));
		
		// Escargot et Trains
		skin.add("escargot", new Texture(Gdx.files.internal("escargot_0.png")));
		skin.add("train", new Texture(Gdx.files.internal("train_0.png")));
		
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
		but_play = new Button(skin.getDrawable("play"));
		but_play.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new GameScreen(game));
			}
		});
		but_score = new Button(skin.getDrawable("score"));
		but_score.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(new ScoreScreen(game));
			}
		});
		but_quit = new Button(skin.getDrawable("quit"));
		but_quit.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.dispose();
			}
		});
		// Add to stage
		main_buttons.addActor(but_score);
		main_buttons.addActor(but_play);
		main_buttons.addActor(but_quit);
		stage.addActor(new EscargotActor());

		main_buttons.pack();
		main_buttons.setPosition((Gdx.graphics.getWidth() - main_buttons.getMinWidth()) / 2, 0);
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
		game.batch.draw(game.bg0, 0, 0, 640, 480);
		game.font.draw(game.batch, game.mainscreen1, 120, 300, 400,	Align.center, true);
		game.font.draw(game.batch, game.mainscreen2, 120, 250, 400, Align.center, true);
		//game.batch.draw(game.tex_escargot, 300, 64, 161, 100);
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
