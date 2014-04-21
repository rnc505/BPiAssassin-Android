LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

include ../../OpenCV-2.4.8-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_SRC_FILES  := com_pankaj_assassins_DetectionBasedTracker.cpp
LOCAL_SRC_FILES  += main.cpp

LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_LDLIBS     += -llog -ldl

LOCAL_MODULE     := detectionBasedTracker

include $(BUILD_SHARED_LIBRARY)
