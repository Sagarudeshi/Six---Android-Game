package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MainGame;



public class SummaryScreen implements Screen {
	MainGame game;
	Stage stage;

	Table rootTable;
	Label scoreLabel;
	Label timeLabel;
	Label pointsLabel;
	Label totalPointsLabel;
	TextButton continueButton;
	TextButton againButton;

	public SummaryScreen( final MainGame game ) {
		this.game = game;
		stage = new Stage( );

		rootTable = new Table( );
		rootTable.setFillParent( true );

		scoreLabel = new Label( "", game.labelStyle );
		scoreLabel.setFontScale( 2.0f );
		rootTable.add( scoreLabel ).expandX( ).row( );

		timeLabel = new Label( "", game.labelStyle );
		timeLabel.setFontScale( 2.0f );
		rootTable.add( timeLabel ).expandX( ).row( );

		pointsLabel = new Label( "", game.labelStyle );
		pointsLabel.setFontScale( 2.0f );
		rootTable.add( pointsLabel ).expandX( ).row( );

		totalPointsLabel = new Label( "", game.labelStyle );
		totalPointsLabel.setFontScale( 2.0f );
		rootTable.add( totalPointsLabel ).expandX( ).row( );

		continueButton = new TextButton( "Continue", game.skinUI );
		continueButton.getLabel( ).setFontScale( 1.5f );
		continueButton.addListener( new ClickListener( ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				super.touchUp( event, x, y, pointer, button );

				game.setScreen( game.mainMenu );
			}
		} );
		rootTable.add( continueButton ).expandX( ).width( 300 ).height( 100 ).padTop( 100 ).row();

		againButton = new TextButton( "Play Again", game.skinUI );
		againButton.getLabel().setFontScale( 1.5f );
		againButton.addListener( new ClickListener( ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				super.touchUp( event, x, y, pointer, button );
				if( game.activeMode == MainGame.GameMode.GM_RACE ) {
					game.setScreen( game.raceScreen );
				}
				else {
					game.setScreen( game.gameScreen );
				}
			}
		} );
		rootTable.add( againButton ).expandX( ).width( 300 ).height( 100 ).padTop( 50 );

		stage.addActor( rootTable );
	}

	@Override
	public void show( ) {
		Gdx.input.setInputProcessor( stage );
		int totalEarned = 0;

		totalEarned = game.leaderBoard.entryLast.score / 10;

		if( game.activeMode == MainGame.GameMode.GM_NORMAL ) {
			game.leaderBoard.addEntry( game.leaderBoard.entryLast );
		}

		if( game.activeMode == MainGame.GameMode.GM_CHALLENGE &&
			game.leaderBoard.entryLast.score >= game.goalRace ) {
			totalEarned += game.rewardRace;
		}
		game.customizeData.numPoints += totalEarned;

		scoreLabel.setText( "You got a score of " + game.leaderBoard.entryLast.score + "!" );
		timeLabel.setText( "You finished in " + ( int ) ( game.leaderBoard.entryLast.time * 10.0f ) / 10.0f + " seconds!" );
		pointsLabel.setText( "You earned " + totalEarned + " points!" );
		totalPointsLabel.setText( "You have " + game.customizeData.numPoints + " total points!" );

		if( game.activeMode == MainGame.GameMode.GM_CHALLENGE &&
			game.leaderBoard.entryLast.score >= game.goalRace ) {
			againButton.setVisible( false );
		}
		else {
			againButton.setVisible( true );
		}

		game.saveFileData( );
	}

	@Override
	public void render( float delta ) {
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

		stage.act( delta );
		stage.draw( );
	}

	@Override
	public void resize( int width, int height ) {

	}

	@Override
	public void pause( ) {

	}

	@Override
	public void resume( ) {

	}

	@Override
	public void hide( ) {

	}

	@Override
	public void dispose( ) {

	}
}
