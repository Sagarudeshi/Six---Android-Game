package com.mygdx.game.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.globals.Globals;
import com.mygdx.game.data.Grid;



public class SquareShape extends BasicShape {

	public SquareShape( World world ) {
		super( world );

		setColor( Color.TAN );

		vertices = new float[]{
			Globals.sizeBlock * -0.5f, Globals.sizeBlock * -0.5f,
			Globals.sizeBlock * 0.5f, Globals.sizeBlock * -0.5f,
			Globals.sizeBlock * 0.5f, Globals.sizeBlock * 0.5f,
			Globals.sizeBlock * -0.5f, Globals.sizeBlock * 0.5f
		};

		PolygonShape shape = new PolygonShape( );
		shape.set( vertices );

		Fixture fixture = body.createFixture( shape, Globals.densityDefault );
		fixture.setFriction( Globals.frictionDefault );
		fixture.setRestitution( Globals.restitutionDefault );

		mask = new Grid( new boolean[][]{
			{ true }
		} );

		maskOffset = new Vector2(
			Globals.sizeBlock * 0.5f, Globals.sizeBlock * 0.5f );
	}

	public void render( ShapeRenderer renderer ) {
		super.render( renderer );

		renderer.rect(
			Globals.sizeBlock * -0.5f, Globals.sizeBlock * -0.5f,
			Globals.sizeBlock * 1.0f, Globals.sizeBlock * 1.0f );

		renderer.setColor( colorOutline );

		renderer.line( vertices[ 0 ], vertices[ 1 ], vertices[ 2 ], vertices[ 3 ] );
		renderer.line( vertices[ 2 ], vertices[ 3 ], vertices[ 4 ], vertices[ 5 ] );
		renderer.line( vertices[ 4 ], vertices[ 5 ], vertices[ 6 ], vertices[ 7 ] );
		renderer.line( vertices[ 6 ], vertices[ 7 ], vertices[ 0 ], vertices[ 1 ] );
	}

	public BasicShape create( World world ) {
		return new SquareShape( world );
	}
}
