package com.pankaj.assassins;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
public class RecognizeActivity extends Activity  implements CvCameraViewListener2 {
	   private static final String    TAG                 = "RecognizeActivity";
	   
       private MediaPlayer m_mediaPlayer;

	   private Mat                    mRgba;
	   private Mat                    mGray;

	   
	   Mat mBgr;
	   Mat outputBgr;
	   Mat displayedFrameBgra;
	   int lockedTarget;
	   String targetCodeName;
	   String targetPhoneNumber;
	   String targetEmailAddress;
       
	   private DetectionBasedTracker  mNativeDetector1 = new DetectionBasedTracker();


	   private CameraBridgeViewBase   mOpenCvCameraView;

	   private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
	       @Override
	       public void onManagerConnected(int status) {
	           switch (status) {
	               case LoaderCallbackInterface.SUCCESS:
	               {
	                   Log.i(TAG, "OpenCV loaded successfully");

	                   System.loadLibrary("detection_based_tracker");
	                   

	                   mOpenCvCameraView.enableView();
	               } break;
	               default:
	               {
	                   super.onManagerConnected(status);
	               } break;
	           }
	       }
	   };

	   public RecognizeActivity() {

	       Log.i(TAG, "Instantiated new " + this.getClass());
	   }

	   /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       Log.i(TAG, "called onCreate");
	       super.onCreate(savedInstanceState);
	       getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	   
	       setContentView(R.layout.recognize_layout);
	       
	       //MUSIC
	       PankajUtils.startMusic(2, RecognizeActivity.this);
			
	       Log.e(TAG, "INTENT 1");

	       Intent intent = getIntent();
	       
	       Log.e(TAG, "INTENT 2");
	       
	       if(intent == null) Log.e(TAG, "INTENT IS NULL");
	       
	       Log.e(TAG, "INTENT 3");
	       
	       
	       targetCodeName = intent.getStringExtra("codeName");
	       targetPhoneNumber = intent.getStringExtra("phoneNumber");
	       targetEmailAddress = intent.getStringExtra("emailAddress");

	       mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);

	       if(PankajUtils.findBackFacingCamera() == -2) {
	     	  mOpenCvCameraView.setCameraIndex(0);  
	       }
	       else {
	     	  mOpenCvCameraView.setCameraIndex(PankajUtils.findBackFacingCamera());
	       }
	       mOpenCvCameraView.setCvCameraViewListener(this);
	   }

	   @Override
	   public void onPause()
	   {
	       super.onPause();
	       if (mOpenCvCameraView != null)
	           mOpenCvCameraView.disableView();
	   }

	   @Override
	   public void onResume()
	   {
	       super.onResume();
	       OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	   }

	   public void onDestroy() {
	       super.onDestroy();
	       if(mOpenCvCameraView != null)
	           mOpenCvCameraView.disableView();
	   }

	   public void onCameraViewStarted(int width, int height) {
	       mGray = new Mat();
	       mRgba = new Mat();
	   	
	       mBgr = new Mat();
	       outputBgr = new Mat();
	       displayedFrameBgra = new Mat();
	       lockedTarget = 0;
	       mNativeDetector1.initDetectors(PankajUtils.pathVariable);
	   }

	   public void onCameraViewStopped() {
	       mGray.release();
	       mRgba.release();
	       
	       mBgr.release();
	       outputBgr.release();
	       displayedFrameBgra.release();
	   }

	   public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

	       mRgba = inputFrame.rgba();
	       mGray = inputFrame.rgba();

	       Log.d(TAG, "SEPARATOR1");
	       Imgproc.cvtColor(mRgba, mBgr, Imgproc.COLOR_BGRA2BGR);
	       Imgproc.cvtColor(mRgba, outputBgr, Imgproc.COLOR_BGRA2BGR);
	       
	     
	      
	      lockedTarget = mNativeDetector1.recognize(mBgr, outputBgr);
	      
	      if(lockedTarget == 1) {
	    	  //Send SMS to target
	    	  SmsManager mySMS = SmsManager.getDefault();
	    	  String destination = targetPhoneNumber;
	    	  if(targetPhoneNumber == null) Log.e(TAG ,"Phone Number is NULL");
	    	  else Log.e(TAG ,"Phone Number is "+ targetPhoneNumber);
	    	  String msg = "Killed Target " + targetCodeName;
	    	  if(destination == null) Log.e(TAG ,"destination is NULL");
	    	  else Log.e(TAG ,"destination is "+ destination);
	    	  mySMS.sendTextMessage(destination, null, msg, null, null);
	    	  
	    	  
	    	  Intent intent = new Intent(RecognizeActivity.this, EndGameActivity.class);
	    	  intent.putExtra("targetCodeName", targetCodeName);
	    	  PankajUtils.incrementScore();
	    	  startActivity(intent);
	      }
	       
	       Imgproc.cvtColor(outputBgr, displayedFrameBgra, Imgproc.COLOR_BGR2BGRA);

	       
	       return displayedFrameBgra;
	   }
}
