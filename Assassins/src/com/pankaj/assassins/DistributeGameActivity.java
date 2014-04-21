package com.pankaj.assassins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DistributeGameActivity extends Activity {
	private static final String    TAG                 = "DistributeGameActivity";
	
	String folderPath;
	
	//These are shuffled and shown on the left hand side
	String[] emailAddresses;
	String[] codeNames;

	//These are not shuffled and shown on the right hand side
	String[] targetCodeNames;
	
	File [] listOfFiles;
	
	boolean shuffleComplete = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.distribute_game_layout);
		Log.d(TAG, "onCreate()");
		
		//DELETE THIS
		((EditText) findViewById(R.id.editText1)).setText("/storage/sdcard0/Pictures");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void backToMainPage (View button2) {
		Intent intent = new Intent(DistributeGameActivity.this, MainActivity.class);
	    startActivity(intent);	
	}
	
	public void distributeGameFiles(View button1) {
		Log.d(TAG, "distributeGameFiles()");
		
		if (shuffleComplete == false) {
	    	//folderPath string is empty
	    	Toast.makeText(this, "Please Shuffle Before Distributing The Game Files", Toast.LENGTH_SHORT).show();
	    }
		else {
			emailFiles(folderPath);
		}
	}
	
	public void shuffle(View button3) {

		Log.d(TAG, "shuffle()");
		folderPath = ((EditText) findViewById(R.id.editText1)).getText().toString();
		if (folderPath.length() == 0) {
	    	//folderPath string is empty
	    	Toast.makeText(this, "Please enter the Absolute Path of the Source Folder", Toast.LENGTH_SHORT).show();
	    }
		else {
			File fRoot = new File(folderPath);
			//Check if Folder exist isDirectory()
			if(fRoot.isDirectory() == false)  {
				Log.d(TAG, "2");
				Toast.makeText(this, "That Folder Does Not Exist In The File System", Toast.LENGTH_SHORT).show();
			}   
			else {
				String[] fileNames;

				if (PankajUtils.isExternalStorageWritable() == true) {
					Log.e(TAG, "Can WRITE1");
				}
				
		        if (PankajUtils.isExternalStorageReadable() == true) {
		        	Log.e(TAG, "Can READ1");
				}

				//get array of files
				File folder = new File(folderPath);
				listOfFiles = folder.listFiles();
				
				//get fileNames, codeNames and emailAddress
				fileNames = getFileNames();
				getCodenameAndEmailAddresses();
				
				targetCodeNames = Arrays.copyOf(codeNames, listOfFiles.length);
				
				randomSort();
				
				//After Sort
				((EditText) findViewById(R.id.editText2)).setText(" ");
				((EditText) findViewById(R.id.editText3)).setText(" ");
				
				String serialNumber;
                                for(int i = 0; i < listOfFiles.length; i++) {
                	                 serialNumber = Integer.toString(i+1);
					((EditText) findViewById(R.id.editText2)).append(serialNumber + ". " + codeNames[i]+'\n');
					((EditText) findViewById(R.id.editText3)).append(serialNumber + ". " + targetCodeNames[i]+'\n');
				}
                
				shuffleComplete = true;
			}
		}
		
	}
	
	public void emailFiles(String folderPath) {
				
		boolean flag = false;
		
		for(int i = 0; i < listOfFiles.length; i++) {
			//send email
			//((EditText) findViewById(R.id.editText1)).append(codeNames[i]+""+emailAddresses[i]+" "+ fileNames[i]);
			try {
				flag = sendEmail(codeNames[i], emailAddresses[i], listOfFiles[i]);
			}
			catch (android.content.ActivityNotFoundException ex) {
			    Toast.makeText(DistributeGameActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
			}
			if(flag) {
				continue;
			}
		}
		
	}
	
	public boolean sendEmail(String codeName, String emailAddress, File file) {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});		  
		email.putExtra(Intent.EXTRA_SUBJECT, "Assassins - Target Assigned");
		email.putExtra(Intent.EXTRA_TEXT, "Dear "+ codeName +", Find your assigned target in the attached file");
		
		//Uri attachedFile = Uri.parse("file://" + file); 
		Uri attachedFile = Uri.fromFile(file);
		email.putExtra(Intent.EXTRA_STREAM, attachedFile);
		
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
		return true;
	}
	
	
	
	public String[] getFileNames() {
		String[] files = new String[listOfFiles.length];
		for(int i = 0; i < listOfFiles.length; i++) {
			if(listOfFiles[i].isFile()) {
				files[i] = 	listOfFiles[i].getName();
			}
			
		}
		return files;
	}
	
	public void getCodenameAndEmailAddresses() {
		codeNames = new String[listOfFiles.length];
		emailAddresses = new String[listOfFiles.length];
		
		MugshotFiles mFile = new MugshotFiles();
		FileInputStream fis = null;

        
		for(int i = 0; i < listOfFiles.length; i++) {
			mFile = null;
			mFile = new MugshotFiles();
			
			if(listOfFiles[i].isFile()) {
			    try{
		       	    fis=new FileInputStream(listOfFiles[i]);
		       	
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
			    
			    if(mFile != null) {
			    	codeNames[i] = mFile.codeName;
			        emailAddresses[i] = mFile.emailAddress;
			    }
			}
		}
	}
	
	public void randomSort() {
		
		int n = emailAddresses.length;
		
	    Random random = new Random();
	    random.nextInt();
	    for (int i = 0; i < n; i++) {
	      int change = i + random.nextInt(n - i);
	      swap(emailAddresses, i, change);
	      swap(codeNames, i, change);
	    }
	}
	
	private void swap(String[] str, int i, int change) {
	    String helper = str[i];
	    str[i] = str[change];
	    str[change] = helper;
	}
}
