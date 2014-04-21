#include "com_pankaj_assassins_DetectionBasedTracker.h"

#include <opencv2/core/core.hpp>
#include <opencv2/contrib/detection_based_tracker.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>

#include "main.h"

#include <iostream>
#include <string>
#include <vector>

#include <android/log.h>

#define LOG_TAG "Assassins/DetectionBasedTracker"
#define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))

using namespace std;
using namespace cv;

/*
 * Class:     com_pankaj_assassins_DetectionBasedTracker
 * Method:    nativeInitLoaders
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_pankaj_assassins_DetectionBasedTracker_nativeInitLoaders
  (JNIEnv *jenv, jclass je, jstring jpathVariable) {
	try
	    {
		    const jsize len = jenv->GetStringUTFLength(jpathVariable);
		    const char* strChars = jenv->GetStringUTFChars(jpathVariable, 0);
		    initDetectors((char *)strChars);
		    jenv->ReleaseStringUTFChars(jpathVariable, strChars);
	    }
	    catch(cv::Exception& e)
	    {
	        LOGD("PANKAJCHANDnativeInitLoaders caught cv::Exception: %s", e.what());
	        jclass je = jenv->FindClass("org/opencv/core/CvException");
	        if(!je)
	            je = jenv->FindClass("java/lang/Exception");
	        jenv->ThrowNew(je, e.what());
	    }
}

/*
 * Class:     com_pankaj_assassins_DetectionBasedTracker
 * Method:    nativeCollect
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_com_pankaj_assassins_DetectionBasedTracker_nativeCollect
  (JNIEnv *jenv, jclass je, jlong cameraFrameBGR, jlong displayFrameBGR) {
	try
		    {
			 return collect((cameraFrameBGR), (displayFrameBGR));
		    }
		    catch(cv::Exception& e)
		    {
		        LOGD("PANKAJCHANDnativeCollect caught cv::Exception: %s", e.what());
		        jclass je = jenv->FindClass("org/opencv/core/CvException");
		        if(!je)
		            je = jenv->FindClass("java/lang/Exception");
		        jenv->ThrowNew(je, e.what());
		    }

}

/*
 * Class:     com_pankaj_assassins_DetectionBasedTracker
 * Method:    nativeTrain
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_pankaj_assassins_DetectionBasedTracker_nativeTrain
  (JNIEnv *jenv, jclass je) {
	try
		    {
			  return train();
		    }
		    catch(cv::Exception& e)
		    {
		        LOGD("PANKAJCHANDnativeTrain caught cv::Exception: %s", e.what());
		        jclass je = jenv->FindClass("org/opencv/core/CvException");
		        if(!je)
		            je = jenv->FindClass("java/lang/Exception");
		        jenv->ThrowNew(je, e.what());
		    }

}

/*
 * Class:     com_pankaj_assassins_DetectionBasedTracker
 * Method:    nativeRecognize
 * Signature: (JJ)V
 */
JNIEXPORT jint JNICALL Java_com_pankaj_assassins_DetectionBasedTracker_nativeRecognize
  (JNIEnv *jenv, jclass je, jlong cameraFrameBGR, jlong displayFrameBGR) {
	try
		    {
			 return recognize(cameraFrameBGR), (displayFrameBGR));
		    }
		    catch(cv::Exception& e)
		    {
		        LOGD("PANKAJCHANDnativeRecognize caught cv::Exception: %s", e.what());
		        jclass je = jenv->FindClass("org/opencv/core/CvException");
		        if(!je)
		            je = jenv->FindClass("java/lang/Exception");
		        jenv->ThrowNew(je, e.what());
		    }

}

/*
 * Class:     com_pankaj_assassins_DetectionBasedTracker
 * Method:    nativeReset
 * Signature: ()V
 */

JNIEXPORT void JNICALL Java_com_pankaj_assassins_DetectionBasedTracker_nativeReset
  (JNIEnv *, jclass) {
	pankajChandReset();
}
