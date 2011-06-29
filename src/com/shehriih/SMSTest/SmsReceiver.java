package com.shehriih.SMSTest;

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
		String smsSendedNum = "";
		if (bundle != null)
		{
			//---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];            
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
				str += msgs[i].getMessageBody().toString();      
				smsSendedNum = msgs[i].getOriginatingAddress();
			}
			//---display the new SMS message---
			Intent newIntent = new Intent(context, CustomDialogActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			newIntent.putExtra("commanderNumber",smsSendedNum.toString());
			newIntent.putExtra("msg",str );
			MessageParser mp  = new MessageParser(str);
			// only fire the flashing screen when it is an sms for app and it is not an ack sms
			if(mp.isAppMessage())
			{
				if(!mp.isAcknowledgement()) // here we are in the responder screen
				{
					context.startActivity(newIntent);
				}
				else // it is an ack for the commander
				{
					DBAdapter dba = new DBAdapter(context);
					dba.open();
					dba.updateNumberArray(mp.getMessageStamp(), smsSendedNum);
				}
			}
			
			
			
		}
	}


}