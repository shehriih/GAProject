package com.shehriih.SMSTest;
public class MessageParser {
	
	private  boolean isappmessage;
	private  boolean isacknowledgement;
	private  String  messagestamp;
	private  String  messagecontent;
	private  String  msg;
	
	public static final String applicationstamp= "*!=";
	public static final String ackstamp="ACK";

	public MessageParser(String msg)
	{
		this.msg =msg;
		
		if(msg!=null && msg.length()>=9)
		{
			if(msg.substring(0,3).equals(applicationstamp))
				isappmessage=true;
			else
				isappmessage=false;
		
			if(msg.substring(3,6).equals(ackstamp))
				isacknowledgement=true;
			else
				isacknowledgement=false;
		
			this.messagestamp= msg.substring(6,9);
			this.messagecontent= msg.substring(9);
		}
		else
		{
			this.isappmessage=false;
			this.isacknowledgement=false;
		}
	}
	

	public boolean isAppMessage()
	{
		return isappmessage;
	}
	
	public boolean isAcknowledgement()
	{
		return isacknowledgement;
	}
	
	public String getMessage()
	{
		return messagecontent;
	}
	
	public String getMessageStamp()
	{
		return messagestamp;
	}
	
}
