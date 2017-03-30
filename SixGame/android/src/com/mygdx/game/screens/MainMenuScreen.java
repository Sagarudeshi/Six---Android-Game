package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MainGame;
import com.mygdx.game.data.LeaderBoardUI;



public class MainMenuScreen implements Screen {
	MainGame game;
	Stage stage;

	LeaderBoardUI leaderBoardUI;

	Table rootTable;
	TextButton buttonChallenge;
	TextButton buttonNormal;
	TextButton buttonJenga;
	TextButton buttonRace;
	TextButton buttonCustomize;

	public MainMenuScreen( final MainGame game ) {
		this.game = game;
		stage = new Stage( );

		// Setup LeaderBoard UI
		leaderBoardUI = new LeaderBoardUI( game, stage );

		rootTable = new Table( );
		rootTable.setFillParent( true );
		rootTable.bottom( );

		// Setup Buttons
		buttonChallenge = new TextButton( "Challenge", game.skinUI );
		buttonChallenge.getLabel().setFontScale( 1.5f );
		buttonChallenge.addListener( new ClickListener(  ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				game.activeMode = MainGame.GameMode.GM_CHALLENGE;
				game.setScreen( game.challengeScreen );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		rootTable.add( buttonChallenge ).expandX().width( 300 ).height( 100 ).padBottom( 50 ).colspan( 3 ).row();

		buttonNormal = new TextButton( "Normal", game.skinUI );
		buttonNormal.getLabel( ).setFontScale( 1.5f );
		buttonNormal.addListener( new ClickListener( ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				super.touchUp( event, x, y, pointer, button );
				game.activeMode = MainGame.GameMode.GM_NORMAL;
				game.setScreen( game.gameScreen );
			}
		} );
		rootTable.add( buttonNormal ).expandX( ).width( 300 ).height( 100 ).padBottom( 50 );

		buttonJenga = new TextButton( "Jenga", game.skinUI );
		buttonJenga.getLabel( ).setFontScale( 1.5f );
		buttonJenga.addListener( new ClickListener( ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				super.touchUp( event, x, y, pointer, button );
				game.activeMode = MainGame.GameMode.GM_JENGA;
				game.setScreen( game.gameScreen );
			}
		} );
		rootTable.add( buttonJenga ).expandX( ).width( 300 ).height( 100 ).padBottom( 50 );

		buttonRace = new TextButton( "Race", game.skinUI );
		buttonRace.getLabel( ).setFontScale( 1.5f );
		buttonRace.addListener( new ClickListener( ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				super.touchUp( event, x, y, pointer, button );
				game.activeMode = MainGame.GameMode.GM_RACE;
				game.setScreen( game.raceScreen );
			}
		} );
		rootTable.add( buttonRace ).expandX( ).width( 300 ).height( 100 ).padBottom( 50 ).row( );

		buttonCustomize = new TextButton( "Customize", game.skinUI );
		buttonCustomize.getLabel( ).setFontScale( 1.5f );
		buttonCustomize.addListener( new ClickListener( ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				super.touchUp( event, x, y, pointer, button );
				game.setScreen( game.customizeScreen );
			}
		} );
		rootTable.add( buttonCustomize ).expandX( ).width( 300 ).height( 100 ).padBottom( 50 ).colspan( 3 );

		stage.addActor( rootTable );
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
	public void resume( ) {
	}

	@Override
	public void pause( ) {
	}

	@Override
	public void show( ) {
		Gdx.input.setInputProcessor( stage );

		leaderBoardUI.show( );
	}

	@Override
	public void hide( ) {

	}

	@Override
	public void dispose( ) {
		stage.dispose( );
	}
}
