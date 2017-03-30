package com.mygdx.game.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.globals.Globals;


public class PlayerShape extends BasicShape {
	private float radius;
	private int sides = 6;

	public PlayerShape( World world, float radius ) {
		super( world );

		body.setType( BodyDef.BodyType.DynamicBody );

		color.set( Color.RED );
		this.radius = radius;

		vertices = new float[ sides * 2 ];
		for( int i = 0; i < sides; ++i ) {
			vertices[ i * 2 + 0 ] = MathUtils.cosDeg( 360.0f / sides * i ) * radius;
			vertices[ i * 2 + 1 ] = MathUtils.sinDeg( 360.0f / sides * i ) * radius;
		}

		PolygonShape shape = new PolygonShape( );
		shape.set( vertices );

		//System.out.println( "ArrayLength: " + vertices.length );
		//System.out.println( "Verts: " + shape.getVertexCount() );

		Fixture fixture = body.createFixture( shape, Globals.densityDefault );
		fixture.setFriction( Globals.frictionDefault );
		fixture.setRestitution( Globals.restitutionDefault );
	}

	public void render( ShapeRenderer renderer ) {
		super.render( renderer );

		for( int i = 0; i < sides - 1; ++i ) {
			renderer.triangle(
				vertices[ i * 2 + 0 ], vertices[ i * 2 + 1 ],
				0, 0,
				vertices[ ( i + 1 ) * 2 + 0 ], vertices[ ( i + 1 ) * 2 + 1 ] );
		}

		renderer.triangle(
			vertices[ ( sides - 1 ) * 2 + 0 ], vertices[ ( sides - 1 ) * 2 + 1 ],
			0, 0,
			vertices[ 0 * 2 + 0 ], vertices[ 0 * 2 + 1 ] );

		renderer.setColor( colorOutline );

		for( int i = 0; i < sides - 1; ++i ) {
			renderer.line(
				vertices[ i * 2 + 0 ], vertices[ i * 2 + 1 ],
				vertices[ ( i + 1 ) * 2 + 0 ], vertices[ ( i + 1 ) * 2 + 1 ] );
		}

		renderer.line(
			vertices[ ( sides - 1 ) * 2 + 0 ], vertices[ ( sides - 1 ) * 2 + 1 ],
			vertices[ 0 * 2 + 0 ], vertices[ 0 * 2 + 1 ] );
	}

	public void reshape( int sides ) {
		this.sides = sides;
		vertices = new float[ sides * 2 ];
		for( int i = 0; i < sides; ++i ) {
			vertices[ i * 2 + 0 ] = MathUtils.cosDeg( 360.0f / sides * i ) * radius;
			vertices[ i * 2 + 1 ] = MathUtils.sinDeg( 360.0f / sides * i ) * radius;
		}
		PolygonShape shape = ( PolygonShape ) body.getFixtureList( ).get( 0 ).getShape( );
		shape.set( vertices );
	}
}
