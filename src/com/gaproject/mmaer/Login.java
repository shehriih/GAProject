package com.gaproject.mmaer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class Login extends Activity 
{
	//DBAdapter db = new DBAdapter(this);
	
	private Button btnLogin;
	private RadioButton radBtnResponder;
	private RadioButton radBtnCommander;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login); 
		btnLogin = (Button) findViewById(R.id.btnLogin);
		radBtnResponder = (RadioButton)findViewById(R.id.radBtnResponder);
		radBtnCommander = (RadioButton)findViewById(R.id.radBtnCommander);
		
		
		btnLogin.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	
            	
            	if(radBtnResponder.isChecked())
            	{
            		Toast.makeText(Login.this, "Launching Responder !", Toast.LENGTH_SHORT).show();
            		launchActivity(ResponderSMSSend.class);
            		
            	}
            	else if(radBtnCommander.isChecked())
            	{
            		Toast.makeText(Login.this, "Launching List!", Toast.LENGTH_SHORT).show();
            		launchActivity(Commander.class);
            		
            	}
            	else
            	{
            		Toast.makeText(Login.this, "You did not choose any role !", Toast.LENGTH_SHORT).show();
            	}
            }
        });    

		
		
	}
	
	private void launchActivity(Class<? extends Activity> className)
	{
		DBAdapter db = new DBAdapter(this);
		Intent intent = new Intent(this, className);
		if(className.equals(Commander.class))
		{
			db.open();
			// for debugging only
			db.resetTables();
			if(db.getAllNames()==null || db.getAllNames().length==0)
			{
				db.insertPersonnel("Engin","4089921363");
				db.insertPersonnel("Ibrahim","4082158400");
				db.insertPersonnel("Engin-2","4083685859");
				db.insertPersonnel("Test 1 5554", "15555215554");
				db.insertPersonnel("Test 2 5556", "15555215556");
				db.insertPersonnel("Test 3 5558", "15555215558");
				
			}
			
			if(db.getAllMessages()==null || db.getAllMessages().length==0)
			{
				//db.insertMessage("Refresh","test","4083685859");
			}
			db.close();
		}
		
		startActivity(intent);
	}
	

}
