package com.mygdx.game.screens;

import android.opengl.GLES10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MainGame;
import com.mygdx.game.data.Grid;
import com.mygdx.game.data.Particle;
import com.mygdx.game.data.ParticleSystem;
import com.mygdx.game.globals.Globals;
import com.mygdx.game.shapes.*;

import java.util.Iterator;
import java.util.Vector;

public class GameScreen implements Screen {
	// Conv Vars
	static final float timeStep = 1.0f / 60.0f;
	static final float timeSuper = 3.0f;

	static final int maxFails = 5;
	static final int maxPlacements = 20;

	static final float percSuper = 0.01f;
	static final float percObstacle = 0.02f;

	static final int penaltySuper = 30;

	// Time Vars
	private float timeSuperRemaining = 0.0f;
	private float accumTime = 0.0f;

	// Mouse Vars
	private Vector2 lastPos = new Vector2( );
	private Vector2 currPos = new Vector2( );
	private Vector2 deltaPos = new Vector2( );
	private Vector2 ppmPos = new Vector2( );

	// Game/Stage
	private MainGame game;
	private Stage stage;
	private SpriteBatch batch;
	private boolean	isPaused;
	private boolean isMuted;

	// Table
	Table rootTable;

	// Score/Time labels
	private int yScore;
	private int yCheckpoint;
	private Label scoreLabel;
	private Label timeLabel;

	// Pause Button
	private TextButton pauseButton;

	// Pause Menu Table
	private Table pauseTable;

	// Pause Menu Buttons
	private TextButton resumeButton;
	private TextButton restartButton;
	private TextButton mainMenuButton;
	private TextButton muteButton;

	// Shape Lists
	private Vector< BasicShape > listDynamic;
	private Vector< BasicShape > listStatic;

	// Generate Grid
	private int yGen;
	private Grid gridGenerate;

	// Star
	Sprite starSprite;

	// Particle System
	private ParticleSystem particleSystem;

	PlayerShape hexagon;

	public GameScreen( final MainGame game ) {
		this.game = game;
		stage = new Stage( );
		batch = new SpriteBatch( );

		// Setup the click listeners
		stage.addListener( new ClickListener( ) {
			@Override
			public boolean touchDown( InputEvent event, float x, float y, int pointer, int button ) {
				currPos.set( x, y );
				lastPos.set( currPos );
				deltaPos.set( 0, 0 );

				ppmPos.set(
					game.camera.position.x + ( x - game.windowDim.x / 2 ) / game.windowDim.x * game.ppmDim.x,
					game.camera.position.y + ( y - game.windowDim.y / 2 ) / game.windowDim.y * game.ppmDim.y
				);

				if( !isPaused ) {
					for( BasicShape shape : listDynamic ) {
						if( shape.isPointInShape( ppmPos ) ) {
							if( shape.isObstacle( ) ) {
								if( shape.getNumClickBreak( ) > 0 ) {
									if( !isMuted ) {
										game.weakenSound.play( 1.0f );
									}

									shape.setNumClickBreak( shape.getNumClickBreak( ) - 1 );
									break;
								}
							}

							if( shape.isSuper( ) ) {
								game.leaderBoard.entryLast.score -= penaltySuper;
								timeSuperRemaining = timeSuper;
							}

							if( !isMuted ) {
								game.breakSound.play( 0.5f );
							}

							particleBreakShape( shape );
							shape.clearBody( game.world );
							listDynamic.remove( shape );

							break;
						}
					}
				}

				return super.touchDown( event, x, y, pointer, button );
			}

			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				super.touchUp( event, x, y, pointer, button );

				currPos.set( x, y );
				lastPos.set( currPos );
				deltaPos.set( 0, 0 );

				ppmPos.set(
					game.camera.position.x + ( x - game.windowDim.x / 2 ) / game.windowDim.x * game.ppmDim.x,
					game.camera.position.y + ( y - game.windowDim.y / 2 ) / game.windowDim.y * game.ppmDim.y
				);
			}
		} );

		// Setup Table
		rootTable = new Table(  );
		rootTable.setFillParent( true );
		rootTable.top();

		// Setup Labels/Buttons
		scoreLabel = new Label( "Score: ", game.labelStyle );
		scoreLabel.setFontScale( 2.0f );
		rootTable.add( scoreLabel ).expandX().width( 300 ).height( 100 ).left().padLeft( 50 );

		pauseButton = new TextButton( "Pause", game.skinUI );
		pauseButton.getLabel().setFontScale( 1.5f );
		pauseButton.addListener( new ClickListener(  ){
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				isPaused = true;
				pauseTable.setVisible( true );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		rootTable.add( pauseButton ).expandX().width( 300 ).height( 100 ).padTop( 50 );

		timeLabel = new Label( "Time: ", game.labelStyle );
		timeLabel.setFontScale( 2.0f );
		rootTable.add( timeLabel ).expandX().width( 300 ).height( 100 ).right().padRight( 50 );

		stage.addActor( rootTable );

		// Pause Menu Table
		pauseTable = new Table(  );
		pauseTable.center();
		pauseTable.setFillParent( true );

		resumeButton = new TextButton( "Resume", game.skinUI );
		resumeButton.getLabel().setFontScale( 1.5f );
		resumeButton.addListener( new ClickListener(  ){
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				isPaused = false;
				pauseTable.setVisible( false );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		pauseTable.add( resumeButton ).expandX().width( 300 ).height( 100 ).padBottom( 50 ).row();

		restartButton = new TextButton( "Restart", game.skinUI );
		restartButton.getLabel().setFontScale( 1.5f );
		restartButton.addListener( new ClickListener(  ){
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				clearBoard();
				show( );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		pauseTable.add( restartButton ).expandX().width( 300 ).height( 100 ).padBottom( 50 ).row();

		muteButton = new TextButton( "Mute", game.skinUI );
		muteButton.getLabel().setFontScale( 1.5f );
		muteButton.addListener( new ClickListener(  ){
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				isMuted = !isMuted;
				if( isMuted ) {
					muteButton.setText( "Unmute" );
					game.loopMusic.setVolume( 0.0f );
				}
				else {
					muteButton.setText( "Mute" );
					game.loopMusic.setVolume( 1.0f );
				}

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		pauseTable.add( muteButton ).expandX().width( 300 ).height( 100 ).padBottom( 50 ).row();

		mainMenuButton = new TextButton( "Main Menu", game.skinUI );
		mainMenuButton.getLabel().setFontScale( 1.5f );
		mainMenuButton.addListener( new ClickListener(  ){
			@Override
			public void touchUp( InputEvent event, float x, float y, int pointer, int button ) {
				clearBoard();
				game.setScreen( game.mainMenu );

				super.touchUp( event, x, y, pointer, button );
			}
		} );
		pauseTable.add( mainMenuButton ).expandX().width( 300 ).height( 100 ).padBottom( 50 ).row();

		stage.addActor( pauseTable );

		// Setup Sprite
		starSprite = new Sprite( game.textureStar );
		starSprite.setSize( Globals.sizeBlock, Globals.sizeBlock );

		// Setup the shape lists
		listStatic = new Vector< BasicShape >( );
		listDynamic = new Vector< BasicShape >( );

		// Setup the generation grid
		gridGenerate = new Grid( ( int ) Globals.numBlock, 10 );

		// Particle System
		particleSystem = new ParticleSystem( game );

		// Setup Player
		hexagon = new PlayerShape( game.world, Globals.sizeBlock );
	}

	static final Vector2[] particleOffsets = new Vector2[]{
		new Vector2( Globals.sizeBlock * -0.25f, Globals.sizeBlock * -0.25f ),
		new Vector2( Globals.sizeBlock * 0.25f, Globals.sizeBlock * -0.25f ),
		new Vector2( Globals.sizeBlock * 0.25f, Globals.sizeBlock * 0.25f ),
		new Vector2( Globals.sizeBlock * -0.25f, Globals.sizeBlock * 0.25f )
	};

	public void particleBreakShape( BasicShape shape ) {
		//Particle particle;
		Grid mask = shape.getMask();
		Vector2 pos = new Vector2(  );

		for( int i = 0; i < mask.getDimX(); ++i ) {
			for( int j = 0; j < mask.getDimY(); ++j ) {
				if( !mask.get( i, j ) ) {
					continue;
				}

				for( int k = 0; k < 4; ++k ) {
					if( ( int ) ( Math.random( ) * 2 ) == 0 ) {
						continue;
					}

					pos.set(
						i * Globals.sizeBlock - shape.getMaskOffset( ).x + Globals.sizeBlock * 0.5f + particleOffsets[ k ].x,
						j * Globals.sizeBlock - shape.getMaskOffset( ).y + Globals.sizeBlock * 0.5f + particleOffsets[ k ].y
					);

					pos.set( shape.localToWorldPos( pos ) );

					Particle particle = new Particle( );
					particle.life = ( float ) Math.random() * 0.125f + 0.125f;
					particle.lifeFade = 0.125f;
					particle.pos.set( pos );
					particle.rotation = shape.getAngle();
					particle.color.set( shape.getColor( ) );
					particle.colorOutline.set( shape.getColorOutline() );
					particle.veloc.set(
						( ( float ) Math.random( ) * Globals.sizeBlock - Globals.sizeBlock / 2 ) * 2.0f,
						( ( float ) Math.random( ) * Globals.sizeBlock - Globals.sizeBlock / 2 ) * 2.0f );
					particle.velocRotation = ( float ) Math.random( ) * Globals.sizeBlock - Globals.sizeBlock / 2;
					particle.dim.set( 0.5f * Globals.sizeBlock * 0.5f, 0.5f * Globals.sizeBlock * 0.5f );


					particleSystem.add( particle );
				}
			}
		}
	}

	@Override
	public void show( ) {
		Gdx.input.setInputProcessor( stage );

		isPaused = false;
		isMuted = false;
		pauseTable.setVisible( false );
		game.leaderBoard.entryLast.score = 0;
		game.leaderBoard.entryLast.time = 0;

		hexagon.setColor( game.customizeData.colorSelected );
		hexagon.reshape( game.customizeData.shapeSelected );

		initBoard( );
	}

	private BasicShape getRandShape( ) {
		final int numShapes = 7;
		int rand = ( int ) ( Math.random( ) * numShapes );

		switch( rand ) {
			case 0:
				return new SShape( game.world );
			case 1:
				return new CornerShape( game.world );
			case 2:
				return new UShape( game.world );
			case 3:
				return new RectThreeHShape( game.world );
			case 4:
				return new RectThreeVShape( game.world );
			case 5:
				return new SquareTwoByTwoShape( game.world );
			case 6:
				return new LShape( game.world );
			default:
				return new SquareShape( game.world );
		}
	}

	void genStatic( ) {
		int fails = 0;
		int placements = maxPlacements;
		int x, y;

		gridGenerate.clear( );

		BasicShape shape;

		while( placements > 0 ) {
			if( fails >= maxFails ) {
				fails = 0;
				placements--;
				continue;
			}

			x = ( int ) ( Math.random( ) * ( gridGenerate.getDimX( ) ) );
			y = ( int ) ( Math.random( ) * ( gridGenerate.getDimY( ) ) );

			shape = getRandShape( );

			if( !mask( x, y, gridGenerate, shape.getMask( ) ) ) {
				shape.clearBody( game.world );
				fails++;
				continue;
			}

			shape.setPos(
				x * Globals.sizeBlock + shape.getMaskOffset( ).x,
				yGen * gridGenerate.getDimY( ) * Globals.sizeBlock + y * Globals.sizeBlock + shape.getMaskOffset( ).y
			);

			//shape.offsetColor( );

			if( game.activeMode != MainGame.GameMode.GM_RACE &&
				Math.random( ) * 1000 < percSuper * 1000 ) {

				shape.setSuper( true );
			}
			else if( game.activeMode != MainGame.GameMode.GM_RACE &&
				Math.random( ) * 1000 < percObstacle * 1000 ) {

				shape.setObstacle( true );
			}

			listStatic.add( shape );

			placements--;
			fails = 0;
		}

		for( int i = 0; i < gridGenerate.getDimX( ); ++i ) {
			for( int j = 0; j < gridGenerate.getDimY( ); ++j ) {
				// If we can add a double rect
				shape = new RectTwoHShape( game.world );

				if( mask( i, j, gridGenerate, shape.getMask( ) ) ) {
					shape.setPos(
						i * Globals.sizeBlock + shape.getMaskOffset( ).x,
						yGen * gridGenerate.getDimY( ) * Globals.sizeBlock + j * Globals.sizeBlock + shape.getMaskOffset( ).y
					);

					if( game.activeMode != MainGame.GameMode.GM_RACE &&
						Math.random( ) * 1000 < percSuper * 1000 ) {

						shape.setSuper( true );
					}
					else if( game.activeMode != MainGame.GameMode.GM_RACE &&
						Math.random( ) * 1000 < percObstacle * 1000 ) {

						shape.setObstacle( true );
					}

					listStatic.add( shape );
					continue;
				}

				shape.clearBody( game.world );

				// Else create a single square
				shape = new SquareShape( game.world );

				if( game.activeMode == MainGame.GameMode.GM_JENGA &&
					Math.random( ) * 100 > 50.0f ) {
				}
				else {
					if( mask( i, j, gridGenerate, shape.getMask( ) ) ) {
						shape.setPos(
							i * Globals.sizeBlock + shape.getMaskOffset( ).x,
							yGen * gridGenerate.getDimY( ) * Globals.sizeBlock + j * Globals.sizeBlock + shape.getMaskOffset( ).y
						);

						if( game.activeMode != MainGame.GameMode.GM_RACE &&
							Math.random( ) * 1000 < percSuper * 1000 ) {

							shape.setSuper( true );
						}
						else if( game.activeMode != MainGame.GameMode.GM_RACE &&
							Math.random( ) * 1000 < percObstacle * 1000 ) {

							shape.setObstacle( true );
						}

						listStatic.add( shape );
						continue;
					}
				}

				shape.clearBody( game.world );
			}
		}
	}

	private void initBoard( ) {
		timeSuperRemaining = 0.0f;

		hexagon.resetForces( );
		hexagon.setPos(
			gridGenerate.getDimX( ) * 0.5f * Globals.sizeBlock,
			Globals.sizeBlock
		);

		yGen = 0;
		yCheckpoint = ( int ) ( hexagon.getPos( ).y / ( gridGenerate.getDimY() * Globals.sizeBlock ) );

		starSprite.setPosition(
			Globals.sizeBlock * -2.0f,
			( yCheckpoint  - 1 ) * gridGenerate.getDimY() * Globals.sizeBlock );

		yScore = ( int ) ( hexagon.getPos( ).y / Globals.sizeBlock );
	}

	private void clearBoard( ) {
		for( BasicShape shape : listDynamic ) {
			shape.clearBody( game.world );
		}

		for( BasicShape shape : listStatic ) {
			shape.clearBody( game.world );
		}

		listDynamic.clear( );
		listStatic.clear( );
	}

	private void checkBounds( ) {
		float midX = gridGenerate.getDimX( ) * Globals.sizeBlock * 0.5f;
		float boundsX;

		BasicShape shape;
		Vector2 pos;

		if( game.activeMode == MainGame.GameMode.GM_RACE &&
			game.leaderBoard.entryLast.time >= game.timeRace ) {

			clearBoard( );
			game.setScreen( game.summaryScreen );
		}

		if( game.activeMode == MainGame.GameMode.GM_CHALLENGE && (
			game.leaderBoard.entryLast.time >= game.timeRace ||
			game.leaderBoard.entryLast.score >= game.goalRace ) ) {

			clearBoard( );
			game.setScreen( game.summaryScreen );
		}

		pos = hexagon.getPos( );
		boundsX = gridGenerate.getDimX( ) * Globals.sizeBlock * 0.5f + 1.0f * Globals.sizeBlock;

		if( pos.x < midX - boundsX ||
			pos.x > midX + boundsX ) {

			clearBoard( );
			game.setScreen( game.summaryScreen );
		}

		boundsX = gridGenerate.getDimX( ) * Globals.sizeBlock * 0.5f + 3.0f * Globals.sizeBlock;

		for( Iterator< BasicShape > iter = listDynamic.iterator( ); iter.hasNext( ); ) {
			shape = iter.next( );
			pos = shape.getPos( );

			if( pos.x < midX - boundsX ||
				pos.x > midX + boundsX ) {

				shape.clearBody( game.world );
				iter.remove( );
			}
		}
	}

	private boolean mask( int x, int y, Grid grid, Grid mask ) {
		if( !grid.canMask( x, y, mask ) ) return false;

		grid.setMask( x, y, mask );
		return true;
	}

	private void checkSuper( ) {
		if( timeSuperRemaining > 0.0f ) {
			timeSuperRemaining -= timeStep;

			Iterator< BasicShape > iter = listDynamic.iterator( );
			BasicShape shape;
			while( iter.hasNext( ) ) {
				shape = iter.next( );

				if( shape.getPos( ).y > hexagon.getPos( ).y - Globals.sizeBlock * 4.0f ) {

					if( !isMuted ) {
						game.breakSound.play( 0.5f );
					}

					particleBreakShape( shape );
					shape.clearBody( game.world );
					iter.remove( );
				}
			}
		}
	}

	private void checkScore( ) {
		int yScoreNext = ( int ) ( hexagon.getPos( ).y / Globals.sizeBlock );
		if( yScoreNext < yScore ) {
			game.leaderBoard.entryLast.score += yScore - yScoreNext;
			yScore = yScoreNext;
		}

		int yCheckpointNext = ( int ) ( hexagon.getPos( ).y / ( gridGenerate.getDimY() * Globals.sizeBlock ) );
		if( yCheckpointNext < yCheckpoint ) {
			game.leaderBoard.entryLast.score += gridGenerate.getDimY() / 2;
			yCheckpoint = yCheckpointNext;
			for( int i = 0; i < 5; ++i ) {
				Particle particle = new Particle();
				particle.life = ( float ) Math.random() * 0.25f + 0.25f;
				particle.lifeFade = 0.25f;
				particle.pos.set(
					starSprite.getX() + Globals.sizeBlock * 0.5f,
					starSprite.getY() + Globals.sizeBlock * 0.5f );
				particle.rotation = ( float ) Math.random( ) * 360;
				particle.color.set( Color.YELLOW );
				particle.colorOutline.set( Color.ORANGE );
				particle.veloc.set(
					( ( float ) Math.random( ) * Globals.sizeBlock - Globals.sizeBlock / 2 ) * 4.0f,
					( ( float ) Math.random( ) * Globals.sizeBlock - Globals.sizeBlock / 2 ) * 4.0f );
				particle.velocRotation = ( float ) Math.random( ) * Globals.sizeBlock - Globals.sizeBlock / 2;
				particle.dim.set( 0.5f * Globals.sizeBlock * 0.5f, 0.5f * Globals.sizeBlock * 0.5f );

				particleSystem.add( particle );
			}

			starSprite.setPosition(
				Globals.sizeBlock * -2.0f,
				( yCheckpoint  - 1 ) * gridGenerate.getDimY() * Globals.sizeBlock );
		}

		scoreLabel.setText( "Score: " + game.leaderBoard.entryLast.score );
		if( game.activeMode == MainGame.GameMode.GM_RACE || game.activeMode == MainGame.GameMode.GM_CHALLENGE ) {
			timeLabel.setText( "Time: " + ( int ) ( ( game.timeRace - game.leaderBoard.entryLast.time ) * 10.0f ) / 10.0f );
		}
		else {
			timeLabel.setText( "Time: " + ( int ) ( game.leaderBoard.entryLast.time * 10.0f ) / 10.0f );
		}
	}

	private void checkGen( ) {
		int yGenNext = ( int ) (
			( game.camera.position.y - game.ppmDim.y * 1.5f - gridGenerate.getDimY( ) * Globals.sizeBlock * 0.5f ) /
				( gridGenerate.getDimY( ) * Globals.sizeBlock )
		);

		while( yGenNext < yGen ) {
			yGen--;

			for( BasicShape shape : listStatic ) {
				if( !shape.isObstacle( ) ) {
					shape.setDynamic( );
				}

				listDynamic.add( shape );
			}

			listStatic.clear( );
			genStatic( );
		}
	}

	public void update( float delta ) {
		if( isPaused )
			return;

		accumTime += delta;

		if( accumTime >= timeStep ) {
			accumTime -= timeStep;

			checkBounds( );
			checkSuper( );

			game.world.step( timeStep * 2.0f, 16, 8 );
			game.world.clearForces( );

			particleSystem.update( timeStep );

			game.camera.position.set(
				gridGenerate.getDimX( ) * Globals.sizeBlock * 0.5f,
				hexagon.getPos( ).y,
				0 );

			game.camera.update( );

			game.leaderBoard.entryLast.time += timeStep;
			checkScore( );
			checkGen( );
		}
	}

	@Override
	public void render( float delta ) {
		update( delta );

		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

		batch.setProjectionMatrix( game.camera.combined );
		batch.enableBlending();
		batch.begin();
		batch.setBlendFunction( GLES10.GL_SRC_ALPHA, GLES10.GL_ONE_MINUS_SRC_ALPHA );
		starSprite.draw( batch );
		batch.end( );

		game.shapeRenderer.setProjectionMatrix( game.camera.combined );
		game.shapeRenderer.begin( ShapeRenderer.ShapeType.Filled );

		Gdx.gl.glEnable( GLES10.GL_BLEND);
		Gdx.gl.glBlendFunc(GLES10.GL_SRC_ALPHA, GLES10.GL_ONE_MINUS_SRC_ALPHA);

		particleSystem.render();

		Gdx.gl.glDisable( GLES10.GL_BLEND );

		for( BasicShape shape : listStatic ) {
			shape.render( game.shapeRenderer );
		}

		for( BasicShape shape : listDynamic ) {
			shape.render( game.shapeRenderer );
		}

		hexagon.render( game.shapeRenderer );

		game.shapeRenderer.end( );

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
		game.world.dispose( );
	}
}
