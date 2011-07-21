package com.gaproject.mmaer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		//---get the SMS message passed in---
		Bundle bundle = intent.getExtras();        
		SmsMessage[] msgs = null;
		String str = "";  
		String smsSenderNum = "";
		if (bundle != null)
		{
			//---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];            
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
				str += msgs[i].getMessageBody().toString();      
				smsSenderNum = msgs[i].getOriginatingAddress();
			}
			
			if(smsSenderNum.contains("+"))
				smsSenderNum = smsSenderNum.substring(smsSenderNum.length()-10, smsSenderNum.length());
	
			//---display the new SMS message---
			Intent newIntent = new Intent(context, CustomDialogActivity.class);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			newIntent.putExtra("commanderNumber",smsSenderNum.toString());
			newIntent.putExtra("msg",str );
			MessageParser mp  = new MessageParser(str);
			Log.v("SMS Sender Number",smsSenderNum);
			Toast.makeText(context, smsSenderNum, Toast.LENGTH_SHORT).show();
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
					dba.updateNumberArray(mp.getMessageStamp(), smsSenderNum);
					//Updating the AcknowledgmentTab List view if the it is on the foreground
					MySharedData sd = (MySharedData)context.getApplicationContext();
					if(sd.getAckTabObj()!=null)
					 sd.getAckTabObj().updateData();
				}
			}
			
			
			
		}
	}


}