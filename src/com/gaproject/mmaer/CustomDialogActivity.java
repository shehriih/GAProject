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
		MessageParser mp= new MessageParser(extras.getString("msg"));
		String messagestamp=mp.getMessageStamp();
		CustomizeDialog customizeDialog = new CustomizeDialog(this,commanderNumber, messagestamp);
		
		customizeDialog.setTopTitle(extras.getString("msg"));
		customizeDialog.show();
		
	
		
		
		
	}
	
}