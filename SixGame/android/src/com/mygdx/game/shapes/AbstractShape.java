package com.mygdx.game.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.data.Grid;



public abstract class AbstractShape {
	protected World world;
	protected Color color;
	protected Color colorOutline;
	protected float[] vertices;
	protected Grid mask;
	protected Vector2 maskOffset;

	protected Body body;

	public AbstractShape( World world ) {
		this.world = world;
		color = new Color( );
		colorOutline = new Color( );
		body = world.createBody( new BodyDef( ) );
	}

	public abstract void render( ShapeRenderer renderer );
}
