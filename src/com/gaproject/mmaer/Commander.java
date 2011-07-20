package com.gaproject.mmaer;




import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class Commander extends TabActivity {
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commander_tabs);
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); 
		TabHost.TabSpec spec;  // Resusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab



		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, MessageTab.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("message").setIndicator("Messages",
				res.getDrawable(R.drawable.tab_messages))
				.setContent(intent);
		tabHost.addTab(spec);
		tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 75;

		// Do the same for the other tabs
		intent = new Intent().setClass(this, ContactTab.class);
		spec = tabHost.newTabSpec("contact").setIndicator("Contacts",
				res.getDrawable(R.drawable.tab_contacts))
				.setContent(intent);
		tabHost.addTab(spec);
		tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 75;
		
		intent = new Intent().setClass(this, AcknowledgementTab.class);
		spec = tabHost.newTabSpec("ack").setIndicator("Acks",
				res.getDrawable(R.drawable.tab_acknowledgements))
				.setContent(intent);
		tabHost.addTab(spec);
		tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = 75;

		tabHost.setCurrentTab(0);
	}
	
	
	

}
