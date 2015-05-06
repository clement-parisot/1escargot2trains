package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.escargot.game.EscargotGame;

public class ScoreScreen implements Screen {

	final EscargotGame game;
	private OrthographicCamera camera;
	private Button retour, sign_in, sign_out, achiev, rank;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup table;
	private boolean isSignedIn;

	public ScoreScreen(final EscargotGame game) {
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
		skin.add("signIn", new Texture(Gdx.files.internal("signIn.png")));
		skin.add("signOut", new Texture(Gdx.files.internal("signOut.png")));
		skin.add("achiev", new Texture(Gdx.files.internal("achiev.png")));
		skin.add("rank", new Texture(Gdx.files.internal("leader.png")));

		// Button
		retour = new Button(skin.getDrawable("retour"));
		retour.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(game.loadingScreen);
				dispose();
			}
		});
		// Button
		sign_in = new Button(skin.getDrawable("signIn"));
		sign_in.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.myRequestHandler.beginUserSignIn();
			}
		});
		sign_out = new Button(skin.getDrawable("signOut"));
		sign_out.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (game.myRequestHandler.isConnected()) {
					game.myRequestHandler.signOutUser();
				}
			}
		});
		// Button
		achiev = new Button(skin.getDrawable("achiev"));
		achiev.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.myRequestHandler.showAchievments();
			}
		});
		// Button
		rank = new Button(skin.getDrawable("rank"));
		rank.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.myRequestHandler.classement();
			}
		});
		
		table.addActor(retour);
		table.addActor(sign_in);
		table.addActor(sign_out);
		table.addActor(achiev);
		table.addActor(rank);
		
		table.bottom();
	}

	@Override
	public void render(float delta) {
		isSignedIn = game.myRequestHandler.isSignedIn();
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		game.batch.draw(game.manager.get("background_0", Texture.class), -512, 0, 1920, 1200);
		if (!isSignedIn) {
			BitmapFont font = game.manager.get("fontVanilla", BitmapFont.class);
			font.draw(game.batch,
					"Please sign in to Google Play Game", 160, 240, 320,
					Align.center, true);
			sign_in.setVisible(true);
			sign_out.setVisible(false);
		}else{
			sign_in.setVisible(false);
			sign_out.setVisible(true);
		}
		if (!game.myRequestHandler.isConnected()) {
			rank.setVisible(false);
			achiev.setVisible(false);
		} else {
			rank.setVisible(true);
			achiev.setVisible(true);
		}
		game.batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		game.myRequestHandler.showAds(true);
		Gdx.input.setInputProcessor(stage);
		if (!game.myRequestHandler.isConnected()) {
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
		skin.dispose();
		stage.dispose();
	}

}
