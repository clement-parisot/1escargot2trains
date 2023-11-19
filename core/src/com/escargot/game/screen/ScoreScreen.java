package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.escargot.game.EscargotGame;
import com.escargot.game.RessourcesManager;
import com.escargot.game.tuto.TrainActorTuto;

public class ScoreScreen implements Screen {

	private OrthographicCamera camera;
	private Button retour, sign_in, sign_out, achiev, rank;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup table;
	private boolean isSignedIn;
	private Sprite spriteRails;
	private TrainActorTuto train;
	private SpriteBatch batch;
	private Texture bgdTexture;
	private BitmapFont fontVani;

	public ScoreScreen() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 960, 540);
		stage = new Stage(new StretchViewport(960, 540));
		Gdx.input.setInputProcessor(stage);
		table = new HorizontalGroup();
		table.setFillParent(true);
		stage.addActor(table);


		skin = new Skin();
		TextureAtlas atlas = RessourcesManager.getInstance().getAtlas("pack.atlas");
		skin.addRegions(atlas);

		// Button
		retour = new Button(skin.getDrawable("back"));
		retour.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
				ScreenManager.getInstance().show(ScreenName.MAIN_MENU);
			}
		});
		// Button
		sign_in = new Button(skin.getDrawable("signIn"));
		sign_in.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
			}
		});
		sign_out = new Button(skin.getDrawable("signOut"));
		sign_out.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
			}
		});
		// Button
		achiev = new Button(skin.getDrawable("achiev"));
		achiev.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
			}
		});
		// Button
		rank = new Button(skin.getDrawable("leader"));
		rank.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
			}
		});
		
		table.addActor(retour);
		table.addActor(sign_in);
		table.addActor(sign_out);
		table.addActor(achiev);
		table.addActor(rank);
		
		table.bottom();
		train = new TrainActorTuto(600, 150, 1f,-1,0);
		train.setTexture(atlas.findRegion("train"));
		stage.addActor(train);
		Texture railsTexture = new Texture(Gdx.files.internal("rails_0.png"));
		railsTexture.setWrap(TextureWrap.MirroredRepeat,TextureWrap.ClampToEdge);
		spriteRails = new Sprite(railsTexture, 4096, 88);
		bgdTexture = RessourcesManager.getInstance().getTexture("background_0.jpg");
		fontVani = RessourcesManager.getInstance().getFont("vanilla.fnt");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		this.batch.setProjectionMatrix(camera.combined);

		this.batch.begin();
		this.batch.draw(bgdTexture, -512, 0, 1920, 1200);
		if (!isSignedIn) {
			fontVani.draw(this.batch,
					"Please sign in to Google Play Game", 0, 480, 960,
					Align.center, true);
			sign_in.setVisible(true);
			sign_out.setVisible(false);
		}else{
			sign_in.setVisible(false);
			sign_out.setVisible(true);
		}
		if (!false) {
			rank.setVisible(false);
			achiev.setVisible(false);
		} else {
			rank.setVisible(true);
			achiev.setVisible(true);
		}

		this.batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		this.batch.begin();
		this.batch.draw(spriteRails, 0, 118, 1920, 32);
		this.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		RessourcesManager.getInstance().finishLoad();
		Gdx.input.setInputProcessor(stage);
		if (!false) {
			sign_in.setVisible(true);
			sign_out.setVisible(false);
			rank.setVisible(false);
			achiev.setVisible(false);
		} else {
			sign_in.setVisible(false);
			sign_out.setVisible(true);
			rank.setVisible(true);
			achiev.setVisible(true);
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		ScreenManager.getInstance().show(ScreenName.LOADING);
	}

	@Override
	public void dispose() {
		skin.dispose();
		stage.dispose();
		batch.dispose();
	}

}
