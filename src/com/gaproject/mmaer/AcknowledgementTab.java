package com.gaproject.mmaer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class AcknowledgementTab extends ListActivity{
	
	DBAdapter db = new DBAdapter(this);
	MyCustomAdapter adapter;
	List<String> listOfMsgs;
	AlertDialog alert;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 db.open();
		listOfMsgs = new ArrayList<String>(Arrays.asList(db.getAllMessages()));
		adapter = new MyCustomAdapter(this,  R.drawable.ack_msg_list, db.getAllMessages());
	    setListAdapter(adapter);
	   
		//ListView lv= this.getListView();
		//lv.setDividerHeight(2);
		
	}
	
	private class MyCustomAdapter extends ArrayAdapter<String> {

	private String[] messages ;
	
	public MyCustomAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.messages = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String[] preDefMsgs = getResources().getStringArray(R.array.messages_array);
		List<String> preDefMsgslist = Arrays.asList(preDefMsgs);
		
		LayoutInflater inflater=getLayoutInflater();
		View row=inflater.inflate( R.drawable.ack_msg_list, parent, false);
		
		String item = messages[position];
		String[] msgArr = item.split("-");
		String msg = msgArr[0]; // e.g. Vacate
		String numOfMissingAck = msgArr[2];
		
		TextView label=(TextView)row.findViewById(R.id.msgAckListTopText);
		label.setText(msg);
		TextView label2=(TextView)row.findViewById(R.id.msgAckListBottomText);
		label2.setText("Number of Missing Ack : "+numOfMissingAck);
		ImageView icon=(ImageView)row.findViewById(R.id.msgAckListIcon);
		
		int intNumOfAck = Integer.parseInt(numOfMissingAck);
		
		if(intNumOfAck>1)
			row.setBackgroundColor(Color.RED);
		
		  switch (preDefMsgslist.indexOf(msg)) {
		case 0:  icon.setImageResource(R.drawable.icon_vacate);       break;
		case 1:  icon.setImageResource(R.drawable.icon_utilitieson);      break;
		case 2:  icon.setImageResource(R.drawable.icon_utilitiesoff);         break;
		case 3:  icon.setImageResource(R.drawable.icon_mandown);         break;
		case 4:  icon.setImageResource(R.drawable.icon_allclear);         break;
		case 5:  icon.setImageResource(R.drawable.icon_writemessage);         break;
		// TODO design an icon for PAR msgs /
		default: icon.setImageResource(R.drawable.icon_writemessage);         break;
		}
		
		return row;
	}
};
	
	public void updateData()
	{
		//Toast.makeText(this, "Testing Data Update2", Toast.LENGTH_LONG).show();
		db.open();
		listOfMsgs = new ArrayList<String>(Arrays.asList(db.getAllMessages()));
		adapter = new MyCustomAdapter(this, R.drawable.ack_msg_list, db.getAllMessages());
		
		adapter.notifyDataSetChanged();
		getListView().setAdapter(adapter);
	}
	
	
	
	
	@Override
	protected void onStart() 
	{
		// TODO Auto-generated method stub
		super.onStart();
		MySharedData sd = (MySharedData)getApplicationContext();
		sd.setAckTabObj(this);
		updateData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MySharedData sd = (MySharedData)getApplicationContext();
		sd.setAckTabObj(this);	
		updateData();
		}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MySharedData sd = (MySharedData)getApplicationContext();
		sd.setAckTabObj(null);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		MySharedData sd = (MySharedData)getApplicationContext();
		sd.setAckTabObj(null);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Log.v("--- GA Deb--","in ItemClick");
		String selection = l.getItemAtPosition(position).toString();
		db.open();
		String [] temparray=selection.split("-");
		Log.v("--- GA Deb--","After DB open");
		
		List<String> missingAcksList= db.getAllMissingAcks(temparray[1]);
		
		updateData();
		String[] arrList = (String[])missingAcksList.toArray(new String[0]);
		 
		  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	         builder.setTitle(missingAcksList.size()+" Acks. Missing");
	         builder.setCancelable(true);
	         builder.setItems(arrList, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int item) {
	                   dialog.cancel();
	                  
	                }
	            });

	         alert = builder.create();
	         alert.show();
		 
	         //Toast.makeText(this, missingAcksList.size()+" Acks. Missing", Toast.LENGTH_LONG).show();
	
	}
	
	



}