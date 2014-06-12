package com.escargot.game;

public interface SigningResolver {
	public boolean signAccept = false;
	public void onSignInFailed();
	public void onSignInSucceeded();
}
