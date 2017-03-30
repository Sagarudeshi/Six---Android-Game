package com.mygdx.game.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Amani on 06/12/2016.
 */

public class Particle {
	public Vector2 pos;
	public Vector2 dim;
	public Vector2 veloc;

	public float velocRotation;
	public float rotation;

	public float life;
	public float lifeFade;

	public Color color;
	public Color colorOutline;

	public Particle( ) {
		pos = new Vector2( );
		dim = new Vector2( );
		veloc = new Vector2( );

		velocRotation = 0;
		rotation = 0;

		life = 0;
		lifeFade = 0;

		color = new Color( );
		colorOutline = new Color( );
	}
}
