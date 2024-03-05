package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.escargot.game.EscargotGame;
import com.escargot.game.RessourcesManager;

public class SimpleScoreScreen implements Screen {

	private OrthographicCamera camera;
	private Button retour;
	private Stage stage;
	private Skin skin;
	private HorizontalGroup table;
	private SpriteBatch batch;
	private Container<Label> bestscore_player;
	private Texture bgd_tex;

	public SimpleScoreScreen() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 960, 540);
		stage = new Stage(new StretchViewport(960, 540)){
			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK || keycode == Input.Keys.LEFT) {
					ScreenManager.getInstance().show(ScreenName.MAIN_MENU);
					return true;
				}
				return false;
			}
		};
		Gdx.input.setInputProcessor(stage);
		table = new HorizontalGroup();
		table.pad(30);
		table.setFillParent(true);
		stage.addActor(table);
		Table t = new Table();
		t.setFillParent(true);
		
		stage.addActor(t);
		LabelStyle vanillaStyle = new LabelStyle(RessourcesManager.getInstance().getFont("vanilla.fnt"), Color.WHITE);
		LabelStyle nashStyle = new LabelStyle(RessourcesManager.getInstance().getFont("nashville.fnt"), Color.WHITE);
		Container<Label> gameover = new Container<Label>(new Label(RessourcesManager.getInstance().getBundle().get("simple_score"), nashStyle));
		Container<Label> bestscore = new Container<Label>(new Label(RessourcesManager.getInstance().getBundle().get("bestscore"), nashStyle));
		vanillaStyle.font.setFixedWidthGlyphs(""+EscargotGame.score_player.getMaxScore());
		bestscore_player = new Container<Label>(new Label(""+ EscargotGame.score_player.getMaxScore(), vanillaStyle));
		gameover.fillX();
		Cell<Container<Label>> c = t.add(gameover);
		c.colspan(2);
		t.row();
		t.add(bestscore);
		t.add(bestscore_player);
		t.pack();
		t.setPosition(480, 350, Align.center);
		
		skin = new Skin();
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		if(prefs.getBoolean("gold", false)){
			skin.addRegions(RessourcesManager.getInstance().getAtlas("pack_gold.atlas"));
		}else {
			skin.addRegions(RessourcesManager.getInstance().getAtlas("pack.atlas"));
		}

		retour = new Button(skin.getDrawable("back"));
		retour.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(EscargotGame.vibre_on)
					Gdx.input.vibrate(50);
				ScreenManager.getInstance().show(ScreenName.MAIN_MENU);
			}
		});

		table.addActor(retour);

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
				ScreenManager.getInstance().show(ScreenName.MAIN_MENU);
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		EscargotGame.myRequestHandler.showAds(true);
		RessourcesManager.getInstance().finishLoad();
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("son_on", EscargotGame.son_on);
		prefs.putBoolean("vibre_on", EscargotGame.vibre_on);
		prefs.putFloat("max_score", EscargotGame.score_player.getMaxScoreValue());
		prefs.flush();
		Gdx.input.setInputProcessor(stage);
		bestscore_player.getActor().setText(""+EscargotGame.score_player.getMaxScore());
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
