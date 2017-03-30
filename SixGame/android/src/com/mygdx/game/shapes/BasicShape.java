package com.mygdx.game.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.data.Grid;

public class BasicShape extends AbstractShape {
	static int numClickLow = 2;
	static int numClickHigh = 5;
	static float offsetOutline = 0.9f;

	private boolean isSuper;
	private boolean isObstacle;

	private int numClickBreak;

	public BasicShape( World world ) {
		super( world );
		isSuper = false;
		isObstacle = false;

		color.set( Color.GRAY );
		colorOutline.set( Color.WHITE );
		body.setType( BodyDef.BodyType.StaticBody );
		body.setTransform( 0, 0, 0 );
	}

	public Color getColorOutline( ) {
		return colorOutline;
	}

	public void setColorOutline( Color color ) {
		colorOutline.set( color );
	}

	public void clearBody( World world ) {
		world.destroyBody( body );
	}

	public void setColor( Color color ) {
		this.color.set( color );
	}

	public Color getColor( ) {
		return color;
	}

	public void setPos( Vector2 pos ) {
		body.setTransform( pos, body.getAngle( ) );
	}

	public void setPos( float x, float y ) {
		body.setTransform( new Vector2( x, y ), body.getAngle( ) );
	}

	public void setPos( Vector2 pos, float angle ) {
		body.setTransform( pos, angle );
	}

	public Vector2 getPos( ) {
		return body.getPosition( );
	}

	public void addAngle( float angle ) {
		body.setTransform( body.getPosition( ), body.getAngle( ) + angle );
	}

	public void setAngle( float angle ) {
		body.setTransform( body.getPosition( ), angle );
	}

	public void resetForces( ) {
		body.setLinearVelocity( 0, 0 );
		body.setAngularVelocity( 0 );
		setAngle( 0 );
	}

	public void setStatic( ) {
		body.setType( BodyDef.BodyType.StaticBody );
	}

	public void setDynamic( ) {
		body.setType( BodyDef.BodyType.DynamicBody );
	}

	public boolean isSuper( ) {
		return isSuper;
	}

	public void setSuper( boolean isSuper ) {
		if( isSuper ) {
			setColor( Color.NAVY );
		}

		this.isSuper = isSuper;
	}

	public boolean isObstacle( ) {
		return isObstacle;
	}

	public void setObstacle( boolean isObstacle ) {
		if( isObstacle ) {
			numClickBreak = ( int ) ( Math.random( ) * ( numClickHigh - numClickLow ) ) + numClickLow;
			setColor( Color.DARK_GRAY );
		}

		this.isObstacle = isObstacle;
	}

	public int getNumClickBreak( ) {
		return numClickBreak;
	}

	public Vector2 localToWorldPos( Vector2 pos ) {
		return body.getWorldPoint( pos );
	}

	public void setNumClickBreak( int numClickBreak ) {
		this.numClickBreak = numClickBreak;
	}

	public Vector2 getMaskOffset( ) {
		return maskOffset;
	}

	public Grid getMask( ) {
		return mask;
	}

	public float getAngle( ) {
		return body.getAngle();
	}

	static final float maxOffsetColor = 1.0f;

	public void offsetColor( ) {
		float offset = 1.0f;
		offset += ( float ) ( Math.random( ) * maxOffsetColor );
		offset -= maxOffsetColor * 0.5f;

		//System.out.println( "Offset: " + offset );

		color.r *= offset;
		color.g *= offset;
		color.b *= offset;
		color.clamp( );
	}

	@Override
	public void render( ShapeRenderer renderer ) {
		renderer.identity();
		renderer.setColor( color );
		renderer.translate( body.getPosition( ).x, body.getPosition( ).y, 0 );
		renderer.rotate( 0, 0, 1, ( float ) Math.toDegrees( body.getAngle( ) ) );
	}

	public boolean isPointInShape( Vector2 point ) {
		point = body.getLocalPoint( point );

		for( int i = 0; i < vertices.length; i += 4 * 2 ) {
			if( Intersector.isPointInPolygon( vertices, i, 4 * 2, point.x, point.y ) )
				return true;
		}

		return false;
	}
}