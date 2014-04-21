package com.pankaj.assassins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.json.JSONObject;
import org.opencv.core.Mat;

import android.content.Context;
import android.content.res.AssetManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

public class PankajUtils {
	
	public final static String TAG = "PankajUtils";
	
	public static String pathVariable = "";
	
	public final static int MAX_FACES = 5;
	
	public final static int SONG_1 = 1;
	public final static int SONG_2 = 2;
	private static int currentSong = 0;
	private static int score = 0;
	
	static MediaPlayer m_mediaPlayer= new MediaPlayer();
	
	public static String mugShotToFile(MugshotFiles mugshot) {
		
		File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	    root.mkdirs();	      
        File output = new File(root, "MugShot");
	      
	    if (output.exists()) {
	        output.delete();
	    }
	    
	    FileOutputStream fos = null;
	    
		if (isExternalStorageWritable() == true) {
			Log.e(TAG, "Can WRITE");
		}
		
                if (isExternalStorageReadable() == true) {
        	Log.e(TAG, "Can READ");
		}
        
	    try{
	       	fos=new FileOutputStream(output.getPath());
	    	
	       	ObjectOutputStream oos = new ObjectOutputStream(fos);   
			oos.writeObject(mugshot);
			oos.close();
			
			fos.close();
		   }
		    catch (FileNotFoundException e) {
		    	Log.e(TAG, "File Not Found Exception1");
		    	e.printStackTrace();
	        }
	        catch (IOException e) {
		    	Log.e(TAG, "IOException1");
		    	e.printStackTrace();
		    }
	    return output.getAbsolutePath();
	    
	}
	
	
	public static MugshotFiles fileToMugShot(String targetPath) {
		MugshotFiles mFile = new MugshotFiles();
		FileInputStream fis = null;
		
		if (isExternalStorageWritable() == true) {
			Log.e(TAG, "Can WRITE2");
		}
		
                if (isExternalStorageReadable() == true) {
        	Log.e(TAG, "Can READ2");
		}
        
		try{
	       	    fis=new FileInputStream(targetPath);
	       	
	    	    ObjectInputStream ois = new ObjectInputStream(fis);
	    	
		    mFile = (MugshotFiles)ois.readObject();
			
		    ois.close();
	            fis.close();
                }
		catch (FileNotFoundException e) {
		   	Log.e(TAG, "File Not Found Exception2");
	        }
	    	catch (IOException e) {
		    Log.e(TAG, "IOException2");
		    e.printStackTrace();
		}
		catch(ClassNotFoundException cne) {
			Log.e(TAG, "ClassNotFoundException2");
		}
		return mFile;
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}

	public static int findFrontFacingCamera() {
	    int cameraId = -2;
	    // Search for the front facing camera
	    int numberOfCameras = Camera.getNumberOfCameras();
	    for (int i = 0; i < numberOfCameras; i++) {
	        CameraInfo info = new CameraInfo();
	        Camera.getCameraInfo(i, info);
	        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	            Log.d(TAG, "Front Camera found");
	            cameraId = i;
	            break;
	        }
	    }
	      return cameraId;
    }
	
	public static int findBackFacingCamera() {
	    int cameraId = -2;
	    // Search for the front facing camera
	    int numberOfCameras = Camera.getNumberOfCameras();
	    for (int i = 0; i < numberOfCameras; i++) {
	        CameraInfo info = new CameraInfo();
	        Camera.getCameraInfo(i, info);
	        if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
	            Log.d(TAG, "Back Camera found");
	            cameraId = i;
	            break;
	        }
	    }
	      return cameraId;
    }
	
	public static void startMusic(int song, Context context) {
		switch(song) {
		    case SONG_1:
			    if(currentSong != SONG_1) {
				    stopMusic();
			    	try {
			    		m_mediaPlayer = MediaPlayer.create(context, R.raw.bb);
			    	}
	    	    	catch(Exception e) {
			    	}
			    	m_mediaPlayer.start();
			    	currentSong = SONG_1;
                }
		        break;
		    
		    case SONG_2:
		    	if(currentSong != SONG_2) {
		        	stopMusic();
		    	    try {
		    		    m_mediaPlayer = MediaPlayer.create(context, R.raw.aa);
		    	    }
    	        	catch(Exception e) {
    	        	}	
			    }
		    	m_mediaPlayer.start();
		    	currentSong = SONG_2;
		        break;
		
		default:
			stopMusic();
			try {
				m_mediaPlayer = MediaPlayer.create(context, R.raw.aa);
			}
    		catch(Exception e) {
				
			}
			m_mediaPlayer.start();
			currentSong = SONG_2;
		}
	}
	
	public static void stopMusic () {
		if(m_mediaPlayer.isPlaying()) {
	        m_mediaPlayer.stop();
	    }

	}
	
	public static void copyFile(Context context, String fileName) 
	{
	    AssetManager assetManager = context.getAssets();

	    InputStream in = null;
	    OutputStream out = null;
	    
	    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	    root.mkdirs();	      
        File output = new File(root, fileName);
	      
	    if (output.exists()) {
	        output.delete();
	    }
	    
	    FileOutputStream fos = null;
	    
	    try 
	    {
	        in = assetManager.open(fileName);
	        
			if (isExternalStorageWritable() == true) {
				Log.e(TAG, "Can WRITE");
			}
			
	        if (isExternalStorageReadable() == true) {
	        	Log.e(TAG, "Can READ");
			}
	        
	        fos = new FileOutputStream(output.getPath());

	        byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1) 
	        {
	            fos.write(buffer, 0, read);
	        }
	        in.close();
	        in = null;
	        fos.flush();
	        fos.close();
	        fos = null;
	    }
	    catch (IOException ioe) {
	        ioe.printStackTrace();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    finally{
	        if(in!=null){
	            try {
	                in.close();
	            } catch (IOException e) {
	                Log.e(TAG, "Exception while closing input stream");
	            }
	        }
	        if(out!=null){
	            try {
	                out.close();
	            } catch (IOException e) {
	                Log.e(TAG, "Exception while closing output stream");
	            }
	        }
	        if(fos != null){
	            try {
	                fos.close();
	            } catch (IOException e) {
	                Log.e(TAG, "Exception while closing file stream");
	            }
	        }
	    }
	    pathVariable = root.getAbsolutePath();
	}
	
	static void incrementScore() {
		score++;
	}
	
	static int returnScore() {
		return score;
	}
}
