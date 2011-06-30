package com.shehriih.SMSTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class Login extends Activity 
{
	DBAdapter db = new DBAdapter(this);
	
	private Button btnLogin;
	private RadioButton radBtnResponder;
	private RadioButton radBtnList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login); 
		btnLogin = (Button) findViewById(R.id.btnLogin);
		radBtnResponder = (RadioButton)findViewById(R.id.radBtnResponder);
		radBtnList = (RadioButton)findViewById(R.id.radBtnList);
		
		
		btnLogin.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	
            	
            	if(radBtnResponder.isChecked())
            	{
            		Toast.makeText(Login.this, "Lanuching Responder !", Toast.LENGTH_SHORT).show();
            		launchActivity(ResponderSMSSend.class);
            		
            	}
            	else if(radBtnList.isChecked())
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
	
	private void launchActivity(Class className)
	{
		Intent intent = new Intent(this, className);
		if(className.equals(Commander.class))
		{
			db.open();
			db.resetTables();
			
			db.insertPersonnel("Engin","5556");
			db.insertPersonnel("Ibrahim","4082158400");
			db.insertPersonnel("Engin-2","4083685859");
			
			db.insertMessage("Refresh","test","4083685859");
			db.close();
		}
		
		startActivity(intent);
	}
	
}
