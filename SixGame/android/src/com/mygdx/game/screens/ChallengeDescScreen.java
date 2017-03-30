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



public class ChallengeDescScreen implements Screen {
	private MainGame game;
	private Stage stage;

	private Table rootTable;

	private Label basicLabel;
	private Label goalLabel;
	private Label timeLabel;
	private Label rewardsLabel;
	private TextButton continueButton;

	public ChallengeDescScreen( final MainGame game ) {
		this.game = game;
		stage = new Stage( );

		rootTable = new Table( );
		rootTable.setFillParent( true );
		rootTable.center();

		basicLabel = new Label( "The Challenge is: ", game.skinUI );
		basicLabel.setFontScale( 2.0f );
		rootTable.add( basicLabel ).expandX().padBottom( 50 ).row( );

		goalLabel = new Label( "To get a score of " + game.goalRace + ".", game.skinUI );
		goalLabel.setFontScale( 2.0f );
		rootTable.add( goalLabel ).expandX().padBottom( 50 ).row( );

		timeLabel = new Label( "In " + game.timeRace + " seconds.", game.skinUI );
		timeLabel.setFontScale( 2.0f );
		rootTable.add( timeLabel ).expandX().padBottom( 50 ).row( );

		rewardsLabel = new Label( "For a reward of " + game.rewardRace + " points.", game.skinUI );
		rewardsLabel.setFontScale( 2.0f );
		rootTable.add( rewardsLabel ).expandX().padBottom( 50 ).row( );

		continueButton = new TextButton( "Continue", game.skinUI );
		continueButton.getLabel().setFontScale( 1.5f );
		continueButton.addListener( new ClickListener(  ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				game.setScreen( game.gameScreen );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		rootTable.add( continueButton ).expandX().width( 300 ).height( 100 );

		stage.addActor( rootTable );
	}

	@Override
	public void show( ) {
		Gdx.input.setInputProcessor( stage );

		basicLabel.setText( "The Challenge is: " );
		goalLabel.setText( "To get a score of " + game.goalRace + "." );
		timeLabel.setText( "In " + game.timeRace + " seconds." );
		rewardsLabel.setText( "For a reward of " + game.rewardRace + " points." );
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