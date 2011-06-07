package com.shehriih.SMSTest;

//import com.shehriih.SMSTest.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommanderSMSSend extends Activity implements View.OnClickListener
{
	
	
	

	Button btnSendSMS,btnVacate,btnUtilitiesOn,btnUtilitiesOff,btnAllClear,btnManDown;
	EditText txtPhoneNo;
	EditText txtMessage;
	
	 
	
	
	@Override
	public void onClick(View v) 
	{
		Button btn     = (Button) findViewById(v.getId());
		txtPhoneNo     = (EditText) findViewById(R.id.txtPhoneNo);
		final String phoneNo = txtPhoneNo.getText().toString();
		final String message = btn.getText().toString();
		
		//Toast.makeText(this, btn.getText(), Toast.LENGTH_LONG).show();
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Are you sure you want to send "+message+" ?")
	               .setCancelable(false)
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                        //CommanderSMSSend.this.finish();
	                	   sendSMS(phoneNo, message);	                   }
	               })
	               .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                        dialog.cancel();
	                   }
	               });
	        final AlertDialog alert = builder.create();
	        if (phoneNo.length()>0) 
            {
            	alert.show();
               
            }
            else
            	Toast.makeText(getBaseContext(), 
                    "Please enter both phone number and message.", 
                    Toast.LENGTH_SHORT).show();
	        
	
		 
		
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.commander); 
        btnSendSMS    = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo    = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage    = (EditText) findViewById(R.id.txtMessage);
        btnVacate     = (Button) findViewById(R.id.btnVacate);
        btnUtilitiesOn= (Button) findViewById(R.id.btnUtilitiesOn);
        btnUtilitiesOff= (Button) findViewById(R.id.btnUtilitiesOff);
        btnAllClear= (Button) findViewById(R.id.btnAllClear);
        btnManDown= (Button) findViewById(R.id.btnManDown);
        txtPhoneNo.getText().toString();
    	 
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to send ?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        //CommanderSMSSend.this.finish();
                	   sendSMS(txtPhoneNo.getText().toString(), txtMessage.getText().toString());
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                   }
               });
        final AlertDialog alert = builder.create();
        
        
        btnVacate.setOnClickListener(this);
        btnUtilitiesOn.setOnClickListener(this);
        btnUtilitiesOff.setOnClickListener(this);
        btnAllClear.setOnClickListener(this);
        btnManDown.setOnClickListener(this);

        
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	
            	String phoneNo = txtPhoneNo.getText().toString();
            	String message = txtMessage.getText().toString();             	
                if (phoneNo.length()>0 && message.length()>0) 
                {
                	alert.show();
                   // sendSMS(phoneNo, message);
                }
                else
                	Toast.makeText(getBaseContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        });        
    }
    
    //---sends a SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {      
    	/*
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, test.class), 0);                
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, pi, null);        
        */
    	
    	String SENT = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
    	
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
    	
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
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
        
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
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
        for(String contact:listOfContacts)
        {
        	sms.sendTextMessage(contact, null, message, sentPI, deliveredPI); 
        }
                      
    }   
    
    
}