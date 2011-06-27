package com.shehriih.SMSTest;

import android.app.Activity;
import android.os.Bundle;

public class CustomDialogActivity extends Activity {
	/** Called when the activity is first created. */
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** Display Custom Dialog */
		CustomizeDialog customizeDialog = new CustomizeDialog(this);
		customizeDialog.show();
		
	}
	
}