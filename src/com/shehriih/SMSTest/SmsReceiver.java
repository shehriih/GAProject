package com.shehriih.SMSTest;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		//---get the SMS message passed in---
		Bundle bundle = intent.getExtras();        
		SmsMessage[] msgs = null;
		String str = "";  
		String commanderNumber = "";
		if (bundle != null)
		{
			//---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];            
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
				str += "SMS from " + msgs[i].getOriginatingAddress();                     
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";        
				commanderNumber = msgs[i].getOriginatingAddress();
			}
			//---display the new SMS message---
			//Toast.makeText(context, str, Toast.LENGTH_LONG).show();
			Intent newIntent = new Intent(context, CustomDialogActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			newIntent.putExtra("commanderNumber",commanderNumber );
			newIntent.putExtra("msg",str );


			context.startActivity(newIntent);
		}
	}


}