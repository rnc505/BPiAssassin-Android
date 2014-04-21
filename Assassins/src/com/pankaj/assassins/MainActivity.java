package com.pankaj.assassins;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {

	static final String MUSIC_DIR = "/music/";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PankajUtils.copyFile(MainActivity.this, "haarcascade_eye_tree_eyeglasses.xml");
		PankajUtils.copyFile(MainActivity.this, "haarcascade_eye.xml");
		PankajUtils.copyFile(MainActivity.this, "haarcascade_frontalface_default.xml");
		PankajUtils.copyFile(MainActivity.this, "lbpcascade_frontalface.xml");
		PankajUtils.startMusic(1, MainActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void registerUser (View button1) {
		Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
		startActivity(intent);
	}
	
	public void playGame (View button2) {
		Intent intent = new Intent(MainActivity.this, GetTargetActivity.class);
		startActivity(intent);
	}
	
	public void distributeGame (View button3) {
		Intent intent = new Intent(MainActivity.this, DistributeGameActivity.class);
		startActivity(intent);
	}
}
