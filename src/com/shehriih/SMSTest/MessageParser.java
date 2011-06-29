package com.shehriih.SMSTest;
public class MessageParser {
	
	private final boolean isappmessage;
	private final boolean isacknowledgement;
	private final String  messagestamp;
	private final String  messagecontent;
	
	private final String applicationstamp= "*!=";
	private final String ackstamp="ACK";

	public MessageParser(String message)
	{
		if(message.substring(0,3).equals(applicationstamp))
			isappmessage=true;
		else
			isappmessage=false;
		
		if(message.substring(3,6).equals(ackstamp))
			isacknowledgement=true;
		else
			isacknowledgement=false;
		
		this.messagestamp= message.substring(6,9);
		this.messagecontent= message.substring(9);
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
