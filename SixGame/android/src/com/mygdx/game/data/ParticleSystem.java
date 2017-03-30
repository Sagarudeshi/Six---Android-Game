package com.mygdx.game.data;

import android.opengl.GLES10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.MainGame;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Amani on 06/12/2016.
 */

public class ParticleSystem {
	private MainGame game;
	private ArrayList< Particle > particleList;

	public ParticleSystem( MainGame game ) {
		this.game = game;
		particleList = new ArrayList< Particle >( );
	}

	public void add( Particle particle ) {
		particleList.add( particle );
	}

	public void update( float delta ) {
		Particle particle;
		Iterator< Particle > iter = particleList.iterator();
		while( iter.hasNext() ) {
			particle = iter.next();
			if( particle.life <= 0 ) {
				iter.remove();
				continue;
			}

			if( particle.life < particle.lifeFade ) {
				particle.color.a = particle.life / particle.lifeFade;
				particle.colorOutline.a = particle.color.a;
			}

			particle.life -= delta;

			particle.pos.x += particle.veloc.x * delta;
			particle.pos.y += particle.veloc.y * delta;

			particle.rotation += particle.velocRotation * delta;
		}
	}

	public void render( ) {
		for( Particle particle : particleList ) {
			game.shapeRenderer.identity();
			game.shapeRenderer.translate( particle.pos.x, particle.pos.y, 0 );
			game.shapeRenderer.rotate( 0, 0, 1, particle.rotation );

			game.shapeRenderer.setColor( particle.color );

			game.shapeRenderer.rect(
				-particle.dim.x, -particle.dim.y,
				particle.dim.x * 2, particle.dim.y * 2 );

			game.shapeRenderer.setColor( particle.colorOutline );

			game.shapeRenderer.line( -particle.dim.x, -particle.dim.y, particle.dim.x, -particle.dim.y );
			game.shapeRenderer.line( particle.dim.x, -particle.dim.y, particle.dim.x, particle.dim.y );
			game.shapeRenderer.line( particle.dim.x, particle.dim.y, -particle.dim.x, particle.dim.y );
			game.shapeRenderer.line( -particle.dim.x, particle.dim.y, -particle.dim.x, -particle.dim.y );
		}
	}
}
