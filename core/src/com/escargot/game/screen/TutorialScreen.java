 package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.escargot.game.EscargotActor;
import com.escargot.game.EscargotGame;
import com.escargot.game.Fumee;
import com.escargot.game.RailActor;
import com.escargot.game.Score;
import com.escargot.game.TrainActor;
import com.escargot.game.screen.GameScreen.MyInputProcessorKey;
import com.escargot.game.screen.GameScreen.MyInputProcessorTouch;
import com.escargot.game.tuto.EscargotActorTuto;
import com.escargot.game.tuto.RailActorTuto;
import com.escargot.game.tuto.TrainActorTuto;

public class TutorialScreen implements Screen {
	final EscargotGame game;

	OrthographicCamera cameraGame;
	private Stage stage;
	private Skin skin;
	private EscargotActorTuto escargot;
	private Array<PooledEffect> effects;
	private Fumee fumee;
	private ParticleEffectPool fumeePool;
	private TrainActorTuto trainG;
	private TrainActorTuto trainD;
	private OrthographicCamera cameraScore;
	private Music music_bg;
	private Sound sound_train;
	private Sound sound_mort;
	private float zoom_factor;

	private Sprite spriteBgd;

	private float scrollTimer=0.0f;

	private Texture spriteRails;

	private Score score_game;
	
	// Animation steps
	private boolean step0_displayText = true;
	private boolean step1_displayRight = false;
	private boolean step1_5_wait = false;
	private boolean step2_displayScore = false;
	private boolean step2_5_wait = false;
	private boolean step3_displayLeft = false;
	private boolean step4_displaySante = false;
	private float elapsedTime = 0;

	public boolean faster;

	private Animation animation;

	public TutorialScreen(final EscargotGame game, Score score) {
		this.game = game;
		stage = new Stage(new StretchViewport(1920, 1200)){
			@Override
			public boolean keyDown(int keycode) {
				if(step0_displayText){
					game.resume();
					step0_displayText = false;
				}
				if(step2_displayScore){
					game.resume();
					step2_5_wait = true;
					step2_displayScore = false;
				}
				if(step3_displayLeft){
					game.resume();
					step4_displaySante = true;
					step3_displayLeft = false;
				}
				if (keycode == Keys.LEFT) {
					faster = true;
					escargot.setVitesse(1.2);
					escargot.flip(0);
					return true;
				}
				if (keycode == Keys.RIGHT) {
					faster = true;
					escargot.setVitesse(1.2);
					escargot.flip(640);
					if(step1_displayRight){
						game.resume();
						step1_5_wait = true;
						step1_displayRight = false;
					}
					return true;
				}
				return true;
			}

			@Override
			public boolean keyUp(int keycode) {
				faster = false;
				escargot.setVitesse(0.6);
				return true;
			}
		};
		stage.setDebugAll(true);
		score_game = new Score();

		
		// Tout l'ecran de jeu (1920/1200)
		cameraGame = (OrthographicCamera) stage.getCamera();
		cameraGame.setToOrtho(false, 1920, 1200);
		cameraScore = new OrthographicCamera();
		cameraScore.setToOrtho(false, 640, 480);
		
		// Sons
		music_bg = Gdx.audio.newMusic(Gdx.files.internal("sound0.mp3"));
		sound_train = Gdx.audio.newSound(Gdx.files.internal("sound2.wav"));
		sound_mort = Gdx.audio.newSound(Gdx.files.internal("sound1.wav"));

		// Musique en boucle
		music_bg.setLooping(true);

		skin = new Skin();
		// Load textures
		skin.addRegions(game.manager.get("pack.atlas", TextureAtlas.class));
		TextureAtlas atlas =  game.manager.get("pack.atlas", TextureAtlas.class);
		escargot = new EscargotActorTuto(0, 145, 0.16f, 0.6);
		escargot.setPosition(0, 147, Align.bottomLeft);
		animation = new Animation(1/15f, atlas.findRegions("escargot"));
		escargot.setTexture(animation);
		stage.addActor(escargot);
		trainG = new TrainActorTuto(0, 150, 0.5f,-1,0.4);
		trainG.setTexture(skin.getSprite("train"));
		stage.addActor(trainG);
		trainD = new TrainActorTuto(1920, 150,0.5f,1,0.4);
		trainD.setTexture(skin.getSprite("train"));
		stage.addActor(trainD);
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
		stage.addListener(new ActorGestureListener(20.0f,0.4f,0.3f,0.15f){

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				if(step1_displayRight){
					step1_5_wait = true;
					step1_displayRight = false;
					game.resume();
				}
				if (!step1_displayRight && !step1_5_wait && !step2_displayScore && !step2_5_wait && ! step3_displayLeft && !step4_displaySante)
					return true;
				escargot.setVitesse(1.2);
				faster = true;
				return false;
			}
			
			@Override
			public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(step0_displayText){
					game.resume();
					step0_displayText = false;
				}
				if(step2_displayScore){
					game.resume();
					step2_5_wait = true;
					step2_displayScore = false;
				}
				if(step3_displayLeft){
					game.resume();
					step4_displaySante = true;
					step3_displayLeft = false;
				}
				if (!step1_displayRight && !step1_5_wait && !step2_displayScore && !step2_5_wait && !step3_displayLeft && !step4_displaySante)
					return;
				x = stage.stageToScreenCoordinates(new Vector2(x, y)).x;
				if (step3_displayLeft || step4_displaySante)
					escargot.flip((int) x);
				escargot.setVitesse(1.2);
				faster = true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				faster = false;
				escargot.setVitesse(0.6);
			}
		});
		
		// Pub
		game.myRequestHandler.showAds(false);
		Texture spriteTexture = game.manager.get("background_0.jpg",Texture.class);
		spriteTexture.setWrap(TextureWrap.MirroredRepeat,TextureWrap.ClampToEdge);
		spriteBgd = new Sprite(spriteTexture, 0, 0, 2048, 1024);
		spriteBgd.setSize(1920, 1200);
		spriteRails = game.manager.get("rails_0.png",Texture.class);
		spriteRails.setWrap(TextureWrap.MirroredRepeat,TextureWrap.ClampToEdge);
	}
	
	private void camera_zoom(float dist, Vector2 posEscargot) {
		
		float pas = Interpolation.linear.apply(dist);
		zoom_factor = Math.max(0.2f, Math.min(pas, 1.0f));
		cameraGame.viewportHeight = 1200 * zoom_factor;
		cameraGame.position.y = Math.max(cameraGame.viewportHeight / 2, posEscargot.y);
		cameraGame.viewportWidth = 1920 * zoom_factor;
		cameraGame.position.x = Math.min(
				Math.max(posEscargot.x, cameraGame.viewportWidth / 2),
				1920 - cameraGame.viewportWidth / 2);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		elapsedTime += Gdx.graphics.getDeltaTime();

		float distEG = escargot.getX(Align.left);
		float distED = escargot.getX(Align.right);
		float distTG = trainG.getX(Align.left);
		float distTD = trainD.getX(Align.left);
		float distg = distEG - distTG;
		float distd = distTD - distED;
		float dist = 2 * Math.min(Math.abs(distg), Math.abs(distd))/ (distTD-distTG-escargot.getWidth());
		camera_zoom(Math.min(Math.abs(distg), Math.abs(distd))/700, new Vector2(escargot.getX(Align.center), escargot.getY(Align.center)));
		cameraGame.update();
		cameraScore.update();
		game.batch.begin();
		game.batch.setProjectionMatrix(cameraGame.combined);
		//game.batch.draw(game.manager.get("background_0.jpg",Texture.class), 0, 0, 640, 480);
		scrollTimer+=Gdx.graphics.getDeltaTime()*0.1f;
	     if(scrollTimer>2.0f)
	         scrollTimer = 0.0f;
	                
//	     spriteBgd.setU(scrollTimer);
//	     spriteBgd.setU2(scrollTimer+1);
//	     spriteRails.setU(scrollTimer);
//	     spriteRails.setU2(scrollTimer+1);
		game.batch.draw(spriteBgd, 0, 0, 1920, 1200);
		game.batch.draw(spriteRails, 0, 118, 0, 0, 1920, 32);
		// Update and draw effects:
		effects.get(0).setPosition(trainG.getFumX(), trainG.getFumY());
		effects.get(1).setPosition(trainD.getFumX(), trainD.getFumY());
		game.batch.end();
		if(!game.pause){
			stage.act(Gdx.graphics.getDeltaTime());
			score_update(dist, delta);
		}
		stage.draw();
		if(!step1_displayRight && dist > 0.05 && !step1_5_wait && !step2_displayScore && !step2_5_wait && ! step3_displayLeft && !step4_displaySante){
			step1_displayRight = true;
			game.pause();
		}
		
		game.sr.setProjectionMatrix(cameraScore.combined);
		game.sr.begin(ShapeType.Filled);
		if(step1_displayRight || (faster && escargot.getDirection() == 1)){
			game.sr.rect(640, 0, -64, 480, new Color(0, 1, 0, 0.5f), new Color(0, 1, 0, 0f), new Color(0, 1, 0, 0), new Color(0, 1, 0, 0.5f));
		}
		if(step2_displayScore|| step3_displayLeft || step4_displaySante){
		}
		if(step3_displayLeft || ((step3_displayLeft  || step4_displaySante) && faster && escargot.getDirection() == -1)){
			game.sr.rect(0, 0, 64, 480, new Color(0, 1, 0, 0.5f), new Color(0, 1, 0, 0f), new Color(0, 1, 0, 0), new Color(0, 1, 0, 0.5f));
		}
		if(step4_displaySante){
			game.sr.rect(160, 0, 320, 30, Color.RED, Color.GREEN, Color.GREEN, Color.RED);
			game.sr.setColor(0,0,0,1);
			game.sr.rect(480, 0, Math.min(Math.max(-1*Math.round((dist)*320/32)*32, -320), 0), 30);
			game.sr.end();
			game.sr.begin(ShapeType.Line);
			game.sr.setColor(0,0,0,1);
			for(int i = 0; i <= 320; i+=32){
				game.sr.rect(160, 0, i, 30);
			}
		}
		game.sr.end();
		game.batch.begin();
		for (int i = effects.size - 1; i >= 0; i--) {
			PooledEffect effect = effects.get(i);
			if(!game.pause)
				effect.draw(game.batch, delta);
			else
				effect.draw(game.batch);
		}
		game.batch.setProjectionMatrix(cameraScore.combined);
		// Step0
		if(step0_displayText){
			BitmapFont font = game.manager.get("nashville.fnt", BitmapFont.class);
			font.draw(game.batch, game.bundle.get("help"), 0, 480, 640, Align.left, true);
			game.pause();
		}
		if(step1_displayRight || step1_5_wait || step2_displayScore || step2_5_wait || step3_displayLeft || step4_displaySante){
			if(faster && escargot.getDirection() == 1)
				game.batch.draw(skin.getSprite("fastRight"), 576, 240);
			else
				game.batch.draw(skin.getSprite("right"), 576, 240);
		}
		if(step1_5_wait && distd < 1000){
			game.pause();
			step2_displayScore = true;
			step1_5_wait = false;
		}
		if(step2_displayScore|| step2_5_wait || step3_displayLeft || step4_displaySante){
			BitmapFont font = game.manager.get("vanilla.fnt", BitmapFont.class);
			font.setFixedWidthGlyphs(score_game.toString());
			font.draw(game.batch, score_game.toString(), 160, 475, 320, Align.center, false);
			font.draw(game.batch, "+10", 160, 415, 320, Align.center, false);
		}
		if(step2_5_wait && distd < distg){
			game.pause();
			step3_displayLeft = true;
			step2_5_wait = false;
		}
		if(step3_displayLeft || step4_displaySante){	
			if(faster && escargot.getDirection() == -1)
				game.batch.draw(skin.getSprite("fastLeft"), 0, 240);
			else
				game.batch.draw(skin.getSprite("left"), 0, 240);

		}
		if(step4_displaySante){
		}

		game.batch.end();

	}
	private void score_update(float dist, float delta) {
		double pas = Math.pow(2, Math.round(Math.max(Math.min((1-dist), 1),0)*10));
		score_game.setScore(pas * delta);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void show() {
		if (EscargotGame.son_on) {
			//sound_train.play();
			//music_bg.play();
		}
		game.myRequestHandler.showAds(false);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		game.myRequestHandler.showAds(false);
		sound_train.stop();
		music_bg.stop();
	}

	@Override
	public void pause() {
		game.pause = true;
		sound_train.stop();
		music_bg.stop();
	}

	@Override
	public void resume() {
		if (EscargotGame.son_on) {
			//sound_train.play();
			//music_bg.play();
		}
		game.pause = false;
	}

	@Override
	public void dispose() {
		music_bg.dispose();
		sound_train.dispose();
		sound_mort.dispose();
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("tutorial", true);
		prefs.flush();
		stage.dispose();
		skin.dispose();
	}
}
