package com.mygdx.game.data;

import java.util.ArrayList;

/**
 * Created by Amani on 02/12/2016.
 */

public class LeaderBoardData {
	public static final int SIZE = 10;

	public ArrayList< ScoreEntry > entryList;

	public ScoreEntry entryLast;

	public LeaderBoardData( ) {
		entryList = new ArrayList< ScoreEntry >( );
		entryLast = new ScoreEntry( 0, 0 );
	}

	public void addEntry( ScoreEntry entry ) {
		int i = 0;

		while( i < entryList.size( ) ) {
			if( entry.score > entryList.get( i ).score ) {
				break;
			}
			i++;
		}

		entryList.add( i, new ScoreEntry( entry ) );

		while( entryList.size( ) > SIZE ) {
			entryList.remove( entryList.size( ) - 1 );
		}
	}
}
