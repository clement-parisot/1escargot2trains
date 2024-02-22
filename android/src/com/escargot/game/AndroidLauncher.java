package com.escargot.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.escargot.game.EscargotGame;

public class AndroidLauncher extends AndroidApplication {

	private GooglePlayService gps;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		this.gps = new GooglePlayService();
		gps.init(this);
		gps.isSignedIn();
		initialize(new EscargotGame(gps), config);
	}
}

