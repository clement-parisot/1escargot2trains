package com.escargot.game;

public interface IActivityRequestHandler {
	public void showAds(boolean show);

	public boolean isSignedIn();

	public void beginUserSignIn();

	public void signOutUser();

	public void rateApp();

	public void unlock(int id);

	public boolean isConnected();

	public void showAchievments();

	public void envoyerScore(int score);

	public void classement();
}
