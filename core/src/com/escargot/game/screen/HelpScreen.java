package com.escargot.game.screen;

import java.awt.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.escargot.game.EscargotGame;

public class HelpScreen implements Screen {

	final EscargotGame game;
	private OrthographicCamera camera;
	private Button retour;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup table;

	public HelpScreen(final EscargotGame game) {
		this.game = game;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640, 480);
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		table = new HorizontalGroup();
		table.setFillParent(true);
		stage.addActor(table);
		skin = new Skin();
		skin.addRegions(game.manager.get("pack.atlas", TextureAtlas.class));
		retour = new Button(skin.getDrawable("back"));
		retour.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setScreen(game.loadingScreen);
			}
		});
		table.addActor(retour);
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
		game.batch.draw(game.manager.get("background_0", Texture.class), -512, 0, 1920, 1200);
		//game.batch.draw(game.tex_escargot, 300, 64, 161, 100);
		BitmapFont font = game.manager.get("fontVanilla", BitmapFont.class);
		font.draw(game.batch, game.bundle.get("help"), 20, 350, 620, Align.center, true);
		game.batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if (Gdx.input.justTouched()) {
			game.setScreen(game.loadingScreen);
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
		stage.dispose();
		skin.dispose();
	}

}
