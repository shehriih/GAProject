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

public class SongsActivity extends ListActivity{

	String [] test={"test"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_multiple_choice,test));

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

	}
}