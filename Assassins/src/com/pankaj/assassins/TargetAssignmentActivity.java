package com.pankaj.assassins;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TargetAssignmentActivity extends Activity {
	
	public final static String TAG = "TargetAssignmentActivity";
	private static final int    FACES = PankajUtils.MAX_FACES;
	String targetPath = new String();
	int trainingFinished;
	MugshotFiles target;
	private ImageView imageView;
	Bitmap bmp;
	Mat targetImage;
	
	Mat outputBgr; //Dummy Mat object
	DetectionBasedTracker  mNativeDetector = new DetectionBasedTracker();
	
	private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
	      @Override
	      public void onManagerConnected(int status) {
	          switch (status) {
	              case LoaderCallbackInterface.SUCCESS:
	              {
	                  Log.i(TAG, "OpenCV loaded successfully");

	                  System.loadLibrary("detection_based_tracker");
	                  

	                  work();

	                  
	              } break;
	              default:
	              {
	                  super.onManagerConnected(status);
	              } break;
	          }
	      }
	  };

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.target_assignment);
		
		trainingFinished = 0;
				
		Intent intent = getIntent();
		targetPath = intent.getStringExtra("targetPath");
		
		Log.i(TAG, "Finished onCreate()");
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onResume()
	{
	    super.onResume();
	    OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}
	
    public void work() {
    	
    	
    	targetImage = new Mat();
    	
    	target = PankajUtils.fileToMugShot(targetPath);
    	
   			
    	if(target == null) {
    		Log.e(TAG , "Object is NULL");
    	}

    	//Initialize the imageView on the start or restart of the application
    	imageView = (ImageView) findViewById(R.id.imageView1);
    	//Set the imageView in the layout to the Portrait of the Target 
    	Imgproc.cvtColor(target.stringToMat(FACES/2), targetImage, Imgproc.COLOR_BGR2BGRA);
    	bmp = Bitmap.createBitmap(targetImage.cols(), targetImage.rows(), Bitmap.Config.ARGB_8888);
    	Utils.matToBitmap(targetImage, bmp);
    	imageView.setImageBitmap(bmp);
    			
    	
    	outputBgr = new Mat();
    	
    	mNativeDetector.reset();
    	mNativeDetector.initDetectors(PankajUtils.pathVariable);
    	
    			
    	for (int i = 1; i <= FACES; i++) {
    		
    	    mNativeDetector.collect(target.stringToMat(i), outputBgr);
    	}
    	
    	
    	trainingFinished = mNativeDetector.train();
    	
    	
  	  Log.e(TAG, "codeName, phoneNumber, email Address is: "+ target.codeName + target.phoneNumber + target.emailAddress);
    }
    
	public void beginHunt(View button) {
	    Intent intent = new Intent(TargetAssignmentActivity.this, RecognizeActivity.class);
	    if(trainingFinished == 0) {
	    	Toast.makeText(this, "Training in Progress. Please try again", Toast.LENGTH_SHORT).show();
	    	return;
	    }
	    else if(trainingFinished != 0) {
	    	intent.putExtra("codeName", target.codeName);
	    	intent.putExtra("phoneNumber", target.phoneNumber);
	    	intent.putExtra("emailAddress", target.emailAddress);
	        startActivity(intent);
	    }
	}
}
