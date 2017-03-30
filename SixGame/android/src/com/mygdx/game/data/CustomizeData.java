package com.mygdx.game.data;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Amani on 04/12/2016.
 */

public class CustomizeData {
	public final int costShapes = 300;
	public final int costColors = 100;

	public final int numShapes = 6;
	public final int numColors = 6;

	public final int startPoints = 1000;

	public int shapeSelected;
	public Color colorSelected;
	public int numPoints;

	public final String[] colorStrings = new String[]{
		"Blue", "Red", "Orange",
		"Pink", "Yellow", "White"
	};
	public final Color[] colorColors = new Color[]{
		Color.BLUE, Color.RED, Color.ORANGE,
		Color.PINK, Color.YELLOW, Color.WHITE
	};

	public final String[] shapeStrings = new String[]{
		"Triangle", "Square", "Pentagon",
		"Hexagon", "Heptagon", "Octagon"
	};

	public final int[] shapeSides = new int[]{
		3, 4, 5,
		6, 7, 8
	};

	public boolean[] shapeUnlocked = new boolean[ numShapes ];
	public boolean[] colorUnlocked = new boolean[ numColors ];

	public CustomizeData( ) {
		numPoints = startPoints;
		colorSelected = new Color( );

		for( int i = 0; i < shapeUnlocked.length; ++i ) {
			shapeUnlocked[ i ] = false;
		}

		for( int i = 0; i < colorUnlocked.length; ++i ) {
			colorUnlocked[ i ] = false;
		}

		int i;

		i = 0;
		shapeSelected = 6;
		for( int sides : shapeSides ) {
			if( sides == shapeSelected ) break;
			i++;
		}
		shapeUnlocked[ i ] = true;

		i = 0;
		colorSelected.set( Color.WHITE );
		for( Color color : colorColors ) {
			if( color.equals( colorSelected ) ) break;
			i++;
		}
		colorUnlocked[ i ] = true;
	}
}
