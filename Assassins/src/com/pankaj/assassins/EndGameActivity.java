package com.pankaj.assassins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EndGameActivity extends Activity {
	
	private int endGameScore = 0;
	private String targetCodeName = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		targetCodeName = intent.getStringExtra("targetCodeName");
		setContentView(R.layout.end_game);
		endGameScore = PankajUtils.returnScore();
		((EditText) findViewById(R.id.editText1)).setText(String.valueOf(endGameScore));
		Toast.makeText(EndGameActivity.this, "Killed Target "+targetCodeName+ "Your Score is "+endGameScore, Toast.LENGTH_SHORT).show();
	}		
	
	public void playAgain(View button1) {
		Intent intent = new Intent(EndGameActivity.this, MainActivity.class);
  	    startActivity(intent);
	}

}
