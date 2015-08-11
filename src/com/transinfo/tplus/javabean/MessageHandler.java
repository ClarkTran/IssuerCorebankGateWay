/*** Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.* This work contains trade secrets and confidential material of* Trans-Info Pte Ltd. Singapore and its use of disclosure in whole* or in part without express written permission of* Trans-Info Pte Ltd. Singapore. is prohibited.* Date of Creation   : Feb 25, 2008* Version Number     : 1.0*                   Modification History:* Date          Version No.         Modified By           Modification Details.*/package com.transinfo.tplus.javabean;

public class MessageHandler{

	private String port;
	private String messageType;
	private String messageHandler;
	private String messageProcessor;

	public void setPort(String port){

		this.port = port;
	}

	public String getPort(){

		return this.port;
	}

	public void setMessageType(String messageType){

		this.messageType = messageType;
	}

	public String getMessageType(){

		return this.messageType;
	}


	public void setMessageHandler(String messageHandler){

		this.messageHandler = messageHandler;
	}

	public String getMessageHandler(){

		return this.messageHandler;
	}


	public void setMessageProcessor(String messageProcessor){

		this.messageProcessor = messageProcessor;
	}

	public String getMessageProcessor(){

		return this.messageProcessor;
	}


	public String toString(){

		return this.port + " -- " + this.messageHandler;
	}
}