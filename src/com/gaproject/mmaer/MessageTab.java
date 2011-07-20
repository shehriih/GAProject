package com.gaproject.mmaer;



import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageTab extends ListActivity{
	
	DBAdapter db = new DBAdapter(this);
	private static final int SETTING_SCREEN = Menu.FIRST;
	public PARTimerTask parTask;
	public Timer parTimer;
	
	//A modified class to override ListView default ListAdapter
	private class MyCustomAdapter extends ArrayAdapter<String> {

		private String[] messages = getResources().getStringArray(R.array.messages_array);

		public MyCustomAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater=getLayoutInflater();
			View row=inflater.inflate(R.drawable.messagelist_item, parent, false);
			TextView label=(TextView)row.findViewById(R.id.Txtmsg);
			label.setText(messages[position]);
			ImageView icon=(ImageView)row.findViewById(R.id.vacate_icon);

			switch (position) {
			case 0:  icon.setImageResource(R.drawable.icon_vacate);       break;
			case 1:  icon.setImageResource(R.drawable.icon_utilitieson);      break;
			case 2:  icon.setImageResource(R.drawable.icon_utilitiesoff);         break;
			case 3:  icon.setImageResource(R.drawable.icon_mandown);         break;
			case 4:  icon.setImageResource(R.drawable.icon_allclear);         break;
			case 5:  icon.setImageResource(R.drawable.icon_writemessage);         break;
			}
			return row;
		}
	};


	class PARTimerTask extends TimerTask
	{

		@Override
		public void run() 
		{
		    db.open();
            String tempmessage=prepareMessage("PAR Request");
            String[] temp= db.getAllActiveNumbers();
            System.out.println(temp);
            for(int i=0;i<temp.length;i++)
            {
         	   sendSMS(temp[i], tempmessage);
            }
            storeMessage(tempmessage,temp);
      	   db.close();
			
		}
	}
	
	public void startPARTask(long sec)
	{
		parTask = new PARTimerTask();
	    parTimer = new Timer();
	    parTimer.schedule(parTask, 1000*sec, 1000*sec);
	}
	
	public void stopPARTask()
	{
		parTask.cancel();
		parTimer.cancel();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] messages = getResources().getStringArray(R.array.messages_array);
		setListAdapter(new MyCustomAdapter(this,R.drawable.messagelist_item,messages));
	
		ListView lv = getListView();
		lv.setDividerHeight(2); 
		
		startPARTask(60);
		
		/*	db.open();
		setListAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_multiple_choice,messages));
		db.close();
		ListView lv= this.getListView();
		//lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setDividerHeight(2);*/
		

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	 final String selection = l.getItemAtPosition(position).toString();
	 
	 if(position!=5)
	 {
	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
     builder.setMessage("Are you sure you want to send "+ selection+ " ?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   db.open();
                   String tempmessage=prepareMessage(selection);
                   String[] temp= db.getAllActiveNumbers();
                   System.out.println(temp);
                   for(int i=0;i<temp.length;i++)
                   {
                	   sendSMS(temp[i], tempmessage);
                   }
                   storeMessage(tempmessage,temp);
             	   db.close();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                     dialog.cancel();
                }
            });
     final AlertDialog alert = builder.create();
     alert.show();
	 }
	 else
	 {
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);
		 alert.setMessage("Enter Your Message");

		 // Set an EditText view to get user input 
		 final EditText input = new EditText(this);
		 alert.setView(input);

		 alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int whichButton) {
		   String value = input.getText().toString();
		   String temp=prepareMessage(value);
		   db.open();
           String[] temparray= db.getAllActiveNumbers();
           System.out.print(temp);
           for(int i=0;i<temparray.length;i++)
           {
        	   sendSMS(temparray[i], temp);
           }
     	   db.close();
		   }
		 });

		 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
		     dialog.dismiss();
		   }
		 });

		 alert.show();     
		
	 }
		
     
	}

	private void sendSMS(String phoneNumber, String message)
    {      
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
   
	public String prepareMessage(String messagecontent)
	{
		
		 Random r = new Random();
		 String token = Long.toString(Math.abs(r.nextLong()), 36).substring(0,3);

		 String preparedmessage="*!="+"MES"+token+messagecontent;
		
		return preparedmessage;
	}
	public void storeMessage(String preparedmessage, String[] numberarray)
	{
		MessageParser mp= new MessageParser(preparedmessage);
		String numberString=arrayToString2(numberarray,";");		
		db.open();
		db.insertMessage(mp.getMessage(), mp.getMessageStamp(),numberString);
		db.close();
		
	}
	
	
	public static String arrayToString2(String[] a, String separator) {
	    StringBuffer result = new StringBuffer();
	    if (a.length > 0) {
	        result.append(a[0]);
	        for (int i=1; i<a.length; i++) {
	            result.append(separator);
	            result.append(a[i]);
	        }
	    }
	    return result.toString();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, SETTING_SCREEN, 0, "Settings");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		Log.v("GA Project","in Item Selected ");
		switch (item.getItemId())
		{
		case SETTING_SCREEN:
			//Toast.makeText(this, "Settings commadner", Toast.LENGTH_LONG).show();
			 AlertDialog.Builder alert = new AlertDialog.Builder(this);
			 alert.setMessage("Enter the Number of Seconds for PAR reqs");

			 // Set an EditText view to get user input 
			 final EditText input = new EditText(this);
			 input.setInputType(InputType.TYPE_CLASS_NUMBER);
			 alert.setView(input);

			 alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
			   String value = input.getText().toString();
			   int intValue = Integer.parseInt(value);
			   if(intValue>0)
			   {
				   stopPARTask();
				   startPARTask(intValue);
			   }
			   else
				   stopPARTask();
			 }
			 });

			 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int whichButton) {
			     dialog.dismiss();
			   }
			 });

			 alert.show();     
		
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}


}
