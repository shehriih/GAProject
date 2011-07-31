package com.gaproject.mmaer;

import android.app.Activity;
import android.os.Bundle;

public class CustomDialogActivity extends Activity {
	/** Called when the activity is first created. */
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** Display Custom Dialog */
		Bundle extras = getIntent().getExtras(); 
		String commanderNumber=extras.getString("commanderNumber");
		String msg            =extras.getString("msg");
		
		CustomDialog customizeDialog = new CustomDialog(this,commanderNumber, msg);
		customizeDialog.show();
		
	
		
		
		
	}
	
}