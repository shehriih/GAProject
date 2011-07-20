package com.gaproject.mmaer;

import android.app.Application;

public class MySharedData extends Application
{
	private AcknowledgementTab ackTabObj; // it will have the current running object or null

	public AcknowledgementTab getAckTabObj() {
		return ackTabObj;
	}

	public void setAckTabObj(AcknowledgementTab ackTabObj) {
		this.ackTabObj = ackTabObj;
	}
	

}
