package com.gaproject.mmaer;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResponderSMSSend extends Activity {

	private Button   btnManDown;
	private EditText txtPhoneNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.responder);
		btnManDown = (Button) findViewById(R.id.btnManDown);
		txtPhoneNo    = (EditText) findViewById(R.id.txtPhoneNo);
		
		btnManDown.setOnClickListener(new View.OnClickListener() 
	        {
	            public void onClick(View v) 
	            {            	
	            	String phoneNo = txtPhoneNo.getText().toString();
	            	String message =   btnManDown.getText().toString();      	
	                if (phoneNo.length()>0 && message.length()>0)                
	                    sendSMS(phoneNo, message);                
	                else
	                	Toast.makeText(getBaseContext(), 
	                        "Please enter both phone number and message.", 
	                        Toast.LENGTH_SHORT).show();
	            }
	        });  
	}

	// ---sends a SMS message to another device---
	private void sendSMS(String phoneNumber, String message) {
		/*
		 * PendingIntent pi = PendingIntent.getActivity(this, 0, new
		 * Intent(this, test.class), 0); SmsManager sms =
		 * SmsManager.getDefault(); sms.sendTextMessage(phoneNumber, null,
		 * message, pi, null);
		 */

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		String[] listOfContacts = phoneNumber.split(",");
		for (String contact : listOfContacts) {
			sms.sendTextMessage(contact, null, message, sentPI, deliveredPI);
		}

	}

}
