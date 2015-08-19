 package com.escargot.game.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.escargot.game.EscargotGame;
import com.escargot.game.Fumee;
import com.escargot.game.RessourcesManager;
import com.escargot.game.Score;
import com.escargot.game.tuto.EscargotActorTuto;
import com.escargot.game.tuto.TrainActorTuto;

public class TutorialScreen implements Screen {
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

	private Sprite spriteRails;

	private Score score_game;
	
	// Animation steps
	private boolean step0_displayText = true;
	private boolean step1_displayRight = false;
	private boolean step1_5_wait = false;
	private boolean step2_displayScore = false;
	private boolean step2_5_wait = false;
	private boolean step3_displayLeft = false;
	private boolean step4_displaySante = false;
	public boolean faster;

	private Animation animation;

	private Blinker blinkerRight;
	private Blinker blinkerLeft;

	private boolean end = false;

	private List<TrainActorTuto> listeTrain;

	private ParticleEffect mort;

	private ParticleEffectPool mortPool;

	private PooledEffect mortEffect;

	private Batch batch;

	private ShapeRenderer sr;
	private boolean pause;
	private BitmapFont fontNash;
	private CharSequence help;
	private BitmapFont fontVani;

	public TutorialScreen() {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		Gdx.input.setCatchBackKey(true);
		stage = new Stage(new StretchViewport(1920, 1080)){
			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.ESCAPE){
					Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
					if (!prefs.getBoolean("firstGame", true)) {
						ScreenManager.getInstance().show(ScreenName.MAIN_MENU);
					}
					return false;
				}
				if(step0_displayText){
					resume();
					step0_displayText = false;
				}
				if(step2_displayScore){
					resume();
					step2_5_wait = true;
					step2_displayScore = false;
				}
				if(step3_displayLeft){
					resume();
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
						resume();
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
		score_game = new Score();

		// Tout l'ecran de jeu (1920/1080)
		cameraGame = (OrthographicCamera) stage.getCamera();
		cameraGame.setToOrtho(false, 1920, 1080);
		cameraScore = new OrthographicCamera();
		cameraScore.setToOrtho(false, 960, 540);
		
		// Sons
		music_bg = RessourcesManager.getInstance().getMusic("sound0.mp3");
		sound_train = RessourcesManager.getInstance().getSound("sound2.wav");
		sound_mort = RessourcesManager.getInstance().getSound("sound1.wav");

		// Musique en boucle
		music_bg.setLooping(true);

		skin = new Skin();
		// Load textures
		TextureAtlas atlas =  RessourcesManager.getInstance().getAtlas("pack.atlas");
		skin.addRegions(atlas);
		
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
		listeTrain = new ArrayList<TrainActorTuto>();
		listeTrain.add(trainD);
		listeTrain.add(trainG);
		stage.addActor(trainD);
		stage.act(1);
		
		effects = new Array<PooledEffect>();
		mort = new ParticleEffect();
		mort.load(Gdx.files.internal("escargot"), atlas);
		mortPool = new ParticleEffectPool(mort, 1, 2);
		mortEffect = mortPool.obtain();
		fumee = new Fumee();
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
					resume();
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
					resume();
					step0_displayText = false;
				}
				if(step2_displayScore){
					resume();
					step2_5_wait = true;
					step2_displayScore = false;
				}
				if(step3_displayLeft){
					resume();
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
		EscargotGame.myRequestHandler.showAds(false);
		Texture bgdTexture = RessourcesManager.getInstance().getTexture("background_0.jpg");
		bgdTexture.setWrap(TextureWrap.MirroredRepeat,TextureWrap.ClampToEdge);
		spriteBgd = new Sprite(bgdTexture, 0, 0, 1920, 1080);
		Texture railsTexture = RessourcesManager.getInstance().getTexture("rails_0.png");
		railsTexture.setWrap(TextureWrap.MirroredRepeat,TextureWrap.ClampToEdge);
		spriteRails = new Sprite(railsTexture, 4096, 88);
		
		blinkerRight = new Blinker();
		blinkerRight.setBlinking(true);
		
		blinkerLeft = new Blinker();
		blinkerLeft.setBlinking(true);
		fontNash = RessourcesManager.getInstance().getFont("nashville.fnt");
		fontVani = RessourcesManager.getInstance().getFont("vanilla.fnt");
		help = RessourcesManager.getInstance().getBundle().get("help");
	}
	
	private void camera_zoom(float dist, Vector2 posEscargot) {
		
		float pas = Interpolation.linear.apply(dist);
		zoom_factor = Math.max(0.2f, Math.min(pas, 1.0f));
		cameraGame.viewportHeight = 1080 * zoom_factor;
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

		Gdx.graphics.getDeltaTime();

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
		this.batch.begin();
		this.batch.setProjectionMatrix(cameraGame.combined);
		//this.batch.draw(game.manager.get("background_0.jpg",Texture.class), 0, 0, 640, 480);
		scrollTimer+=Gdx.graphics.getDeltaTime()*0.1f;
		if(scrollTimer>2.0f)
			scrollTimer = 0.0f;

		//	     spriteBgd.setU(scrollTimer);
		//	     spriteBgd.setU2(scrollTimer+1);
		//	     spriteRails.setU(scrollTimer);
		//	     spriteRails.setU2(scrollTimer+1);
		this.batch.draw(spriteBgd, 0, 0, 1920, 1080);
		this.batch.end();
		stage.draw();
		this.batch.begin();
		this.batch.draw(spriteRails, 0, 118, 1920, 32);
		// Update and draw effects:
		effects.get(0).setPosition(trainG.getFumX(), trainG.getFumY());
		effects.get(1).setPosition(trainD.getFumX(), trainD.getFumY());
		this.batch.end();
		if(!this.pause){
			stage.act(Gdx.graphics.getDeltaTime());
			score_update(dist, delta);
		}
		
		if(!step1_displayRight && dist > 0.05 && !step1_5_wait && !step2_displayScore && !step2_5_wait && ! step3_displayLeft && !step4_displaySante){
			step1_displayRight = true;
			pause();
		}
		Gdx.gl.glEnable(GL20.GL_BLEND);
		this.sr.setProjectionMatrix(cameraScore.combined);
		this.sr.begin(ShapeType.Filled);
		
		if(faster && escargot.getDirection() == 1){
			this.sr.rect(960, 0, -240, 540, new Color(0, 1, 0, 0.5f), new Color(0, 1, 0, 0f), new Color(0, 1, 0, 0), new Color(0, 1, 0, 0.5f));
		}
		if(step2_displayScore|| step3_displayLeft || step4_displaySante){
		}
		if((step3_displayLeft  || step4_displaySante) && faster && escargot.getDirection() == -1){
			this.sr.rect(0, 0, 240, 540, new Color(0, 1, 0, 0.5f), new Color(0, 1, 0, 0f), new Color(0, 1, 0, 0), new Color(0, 1, 0, 0.5f));
		}
		if(step4_displaySante){
			this.sr.rect(240, 0, 480, 40, Color.RED, Color.GREEN, Color.GREEN, Color.RED);
			this.sr.setColor(0,0,0,1);
			this.sr.rect(720, 0, Math.min(Math.max(-1*Math.round((dist)*480/48)*48, -480), 0), 40);
			this.sr.end();
			this.sr.begin(ShapeType.Line);
			this.sr.setColor(0,0,0,1);
			for(int i = 0; i <= 480; i+=48){
				this.sr.rect(240, 0, i, 40);
			}
		}
		this.sr.end();
		this.batch.begin();
		for (int i = effects.size - 1; i >= 0; i--) {
			PooledEffect effect = effects.get(i);
			if(!this.pause)
				effect.draw(this.batch, delta);
			else
				effect.draw(this.batch);
		}
		if (end) {
			mortEffect.draw(this.batch, delta);
			if (mortEffect.isComplete()) {
				end_game();
			}
		}
		this.batch.setProjectionMatrix(cameraScore.combined);
		// Step0
		if(step0_displayText){
			fontNash.draw(this.batch, help, 0, 540, 960, Align.left, true);
			pause();
		}
		if(step1_displayRight || step1_5_wait || step2_displayScore || step2_5_wait || step3_displayLeft || step4_displaySante){
			if(faster && escargot.getDirection() == 1){
				this.batch.draw(skin.getSprite("fastRight"), 832, 270);
			}
			else{
				if(!step1_displayRight || !blinkerRight.shouldBlink(delta))
					this.batch.draw(skin.getSprite("right"), 832, 270);
			}
		}
		if(step1_5_wait && distd < 1000){
			pause();
			step2_displayScore = true;
			step1_5_wait = false;
		}
		if(step2_displayScore|| step2_5_wait || step3_displayLeft || step4_displaySante){
			fontVani.setFixedWidthGlyphs(score_game.toString());
			fontVani.draw(this.batch, score_game.toString(), 240, 535, 480, Align.center, false);
		}
		if(step2_5_wait && distd < distg){
			pause();
			step3_displayLeft = true;
			step2_5_wait = false;
		}
		if(step3_displayLeft || step4_displaySante){	
			if(faster && escargot.getDirection() == -1)
				this.batch.draw(skin.getSprite("fastLeft"), 64, 270);
			else
				if(!step3_displayLeft || !blinkerLeft.shouldBlink(delta))
					this.batch.draw(skin.getSprite("left"), 64, 270);

		}
		if(step4_displaySante){
		}
		this.batch.end();
		
		if(!end){
			if(distg <= 0 || distd <= 0){
				if (EscargotGame.son_on) {
					music_bg.stop();
					sound_mort.play();
				}
				if (EscargotGame.vibre_on)
					Gdx.input.vibrate(1000);
				mortEffect.setPosition(escargot.getX(), escargot.getY());
				escargot.setVisible(false);
				end = true;
			}
		}

	}

	private void end_game() {
		ScreenManager.getInstance().show(ScreenName.END);
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
		RessourcesManager.getInstance().finishLoad();
		pause = false;
		if (EscargotGame.son_on) {
			sound_train.play();
			music_bg.play();
		}
		EscargotGame.myRequestHandler.showAds(false);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		EscargotGame.myRequestHandler.showAds(false);
		sound_train.stop();
		music_bg.stop();
	}

	@Override
	public void pause() {
		pause = true;
		sound_train.stop();
		music_bg.stop();
	}

	@Override
	public void resume() {
		RessourcesManager.getInstance().finishLoad();
		pause = false;
	}

	@Override
	public void dispose() {
		Preferences prefs = Gdx.app.getPreferences("Escargot prefs");
		prefs.putBoolean("tutorial", true);
		prefs.flush();
		mort.dispose();
		fumee.dispose();
		stage.dispose();
		skin.dispose();
		batch.dispose();
		sr.dispose();
	}
	
	public class Blinker {
		private float BLINK_TIME = 100f;
		private int BLINKING_FRAMES = 30;

		private boolean isBlinking;
		private int blinkFrameCounter;
		private float blinkTimer;

		public Blinker() {
		    this.blinkTimer = 0;
		    this.blinkFrameCounter = 0;
		    this.isBlinking = false;
		}

		public boolean shouldBlink(float delta) {
		    if (isBlinking) {
		        blinkTimer += delta;
		        blinkFrameCounter++;
		        if (blinkTimer < BLINK_TIME) {
		            if (blinkFrameCounter % BLINKING_FRAMES <20) {
		                return true;
		            }
		        } else {
		            blinkTimer = 0;
		            isBlinking = false;
		        }
		    }
		    return false;
		}

		public boolean isBlinking() {
		    return isBlinking;
		}

		public void setBlinking(boolean isBlinking) {
		    this.isBlinking = isBlinking;
		}
		}
}
