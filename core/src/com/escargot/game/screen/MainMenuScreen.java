 package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
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
import com.escargot.game.EscargotGame;
import com.escargot.game.Fumee;
import com.escargot.game.RessourcesManager;
import com.escargot.game.TrainActor;
import com.escargot.game.tuto.EscargotActorMenu;

public class MainMenuScreen implements Screen {
	Button but_play, but_score, but_quit;
	Button but_son, but_aide, but_vibre, but_param;

	OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup main_buttons;
	private EscargotActorMenu escargot;
	private Array<PooledEffect> effects;
	private Fumee fumee;
	private ParticleEffectPool fumeePool;
	private TrainActor trainG;
	private TrainActor trainD;
	private Sprite spriteRails;
	private Animation animation;
	private HorizontalGroup menuBar;
	private Batch batch;
	private Texture bgd_tex;

	public MainMenuScreen() {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 960, 540);
		stage = new Stage(new StretchViewport(960, 540));
		main_buttons = new HorizontalGroup();
		main_buttons.align(Align.center);
		menuBar = new HorizontalGroup();
		menuBar.align(Align.bottomRight);
		stage.addActor(menuBar);
		stage.addActor(main_buttons);

		skin = new Skin();
		// Main buttons
		TextureAtlas atlas = RessourcesManager.getInstance().getAtlas("pack.atlas");
		skin.addRegions(atlas);
		
		// Button
		but_son = new Button(skin.getDrawable("son_on"),skin.getDrawable("son_off"),skin.getDrawable("son_off"));
		but_son.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				EscargotGame.son_on = !but_son.isChecked();
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
			}
		});
		but_vibre = new Button(skin.getDrawable("vibre_on"),skin.getDrawable("vibre_off"),skin.getDrawable("vibre_off"));
		but_vibre.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				EscargotGame.vibre_on = !but_vibre.isChecked();
				if(!but_vibre.isChecked())
					Gdx.input.vibrate(500);
			}
		});
		but_play = new Button(skin.getDrawable("play"));
		but_play.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
				Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
				if (prefs.getBoolean("firstGame", true)) {
					prefs.putBoolean("firstGame", false);
					prefs.flush();
				}
				ScreenManager.getInstance().show(ScreenName.GAME);
			}
		});
		but_score = new Button(skin.getDrawable("score"));
		but_score.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
				ScreenManager.getInstance().show(ScreenName.SCORE);
			}
		});
		but_quit = new Button(skin.getDrawable("signOut"));
		but_quit.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
				Gdx.app.exit();
			}
		});
		but_param = new Button(skin.getDrawable("param"));
		but_param.setTransform(true);
		but_param.setOrigin(Align.center);
		but_param.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
				but_vibre.setVisible(but_param.isChecked());
				but_son.setVisible(but_param.isChecked());
				but_param.addAction(Actions.sequence(Actions.rotateBy(180f, 0.5f)));
			}
		});
		but_aide = new Button(skin.getDrawable("aide"));
		but_aide.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
					ScreenManager.getInstance().show(ScreenName.TUTO);
			}
		});
		but_aide.setPosition(960, 0, Align.bottomRight);
		stage.addActor(but_aide);
		// Add to stage
		main_buttons.addActor(but_score);
		main_buttons.addActor(but_play);
		main_buttons.addActor(but_quit);
		menuBar.addActor(but_param);
		menuBar.addActor(but_son);
		menuBar.addActor(but_vibre);
		menuBar.pack();
		
		escargot = new EscargotActorMenu(480, 145, 0.16f, 1);
		animation = new Animation(1/15f, atlas.findRegions("escargot"));
		escargot.setTexture(animation);
		escargot.addAction(Actions.forever(Actions.sequence(
				Actions.moveToAligned(280.0f,145.0f,Align.bottomLeft,1.6f,Interpolation.sine),
                Actions.scaleTo(-1f, 1f),
				Actions.moveToAligned(680.0f,145.0f,Align.bottomRight,1.6f,Interpolation.sine), 
				Actions.scaleTo(1f, 1f))));
		stage.addActor(escargot);
		trainG = new TrainActor(280,145,0.5f,-1,0);
		trainG.setTexture(atlas.findRegion("train"));
		stage.addActor(trainG);
		trainD = new TrainActor(680,145,0.5f,1,0);
		trainD.setTexture(atlas.findRegion("train"));
		stage.addActor(trainD);
		Texture railsTexture = new Texture(Gdx.files.internal("rails_0.png"));
		railsTexture.setWrap(TextureWrap.MirroredRepeat,TextureWrap.ClampToEdge);
		spriteRails = new Sprite(railsTexture, 4096, 88);
		stage.act(1);
		
		effects = new Array<PooledEffect>();
		fumee = new Fumee();
		fumeePool = new ParticleEffectPool(fumee, 1, 2);
		PooledEffect effect = fumeePool.obtain();
		effect.setPosition(trainG.getFumX(), trainG.getFumY());
		effects.add(effect);
		PooledEffect effect2 = fumeePool.obtain();
		effect2.setPosition(trainD.getFumX(), trainD.getFumY());
		effects.add(effect2);

		main_buttons.pack();
		main_buttons.setPosition(480, 0, Align.bottom+Align.center);
		Table t = new Table();
		
		t.defaults().pad(10);
		stage.addActor(t);
		//t.setFillParent(true);
		LabelStyle vanillaStyle = new LabelStyle(RessourcesManager.getInstance().getFont("vanilla.fnt"), Color.WHITE);
		LabelStyle nashStyle = new LabelStyle(RessourcesManager.getInstance().getFont("nashville.fnt"), Color.WHITE);
		Container<Label> lab_1 = new Container<Label>(new Label("1", vanillaStyle));
		Container<Label> lab_2 = new Container<Label>(new Label("2", vanillaStyle));
		Container<Label> lab_escargot = new Container<Label>(new Label(RessourcesManager.getInstance().getBundle().get("snail"), nashStyle));
		Container<Label> lab_trains = new Container<Label>(new Label(RessourcesManager.getInstance().getBundle().get("trains"), nashStyle));
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
		t.setPosition(480, 500, Align.top+Align.center);
		main_buttons.addAction(Actions.sequence(Actions.alpha(0),Actions.delay(2f),Actions.alpha(1.00f,0.5f)));
		menuBar.addAction(Actions.sequence(Actions.alpha(0),Actions.delay(2f),Actions.alpha(1.00f,0.5f)));
		but_aide.addAction(Actions.sequence(Actions.alpha(0),Actions.delay(2f),Actions.alpha(1.00f,0.5f)));
		bgd_tex = RessourcesManager.getInstance().getTexture("background_0.jpg");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		this.batch.setProjectionMatrix(camera.combined);

		this.batch.begin();
		this.batch.draw(bgd_tex, 0, 0, 960, 540);


		this.batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		this.batch.begin();
		this.batch.enableBlending();
		this.batch.draw(spriteRails, 0, 115, 960, 32);
		// Update and draw effects:
		for (int i = effects.size - 1; i >= 0; i--) {
			PooledEffect effect = effects.get(i);
			effect.draw(this.batch, delta);
		}
		this.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		but_son.setVisible(false);
		but_vibre.setVisible(false);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		ScreenManager.getInstance().dispose(ScreenName.MAIN_MENU);
		ScreenManager.getInstance().show(ScreenName.LOADING);
	}

	@Override
	public void dispose() {
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", EscargotGame.son_on);
		prefs.putBoolean("vibre_on", EscargotGame.vibre_on);
		prefs.flush();
		fumee.dispose();
		stage.dispose();
		skin.dispose();
		batch.dispose();
	}
}
