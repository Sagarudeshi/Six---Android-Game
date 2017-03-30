package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MainGame;



public class RaceScreen implements Screen {
	private MainGame game;
	private Stage stage;

	private Table rootTable;

	private Label inputLabel;
	private TextField textInput;
	private TextButton continueButton;
	private TextButton backButton;

	public RaceScreen( final MainGame game ) {
		this.game = game;
		stage = new Stage( );

		rootTable = new Table( );
		rootTable.setFillParent( true );
		rootTable.center();

		inputLabel = new Label( "Enter a time limit in seconds:", game.skinUI );
		inputLabel.setFontScale( 2.0f );
		rootTable.add( inputLabel ).expandX().padBottom( 50 ).row( );

		textInput = new TextField( "30", game.skinUI );
		rootTable.add( textInput ).expandX().width( 300 ).height( 100 ).padBottom( 50 ).row();

		continueButton = new TextButton( "Continue", game.skinUI );
		continueButton.getLabel().setFontScale( 1.5f );
		continueButton.addListener( new ClickListener(  ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				try{
					game.timeRace = Integer.parseInt( textInput.getText() );
					textInput.getOnscreenKeyboard().show( false );
					game.setScreen( game.gameScreen );
				}
				catch( Exception e ) {
					textInput.getOnscreenKeyboard().show( false );
					textInput.setText( "Invalid" );
				}

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		rootTable.add( continueButton ).expandX().width( 300 ).height( 100 ).padBottom( 50 ).row();

		backButton = new TextButton( "Back", game.skinUI );
		backButton.getLabel().setFontScale( 1.5f );
		backButton.addListener( new ClickListener(  ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				game.setScreen( game.mainMenu );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		rootTable.add( backButton ).expandX().width( 300 ).height( 100 );

		stage.addActor( rootTable );
	}

	@Override
	public void show( ) {
		Gdx.input.setInputProcessor( stage );
		textInput.setText( "30" );
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
