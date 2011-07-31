package com.gaproject.mmaer;

import android.app.Application;

public class MySharedData extends Application
{
	private AcknowledgementTab ackTabObj = null;; // it will have a ref to the current running object or null
	private CustomDialog flashScreen = null;     // it will have a ref to the current running flashscreen. 

	public CustomDialog getFlashScreen() {
		return flashScreen;
	}

	public void setFlashScreen(CustomDialog flashScreen) {
		this.flashScreen = flashScreen;
	}

	public AcknowledgementTab getAckTabObj() {
		return ackTabObj;
	}

	public void setAckTabObj(AcknowledgementTab ackTabObj) {
		this.ackTabObj = ackTabObj;
	}
	

}
