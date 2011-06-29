package com.shehriih.SMSTest;

import android.app.Activity;
import android.os.Bundle;

public class CustomDialogActivity extends Activity {
	/** Called when the activity is first created. */
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** Display Custom Dialog */
		Bundle extras = getIntent().getExtras(); 
		String commandernumber=extras.getString("commanderNumber");
		String messagestamp=extras.getString("msg").substring(6,9);
		CustomizeDialog customizeDialog = new CustomizeDialog(this,commandernumber, messagestamp);
		
		customizeDialog.setTopTitle(extras.getString("msg"));
		customizeDialog.show();
		
	
		
		
		
	}
	
}