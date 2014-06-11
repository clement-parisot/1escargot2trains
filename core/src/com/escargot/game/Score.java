package com.escargot.game;

import java.text.DecimalFormat;
import java.text.Format;

public class Score {
	private float score;
	private static float max_score;
	private Format df = new DecimalFormat("###,###");

	public Score() {
		this.score = 0;
	}

	public int getScore() {
		return (int) score;
	}

	public void resetScore() {
		score = 0;
	}

	public void setScore(double d) {
		this.score += d;
		updateBestScore();
	}

	public void updateBestScore() {
		if (this.score > max_score)
			max_score = score;
	}

	@Override
	public String toString() {
		return df.format(score);
		// return ""+(int)score;
	}

	public String getMaxScore() {
		return df.format(max_score);
		// return ""+(int)max_score;
	}

	public float getMaxScoreValue() {
		return max_score;
	}
}
