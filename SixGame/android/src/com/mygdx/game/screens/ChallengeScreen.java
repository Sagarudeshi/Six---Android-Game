package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MainGame;



public class ChallengeScreen implements Screen {
	private MainGame game;
	private Stage stage;

	private Table rootTable;

	public final int numChallenges = 3;

	private Label inputLabel;
	public boolean isChallenge[];
	private TextButton[] challengeButtons;
	private TextButton backButton;

	public ChallengeScreen( final MainGame game ) {
		this.game = game;
		stage = new Stage( );

		rootTable = new Table( );
		rootTable.setFillParent( true );
		rootTable.center();

		inputLabel = new Label( "Select a daily Challenge:", game.skinUI );
		inputLabel.setFontScale( 2.0f );
		rootTable.add( inputLabel ).expandX().padBottom( 50 ).colspan( 3 ).row();

		isChallenge = new boolean[ 3 ];
		challengeButtons = new TextButton[ 3 ];

		for( int i = 0; i < numChallenges; ++i ) {
			final int idx = i;
			isChallenge[ i ] = true;
			challengeButtons[ i ] = new TextButton( "Challenge " + ( i + 1 ), game.skinUI );
			challengeButtons[ i ].getLabel().setFontScale( 1.5f );
			challengeButtons[ i ].addListener( new ClickListener(  ) {
				@Override
				public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
					if( isChallenge[ idx ] ) {
						game.timeRace = 60 - idx * 15;
						game.goalRace = 100;
						game.rewardRace = ( idx + 1 ) * 10;

						isChallenge[ idx ] = false;
						game.setScreen( game.challengeDescScreen );
					}

					super.touchUp( event, x, y, pointer, button );
				}
			} );
			rootTable.add( challengeButtons[ i ] ).expandX().width( 300 ).height( 100 ).padBottom( 50 );
		}
		rootTable.row();

		backButton = new TextButton( "Back", game.skinUI );
		backButton.getLabel().setFontScale( 1.5f );
		backButton.addListener( new ClickListener(  ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				game.setScreen( game.mainMenu );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		rootTable.add( backButton ).expandX().width( 300 ).height( 100 ).colspan( 3 );

		stage.addActor( rootTable );
	}

	@Override
	public void show( ) {
		Gdx.input.setInputProcessor( stage );
		for( int i = 0; i < numChallenges; ++i ) {
			if( isChallenge[ i ] ) {
				challengeButtons[ i ].getLabel( ).setColor( Color.GREEN );
			}
			else {
				challengeButtons[ i ].getLabel( ).setColor( Color.RED );
			}
		}
	}

	@Override
	public void render( float delta ) {
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

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