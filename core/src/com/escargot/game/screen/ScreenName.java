package com.escargot.game.screen;

import com.escargot.game.IActivityRequestHandler;

public enum ScreenName {
	 
    LOADING {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
            return new LoadingScreen();
        }
    },
 
    MAIN_MENU {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new MainMenuScreen();
        }
    },
 
    GAME {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new GameScreen();
        }
    },
 
    END {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new EndScreen();
        }
    }, 
    
    TUTO{
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new TutorialScreen();
        }
    },
    
    SCORE{
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
             return new ScoreScreen();
        }
    },

    SIMPLESCORE{
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() { return new SimpleScoreScreen(); }
    };

    protected abstract com.badlogic.gdx.Screen getScreenInstance();
 
}
