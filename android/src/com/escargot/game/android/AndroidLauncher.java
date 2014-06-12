package com.escargot.game.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.escargot.game.SigningResolver;
import com.escargot.game.EscargotGame;
import com.escargot.game.IActivityRequestHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.example.games.basegameutils.BaseGameActivity;

public class AndroidLauncher extends BaseGameActivity implements
		IActivityRequestHandler {

	protected AdView adView;

	private final static String TAG = "AndroidLauncher";

	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private final int RATE_APP = 2;
	private boolean signInSuccess = false;

	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_ADS: {
				adView.setVisibility(View.VISIBLE);
				break;
			}
			case HIDE_ADS: {
				adView.setVisibility(View.GONE);
				break;
			}
			case RATE_APP:{
				AppRater.showRateDialog(adView.getContext());
			}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		RelativeLayout layout = new RelativeLayout(this);
		View gameView = initializeForView(new EscargotGame(this), config);
		adView = new AdView(this);
		adView.setAdUnitId("***REMOVED***");
		adView.setAdSize(AdSize.SMART_BANNER);
		// Initiez une demande générique.
		final String id1 = "***REMOVED***";
		final String id2 = "***REMOVED***";
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(id1)
				.addTestDevice(id2).build();

		adView.loadAd(adRequest);
		layout.addView(gameView);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layout.addView(adView, adParams);
		setContentView(layout);
		AppRater.app_launched(this);
	}

	// This is the callback that posts a message for the handler
	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onPause() {
		adView.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		adView.resume();
	}

	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
	}

	@Override
	public void onSignInFailed() {
		System.out.println("anotherday...");
		this.signInSuccess = false;
	}

	@Override
	public void onSignInSucceeded() {
		System.out.println("success");
		this.signInSuccess = true;
	}

	@Override
	public boolean isSignedIn() {
		return mHelper.isSignedIn();
	}

	@Override
	public void beginUserSignIn() {
		this.beginUserInitiatedSignIn();
	}

	@Override
	public void signOutUser() {
		this.signOut();
	}

	@Override
	public void rateApp() {
		handler.sendEmptyMessage(RATE_APP);
	}
}
