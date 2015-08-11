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

package com.transinfo.tplus.messaging;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.TPlusUtility;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.db.WriteLogDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.log.TransactionDataBean;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;
import com.transinfo.tplus.messaging.parser.TPlusISOMsg;

@SuppressWarnings({"static-access","unused"})
public class RequestProcessor extends IRequestProcessor
{

	public RequestProcessor(){}

	public RequestBaseHandler getRequestHandler(IParser objISO)throws TPlusException
	{

		String reqHandler =null;

		/*try
		{

		Class classObj =  Class.forName("com.transinfo.tplus.messaging.credit.jcb.SaleRequest");
		RequestBaseHandler objReqHandler = (RequestBaseHandler)classObj.newInstance();
		return objReqHandler;

		}
		catch(Exception exp)
		{
			throw new OnlineException("96","A05374","Error in RequestProcessor:Unable to create request handler");

		}*/

		try
		{
			System.out.println(objISO.getMTI());

			TransactionDB objTranx = new TransactionDB();


			objISO =objTranx.getRequestDetails(objISO);

			if(objISO ==null)
			{
				return null;
			}

			ISOMsg actualReq = objISO.clone();

			System.out.println("&*&*&*&*&*&"+objISO.getConnectionName());


			objISO.setMsgObject(actualReq);
			reqHandler = objISO.getReqHandler();



			if(reqHandler !=null)
			{

				TPlusPrintOutput.printMessage("RequestBaseHandler","Creating Handler Class for="+reqHandler);
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("RequestProcessor: Creating Handler Class for "+reqHandler);

				Class classObj =  Class.forName(reqHandler);
				RequestBaseHandler objReqHandler = (RequestBaseHandler)classObj.newInstance();
				return objReqHandler;
			}
			else
			{

				return null;
			}

		}
		catch(TPlusException tplusexce)
		{
			throw new OnlineException("12","A05374","Error in RequestProcessor:Unable to create request handler");
		}catch (OnlineException e) {
			throw e;
		}
		catch(Exception exp)
		{
			throw new OnlineException("96","A05374","Error in RequestProcessor:Unable to create request handler");

		}


	}

	public IParser processRequest(IParser objISO)  throws TPlusException
	{

		byte[] byteResponse = null;
		IParser objResISO=null;

		try
		{

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("RequestProcessor:In ProcessRequest...");
			TPlusPrintOutput.printMessage("RequestProcessor","In ProcessRequest...");

			System.out.println("Populating Data1..");
			com.transinfo.tplus.log.TransactionDataBean objTransactionDataBean = new com.transinfo.tplus.log.TransactionDataBean();
			objTransactionDataBean.populateData(objISO);

			TransactionDB objTranxDB = new TransactionDB();

			objISO.setTransactionDataBean(objTransactionDataBean);
			TransactionDataBean objTranxBean = objISO.getTransactionDataBean();

			objTranxBean =objTranxDB.checkOffusTransaction(objISO);

			System.out.println("Populating Data Completed..");

			RequestBaseHandler objReqHandler = getRequestHandler(objISO);

			objTransactionDataBean.setTranxCode(objISO.getTranxType());

			if(objReqHandler !=null)
			{

				objISO = objReqHandler.execute(objISO);

				TPlusPrintOutput.printMessage("RequestProcessor","Request Processed Successfully and Response sent"+objISO);
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("RequestProcessor:Request Processed Successfully and Response sent... ");
			}
			else
			{
				objISO.setValue(39,"12");
				objISO.setRemarks("Invalid Transaction (MTI or ProcessingCode)");

				TPlusPrintOutput.printMessage("RequestProcessor","Invalid Transaction  Conn-MTI-ProcessingCode  "+ objISO.getConnectionId()+" - "+objISO.getValue(TPlusISOCode.MTI)+" - "+objISO.getValue(TPlusISOCode.PROCESSING_CODE));
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("RequestProcessor :Invalid Transaction  Conn-MTI-ProcessingCode  "+ objISO.getConnectionId()+" - "+objISO.getValue(TPlusISOCode.MTI)+"- "+objISO.getValue(TPlusISOCode.PROCESSING_CODE));
				//MonitorLogListener monitorLogListener = MonitorLogListener.getMonitorLogListener(8005);
				//monitorLogListener.sendSystemLog("Invalid Transaction  Conn-MTI-ProcessingCode  "+ objISO.getConnectionId()+" - "+objISO.getValue(TPlusISOCode.MTI)+" - "+objISO.getValue(TPlusISOCode.PROCESSING_CODE),MonitorLogListener.RED);
				throw new OnlineException("12","A05374","Error in RequestProcessor:unable to found request handler");

			}
		}
		catch(OnlineException onlineExp)
		{
			System.out.println("OnLINBE Exp Happened");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Online Exception Happened.."+onlineExp.getDescription());

			objISO.getTransactionDataBean().setResponseCode(onlineExp.getResponseCode());
			objISO.getTransactionDataBean().setRemarks(onlineExp.getDescription());

			if(objISO.getCloneISO() !=null)
			{
				objISO.setMsgObject(objISO.getCloneISO());
			}

			objISO.setValue(39,onlineExp.getResponseCode());

			try
			{

				WriteLogDB objWriteLogDb = new WriteLogDB();
				objWriteLogDb.updateLog(objISO.getTransactionDataBean());
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Transaction Inserted....");
				System.out.println("Transaction Inserted");

			}
			catch(Exception e)
			{
				e.printStackTrace();
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Error Inserting Transaction...");
				System.out.println("Error Inserting Transaction...");
				objISO.setValue(39,"96");
			}

		}
		catch(TPlusException tplusExp)
		{
			//Set Response
			System.out.println("###############ERROR="+tplusExp.getMessage());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("RequestProcessor:Error in processing the message: "+tplusExp.getMessage());
			throw tplusExp;
		}
		catch(Exception exp)
		{
			//Set Response
			System.out.println("ERROR="+exp.getMessage());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("RequestProcessor:Error in processing the message: "+exp);
			throw new TPlusException(TPlusCodes.ERR_REQ,"Error: in RequestProcessor= "+exp.toString());
		}
		finally
		{

		}

		System.out.println("------------------- Response SEND-----------------------");
		return objISO;
	}


}