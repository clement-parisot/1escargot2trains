package com.escargot.game.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.escargot.game.EscargotGame;
import com.escargot.game.IActivityRequestHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class AndroidLauncher extends BaseGameActivity implements
		IActivityRequestHandler {

	protected AdView adView;
	protected InterstitialAd mInterstitialAd;

	private static final int REQUEST_ACHIEVEMENTS = 42;

	private static final int REQUEST_LEADERBOARD = 1337;

	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private final int RATE_APP = 2;
	private final int SHOW_INTER_ADS = 3;
	@SuppressWarnings("unused")
	private boolean signInSuccess = false;
	private int inter_ad_counter = 5;

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
			case RATE_APP: {
				AppRater.showRateDialog(adView.getContext());
			}
			case SHOW_INTER_ADS: {
				if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
			}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		RelativeLayout layout = new RelativeLayout(this);
		View gameView = initializeForView(new EscargotGame(this), config);
		adView = new AdView(this);
		adView.setAdUnitId("***REMOVED***");
		adView.setAdSize(AdSize.SMART_BANNER);
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId("***REMOVED***");
		// Initiez une demande générique.
		requestNewAd();
		layout.addView(gameView);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layout.addView(adView, adParams);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewAd();
            }
        });
		setContentView(layout);
		AppRater.app_launched(this);
	}
	
    private void requestNewAd() {
    	// Initiez une demande générique.
		final String id1 = "***REMOVED***";
		final String id2 = "***REMOVED***";
		final String id3 = "***REMOVED***";
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(id1)
				.addTestDevice(id2)
				.addTestDevice(id3)
				.build();
		adView.loadAd(adRequest);
		mInterstitialAd.loadAd(adRequest);
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
		SharedPreferences prefs = this.getSharedPreferences("ggame", 0);
		final SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("anotherday", true);
		editor.commit();
	}

	@Override
	public void onSignInSucceeded() {
		System.out.println("success");
		this.signInSuccess = true;
		SharedPreferences prefs = this.getSharedPreferences("ggame", 0);
		final SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("anotherday", false);
		editor.commit();
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
		SharedPreferences prefs = this.getSharedPreferences("ggame", 0);
		final SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("anotherday", true);
		editor.commit();
	}

	@Override
	public void rateApp() {
		handler.sendEmptyMessage(RATE_APP);
	}

	@Override
	public void unlock(int id) {
		if (isConnected()) {
			switch (id) {
			case 0:
				Games.Achievements.unlock(getApiClient(),
						getString(R.string.achievement_500_points));
				break;
			case 1:
				Games.Achievements.unlock(getApiClient(),
						getString(R.string.achievement_1000_points));
				break;
			case 2:
				Games.Achievements.unlock(getApiClient(),
						getString(R.string.achievement_2000_points));
				break;
			case 3:
				Games.Achievements.unlock(getApiClient(),
						getString(R.string.achievement_3000_points));
				break;
			case 4:
				Games.Achievements.unlock(getApiClient(),
						getString(R.string.achievement_4000_points));
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean isConnected() {
		return isSignedIn() && mHelper.getApiClient().isConnected();
	}

	@Override
	public void showAchievments() {
		if (isConnected()) {
		startActivityForResult(
				Games.Achievements.getAchievementsIntent(getApiClient()),
				REQUEST_ACHIEVEMENTS);
		}
	}

	@Override
	public void envoyerScore(int score) {
		if (isConnected()){
			Games.Leaderboards.submitScore(getApiClient(),
				getString(R.string.leaderboard_highscores), score);
		}
	}

	@Override
	public void classement() {
		if (isConnected()) {
		startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
				getApiClient(), getString(R.string.leaderboard_highscores)),
				REQUEST_LEADERBOARD);
		}
	}

	@Override
	public void show_inter_ads() {
		Log.d("Inter", ""+this.inter_ad_counter);
		this.inter_ad_counter ++;
		if(inter_ad_counter >= 3){
			handler.sendEmptyMessage(SHOW_INTER_ADS);
			this.inter_ad_counter=0;
		}
	}

}
