package com.escargot.game.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.escargot.game.AbstractGameObject;
import com.escargot.game.Escargot;
import com.escargot.game.EscargotGame;
import com.escargot.game.Score;
import com.escargot.game.Train;

public class GameScreen implements Screen {
	Music music_bg;
	Sound sound_train;
	Sound sound_mort;

	OrthographicCamera camera;

	Texture tex_train;

	Escargot obj_escargot;
	Train obj_trainG;
	Train obj_trainD;
	List<AbstractGameObject> listeTrains;

	private Score score_game;
	final EscargotGame game;
	float zoom_factor;
	private OrthographicCamera cam2;

	ParticleEffect fumee;
	ParticleEffectPool fumeePool, mortPool;
	Array<PooledEffect> effects;

	@SuppressWarnings("unused")
	private boolean faster = false;
	private ParticleEffect mort;
	private boolean end = false;
	private PooledEffect mortEffect;

	public GameScreen(final EscargotGame game) {
		this.game = game;
		// Sons
		music_bg = Gdx.audio.newMusic(Gdx.files.internal("sound0.mp3"));
		sound_train = Gdx.audio.newSound(Gdx.files.internal("sound2.wav"));
		sound_mort = Gdx.audio.newSound(Gdx.files.internal("sound1.wav"));

		// Musique en boucle
		music_bg.setLooping(true);

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1200);
		cam2 = new OrthographicCamera();
		cam2.setToOrtho(false, 640, 480);

		// Textures
		tex_train = new Texture(Gdx.files.internal("train_0.png"));

		// Objets
		obj_escargot = new Escargot(1920 / 2 - 32 / 2, 20, 32, 20,
				game.tex_escargot, 40);
		obj_trainG = new Train(-355, 20, 355, 128, tex_train, -1, 20);
		obj_trainD = new Train(1920, 20, 355, 128, tex_train, 1, 20);

		// Groupes de collision
		listeTrains = new ArrayList<AbstractGameObject>();
		listeTrains.add(obj_trainD);
		listeTrains.add(obj_trainG);

		score_game = new Score();
		InputMultiplexer im = new InputMultiplexer();
		MyInputProcessorKey inputProcessorKey = new MyInputProcessorKey();
		MyInputProcessorTouch inputProcessorTouch = new MyInputProcessorTouch();
		im.addProcessor(new GestureDetector(20.0f,0.4f,0.3f,0.15f, inputProcessorTouch));
		im.addProcessor(inputProcessorKey);
		Gdx.input.setInputProcessor(im);

		effects = new Array<PooledEffect>();
		fumee = new ParticleEffect();
		fumee.load(Gdx.files.internal("fumee_train"), Gdx.files.internal(""));
		mort = new ParticleEffect();
		mort.load(Gdx.files.internal("escargot"), Gdx.files.internal(""));
		mortPool = new ParticleEffectPool(mort, 1, 2);
		mortEffect = mortPool.obtain();
		fumeePool = new ParticleEffectPool(fumee, 1, 2);
		PooledEffect effect = fumeePool.obtain();
		effect.setPosition(obj_trainG.x, 240);
		effects.add(effect);
		PooledEffect effect2 = fumeePool.obtain();
		effect2.setPosition(obj_trainD.x, 240);
		effects.add(effect2);
		game.myRequestHandler.showAds(false);
	}

	public class MyInputProcessorKey implements InputProcessor {

		@Override
		public boolean keyDown(int keycode) {
			if (keycode == Keys.LEFT) {
				faster = true;
				obj_escargot.vitesse = 40;
				obj_escargot.flip(0);
				;
				return true;
			}
			if (keycode == Keys.RIGHT) {
				faster = true;
				obj_escargot.vitesse = 40;
				obj_escargot.flip(640);
				;
				return true;
			}
			return true;
		}

		@Override
		public boolean keyUp(int keycode) {
			faster = false;
			obj_escargot.vitesse = 40;
			return true;
		}

		@Override
		public boolean keyTyped(char character) {
			return true;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			faster = false;
			obj_escargot.vitesse = 40;
			return true;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return true;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return true;
		}

		@Override
		public boolean scrolled(int amount) {
			return true;
		}
	}

	public class MyInputProcessorTouch implements GestureListener {

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			return true;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			obj_escargot.flip(0);
			faster = false;
			obj_escargot.vitesse = 40;
			return true;
		}

		@Override
		public boolean longPress(float x, float y) {
			obj_escargot.vitesse = 120;
			faster = true;
			return true;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			return true;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			return true;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			return true;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			return true;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
				Vector2 pointer1, Vector2 pointer2) {
			return false;
		}

	}

	@Override
	public void render(float delta) {
		float dist = Math.min(Math.abs(obj_escargot.x - obj_trainG.x - 355),
				Math.abs(obj_trainD.x - obj_escargot.x - 32));
		if (!end) {
			move(delta);
			score_update(dist, delta);
		}
		// Effacer l'ecran
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera_zoom(dist);
		camera.update();
		cam2.update();

		game.batch.begin();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.draw(game.bg0, 0, 0, 1920, 1200);
		if (!end) {
			obj_escargot.draw(game.batch);
		}
		obj_trainG.draw(game.batch);
		obj_trainD.draw(game.batch);
		// Update and draw effects:
		for (int i = effects.size - 1; i >= 0; i--) {
			PooledEffect effect = effects.get(i);
			effect.draw(game.batch, delta);
		}
		if (end) {
			mortEffect.draw(game.batch, delta);
			if (mortEffect.isComplete()) {
				end_game();
			}
		}
		game.batch.setProjectionMatrix(cam2.combined);
		game.font.draw(game.batch, score_game.toString(), 340 - score_game
				.toString().length() / 2 * 20, 150);

		game.batch.end();
		game.sr.begin(ShapeType.Filled);
		game.sr.setProjectionMatrix(cam2.combined);
		game.sr.setColor(Math.max(0.0f, 1.0f - dist / 925.0f),
				Math.min(dist / 925.0f, 1.0f), 0.1f, 1);
		game.sr.rect(288, 170, dist / 8, 20);

		game.sr.end();
		if (!end) {
			if (collision(obj_escargot, listeTrains)) {
				if (EscargotGame.son_on) {
					music_bg.stop();
					sound_mort.play();
				}
				if (EscargotGame.vibre_on)
					Gdx.input.vibrate(1000);
				mortEffect.setPosition(obj_escargot.x, obj_escargot.y);
				end = true;
			}
		}
	}

	private void end_game() {
		game.score_player = score_game;
		game.setScreen(game.endScreen);
		dispose();
	}

	private void score_update(float dist, float delta) {
		float x = 0.0f;
		if (dist < 600) {
			x = ((600 - dist) / 600);
		}
		double pas = Math.floor(1.0 + 100.0 * Math.pow(x, 10));
		score_game.setScore(pas * 2.0 * delta);
	}

	private void camera_zoom(float dist) {
		float pas = dist / 650.0f;
		zoom_factor = Math.max(0.2f, Math.min(pas, 1.0f));
		camera.viewportHeight = 1200 * zoom_factor;
		camera.position.y = camera.viewportHeight / 2;
		camera.viewportWidth = 1920 * zoom_factor;
		camera.position.x = Math.min(
				Math.max(obj_escargot.x, camera.viewportWidth / 2),
				1920 - camera.viewportWidth / 2);
	}

	@Override
	public void dispose() {
		music_bg.dispose();
		sound_train.dispose();
		sound_mort.dispose();

		tex_train.dispose();
		for (int i = effects.size - 1; i >= 0; i--)
			effects.get(i).free();
		effects.clear();
		mort.dispose();

		fumee.dispose();
	}

	private void move(float delta) {
		obj_trainG.x += 20 * delta;
		obj_trainD.x -= 20 * delta;
		effects.get(0).setPosition(obj_trainG.x + 320, 145);
		effects.get(1).setPosition(obj_trainD.x + 35, 145);
		obj_escargot.x -= obj_escargot.getDirection() * obj_escargot.vitesse
				* delta;
	}

	private boolean collision(AbstractGameObject objA,
			List<AbstractGameObject> objetsB) {
		for (AbstractGameObject objB : objetsB) {
			if (objA.overlaps(objB)) {
				// Collision
				return true;
			}
		}
		return false;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		if (EscargotGame.son_on) {
			sound_train.play();
			music_bg.play();
		}
		game.myRequestHandler.showAds(false);
	}

	@Override
	public void hide() {
		game.myRequestHandler.showAds(true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
