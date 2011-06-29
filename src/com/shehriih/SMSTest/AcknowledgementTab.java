package com.shehriih.SMSTest;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AcknowledgementTab extends ListActivity{
	
	DBAdapter db = new DBAdapter(this);
	ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db.open();

		adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,db.getAllMessages());
		
	    setListAdapter(adapter);
	    

		ListView lv= this.getListView();
		lv.setDividerHeight(2);




		//Setting for the TextView to be displayed when there are no messages sent
		TextView emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setText("You have no messages sent");
		emptyView.setTextSize(18);emptyView.setGravity(Gravity.CENTER | Gravity.TOP);
		((ViewGroup)lv.getParent()).addView(emptyView);
		lv.setEmptyView(emptyView);

	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		final CharSequence[] items = {"Red", "Green", "Blue"};
		
		db.open();
		adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,db.getAllMessages());
		db.close();
		adapter.notifyDataSetChanged();
		l.setAdapter(adapter);

		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("Pick a color");
		 builder.setCancelable(true);
		 builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			       dialog.cancel();
			    }
			});

		 AlertDialog alert = builder.create();
		 alert.show();
		 
		 
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
	}
}