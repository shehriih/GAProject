package com.shehriih.SMSTest;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class ContactTab extends ListActivity{

	DBAdapter db = new DBAdapter(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		db.open();
		db.resetTables();
		db.insertPersonnel("Hakki","4083685859");
		db.insertPersonnel("Husam","4089921363");
		db.insertPersonnel("Hafiz","4083685859");
		
      // final String[] contacts =getResources().getStringArray(R.array.contacts_array);
		

		setListAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_multiple_choice,db.getAllNames()));

		ListView lv= this.getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setDividerHeight(2);
		
		
		
		
		
		//Setting for the TextView to be displayed when there are no contacts
		TextView emptyView = new TextView(this);
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setText("You have no contacts");
		emptyView.setTextSize(18);emptyView.setGravity(Gravity.CENTER | Gravity.TOP);
		emptyView.setVisibility(View.GONE);
		((ViewGroup)lv.getParent()).addView(emptyView);
		lv.setEmptyView(emptyView);
		db.close();

	}
	
	
	@Override
	 protected void onListItemClick(ListView l, View v, int position, long id) {
	  // TODO Auto-generated method stub
	  super.onListItemClick(l, v, position, id);
	  
	  if(l.isItemChecked(position)){
		  db.open();
		  db.activatePersonnel((String)l.getItemAtPosition(position));
		  db.close();
	  }
	  else{
		  db.open();
		  db.deactivatePersonnel((String)l.getItemAtPosition(position));
		  db.close();
	  }
	}	  
	  	
}