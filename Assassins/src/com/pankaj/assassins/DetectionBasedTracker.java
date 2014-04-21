package com.pankaj.assassins;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

import android.util.Log;

public class DetectionBasedTracker
{
    private static final String    TAG                 = "DetectionBasedTracker";
	///To make faster remove all the include log files and Log statements from the cpp file  

    public DetectionBasedTracker() {
    	
    }

    public void initDetectors(String pathVariable) {
    	nativeInitLoaders(pathVariable);
    }
    public int collect(Mat cameraFrameBGR, Mat displayFrameBGR) {
        	
       return nativeCollect(cameraFrameBGR.getNativeObjAddr(), displayFrameBGR.getNativeObjAddr());
    }

    public int train() {
    	
       return nativeTrain();
    }

    public int recognize(Mat cameraFrameBGR, Mat displayFrameBGR) {
    	
       return nativeRecognize(cameraFrameBGR.getNativeObjAddr(), displayFrameBGR.getNativeObjAddr());
    }

    public void reset() {
    	nativeReset();
    }
    
    private static native void nativeInitLoaders(String pathVariable);
    private static native int nativeCollect(long inputImage, long outputimage);
    private static native int nativeTrain();
    private static native int nativeRecognize(long inputImage, long outputimage);
	
    private static native void nativeReset();
     

}
