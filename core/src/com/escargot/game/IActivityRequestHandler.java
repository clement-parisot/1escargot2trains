package com.escargot.game;

public interface IActivityRequestHandler {
	public void showAds(boolean show);
	public boolean isSignedIn();
	public void beginUserSignIn();
	public void signOutUser();
	public void rateApp();
}
