package com.mygdx.game;

import android.content.Context;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import com.mygdx.game.data.CustomizeData;
import com.mygdx.game.data.LeaderBoardData;
import com.mygdx.game.data.ScoreEntry;
import com.mygdx.game.globals.Globals;
import com.mygdx.game.screens.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.spec.ECParameterSpec;

public class MainGame extends Game {
	// Game Mode data
	public enum GameMode {
		GM_NORMAL,
		GM_JENGA,
		GM_RACE,
		GM_CHALLENGE
	};

	public float timeRace;
	public int goalRace;
	public int rewardRace;

	public GameMode activeMode;

	// File data
	String fileName;

	// Logger/Renderers
	public FPSLogger logger;
	public Box2DDebugRenderer debugRenderer;
	public ShapeRenderer shapeRenderer;

	// Camera
	public OrthographicCamera camera;

	// World
	public World world;

	// Window
	public float aspect;
	public Vector2 windowDim;
	public Vector2 ppmDim;

	// Textures
	public TextureAtlas textureAtlas;
	public Skin skinUI;
	public Skin skin;
	public BitmapFont bitmapFont;
	public Texture textureStar;

	// Button/Label Styles
	public TextButtonStyle textButtonStyle;
	public LabelStyle labelStyle;

	// Data
	public LeaderBoardData leaderBoard;
	public CustomizeData customizeData;

	// Sounds
	public Music loopMusic;

	public Sound breakSound;
	public Sound weakenSound;

	// Screens
	public MainMenuScreen mainMenu;
	public GameScreen gameScreen;
	public CustomizeScreen customizeScreen;
	public SummaryScreen summaryScreen;
	public RaceScreen raceScreen;
	public ChallengeScreen challengeScreen;
	public ChallengeDescScreen challengeDescScreen;

	public MainGame( ) {
	}

	@Override
	public void create( ) {
		// File data
		fileName = "saveData";

		//Setup Logger
		logger = new FPSLogger( );

		// Setup Renderers
		debugRenderer = new Box2DDebugRenderer( );
		shapeRenderer = new ShapeRenderer( );

		// Setup the world
		world = new World( new Vector2( 0, -32.17f ), false );

		// Setup initial windows dimensions
		windowDim = new Vector2( Gdx.graphics.getWidth( ), Gdx.graphics.getHeight( ) );
		aspect = windowDim.y / windowDim.x;
		ppmDim = new Vector2( Globals.PPM, Globals.PPM * aspect );

		// Setup the camera
		camera = new OrthographicCamera( ppmDim.x, ppmDim.y );
		camera.position.set( Globals.sizeBlock * Globals.numBlock / 2.0f, 0.0f, 0.0f );
		camera.update( );

		// Setup Texture/Skins/Fonts
		textureAtlas = new TextureAtlas( Gdx.files.internal( "button.pack" ) );
		skinUI = new Skin( Gdx.files.internal( "uiskin.json" ) );
		skin = new Skin( textureAtlas );
		bitmapFont = new BitmapFont( Gdx.files.internal( "font.fnt" ), false );
		textureStar = new Texture( Gdx.files.internal( "star.bmp" ) );

		// Setup Styles
		labelStyle = new LabelStyle( bitmapFont, Color.RED );
		textButtonStyle = new TextButtonStyle(
			skin.getDrawable( "button" ),
			skin.getDrawable( "buttonpressed" ),
			null,
			bitmapFont
		);

		// Setup Data
		leaderBoard = new LeaderBoardData( );
		customizeData = new CustomizeData( );

		// Sounds
		loopMusic = Gdx.audio.newMusic( Gdx.files.internal( "gameloop.ogg" ) );

		breakSound = Gdx.audio.newSound( Gdx.files.internal( "break.ogg" ) );
		weakenSound = Gdx.audio.newSound( Gdx.files.internal( "weaken.ogg" ) );

		// Setup Screens
		mainMenu = new MainMenuScreen( this );
		gameScreen = new GameScreen( this );
		customizeScreen = new CustomizeScreen( this );
		summaryScreen = new SummaryScreen( this );
		raceScreen = new RaceScreen( this );
		challengeScreen = new ChallengeScreen( this );
		challengeDescScreen = new ChallengeDescScreen( this );

		//File file = new File( AndroidLauncher.getAppContext().getFilesDir(), fileName );
		//file.delete();

		loadFileData( );

		loopMusic.play( );
		loopMusic.setLooping( true );

		// Set Current Screen
		setScreen( mainMenu );
	}

	@Override
	public void render( ) {
		super.render( );
	}

	@Override
	public void dispose( ) {

	}

	public boolean fileExists( ) {
		File file = new File( AndroidLauncher.getAppContext( ).getFilesDir( ), fileName );
		return file.exists( );
	}

	public void createFile( ) {
		File file = new File( AndroidLauncher.getAppContext( ).getFilesDir( ), fileName );
		try {
			file.createNewFile( );
			saveFileData( );
		} catch( Exception e ) {
			e.printStackTrace( );
		}
	}

	public void loadFileData( ) {
		if( !fileExists( ) ) {
			createFile( );
			return;
		}

		FileInputStream inFile;
		ObjectInputStream inObject;

		try {
			inFile = AndroidLauncher.getAppContext( ).openFileInput( fileName );
			inObject = new ObjectInputStream( inFile );

			int numEntry = inObject.readInt( );

			for( int i = 0; i < numEntry; ++i ) {
				leaderBoard.addEntry( new ScoreEntry(
					inObject.readInt( ),
					inObject.readFloat( )
				) );
			}

			customizeData.numPoints = inObject.readInt( );
			int numShapes = inObject.readInt( );
			int numColors = inObject.readInt( );

			if( numShapes != customizeData.numShapes ||
				numColors != customizeData.numColors ) {
				throw new Exception( "Number of Shapes/Colors mismatch!" );
			}

			for( int i = 0; i < customizeData.numShapes; ++i ) {
				customizeData.shapeUnlocked[ i ] = inObject.readBoolean( );
			}

			for( int i = 0; i < customizeData.numColors; ++i ) {
				customizeData.colorUnlocked[ i ] = inObject.readBoolean( );
			}

			customizeData.shapeSelected = inObject.readInt( );

			customizeData.colorSelected.set(
				inObject.readFloat( ),
				inObject.readFloat( ),
				inObject.readFloat( ),
				inObject.readFloat( )
			);

			for( int i = 0; i < challengeScreen.numChallenges; ++i ) {
				challengeScreen.isChallenge[ i ] = inObject.readBoolean( );
			}

			inObject.close( );
			inFile.close( );
		} catch( Exception e ) {
			e.printStackTrace( );
		}
	}

	public void saveFileData( ) {
		FileOutputStream outFile;
		ObjectOutputStream outObject;

		try {
			outFile = AndroidLauncher.getAppContext( ).openFileOutput( fileName, Context.MODE_PRIVATE );
			outObject = new ObjectOutputStream( outFile );

			outObject.writeInt( leaderBoard.entryList.size( ) );

			for( ScoreEntry entry : leaderBoard.entryList ) {
				outObject.writeInt( entry.score );
				outObject.writeFloat( entry.time );
			}

			outObject.writeInt( customizeData.numPoints );
			outObject.writeInt( customizeData.numShapes );
			outObject.writeInt( customizeData.numColors );

			for( int i = 0; i < customizeData.numShapes; ++i ) {
				outObject.writeBoolean( customizeData.shapeUnlocked[ i ] );
			}

			for( int i = 0; i < customizeData.numColors; ++i ) {
				outObject.writeBoolean( customizeData.colorUnlocked[ i ] );
			}

			outObject.writeInt( customizeData.shapeSelected );
			outObject.writeFloat( customizeData.colorSelected.r );
			outObject.writeFloat( customizeData.colorSelected.g );
			outObject.writeFloat( customizeData.colorSelected.b );
			outObject.writeFloat( customizeData.colorSelected.a );

			for( int i = 0; i < challengeScreen.numChallenges; ++i ) {
				outObject.writeBoolean( challengeScreen.isChallenge[ i ] );
			}

			outObject.close( );
			outFile.close( );
		} catch( Exception e ) {
			e.printStackTrace( );
		}
	}
}
