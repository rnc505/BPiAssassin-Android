#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <string>
#include <iostream>

#include "/home/pankaj/android/OpenCV/opencv-2.4.8/include/opencv2/opencv.hpp"

// Include the rest of our code!
#include "detectObject.h"       // Easily detect faces or eyes (using LBP or Haar Cascades).
#include "preprocessFace.h"     // Easily preprocess face images, for face recognition.
#include "recognition.h"     // Train the face recognition system and recognize a person from an image.

//#include "ImageUtils.h"      // Shervin's handy OpenCV utility functions.

//void facerecognition(/*int argc, char *argv[]*/Mat cameraFrameBGR, Mat displayedFrameBGR);
void initDetectors(char *pathName);
int collect(Mat cameraFrame, Mat displayedFrame);
int train();
int recognize(Mat cameraFrame, Mat displayedFrame);
void pankajChandReset();
