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

package com.transinfo.tplus.messaging.common;

import java.util.Date;

import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;

import com.transinfo.tplus.TPlusCalendar;
import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.messaging.RequestBaseHandler;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;

public class EchoRequest extends RequestBaseHandler
{

	int N=1;
	String strDesc = "";
	String strConnName="";
	String strResCode="";
	public EchoRequest(){}

	public IParser execute(IParser objISO)throws TPlusException
	{

		try
		{
			ISOMsg objMsg = new ISOMsg();

			System.out.println("Processing for ECHO Request");
			Date d = new Date();
			TransactionDB tranx = new TransactionDB();
			String traceNo = tranx.getTraceNo();

			objMsg.set(0,"0800");
			objMsg.set(7,ISODate.getDateTime(d));
		    objMsg.set(11,traceNo);
			long jDate = TPlusCalendar.getJulianDate();
			System.out.println(jDate);
			objMsg.set(37, jDate+"0"+N++);
		    objMsg.set(70,"301");

			objISO.setMsgObject(objMsg);
			objISO.setParse(true);
			objISO.setMsgType("ISO");

			strConnName=objISO.getConnectionName();
			ISOMsg issuerResObj =	sendAndReceiveDestination(objISO);


			if(issuerResObj !=null)
			{

				strResCode = issuerResObj.getString(39);
				return updateResponse(objISO,issuerResObj);
			}
			else
			{
				strDesc = " No Response from Host ";
				return null;
			}



		}
		catch(TPlusException tplusExp)
		{
				System.out.println("Exception while execute SignOn Request.."+tplusExp.getMessage());
				objISO.setValue(TPlusISOCode.RESPONSE_CODE,tplusExp.getErrorCode());
				throw tplusExp;

		}catch(Exception exp)
		{
				System.out.println("Exception while SignOn Request ."+exp);
				throw new TPlusException(TPlusCodes.APPL_ERR," Error: in SALE TRANX: "+exp.getMessage());
		}
		finally
		{

		}
	}

}