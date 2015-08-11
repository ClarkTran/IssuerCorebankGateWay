/**
 * Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential material of
 * Trans-Info Pte Ltd. Singapore and its use of disclosure in whole
 * or in part without express written permission of
 * Trans-Info Pte Ltd. Singapore. is prohibited.
 * Date of Creation   : Feb 25, 2008
 * Version Number     : 1.0
 *                   Modification History:
 * Date          Version No.         Modified By           Modification Details.
 */

package com.transinfo.tplus.messaging.converter;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.debug.DebugWriter;

public class MessageConverter
{


	/*

public ISOMsg muxObj(byte[] isoMsg)throws TPlusException
{
	ISOMsg muxObj = null;
		//TransactionDB objTranx = new TransactionDB();



	try
	{

		TPlusPrintOutput.printMessage("MessageConverter","In muxObj....");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: In muxObj....");

		TPlus8583Parser objParser = new TPlus8583Parser();
		muxObj = objParser.muxObj(isoMsg);

		return muxObj;

	}
	catch(TPlusException exp)
	{
		System.out.println("Errror="+exp);
		throw exp;
 	}
 	catch(Exception exp)
 	{
		System.out.println("Errror="+exp);
		throw new TPlusException(TPlusCodes.APPL_ERR,"Error in ObjToMsg"+exp);
	}

}



public byte[] objToMsg(Object msgObject,String mode)throws TPlusException
{
	byte message[]=null;

	try
	{

		TPlusPrintOutput.printMessage("MessageConverter","In ObjToMsg....");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: In ObjToMsg....");
		IParser msgObj = (IParser)msgObject;

		TransactionDB objTranx = new TransactionDB();

		if(mode!=null && mode.equals("D"))
		{
			TPlusPrintOutput.printMessage("MessageConverter","Destination Message Type "+msgObj.getDestination());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Destination Message Type "+msgObj.getDestination());
			String strMsgProcessor = objTranx.getMessageHandler(msgObj.getDestination());
			IParser objParser = (IParser)getProcessorInstance(strMsgProcessor);
			message = objParser.repack(msgObj,msgObj.getDestinationBits());
		}
		else if(mode!=null && mode.equals("S"))
		{
			TPlusPrintOutput.printMessage("MessageConverter","Source Message Type "+msgObj.getSource());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Source Message Type "+msgObj.getSource());
			String strMsgProcessor = objTranx.getMessageHandler(msgObj.getSource());
			IParser objParser = (IParser)getProcessorInstance(strMsgProcessor);
			message = objParser.repack(msgObj,msgObj.getSourceBits());
		}
		else
		{
			TPlusPrintOutput.printMessage("MessageConverter","Error: Invalid Message Mode..");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Error: Invalid Message Mode..");
			 throw new TPlusException("1001","Invalid Message Mode..."+mode);
		}
	}
	catch(TPlusException exp)
	{
		System.out.println("Errror="+exp);
		throw exp;
 	}
 	catch(Exception exp)
 	{
		System.out.println("Errror="+exp);
		throw new TPlusException(TPlusCodes.APPL_ERR,"Error in ObjToMsg"+exp);
	}

	return message;

}


public IParser msgToObj(IParser orgParser,String strMsg,String mode)throws TPlusException
{
	IParser objParser=null;

	try
	{

		TPlusPrintOutput.printMessage("MessageConverter","In msgToObj....");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: In msgToObj....");

		TransactionDB objTranx = new TransactionDB();

		if(mode!=null && mode.equals("N"))// Actual bit Object
		{
			TPlusPrintOutput.printMessage("MessageConverter","Normal Message Type "+orgParser.getDestination());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Normal Message Type "+orgParser.getDestination());
			String strMsgProcessor = objTranx.getMessageHandler(orgParser.getDestination());
			objParser = (IParser)getProcessorInstance(strMsgProcessor);
			objParser.visaParse(strMsg);
			System.out.println("Normal Message Successfully");
		}
		else if(mode!=null && mode.equals("D")) // Convert to Destination Bit Object
		{
			TPlusPrintOutput.printMessage("MessageConverter","Destination Message Type "+orgParser.getDestination());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Destination Message Type "+orgParser.getDestination());
			String strMsgProcessor = objTranx.getMessageHandler(orgParser.getDestination());
			objParser = (IParser)getProcessorInstance(strMsgProcessor);
			objParser.parse(strMsg,orgParser.getDestinationBits());
		}
		else if(mode!=null && mode.equals("S"))// Convert to Source Bit Object
		{
			TPlusPrintOutput.printMessage("MessageConverter","Source Message Type "+orgParser.getSource());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Source Message Type "+orgParser.getSource());
			String strMsgProcessor = objTranx.getMessageHandler(orgParser.getSource());

			objParser = (IParser)getProcessorInstance(strMsgProcessor);
			objParser.parse(strMsg,orgParser.getSourceBits());
		}
		else
		{
			TPlusPrintOutput.printMessage("MessageConverter","Error: Invalid Message Mode..");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Error: Invalid Message Mode..");
			 throw new TPlusException(TPlusCodes.INV_MSG_MODE,"Invalid Message Mode..."+mode);
		}


	}
	catch(TPlusException exp)
	{
		System.out.println("Errror="+exp);
		throw exp;
 	}
 	catch(Exception exp)
 	{
		System.out.println("Errror="+exp);
		throw new TPlusException(TPlusCodes.APPL_ERR,"Error IN ObjToMsg"+exp);
	}

	System.out.println("Normal Object return");
	return objParser;

}



public IParser ObjToObj(IParser objISO,String strIssuerId,String strTranxType,String mode)throws TPlusException
{
	IParser objParser=null;

	try
	{

		TPlusPrintOutput.printMessage("MessageConverter","In ObjToObj....");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: In ObjToMsg....");

		TransactionDB objTranx = new TransactionDB();

		if(mode!=null && mode.equals("D"))
		{
			TPlusPrintOutput.printMessage("MessageConverter","Destination Message Type "+objISO.getDestination());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Destination Message Type "+objISO.getDestination());
			String strMsgProcessor = objTranx.getMessageHandler(objISO.getDestination());
			objParser = (IParser)getProcessorInstance(strMsgProcessor);
			objParser.parse(objISO,objISO.getDestinationBits());
		}
		else if(mode!=null && mode.equals("S"))
		{
			TPlusPrintOutput.printMessage("MessageConverter","Source Message Type "+objISO.getSource());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Source Message Type "+objISO.getSource());
			String strMsgProcessor = objTranx.getMessageHandler(objISO.getSource());
			objParser = (IParser)getProcessorInstance(strMsgProcessor);
			objParser.parse(objISO,objISO.getSourceBits());
		}
		else
		{
			TPlusPrintOutput.printMessage("MessageConverter","Error: Invalid Message Mode..");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Error: Invalid Message Mode..");
			 throw new TPlusException(TPlusCodes.INV_MSG_MODE,"Invalid Message Mode..."+mode);
		}


	}
	catch(TPlusException exp)
	{
		System.out.println("Errror="+exp);
		throw exp;
 	}
 	catch(Exception exp)
 	{
		System.out.println("Errror="+exp);
		throw new TPlusException(TPlusCodes.APPL_ERR,"Error while ObjToMsg"+exp);
	}

	return objParser;

}

	 */


	@SuppressWarnings("unchecked")
	public static Object getProcessorInstance(String strMsgProcessor) throws TPlusException
	{
		Object objIssuer = null;

		try
		{

			if(strMsgProcessor != null)
			{

				TPlusPrintOutput.printMessage("MessageConverter","Message Processor Class Available..");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: Message Processor Class Available..");

				/*if (!hstVersions.containsKey(strVersion)){
				throw new MPIException(MPICodes.UNSUP_VERSION, MPICodes.getErrorDesc(MPICodes.UNSUP_VERSION) + " Version:"+strVersion);
			}*/

				Class classObj =  Class.forName(strMsgProcessor);
				objIssuer = classObj.newInstance();
			}
			else
			{

				TPlusPrintOutput.printMessage("MessageConverter"," No message processor class found");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("MessageConverter: No message processor class found");
				throw new TPlusException(TPlusCodes.NO_REQ_PROCESSOR,"No message processor class found");
			}
		}
		catch (Exception exp)
		{
			System.out.println("error11="+exp);
			throw new TPlusException(TPlusCodes.OBJ_CREATION_FAIL, " Error while trying to instantiate Processor Class: "+exp.getMessage());
		}

		return objIssuer;


	}

}