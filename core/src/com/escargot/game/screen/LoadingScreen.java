 package com.escargot.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.I18NBundle;
import com.escargot.game.EscargotGame;

/**
 * @author Mats Svensson
 */
public class LoadingScreen implements Screen {
	final EscargotGame game;
	   private Stage stage;

	    private Image logo;
	    private Image loadingFrame;
	    private Image loadingBarHidden;
	    private Image screenBg;
	    private Image loadingBg;

	    private float startX, endX;
	    private float percent;

	    private Actor loadingBar;
		private AssetManager manager;

	    public LoadingScreen(EscargotGame game) {
	        super();
	        this.game = game;
	        game.myRequestHandler.showAds(false);
	    }

	    @Override
	    public void show() {
	        // Tell the manager to load assets for the loading screen
	    	manager = new AssetManager();
	        manager.load("loading.pack", TextureAtlas.class);
	        // Wait until they are finished loading
	        manager.finishLoading();

	        // Initialize the stage where we will place everything
	        stage = new Stage();

	        // Get our textureatlas from the manager
	        TextureAtlas atlas = manager.get("loading.pack", TextureAtlas.class);

	        // Grab the regions from the atlas and create some images
	        logo = new Image(atlas.findRegion("libgdx-logo"));
	        loadingFrame = new Image(atlas.findRegion("loading-frame"));
	        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
	        screenBg = new Image(atlas.findRegion("screen-bg"));
	        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

	        // Add the loading bar animation
	        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
	        anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
	        /**
	         * @author Mats Svensson
	         */
	        class LoadingBar extends Actor {

	            Animation animation;
	            TextureRegion reg;
	            float stateTime;

	            public LoadingBar(Animation animation) {
	                this.animation = animation;
	                reg = animation.getKeyFrame(0);
	            }

	            @Override
	            public void act(float delta) {
	                stateTime += delta;
	                reg = animation.getKeyFrame(stateTime);
	            }

	            @Override
	            public void draw(Batch batch, float parentAlpha) {
	                batch.draw(reg, getX(), getY());
	            }
	        }
	        loadingBar = new LoadingBar(anim);

	        // Add all the actors to the stage
	        stage.addActor(screenBg);
	        
	        stage.addActor(loadingBar);
	        stage.addActor(loadingBarHidden);
	        stage.addActor(loadingBg);
	        stage.addActor(loadingFrame);
	        stage.addActor(logo);
	    }

	    @Override
	    public void resize(int width, int height) {
	        // Set our screen to always be XXX x 480 in size
	        stage.getViewport().update(width, height, true);

	        // Make the background fill the screen
	        screenBg.setSize(width, height);

	        // Place the logo in the middle of the screen and 100 px up
	        logo.setX((width - logo.getWidth()) / 2);
	        logo.setY((height - logo.getHeight()) / 2 + 100);

	        // Place the loading frame in the middle of the screen
	        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
	        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

	        // Place the loading bar at the same spot as the frame, adjusted a few px
	        loadingBar.setX(loadingFrame.getX() + 15);
	        loadingBar.setY(loadingFrame.getY() + 5);

	        // Place the image that will hide the bar on top of the bar, adjusted a few px
	        loadingBarHidden.setX(loadingBar.getX() + 35);
	        loadingBarHidden.setY(loadingBar.getY() - 3);
	        // The start position and how far to move the hidden loading bar
	        startX = loadingBarHidden.getX();
	        endX = 440;

	        // The rest of the hidden bar
	        loadingBg.setSize(450, 50);
	        loadingBg.setX(loadingBarHidden.getX() + 30);
	        loadingBg.setY(loadingBarHidden.getY() + 3);
	    }

	    @Override
	    public void render(float delta) {
	        // Clear the screen
			Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			game.batch.begin();game.batch.end();

	        if (game.manager.update() && percent >= 0.99f) { // Load some, will return true if done loading
	        	game.bundle = game.manager.get("i18n/MyBundle", I18NBundle.class);
	        	game.loadingScreen = new MainMenuScreen(game);
	    		game.helpScreen = new HelpScreen(game);
	    		game.endScreen = new EndScreen(game);
	            game.setScreen(game.loadingScreen);
	        }
	        
	        // Interpolate the percentage to make it more smooth
	        percent = Interpolation.linear.apply(percent, game.manager.getProgress(), 0.1f);

	        // Update positions (and size) to match the percentage
	        loadingBarHidden.setX(startX + endX * percent);
	        loadingBg.setX(loadingBarHidden.getX() + 30);
	        loadingBg.setWidth(450 - 450 * percent);
	        loadingBg.invalidate();

	        // Show the loading screen
	        stage.act(Gdx.graphics.getDeltaTime());
	        stage.draw();
	        
	    }

	    @Override
	    public void hide() {
	        // Dispose the loading assets as we no longer need them
	    	manager.unload("loading.pack");
	    }

		@Override
		public void pause() {
		}

		@Override
		public void resume() {
		}

		@Override
		public void dispose() {
			if(manager != null)
				manager.dispose();
		}
	}