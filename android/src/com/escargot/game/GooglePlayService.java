package com.escargot.game;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.games.AuthenticationResult;
import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class GooglePlayService implements IActivityRequestHandler {

    private Activity activity;
    private static final int RC_ACHIEVEMENT_UI = 9003;
    private static final int RC_LEADERBOARD_UI = 9004;

    public void init(Activity a) {
        this.activity = a;
        PlayGamesSdk.initialize(a);
    }

    @Override
    public void showAds(boolean show) {

    }

    @Override
    public void show_inter_ads() {

    }

    @Override
    public boolean isSignedIn() {
        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(this.activity);
        OnCompleteListener<AuthenticationResult> completeListener = null;
        Task<AuthenticationResult> isAuthenticatedTask = gamesSignInClient.isAuthenticated().addOnCompleteListener(completeListener);
        boolean isAuthenticated = (isAuthenticatedTask.isSuccessful() && isAuthenticatedTask.getResult().isAuthenticated());
        return isAuthenticated;
    }

    @Override
    public void beginUserSignIn() {
        System.out.println("test sign in");
        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient( this.activity);
        gamesSignInClient.signIn();
    }

    @Override
    public void signOutUser() {

    }

    @Override
    public void rateApp() {

    }

    @Override
    public void unlock(int id) {
        switch(id) {
            case 1:
                PlayGames.getAchievementsClient(this.activity).unlock(activity.getString(R.string.achievement_500_points));
                break;
            case 2:
                PlayGames.getAchievementsClient(this.activity).unlock(activity.getString(R.string.achievement_1000_points));
                break;
            case 3:
                PlayGames.getAchievementsClient(this.activity).unlock(activity.getString(R.string.achievement_2000_points));
                break;
            case 4:
                PlayGames.getAchievementsClient(this.activity).unlock(activity.getString(R.string.achievement_3000_points));
                break;
            case 5:
                PlayGames.getAchievementsClient(this.activity).unlock(activity.getString(R.string.achievement_4000_points));
                break;
        }
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void showAchievments() {
        PlayGames.getAchievementsClient(this.activity)
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        activity.startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                });
    }

    @Override
    public void envoyerScore(int score) {
        PlayGames.getLeaderboardsClient(this.activity).submitScore(activity.getString(R.string.leaderboard_highscores), score);
    }

    @Override
    public void classement() {

        PlayGames.getLeaderboardsClient(this.activity)
                .getLeaderboardIntent(activity.getString(R.string.leaderboard_highscores))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        activity.startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }
}
