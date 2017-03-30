package com.mygdx.game.data;

/**
 * Created by Amani on 02/12/2016.
 */

public class ScoreEntry {
	public int score;
	public float time;

	public ScoreEntry( int score, float time ) {
		this.score = score;
		this.time = time;
	}

	public ScoreEntry( ScoreEntry entry ) {
		score = entry.score;
		time = entry.time;
	}
}