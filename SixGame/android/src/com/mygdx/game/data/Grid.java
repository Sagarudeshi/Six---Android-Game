package com.mygdx.game.data;

/**
 * Created by Amani on 12/11/2016.
 */

public class Grid {
	private int dimX, dimY;
	private boolean data[][];

	public Grid( int x, int y ) {
		resize( x, y );
	}

	public Grid( boolean[][] data ) {
		dimY = data.length;
		dimX = data[ 0 ].length;

		this.data = data;
	}

	public void resize( int x, int y ) {
		dimX = x;
		dimY = y;

		data = new boolean[ dimY ][ dimX ];
	}

	public void clear( ) {
		for( int i = 0; i < dimY; ++i ) {
			for( int j = 0; j < dimX; ++j ) {
				data[ i ][ j ] = false;
			}
		}
	}

	public void set( int x, int y ) {
		data[ y ][ x ] = true;
	}

	public boolean canMask( int x, int y, Grid mask ) {
		int lx, ly;

		for( int i = 0; i < mask.getDimY( ); ++i ) {
			for( int j = 0; j < mask.getDimX( ); ++j ) {
				if( mask.get( j, i ) == false ) continue;

				lx = x + j;
				ly = y + i;

				if( lx < 0 || ly < 0 || lx >= dimX || ly >= dimY ) return false;

				if( data[ ly ][ lx ] == true ) return false;
			}
		}

		return true;
	}

	public void setMask( int x, int y, Grid mask ) {
		int lx, ly;

		for( int i = 0; i < mask.getDimY( ); ++i ) {
			for( int j = 0; j < mask.getDimX( ); ++j ) {
				if( mask.get( j, i ) == false ) continue;

				lx = x + j;
				ly = y + i;

				if( lx < 0 || ly < 0 || lx >= dimX || ly >= dimY ) continue;

				data[ ly ][ lx ] = true;
			}
		}
	}

	public boolean get( int x, int y ) {
		return data[ y ][ x ];
	}

	public int getDimX( ) {
		return dimX;
	}

	public int getDimY( ) {
		return dimY;
	}


	@Override
	public String toString( ) {
		String out = new String( );
		for( int i = 0; i < dimY; i++ ) {
			for( int j = 0; j < dimX; j++ ) {
				out += data[ i ][ j ];

				if( j != dimX - 1 ) out += " ";
			}

			out += "\n";
		}
		return out;
	}
}