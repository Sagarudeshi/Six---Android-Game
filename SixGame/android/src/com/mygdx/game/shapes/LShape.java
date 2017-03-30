package com.mygdx.game.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.globals.Globals;
import com.mygdx.game.data.Grid;



public class LShape extends BasicShape {

	public LShape( World world ) {
		super( world );

		color.set( Color.SALMON );

		vertices = new float[]{
			Globals.sizeBlock * -1.0f, Globals.sizeBlock * -1.5f,
			Globals.sizeBlock * 0.0f, Globals.sizeBlock * -1.5f,
			Globals.sizeBlock * 0.0f, Globals.sizeBlock * 1.5f,
			Globals.sizeBlock * -1.0f, Globals.sizeBlock * 1.5f,

			Globals.sizeBlock * 0.0f, Globals.sizeBlock * -1.5f,
			Globals.sizeBlock * 1.0f, Globals.sizeBlock * -1.5f,
			Globals.sizeBlock * 1.0f, Globals.sizeBlock * -0.5f,
			Globals.sizeBlock * 0.0f, Globals.sizeBlock * -0.5f,
		};

		PolygonShape shapeLeft = new PolygonShape( );
		shapeLeft.set( vertices, 0, 4 * 2 );

		Fixture fixtureLeft = body.createFixture( shapeLeft, Globals.densityDefault );
		fixtureLeft.setFriction( Globals.frictionDefault );
		fixtureLeft.setRestitution( Globals.restitutionDefault );

		PolygonShape shapeRight = new PolygonShape( );
		shapeRight.set( vertices, 4 * 2, 4 * 2 );

		Fixture fixtureRight = body.createFixture( shapeRight, Globals.densityDefault );
		fixtureRight.setFriction( Globals.frictionDefault );
		fixtureRight.setRestitution( Globals.restitutionDefault );

		mask = new Grid( new boolean[][]{
			{ true, true },
			{ true, false },
			{ true, false }
		} );

		maskOffset = new Vector2(
			Globals.sizeBlock * 1.0f, Globals.sizeBlock * 1.5f );
	}

	public void render( ShapeRenderer renderer ) {
		super.render( renderer );

		renderer.rect(
			Globals.sizeBlock * -1.0f, Globals.sizeBlock * -1.5f,
			Globals.sizeBlock * 1.0f, Globals.sizeBlock * 3.0f );

		renderer.rect(
			Globals.sizeBlock * 0.0f, Globals.sizeBlock * -1.5f,
			Globals.sizeBlock * 1.0f, Globals.sizeBlock * 1.0f );

		renderer.setColor( colorOutline );

		renderer.line( vertices[ 0 ], vertices[ 1 ], vertices[ 10 ], vertices[ 11 ] );
		renderer.line( vertices[ 10 ], vertices[ 11 ], vertices[ 12 ], vertices[ 13 ] );
		renderer.line( vertices[ 12 ], vertices[ 13 ], vertices[ 14 ], vertices[ 15 ] );
		renderer.line( vertices[ 14 ], vertices[ 15 ], vertices[ 4 ], vertices[ 5 ] );
		renderer.line( vertices[ 4 ], vertices[ 5 ], vertices[ 6 ], vertices[ 7 ] );
		renderer.line( vertices[ 6 ], vertices[ 7 ], vertices[ 0 ], vertices[ 1 ] );
	}
}
