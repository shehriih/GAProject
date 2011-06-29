package com.shehriih.SMSTest;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.telephony.SmsManager;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/** Class Must extends with Dialog */

public class CustomizeDialog extends Dialog implements OnSeekBarChangeListener{
	private AnimationDrawable animation;
	private SoundManager mSoundManager = new SoundManager();
	private SeekBar mSeekBar;
	Context context;
	String commanderNumber,messageStamp;
	

	public CustomizeDialog(Context context,String commanderNumber,String messageStamp) {
		super(context);
		
		this.context=context;
		this.commanderNumber=commanderNumber;
		this.messageStamp=messageStamp;
		
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.flashscreen);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.RelativeLayout01);
		layout.setBackgroundResource(R.drawable.color_animation);
		animation = (AnimationDrawable) layout.getBackground();	
		
		mSoundManager.initSounds(context);
		mSoundManager.addSound(1, R.raw.ring);
		
		

        mSeekBar = (SeekBar)findViewById(R.id.FlashScreenOkBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        
	}


	

	public String getCommanderNumber() {
		return commanderNumber;
	}

	public void setCommanderNumber(String commanderNumber) {
		this.commanderNumber = commanderNumber;
	}

	public String getMessageStamp() {
		return messageStamp;
	}

	public void setMessageStamp(String messageStamp) {
		this.messageStamp = messageStamp;
	}

	@Override   
	public void onWindowFocusChanged(boolean hasFocus){
		
		animation.start();
		mSoundManager.playLoopedSound(1);	       
	    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		
		if(mSeekBar.getProgress()== mSeekBar.getMax())
		{
			Toast.makeText(getContext(),"Test // Test // Test", Toast.LENGTH_SHORT).show();
			SoundManager.stopSound(1);
			SoundManager.cleanup();
			
			Intent Intent = new Intent(this.getContext(), ResponderSMSSend.class);
	        Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        sendSMS(getCommanderNumber(), createAcknowledgement(getMessageStamp()));     
	        this.getContext().startActivity(Intent);
			dismiss();
			
		}
		
    }

	@Override
    public void onStartTrackingTouch(SeekBar seekBar) 
	{
   
    }

	@Override
    public void onStopTrackingTouch(SeekBar seekBar) 
	{
       
    }
	
	public void setTopTitle(String title)
	{
		TextView textview = (TextView) findViewById(R.id.FlashScreenTextView01);
		textview.setText(title);
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

		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(context, "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(context, "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(context, "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(context, "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(context, "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(context, "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(context, "SMS not delivered",
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

	public String createAcknowledgement(String messageStamp)
	{
		String ack= MessageParser.applicationstamp+ MessageParser.ackstamp+messageStamp+"OK";
		
		return ack;
	}
	
}


