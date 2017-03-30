package com.mygdx.game;

import android.content.Context;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	private static Context context;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration( );
		initialize( new MainGame( ), config );

		AndroidLauncher.context = getApplicationContext( );
	}

	public static Context getAppContext( ) {
		return AndroidLauncher.context;
	}
}
