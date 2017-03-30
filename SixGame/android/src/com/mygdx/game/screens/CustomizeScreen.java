package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MainGame;



public class CustomizeScreen implements Screen {
	private MainGame game;
	private Stage stage;

	private Table rootTable;

	private Label pointsLabel;
	private Label keyShapeLabel;
	private TextButton[] shapeButtons;
	private Label keyColorLabel;
	private TextButton[] colorButtons;
	private TextButton backButton;
	private TextButton resetButton;

	public CustomizeScreen( final MainGame game ) {
		this.game = game;
		stage = new Stage( );

		// Table
		rootTable = new Table( );
		rootTable.setFillParent( true );
		rootTable.center( );

		// Label Shape
		keyShapeLabel = new Label( "Shapes( " + game.customizeData.costShapes + "ea )", game.labelStyle );
		keyShapeLabel.setFontScale( 2.0f );
		rootTable.add( keyShapeLabel ).expandX( ).colspan( 3 ).left( ).padLeft( 50 ).padBottom( 25 ).row( );

		// Shape Buttons
		shapeButtons = new TextButton[ game.customizeData.numShapes ];

		for( int i = 0; i < game.customizeData.numShapes; ++i ) {
			final int idxShape = i;

			shapeButtons[ idxShape ] = new TextButton( game.customizeData.shapeStrings[ i ], game.skinUI );
			shapeButtons[ idxShape ].getLabel( ).setFontScale( 1.5f );

			shapeButtons[ idxShape ].addListener( new ClickListener( ) {
				@Override
				public void touchUp( InputEvent event, float x, float y, int pointer, int mouseButton ) {
					super.touchUp( event, x, y, pointer, mouseButton );
					if( !game.customizeData.shapeUnlocked[ idxShape ] ) {
						if( game.customizeData.numPoints >= game.customizeData.costShapes ) {
							game.customizeData.numPoints -= game.customizeData.costShapes;
							game.customizeData.shapeUnlocked[ idxShape ] = true;
							show( );
						}

						game.saveFileData( );
						return;
					}

					game.customizeData.shapeSelected = game.customizeData.shapeSides[ idxShape ];
					game.saveFileData( );
				}
			} );

			rootTable.add( shapeButtons[ i ] ).expandX( ).width( 300 ).height( 100 ).padBottom( 25 );

			if( i % 3 == 2 ) rootTable.row( );
		}

		// Label Colors
		keyColorLabel = new Label( "Colors( " + game.customizeData.costColors + "ea )", game.labelStyle );
		keyColorLabel.setFontScale( 2.0f );
		rootTable.add( keyColorLabel ).expandX( ).colspan( 3 ).left( ).padLeft( 50 ).padTop( 75 ).padBottom( 25 ).row( );

		// Color Buttons
		colorButtons = new TextButton[ game.customizeData.numColors ];

		for( int i = 0; i < game.customizeData.numColors; ++i ) {
			final int idxColor = i;

			colorButtons[ idxColor ] = new TextButton( game.customizeData.colorStrings[ i ], game.skinUI );
			colorButtons[ idxColor ].getLabel( ).setFontScale( 1.5f );

			colorButtons[ idxColor ].addListener( new ClickListener( ) {
				@Override
				public void touchUp( InputEvent event, float x, float y, int pointer, int mouseButton ) {
					super.touchUp( event, x, y, pointer, mouseButton );
					if( !game.customizeData.colorUnlocked[ idxColor ] ) {
						if( game.customizeData.numPoints >= game.customizeData.costColors ) {
							game.customizeData.numPoints -= game.customizeData.costColors;
							game.customizeData.colorUnlocked[ idxColor ] = true;
							show( );
						}

						game.saveFileData( );
						return;
					}

					game.customizeData.colorSelected = game.customizeData.colorColors[ idxColor ];
					game.saveFileData( );
				}
			} );

			rootTable.add( colorButtons[ i ] ).expandX( ).width( 300 ).height( 100 ).padBottom( 25 );

			if( i % 3 == 2 ) rootTable.row( );
		}

		// Label Points
		pointsLabel = new Label( "", game.labelStyle );
		pointsLabel.setFontScale( 2.0f );
		rootTable.add( pointsLabel ).expandX( ).colspan( 3 ).left( ).padLeft( 50 ).row( );

		// Reset Button
		resetButton = new TextButton( "Reset", game.skinUI );
		resetButton.getLabel().setFontScale( 1.5f );
		resetButton.addListener( new ClickListener( ) {
			@Override
			public void touchUp ( InputEvent event,float x, float y, int pointer, int button){

				super.touchUp( event, x, y, pointer, button );
			}
		} );

		// Back Button
		backButton = new TextButton( "Back", game.skinUI );
		backButton.getLabel( ).setFontScale( 1.5f );
		backButton.addListener( new ClickListener( ) {
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				game.setScreen( game.mainMenu );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		rootTable.add( backButton ).expandX( ).width( 300 ).height( 100 ).colspan( 3 ).bottom( ).padTop( 100 );

		stage.addActor( rootTable );
	}

	@Override
	public void show( ) {
		Gdx.input.setInputProcessor( stage );

		pointsLabel.setText( "Points: " + game.customizeData.numPoints );

		for( int i = 0; i < game.customizeData.numShapes; ++i ) {
			if( game.customizeData.shapeUnlocked[ i ] ) {
				shapeButtons[ i ].getLabel( ).setColor( Color.GREEN );
			}
			else {
				shapeButtons[ i ].getLabel( ).setColor( Color.RED );
			}
		}

		for( int i = 0; i < game.customizeData.numColors; ++i ) {
			if( game.customizeData.colorUnlocked[ i ] ) {
				colorButtons[ i ].getLabel( ).setColor( Color.GREEN );
			}
			else {
				colorButtons[ i ].getLabel( ).setColor( Color.RED );
			}
		}
	}

	@Override
	public void render( float delta ) {
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

		stage.act( delta );
		stage.draw( );

		game.camera.position.set( 0, 0, 0 );
		game.camera.update( );

		float radius = 10.0f;
		game.shapeRenderer.setProjectionMatrix( game.camera.combined );
		game.shapeRenderer.begin( ShapeRenderer.ShapeType.Filled );

		game.shapeRenderer.identity( );

		game.shapeRenderer.translate( 0, game.ppmDim.y * 0.5f - radius - 7, 0 );
		if( game.customizeData.shapeSelected % 2 == 0 ) {
		}
		//game.shapeRenderer.rotate( 0, 0, 1, 180.0f / game.customizeData.shapeSelected );
		else
			game.shapeRenderer.rotate( 0, 0, 1, 90 );

		game.shapeRenderer.setColor( game.customizeData.colorSelected );

		for( int i = 0; i < game.customizeData.shapeSelected - 1; ++i ) {
			game.shapeRenderer.triangle(
				MathUtils.cosDeg( 360.0f / game.customizeData.shapeSelected * i ) * radius,
				MathUtils.sinDeg( 360.0f / game.customizeData.shapeSelected * i ) * radius,
				0, 0,
				MathUtils.cosDeg( 360.0f / game.customizeData.shapeSelected * ( i + 1 ) ) * radius,
				MathUtils.sinDeg( 360.0f / game.customizeData.shapeSelected * ( i + 1 ) ) * radius
			);
		}

		game.shapeRenderer.triangle(
			MathUtils.cosDeg( 360.0f / game.customizeData.shapeSelected * ( game.customizeData.shapeSelected - 1 ) ) * radius,
			MathUtils.sinDeg( 360.0f / game.customizeData.shapeSelected * ( game.customizeData.shapeSelected - 1 ) ) * radius,
			0, 0,
			MathUtils.cosDeg( 360.0f / game.customizeData.shapeSelected * 0 ) * radius,
			MathUtils.sinDeg( 360.0f / game.customizeData.shapeSelected * 0 ) * radius
		);

		game.shapeRenderer.setColor( Color.WHITE );

		for( int i = 0; i < game.customizeData.shapeSelected - 1; ++i ) {
			game.shapeRenderer.line(
				MathUtils.cosDeg( 360.0f / game.customizeData.shapeSelected * i ) * radius,
				MathUtils.sinDeg( 360.0f / game.customizeData.shapeSelected * i ) * radius,
				MathUtils.cosDeg( 360.0f / game.customizeData.shapeSelected * ( i + 1 ) ) * radius,
				MathUtils.sinDeg( 360.0f / game.customizeData.shapeSelected * ( i + 1 ) ) * radius
			);
		}

		game.shapeRenderer.line(
			MathUtils.cosDeg( 360.0f / game.customizeData.shapeSelected * ( game.customizeData.shapeSelected - 1 ) ) * radius,
			MathUtils.sinDeg( 360.0f / game.customizeData.shapeSelected * ( game.customizeData.shapeSelected - 1 ) ) * radius,
			MathUtils.cosDeg( 360.0f / game.customizeData.shapeSelected * 0 ) * radius,
			MathUtils.sinDeg( 360.0f / game.customizeData.shapeSelected * 0 ) * radius
		);

		game.shapeRenderer.end( );
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
