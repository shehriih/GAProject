package com.shehriih.SMSTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUPBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		// TODO Auto-generated method stub
		// start the service ...
		
		Intent startServiceIntent = new Intent(context, MyService.class);
        context.startService(startServiceIntent);
	}

}
