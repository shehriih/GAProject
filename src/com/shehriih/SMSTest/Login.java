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
	
	private Button btnLogin;
	private RadioButton radBtnCommander;
	private RadioButton radBtnResponder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login); 
		btnLogin = (Button) findViewById(R.id.btnLogin);
		radBtnCommander = (RadioButton)findViewById(R.id.radBtnCommander);
		radBtnResponder = (RadioButton)findViewById(R.id.radBtnResponder);
		
		
		btnLogin.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	
            	if(radBtnCommander.isChecked())
            	{
            		Toast.makeText(Login.this, "Launching Commander !", Toast.LENGTH_SHORT).show();
            		launchActivity(CommanderSMSSend.class);
            	}
            	else if(radBtnResponder.isChecked())
            	{
            		Toast.makeText(Login.this, "Lanuching Responder !", Toast.LENGTH_SHORT).show();
            		launchActivity(ResponderSMSSend.class);
            		
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
		startActivity(intent);
	}

}
