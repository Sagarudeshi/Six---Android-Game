package com.mygdx.game.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.globals.Globals;
import com.mygdx.game.data.Grid;



public class RectThreeHShape extends BasicShape {

	public RectThreeHShape( World world ) {
		super( world );

		color.set( Color.RED );

		vertices = new float[]{
			Globals.sizeBlock * -1.5f, Globals.sizeBlock * -0.5f,
			Globals.sizeBlock * 1.5f, Globals.sizeBlock * -0.5f,
			Globals.sizeBlock * 1.5f, Globals.sizeBlock * 0.5f,
			Globals.sizeBlock * -1.5f, Globals.sizeBlock * 0.5f
		};

		PolygonShape shape = new PolygonShape( );
		shape.set( vertices, 0, 4 * 2 );

		Fixture fixture = body.createFixture( shape, Globals.densityDefault );
		fixture.setFriction( Globals.frictionDefault );
		fixture.setRestitution( Globals.restitutionDefault );

		mask = new Grid( new boolean[][]{
			{ true, true, true }
		} );

		maskOffset = new Vector2(
			Globals.sizeBlock * 1.5f, Globals.sizeBlock * 0.5f );
	}

	public void render( ShapeRenderer renderer ) {
		super.render( renderer );

		renderer.rect(
			Globals.sizeBlock * -1.5f, Globals.sizeBlock * -0.5f,
			Globals.sizeBlock * 3.0f, Globals.sizeBlock * 1.0f );

		renderer.setColor( colorOutline );

		renderer.line( vertices[ 0 ], vertices[ 1 ], vertices[ 2 ], vertices[ 3 ] );
		renderer.line( vertices[ 2 ], vertices[ 3 ], vertices[ 4 ], vertices[ 5 ] );
		renderer.line( vertices[ 4 ], vertices[ 5 ], vertices[ 6 ], vertices[ 7 ] );
		renderer.line( vertices[ 6 ], vertices[ 7 ], vertices[ 0 ], vertices[ 1 ] );
	}
}
