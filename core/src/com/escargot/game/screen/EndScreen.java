package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.escargot.game.RessourcesManager;

public class EndScreen implements Screen {

	private OrthographicCamera camera;
	private Button retour, noter;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup table;
	private SpriteBatch batch;
	private Container<Label> score_player;
	private Container<Label> bestscore_player;
	private Texture bgd_tex;

	public EndScreen() {
		batch = new SpriteBatch();
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
		LabelStyle vanillaStyle = new LabelStyle(RessourcesManager.getInstance().getFont("vanilla.fnt"), Color.WHITE);
		LabelStyle nashStyle = new LabelStyle(RessourcesManager.getInstance().getFont("nashville.fnt"), Color.WHITE);
		Container<Label> gameover = new Container<Label>(new Label(RessourcesManager.getInstance().getBundle().get("gameover"), nashStyle));
		Container<Label> score_label = new Container<Label>(new Label(RessourcesManager.getInstance().getBundle().get("score"), nashStyle));
		vanillaStyle.font.setFixedWidthGlyphs(""+EscargotGame.score_player.toString());
		score_player = new Container<Label>(new Label(""+EscargotGame.score_player, vanillaStyle));
		Container<Label> bestscore = new Container<Label>(new Label(RessourcesManager.getInstance().getBundle().get("bestscore"), nashStyle));
		vanillaStyle.font.setFixedWidthGlyphs(""+EscargotGame.score_player.getMaxScore());
		bestscore_player = new Container<Label>(new Label(""+ EscargotGame.score_player.getMaxScore(), vanillaStyle));
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
		t.setPosition(480, 350, Align.center);
		
		skin = new Skin();
		skin.addRegions(RessourcesManager.getInstance().getAtlas("pack.atlas"));

		retour = new Button(skin.getDrawable("back"));
		retour.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
				ScreenManager.getInstance().show(ScreenName.MAIN_MENU);
			}
		});

		noter = new Button(skin.getDrawable("star"));
		noter.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
			}
		});
		table.addActor(retour);
		table.addActor(noter);

		table.bottom();
		bgd_tex = RessourcesManager.getInstance().getTexture("background_0.jpg");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		this.batch.setProjectionMatrix(camera.combined);

		this.batch.begin();
		this.batch.draw(bgd_tex, 1000, -220, -1920, 1200);
		this.batch.draw(skin.getRegion("escargot_end"), 224, 20);
		this.batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if (Gdx.input.justTouched()) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(),
					0);
			camera.unproject(touchPos);
			if (touchPos.y > 70)
				ScreenManager.getInstance().show(ScreenName.GAME);
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		RessourcesManager.getInstance().finishLoad();
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", EscargotGame.son_on);
		prefs.putBoolean("vibre_on", EscargotGame.vibre_on);
		prefs.putFloat("max_score", EscargotGame.score_player.getMaxScoreValue());
		prefs.flush();
		int scoreNb = EscargotGame.score_player.getScore();
		//EscargotGame.myRequestHandler.envoyerScore(scoreNb);
		if (scoreNb >= 500 && !EscargotGame.achievementList[0]) {
			//EscargotGame.myRequestHandler.unlock(0);
			EscargotGame.achievementList[0] = true;
			prefs.putBoolean("a0", true);
		}
		if (scoreNb >= 1000 && !EscargotGame.achievementList[1]) {
			//EscargotGame.myRequestHandler.unlock(1);
			EscargotGame.achievementList[1] = true;
			prefs.putBoolean("a1", true);
		}
		if (scoreNb >= 2000 && !EscargotGame.achievementList[2]) {
			//EscargotGame.myRequestHandler.unlock(2);
			EscargotGame.achievementList[2] = true;
			prefs.putBoolean("a2", true);
		}
		if (scoreNb >= 3000 && !EscargotGame.achievementList[3]) {
			//EscargotGame.myRequestHandler.unlock(3);
			EscargotGame.achievementList[3] = true;
			prefs.putBoolean("a3", true);
		}
		if (scoreNb >= 4000 && !EscargotGame.achievementList[4]) {
			//EscargotGame.myRequestHandler.unlock(4);
			EscargotGame.achievementList[4] = true;
			prefs.putBoolean("a4", true);
		}
		prefs.flush();
		Gdx.input.setInputProcessor(stage);
		score_player.getActor().setText(""+EscargotGame.score_player);
		bestscore_player.getActor().setText(""+EscargotGame.score_player.getMaxScore());
		if(EscargotGame.playTime > 3){
			//EscargotGame.myRequestHandler.show_inter_ads();
			EscargotGame.playTime = 0;
			System.out.println("show");
		}
		System.out.println(EscargotGame.playTime);
		EscargotGame.playTime +=1;
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		RessourcesManager.getInstance().finishLoad();
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		batch.dispose();
	}

}
