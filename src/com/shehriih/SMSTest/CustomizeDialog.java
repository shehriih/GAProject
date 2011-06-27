package com.shehriih.SMSTest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

/** Class Must extends with Dialog */

public class CustomizeDialog extends Dialog implements OnSeekBarChangeListener{
	private AnimationDrawable animation;
	private SoundManager mSoundManager = new SoundManager();
	private SeekBar mSeekBar;
	

	public CustomizeDialog(Context context) {
		super(context);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.flashscreen);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.RelativeLayout01);
		layout.setBackgroundResource(R.drawable.color_animation);
		animation = (AnimationDrawable) layout.getBackground();	
		
		mSoundManager.initSounds(context);
		mSoundManager.addSound(1, R.raw.ring);
		

        mSeekBar = (SeekBar)findViewById(R.id.FlashScreenOkBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        
	}

	@Override   
	public void onWindowFocusChanged(boolean hasFocus){
		
		animation.start();
		mSoundManager.playLoopedSound(1);	       
	    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		
		if(mSeekBar.getProgress()== mSeekBar.getMax())
		{
			Toast.makeText(getContext(),"Test // Test // Test", Toast.LENGTH_SHORT).show();
			SoundManager.stopSound(1);
			SoundManager.cleanup();
			
			Intent Intent = new Intent(this.getContext(), ResponderSMSSend.class);
	        Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        
	        this.getContext().startActivity(Intent);
			dismiss();
			
		}
		
    }

	@Override
    public void onStartTrackingTouch(SeekBar seekBar) {
   
    }

	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
       
    }
}
