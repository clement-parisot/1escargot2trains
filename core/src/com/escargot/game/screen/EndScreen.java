package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.escargot.game.EscargotGame;

public class EndScreen implements Screen {

	final EscargotGame game;
	private OrthographicCamera camera;
	private Button retour, noter;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup table;

	public EndScreen(final EscargotGame game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 960, 540);
		stage = new Stage(new StretchViewport(960, 540));
		Gdx.input.setInputProcessor(stage);
		table = new HorizontalGroup();
		table.setFillParent(true);
		stage.addActor(table);
		Table t = new Table();
		t.setFillParent(true);
		
		stage.addActor(t);
		LabelStyle vanillaStyle = new LabelStyle(game.manager.get("vanilla.fnt",BitmapFont.class), Color.WHITE);
		LabelStyle nashStyle = new LabelStyle(game.manager.get("nashville.fnt",BitmapFont.class), Color.WHITE);
		Container<Label> gameover = new Container<Label>(new Label(game.bundle.get("gameover"), nashStyle));
		Container<Label> score_label = new Container<Label>(new Label(game.bundle.get("score"), nashStyle));
		vanillaStyle.font.setFixedWidthGlyphs(""+game.score_player.toString());
		Container<Label> score_player = new Container<Label>(new Label(""+game.score_player, vanillaStyle));
		Container<Label> bestscore = new Container<Label>(new Label(game.bundle.get("bestscore"), nashStyle));
		vanillaStyle.font.setFixedWidthGlyphs(""+game.score_player.getMaxScore());
		Container<Label> bestscore_player = new Container<Label>(new Label(""+ game.score_player.getMaxScore(), vanillaStyle));
		gameover.fillX();
		Cell<Container<Label>> c = t.add(gameover);
		c.colspan(2);
		t.row();
		t.add(score_label);
		t.add(score_player);
		t.row();
		t.add(bestscore);
		t.add(bestscore_player);
		t.pack();
		t.setPosition(480, 400, Align.center);
		
		skin = new Skin();
		skin.addRegions(game.manager.get("pack.atlas", TextureAtlas.class));

		retour = new Button(skin.getDrawable("back"));
		retour.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
				game.setScreen(game.loadingScreen);
			}
		});

		noter = new Button(skin.getDrawable("star"));
		noter.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
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
		game.batch.draw(game.manager.get("background_0.jpg", Texture.class), 1000, -220, -1920, 1200);
		game.batch.draw(skin.getRegion("escargot_end"), 224, 20);
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
