package com.pankaj.assassins;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GetTargetActivity extends Activity {
	private static final String    TAG                 = "GetTargetActivity";
	
	String targetPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_target_layout);
		Log.d(TAG, "onCreate()");
		
		//DELETE THIS
		((EditText) findViewById(R.id.editText1)).setText("/storage/sdcard0/Download/MugShot");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void collectMugshot(View button1) {
		Log.d(TAG, "collectMugshot()");
		targetPath = ((EditText) findViewById(R.id.editText1)).getText().toString();
		if (targetPath.length() == 0) {
	    	//targetPath string is empty
	    	Toast.makeText(this, "Please enter the Absolute Path of the Target's File", Toast.LENGTH_SHORT).show();
	    	
	    }
		else {
			File fRoot = new File(targetPath);
			if(fRoot.exists()) {
			    Intent intent = new Intent(GetTargetActivity.this, TargetAssignmentActivity.class);
			    intent.putExtra("targetPath", targetPath);
			    startActivity(intent);	
			}
			else {
				Toast.makeText(this, "File does not exist on your Filesystem. Please enter the Absolute Path of the Target's File", Toast.LENGTH_SHORT).show();
			}
			
		}
	}


}
