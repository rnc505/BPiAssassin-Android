package com.pankaj.assassins;

import java.io.Serializable;

import org.opencv.core.Mat;

import android.util.Base64;
import android.util.Log;

public class MugshotFiles implements Serializable {
	
	private static final String    TAG                 = "MugshotFiles";
	private static final int    FACES = PankajUtils.MAX_FACES;
	
	String codeName;
	String phoneNumber;
	String emailAddress;

	
	int [] cols;
        int [] rows;
        int [] type;
    
	String [] images;
	
	public MugshotFiles() {
		codeName = new String();
		phoneNumber = new String();
		emailAddress = new String();
		cols = new int[FACES];
	    rows = new int[FACES];
	    type = new int[FACES];
	    
		images = new String [FACES];
    }
	
	public  void matToString (int  numberofFacesCollected, Mat mat) {
		
		int index = numberofFacesCollected-1;
		
		if(mat.isContinuous()){
	        cols[index] = mat.cols();
	        rows[index] = mat.rows();
	        type[index] = mat.type();
	        int elemSize = (int) mat.elemSize();    

	        byte[] data = new byte[cols[index] * rows[index] * elemSize];

	        mat.get(0, 0, data);
	        images[index] = new String(Base64.encode(data, Base64.DEFAULT));
	    } else {
	        Log.e(TAG, "Mat not continuous.");
	    }
    }
	
    public  Mat stringToMat (int  numberofFacesCollected) {

	int index = numberofFacesCollected - 1;
		
    byte[] data = Base64.decode(images[index].getBytes(), Base64.DEFAULT); 

    Mat mat = new Mat(rows[index], cols[index], type[index]);
    mat.put(0, 0, data);

    return mat;
    
    
    }
}


