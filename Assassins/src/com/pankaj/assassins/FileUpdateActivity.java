package com.pankaj.assassins;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FileUpdateActivity extends Activity {

	String mugshotFolder;
	private EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_update_layout);

		Intent intent = getIntent();
		mugshotFolder = intent.getStringExtra("mugShotFolder");
		
		editText = (EditText) findViewById(R.id.editText1);
        
        editText.setText(mugshotFolder);

	}
	
	public void closeApp(View button) {
		Intent intent = new Intent(FileUpdateActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
}
