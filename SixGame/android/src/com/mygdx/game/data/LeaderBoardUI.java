package com.mygdx.game.data;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MainGame;

/**
 * Created by Amani on 02/12/2016.
 */

public class LeaderBoardUI {
	MainGame game;
	Stage stage;

	private Table rootTable;
	private Label titleLabel;
	private Label leaderLabel;
	private Label keyRankLabel;
	private Label keyScoreLabel;
	private Label keyTimeLabel;
	private Label[] numLabels;
	private Label[] scoreLabels;
	private Label[] timeLabels;

	public LeaderBoardUI( MainGame game, Stage stage ) {
		this.game = game;
		this.stage = stage;

		// Setup Leader Board
		rootTable = new Table( );
		rootTable.top( );
		//rootTable.debug();
		rootTable.setFillParent( true );

		titleLabel = new Label( "Six!", game.skinUI );
		titleLabel.setFontScale( 5.0f );
		rootTable.add( titleLabel ).expandX( ).padTop( 15 ).padBottom( 15 ).colspan( 3 ).row( );

		leaderLabel = new Label( "Leader Board", game.skinUI );
		leaderLabel.setFontScale( 3.0f );
		rootTable.add( leaderLabel ).expandX( ).padTop( 15 ).padBottom( 15 ).colspan( 3 ).row( );

		keyRankLabel = new Label( "#", game.skinUI );
		keyRankLabel.setFontScale( 2.0f );
		rootTable.add( keyRankLabel ).right( ).padLeft( 100 );

		keyScoreLabel = new Label( "Score", game.skinUI );
		keyScoreLabel.setFontScale( 2.0f );
		rootTable.add( keyScoreLabel ).expandX( ).padLeft( 100 );

		keyTimeLabel = new Label( "Time", game.skinUI );
		keyTimeLabel.setFontScale( 2.0f );
		rootTable.add( keyTimeLabel ).expandX( ).padRight( 100 ).row( );

		numLabels = new Label[ LeaderBoardData.SIZE ];
		scoreLabels = new Label[ LeaderBoardData.SIZE ];
		timeLabels = new Label[ LeaderBoardData.SIZE ];

		Label numLabel, scoreLabel, timeLabel;
		for( int i = 0; i < LeaderBoardData.SIZE; ++i ) {
			numLabels[ i ] = new Label( "" + ( i + 1 ), game.skinUI );
			scoreLabels[ i ] = new Label( "", game.skinUI );
			timeLabels[ i ] = new Label( "", game.skinUI );

			numLabel = numLabels[ i ];
			numLabel.setFontScale( 2.0f );
			rootTable.add( numLabel ).right( ).padLeft( 100 );

			scoreLabel = scoreLabels[ i ];
			scoreLabel.setFontScale( 2.0f );
			rootTable.add( scoreLabel ).expandX( ).padLeft( 100 );

			timeLabel = timeLabels[ i ];
			timeLabel.setFontScale( 2.0f );
			rootTable.add( timeLabel ).expandX( ).padRight( 100 ).row( );
		}

		stage.addActor( rootTable );
	}

	public void show( ) {
		ScoreEntry entry;
		Label scoreLabel, timeLabel;
		for( int i = 0; i < LeaderBoardData.SIZE; ++i ) {
			scoreLabel = scoreLabels[ i ];
			timeLabel = timeLabels[ i ];

			if( i < game.leaderBoard.entryList.size( ) ) {
				entry = game.leaderBoard.entryList.get( i );

				scoreLabel.setText( " " + entry.score );
				timeLabel.setText( " " + ( ( int ) ( entry.time * 10.0f ) ) / 10.0f + "s" );
			}
			else {
				scoreLabel.setText( " " + 0 );
				timeLabel.setText( " " + 0 + "s" );
			}
		}
	}
}
