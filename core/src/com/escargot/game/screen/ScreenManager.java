package com.escargot.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.IntMap;

public final class ScreenManager {
	 
    private static ScreenManager instance;
 
    private Game game;
    private IntMap<com.badlogic.gdx.Screen> screens;
    
    private ScreenManager() {
    	screens = new IntMap<com.badlogic.gdx.Screen>();
    }
 
    public static ScreenManager getInstance() {
        if (null == instance) {
            instance = new ScreenManager();
        }
        return instance;
    }
    
    public void initialize(Game game) {
        this.game = game;
    }
    
    public void show(ScreenName screen) {
        if (null == game) return;
        if (screen == ScreenName.GAME || screen == ScreenName.TUTO || screen == ScreenName.LOADING){
        	dispose(screen);
        }
        if (!screens.containsKey(screen.ordinal())) {
            screens.put(screen.ordinal(), screen.getScreenInstance());
        }
        game.setScreen(screens.get(screen.ordinal()));
    }
 
    public void dispose(ScreenName screen) {
        if (!screens.containsKey(screen.ordinal())) return;
        screens.remove(screen.ordinal()).dispose();
    }
 
    public void dispose() {
        for (com.badlogic.gdx.Screen screen : screens.values()) {
            screen.dispose();
        }
        screens.clear();
        instance = null;
    } 
}