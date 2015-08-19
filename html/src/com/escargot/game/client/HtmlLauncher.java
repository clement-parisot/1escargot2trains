package com.escargot.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.escargot.game.EscargotGame;
import com.escargot.game.IActivityRequestHandler;

public class HtmlLauncher extends GwtApplication implements IActivityRequestHandler{
		@Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
    		return new EscargotGame(this);
        }

		@Override
		public void showAds(boolean show) {
		}

		@Override
		public boolean isSignedIn() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void beginUserSignIn() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void signOutUser() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void rateApp() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unlock(int id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isConnected() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void showAchievments() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void envoyerScore(int score) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void classement() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void show_inter_ads() {
			// TODO Auto-generated method stub
			
		}
}