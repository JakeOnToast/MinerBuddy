package com.jake.minerbuddy.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.jake.minerbuddy.main.MinerBuddyGame;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
		configuration.useAccelerometer = false;
		configuration.useCompass = false;
		initialize(new MinerBuddyGame(), configuration);

	}


}