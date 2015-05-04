 package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.escargot.game.EscargotActor;
import com.escargot.game.EscargotGame;
import com.escargot.game.Fumee;
import com.escargot.game.RailActor;
import com.escargot.game.Score;
import com.escargot.game.TrainActor;

public class MainMenuScreen implements Screen {
	final EscargotGame game;
	Button but_play, but_score, but_quit;
	Button but_son_on, but_son_off, but_aide, but_vibre_on, but_vibre_off;

	OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup main_buttons;
	private EscargotActor escargot;
	private Array<PooledEffect> effects;
	private Fumee fumee;
	private ParticleEffectPool fumeePool;
	private TrainActor trainG;
	private TrainActor trainD;
	private String vanillaString;
	private String nashString;

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
		skin.add("play", game.atlas.createSprite("play"));
		skin.add("score", game.atlas.createSprite("score"));
		skin.add("quit", game.atlas.createSprite("signOut"));
		
		// Parameters
		skin.add("param", game.atlas.createSprite("param"));
		skin.add("son_on", game.atlas.createSprite("son_on"));
		skin.add("son_off", game.atlas.createSprite("son_off"));
		skin.add("vibre_on", game.atlas.createSprite("vibre_on"));
		skin.add("vibre_off", game.atlas.createSprite("vibre_off"));
		
		// Escargot et Trains
		skin.add("escargot", game.atlas.createSprite("escargot"));
		skin.add("train", game.atlas.createSprite("train"));
		skin.add("rails", game.atlas.createSprite("rails"));
		
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
				Gdx.app.exit();
			}
		});
		// Add to stage
		main_buttons.addActor(but_score);
		main_buttons.addActor(but_play);
		main_buttons.addActor(but_quit);
		
		escargot = new EscargotActor(320, 140, 0.16f, 0);
		escargot.setTexture(skin.getSprite("escargot"));
		escargot.addAction(Actions.forever(Actions.sequence(
				Actions.moveToAligned(200.0f,140.0f,Align.bottomLeft,1.0f,Interpolation.sine),
                Actions.scaleTo(-1.0f, 1.0f),
				Actions.moveToAligned(440.0f,140.0f,Align.bottomRight,1.0f,Interpolation.sine), 
				Actions.scaleTo(1.0f, 1.0f))));
		stage.addActor(escargot);
		trainG = new TrainActor(200,140,0.5f,-1,0);
		trainG.setTexture(skin.getSprite("train"));
		stage.addActor(trainG);
		trainD = new TrainActor(440,140,0.5f,1,0);
		trainD.setTexture(skin.getSprite("train"));
		stage.addActor(trainD);
		RailActor rails = new RailActor();
		rails.setTexture(skin.getSprite("rails"));
		stage.addActor(rails);
		stage.act(1);
		
		effects = new Array<PooledEffect>();
		fumee = new Fumee(game);
		fumeePool = new ParticleEffectPool(fumee, 1, 2);
		PooledEffect effect = fumeePool.obtain();
		effect.setPosition(trainG.getFumX(), trainG.getFumY());
		effects.add(effect);
		PooledEffect effect2 = fumeePool.obtain();
		effect2.setPosition(trainD.getFumX(), trainD.getFumY());
		effects.add(effect2);

		main_buttons.pack();
		main_buttons.setPosition(320, 0, Align.bottom+Align.center);
		// Pub
		game.myRequestHandler.showAds(true);
		Table t = new Table();
		
		t.defaults().pad(10);
		stage.addActor(t);
		//t.setFillParent(true);
		LabelStyle vanillaStyle = new LabelStyle(game.fontVanilla, Color.WHITE);
		LabelStyle nashStyle = new LabelStyle(game.fontNashville, Color.WHITE);
		Container<Label> lab_1 = new Container<Label>(new Label("1", vanillaStyle));
		Container<Label> lab_2 = new Container<Label>(new Label("2", vanillaStyle));
		Container<Label> lab_escargot = new Container<Label>(new Label(game.escargot, nashStyle));
		Container<Label> lab_trains = new Container<Label>(new Label(game.trains, nashStyle));
		lab_1.setTransform(true);
		lab_1.addAction(Actions.sequence(Actions.scaleTo(0.5f, 0.0f),Actions.scaleTo(1.00f, 1.00f,0.5f)));
		lab_escargot.setTransform(true);
		lab_escargot.addAction(Actions.sequence(Actions.scaleTo(0.5f, 0.0f),Actions.delay(0.5f),Actions.scaleTo(1.00f, 1.00f,0.5f)));
		lab_2.setTransform(true);
		lab_2.addAction(Actions.sequence(Actions.scaleTo(0.5f, 0.0f),Actions.delay(1f),Actions.scaleTo(1.00f, 1.00f,0.5f)));
		lab_trains.setTransform(true);
		lab_trains.addAction(Actions.sequence(Actions.scaleTo(0.5f, 0.0f),Actions.delay(1.5f),Actions.scaleTo(1.00f, 1.00f,0.5f)));
        t.add(lab_1);
		t.add(lab_escargot);
		//t.row();
		t.add(lab_2);
		t.add(lab_trains);
		t.pack();
		t.setPosition(320, 420, Align.top+Align.center);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(game.bg0, 0, 0, 640, 480);
		// Update and draw effects:
		for (int i = effects.size - 1; i >= 0; i--) {
			PooledEffect effect = effects.get(i);
			effect.draw(game.batch, delta);
		}

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
