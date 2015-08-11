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

package com.transinfo.tplus.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oracle.jdbc.driver.OracleTypes;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.tlv.TLVList;
import org.jpos.tlv.TLVMsg;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.TransactionException;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.AccountChangeLogBean;
import com.transinfo.tplus.javabean.AccountInfo;
import com.transinfo.tplus.javabean.CardInfo;
import com.transinfo.tplus.javabean.CoreBankReqResBean;
import com.transinfo.tplus.javabean.RiskControlRule;
import com.transinfo.tplus.javabean.TranxInfo;
import com.transinfo.tplus.log.TransactionDataBean;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.TPlus8583Parser;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;
import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.DBManagerCB;
import com.transinfo.tplus.util.DBUtil;
import com.transinfo.tplus.util.DateUtil;
import com.transinfo.tplus.util.StringUtil;
import com.transinfo.tplus.util.TPlusResultSet;

/**
 *
 * @author  Owner
 */

@SuppressWarnings({ "unused", "unchecked", "null" })
public class TransactionDB
{

	static HashMap writeoffMap = new HashMap();
	static
	{

		writeoffMap.put("0","DPD0");
		writeoffMap.put("1","DPD30");
		writeoffMap.put("2","DPD60");
		writeoffMap.put("3","DPD90");
		writeoffMap.put("4","DPD120");
	}


	/** Creates a new instance of TransactionDB */
	// TO BE REMOVE
	public TransactionDB()throws Exception
	{
		DBManager.initDBPool();
		DBManagerCB.initDBPool();
	}


	public Map getRequestHandler(String strIssuerId,String strMTI,String strProcessingCode)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getRequestHandler");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strIssuer = null;
		Map objReqMap = null;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT REQUEST_HANDLER,REQUEST_TYPE FROM REQ_LOOKUP WHERE ISSUER_ID='"+ strIssuerId +"' AND MESSAGE_TYPE= '"+ strMTI +"' AND PROCESSING_CODE='"+strProcessingCode.substring(0,2)+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				System.out.println("record available");
				objReqMap = new HashMap();
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				objReqMap.put("REQHANDLER", objRs.getString("REQUEST_HANDLER"));
				objReqMap.put("REQTYPE", objRs.getString("REQUEST_TYPE"));
			}
		}
		catch(Exception exp)
		{
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error"+exp);
		}

		return objReqMap;
	}


	public Map getSignOnList()throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getSignOnList");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strIssuer = null;
		Map signonMap = new HashMap();
		TPlus8583Parser objISO=null;


		try
		{
			System.out.println(" getSignOnList....");

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT CONNECTION_NAME,CLASS,SIGNON_CLASS,SIGNOFF_CLASS,ISSUER_HOST,ISSUER_PORT,STATION_ID,SIGNON_NEEDED FROM CONNECTION WHERE SIGNON='Y'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			while (objRs.next())
			{

				System.out.println("record available");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");

				objISO = new TPlus8583Parser();
				objISO.setIssuerHost(objRs.getString("ISSUER_HOST"));
				objISO.setIssuerPort(objRs.getString("ISSUER_PORT"));
				objISO.setStationId(objRs.getString("STATION_ID"));
				objISO.setConnectionName(objRs.getString("CONNECTION_NAME"));
				objISO.setClassName(objRs.getString("CLASS"));
				objISO.setSignOnClass(objRs.getString("SIGNON_CLASS"));
				objISO.setSignOffClass(objRs.getString("SIGNOFF_CLASS"));
				signonMap.put(objRs.getString("CONNECTION_NAME"),objISO);
				objISO.setSignOnNeeded(objRs.getString("SIGNON_NEEDED"));

			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error"+exp);
		}

		return signonMap;
	}

	public Map getSignOnList(String scheme) throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getSignOnList");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strIssuer = null;
		Map signonMap = new HashMap();
		TPlus8583Parser objISO=null;


		try
		{
			System.out.println("getSignOnList(String scheme)....");

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT CONNECTION_NAME,CLASS,SIGNON_CLASS,SIGNOFF_CLASS,ISSUER_HOST,ISSUER_PORT,STATION_ID,SIGNON_NEEDED FROM CONNECTION WHERE  SIGNON='Y' AND CONNECTION_NAME='"+scheme+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			while (objRs.next())
			{

				System.out.println("record available");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");

				objISO = new TPlus8583Parser();
				objISO.setIssuerHost(objRs.getString("ISSUER_HOST"));
				objISO.setIssuerPort(objRs.getString("ISSUER_PORT"));
				objISO.setStationId(objRs.getString("STATION_ID"));
				objISO.setConnectionName(objRs.getString("CONNECTION_NAME"));
				objISO.setClassName(objRs.getString("CLASS"));
				objISO.setSignOnClass(objRs.getString("SIGNON_CLASS"));
				objISO.setSignOffClass(objRs.getString("SIGNOFF_CLASS"));
				signonMap.put(objRs.getString("CONNECTION_NAME"),objISO);
				objISO.setSignOnNeeded(objRs.getString("SIGNON_NEEDED"));

			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error"+exp);
		}

		return signonMap;
	}

	public Map getReSignOnList()throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getSignOnList");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strIssuer = null;
		Map signonMap = new HashMap();
		TPlus8583Parser objISO=null;


		try
		{
			System.out.println(" getSignOnList....");

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT CONNECTION_NAME,CLASS,SIGNON_CLASS,SIGNOFF_CLASS,ISSUER_HOST,ISSUER_PORT,STATION_ID,SIGNON_NEEDED FROM CONNECTION WHERE SIGNON='Y' AND SIGNON_NEEDED='Y' AND CONNECTION_NAME='VISA'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Resignon");
			System.out.println("TransactionDB:Resignon");
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			while (objRs.next())
			{

				System.out.println("record available");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");

				objISO = new TPlus8583Parser();
				objISO.setIssuerHost(objRs.getString("ISSUER_HOST"));
				objISO.setIssuerPort(objRs.getString("ISSUER_PORT"));
				objISO.setStationId(objRs.getString("STATION_ID"));
				objISO.setConnectionName(objRs.getString("CONNECTION_NAME"));
				objISO.setClassName(objRs.getString("CLASS"));
				objISO.setSignOnClass(objRs.getString("SIGNON_CLASS"));
				objISO.setSignOffClass(objRs.getString("SIGNOFF_CLASS"));
				objISO.setSignOnNeeded(objRs.getString("SIGNON_NEEDED"));
				signonMap.put(objRs.getString("CONNECTION_NAME"),objISO);


			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error"+exp);
		}

		return signonMap;
	}

	public Map getSwitchDetailsByCard(String strCardNumber)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCardDetails");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strIssuer = null;
		Map cardMap = new HashMap();

		try
		{
			System.out.println(" getSwitchDetailsByCard....");

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT ISSUER_ID,SOURCE,DESTINATION,SOURCE_BITS,DESTINATION_BITS,ISSUER_HOST,ISSUER_PORT FROM CARD_TYPES WHERE "+ strCardNumber +">= CARD_LOW AND "+ strCardNumber +"<= CARD_HIGH");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				System.out.println("record available");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				cardMap.put("ISSUER",objRs.getString("ISSUER_ID"));
				cardMap.put("SOURCE",objRs.getString("SOURCE"));
				cardMap.put("DESTINATION",objRs.getString("DESTINATION"));
				cardMap.put("SOURCE_BITS",objRs.getString("SOURCE_BITS"));
				cardMap.put("DESTINATION_BITS",objRs.getString("DESTINATION_BITS"));
				cardMap.put("ISSUER_HOST",objRs.getString("ISSUER_HOST"));
				cardMap.put("ISSUER_PORT",objRs.getString("ISSUER_PORT"));

			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error"+exp);
		}

		return cardMap;
	}


	public Map getSwitchDetailsByTerminal(String strTerminalNo)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getSwitchDetailsByTerminal");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strIssuer = null;
		Map cardMap = new HashMap();


		try
		{
			System.out.println(" getSwitchDetailsByTerminal....");

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT ISSUER_ID,SOURCE,DESTINATION,SOURCE_BITS,DESTINATION_BITS,ISSUER_HOST,ISSUER_PORT FROM CARD_TYPES");
			sbfDTDVal.append(" WHERE CARD_TYPE_ID='00' and ISSUER_ID=(SELECT ISSUER_ID FROM TERMINAL WHERE TERMINAL_NO='"+strTerminalNo.trim()+"')");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				System.out.println("record available");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				cardMap.put("ISSUER",objRs.getString("ISSUER_ID"));
				cardMap.put("SOURCE",objRs.getString("SOURCE"));
				cardMap.put("DESTINATION",objRs.getString("DESTINATION"));
				cardMap.put("SOURCE_BITS",objRs.getString("SOURCE_BITS"));
				cardMap.put("DESTINATION_BITS",objRs.getString("DESTINATION_BITS"));
				cardMap.put("ISSUER_HOST",objRs.getString("ISSUER_HOST"));
				cardMap.put("ISSUER_PORT",objRs.getString("ISSUER_PORT"));

			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error"+exp);
		}

		return cardMap;
	}



	public Map getMessageDetails(String strIssuerId, String strTranxType)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCardDetails");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strIssuer = null;
		Map bitMap = null;


		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT ISSUER_ID,SOURCE,DESTINATION,SOURCE_BITS,DESTIN_BITS FROM MESSAGE_BITS WHERE ISSUER_ID='"+ strIssuerId+"' AND TRANX_TYPE='"+ strTranxType +"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				bitMap = new HashMap();
				System.out.println("record available");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				bitMap.put("ISSUER",objRs.getString("ISSUER_ID"));
				bitMap.put("SOURCE",objRs.getString("SOURCE"));
				bitMap.put("DESTINATION",objRs.getString("DESTINATION"));
				bitMap.put("SOURCE_BITS",objRs.getString("SOURCE_BITS"));
				bitMap.put("DESTINATION_BITS",objRs.getString("DESTIN_BITS"));

				System.out.println("Destination="+(String)bitMap.get("DESTINATION"));

			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getMessageDetails"+exp);
		}

		return bitMap;
	}



	public String getMessageHandler(String strMsgType)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getMsgHandler");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strMsgProcessor = null;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT MESSAGE_PROCESSOR FROM MESSAGE_CONFIG WHERE MESSAGE_TYPE = '"+ strMsgType+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				TPlusPrintOutput.printMessage("MessageConverter","Message Handler Available..");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Message Handler Available..");
				strMsgProcessor = objRs.getString("MESSAGE_PROCESSOR");
			}


		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getMessageHandler"+exp);
		}

		return strMsgProcessor;
	}

	public String getTranxType(String strMsgType,String strProcessingCode)
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getMsgHandler");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		String strTranxType = null;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT TRANX_TYPE FROM REQ_LOOKUP WHERE MESSAGE_TYPE = '"+ strMsgType+"' AND PROCESSING_CODE='"+strProcessingCode.substring(0,2)+"' AND CARD_TYPE_ID='88'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				TPlusPrintOutput.printMessage("MessageConverter","Message Handler Available..");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Message Handler Available..");
				strTranxType = objRs.getString("TRANX_TYPE");
			}


		}
		catch(Exception exp)
		{
			System.out.println(exp);
			//throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getMessageHandler"+exp);
		}

		return strTranxType;
	}




	public IParser getRequestDetails(IParser objISO)throws TPlusException
	{
		try
		{

			/*Map cardIssuerMap = getCardType(objISO);
			if(cardIssuerMap == null)
			{
				System.out.println("Card Issuer Map is Null");
				return null;
			}
			else
			{
				objISO.setIssuer((String)cardIssuerMap.get("ISSUERID"));
				objISO.setCardTypeId((String)cardIssuerMap.get("CARDTYPEID"));

				// objISO.setConnectionId((String)cardIssuerMap.get("CONNECTIONID"));
				// objISO.setCardTypeId((String)cardIssuerMap.get("CARDTYPEID"));
			}
			System.out.println("Geting messgae Details");*/

			objISO = getMessageDetails(objISO);

		}catch(TPlusException tplusExp)
		{
			throw tplusExp;
		}
		catch(Exception exp)
		{
			System.out.println("getRequestDetails: Error="+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Exception at getRequestDetails "+exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getRequestDetails"+exp);

		}


		return objISO;
	}



	public IParser getMessageDetails(IParser objISO)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getMessageDetails");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;

		try {
			StringBuffer sbfDTDVal = new StringBuffer();

			if(objISO.getCardNumber()!=null && !objISO.getCardNumber().trim().equals("")) {

				sbfDTDVal.append("SELECT RL.TRANX_TYPE,RL.REQUEST_HANDLER ");
				sbfDTDVal.append("FROM REQ_LOOKUP RL ");
				sbfDTDVal.append("WHERE ");
				sbfDTDVal.append("RL.MESSAGE_TYPE='"+objISO.getMTI()+"' ");
				sbfDTDVal.append("AND RL.PROCESSING_CODE='"+objISO.getValue(TPlusISOCode.PROCESSING_CODE).substring(0,2)+"' ");

			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next()) {

				TPlusPrintOutput.printMessage("TransactionDB","Record  available..."+objRs.getString("TRANX_TYPE"));
				objISO.setTranxType(objRs.getString("TRANX_TYPE"));
				objISO.setReqHandler(objRs.getString("REQUEST_HANDLER"));

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Message Details inserted ");
			}
			else {
				System.out.println("Card and Issuer record not available");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Card and Issuer record not available ");

			}
		}
		catch(Exception exp) {
			System.out.println(exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Exception while retriving the Card and Issue info:Error "+exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getMessageDetails"+exp);

		}

		return objISO;


	}
	//DELETED

	/*public IParser getMessageDetails(IParser objISO)throws TPlusException
{

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getMessageDetails");

			DBManager objDBMan = new DBManager();
			TPlusResultSet objRs = null;
			boolean bolExecute=false;
			boolean recAvailable = false;

			try
			{
					StringBuffer sbfDTDVal = new StringBuffer();

					if(objISO.getCardNumber()!=null && !objISO.getCardNumber().trim().equals(""))
					{

						sbfDTDVal.append("SELECT CT.CARD_TYPE_ID,RL.TRANX_TYPE,RL.REQUEST_HANDLER,C.CONNECTION_NAME,C.CLASS,C.STATION_ID,C.ISSUER_HOST,C.ISSUER_PORT,C.ACQ_INST_ID,C.SIGNON_NEEDED ");
						sbfDTDVal.append("FROM CARD_TYPES CT,REQ_LOOKUP RL,CONNECTION C ");
						sbfDTDVal.append("WHERE ");
						sbfDTDVal.append("CT.CONNECTION_ID = RL.CONNECTION_ID AND ");
						sbfDTDVal.append("CT.CONNECTION_ID = C.CONNECTION_ID AND ");
						sbfDTDVal.append("CT.CONNECTION_ID='"+objISO.getConnectionId()+"' AND MESSAGE_TYPE='"+objISO.getValue(TPlusISOCode.MTI)+"' AND PROCESSING_CODE='"+objISO.getValue(TPlusISOCode.PROCESSING_CODE).substring(0,2)+"' AND ");
						sbfDTDVal.append("RL.TRANX_TYPE=(SELECT TRANX_TYPE FROM REQ_LOOKUP WHERE CONNECTION_ID='"+objISO.getConnectionId()+"' AND MESSAGE_TYPE='"+objISO.getValue(TPlusISOCode.MTI)+"' AND PROCESSING_CODE='"+objISO.getValue(TPlusISOCode.PROCESSING_CODE).substring(0,2)+"')");
					}
					else
					{
						sbfDTDVal.append("SELECT CT.CARD_TYPE_ID,CT.ISSUER_ID,RL.TRANX_TYPE,RL.REQUEST_HANDLER,C.CONNECTION_NAME,C.CLASS,C.STATION_ID,C.ISSUER_HOST,C.ISSUER_PORT,C.ACQ_INST_ID,C.SIGNON_NEEDED ");
						sbfDTDVal.append("FROM CT_COMMON CT,REQ_LOOKUP RL,CONNECTION C  ");
						sbfDTDVal.append("WHERE ");
						sbfDTDVal.append("CT.CONNECTION_ID = RL.CONNECTION_ID AND ");
						sbfDTDVal.append("CT.CONNECTION_ID = C.CONNECTION_ID AND ");
						sbfDTDVal.append("CT.CONNECTION_ID='"+objISO.getConnectionId()+"' AND MESSAGE_TYPE='"+objISO.getValue(TPlusISOCode.MTI)+"' AND PROCESSING_CODE='"+objISO.getValue(TPlusISOCode.PROCESSING_CODE).substring(0,2)+"' AND ");
						sbfDTDVal.append("RL.TRANX_TYPE=(SELECT TRANX_TYPE FROM REQ_LOOKUP WHERE CONNECTION_ID='"+objISO.getConnectionId()+"' AND MESSAGE_TYPE='"+objISO.getValue(TPlusISOCode.MTI)+"' AND PROCESSING_CODE='"+objISO.getValue(TPlusISOCode.PROCESSING_CODE).substring(0,2)+"')");

				 	}

					if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
					System.out.println(" SQL="+ sbfDTDVal.toString());
					bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
					objRs = objDBMan.getResultSet();

					if (objRs.next())
					{

						TPlusPrintOutput.printMessage("TransactionDB","Record  available..."+objRs.getString("TRANX_TYPE"));
						objISO.setAcqInstId(objRs.getString("ACQ_INST_ID"));
					 	objISO.setTranxType(objRs.getString("TRANX_TYPE"));
					 	objISO.setReqHandler(objRs.getString("REQUEST_HANDLER"));
					 	objISO.setIssuerHost(objRs.getString("ISSUER_HOST"));
					 	objISO.setIssuerPort(objRs.getString("ISSUER_PORT"));
					 	objISO.setConnectionName(objRs.getString("CONNECTION_NAME"));
						objISO.setClassName(objRs.getString("CLASS"));
						objISO.setStationId(objRs.getString("STATION_ID"));
						objISO.setSignOnNeeded(objRs.getString("SIGNON_NEEDED"));

					 	if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Message Details inserted ");
				 	}
				 	else
				 	{
						System.out.println("Card and Issuer record not available");
					 	if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Card and Issuer record not available ");

					}
			}
			catch(Exception exp)
			{
				System.out.println(exp);
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Exception while retriving the Card and Issue info:Error "+exp);
				throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getMessageDetails"+exp);

			}

		return objISO;


}*/

	public Map getCardType(IParser objISO)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCardType");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		Map cardIssMap = null;


		try {
			StringBuffer sbfDTDVal = new StringBuffer();

			if(objISO.getCardNumber() != null && !objISO.getCardNumber().trim().equals("")) {
				sbfDTDVal.append("SELECT CARD_TYPE_ID,ISSUER_ID FROM CARD_PRODUCTS WHERE BIN='"+objISO.getCardNumber().substring(0,6)+"'");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next()) {
				cardIssMap = new HashMap();
				System.out.println("Issuer record available");
				cardIssMap.put("CARDTYPEID", objRs.getString("CARD_TYPE_ID"));
				cardIssMap.put("ISSUERID", objRs.getString("ISSUER_ID"));
				System.out.println("IssuerId="+(String)cardIssMap.get("ISSUERID"));
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:CardType record available ");
			}
			else {
				System.out.println("Card and Issuer record not available");

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:CardType record not available ");

			}
		}
		catch(Exception exp) {
			System.out.println(exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Exception while retriving the CardType info:Error "+exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getCardAndIssuer "+exp);

		}

		return cardIssMap;
	}

	// REMOVE

	public Map getCardAndIssuer(IParser objISO)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCardAndIssuer");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		Map cardIssMap = null;


		try
		{
			System.out.println("1");
			StringBuffer sbfDTDVal = new StringBuffer();

			System.out.println(objISO.getCardNumber());

			if(objISO.getCardNumber() != null && !objISO.getCardNumber().trim().equals(""))
			{
				sbfDTDVal.append("SELECT CARD_TYPE_ID,CONNECTION_ID FROM CARD_TYPES WHERE "+ objISO.getCardNumber() +">= CARD_LOW AND "+ objISO.getCardNumber() +"<= CARD_HIGH");
			}
			else
			{
				sbfDTDVal.append("SELECT '21' as CONNECTION_ID, '00' as CARD_TYPE_ID  FROM DUAL");
			}


			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				cardIssMap = new HashMap();
				System.out.println("Issuer record available");
				cardIssMap.put("CARDTYPEID", objRs.getString("CARD_TYPE_ID"));
				cardIssMap.put("CONNECTIONID", objRs.getString("CONNECTION_ID"));
				System.out.println("CONNECTIONID="+(String)cardIssMap.get("CONNECTIONID"));
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Issuer record available ");
			}
			else
			{
				System.out.println("Card and Connection record not available");

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Card and Connection record not available ");

			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Exception while retriving the Card and Connection info:Error "+exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getCardAndIssuer"+exp);

		}

		return cardIssMap;
	}



	public int updateSignOn(String strSignReq)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updateSignOn");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE CONNECTION SET SIGNON_NEEDED='"+strSignReq+"' WHERE CONNECTION_NAME='VISA'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			rec = objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return rec;
	}



	/**
	 * This method is used to check whether Batch request is a duplicate request.
	 * @param intCapSalesCount,intCapSalesAmt
	 * @returns int. 1-> Duplicate 0 -> No Settelement has done
	 * @throws TPlusException
	 */

	public int isDuplicated(IParser objISO, String CapturedSalesAmount, String CapturedRefundAmount)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:isDuplicate");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int BatchTotalCount=-1;
		int intDup=0;
		final int TRACENO    = 11;
		final int MERCHANTID = 42;
		final int TERMINALID = 41;
		final int BATCHNUMER = 60;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT COUNT(*) AS RECORDCOUNT FROM BATCHLOG ,"+
					"BATCHLOGDETAIL WHERE BATCHLOG.BATCHLOGID = BATCHLOGDETAIL.BATCHLOGID"+
					" AND TERMINALID='" + objISO.getValue(TERMINALID) + "'"+
					" AND MERCHANTID = '" + objISO.getValue(MERCHANTID) +"'"+
					" AND BATCHNUMBER = " + new Integer(objISO.getValue(BATCHNUMER)).intValue() +
					" AND SALEAMT*100 = " + new Long(CapturedSalesAmount).longValue()+
					" AND REFUNDAMT*100 = " + new Long( CapturedRefundAmount).longValue() );
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				if(objRs.getInt("RECORDCOUNT")>0)
					intDup = 1;
				else
					intDup = 0;    // Not Duplicate
			}

			if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:isDuplicate = "+intDup);
			System.out.println("isDUP="+intDup);

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while checking isDuplicate"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return intDup;
	}


	public void updateSettlement(String strTerminalId)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB: updateSettlement :");


		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET SETTLED='Y' WHERE TerminalID='" + strTerminalId + "'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: updateSettlement"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block
	}

	/**
	 * This method is used to update the status of tranx record as deleted
	 * This only happens for the Settlement request
	 * @param strTerminalId,lngBatchNo
	 * @returns none
	 * @throws TPlusException
	 */


	public void deleteAuthLog(IParser objISO)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();

		try
		{

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String strMTI = objISO.getValue(90).substring(0,4);
			String strTraceNo = objISO.getValue(90).substring(4,10);
			String strTransimissionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
			sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo + "' ");
			sbfDTDVal.append(" AND TRANSMISSION_DATETIME='"+strTransimissionDt+"' AND ACQID='"+strAcqId+"'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}


	public void deleteCUPAuthLogNew(IParser objISO)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();

		try
		{

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String approvalCode = objISO.getValue(38);

			StringBuffer sbfDTDVal = new StringBuffer();

			if(objISO.getValue(90) != null && !"".equals(objISO.getValue(90))){

				String strMTI = objISO.getValue(90).substring(0,4);
				String strTraceNo = objISO.getValue(90).substring(4,10);
				String strTransimissionDt = objISO.getValue(90).substring(10,20);
				String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

				sbfDTDVal = new StringBuffer();

				sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
				sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo + "' ");
				sbfDTDVal.append(" AND TRANSMISSION_DATETIME='"+strTransimissionDt+"' AND ACQID='"+strAcqId+"'");

			}else{

				sbfDTDVal = new StringBuffer();

				sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
				sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TERMINALID='"+ strTID + "' ");
				sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
				sbfDTDVal.append(" AND APPROVALCODE='"+approvalCode+"' ");

			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}



	/**
	 * This method is used to update the status of tranx record as deleted
	 * This only happens for the Settlement request
	 * @param strTerminalId,lngBatchNo
	 * @returns none
	 * @throws TPlusException
	 */


	public int updateReversal(IParser objISO,String strOrgTraceNo)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();
		int bolExecute=0;
		final int TERMINALID = 41;
		final int CARDNUMBER = 2;
		final int TRACENO = 11;
		final int INVOICENO = 62;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE TERMINALID='"+objISO.getValue(TERMINALID)+"'");
			if(objISO.getCardNumber()!=null && !objISO.getCardNumber().equals(""))
			{
				sbfDTDVal.append(" AND CARDNUMBER='" + objISO.getCardNumber()+"'");
			}
			sbfDTDVal.append(" AND TRACENO='" +objISO.getValue(TRACENO) +"'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

			sbfDTDVal = new StringBuffer();

			if(!strOrgTraceNo.equals(objISO.getValue(TRACENO)))
			{
				sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'N' WHERE TERMINALID='" +
						objISO.getValue(TERMINALID) + "' AND CARDNUMBER='" + objISO.getValue(CARDNUMBER) +
						"' AND TRACENO='" +strOrgTraceNo +"' AND TRANXCODE <> 'VOID'");
				System.out.println(" mySQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());
			}

			return bolExecute;


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	public TranxInfo recordExistsReversal(IParser objISO)throws Exception
	{
		//TransactionDataBean objTransactionDataBean = new TransactionDataBean();
		int recordExists = 2;

		DBManager objDBMan = new DBManager();
		TranxInfo objTranxInfo = null;
		TPlusResultSet objRs = null;

		try
		{
			String strCardno = objISO.getValue(2);
			String strTID = objISO.getValue(41);
			String strTraceNo = objISO.getValue(90).substring(4,10);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNo:"+strTraceNo);

//			String strTransimisionDt = objISO.getValue(90).substring(10,20);

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE ");
			sbfDTDVal.append(" TRACENO='" + strTraceNo + "' ");

			sbfDTDVal.append(" AND CARDNUMBER='" + strCardno + "' ");

			sbfDTDVal.append(" AND DELETED = 'N' AND RESPONSECODE = '00' AND TRANXCODE <> 'REVERSAL'");

			sbfDTDVal.append(" AND TERMINALID='" + strTID + "'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				objTranxInfo = new TranxInfo();
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setTranx_dateTime(objRs.getString("TRANX_DATETIME"));
			}

		}catch(Exception vep)
		{
			System.out.println("Exception while getting Total Txn for given card : "+vep.toString());
			throw vep;
		}

		return objTranxInfo;

	}


	public TranxInfo getPreAuthTranx(IParser objISO, String refNo)throws Exception
	{
		//TransactionDataBean objTransactionDataBean = new TransactionDataBean();
		int recordExists = 2;

		DBManager objDBMan = new DBManager();
		TranxInfo objTranxInfo = null;
		TPlusResultSet objRs = null;

		try
		{

			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE ");
			sbfDTDVal.append(" TERMINALID='" + strTID + "'");

			if(strCardno != null && !strCardno.equals(""))
			{
				sbfDTDVal.append(" AND CARDNUMBER='" + strCardno + "' ");
			}

			sbfDTDVal.append(" AND REFNO='" + refNo + "' AND DELETED = 'N' AND RESPONSECODE = '00' AND TRANXCODE = 'PREAUTH'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				objTranxInfo = new TranxInfo();
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				objTranxInfo.setTransmissionDateTime(objRs.getString("TRANSMISSION_DATETIME"));
				//objTranxInfo.setTranxTime(objRs.getString("TRANXTIME"));
				//objTranxInfo.setTranxDate(objRs.getString("TRANXDATE"));
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setRefNo(objRs.getString("REFNO"));
				objTranxInfo.setResponseCode(objRs.getString("RESPONSECODE"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				//objTranxInfo.setF55Res(objRs.getString("F55_RESPONSE"));
				objTranxInfo.setOrgTraceNo(objRs.getString("TRACENO2"));
				objTranxInfo.setMTI(objRs.getString("MTI"));
				//objTranxInfo.setCupCutoff(objRs.getString("CUP_CUTOFF"));
				objTranxInfo.setTranxCardHolderAmt(Double.valueOf(objRs.getString("TRANX_CARDHOLDER_AMT")).doubleValue());
				objTranxInfo.setTranxLogId(objRs.getString("TRANXLOGID"));
				objTranxInfo.setTranxCurrConvAmt(objRs.getDouble("TRANX_CURRCONV_AMT"));

			}

		}catch(Exception vep)
		{
			System.out.println("Exception while getting Total Txn for given card : "+vep.toString());
			throw vep;
		}

		return objTranxInfo;

	}

	public int updateReversalAndVoid(IParser objISO, String orgTraceNo, String accountId, double amount, String tranxCode)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();
		int bolExecute=0;
		final int TERMINALID = 41;
		final int CARDNUMBER = 2;
		final int TRACENO = 11;
		final int INVOICENO = 62;

		try
		{

			String sign = "-";

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String strMTI = objISO.getValue(90).substring(0,4);

			String strTraceNo = objISO.getValue(90).substring(4,10);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNo:"+strTraceNo);

			String strTransimissionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			String strTraceNoFromIso = objISO.getValue(11);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNoFromIso:"+strTraceNoFromIso);
			if(strTraceNo == null || "000000".equals(strTraceNo)){
				strTraceNo = strTraceNoFromIso;
			}

			String refNo = "";
			if(strTID == null || "".equals(strTID)){
				refNo = objISO.getValue(37);
			}

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
			sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo + "' ");

			String cardSheme = objISO.getCardProduct();

			if((objISO.getValue(25).equals("59") && "VI".equals(cardSheme)) || (objISO.getValue(25).equals("05") && "CU".equals(cardSheme)) )
			{
				sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
			}else{

				if(strTID != null && !"".equals(strTID)){
					sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
				}

				if(refNo != null && !"".equals(refNo)){
					sbfDTDVal.append(" AND REFNO='" + refNo + "' ");
				}
			}

			/*if(!objISO.getValue(25).equals("59"))
			{
				sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
			}else{
				sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
			}*/

			//sbfDTDVal.append(" AND TRANSMISSION_DATETIME='"+strTransimissionDt+"' AND ACQID='"+strAcqId+"'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

			sbfDTDVal = new StringBuffer();

			if(!orgTraceNo.equals(strTraceNo))
			{
				sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'N' WHERE ");
				sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ orgTraceNo + "' AND TRANXCODE <> 'VOID' ");

				if((objISO.getValue(25).equals("59") && "VI".equals(cardSheme)) || (objISO.getValue(25).equals("05") && "CU".equals(cardSheme)) )
				{
					sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
				}else{
					sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
				}

				/*if(!objISO.getValue(25).equals("59"))
				{
					sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
				}else{
					sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
				}*/

				System.out.println(" mySQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

				sign = "+";
			}

			double purchaseAmount = amount;
			double cashAmount = 0;

			if("CASH".equals(tranxCode)){
				purchaseAmount = 0;
				cashAmount = amount;
			}

			// update the customer limit value
			updateLimitUsed(accountId, purchaseAmount, cashAmount, sign,strCardno,tranxCode);

			return bolExecute;


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	public int updateReversalAndVoidPartial(IParser objISO, String orgTraceNo, String accountId, double amount, String tranxCode, boolean isDeleteRec)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();
		int bolExecute=0;
		final int TERMINALID = 41;
		final int CARDNUMBER = 2;
		final int TRACENO = 11;
		final int INVOICENO = 62;

		try
		{

			String sign = "-";

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String strMTI = objISO.getValue(90).substring(0,4);

			String strTraceNo = objISO.getValue(90).substring(4,10);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNo:"+strTraceNo);

			String strTransimissionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			String strTraceNoFromIso = objISO.getValue(11);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNoFromIso:"+strTraceNoFromIso);
			if(strTraceNo == null || "000000".equals(strTraceNo)){
				strTraceNo = strTraceNoFromIso;
			}

			String refNo = "";
			if(strTID == null || "".equals(strTID)){
				refNo = objISO.getValue(37);
			}

			String cardSheme = objISO.getCardProduct();

			StringBuffer sbfDTDVal = new StringBuffer();

			if(isDeleteRec){

				sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
				sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo + "' ");

				if((objISO.getValue(25).equals("59") && "VI".equals(cardSheme)) || (objISO.getValue(25).equals("05") && "CU".equals(cardSheme)) )
				{
					sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
				}else{

					if(strTID != null && !"".equals(strTID)){
						sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
					}

					if(refNo != null && !"".equals(refNo)){
						sbfDTDVal.append(" AND REFNO='" + refNo + "' ");
					}
				}

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
				System.out.println(" mySQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

			}

			sbfDTDVal = new StringBuffer();

			if(!orgTraceNo.equals(strTraceNo))
			{
				if(isDeleteRec){

					sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'N' WHERE ");
					sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ orgTraceNo + "' AND TRANXCODE <> 'VOID' ");

					if((objISO.getValue(25).equals("59") && "VI".equals(cardSheme)) || (objISO.getValue(25).equals("05") && "CU".equals(cardSheme)) )
					{
						sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
					}else{
						sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
					}

					System.out.println(" mySQL="+ sbfDTDVal.toString());
					bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

				}

				sign = "+";
			}

			double purchaseAmount = amount;
			double cashAmount = 0;

			if("CASH".equals(tranxCode)){
				purchaseAmount = 0;
				cashAmount = amount;
			}

			// update the customer limit value
			updateLimitUsed(accountId, purchaseAmount, cashAmount, sign,strCardno,tranxCode);

			return bolExecute;


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	public int updateRefundReversalAndVoid(IParser objISO, String orgTraceNo, String accountId, double amount)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();
		int bolExecute=0;
		final int TERMINALID = 41;
		final int CARDNUMBER = 2;
		final int TRACENO = 11;
		final int INVOICENO = 62;

		try
		{

			String sign = "+";

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String strMTI = objISO.getValue(90).substring(0,4);
			String strTraceNo = objISO.getValue(90).substring(4,10);
			String strTransimissionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
			sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo + "' ");
			sbfDTDVal.append(" AND TRANSMISSION_DATETIME='"+strTransimissionDt+"' AND ACQID='"+strAcqId+"'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

			sbfDTDVal = new StringBuffer();

			if(!orgTraceNo.equals(strTraceNo))
			{
				sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'N' WHERE TERMINALID='" +
						strTID + "' AND CARDNUMBER='" + strCardno +
						"' AND TRACENO='" +orgTraceNo +"' AND TRANXCODE <> 'VOID'");
				System.out.println(" mySQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

				sign = "-";
			}

			// update the customer limit value
			updateLimitUsed(accountId, amount, 0, sign,strCardno,"REFUND");

			return bolExecute;


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	public int updateAuthCompleteReversalAndVoid(IParser objISO, String orgTraceNo, String accountId, double amount, String preAuthTranxId, double preAuthAmount)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();
		int bolExecute=0;
		final int TERMINALID = 41;
		final int CARDNUMBER = 2;
		final int TRACENO = 11;
		final int INVOICENO = 62;

		try
		{

			String sign = "+";
			String comStatus = "N";

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String strMTI = objISO.getValue(90).substring(0,4);

			String strTraceNo = objISO.getValue(90).substring(4,10);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNo:"+strTraceNo);

			String strTransimissionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			String strTraceNoFromIso = objISO.getValue(11);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNoFromIso:"+strTraceNoFromIso);
			if(strTraceNo == null || "000000".equals(strTraceNo)){
				strTraceNo = strTraceNoFromIso;
			}


			String limitUsedSign = "+";
			double limitUsedAmt = 0;

			if(preAuthAmount < amount){
				limitUsedSign = "-";
				limitUsedAmt = amount-preAuthAmount;
			}else{
				limitUsedSign = "+";
				limitUsedAmt = preAuthAmount-amount;					
			}

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
			sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo + "' ");
			//sbfDTDVal.append(" AND TRANSMISSION_DATETIME='"+strTransimissionDt+"' AND ACQID='"+strAcqId+"'");

			if(!objISO.getValue(25).equals("59"))
			{
				sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
			}else{
				sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

			sbfDTDVal = new StringBuffer();

			if(!orgTraceNo.equals(strTraceNo))
			{

				if(preAuthAmount < amount){
					limitUsedSign = "+";
				}else{
					limitUsedSign = "-";				
				}

				/*sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'N' WHERE TERMINALID='" +
						strTID + "' AND CARDNUMBER='" + strCardno +
						"' AND TRACENO='" +orgTraceNo +"' AND TRANXCODE <> 'VOID'");*/

				sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'N' WHERE ");
				sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ orgTraceNo + "' AND TRANXCODE <> 'VOID' ");

				if(!objISO.getValue(25).equals("59"))
				{
					sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
				}else{
					sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
				}

				System.out.println(" mySQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

				sign = "-";
				comStatus = "C";
			}

			// update limit used
			updateLimitUsed(accountId, limitUsedAmt, 0, limitUsedSign,strCardno,"AUTHCOMPLETE");

			// update customer account
			updatePreAuthComplete(accountId, preAuthAmount, sign);

			// update preauth tranx table
			updatePreAuthTranx(preAuthTranxId, comStatus);

			return bolExecute;


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	public void deleteLog(IParser objISO)throws TPlusException,Exception
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Failed Log:");


		DBManager objDBMan = new DBManager();
		int bolExecute=0;
		Connection con=null;
		ResultSet objRs=null;


		try
		{

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String strMTI = objISO.getValue(90).substring(0,4);
			String strTraceNo = objISO.getValue(90).substring(4,10);
			String strTransimissionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
			sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo+"'");
			sbfDTDVal.append(" AND TRANSMISSION_DATETIME='"+strTransimissionDt+"' AND ACQID='"+strAcqId+"'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());

			bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting FailedLog"+e.getMessage());
		}

	}

	public void deleteLogDebit(IParser objISO)throws TPlusException,Exception
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:deleteLogDebit Log New Update:");
		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:deleteLogDebit Log:");


		DBManager objDBMan = new DBManager();
		int bolExecute=0;
		Connection con=null;
		ResultSet objRs=null;


		try
		{

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String strMTI = objISO.getValue(90).substring(0,4);

			String strTraceNo = objISO.getValue(90).substring(4,10);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNo:"+strTraceNo);

			String strTransimissionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			String strTraceNoFromIso = objISO.getValue(11);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNoFromIso:"+strTraceNoFromIso);
			if(strTraceNo == null || "000000".equals(strTraceNo)){
				strTraceNo = strTraceNoFromIso;
			}

			StringBuffer sbfDTDVal = new StringBuffer();

			/*sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
			sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo+"'");
			sbfDTDVal.append(" AND TRANSMISSION_DATETIME='"+strTransimissionDt+"' AND ACQID='"+strAcqId+"'");*/

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
			sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo + "' ");

			if(!objISO.getValue(25).equals("59"))
			{
				sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
				//sbfDTDVal.append(" AND TRANSMISSION_DATETIME='"+strTransimissionDt+"' AND ACQID='"+strAcqId+"'");
			}else{
				sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Done :: "+bolExecute);
			System.out.println("Done :: "+bolExecute);

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting FailedLog"+e.getMessage());
		}

	}

	public void deleteLogDebitVoidReversal(IParser objISO, String orgTraceNo)throws TPlusException,Exception
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:deleteLogDebitVoidReversal Log New Update:");
		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:deleteLogDebitVoidReversal Log:");


		DBManager objDBMan = new DBManager();
		int bolExecute=0;
		Connection con=null;
		ResultSet objRs=null;

		try
		{

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			String strMTI = objISO.getValue(90).substring(0,4);

			String strTraceNo = objISO.getValue(90).substring(4,10);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNo:"+strTraceNo);

			String strTransimissionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			String strTraceNoFromIso = objISO.getValue(11);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNoFromIso:"+strTraceNoFromIso);
			if(strTraceNo == null || "000000".equals(strTraceNo)){
				strTraceNo = strTraceNoFromIso;
			}

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE ") ;
			sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo + "' ");

			if(!objISO.getValue(25).equals("59"))
			{
				sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
			}else{
				sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Done :: "+bolExecute);
			System.out.println("Done :: "+bolExecute);

			// check void reversal or not and update
			sbfDTDVal = new StringBuffer();

			if(!orgTraceNo.equals(strTraceNo))
			{
				sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'N' WHERE ");
				sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACENO='"+ orgTraceNo + "' AND TRANXCODE <> 'VOID' ");

				if(!objISO.getValue(25).equals("59"))
				{
					sbfDTDVal.append(" AND TERMINALID='" + strTID + "' ");
				}else{
					sbfDTDVal.append(" AND MERCHANTID='"+strMid+"' ");
				}

				System.out.println(" mySQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Done void reversal :: "+bolExecute);
				System.out.println("Done void reversal :: "+bolExecute);

			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting FailedLog"+e.getMessage());
		}

	}


	/**
	 * This method is used to update the status of tranx record as deleted
	 * This only happens for the Settlement request
	 * @param strTerminalId,lngBatchNo
	 * @returns none
	 * @throws TPlusException
	 */


	public int updateAdjusted(IParser objISO)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();
		int  intRes=0;


		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Adjusted = 'Y' WHERE TERMINALID='" +
					objISO.getValue(TPlusISOCode.TERMINAL_ID) + "' AND REFNO='" +objISO.getValue(TPlusISOCode.REF_NO) + "' AND APPROVALCODE='" + objISO.getValue(TPlusISOCode.APPROVAL_CODE) + "'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			intRes =  objDBMan.executeUpdate(sbfDTDVal.toString());

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return intRes;
	}



	/**
	 * This method is used to update the status of tranx record as deleted for certain terminal
	 * This only happens for the Settlement request
	 * @param strTerminalId,lngBatchNo
	 * @returns none
	 * @throws TPlusException
	 */


	public void settleAllAuthLog(String strTerminalID)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete ALL Authorization Log:");


		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("UPDATE TRANXLOG SET SETTLED = 'Y' WHERE TERMINALID='" + strTerminalID + "' AND RECONCILIATION IN ('+','=')");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting all Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}


	/**
	 * This method is used to update the status of tranx record as deleted for certain terminal
	 * This only happens for the reversal request
	 * @param TplusISOPraser
	 * @returns none
	 * @throws TPlusException
	 */

	public void deleteFailedLog(IParser objISO)throws TPlusException,Exception
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Failed Log:");


		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;
		Statement statement=null;
		Connection con=null;
		ResultSet objRs=null;


		try
		{
			objDBMan = new DBManager();
			if(con==null)
			{
				con=objDBMan.getConnection();
			}

			con.setAutoCommit( false );



			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE  TRACENO='" + objISO.getValue(11) +
					"' AND CARDNUMBER='" + objISO.getCardNumber() + "'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			statement = con.createStatement();
			statement.executeUpdate(sbfDTDVal.toString());



			/*sbfDTDVal.append("DELETE TRANXLOG WHERE TERMINALID='" + objISO.getValue(41) +
			"' AND TRACENO='" + objISO.getValue(11) + "' AND TRANXCODE='REVERSAL'");
			"' AND CARDNUMBER='" + iso.getCardNumber() + "' AND TRACENO='" + iso.Fields[11] + "' AND TRANXCODE='REVERSAL'"

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			statement = con.createStatement();
			statement.executeUpdate(sbfDTDVal.toString());*/

			con.commit();



		}
		catch (TPlusException tplusexp)
		{	con.rollback();
		throw tplusexp;
		}
		catch (Exception e)
		{
			con.rollback();
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting FailedLog"+e.getMessage());
		}
		finally
		{

			try
			{

				//con.commit();
				con.setAutoCommit( true );
				if(objRs!=null)
				{
					objRs.close();
				}
				if(statement!=null)
				{
					statement.close();
					statement=null;
				}
				if(con!=null)
				{
					objDBMan.closeConnection(con);
					con=null;
				}

			}
			catch(Exception exp)
			{
				//throw new MPIException(TPlusCodes.APPL_ERR,exp.getMessage());
			}
		} // End of the Main Try Block


	}


	/**
	 * This method is used to check the transaction is available in TranxLog.
	 * @param strTerminalId,strCardNumber,strRefNo,ApprovalCode
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */


	public TranxInfo getExistingTranx(String strTerminalID, String strCardNumber, String strRefNo, String strApprovalCode)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getExistingTranx");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		TranxInfo objTranxInfo = null;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE TERMINALID='" + strTerminalID + "' ");
			if(strCardNumber != null && !strCardNumber.equals(""))
			{
				sbfDTDVal.append("AND CARDNUMBER='" + strCardNumber + "' ");

			}

			sbfDTDVal.append(" AND REFNO='" + strRefNo + "' AND RESPONSECODE='00'");
			if(!strCardNumber.startsWith("888888") && !strCardNumber.startsWith("621354") && strApprovalCode!=null && !strApprovalCode.trim().equals(""))
			{
				sbfDTDVal.append(" AND APPROVALCODE = '"+strApprovalCode+"'");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				objTranxInfo = new TranxInfo();
				recAvailable = true;
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				objTranxInfo.setResponseCode(objRs.getString("RESPONSECODE"));
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setRefNo(objRs.getString("REFNO"));
				objTranxInfo.setTransmissionDateTime(objRs.getString("TRANSMISSION_DATETIME"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				//objTranxInfo.setF55Res(objRs.getString("F55_RESPONSE"));
				objTranxInfo.setOrgTraceNo(objRs.getString("TRACENO2"));
				objTranxInfo.setTranxCardHolderAmt(Double.valueOf(objRs.getString("TRANX_CARDHOLDER_AMT")).doubleValue());
				objTranxInfo.setIsAuthComVoid(objRs.getString("ISAUTHCOMVOID"));
				objTranxInfo.setTranxCurrConvAmt(objRs.getDouble("TRANX_CURRCONV_AMT"));
				objTranxInfo.setTranxCardHolderCurr(objRs.getString("TRANX_SETTLEMENT_CURR"));
				objTranxInfo.setPosEntryMode(objRs.getString("POSENTRYMODE"));

			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the ExistingTranx info"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objTranxInfo;
	}

	public TranxInfo getExistingCUPTranx(IParser objISO)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getExistingTranx");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		TranxInfo objTranxInfo = null;

		try
		{

			String strMid = objISO.getValue(42);
			String strTerminalID = objISO.getValue(41);
			String strApprovalCode = objISO.getValue(38);

			String strCardNumber =objISO.getCardNumber();

			String strMTI = "";
			String strTraceNo = "";
			String strAcqId = "";

			if(objISO.getValue(90) != null && !"".equals(objISO.getValue(90))){

				strMTI = objISO.getValue(90).substring(0,4);
				strTraceNo = objISO.getValue(90).substring(4,10);
				strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strMTI:"+strMTI);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strTraceNo:"+strTraceNo);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("strAcqId:"+strAcqId);


			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE TERMINALID='" + strTerminalID + "' ");

			if(strCardNumber != null && !strCardNumber.equals(""))
			{
				sbfDTDVal.append(" AND CARDNUMBER='" + strCardNumber + "' ");
			}

			if(!"".equals(strTraceNo)){
				sbfDTDVal.append(" AND TRACENO='" + strTraceNo + "' ");
			}

			if(!"".equals(strMTI)){
				sbfDTDVal.append(" AND MTI='"+strMTI+"' ");
			}

			if(!"".equals(strAcqId)){
				sbfDTDVal.append(" AND ACQID='"+strAcqId+"' ");
			}

			sbfDTDVal.append(" AND RESPONSECODE='00' ");

			if(!strCardNumber.startsWith("888888") && !strCardNumber.startsWith("621354") && strApprovalCode!=null && !strApprovalCode.trim().equals(""))
			{
				sbfDTDVal.append(" AND APPROVALCODE = '"+strApprovalCode+"'");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				objTranxInfo = new TranxInfo();
				recAvailable = true;
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				objTranxInfo.setResponseCode(objRs.getString("RESPONSECODE"));
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setRefNo(objRs.getString("REFNO"));
				objTranxInfo.setTransmissionDateTime(objRs.getString("TRANSMISSION_DATETIME"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				//objTranxInfo.setF55Res(objRs.getString("F55_RESPONSE"));
				objTranxInfo.setOrgTraceNo(objRs.getString("TRACENO2"));
				objTranxInfo.setTranxCardHolderAmt(Double.valueOf(objRs.getString("TRANX_CARDHOLDER_AMT")).doubleValue());
				objTranxInfo.setIsAuthComVoid(objRs.getString("ISAUTHCOMVOID"));
				objTranxInfo.setTranxCurrConvAmt(objRs.getDouble("TRANX_CURRCONV_AMT"));
				objTranxInfo.setTranxCardHolderCurr(objRs.getString("TRANX_SETTLEMENT_CURR"));
				objTranxInfo.setPosEntryMode(objRs.getString("POSENTRYMODE"));

			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the ExistingTranx info"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objTranxInfo;
	}

	/**
	 * This method is used to check the transaction is available in TranxLog.
	 * @param strTerminalId,strCardNumber,strRefNo,ApprovalCode
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */


	public TranxInfo getNaradaExistingTranx(String strTerminalID, String strCardNumber, String strRefNo, String strApprovalCode)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getExistingTranx");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		TranxInfo objTranxInfo = null;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE TERMINALID='" + strTerminalID + "' ");
			if(strCardNumber != null && !strCardNumber.equals(""))
			{
				sbfDTDVal.append("AND CARDNUMBER='" + strCardNumber + "' ");

			}

			sbfDTDVal.append(" AND REFNO='" + strRefNo + "' AND RESPONSECODE='00'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				objTranxInfo = new TranxInfo();
				recAvailable = true;
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				objTranxInfo.setResponseCode(objRs.getString("RESPONSECODE"));
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setRefNo(objRs.getString("REFNO"));
				objTranxInfo.setTransmissionDateTime(objRs.getString("TRANSMISSIONDATETIME"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				objTranxInfo.setF55Res(objRs.getString("F55_RESPONSE"));


			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the ExistingTranx info"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objTranxInfo;
	}





	/**
	 * This method is used to calculate the Risk Management for the CardHolder
	 * It check against each rule of the acquirer
	 * @param strTerminalId,lngBatchNo
	 * @returns none
	 * @throws TPlusException
	 */

	public String riskControlCheck(String strMCC, String strMerchantRefno,String strMerchantID, String strCardNumber, double Amount, String strCurrCode,String strTranxType)throws TPlusException
	{


		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:RiskControlCheck :");




		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute	 = false;
		boolean bApplicable  = false;
		int RECENTDAYS_MAX = 99;
		String USD_ISO 		 = "840";

		String tranxType = "";
		String currCode = "";
		double amount = 0.0;
		double tranxAmount = Amount;
		double limitAmount = -1;
		int sign =0;
		int days =0;
		String isValid="00";


		int AccumCountALL[] 	= new int[RECENTDAYS_MAX];
		double AccumAmountALL[] = new double[RECENTDAYS_MAX];
		int AccumCountMCC[] 	= new int[RECENTDAYS_MAX];
		double AccumAmountMCC[] = new double[RECENTDAYS_MAX];
		int AccumCountMERCHANTID[] 	   = new int[RECENTDAYS_MAX];
		double AccumAmountMERCHANTID[] = new double[RECENTDAYS_MAX];


		try
		{


			if(strTranxType != null && strTranxType.equals("SALE"))
			{
				strTranxType = "'SALE','MANUAL','AUTHCOMPLETE','MOTO','ADJUST'";
			}
			else
			{
				strTranxType = "'CASH'";
			}

			StringBuffer sbfDTDVal = new StringBuffer();





			// Get all the risk control available for validation
			ArrayList objRuleArr = getTPlusRiskRulesInfo(strTranxType,strCurrCode);

			sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT CEIL(SYSDATE-DATETIME) AS DAYS, TRANXCODE, MCC, MERCHANT_REFNO,MERCHANTID, AMOUNT,DESTINATION_AMOUNT,"+
					"CURRCODE FROM qryRiskControlLog WHERE (SYSDATE-DATETIME <= " +
					RECENTDAYS_MAX +") AND (CARDNUMBER = '" + strCardNumber + "') AND TRANXCODE in ("+strTranxType+") AND CURRCODE='"+strCurrCode+"'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();
			while(objRs.next())
			{

				currCode = objRs.getString("CURRCODE");
				tranxType = objRs.getString("TRANXCODE");
				//AmountUSD = equalAmount(objRs.getDouble("AMOUNT"), currCode, USD_ISO);
				amount = objRs.getDouble("AMOUNT");
				days = objRs.getInt("DAYS");
				sign = 1;


				// Calculate Amount for each day and store in a array

				for (int i=0; i<RECENTDAYS_MAX; i++)
				{

					if (days == i)
					{

						System.out.println("day="+days+" "+amount+"  "+tranxType+"  "+sign);
						AccumCountALL[i] += sign;
						AccumAmountALL[i] += sign * amount;

						if (objRs.getString("MCC") != null && objRs.getString("MCC") == strMCC)
						{
							AccumCountMCC[i] += sign;
							AccumAmountMCC[i] += sign * amount;
						}

						if (objRs.getString("MERCHANTID") != null && objRs.getString("MERCHANTID") == strMerchantID)
						{
							AccumCountMERCHANTID[i] += sign;
							AccumAmountMERCHANTID[i] += sign * amount;
						}

						System.out.println("Tot Amount for Day="+i+"   "+AccumAmountALL[i]);
					}
				}
			}


			// Checking for each rule
			for (int i=0; i<objRuleArr.size(); i++)
			{
				RiskControlRule objRiskArr =(RiskControlRule)objRuleArr.get(i);
				System.out.println("Tranx Type="+objRiskArr.getType());
				// Single Transaction Restriction Rule
				System.out.println(objRiskArr.getType()+"  "+objRiskArr.getScope()+"  "+objRiskArr.getMerchantRefno()+"  "+strMerchantRefno);
				bApplicable = false;
				if (objRiskArr.getType().equals("S"))
				{
					if (objRiskArr.getScope().equals("1"))
					{
						bApplicable = true;
					}
					if ((objRiskArr.getScope().equals("2")) && (objRiskArr.getMCC().equals(strMCC)))
					{
						bApplicable = true;
					}
					if ((objRiskArr.getScope().equals("3")) && (objRiskArr.getMerchantRefno().equals(strMerchantRefno)))
					{
						bApplicable = true;
					}
					if (bApplicable)		// Start checking
					{
						System.out.println("RISK CHECKING.."+Amount +"   "+objRiskArr.getAmount());

						if (Amount > objRiskArr.getAmount())
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("Single Transaction Risk Control Limit exceeded.");
							return objRiskArr.getResponse();
						}
					}
				}

				// Accummulating Transaction Restriction Rule
				bApplicable = false;
				days = min(objRiskArr.getLastRecentDays(), RECENTDAYS_MAX);


				double TotAccumAmountALL=0.0;
				double TotAccumAmountMCC=0.0;
				double TotAccumAmountMERCHANTID=0.0;

				// Accumulate Total upto that day and store in an array.
				for(int day=0;day<=days;day++)
				{
					System.out.println("Daya="+day+"  "+AccumAmountALL[day]+"  "+AccumAmountMCC[day]+"  "+AccumAmountMERCHANTID[day]);
					TotAccumAmountALL +=AccumAmountALL[day];
					TotAccumAmountMCC +=AccumAmountMCC[day];
					TotAccumAmountMERCHANTID +=AccumAmountMERCHANTID[day];
				}

				System.out.println(objRiskArr.getLastRecentDays()+"  "+TotAccumAmountALL+"  "+TotAccumAmountMCC+"  "+TotAccumAmountMERCHANTID+" limitAmount "+objRiskArr.getAmount());
				if (objRiskArr.getType().equals("A"))
				{

					limitAmount = objRiskArr.getAmount();
					System.out.println("Total Amt spend="+(TotAccumAmountALL + tranxAmount));
					if (objRiskArr.getScope().equals("1"))
					{
						if (TotAccumAmountALL + tranxAmount > limitAmount)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("??????????Accumulating Transaction Risk Control Limit (ALL) is exceeded.");
							System.out.println("??????????Accumulating Transaction Risk Control Limit (ALL) is exceeded.");

							if(tempRiskLimit(strMerchantRefno,strCardNumber,TotAccumAmountALL + tranxAmount,limitAmount,strCurrCode,strTranxType))
								return objRiskArr.getResponse();
						}
					}
					if ((objRiskArr.getScope().equals("2")) && (objRiskArr.getMCC().equals(strMCC)))
					{
						if (TotAccumAmountMCC + tranxAmount > limitAmount)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("???????????Accumulating Transaction Risk Control Limit (MCC = " + strMCC + ") is exceeded.");
							if(tempRiskLimit(strMerchantRefno,strCardNumber,TotAccumAmountALL + tranxAmount,limitAmount,strCurrCode,strTranxType))
								return objRiskArr.getResponse();

						}
					}
					if ((objRiskArr.getScope().equals("3")) && (objRiskArr.getMerchantRefno().equals(strMerchantRefno)))
					{
						if (TotAccumAmountMERCHANTID + tranxAmount > limitAmount)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("??????????Accumulating Transaction Risk Control Limit (MERCHANTID = " + strMerchantID + ") is exceeded.");
							if(tempRiskLimit(strMerchantRefno,strCardNumber,TotAccumAmountALL + tranxAmount,limitAmount,strCurrCode,strTranxType))
								return objRiskArr.getResponse();

						}
					}
				}

			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the data for RiskControl"+e);
		}
		finally
		{
		} // End of the Main Try Block

		return isValid;
	}


	public boolean tempRiskLimit(String strMerchantRefno ,String strCardNumber,double tranxAmt,double limitAmt,String strCurrCode,String strTranxType )throws TPlusException
	{

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute	 = false;
		boolean bApplicable  = true;

		try
		{

			//if(strTranxType.equals("SALE") || strTranxType.equals("MOTO")||strTranxType.equals("ADJUST")||strTranxType.equals("AUTHCOMPLETE"))
			//strTranxType ="SALE";

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("SELECT Amount ");
			sbfDTDVal.append(" FROM RISK_TEMP_LIMIT WHERE MERCHANT_REFNO='"+strMerchantRefno+"' AND CARDNUMBER = '" + strCardNumber + "'");
			sbfDTDVal.append(" AND TRANX_TYPE in ("+strTranxType+") AND CURRCODE='"+strCurrCode+"'");

			sbfDTDVal.append(" AND (to_char(SYSDATE,'DD/MM/YYYY')>=to_char(fromdate,'DD/MM/YYYY') and to_char(SYSDATE,'DD/MM/YYYY')<=to_char(todate,'DD/MM/YYYY'))");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();
			if(objRs.next())
			{

				System.out.println("Temp Limit Amount="+objRs.getDouble("Amount") +"  "+ tranxAmt+"  "+limitAmt);
				if(tranxAmt >= limitAmt+objRs.getDouble("Amount"))
				{
					bApplicable = true;
				}
				else
				{
					bApplicable = false;
				}

			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the data for BatchTotal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		System.out.println("bApplicable"+bApplicable);

		return bApplicable;

	}


	public boolean checkBacklisted(String strMerchantId,String strCardno)throws TPlusException
	{

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute	 = false;
		boolean bApplicable  = false;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();

			String qry1 = "SELECT COUNT(*) as Count FROM BLACKLISTMERCHANT WHERE MERCHANT_REFNO='"+strMerchantId+"'";
			String qry2 = "SELECT COUNT(*) as Count FROM BLACKLISTCARD WHERE CARDNUMBER='"+strCardno+"'";
			ArrayList sqlList = new ArrayList();
			sqlList.add(qry1);
			sqlList.add(qry2);
			ArrayList resList = objDBMan.executeMultipleSelect(sqlList);

			for(int i=0;i<resList.size();i++)
			{
				objRs = (TPlusResultSet)resList.get(i);
				if(objRs.next())
				{
					System.out.println(objRs.getString("count"));
					if(objRs.getInt("count")>0)
					{
						bApplicable=true;
						break;
					}
				}


			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: BlackList Checking not successfull"+e.getMessage());
		}





		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:BlackList Response "+bApplicable);
		System.out.println("TransactionDB:BlackList Response "+bApplicable);

		return bApplicable;
	}





	public String getTerminalMK(String strTerminalID)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getTerminalMK");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strMK="";

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT MK FROM TERMINAL_MASTER WHERE TERMINAL_REFNO=");
			sbfDTDVal.append("(SELECT TERMINAL_REFNO FROM TRANX_TERMINAL WHERE TERMINALID='" + strTerminalID + "')");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				strMK = objRs.getString("MK");
			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Master Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return strMK;
	}

	public String getTerminalWK(String strTerminalID)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getTerminalMK");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strWK="";

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT WORKING_KEY FROM TERMINAL_MASTER WHERE TERMINAL_REFNO=");
			sbfDTDVal.append("(SELECT TERMINAL_REFNO FROM TRANX_TERMINAL WHERE TERMINALID='" + strTerminalID + "')");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				strWK = objRs.getString("WORKING_KEY");
			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return strWK;
	}



	public int storeTerminalWK(String strTerminalID,String strTerminalWK,String strWKKVC)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getStoreTerminalMK");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TERMINAL_MASTER SET WORKING_KEY='"+strTerminalWK+"', WORKING_KEY_KVC='"+strWKKVC+"' WHERE TERMINAL_REFNO=");
			sbfDTDVal.append("(SELECT TERMINAL_REFNO FROM TRANX_TERMINAL WHERE TERMINALID='" + strTerminalID + "')");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			rec = objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return rec;
	}


	/*public int storeTerminalWK(String strTerminalID,String strTerminalWK)throws TPlusException
{

	if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getStoreTerminalMK");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TERMINAL_MASTER SET WORKING_KEY='"+strTerminalWK+"' WHERE TERMINAL_REFNO=");
			sbfDTDVal.append("(SELECT TERMINAL_REFNO FROM TRANX_TERMINAL WHERE TERMINALID='" + strTerminalID + "')");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			rec = objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return rec;
}*/




	public String getLogonIssuerWK()throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getLogonIssuerWK");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strWK="";

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT ISS_WORKING_KEY FROM CONNECTION WHERE CONNECTION_NAME='Narada'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				strWK = objRs.getString("ISS_WORKING_KEY");
			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Issuer Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return strWK;
	}


	/**
	 * This method is used to check the transaction is available in TranxLog.
	 * @param strTerminalId,strCardNumber,strTraceNo
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */


	public TranxInfo getExistingTranxByTraceNo(String strTerminalID, String strCardNumber, String strTraceNo)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getExistingTranxByTraceNo");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable =false;
		TranxInfo objTranxInfo = null;

		try
		{
			System.out.println("strCardNumber"+strCardNumber);
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE ");
			sbfDTDVal.append(" TERMINALID='" + strTerminalID + "'");

			if(strCardNumber != null && !strCardNumber.equals(""))
			{
				sbfDTDVal.append(" AND CARDNUMBER='" + strCardNumber + "' ");
			}

			sbfDTDVal.append(" AND TRACENO='" + strTraceNo + "' AND DELETED = 'N' AND TRANXCODE <> 'REVERSAL'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				objTranxInfo = new TranxInfo();
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				objTranxInfo.setTransmissionDateTime(objRs.getString("TRANSMISSIONDATETIME"));
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setRefNo(objRs.getString("REFNO"));
				objTranxInfo.setResponseCode(objRs.getString("RESPONSECODE"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				objTranxInfo.setF55Res(objRs.getString("F55_RESPONSE"));
				objTranxInfo.setOrgTraceNo(objRs.getString("ORGTRACENO"));

				System.out.println("objRs.getString(F55_RESPONSE)"+objRs.getString("F55_RESPONSE"));


			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Tranx by TraceNo"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objTranxInfo;
	}



	public String getOrgTranx(String strTerminalID, String strCardNumber, String strTraceNo)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getOrgTranx");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable =false;
		String strTranxCode = "";

		try
		{
			System.out.println("strCardNumber"+strCardNumber);
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT TRANXCODE FROM TRANXLOG WHERE ");
			sbfDTDVal.append(" TERMINALID='" + strTerminalID + "'");

			if(strCardNumber != null && !strCardNumber.equals(""))
			{
				sbfDTDVal.append(" AND CARDNUMBER='" + strCardNumber + "' ");
			}

			sbfDTDVal.append(" AND TRACENO='" + strTraceNo + "' AND TRANXCODE NOT IN('REVERSAL','VOID')");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				strTranxCode = objRs.getString("TRANXCODE");

			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Tranx by TraceNo"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


		return strTranxCode;
	}


	/**
	 * This method is used to check the transaction is available in TranxLog.
	 * @param strTerminalId,strCardNumber,strTraceNo
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */


	public TranxInfo getExistingTranxByApprCode(String strTerminalID, String strCardNumber, String strApprCode,String strTranxCode)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getExistingTranxByApprCode");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable =false;
		TranxInfo objTranxInfo = null;


		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT T.*, PT.COMPLETED FROM TRANXLOG T, PREAUTH_TRANX PT WHERE T.TRANXLOGID = PT.TRANXLOGID "+
					" AND TERMINALID='" + strTerminalID + "' AND CARDNUMBER='" + strCardNumber + "' "+
					" AND APPROVALCODE='" + strApprCode + "' AND DELETED = 'N' AND TRANXCODE = '"+strTranxCode+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				objTranxInfo = new TranxInfo();
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				objTranxInfo.setTranxLogId(objRs.getString("TRANXLOGID"));
				objTranxInfo.setTranxCardHolderAmt(Double.valueOf(objRs.getString("TRANX_CARDHOLDER_AMT")).doubleValue());
				objTranxInfo.setIsAuthComVoid(objRs.getString("ISAUTHCOMVOID"));
				objTranxInfo.setTranxCurrConvAmt(objRs.getDouble("TRANX_CURRCONV_AMT"));
				objTranxInfo.setPreAuthStatus(objRs.getString("COMPLETED"));

			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Tranx by ApprCode"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objTranxInfo;
	}




	/**
	 * This method is used to check the transaction is available in TranxLog.
	 * @param strTerminalId,strCardNumber,strTraceNo
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */


	public TranxInfo getOSaleExistingTranxByApprCode(String strTerminalID, String strCardNumber, String strApprCode,String strTranxCode)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getExistingTranxByApprCode");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable =false;
		TranxInfo objTranxInfo = null;


		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE "+
					" TERMINALID='" + strTerminalID + "' AND CARDNUMBER='" + strCardNumber + "' "+
					" AND APPROVALCODE='" + strApprCode + "' AND DELETED = 'N' AND SETTLED = 'N' AND TRANXCODE = '"+strTranxCode+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				objTranxInfo = new TranxInfo();
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				objTranxInfo.setTransmissionDateTime(objRs.getString("TRANSMISSIONDATETIME"));
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setRefNo(objRs.getString("REFNO"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				objTranxInfo.setF55Res(objRs.getString("F55_RESPONSE"));
				objTranxInfo.setOrgTraceNo(objRs.getString("ORGTRACENO"));

			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Tranx by ApprCode"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objTranxInfo;
	}




	public String getTraceNo()throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getTraceNo");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String traceno="";


		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT seq_traceno.nextval as traceno from dual");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			String dumyVal = "000000";


			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				traceno= objRs.getString("traceno");
				dumyVal=dumyVal+traceno;
				traceno =  dumyVal.substring(traceno.length());
			}
			else
			{
				throw new TPlusException(TPlusCodes.APPL_ERR,"Error Trace No sequence not found");
			}



		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getMessageHandler"+exp);
		}

		return traceno;
	}


	/**
	 * This method returns minimum value of the two.
	 *
	 * @params int,int
	 * @returns int
	 */

	public int min(int first,int second)
	{
		if(first>second)
			return second;
		else
			return first;

	}

	public String getBase2Code(String strTranxCode)throws TPlusException
	{
		try
		{
			DBManager objDBMan = new DBManager();
			TPlusResultSet objrs = null;
			boolean bolExecute=false;
			String base2code="";
			StringBuffer strBuff = new StringBuffer();

			strBuff.append("SELECT BASE2_CODE AS BASE2CODE FROM BASE2CODE WHERE TRANX_CODE='"+strTranxCode+"'");


			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+strBuff.toString());
			System.out.println(" SQL="+ strBuff.toString());
			bolExecute = objDBMan.executeSQL(strBuff.toString());
			objrs = objDBMan.getResultSet();

			if(objrs.next())
			{
				base2code = objrs.getString("BASE2CODE");
			}

			return base2code;

		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,"Error in getMessageHandler"+exp);
		}



	}

	public String getTagValue(TLVList tlv,String strTag,String strType)throws Exception
	{

		int tag =	Integer.parseInt(strTag,16);
		TLVMsg iad = tlv.find(tag);
		if(iad != null)
		{
			if(strType.equals("AN"))
			{
				System.out.println(strTag+" = "+new String(iad.getValue()));
				return new String(iad.getValue());
			}
			else
			{
				System.out.println(strTag+"="+ISOUtil.hexString(iad.getValue()));
				return ISOUtil.hexString(iad.getValue());
			}
		}
		else
		{
			return null;
		}


	}


	public boolean checkMerchantTranxCode(String strTranxCode,String strTerminalRefno,String strBinName)
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:checkMerchantTranxCode");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean bolRes= false;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT COUNT(*) AS CNT FROM TRANX_TERMINAL_CODES WHERE TERMINAL_REFNO='"+strTerminalRefno+"' and TRANXCODE='"+ strTranxCode+"' and BIN_NAME='"+strBinName+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				if(objRs.getInt("CNT")>0)
					bolRes = true;
			}



		}
		catch (TPlusException tplusexp)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:checkMerchantTranxCode "+tplusexp);

		}
		catch (Exception e)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:checkMerchantTranxCode "+e);

		}
		finally
		{
		} // End of the Main Try Block

		return bolRes;
	}

	public boolean validateCurr(String strCurrCode)
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:validateCurr");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT CURR_CODE FROM ISSUER_CURRENCY");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				System.out.println(strCurrCode+"   "+objRs.getString("CURR_CODE"));
				if(strCurrCode.equals(objRs.getString("CURR_CODE")))
				{
					return true;
				}

			}
		}
		catch (Exception e)
		{
			System.out.println(e);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrRate "+e);
		}
		finally
		{
		} // End of the Main Try Block
		return false;
	}



	public double getCurrRate()
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrRate");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		double strRate = 0.0;


		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT RATE FROM CURR_RATE WHERE CURR_CODE=840");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				strRate = objRs.getDouble("RATE");
			}



		}
		catch (TPlusException tplusexp)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrRate "+tplusexp);


		}
		catch (Exception e)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrRate "+e);


		}
		finally
		{
		} // End of the Main Try Block


		return strRate;

	}

	public String getCurrDecimalPoint(String strCurrCode)
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrDecimalPoint");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strRate = "100";

		int decimalPoint = 2;
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrDecimalPoint default decimalPoint " + decimalPoint);

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT DECIMAL_POINT FROM CURRENCIES WHERE CURR_CODE='"+strCurrCode+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			int decimalPointFromDb = -1;

			if (objRs.next())
			{
				decimalPointFromDb = Integer.valueOf(objRs.getString("DECIMAL_POINT")).intValue();
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrDecimalPoint decimalPointFromDb " + decimalPointFromDb);

			if(decimalPointFromDb != -1){
				decimalPoint = decimalPointFromDb;
			}

		}
		catch (TPlusException tplusexp)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrRate "+tplusexp);

		}
		catch (Exception e)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrRate "+e);

		}
		finally
		{
		} // End of the Main Try Block


		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrDecimalPoint decimalPoint " + decimalPoint);

		switch (decimalPoint) {

		case 0:
			strRate = "1";
			break;
		case 2:
			strRate = "100";
			break;
		case 3:
			strRate = "1000";
			break;
		case 4:
			strRate = "10000";
			break;

		default:
			strRate = "100";
			break;
		}

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCurrDecimalPoint strRate " + strRate);

		return strRate;

	}


	/**
	 * This method is used to get TPlus RiskControlRules Info
	 * @param none
	 * @returns RiskControlRule
	 * @throws TPlusException
	 */

	public static ArrayList getTPlusRiskRulesInfo(String strTranxType,String strCurrCode)
			throws TPlusException
			{

		String strValue ="";
		DBManager objDBMan = new DBManager();
		ArrayList objArryRisk = new ArrayList();
		TPlusResultSet objRs = null;
		boolean bolExecute = false;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM RISKCONTROLRULES WHERE TRANX_TYPE in ("+strTranxType+") AND CURRCODE='"+strCurrCode+"'");

			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			while (objRs.next())
			{
				RiskControlRule objRiskRuleInfo = new RiskControlRule();
				String ruleId = objRs.getString("RULEID");
				objRiskRuleInfo.setType(objRs.getString("TYPE"));
				objRiskRuleInfo.setScope(objRs.getString("SCOPE"));
				objRiskRuleInfo.setMCC(objRs.getString("MCC"));
				//objRiskRuleInfo.setMerchantID(objRs.getString("MERCHANTID"));
				objRiskRuleInfo.setCurrCode(objRs.getString("CURRCODE"));
				objRiskRuleInfo.setResponse(objRs.getString("RESPONSE"));

				if(!TPlusConfig.isNumeric(objRs.getString("LASTRECENTDAYS")))
					throw new TPlusException(TPlusCodes.INVALID_PARAM,"LAST Recent Days value should be numeric value for rule ("+ruleId+")");
				else
					objRiskRuleInfo.setLastRecentDays(new Integer(objRs.getString("LASTRECENTDAYS")).intValue());

				if(!TPlusConfig.isNumeric(objRs.getString("COUNT")))
					throw new TPlusException(TPlusCodes.INVALID_PARAM,"RiskControl Count value should be numeric value for rule("+ruleId+")");
				else
					objRiskRuleInfo.setCount(new Integer(objRs.getString("COUNT")).intValue());

				if(!TPlusConfig.isDouble(objRs.getString("AMOUNT")))
					throw new TPlusException(TPlusCodes.INVALID_PARAM,"RiskControl Amount value should be numeric value for rule("+ruleId+")");
				else
					objRiskRuleInfo.setAmount(new Double(objRs.getString("AMOUNT")).doubleValue());

				objArryRisk.add(objRiskRuleInfo);

			}
		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Retrieving the Tag info"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objArryRisk;
			}

	public String  getDBDateTime()
	{

		String strValue =null;

		DBManager objDBMan = new DBManager();

		TPlusResultSet objRs = null;
		boolean bolExecute = false;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("select to_char(sysdate,'MMDDHH24MISS') as datetime from dual");

			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			while (objRs.next())
			{
				strValue = objRs.getString("datetime");
				//System.out.println(date.getHours());
			}
		}
		catch(Exception e){System.out.println(e);}
		return strValue;
	}


	public boolean isVCBMerchant(String strMerchantId, String strTerminalId)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:isVCBMerchant");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean bolVCBSettlement=false;
		String strBinName="";

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT BIN_NAME FROM TRANX_TERMINAL WHERE MERCHANTID='"+strMerchantId+"' AND TERMINALID='"+strTerminalId+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				strBinName = objRs.getString("BIN_NAME");
				if(strBinName.equals("CREDITBIN"))
				{
					bolVCBSettlement = true;
				}


			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Issuer Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return bolVCBSettlement;
	}



	public int updatePreAuthComplete(String strApprovalCode,String strReferenceNo,String strCardNo)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updatePreAuthComplete");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET ISAUTHCOMPLETE='Y' WHERE CARDNUMBER='"+strCardNo+"' AND ");
			sbfDTDVal.append("REFNO='"+strReferenceNo+"'");

			if( strApprovalCode!=null && !strApprovalCode.trim().equals(""))
			{
				sbfDTDVal.append(" AND APPROVALCODE = '"+strApprovalCode+"'");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			rec = objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return rec;
	}



	public boolean isCTFRecordAvailable(String strCardno,String strTerminalNo,String strTraceNo,String strRefno,String strTranxType)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:isCTFRecordAvailable");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean bolCTFRec=false;
		int cntRec=0;


		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			if(!strTranxType.equals("REVERSAL"))
			{

				sbfDTDVal.append("SELECT COUNT(*) as CNT FROM CTF_LOG WHERE ");
				sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND REFNO='"+strRefno+"' AND TRACE_NO='"+strTraceNo+"'");
			}
			else
			{

				sbfDTDVal.append("SELECT COUNT(*) as CNT FROM CTF_LOG WHERE ");
				sbfDTDVal.append(" CARDNUMBER='"+strCardno+"' AND TRACE_NO='"+strTraceNo+"'");
			}



			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();


			if (objRs.next())
			{
				if(objRs.getInt("CNT")>0)
					bolCTFRec=true;

			}

			return bolCTFRec;

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Issuer Keys"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}


	public void updateSingon()throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updateSingon");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE CONNECTION SET SIGNON_NEEDED = 'N'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			rec = objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}


	/**
	 * This method is used insert status of the settlement in settlemet log table
	 * @param strTerminalId,lngBatchNo
	 * @returns none
	 * @throws TPlusException
	 */


	public void keyExchangeLog( String strTerminalId,String strMerchantId,String strTranxType,String strResponseCode)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:keyExchangeLog..");


		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("INSERT INTO KEY_EXCHANGE_LOG (SNO,DATETIME,MERCHANT_ID,TERMINAL_ID,TRANX_TYPE,RESPONSE_CODE) ");
			sbfDTDVal.append("VALUES (SEQ_KEYEXCHANGE_LOG.NEXTVAL,SYSDATE,'"+strMerchantId+"','"+strTerminalId+"','"+strTranxType+"','"+strResponseCode+"')");


			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error:  keyExchangeLog Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	public void UpdateIssuerKeys( String strIssuerId,String PEK,String MPK,String keySchemes)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:IssuerKeys..");


		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;
		String strCardProdBin ="";

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			if(keySchemes.equals("CUP"))
			{
				strCardProdBin = "('624352','624353','624354')";
			}
			else
			{
				throw new TPlusException(TPlusCodes.APPL_ERR,"Error: NO Card Schemes available for PIK Key Exchange...");
			}

			//sbfDTDVal.append("UPDATE ISSUER_KEYS SET PEK='"+PEK+"', MPK='"+MPK+"',TRANX_KEY_INDEX='"+keyIndex+"', DATETIME=SYSDATE WHERE ISSUER_ID='"+strIssuerId+"'");
			sbfDTDVal.append("UPDATE KEY_INDEX SET HOST_KEY='"+PEK+"',USE_HOSTKEY='Y' WHERE KEY_TYPE='PPK' AND CARD_PRODUCT_ID in (SELECT CARD_PRODUCT_ID FROM CARD_PRODUCTS WHERE BIN IN "+strCardProdBin+") AND TRANX_CHANNEL='OFFUS' AND ISSUER_ID='Issuer1'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while copying to IssuerKey Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	public void UpdateSignOn(String connectionName, String status)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:UpdateSignOn..");


		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("UPDATE CONNECTION SET SIGNON_NEEDED='"+status+"' WHERE CONNECTION_NAME='"+connectionName+"'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while copying to IssuerKey Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	public int UpdateTMKKeys( String strTerminalId,String TMK)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:IssuerKeys..");


		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TERMINAL_MASTER SET MK='"+TMK+"' WHERE TERMINAL_REFNO=( ");
			sbfDTDVal.append(" SELECT TERMINAL_REFNO FROM TRANX_TERMINAL WHERE TERMINALID='"+strTerminalId+"')");


			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			return objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while copying to IssuerKey Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}

	/**
	 * This method is used to get Key Index
	 * @param CardProductID
	 * @returns HashMap
	 * @throws TPlusException
	 */


	public HashMap getKeyIndex(String cardProductId,String strTranxChannel)throws TPlusException,Exception {

		System.out.println(" ************ HSM Key Index ****************");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		HashMap keyMap = new HashMap();

		try {
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT KEY_TYPE,KEY_INDEX,TRANX_CHANNEL,USE_HOSTKEY,HOST_KEY FROM KEY_INDEX");
			//sbfDTDVal.append(" WHERE CARD_PRODUCT_ID='"+cardProductId+"' AND TRANX_CHANNEL='"+strTranxChannel+"'");
			sbfDTDVal.append(" WHERE CARD_PRODUCT_ID='"+cardProductId+"' ");

			System.out.println("SQL="+sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			String keyType = "";

			while(objRs.next()) {
				System.out.println(" Record Available"+objRs.getString("KEY_TYPE"));

				keyType = objRs.getString("KEY_TYPE");

				if("OFFUS".equals(strTranxChannel)){

					if("PPK".equals(keyType) && "OFFUS".equals(objRs.getString("TRANX_CHANNEL")))
					{
						System.out.println(objRs.getString("USE_HOSTKEY")+"  "+objRs.getString("HOST_KEY"));
						if(objRs.getString("USE_HOSTKEY") !=null && objRs.getString("USE_HOSTKEY").equals("Y"))
						{
							keyMap.put(objRs.getString("KEY_TYPE"),objRs.getString("HOST_KEY"));
						}
						else
						{
							keyMap.put(objRs.getString("KEY_TYPE"),objRs.getString("KEY_INDEX"));
						}
					}else if("PPK".equals(keyType) && "ONUS".equals(objRs.getString("TRANX_CHANNEL"))){
						// no need to enter
					}else{
						keyMap.put(objRs.getString("KEY_TYPE"),objRs.getString("KEY_INDEX"));
					}

				}else{

					if("PPK".equals(keyType) && "ONUS".equals(objRs.getString("TRANX_CHANNEL"))){
						keyMap.put(objRs.getString("KEY_TYPE"),objRs.getString("KEY_INDEX"));
					}else if("PPK".equals(keyType) && "OFFUS".equals(objRs.getString("TRANX_CHANNEL"))){
						// no need to enter
					}else{
						keyMap.put(objRs.getString("KEY_TYPE"),objRs.getString("KEY_INDEX"));
					}

				}

				//keyMap.put(objRs.getString("KEY_TYPE"),objRs.getString("KEY_INDEX"));

				/* if(objRs.getString("KEY_TYPE").equals("PPK")) {
                      keyMap.put("PPK",objRs.getString("KEY_INDEX"));
                  }
                  if(objRs.getString("KEY_TYPE").equals("PVK")) {
                      keyMap.put("PVK",objRs.getString("KEY_INDEX"));
                  }*/


			}

		}
		catch (Exception e) {

			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while geting Key Index "+e);
		}
		finally {
			// END
		}

		return keyMap;

	}


	/**
	 * This method is used to get Card PVV
	 * @param CardNumber
	 * @returns HashMap
	 * @throws TPlusException
	 */


	public String getPVV(String cardnumber)throws TPlusException,Exception {

		System.out.println(" ************ HSM Key Index ****************");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strPVV=null;
		String strPINBlock=null;

		try {
			StringBuffer sbfDTDVal = new StringBuffer();
			//sbfDTDVal.append("SELECT PVV FROM CARDS ");
			//sbfDTDVal.append(" WHERE CARDNUMBER="+cardNumber);
			sbfDTDVal.append("SELECT PVVOFFSET FROM CARD_DATA ");
			sbfDTDVal.append(" WHERE CARDNUMBER=" + new Long(cardnumber).longValue());

			System.out.println("SQL="+sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();


			if(objRs.next()) {
				//strPVV= objRs.getString("PVV");
				strPINBlock = objRs.getString("PVVOFFSET");
			}

		}
		catch (Exception e) {

			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while geting PVV "+e);
		}
		finally {
			// END
		}

		return strPINBlock;

	}


	public String getCVV(String cardnumber)throws TPlusException,Exception {

		System.out.println(" ************ HSM Key Index ****************");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strCVV=null;
		String strPINBlock=null;

		try {
			StringBuffer sbfDTDVal = new StringBuffer();
			//sbfDTDVal.append("SELECT PVV FROM CARDS ");
			//sbfDTDVal.append(" WHERE CARDNUMBER="+cardNumber);
			sbfDTDVal.append("SELECT CVV FROM CARD_DATA ");
			sbfDTDVal.append(" WHERE CARDNUMBER=" + new Long(cardnumber).longValue());

			System.out.println("SQL="+sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();


			if(objRs.next()) {

				strCVV = objRs.getString("CVV");
			}

		}
		catch (Exception e) {

			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while geting PVV "+e);
		}
		finally {
			// END
		}

		return strCVV;

	}




	/**
	 * This method is used to update PIN status
	 * @param CardNumber
	 * @returns HashMap
	 * @throws TPlusException
	 */


	public void updatePINStatus(String offset,String oldOffset,long cardnumber)throws TPlusException,Exception {

		System.out.println(" ************ HSM Key Index ****************");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strPVV=null;

		try {

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE CARD_DATA SET PVVOFFSET ='"+offset+"' WHERE CARDNUMBER=" + new Long(cardnumber).longValue() + " and CLOSING_DATE is null ");
			System.out.println("SQL="+sbfDTDVal.toString());
			objDBMan.addSQL(sbfDTDVal.toString());

			sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("UPDATE CARDS SET  CARD_STATUS_ID='0',WRONG_PIN_COUNT='0',PIN_RESET='N' WHERE CARDNUMBER=" + new Long(cardnumber).longValue());
			System.out.println("SQL="+sbfDTDVal.toString());
			objDBMan.addSQL(sbfDTDVal.toString());


			if(objDBMan.executeAllSQLs())
			{
				System.out.println(" Updated PIN Status Succesfully");
			}

		}
		catch (Exception e) {

			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while updating PVV Status "+e);
		}
		finally {
			// END
		}


	}




	public HashMap getIssuerKeys(String strIssuerName)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getIssuerKeys");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean bolVCBSettlement=false;
		HashMap keyList=null;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT PEK,MPK,TRANX_KEY_INDEX FROM ISSUER_KEYS WHERE ISSUER_NAME='"+strIssuerName+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				keyList = new HashMap();

				keyList.put("PEK",objRs.getString("PEK"));
				keyList.put("MPK",objRs.getString("MPK"));
				keyList.put("KEYINDEX",objRs.getString("TRANX_KEY_INDEX"));

			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Issuer Keys"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return keyList;
	}



	public String getPassword()throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getPassword");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strPassword=null;



		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT password from password ");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				strPassword = objRs.getString("password");


			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Issuer Keys"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return strPassword;
	}

	public TranxInfo getCUPExistingTranx(String strTerminalID, String strCardNumber, String strRefNo, String strApprovalCode,String strMTI,String strProcessingCode,String strF25)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getExistingTranx");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		TranxInfo objTranxInfo = null;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE TERMINALID='" + strTerminalID + "' ");
			if(strCardNumber != null && !strCardNumber.equals(""))
			{
				sbfDTDVal.append("AND CARDNUMBER='" + strCardNumber + "' ");

			}
			sbfDTDVal.append(" AND REFNO='" + strRefNo + "'  AND RESPONSECODE='00'");
			//sbfDTDVal.append(" AND APPROVALCODE = '"+strApprovalCode+"' AND RESPONSECODE='00'");

			if(strMTI.equals("0200") && strProcessingCode.equals("200000") && strF25.equals("06"))
			{
				sbfDTDVal.append(" AND TRANXCODE = 'SALE' ");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				objTranxInfo = new TranxInfo();
				recAvailable = true;
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				objTranxInfo.setResponseCode(objRs.getString("RESPONSECODE"));
				objTranxInfo.setTraceNo(objRs.getString("CUP_TRACENO"));
				objTranxInfo.setRefNo(objRs.getString("REFNO"));
				objTranxInfo.setTransmissionDateTime(objRs.getString("TRANSMISSIONDATETIME"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				objTranxInfo.setF55Res(objRs.getString("F55_RESPONSE"));
				objTranxInfo.setCupCutoff(objRs.getString("CUP_CUTOFF"));


			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the ExistingTranx info"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objTranxInfo;
	}

	public TranxInfo getCUPExistingTranxByTraceNo(String strTerminalID, String strCardNumber, String strTraceNo)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCUPExistingTranxByTraceNo");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable =false;
		TranxInfo objTranxInfo = null;

		try
		{
			System.out.println("strCardNumber"+strCardNumber);
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM TRANXLOG WHERE ");
			sbfDTDVal.append(" TERMINALID='" + strTerminalID + "'");

			if(strCardNumber != null && !strCardNumber.equals(""))
			{
				sbfDTDVal.append(" AND CARDNUMBER='" + strCardNumber + "' ");
			}

			sbfDTDVal.append(" AND TRACENO='" + strTraceNo + "' AND DELETED = 'N' AND TRANXCODE <> 'REVERSAL'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{

				objTranxInfo = new TranxInfo();
				objTranxInfo.setAmount(objRs.getDouble("AMOUNT"));
				objTranxInfo.setCardNumber(objRs.getString("CARDNUMBER"));
				objTranxInfo.setTranxCode(objRs.getString("TRANXCODE"));
				objTranxInfo.setTransmissionDateTime(objRs.getString("TRANSMISSION_DATETIME"));
				//objTranxInfo.setTranxTime(objRs.getString("TRANXTIME"));
				//objTranxInfo.setTranxDate(objRs.getString("TRANXDATE"));
				objTranxInfo.setTraceNo(objRs.getString("TRACENO"));
				objTranxInfo.setRefNo(objRs.getString("REFNO"));
				objTranxInfo.setResponseCode(objRs.getString("RESPONSECODE"));
				objTranxInfo.setApprovalCode(objRs.getString("APPROVALCODE"));
				//objTranxInfo.setF55Res(objRs.getString("F55_RESPONSE"));
				objTranxInfo.setOrgTraceNo(objRs.getString("TRACENO2"));
				objTranxInfo.setMTI(objRs.getString("MTI"));
				//objTranxInfo.setCupCutoff(objRs.getString("CUP_CUTOFF"));

			}


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Tranx by TraceNo"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objTranxInfo;
	}


	public int CutoffAdvice(ISOMsg objISO)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:CutoffAdvice Log:");

		DBManager objDBMan = new DBManager();
		int bolExecute=0;


		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET CUP_CUTOFF = 'Y' WHERE IS_CUP='Y' AND CUP_CUTOFF='N' ");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeUpdate(sbfDTDVal.toString());
			if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:CutoffAdvice Updated:");
			System.out.println("CutoffAdvice updated...");

			return bolExecute;
		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while CutoffAdvice "+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

	}


	public void cupCutoffRecords(IParser objISO )throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:cupCutoffRecords..");


		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("INSERT INTO CUTOFF_LOG (CUTOFF_ID,TRACENO,CARDNUMBER,REFERENCE_NO,APPROVAL_CODE,DATETIME,TRANX_DATETIME,LAST_UPDATED_DATE,TRANX_TYPE) ");
			sbfDTDVal.append("VALUES (SEQ_CUTOFFLOG.NEXTVAL,'"+objISO.getValue(11)+"','"+objISO.getValue(2)+"','"+objISO.getValue(37)+"','"+objISO.getValue(38)+"',");
			sbfDTDVal.append("SYSDATE,'"+objISO.getValue(7)+"',SYSDATE,','"+objISO.getTranxType()+"')");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error:  cupCutoffRecords Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}


	public void deleteCUPAuthLog(IParser objISO)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Delete Authorization Log:");

		DBManager objDBMan = new DBManager();
		boolean bolExecute=false;
		final int TERMINALID = 41;
		final int CARDNUMBER = 2;
		final int REFNO = 37;
		final int INVOICENO = 62;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("UPDATE TRANXLOG SET Deleted = 'Y' WHERE TERMINALID='"+objISO.getValue(TERMINALID) + "'");
			if(objISO.getCardNumber()!=null && !objISO.getCardNumber().equals(""))
			{
				sbfDTDVal.append(" AND CARDNUMBER='" + objISO.getCardNumber()+"'");
			}

			sbfDTDVal.append(" AND REFNO='" +objISO.getValue(REFNO) + "' AND SETTLED='N'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.executeUpdate(sbfDTDVal.toString());

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while deleting Auth Log"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block


	}


	/**
	 * This method is check the customer temporary Limits
	 * @param cardnumber cardproductid custtypeid mcc amount currency issuerid
	 * @returns double
	 * @throws TPlusException
	 */

	public boolean withdrawRulesLimitCheck(String CardNumber, String CardProductID, String CustTypeID, String MCC, String TranxCode, double Amount, String CURRCODE,String IssuerId)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:temporaryLimitCheck"+TranxCode);

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int AccumCount;
		double AccumAmount;
		boolean bApplicable;

		try {
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT AmountPerTranx, DAILYLIMITCOUNT, DAILYLIMITAMOUNT, MONTHLYLIMITCOUNT, MONTHLYLIMITAMOUNT, CURR_CODE, CARD_PRODUCT_ID, CUST_TYPE_ID, MCC, TRANX_CODE FROM WithdrawalLimitRules WHERE TRANX_CODE = '" + TranxCode + "'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			while (objRs.next()) {

				System.out.println(" SQL="+objRs.getString("CARD_PRODUCT_ID")+"  "+CardProductID +"  "+objRs.getString("CUST_TYPE_ID")+" "+ CustTypeID +"  "+objRs.getString("MCC")+" "+CardProductID+" "+CustTypeID+" "+MCC);
				bApplicable = false;
				if (objRs.getString("MCC").equals("0000"))			// special MCC: ALL MERCHANTS
				{
					if ((objRs.getString("CARD_PRODUCT_ID").equals(CardProductID)) &&
							(objRs.getString("CUST_TYPE_ID").equals(CustTypeID))) {
						bApplicable = true;
					}
				}
				else {
					if ((objRs.getString("CARD_PRODUCT_ID").equals(CardProductID)) &&
							(objRs.getString("CUST_TYPE_ID") .equals(CustTypeID)) &&
							(objRs.getString("MCC").equals(MCC))) {
						bApplicable = true;
					}
				}

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:WithdrawalLimits Exists");
				System.out.println("1" +bApplicable+"  "+Amount+"  "+CURRCODE+"   "+objRs.getDouble("AMOUNTPERTRANX")+"  "+IssuerId+"  "+objRs.getString("CURR_CODE"));
				if (bApplicable) {
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("Applicable");
					// Check Amount/Tranx

					if (objRs.getDouble("AMOUNTPERTRANX") > 0) {
						if (equalAmount(Amount, CURRCODE, objRs.getString("CURR_CODE"),IssuerId) > objRs.getDouble("AMOUNTPERTRANX")) {
							//throw new TPlusException(TPlusCodes.DO_NOT_HONOUR,"Temporary Limit Amount/Tranx","01");
							System.out.println(" >> CardHolder Amt/Tranx exceeded from WithdrawalLimits Rules");
							return false;
						}
					}
					System.out.println("********Check daily and month limit********");
					// Check daily and month limit
					int days = 1;
					String CountField =  "DAILYLIMITCOUNT";
					String AmountField = "DAILYLIMITAMOUNT";
					for (int i=0; i<2; i++) {
						AccumCount = 0;
						AccumAmount = 0.0;
						sbfDTDVal.setLength(0);

						sbfDTDVal.append("SELECT TRANXCODE,AMOUNT,CURRCODE FROM qryWithdrawLog WHERE TRANXCODE ='" + TranxCode + "'");
						sbfDTDVal.append(" and to_char(cardnumber) = '"+ CardNumber +"' ");
						sbfDTDVal.append(" and to_char(sysdate,'yyyymmdd') = to_char(datetime,'yyyymmdd') ");
						if (!objRs.getString("MCC").equals("0000")) {
							sbfDTDVal.append(" AND MCC ='" + MCC + "'");
						}


						if (DebugWriter.boolDebugEnabled) DebugWriter.write(" Daily/Monthly Limit Check:"+sbfDTDVal.toString());
						System.out.println(" SQL="+sbfDTDVal.toString());
						bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
						TPlusResultSet objRs1 = objDBMan.getResultSet();

						while (objRs1.next()) {
							String tranxCode = objRs1.getString("TRANXCODE");
							System.out.println("************TRANXCODE"+tranxCode);
							if ((tranxCode.equals("WITHDRAWAL")) || (tranxCode.equals("TRANSFER"))) {
								System.out.println("1111"+objRs1.getDouble("AMOUNT"));
								System.out.println("1111"+objRs1.getString("CURRCODE"));
								System.out.println("1111"+objRs.getString("CURR_CODE"));
								AccumCount += 1;
								AccumAmount += equalAmount(objRs1.getDouble("AMOUNT"), objRs1.getString("CURRCODE"), objRs.getString("CURR_CODE"),IssuerId);
							}
							System.out.println("1");
						}

						if (objRs.getInt(CountField) > 0) {
							System.out.println("22");
							System.out.println("Accumcount="+AccumCount+" "+objRs.getInt(CountField));
							if (AccumCount > objRs.getInt(CountField)) {
								//throw new TPlusException(TPlusCodes.DO_NOT_HONOUR,"Exceed Daily Limit/ Monthly Limit Count","01");
								System.out.println(" >> CardHolder  Daily Limit/ Monthly Limit Count exceded from WithdrawalLimits Rules");
								return false;
							}
						}
						System.out.println("2 "+objRs.getDouble(AmountField)+"   "+(AccumAmount + equalAmount(Amount, CURRCODE, objRs.getString("CURR_CODE"),IssuerId)));
						if (objRs.getDouble(AmountField) > 0) {
							if (AccumAmount + equalAmount(Amount, CURRCODE, objRs.getString("CURR_CODE"),IssuerId) > objRs.getDouble(AmountField)) {
								//throw new TPlusException(TPlusCodes.DO_NOT_HONOUR,"Exceed Daily Limit Amount/ Monthly Limit Amount","01");
								System.out.println(" >> CardHolder  Daily Limit/ Monthly Limit Amount exceded from WithdrawalLimits Rules");
								return false;
							}
						}
						System.out.println("3");
						days = 30;
						CountField = "MONTHLYLIMITCOUNT";
						AmountField = "MONTHLYLIMITAMOUNT";
					}
				}
			}
		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}
		catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the WithdrawalLimit info"+e);
		}
		finally {
		} // End of the Main Try Block

		return true;
	}





	/**
	 * This method is check the customer temporary Limits
	 * @param cardnumber cardproductid custtypeid mcc amount currency issuerid
	 * @returns double
	 * @throws TPlusException
	 */

	/*public boolean withdrawRulesLimitCheck(String CardNumber, String CardProductID, String CustTypeID, String TranxCode, double Amount, String CURRCODE,String IssuerId)throws TPlusException {

        if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:withdrawRulesLimitCheck"+TranxCode);

        DBManager objDBMan = new DBManager();
        TPlusResultSet objRs = null;
        boolean bolExecute=false;
        int AccumCount;
        double AccumAmount;
        boolean bApplicable;

        try {
            StringBuffer sbfDTDVal = new StringBuffer();
            sbfDTDVal.append("SELECT AmountPerTranx, DAILYLIMITCOUNT, DAILYLIMITAMOUNT, MONTHLYLIMITCOUNT, MONTHLYLIMITAMOUNT, CURR_CODE, PRODUCT_ID,TYPE_ID, TRANX_CODE FROM WithdrawalLimitRules WHERE TRANX_CODE = '" + TranxCode + "'");
            if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
            System.out.println(" SQL="+ sbfDTDVal.toString());
            bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
            objRs = objDBMan.getResultSet();

            while (objRs.next())
            {

	             bApplicable = true;
                if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:WithdrawalLimits Exists");
                System.out.println("1" +bApplicable+"  "+Amount+"  "+CURRCODE+"   "+objRs.getDouble("AMOUNTPERTRANX")+"  "+IssuerId+"  "+objRs.getString("CURR_CODE"));
                if (bApplicable)
                {

						// Check Amount/Tranx

						if (objRs.getDouble("AMOUNTPERTRANX") > 0)
						{
							if (equalAmount(Amount, CURRCODE, objRs.getString("CURR_CODE"),IssuerId) > objRs.getDouble("AMOUNTPERTRANX"))
							{
								//throw new TPlusException(TPlusCodes.DO_NOT_HONOUR,"Temporary Limit Amount/Tranx","01");
								System.out.println(" >> CardHolder Amt/Tranx exceeded from WithdrawalLimits Rules");
								return false;
							}
						}

					sbfDTDVal.setLength(0);
					sbfDTDVal.append("select count(*) cnt, sum(settled_Amt) sumamt from transaction where extract (day from transaction_date)=extract (day from sysdate)");
					sbfDTDVal.append("and  response_code = '00'	AND transaction_status = '0'and transaction_type in('"+TranxCode+"')");

					if (DebugWriter.boolDebugEnabled) DebugWriter.write(" Daily Limit Check:"+sbfDTDVal.toString());
					 System.out.println(" SQL="+sbfDTDVal.toString());
					  bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
					  TPlusResultSet objRs1 = objDBMan.getResultSet();
					 if(objRs1.next() && objRs1.getInt("cnt")>0)
					 {

							if (objRs.getInt("DAILYLIMITCOUNT") > 0 )
							{

								System.out.println("Accumcount="+objRs1.getInt("cnt")+" "+objRs.getInt("DAILYLIMITCOUNT"));
								if (objRs1.getInt("cnt") > objRs.getInt("DAILYLIMITCOUNT"))
								{
									System.out.println(" >> CardHolder  Daily Limit Count exceded from WithdrawalLimits Rules");
									return false;
								}
							 }

							if (objRs.getInt("DAILYLIMITAMOUNT") > 0 )
							{

								System.out.println("Accumcount="+objRs1.getInt("sumamt")+" "+objRs.getInt("DAILYLIMITAMOUNT"));
								if (objRs1.getInt("sumamt") > objRs.getInt("DAILYLIMITAMOUNT"))
								{
									System.out.println(" >> CardHolder  Daily Limit Amount exceded from WithdrawalLimits Rules");
									return false;
								}
							 }
				 	}

					sbfDTDVal.setLength(0);
					sbfDTDVal.append("select count(*) cnt, sum(settled_Amt) sumamt from transaction where sysdate-transaction_date < 30 ");
					sbfDTDVal.append("and  response_code = '00'	AND transaction_status = '0'and transaction_type in('"+TranxCode+"')");

					if (DebugWriter.boolDebugEnabled) DebugWriter.write(" Montly Limit Check:"+sbfDTDVal.toString());
					 System.out.println(" SQL="+sbfDTDVal.toString());
					  bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
					  objRs1 = objDBMan.getResultSet();

					 if(objRs1.next() && objRs1.getInt("cnt")>0)
					 {

							if (objRs.getInt("MONTHLYLIMITCOUNT") > 0 )
							{

								System.out.println("Accumcount="+objRs1.getInt("cnt")+" "+objRs.getInt("MONTHLYLIMITCOUNT"));
								if (objRs1.getInt("cnt") > objRs.getInt("MONTHLYLIMITCOUNT"))
								{
									System.out.println(" >> CardHolder  Montly Limit Count exceded from WithdrawalLimits Rules");
									return false;
								}
							 }

							if (objRs.getInt("MONTHLYLIMITAMOUNT") > 0 )
							{
								System.out.println("22");
								System.out.println("Accumcount="+objRs1.getInt("sumamt")+" "+objRs.getInt("MONTHLYLIMITAMOUNT"));
								if (objRs1.getInt("sumamt") > objRs.getInt("MONTHLYLIMITAMOUNT"))
								{
									System.out.println(" >> CardHolder  Montly Limit Amount exceded from WithdrawalLimits Rules");
									return false;
								}
							 }

				 	}


                }
            }
        }
        catch (TPlusException tplusexp) {
            throw tplusexp;
        }
        catch (Exception e) {
            throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the WithdrawalLimit info"+e);
        }
        finally {
        } // End of the Main Try Block

        return true;
    }*/


	public double equalAmount(double amount,String fromCurrency,String toCurrency,String issuerid)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:equalAmount");

		return amount;
	}

	// From Old Cacis


	/**
	 * This method is reterive the Customer Card Info data
	 * @param cardNumber
	 * @returns CardInfo object
	 * @throws TPlusException
	 */


	public CardInfo getCardInfo(long cardnumber)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCardInfo");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;
		CardInfo objCardInfo = null;

		try {

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM CARDINFO WHERE CARDNUMBER=" + new Long(cardnumber).longValue());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			System.out.println("1");
			objRs = objDBMan.getResultSet();

			if (objRs.next()) {
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				objCardInfo = new CardInfo();
				System.out.println("2 "+objRs.getString("CARD_PRODUCT_ID"));
				objCardInfo.setCardnumber(objRs.getString("CARDNUMBER"));
				objCardInfo.setCardProductId(objRs.getString("CARD_PRODUCT_ID"));
				objCardInfo.setIssuerId(objRs.getString("ISSUER_ID"));
				objCardInfo.setCustomerId(objRs.getString("CUSTOMER_ID"));
				objCardInfo.setAccountId(objRs.getString("ACCOUNT_ID"));
				objCardInfo.setCardholderType(objRs.getString("CARDHOLDER_TYPE"));
				objCardInfo.setExpDate(objRs.getString("EXPDATE"));
				System.out.println("3");
				objCardInfo.setEffectiveDate(objRs.getString("EFFECTIVEDATE"));
				objCardInfo.setCardstatusId(objRs.getString("CARD_STATUS_ID"));
				objCardInfo.setCVKI(objRs.getString("CVKI"));
				objCardInfo.setPVKI(objRs.getString("PVKI"));
				objCardInfo.setCVV(objRs.getString("CVV"));
				objCardInfo.setCVV2(objRs.getString("CVV2"));
				objCardInfo.setPVV(objRs.getString("PVV"));
				objCardInfo.setOPVV(objRs.getString("OPVV"));
				System.out.println("4");
				objCardInfo.setPVVOFFSET(objRs.getString("PVVOFFSET"));
				System.out.println("41");
				// objCardInfo.setPinReset(objRs.getString("PIN_RESET"));
				System.out.println("42");
				objCardInfo.setWrongPINCount(objRs.getString("WRONG_PIN_COUNT"));
				System.out.println("43");
				objCardInfo.setPinDisabled(objRs.getString("PIN_DISABLED"));
				System.out.println("44");
				objCardInfo.setNIP(objRs.getString("NIP"));
				System.out.println("45");
				objCardInfo.setVisaCode(objRs.getString("VISACODE"));
				System.out.println("46");
				objCardInfo.setBillingDate(objRs.getString("BILLING_DATE"));
				System.out.println("5");
				//objCardInfo.setCreditPrecent(objRs.getDouble("CREDITLIMIT_PRECENT"));
				//objCardInfo.setCashPrecent(objRs.getDouble("CASHLIMIT_PRECENT"));
				objCardInfo.setStatus(objRs.getString("STATUS"));
				//objCardInfo.setCVKA(objRs.getString("CVKA"));
				//objCardInfo.setCVKB(objRs.getString("CVKB"));
				//objCardInfo.setPVKA(objRs.getString("PVKA"));
				//objCardInfo.setPVKB(objRs.getString("PVKB"));
				System.out.println("6");
				objCardInfo.setCustomerTypeId(objRs.getString("CUST_TYPE_ID"));
				objCardInfo.setCurrencyCode(objRs.getString("CURRENCY_CODE"));
				objCardInfo.setCheckingAcct(objRs.getString("CHECKING_ACCOUNT"));
				objCardInfo.setSavingAcct(objRs.getString("SAVING_ACCOUNT"));
				objCardInfo.setOc(objRs.getString("OC"));
				objCardInfo.seteComEnable(objRs.getString("ECOMM_ENABLE"));
				objCardInfo.setCurrConvFee(objRs.getDouble("CURR_CONVERSION_FEE"));
				System.out.println("7");
				objCardInfo.setPinBlockDate(objRs.getString("PINBLOCK_DATE"));
				System.out.println("8");
				objCardInfo.setCardScheme(objRs.getString("CARD_SCHEME"));
				System.out.println("9");


			}


		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}
		catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the CardInfo info"+e);
		}
		finally {
		} // End of the Main Try Block

		return objCardInfo;
	}

	public String getLimitUsed(String accountId)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCardInfo");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;

		String res ="";

		try {

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM CUSTOMER_ACCOUNT WHERE ACCOUNT_ID='"+accountId+"'" );
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			System.out.println("1");
			objRs = objDBMan.getResultSet();

			if (objRs.next()) {
				res = objRs.getString("LIMIT_USED");
			}
		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}
		catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the getLimitUsed info"+e);
		}
		finally {
		} // End of the Main Try Block

		return res;
	}

	public AccountInfo getAccountInfo(String accountId)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getAccountInfo");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean recAvailable = false;

		AccountInfo objAccountInfo = null;

		try {

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM CUSTOMER_ACCOUNT WHERE ACCOUNT_ID='"+accountId+"'" );
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			System.out.println("1");
			objRs = objDBMan.getResultSet();

			if (objRs.next()) {

				objAccountInfo = new AccountInfo();

				objAccountInfo.setAccountId(accountId);
				objAccountInfo.setLimitUsed(objRs.getDouble("LIMIT_USED"));
				objAccountInfo.setCashused(objRs.getDouble("CASH_USED"));
				objAccountInfo.setPurchaseUsed(objRs.getDouble("PURCHASE_USED"));

			}

		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}
		catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the getLimitUsed info"+e);
		}
		finally {
		} // End of the Main Try Block

		return objAccountInfo;
	}


	public boolean isBlaclListCard(long cardnumber)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:isBlaclListCard");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;

		boolean isBlackList = false;
		int cnt = 0;

		try {

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT COUNT(*) AS CNT FROM BLACKLIST_CARDS WHERE CARDSTATUSID <> '0' AND CARDNUMBER=" + new Long(cardnumber).longValue());

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());

			objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next()) {
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				cnt = objRs.getInt("CNT");
			}

			if(cnt > 0){
				isBlackList = true;
			}

		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the isBlaclListCard method "+e);
		}
		finally {

		}

		return isBlackList;
	}


	public boolean isBlaclListMerchant(String merchantId)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:isBlaclListMerchant");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;

		boolean isBlackList = false;
		int cnt = 0;

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("merchantId :: " + merchantId);

		String escapedMID = StringUtil.escapeSingeQuote(merchantId);
		//String escapedMID = merchantId;

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("escapedMID :: " + escapedMID);

		try {

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT COUNT(*) AS CNT FROM BLACKLIST_MERCHANT WHERE BLOCKSTATUS = 'B' AND MERCHANTID='" + escapedMID+"'");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());

			objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next()) {
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Transaction Exists");
				cnt = objRs.getInt("CNT");
			}

			if(cnt > 0){
				isBlackList = true;
			}

		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the isBlaclListCard method "+e);
		}
		finally {

		}

		return isBlackList;
	}


	/**
	 * This method is check the customer all Limits requird for tranx
	 * @param cardInfo MCC TranxType Amount CurrCode
	 * @returns boolean
	 * @throws TPlusException
	 */

	public boolean withdrawalLimitCheck(CardInfo objCardInfo, String MCC ,String TranxType, double Amount, String CurrCode,String IssuerId,boolean isDomTranx)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("///////////////////////////////TransactionDB:Limit Check DISABLED//////////");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		boolean response=true;

		try {

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT COUNT(*) AS COUNTX FROM CUSTOMER_LIMITS WHERE CARDNUMBER=" + objCardInfo.getCardnumber() + " ");
			sbfDTDVal.append("AND STATUS='A' ");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString() +"  "+TranxType);
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if(objRs.next()) {

				if(objRs.getInt("COUNTX")>0) {
					System.out.println("IN IF");
					if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB: customer Limits Exists");
					response = cardHolderLimitCheckNew(objCardInfo.getCardnumber(),objCardInfo.getCardProductId(), objCardInfo.getCustomerTypeId(),MCC,Amount,TranxType,CurrCode,IssuerId,isDomTranx);
				}else {
					System.out.println("ELSE IN IF");
					if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB: no customer Limits Exists");
					response = ProductLimitCheckNew(objCardInfo.getCardnumber(),objCardInfo.getCardProductId(), objCardInfo.getCustomerTypeId(),MCC,Amount,TranxType,CurrCode,IssuerId,isDomTranx);
				}

			}

		}catch (TPlusException tplusexp) {
			throw tplusexp;
		}catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the EqualAmount info"+e);
		}finally {
		} // End

		System.out.println(" RESPONSE="+response);

		return response;
	}

	/**
	 * This method is check the customer temporary Limits
	 * @param cardnumber cardproductid custtypeid mcc amount currency issuerid
	 * @returns double
	 * @throws TPlusException
	 */

	public boolean ProductLimitCheck(String CardNumber, String CardProductID, String CustTypeID, String MCC, double Amount,String TranxType, String CURRCODE,String IssuerId)throws TPlusException {

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:temporaryLimitCheck");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int AccumCount=0;
		double AccumAmount=0.0;


		try {
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("SELECT MAX_CARDLIMIT_PER_SAL,MAX_CASHLIMIT_PER_CRL,");
			sbfDTDVal.append("TL.MAX_CREDITAMT_PER_TRANX,TL.MAX_CASHLIMIT_PER_TRANX,TL.MAX_CREDITTRANX_PER_DAY, TL.MAX_CREDITLIMIT_PER_DAY, TL.MAX_CASHTRANX_PER_DAY,");
			sbfDTDVal.append("TL.MAX_CASHLIMIT_PER_DAY,");
			sbfDTDVal.append("'05' AS RESPONSE,ECOMM_ENABLE,ECOMM_AMOUNT FROM CARD_PRODUCT_LIMIT TL,CARD_PRODUCTS CP ");
			sbfDTDVal.append("WHERE CP.CARD_PRODUCT_ID = TL.CARD_PRODUCT_ID AND CP.BIN = SUBSTR('"+CardNumber+"',0,6)");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			while (objRs.next()) {
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Temporary Limits Exists");
				// Check Amount/Tranx
				if(TranxType.equals("ECOMM"))
				{
					if(objRs.getString("ECOMM_ENABLE").equals("Y") && objRs.getDouble("ECOMM_AMOUNT") > 0)
					{
						if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, objRs.getString("CURR_CODE"),IssuerId) > objRs.getDouble("ECOMM_AMOUNT"))
						{
							System.out.println("Error Cardholder Ecomm Amt exceed");
							return false;
						}
					}
					else
					{
						System.out.println("Card Not Enable for Ecomm Tranx..");
						return false;
					}
				}

				else
				{

					if (objRs.getDouble("MAX_CREDITAMT_PER_TRANX") > 0) {
						System.out.println(equalAmount(Amount, CURRCODE, "",IssuerId)+"   "+objRs.getDouble("MAX_CREDITAMT_PER_TRANX"));
						if (equalAmount(Amount, CURRCODE, "",IssuerId) > objRs.getDouble("MAX_CREDITAMT_PER_TRANX")) {
							System.out.println("Throw Exception");
							throw new TPlusException(TPlusCodes.DO_NOT_HONOUR,"Temporary Limit Amount/Tranx","01");
						}
					}

				}

				// Check daily and month limit
				int days = 1;
				String CountField = "MAX_CREDITTRANX_PER_DAY";
				String AmountField = "MAX_CREDITLIMIT_PER_DAY";
				for (int i=0; i<1; i++)
				{
					AccumCount = 0;
					AccumAmount = 0.0;
					sbfDTDVal.setLength(0);

					double tranxAmt = 0;

					sbfDTDVal.append("SELECT * FROM qryWithdrawLog WHERE trunc(SYSDATE-DATETIME) <="+ days+" AND CARDNUMBER = "+CardNumber);
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("    Daily/Monthly Limit Check:"+sbfDTDVal.toString());
					System.out.println(" SQL="+sbfDTDVal.toString());
					bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
					TPlusResultSet objRs1 = objDBMan.getResultSet();
					while (objRs1.next())
					{
						String tranxCode = objRs1.getString("TRANXCODE");
						System.out.println("TranxCODE="+tranxCode);
						if ((tranxCode.equals("SALE")) || (tranxCode.equals("CASH")) || (tranxCode.equals("WITHDRAWAL")))
						{
							tranxAmt = Double.valueOf(objRs1.getString("AMOUNT")).doubleValue();

							AccumCount += 1;
							AccumAmount += equalAmount(tranxAmt, "", "",IssuerId);
						}
					}

					System.out.println("***AccumCount**"+AccumCount+"###"+AccumAmount+"  "+objRs.getInt(CountField));

					if (objRs.getInt(CountField) > 0) {
						if (AccumCount >= objRs.getInt(CountField)) {
							throw new TransactionException(TPlusCodes.DO_NOT_HONOUR,"Temporary Daily/Monthly Limit Count","01");
						}
					}
					if (objRs.getDouble(AmountField) > 0) {
						System.out.println(AccumAmount + equalAmount(Amount, CURRCODE, "",IssuerId)+"  "+objRs.getDouble(AmountField));
						if (AccumAmount + equalAmount(Amount, CURRCODE, "",IssuerId) > objRs.getDouble(AmountField)) {
							throw new TransactionException(TPlusCodes.DO_NOT_HONOUR,"Temporary Daily / Monthly Limit Amount","01");
						}
					}
					days = 30;
					CountField = "MONTHLYLIMITCOUNT";
					AmountField = "MONTHLYLIMITAMOUNT";
				}
			}

			boolean response = creditLimitCheck(CardNumber,Amount,CURRCODE,TranxType,objRs.getDouble("MAX_CASHLIMIT_PER_CRL"),objRs.getDouble("MAX_CARDLIMIT_PER_SAL"),IssuerId);
		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}
		catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the Tempory LimitCheck info"+e);
		}
		finally {
		} // End of the Main Try Block

		return true;
	}


	public boolean ProductLimitCheckNew(String CardNumber,
			String CardProductID, String CustTypeID, String MCC, double Amount,
			String TranxType, String CURRCODE, String IssuerId, boolean isDomTranx)
					throws TPlusException {

		if (DebugWriter.boolDebugEnabled)
			DebugWriter.write("TransactionDB:temporaryLimitCheck");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute = false;
		int AccumCount = 0;
		double AccumAmount = 0.0;

		try {

			String CountField = "MAX_CREDITTRANX_PER_DAY";
			String AmountField = "MAX_CREDITLIMIT_PER_DAY";

			StringBuffer sbfDTDVal = new StringBuffer();

			if(isDomTranx)
			{
				sbfDTDVal.append("SELECT MAX_CARDLIMIT_PER_SAL,MAX_CASHLIMIT_PER_CRL,");
				sbfDTDVal.append("TL.MAX_CREDITAMT_PER_TRANX,TL.MAX_CASHLIMIT_PER_TRANX,TL.MAX_CREDITTRANX_PER_DAY, TL.MAX_CREDITLIMIT_PER_DAY, TL.MAX_CASHTRANX_PER_DAY,");
				sbfDTDVal.append("TL.MAX_CASHLIMIT_PER_DAY,");
				sbfDTDVal.append("'05' AS RESPONSE,ECOMM_ENABLE,ECOM_AMT as ECOMM_AMOUNT, TL.ECOM_AMT_PER_DAY, TL.ECOMM_TRANX_PER_DAY ");
				sbfDTDVal.append("FROM CARD_PRODUCT_LIMIT TL,CARD_PRODUCTS CP ");
				sbfDTDVal.append("WHERE CP.CARD_PRODUCT_ID = TL.CARD_PRODUCT_ID AND CP.BIN = SUBSTR('" + CardNumber + "',0,6)");
			}
			else
			{
				sbfDTDVal.append("SELECT MAX_CARDLIMIT_PER_SAL_INT as MAX_CARDLIMIT_PER_SAL ,MAX_CASHLIMIT_PER_CRL_INT as MAX_CASHLIMIT_PER_CRL,");
				sbfDTDVal.append("TL.MAX_CREDITAMT_PER_TRANX_INT as MAX_CREDITAMT_PER_TRANX,TL.MAX_CASHLIMIT_PER_TRANX_INT as MAX_CASHLIMIT_PER_TRANX,TL.MAX_CREDITTRANX_PER_DAY_INT as MAX_CREDITTRANX_PER_DAY, TL.MAX_CREDITLIMIT_PER_DAY_INT as MAX_CREDITLIMIT_PER_DAY, TL.MAX_CASHTRANX_PER_DAY_INT as MAX_CASHTRANX_PER_DAY,");
				sbfDTDVal.append("TL.MAX_CASHLIMIT_PER_DAY_INT as MAX_CASHLIMIT_PER_DAY,");
				sbfDTDVal.append("'05' AS RESPONSE,ECOMM_ENABLE,ECOM_AMT_INT as ECOMM_AMOUNT, TL.ECOM_AMT_PER_DAY_INT AS ECOM_AMT_PER_DAY, TL.ECOMM_TRANX_PER_DAY_INT AS ECOMM_TRANX_PER_DAY ");
				sbfDTDVal.append("FROM CARD_PRODUCT_LIMIT TL,CARD_PRODUCTS CP ");
				sbfDTDVal.append("WHERE CP.CARD_PRODUCT_ID = TL.CARD_PRODUCT_ID AND CP.BIN = SUBSTR('" + CardNumber + "',0,6)");
			}


			if (DebugWriter.boolDebugEnabled)
				DebugWriter.write("TransactionDB:" + sbfDTDVal.toString());
			System.out.println(" SQL=" + sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

			objRs = objDBMan.getResultSet();

			while (objRs.next()) {

				if (DebugWriter.boolDebugEnabled)
					DebugWriter.write("ProductionLimit:Product Limits Exists");
				// Check Amount/Tranx

				if ("ECOMM".equals(TranxType)) {

					if (objRs.getString("ECOMM_ENABLE").equals("Y") && objRs.getDouble("ECOMM_AMOUNT") > 0) {
						if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, "840", IssuerId) > objRs.getDouble("ECOMM_AMOUNT")) {
							System.out.println("Error Cardholder Ecomm Amt exceed");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("Error Cardholder Ecomm Amt exceed");
							//return false;
							throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded eCom transaction amount Limit");
						}
					} else {
						System.out.println("Amount not configured");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("eCom amount not configured");
						//return false;
						throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "eCom amount not configured");
					}

				} else if ("CASH".equals(TranxType)) {

					CountField = "MAX_CASHTRANX_PER_DAY";
					AmountField = "MAX_CASHLIMIT_PER_DAY";

					if (objRs.getDouble("MAX_CASHLIMIT_PER_TRANX") > 0) {
						if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, "", IssuerId) > objRs.getDouble("MAX_CASHLIMIT_PER_TRANX")) {
							System.out.println("Error Cardholder AMOUNTPERTRANX exceed");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exceeded cash per transaction");
							//return false;
							throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded cash per transaction");
						}
					}else{
						System.out.println("Amount not configured");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("cash amount per tranx not configured");
						//return false;
						throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "cash amount per tranx not configured");
					}

				} else {

					if (objRs.getDouble("MAX_CREDITAMT_PER_TRANX") > 0) {
						System.out.println(equalAmount(Amount, CURRCODE, "", IssuerId) + "   " + objRs.getDouble("MAX_CREDITAMT_PER_TRANX"));
						if (equalAmount(Amount, CURRCODE, "", IssuerId) > objRs.getDouble("MAX_CREDITAMT_PER_TRANX")) {
							System.out.println("Error Cardholder AMOUNTPERTRANX exceed");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exceeded sale per transaction");
							//return false;
							throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded sale per transaction");
						}
					}else{
						System.out.println("Amount not configured");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("sale amount per tranx not configured");
						//return false;
						throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "sale amount per tranx not configured");
					}


				}

				// Check daily and month limit
				int days = 1;
				for (int i = 0; i < 1; i++) {

					AccumCount = 0;
					AccumAmount = 0.0;
					sbfDTDVal = new StringBuffer();

					sbfDTDVal.append("SELECT count(tranxcode) as tranxcount, nvl(sum(amount),'0') as tranxamt FROM qryWithdrawLog WHERE (trunc(sysdate)+1 - trunc(datetime)) <=" + days + " AND CARDNUMBER = " + CardNumber);

					if("CASH".equals(TranxType)){
						sbfDTDVal.append(" and tranxcode in ('CASH') ");
					}else{
						sbfDTDVal.append(" and tranxcode in ('TRANSFER','SALE', 'PREAUTH') ");
					}

					// domestic & international
					if(isDomTranx){
						sbfDTDVal.append(" and acqcountrycode = '418' ");
					}else{
						sbfDTDVal.append(" and acqcountrycode <> '418' ");
					}

					if (DebugWriter.boolDebugEnabled)
						DebugWriter.write("    Daily/Monthly Limit Check:" + sbfDTDVal.toString());
					System.out.println(" SQL=" + sbfDTDVal.toString());

					bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
					TPlusResultSet objRs1 = objDBMan.getResultSet();

					while (objRs1.next())
					{
						AccumCount = objRs1.getInt("TRANXCOUNT");
						AccumAmount = objRs1.getDouble("TRANXAMT");
					}

					System.out.println("AccumCount & Amt="+AccumCount+" "+AccumAmount+"  "+objRs.getInt(CountField)+"  "+objRs.getDouble(AmountField));

					if (objRs.getInt(CountField) > 0) {
						if (AccumCount >= objRs.getInt(CountField)) {
							System.out.println(" >> CardHolder  Daily Limit Count exceded from customer_Limit Rules");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardHolder  Daily Limit Count exceded from customer_Limit Rules");
							throw new TPlusException(TPlusCodes.EXCEED_WITHDRAW_FREQ_LIMIT, "01", "Exceeded daily limit count");
						}
					}else{
						System.out.println("Count not configured");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exceeded daily limit count not configured");
						//return false;
						throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "Exceeded daily limit count not configured");
					}

					if (objRs.getDouble(AmountField) > 0) {
						System.out.println(AccumAmount + equalAmount(Amount, CURRCODE, "", IssuerId) + "  " + objRs.getDouble(AmountField));
						if (AccumAmount+ equalAmount(Amount, CURRCODE, "", IssuerId) > objRs.getDouble(AmountField)) {
							System.out.println(" >> CardHolder Daily Limit Amount exceded from Cardholder_Limit Rules");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardHolder Daily Limit Amount exceded from Cardholder_Limit Rules");
							throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded daily limit amount");
						}
					}else{
						System.out.println("Amount not configured");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exceeded daily limit amount not configured");
						//return false;
						throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "Exceeded daily limit amount not configured");
					}

					days = 30;
					CountField = "MONTHLYLIMITCOUNT";
					AmountField = "MONTHLYLIMITAMOUNT";

				}

				// ecom transaction validation
				if ("ECOMM".equals(TranxType)) {

					int dayseCom = 1;

					AccumCount = 0;
					AccumAmount = 0;

					CountField = "ECOMM_TRANX_PER_DAY";
					AmountField = "ECOM_AMT_PER_DAY";

					sbfDTDVal = new StringBuffer();
					sbfDTDVal.append("SELECT count(tranxcode) as tranxcount, nvl(sum(amount),'0') as tranxamt FROM qryWithdrawLog WHERE (trunc(sysdate)+1 - trunc(datetime)) <=" + dayseCom + " AND CARDNUMBER = " + CardNumber);
					sbfDTDVal.append(" and TRANXCODE_SUBTYPE in ('ECOM') ");

					// domestic & international
					if(isDomTranx){
						sbfDTDVal.append(" and acqcountrycode = '418' ");
					}else{
						sbfDTDVal.append(" and acqcountrycode <> '418' ");
					}

					if (DebugWriter.boolDebugEnabled)
						DebugWriter.write("Daily/Monthly Limit Check of eCom:");
					System.out.println("SQL=" + sbfDTDVal.toString());

					bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
					TPlusResultSet objRs1 = objDBMan.getResultSet();

					while (objRs1.next()) {
						AccumCount = objRs1.getInt("TRANXCOUNT");
						AccumAmount = objRs1.getDouble("TRANXAMT");
					}

					if (objRs.getInt(CountField) > 0) {
						if (AccumCount >= objRs.getInt(CountField)) {
							System.out.println(" >> CardHolder  Daily Limit Count exceded from customer_Limit Rules for eCom");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write(" >> CardHolder  Daily Limit Count exceded from customer_Limit Rules for eCom");
							//return false;
							throw new TPlusException(TPlusCodes.EXCEED_WITHDRAW_FREQ_LIMIT, "01", "Exceeded daily limit count eCom");
						}
					}else{
						System.out.println("Count not configured");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exceeded daily limit count eCom not configured");
						//return false;
						throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "Exceeded daily limit count eCom not configured");
					}

					if (objRs.getDouble(AmountField) > 0) {
						if (AccumAmount + equalAmount(Amount, CURRCODE, "", IssuerId) > objRs.getDouble(AmountField)) {
							System.out.println(" >> CardHolder Daily Limit Amount exceded from Cardholder_Limit Rules eCom");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardHolder Daily Limit Amount exceded from Cardholder_Limit Rules eCom");
							//return false;
							throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded daily limit amount eCom");
						}
					}else{
						System.out.println("Amount not configured");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exceeded daily limit amount eCom not configured");
						//return false;
						throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "Exceeded daily limit amount eCom not configured");
					}

				}

			}

			if(!"470532".equals(CardNumber.subSequence(0, 6))){
				boolean response = creditLimitCheck(CardNumber, Amount, CURRCODE, TranxType, objRs.getDouble("MAX_CASHLIMIT_PER_CRL"), objRs.getDouble("MAX_CARDLIMIT_PER_SAL"), IssuerId);
			}

		} catch (TPlusException tplusexp) {
			throw tplusexp;
		} catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR, "Error :while Retrieving the Tempory LimitCheck info" + e);
		} finally {
		} // End of the Main Try Block

		return true;
	}


	/**
	 * This method is used to check the transaction is available in TranxLog.
	 * @param strTerminalId,strCardNumber,strRefNo,ApprovalCode
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */

	public boolean cardHolderLimitCheck(String CardNumber, String CardProductID, String CustTypeID, String MCC, double Amount,String TranxType, String CURRCODE, String IssuerId)throws TPlusException{

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute = false;
		String sSQL;
		int AccumCount;
		double AccumAmount;
		double CashLimit = 0.0;
		double PurchaseLimit = 0.0;
		boolean bApplicable;

		try {

			System.out.println("********cardHolderLimitCheck*********");
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT * FROM Customer_limits WHERE CardNumber = " + CardNumber);
			System.out.println("SQL=" + sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();
			while (objRs.next()) {

				System.out.println("cardHolderLimitCheck  RECORD AVAILABLE");
				bApplicable = false;
				if ((objRs.getDouble("AMOUNTPERTRANX") != 0)
						|| (objRs.getDouble("DAILYLIMITCOUNT") != 0)
						|| (objRs.getDouble("DAILYLIMITAMOUNT") != 0)
						|| (objRs.getDouble("MONTHLYLIMITCOUNT") != 0)
						|| (objRs.getDouble("MONTHLYLIMITAMOUNT") != 0)) {

					CashLimit = objRs.getDouble("CASH_LIMIT");
					PurchaseLimit = objRs.getDouble("PURCHASE_LIMIT");
					bApplicable = true;

				}

				if (bApplicable) {

					// Check Amount/Tranx
					if ("ECOMM".equals(TranxType)) {

						if (objRs.getDouble("ECOMM_AMOUNT") > 0) {
							if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, objRs.getString("CURR_CODE"), IssuerId) > objRs.getDouble("ECOMM_AMOUNT")) {
								System.out.println("Error Cardholder Ecomm Amt exceed");
								return false;
							}
						}else{
							System.out.println("Amount not configured");
							return false;
						}

					} else if ("CASH".equals(TranxType)) {

						if (objRs.getDouble("CASHPERTRANX") > 0) {
							if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, objRs.getString("CURR_CODE"), IssuerId) > objRs.getDouble("CASHPERTRANX")) {
								System.out.println("Error Cardholder AMOUNTPERTRANX exceed");
								return false;
							}
						}else{
							System.out.println("Amount not configured");
							return false;
						}

					} else {

						if (objRs.getDouble("AMOUNTPERTRANX") > 0) {
							if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, objRs.getString("CURR_CODE"), IssuerId) > objRs.getDouble("AMOUNTPERTRANX")) {
								System.out.println("Error Cardholder AMOUNTPERTRANX exceed");
								return false;
							}
						}else{
							System.out.println("Amount not configured");
							return false;
						}

					}

					// Check daily and month limit
					int days = 1;
					String CountField = "DAILYLIMITCOUNT";
					String AmountField = "DAILYLIMITAMOUNT";

					for (int i = 0; i < 1; i++) {

						AccumCount = 0;
						AccumAmount = 0;
						sbfDTDVal = new StringBuffer();
						sbfDTDVal.append("SELECT * FROM qryWithdrawLog WHERE trunc(SYSDATE-DATETIME) <=" + days + " AND CARDNUMBER = " + CardNumber);

						if (DebugWriter.boolDebugEnabled)
							DebugWriter.write("    Daily/Monthly Limit Check:");
						System.out.println("SQL=" + sbfDTDVal.toString());

						bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
						TPlusResultSet objRs1 = objDBMan.getResultSet();

						while (objRs1.next()) {
							String tranxCode = objRs1.getString("TRANXCODE");
							if ((tranxCode.equals("WITHDRAWAL")) || (tranxCode.equals("TRANSFER")) || (tranxCode.equals("SALE")) || (tranxCode.equals("CASH"))) {
								AccumCount += 1;
								System.out.println(objRs1.getString("AMOUNT")
										+ "   "
										+ Double.valueOf(
												objRs1.getString("AMOUNT"))
												.doubleValue());
								AccumAmount += equalAmount(Double.valueOf(
										objRs1.getString("AMOUNT"))
										.doubleValue(), objRs1
										.getString("CURRCODE"), objRs
										.getString("CURR_CODE"), IssuerId);
							}
						}
						// System.out.println("AccumCount="+AccumCount+"  "+AccumAmount+"  "+objRs1.getString("AMOUNT"));
						if (objRs.getInt(CountField) > 0) {
							if (AccumCount >= objRs.getInt(CountField)) {
								// throw new
								// TPlusException(TPlusCodes.DO_NOT_HONOUR," Exceed Daily Limit Count/ Monthly Limit Count","01");
								System.out
								.println(" >> CardHolder  Daily Limit/ Monthly Limit Count exceded from Cardholder_Limit Rules");
								return false;
							}
						}
						if (objRs.getDouble(AmountField) > 0) {
							if (AccumAmount
									+ equalAmount(Amount, CURRCODE, objRs
											.getString("CURR_CODE"), IssuerId) > objRs
											.getDouble(AmountField)) {
								// throw new
								// TPlusException(TPlusCodes.DO_NOT_HONOUR," Exceed Daily Limit Amount/ Monthly Limit Amount","01");
								System.out
								.println(" >> CardHolder  Daily Limit/ Monthly Limit Amount exceded from Cardholder_Limit Rules");
								return false;
							}
						}

						days = 30;
						CountField = "MONTHLYLIMITCOUNT";
						AmountField = "MONTHLYLIMITAMOUNT";
					}
				}
			}

			boolean response = creditLimitCheck(CardNumber, Amount, CURRCODE, TranxType, CashLimit, PurchaseLimit, IssuerId);

		} catch (TPlusException tplusexp) {
			throw tplusexp;
		} catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR, "Error :while Retrieving the EqualAmount info" + e);
		} finally {
		} // End
		System.out.println(" RETURN TRUE");
		return true;
	}



	public boolean cardHolderLimitCheckNew(String CardNumber, String CardProductID, String CustTypeID, String MCC, double Amount,String TranxType, String CURRCODE, String IssuerId, boolean isDomTranx)throws TPlusException{

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute = false;
		String sSQL;
		int AccumCount;
		double AccumAmount;
		double CashLimit = 0.0;
		double PurchaseLimit = 0.0;
		boolean bApplicable;

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("In cardHolderLimitCheckNew");

		try {

			System.out.println("********cardHolderLimitCheck*********");
			StringBuffer sbfDTDVal = new StringBuffer();
			if(isDomTranx)
			{
				sbfDTDVal.append("SELECT AMOUNTPERTRANX,DAILYLIMITCOUNT,DAILYLIMITAMOUNT,MONTHLYLIMITCOUNT,MONTHLYLIMITAMOUNT,");
				sbfDTDVal.append("DAILYCASHCOUNT,DAILYCASHAMOUNT,CASH_LIMIT,PURCHASE_LIMIT,CASHPERTRANX,ECOMM_AMOUNT,CURR_CODE,ECOMM_AMOUNT_PER_DAY,ECOMM_TRANX_PER_DAY ");
				sbfDTDVal.append("FROM CUSTOMER_LIMITS WHERE CardNumber = " + CardNumber + " ");
				sbfDTDVal.append("AND STATUS='A' ");
			}
			else
			{
				sbfDTDVal.append("SELECT AMOUNTPERTRANX_INT AS AMOUNTPERTRANX,DAILYLIMITCOUNT_INT AS DAILYLIMITCOUNT ,");
				sbfDTDVal.append("DAILYLIMITAMOUNT_INT AS DAILYLIMITAMOUNT,MONTHLYLIMITCOUNT_INT AS MONTHLYLIMITCOUNT,MONTHLYLIMITAMOUNT_INT AS MONTHLYLIMITAMOUNT,");
				sbfDTDVal.append("DAILYCASHCOUNT_INT AS DAILYCASHCOUNT,DAILYCASHAMOUNT_INT AS DAILYCASHAMOUNT,CASH_LIMIT_INT AS CASH_LIMIT,");
				sbfDTDVal.append("PURCHASE_LIMIT_INT AS PURCHASE_LIMIT,CASHPERTRANX_INT AS CASHPERTRANX,ECOMM_AMOUNT_INT AS ECOMM_AMOUNT,CURR_CODE AS CURR_CODE, ");
				sbfDTDVal.append("ECOMM_AMOUNT_PER_DAY_INT AS ECOMM_AMOUNT_PER_DAY, ECOMM_TRANX_PER_DAY_INT AS ECOMM_TRANX_PER_DAY ");
				sbfDTDVal.append("FROM CUSTOMER_LIMITS WHERE CardNumber = " + CardNumber + " ");
				sbfDTDVal.append("AND STATUS='A' ");

			}

			System.out.println("SQL=" + sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();
			while (objRs.next()) {

				System.out.println("cardHolderLimitCheck  RECORD AVAILABLE");
				bApplicable = false;
				if ((objRs.getDouble("AMOUNTPERTRANX") != 0)
						|| (objRs.getDouble("DAILYLIMITCOUNT") != 0)
						|| (objRs.getDouble("DAILYLIMITAMOUNT") != 0)
						|| (objRs.getDouble("MONTHLYLIMITCOUNT") != 0)
						|| (objRs.getDouble("MONTHLYLIMITAMOUNT") != 0))
				{

					CashLimit = objRs.getDouble("CASH_LIMIT");
					PurchaseLimit = objRs.getDouble("PURCHASE_LIMIT");
					bApplicable = true;

				}

				String CountField = "DAILYLIMITCOUNT";
				String AmountField = "DAILYLIMITAMOUNT";

				if (bApplicable) {

					// Check Amount/Tranx
					if ("ECOMM".equals(TranxType)) {

						if (objRs.getDouble("ECOMM_AMOUNT") > 0) {
							if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, objRs.getString("CURR_CODE"), IssuerId) > objRs.getDouble("ECOMM_AMOUNT")) {
								System.out.println("Error Cardholder Ecomm Amt exceed");
								if (DebugWriter.boolDebugEnabled) DebugWriter.write("Error Cardholder Ecomm Amt exceed");
								//return false;
								throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded eCom transaction amount Limit");
							}
						}else{
							System.out.println("Amount not configured");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("eCom amount not configured");
							//return false;
							throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "eCom amount not configured");
						}

					} else if ("CASH".equals(TranxType)) {

						CountField = "DAILYCASHCOUNT";
						AmountField = "DAILYCASHAMOUNT";

						if (objRs.getDouble("CASHPERTRANX") > 0) {
							if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, objRs.getString("CURR_CODE"), IssuerId) > objRs.getDouble("CASHPERTRANX")) {
								System.out.println("Error Cardholder AMOUNTPERTRANX exceed");
								if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exceeded cash per transaction");
								//return false;
								throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded cash per transaction");
							}
						}else{
							System.out.println("Amount not configured");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("cash amount per tranx not configured");
							//return false;
							throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "cash amount per tranx not configured");
						}

					} else {

						if (objRs.getDouble("AMOUNTPERTRANX") > 0) {
							if (equalAmount(new Double(Amount).doubleValue(), CURRCODE, objRs.getString("CURR_CODE"), IssuerId) > objRs.getDouble("AMOUNTPERTRANX")) {
								System.out.println("Error Cardholder AMOUNTPERTRANX exceed");
								if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exceeded sale per transaction");
								//return false;
								throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded sale per transaction");
							}
						}else{
							System.out.println("Amount not configured");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("sale amount per tranx not configured");
							//return false;
							throw new TPlusException(TPlusCodes.NO_TRANX_CUST, "01", "sale amount per tranx not configured");
						}

					}

					// Check daily and month limit
					int days = 1;

					for (int i = 0; i < 1; i++) {

						AccumCount = 0;
						AccumAmount = 0;

						sbfDTDVal.setLength(0);
						sbfDTDVal.append("SELECT count(tranxcode) as tranxcount, nvl(sum(amount),'0') as tranxamt FROM qryWithdrawLog WHERE (trunc(sysdate)+1 - trunc(datetime)) <=" + days + " AND CARDNUMBER = " + CardNumber);

						if("CASH".equals(TranxType)){
							sbfDTDVal.append(" and tranxcode in ('CASH') ");
						}else{
							sbfDTDVal.append(" and tranxcode in ('TRANSFER','SALE', 'PREAUTH') ");
						}

						// domestic & international
						if(isDomTranx){
							sbfDTDVal.append(" and acqcountrycode = '418' ");
						}else{
							sbfDTDVal.append(" and acqcountrycode <> '418' ");
						}

						if (DebugWriter.boolDebugEnabled)
							DebugWriter.write("    Daily/Monthly Limit Check:");
						System.out.println("SQL=" + sbfDTDVal.toString());

						bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
						TPlusResultSet objRs1 = objDBMan.getResultSet();

						while (objRs1.next()) {
							AccumCount = objRs1.getInt("TRANXCOUNT");
							AccumAmount = objRs1.getDouble("TRANXAMT");
						}

						if (objRs.getInt(CountField) > 0) {
							if (AccumCount >= objRs.getInt(CountField)) {
								System.out.println(" >> CardHolder  Daily Limit Count exceded from customer_Limit Rules");
								if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardHolder  Daily Limit Count exceded from customer_Limit Rules");
								//return false;
								throw new TPlusException(TPlusCodes.EXCEED_WITHDRAW_FREQ_LIMIT, "01", "Exceeded daily limit count");
							}
						}

						if (objRs.getDouble(AmountField) > 0) {
							if (AccumAmount + equalAmount(Amount, CURRCODE, objRs .getString("CURR_CODE"), IssuerId) > objRs.getDouble(AmountField)) {
								System.out.println(" >> CardHolder Daily Limit Amount exceded from Cardholder_Limit Rules");
								if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardHolder Daily Limit Amount exceded from Cardholder_Limit Rules");
								//return false;
								throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded daily limit amount");
							}
						}

						days = 30;

						CountField = "MONTHLYLIMITCOUNT";
						AmountField = "MONTHLYLIMITAMOUNT";

					}

					// ecom transaction validation
					if ("ECOMM".equals(TranxType)) {

						int dayseCom = 1;

						AccumCount = 0;
						AccumAmount = 0;

						CountField = "ECOMM_TRANX_PER_DAY";
						AmountField = "ECOMM_AMOUNT_PER_DAY";

						sbfDTDVal = new StringBuffer();
						sbfDTDVal.append("SELECT count(tranxcode) as tranxcount, nvl(sum(amount),'0') as tranxamt FROM qryWithdrawLog WHERE (trunc(sysdate)+1 - trunc(datetime)) <=" + dayseCom + " AND CARDNUMBER = " + CardNumber);
						sbfDTDVal.append(" and TRANXCODE_SUBTYPE in ('ECOM') ");

						// domestic & international
						if(isDomTranx){
							sbfDTDVal.append(" and acqcountrycode = '418' ");
						}else{
							sbfDTDVal.append(" and acqcountrycode <> '418' ");
						}

						if (DebugWriter.boolDebugEnabled)
							DebugWriter.write("    Daily/Monthly Limit Check of eCom:");
						System.out.println("SQL=" + sbfDTDVal.toString());

						bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
						TPlusResultSet objRs1 = objDBMan.getResultSet();

						while (objRs1.next()) {
							AccumCount = objRs1.getInt("TRANXCOUNT");
							AccumAmount = objRs1.getDouble("TRANXAMT");
						}

						if (objRs.getInt(CountField) > 0) {
							if (AccumCount >= objRs.getInt(CountField)) {
								System.out.println(" >> CardHolder  Daily Limit Count exceded from customer_Limit Rules for eCom");
								if (DebugWriter.boolDebugEnabled) DebugWriter.write(" >> CardHolder  Daily Limit Count exceded from customer_Limit Rules for eCom");
								//return false;
								throw new TPlusException(TPlusCodes.EXCEED_WITHDRAW_FREQ_LIMIT, "01", "Exceeded daily limit count eCom");
							}
						}

						if (objRs.getDouble(AmountField) > 0) {
							if (AccumAmount + equalAmount(Amount, CURRCODE, objRs.getString("CURR_CODE"), IssuerId) > objRs.getDouble(AmountField)) {
								System.out.println(" >> CardHolder Daily Limit Amount exceded from Cardholder_Limit Rules eCom");
								if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardHolder Daily Limit Amount exceded from Cardholder_Limit Rules eCom");
								//return false;
								throw new TPlusException(TPlusCodes.EXEED_WITHDRAW_LIMIT, "01", "Exceeded daily limit amount eCom");
							}
						}

					}

				}
			}

			if(!"470532".equals(CardNumber.subSequence(0, 6))){
				boolean response = creditLimitCheck(CardNumber, Amount, CURRCODE, TranxType, CashLimit, PurchaseLimit, IssuerId);
			}

		} catch (TPlusException tplusexp) {
			throw tplusexp;
		} catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR, "Error :while Retrieving the EqualAmount info" + e);
		} finally {
		} // End
		System.out.println(" RETURN TRUE");
		return true;
	}


	/**
	 * This method is used to check the current balance in Customer Account.
	 * @param CardNumber,Amount,CurrCode IssuerId
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */

	public boolean creditLimitCheck(String CardNumber, double Amount, String CURRCODE,String TranxCode,double CashLimit,double PurchaseLimit,String IssuerId )throws TPlusException
	{
		try {

			double availableAmount = 0.0;
			double totAvailableAmount = 0.0;
			DBManager objDBMan = new DBManager();
			TPlusResultSet objRs = null;
			boolean bolExecute = false;

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT CREDIT_LIMIT,LIMIT_USED,CURRENCY_CODE,CREDIT_LIMIT_PERCENT,CASH_LIMIT_PERCENT,ca.CASH_USED,ca.PURCHASE_USED ");
			sbfDTDVal.append("FROM CUSTOMER_ACCOUNT ca,CARDS c ");
			sbfDTDVal.append("WHERE ca.ACCOUNT_ID = c.ACCOUNT_ID AND CardNumber = "+ CardNumber);
			System.out.println("SQL=" + sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (!objRs.next()) {
				throw new TPlusException(TPlusCodes.APPL_ERR, " No Credit Account available for the tranx card", "01");
			}

			System.out.println("32 " + TranxCode + "  " + PurchaseLimit + "   " + CashLimit + "  " + objRs.getDouble("CREDIT_LIMIT") + "  " + objRs.getDouble("LIMIT_USED"));

			// check limit used condition
			if(objRs.getDouble("LIMIT_USED") < 0){

				// transaction type amount limit exceed
				if ("CASH".equals(TranxCode) || "WITHDRAWAL".equals(TranxCode)) {
					// availableAmount = objRs.getDouble("CREDIT_LIMIT") -
					// objRs.getDouble("LIMIT_USED");
					availableAmount = (objRs.getDouble("CREDIT_LIMIT") * (CashLimit / 100)) - objRs.getDouble("LIMIT_USED");
				} else {
					// availableAmount = objRs.getDouble("CREDIT_LIMIT") -
					// objRs.getDouble("LIMIT_USED");
					availableAmount = (objRs.getDouble("CREDIT_LIMIT") * (PurchaseLimit / 100)) - objRs.getDouble("LIMIT_USED");
				}

				System.out.println("3");
				System.out.println(availableAmount + "  " + Amount + "  " + CURRCODE + "  " + objRs.getString("CURRENCY_CODE"));
				// objRs.getString("CURRENCY_CODE")
				if (equalAmount(Amount, CURRCODE, objRs.getString("CURRENCY_CODE"), IssuerId) > availableAmount) {
					throw new TPlusException(TPlusCodes.NO_SUFFICIENT_FUND, "01", "Exceeded availabe amount of transaction");
					// return false;
				}

			}else{

				// transaction type amount limit exceed
				if ("CASH".equals(TranxCode) || "WITHDRAWAL".equals(TranxCode)) {
					// availableAmount = objRs.getDouble("CREDIT_LIMIT") -
					// objRs.getDouble("LIMIT_USED");
					//availableAmount = (objRs.getDouble("CREDIT_LIMIT") * (CashLimit / 100)) - objRs.getDouble("LIMIT_USED");
					availableAmount = (objRs.getDouble("CREDIT_LIMIT") * (CashLimit / 100)) - objRs.getDouble("CASH_USED");
				} else {
					// availableAmount = objRs.getDouble("CREDIT_LIMIT") -
					// objRs.getDouble("LIMIT_USED");
					//availableAmount = (objRs.getDouble("CREDIT_LIMIT") * (PurchaseLimit / 100)) - objRs.getDouble("LIMIT_USED");
					availableAmount = (objRs.getDouble("CREDIT_LIMIT") * (PurchaseLimit / 100)) - objRs.getDouble("PURCHASE_USED");
				}

				System.out.println("3");
				System.out.println(availableAmount + "  " + Amount + "  " + CURRCODE + "  " + objRs.getString("CURRENCY_CODE"));
				// objRs.getString("CURRENCY_CODE")
				if (equalAmount(Amount, CURRCODE, objRs.getString("CURRENCY_CODE"), IssuerId) > availableAmount) {
					throw new TPlusException(TPlusCodes.NO_SUFFICIENT_FUND, "01", "Exceeded availabe amount of transaction");
					// return false;
				}

			}


			totAvailableAmount = objRs.getDouble("CREDIT_LIMIT")  - objRs.getDouble("LIMIT_USED");

			// total amount exceed
			/*if ("CASH".equals(TranxCode) || "WITHDRAWAL".equals(TranxCode)) {
				// availableAmount = objRs.getDouble("CREDIT_LIMIT") -
				// objRs.getDouble("LIMIT_USED");
				totAvailableAmount = (objRs.getDouble("CREDIT_LIMIT") * (CashLimit / 100)) - objRs.getDouble("LIMIT_USED");
			} else {
				// availableAmount = objRs.getDouble("CREDIT_LIMIT") -
				// objRs.getDouble("LIMIT_USED");
				totAvailableAmount = (objRs.getDouble("CREDIT_LIMIT") * (PurchaseLimit / 100)) - objRs.getDouble("LIMIT_USED");
			}*/

			System.out.println("3");
			System.out.println(totAvailableAmount + "  " + Amount + "  " + CURRCODE + "  " + objRs.getString("CURRENCY_CODE"));
			// objRs.getString("CURRENCY_CODE")
			if (equalAmount(Amount, CURRCODE, objRs.getString("CURRENCY_CODE"), IssuerId) > totAvailableAmount) {
				throw new TPlusException(TPlusCodes.NO_SUFFICIENT_FUND, "01", "Exceeded Total available amount");
				// return false;
			}

		} catch (TPlusException tplusexp) {
			throw tplusexp;
		} catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR, "Error :while Retrieving the CreditLimitCheck  info" + e);
		} finally {
		} // End

		return true;
	}

	/**
	 * This method is used to check the country and city as per risk management.
	 * @param CardNumber,cntrycode,citycode IssuerId
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */

	public boolean riskCntryCity(double CardNumber, String CntryCode, String CityCode)throws TPlusException {
		try {

			DBManager objDBMan = new DBManager();
			TPlusResultSet objRs = null;
			boolean bolExecute=false;

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("select count(*) as CNT from risk_country rct,risk_cities rci ");
			sbfDTDVal.append(" where rct.CARDNUMBER = rci.CARDNUMBER AND rct.CNTRY_CODE= rci.cntry_code and rct.CARDNUMBER="+CardNumber);
			sbfDTDVal.append(" AND (rct.CNTRY_CODE='"+CntryCode +"'");
			if(!CityCode.equals("")) {
				sbfDTDVal.append(" AND rci.city='"+CityCode+"')");
			}
			else {
				sbfDTDVal.append(")");
			}

			System.out.println("SQL="+sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next()) {
				if(objRs.getInt("CNT")>0) {
					return true;
				}
				//throw new TPlusException(TPlusCodes.ERROR_CALL_HELP,"Country/City is available in the Risk Country City List  " );
			}


		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}
		catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the CreditLimitCheck  info"+e);
		}
		finally {
		} // End

		return false;
	}



	/**
	 * This method is used to check the SALE or CASH Spending based on MCC.
	 * @param TranxCode,mcc, IssuerId
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */

	public boolean riskSpendingWithdrawal(double tranxAmt, long cardNumber,String tranxCode, String mcc, String issuerId)throws TPlusException {
		try {

			DBManager objDBMan = new DBManager();
			TPlusResultSet objRs = null;
			boolean bolExecute=false;

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("select tranx_no,rsc.id,tranx_code from risktranx_salecash rsc, risktranx_salecash_mccs rmcc ");
			sbfDTDVal.append(" where rsc.id =rmcc.id");
			sbfDTDVal.append(" and rsc.issuer_id='"+issuerId+"' and tranx_code='"+tranxCode+"' and mcc='"+mcc+"'");
			sbfDTDVal.append(" order by tranx_no");

			System.out.println("SQL="+sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			while (objRs.next()) {

				sbfDTDVal = new StringBuffer();
				sbfDTDVal.append("select avg(amount) as AVGAMT from( select amount from tranxlog ");
				sbfDTDVal.append("where cardnumber ="+cardNumber +" and tranxcode in('"+tranxCode+"') ");
				sbfDTDVal.append(" and mcc='"+mcc+"' order by datetime asc) where rownum<"+objRs.getString("tranx_no"));
				System.out.println("SQL="+sbfDTDVal.toString());
				TPlusResultSet objRs1 = null;
				bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
				System.out.println("1");
				objRs1 = objDBMan.getResultSet();
				if(objRs1.next() && !objRs1.getString("AVGAMT").equals("")) {
					System.out.println("AVGAMT="+objRs1.getDouble("AVGAMT"));
					if(tranxAmt > objRs1.getDouble("AVGAMT")) {
						return true;
						//throw new TPlusException(TPlusCodes.ERROR_CALL_HELP,"Country/City is available in the Risk Country City List  " );
					}
				}

			}


		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}
		catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the riskSpendingWithdrawal"+e);
		}
		finally {
		} // End

		return false;
	}

	/**
	 * This method is used to check the country and city as per risk management.
	 * @param CardNumber,cntrycode,citycode IssuerId
	 * @returns boolean (True,Flase)
	 * @throws TPlusException
	 */

	public boolean riskPeriod(long cardnumber ,String issuerId)throws TPlusException {
		try {

			System.out.println(" ************ Risk Period Checking ****************");

			DBManager objDBMan = new DBManager();
			TPlusResultSet objRs = null;
			boolean bolExecute=false;

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("select period,tranx_no from risk_tranxperiod where issuer_id='"+issuerId+"' ");
			System.out.println("SQL="+sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			while (objRs.next()) {

				sbfDTDVal.setLength(0);
				sbfDTDVal.append("select count(*) as TranxCnt from tranxlog where cardnumber="+cardnumber+" and (sysdate-datetime)*(60*24)<"+ objRs.getInt("period")  );
				System.out.println("SQL="+sbfDTDVal.toString());
				TPlusResultSet objRs1 = null;
				bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
				objRs1 = objDBMan.getResultSet();
				if(objRs1.next()) {
					System.out.println(objRs.getInt("tranx_no")+"     "+objRs1.getInt("TranxCnt"));
					if(objRs1.getInt("TranxCnt") > objRs.getInt("tranx_no")) {
						System.out.println("Risk Period Checking True");
						return true;
						//throw new TPlusException(TPlusCodes.ERROR_CALL_HELP,"Country/City is available in the Risk Country City List  " );
					}
				}

			}


		}
		catch (TPlusException tplusexp) {
			throw tplusexp;
		}
		catch (Exception e) {
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error :while Retrieving the CreditLimitCheck  info"+e);
		}
		finally {
		} // End

		return false;
	}

	/*
public TransactionDataBean checkOffusTransaction(IParser objISO) throws OnlineException
{
	TransactionDataBean objTranxDataBean = objISO.getTransactionDataBean();
	try{
		System.out.println("1");

		if(objISO.hasField(6))
		{
			System.out.println("2");
			if(!objISO.hasField(51))
			{
				System.out.println("4");
				throw new OnlineException("05","A05374","F10 or F51 are missing");
			}
			else
			{
				 System.out.println("3");
				//double rate =getCurrRate();
				System.out.println(objISO.getValue(4)+"  "+TPlusUtility.currConversion(objISO.getValue(6),rate));

				if(validateCurr(objISO.getValue(51)))
				{
					objTranxDataBean.setAmount(new Double(objISO.getValue(4)).doubleValue());
					objTranxDataBean.setTranxAmt(objISO.getValue(4));
					//objTranxDataBean.setTranxSettledAmt(double2str(objISO.getValue(5)));
					objTranxDataBean.setTranxCurrCode(objISO.getValue(49));
					objTranxDataBean.setTranxSettledCurr(objISO.getValue(51));
					objTranxDataBean.setTranxCHAmt(double2str(objISO.getValue(6)));
					if(objISO.hasField(28))
					objTranxDataBean.setTranxFee(objISO.getValue(28));
					else
					objTranxDataBean.setTranxFee("0.0");

				}
				else
				{
					System.out.println("Invalid Currecy");
					throw new OnlineException("05","A05374","Invalid Currency..");
				}
			}
		}
		else
		{
			System.out.println("5");
			System.out.println(objISO.getValue(4));
				if(validateCurr(objISO.getValue(49)))
				{
					System.out.println("valid Currecy");
					objTranxDataBean.setAmount(new Double(objISO.getValue(4)).doubleValue());
					objTranxDataBean.setTranxAmt(objISO.getValue(4));
					objTranxDataBean.setTranxSettledAmt(double2str(objISO.getValue(4)));
					objTranxDataBean.setTranxCHAmt(double2str(objISO.getValue(4)));
					objTranxDataBean.setTranxFee("0.0");
					objTranxDataBean.setTranxCurrCode(objISO.getValue(49));
					objTranxDataBean.setTranxSettledCurr(objISO.getValue(49));
				}
				else
				{
					System.out.println("Invalid Currecy");
					throw new OnlineException("05","A05374","Invalid Currency..");
				}
		}

	}
	catch(Exception exp)
	{
		System.out.println(exp);
		throw new OnlineException("05","A05374","Problem in process the Currency Conversion..");
	}
	return objTranxDataBean;

}
	 */

	public TransactionDataBean checkOffusTransaction(IParser objISO) throws OnlineException
	{

		TransactionDataBean objTranxDataBean = objISO.getTransactionDataBean();

		try{

			objTranxDataBean.setAmount(new Double(objISO.getValue(4)).doubleValue());
			objTranxDataBean.setTranxAmt(objISO.getValue(4));
			objTranxDataBean.setTranxSettledAmt(double2str(objISO.getValue(4)));
			objTranxDataBean.setTranxCHAmt(double2str(objISO.getValue(4)));
			objTranxDataBean.setTranxFee("0.0");
			objTranxDataBean.setTranxSettledCurr(objISO.getValue(49));

		}catch(OnlineException exp)
		{
			System.out.println(exp);
			throw exp;
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			throw new OnlineException("05","A05374","Problem in process the Currency Conversion check off us() - transDB ..");
		}
		return objTranxDataBean;

	}

	public String double2str(String strTranxAtm)
	{
		double dlbAmt = new Double(strTranxAtm).doubleValue()/100;
		return new Double(dlbAmt).toString();

	}

	public void UpdatePinCnt(String strCardno, String pinStatus,int wrongPinCnt)throws Exception
	{

		StringBuffer query = new StringBuffer();
		query.append("UPDATE CARDS SET ");
		query.append("WRONG_PIN_COUNT ='"+wrongPinCnt+"', ");
		query.append("PIN_DISABLED='"+pinStatus+"' ");
		query.append("WHERE CARDNUMBER=" + new Long(strCardno).longValue());

		System.out.println(query.toString());
		DBManager objDBManager = new DBManager();
		objDBManager.executeSQL(query.toString());

	}


	public void UpdatePinCntAndDate(String strCardno, String pinStatus,int wrongPinCnt)throws Exception
	{

		StringBuffer query = new StringBuffer();
		query.append("UPDATE CARDS SET ");
		query.append("WRONG_PIN_COUNT ='"+wrongPinCnt+"', ");

		if(wrongPinCnt == 0){
			query.append("PINBLOCK_DATE =NULL, ");
		}else{
			query.append("PINBLOCK_DATE =SYSDATE, ");
		}
		query.append("PIN_DISABLED='"+pinStatus+"' ");
		query.append("WHERE CARDNUMBER=" + new Long(strCardno).longValue());

		System.out.println(query.toString());
		DBManager objDBManager = new DBManager();
		objDBManager.executeSQL(query.toString());

	}


	public int recordExists(String strTranxType,IParser objISO)throws Exception
	{
		//TransactionDataBean objTransactionDataBean = new TransactionDataBean();
		int recordExists = 2;
		try
		{

			String strMid = objISO.getValue(42);
			String strTID = objISO.getValue(41);
			String strCardno =objISO.getCardNumber();
			//double dlbAtm = new Double(objISO.getValue(4)).doubleValue()/100;
			String strMTI = objISO.getValue(90).substring(0,4);
			String strTraceNo = objISO.getValue(90).substring(4,10);
			String strTransimisionDt = objISO.getValue(90).substring(10,20);
			String strAcqId =  new Long(objISO.getValue(90).substring(20,31)).toString();

			StringBuffer query = new StringBuffer();
			query.append("SELECT COUNT(*) as CNT,DELETED FROM TRANXLOG WHERE 1=1");

			if(strTranxType.equals("VOID") || strTranxType.equals("REVERSAL"))
			{

				query.append(" AND CARDNUMBER='"+strCardno+"' AND TRACENO='"+ strTraceNo+"'");
				query.append(" AND TRANSMISSION_DATETIME='"+strTransimisionDt+"'");
				query.append(" AND MTI='"+strMTI+"' AND ACQID='"+strAcqId+"' AND RESPONSECODE='00' ");
				query.append(" GROUP BY DELETED");

			}

			if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:Reversal Sql"+query.toString());
			System.out.println(query.toString());
			DBManager objDBManager = new DBManager();
			objDBManager.executeSQL(query.toString());
			TPlusResultSet rs = objDBManager.getResultSet();
			if(rs.next())
			{
				if(rs.getInt("CNT")>0)
				{
					if(rs.getString("DELETED").equals("N"))
					{
						recordExists =0;
					}
					else if(rs.getString("DELETED").equals("Y"))
					{
						recordExists =1;
					}
				}

			}

		}catch(Exception vep)
		{
			System.out.println("Exception while getting Total Txn for given card : "+vep.toString());
			throw vep;
		}

		return recordExists;

	}


	public boolean repeatTranxExists(IParser objISO)throws Exception
	{
		//TransactionDataBean objTransactionDataBean = new TransactionDataBean();
		boolean recordExists = false;
		try
		{

			String merchantId = objISO.getValue(42);

			StringBuffer query = new StringBuffer();
			query.append("SELECT COUNT(*) as CNT FROM TRANXLOG WHERE RESPONSECODE='00' ");
			if(objISO.getMTI().equals("0101"))
			{
				query.append(" AND MTI='0100' ");

			}else if(objISO.getMTI().equals("0121"))
			{
				query.append(" AND MTI='0120' ");

			}else if(objISO.getMTI().equals("0201"))
			{
				query.append(" AND MTI='0200' ");
			}
			else if(objISO.getMTI().equals("0401"))
			{
				query.append(" AND MTI='0400' ");

			}else if(objISO.getMTI().equals("0421"))
			{
				query.append(" AND MTI='0420' ");
			}

			if(merchantId != null && !"".equals(merchantId)){
				query.append(" AND MERCHANTID='"+merchantId+"'");
			}

			query.append(" AND CARDNUMBER='"+objISO.getCardNumber()+"' AND TRACENO='"+ objISO.getValue(11)+"'");
			query.append(" AND REFNO='"+objISO.getValue(37)+"' ");

			System.out.println(query.toString());
			DBManager objDBManager = new DBManager();
			objDBManager.executeSQL(query.toString());
			TPlusResultSet rs = objDBManager.getResultSet();
			if(rs.next())
			{
				if(rs.getInt("CNT")>0)
				{
					recordExists= true;
				}

			}

		}catch(Exception vep)
		{
			System.out.println("Exception while getting Total Txn for given card : "+vep.toString());
			throw vep;
		}

		return recordExists;

	}


	public String getApprovalCode(IParser objISO)throws Exception
	{
		//TransactionDataBean objTransactionDataBean = new TransactionDataBean();
		String strApprovalCode = "";
		try
		{
			StringBuffer query = new StringBuffer();
			query.append("SELECT APPROVALCODE FROM TRANXLOG WHERE ");
			if(objISO.getMTI().equals("0101"))
			{
				query.append(" MTI='0100' AND ");
			}
			else if(objISO.getMTI().equals("0401"))
			{
				query.append(" MTI='0400' AND");
			}
			query.append(" MERCHANTID='"+objISO.getValue(42)+"'");
			query.append(" AND CARDNUMBER='"+objISO.getCardNumber()+"' AND TRACENO='"+ objISO.getValue(11)+"'");
			query.append(" AND REFNO='"+objISO.getValue(37)+"' AND RESPONSECODE='00'");
			//AND TRANSMISSION_DATETIME='"+objISO.getValue(7)+"'");


			System.out.println(query.toString());
			DBManager objDBManager = new DBManager();
			objDBManager.executeSQL(query.toString());
			TPlusResultSet rs = objDBManager.getResultSet();
			if(rs.next())
			{
				return rs.getString("APPROVALCODE");
			}

		}catch(Exception vep)
		{
			System.out.println("Exception while getting Total Txn for given card : "+vep.toString());
			throw vep;
		}

		return strApprovalCode;

	}



	public String getJulianDate()throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getJulianDate");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strJulianDate=null;



		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT to_char(sysdate,'YDDDMMHHSS') as juliandate from dual");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				strJulianDate = objRs.getString("juliandate");


			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Issuer Keys"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return strJulianDate;
	}


	public String getCardResponseCode(String strCardStatusId)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:getCardResponseCode");

		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		String strResCode="";

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT RESPONSE FROM CARD_STATUS WHERE CARD_STATUS_ID='"+strCardStatusId+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
			objRs = objDBMan.getResultSet();

			if (objRs.next())
			{
				strResCode = objRs.getString("RESPONSE");
				if(strResCode==null || strResCode.trim().equals(""))
				{
					strResCode="05";
				}
			}

		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while getCardResponseCode"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return strResCode;
	}



	public String insertDebitGL(TransactionDataBean objTranxDataBean) throws Exception {
		StringBuffer strQuery = new StringBuffer();
		try{
			String strMerchantName = objTranxDataBean.getMerchantName();
			if(strMerchantName != null)
			{
				strMerchantName.replace("'","''");
			}

			strQuery.append("INSERT INTO TRANX_DEBIT_GL ( ");
			strQuery.append(" SNO,TRANSACTION_ID,DATETIME,CARD_NO,ISSUER_ID,TRANX_TYPE,GL_TYPE,REFNO,AMOUNT,BILLED )");
			strQuery.append(" VALUES (");
			strQuery.append(" SEQ_TRANXDEBITGL.NEXTVAL ,");
			strQuery.append("'"+ objTranxDataBean.getTransactionId()+"', ");
			strQuery.append(" SYSDATE, ");
			strQuery.append("'"+ objTranxDataBean.getCardNo()+"', ");
			strQuery.append("'ISSUER1', ");
			strQuery.append("'"+ objTranxDataBean.getTranxCode()+"', ");
			strQuery.append("'"+ objTranxDataBean.getGLType()+"', ");
			strQuery.append("'"+strMerchantName +"', ");
			strQuery.append("'"+ objTranxDataBean.getAmount()+"', ");
			strQuery.append("'N') ");

			System.out.println(strQuery.toString());
			DBManager objDBManager = new DBManager();
			objDBManager.executeSQL(strQuery.toString());


		}catch(Exception vep) {
			System.out.println("Exception while building the transaction table query : "+vep.toString());
			throw vep;
		}
		return strQuery.toString();
	}

	/*
public String getTranxPinActivityInsertSql() throws Exception {
        StringBuffer strQuery = new StringBuffer();
        try{
            strQuery.append("INSERT INTO PINACTIVITY_TRANXLOG ( ");
			strQuery.append(" TRANSACTION_ID,DATETIME,CARD_NO,ISSUER_ID,TRANX_TYPE,TRACE_NO,REF_NO,RESPONSE_CODE,TRANSIMISSION_TIME,STATUS )");
			strQuery.append(" VALUES (");
			strQuery.append("'"+ this.getTransactionId()+"', ");
			strQuery.append(" SYSDATE, ");
			strQuery.append("'"+ this.getCardNo()+"', ");
			strQuery.append("'"+ this.getIssuerId()+"', ");
			strQuery.append("'"+ this.getTransactionType()+"', ");
			strQuery.append("'"+ this.getSTAN()+"', ");
			strQuery.append("'"+ this.getRefNo()+"', ");
			strQuery.append("'"+ this.getResponseCode()+"', ");
			strQuery.append("'"+ this.getTransmissionDate()+"', ");
			strQuery.append("'0') ");

        }catch(Exception vep) {
            System.out.println("Exception while building the transaction table query : "+vep.toString());
            throw vep;
        }
        return strQuery.toString();
    }

public String getCardActivityInsertSql(String strCardActivity) throws Exception
{
        StringBuffer strQuery = new StringBuffer();
        try
        {
            strQuery.append("INSERT INTO CARD_ACTIVITY ( ");
			strQuery.append(" ACTIVITY_ID,ISSUER_ID,CARD_NO,ACTIVITY,UPDATED_BY,UPDATED_DATE)");
			strQuery.append(" VALUES (");
			strQuery.append("CARDACTIVITY_SEQ.NEXTVAL,");
			strQuery.append("'"+this.getIssuerId() +"', ");
			strQuery.append("'"+ this.getCardNo()+"', ");
			strQuery.append("'"+ strCardActivity+"', ");
			strQuery.append("'ONLINE',SYSDATE) ");
System.out.println(strQuery.toString());
        }catch(Exception vep) {
            System.out.println("Exception while building the transaction table query : "+vep.toString());
            throw vep;
        }
        return strQuery.toString();
    }

	 */

	public static void main(String s[])throws Exception
	{
		/*DBManager objDBMan = new DBManager();
		objDBMan.initDBPool();
		Connection con=null;
		if(con==null)
		{
			con=objDBMan.getConnection();
		}

		TransactionDB trax = new TransactionDB();

		//trax.createCTFData(con);
		System.out.println(trax.getDBDateTime());
		System.out.println(trax.getRequestHandler("CreditISOFac","0200","000000"));
		//trax.getMerchTermInfo("00000011","");
		//trax.getBatchTotal("10005067000","0000000121");
		System.out.println(trax.riskControlCheck("","989800000000155","333388701000037","4705310008900014",1,"840","SALE"));
		System.out.println(trax.getTerminalMK("00000011"));
		System.out.println(trax.getLogonIssuerWK());
		//	trax.copyToReconciliation("00000011","04","SETTLEMENT","Tranx Not Settled with Merchant");
		//trax.getExistingTranx("00000011","4565982010129243","261203492200","001928");

		//trax.createCTFData(con);
		//trax.checkBacklisted("00000000000000001","4878792873498278");
		System.out.println(trax.getBase2Code("SALE"));
		System.out.println(trax.getTraceNo());
		//IParser objISO = new TPlus8583Parser();
		//trax.getRequestDetails(objISO);

		//trax.generateTCR7("5F2A0208405F34010082025C008407A0000000031010950540800080009A030908319C01009F02060000000000019F03060000000000009F0902008C9F100706010A03A0A9009F1A0204189F1E0832393530313235389F2608E967850EACD469939F2701809F3303E0F0C89F34035E03009F3501229F360200DB9F3704532BB2689F4103000002","05","000");
		//trax.updatePreAuthComplete("242432","2424234","457645645");

		System.out.println(trax.isCTFRecordAvailable("42342343323","12345678","123456","123456789012","SALE"));
		try
		{
			CardInfo cardInfo = trax.getCardInfo(new Long("4705300000000137").longValue());
			System.out.println(trax.withdrawalLimitCheck(cardInfo, "7014" ,"SALE", 12, "840","Issuer1", true));
			//System.out.println(trax.temporaryLimitCheck("6221590000006429","888", "641072", "CASH", 500,"840","BIDV"));
		}catch(TPlusException tplusExp)
		{
			System.out.println(tplusExp.getMessage());
		}
		catch(Exception exp){System.out.println(exp);}*/

		CoreBankReqResBean objBankReqResBean = new CoreBankReqResBean();
		objBankReqResBean.setSourceId("10000012");
		objBankReqResBean.setAtmpos("Y");
		objBankReqResBean.setAcqId("637733");
		objBankReqResBean.setCurrCode("2");
		objBankReqResBean.setAcctNo("102128892888");
		objBankReqResBean.setTraceNo("009035");
		objBankReqResBean.setAmt(200);
		objBankReqResBean.setSale("N");
		objBankReqResBean.setDateTime(DateUtil.getFormattedDate());

		TransactionDB objTransactionDB = new TransactionDB();
		objBankReqResBean = objTransactionDB.getCBReqRes(objBankReqResBean);

		System.out.println(objBankReqResBean.getResCode());

	}

	/**
	 * This method is to update the Limit Used of account
	 * @param
	 * @throws TPlusException
	 */

	public int updateLimitUsed(String accountId, double purchaseAmount, double cashAmount, String sign, String strCardNumber,String strTranxCode)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updateLimitUsed");


		DBManager objDBMan = new DBManager();
		Connection con=null;
		CallableStatement callableStatement=null;
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;
		Long cardnumber = new Long(strCardNumber).longValue();

		try
		{

			// get account info before update
			AccountInfo objAccountInfoPre = getAccountInfo(accountId);

			if(con==null)
			{
				con=objDBMan.getConnection();
			}
			double totAmt = purchaseAmount+cashAmount;

			String CardLimitUsed = "{call CardLimitUsed(?,?,?,?,?,?,?,?,?)}";
			callableStatement = con.prepareCall(CardLimitUsed);
			callableStatement.setLong(1,cardnumber );
			callableStatement.setString(2, sign);
			callableStatement.setString(3, strTranxCode);
			callableStatement.setDouble(4, totAmt);
			callableStatement.registerOutParameter(5, java.sql.Types.DOUBLE);
			callableStatement.registerOutParameter(6, java.sql.Types.DOUBLE);
			callableStatement.registerOutParameter(7, java.sql.Types.DOUBLE);
			callableStatement.registerOutParameter(8, java.sql.Types.DOUBLE);
			callableStatement.registerOutParameter(9, java.sql.Types.DOUBLE);

			// execute getDBUSERByUserId store procedure
			callableStatement.execute();

			double AcctPurUsed = callableStatement.getDouble(5);
			double AcctCashUsed = callableStatement.getDouble(6);
			double CardPurUsed = callableStatement.getDouble(7);
			double CardCashUsed = callableStatement.getDouble(8);
			double LimitUsed = callableStatement.getDouble(9);

			System.out.println("Limit="+AcctPurUsed+"  "+AcctCashUsed+"  "+CardPurUsed+"  "+CardCashUsed+"  "+LimitUsed);


			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("update customer_account ca set ca.limit_used = "+ LimitUsed);
			sbfDTDVal.append(", ca.cash_used =  "+AcctCashUsed);
			sbfDTDVal.append(", ca.purchase_used ="+ AcctPurUsed);

			if("PREAUTH".equals(strTranxCode)){
				sbfDTDVal.append(", ca.preauth_amt =  ca.preauth_amt " + sign + " " + totAmt + " ");
			}

			sbfDTDVal.append(" where ca.account_id='"+accountId+"' ");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.addSQL(sbfDTDVal.toString());

			sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("update cards c set c.cash_used ="+  CardCashUsed);
			sbfDTDVal.append(", c.purchase_used ="+CardPurUsed);
			sbfDTDVal.append(" where c.cardnumber ='"+cardnumber+"' ");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			objDBMan.addSQL(sbfDTDVal.toString());

			String amtSign = "D";
			if("+".equals(sign)){
				amtSign = "D";
			}else{
				amtSign = "C";
			}

			// account change log table insert
			DBUtil objDbUtil = new DBUtil();
			String accLogId = objDbUtil.getSeqNextValue("SEQ_ACCT_CHG_LOG");

			AccountChangeLogBean objAccountChangeLogBean = new AccountChangeLogBean();
			objAccountChangeLogBean.setSno(accLogId);
			objAccountChangeLogBean.setCardNumber(cardnumber);
			objAccountChangeLogBean.setAdjAmt(totAmt);
			objAccountChangeLogBean.setReason("AUTH TRANX");
			objAccountChangeLogBean.setAmtSign(amtSign);
			objAccountChangeLogBean.setAmtSrc(strTranxCode);
			objAccountChangeLogBean.setChangeType("ENGINE");
			objAccountChangeLogBean.setUpdatedBy("ISS ENGINE");
			objAccountChangeLogBean.setPreLimitUsed(objAccountInfoPre.getLimitUsed());
			objAccountChangeLogBean.setPrePurchasedUsed(objAccountInfoPre.getPurchaseUsed());
			objAccountChangeLogBean.setPreCashUsed(objAccountInfoPre.getCashused());
			objAccountChangeLogBean.setLimitUsed(LimitUsed);
			objAccountChangeLogBean.setPurchaseUsed(AcctPurUsed);
			objAccountChangeLogBean.setCashUsed(AcctCashUsed);
			objAccountChangeLogBean.setAccountId(accountId);

			String sql = objAccountChangeLogBean.getInsert(); 

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sql);
			System.out.println(" mySQL="+ sql);
			objDBMan.addSQL(sql);

			boolean result = objDBMan.executeAllSQLs();
			if(result)
			{
				rec = 1;
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB: Successfully Limits Updated...");
				System.out.println("Successfully Limits Updated...");
			}
			else
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB: Error while updating Limits...");
				System.out.println(" Error while updating Limits...");
			}

		}

		catch (TPlusException tplusexp)

		{

			throw tplusexp;

		}

		catch (Exception e)

		{

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in updating Limit Used."+            e.toString());

			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,e.getMessage());

		}

		finally

		{

			try

			{

				if(callableStatement!=null)

				{

					callableStatement.close();

					callableStatement=null;

				}

			}

			catch(Exception objExcep)

			{

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in closing callableStatement statement.");

				throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,

						objExcep.getMessage());

			}

			finally

			{

				try

				{

					if(con!=null)

					{

						objDBMan.closeConnection(con);

						con=null;

					}

				}

				catch(Exception Exp)

				{

					throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,Exp.getMessage());

				}

			}

		}



		return rec;
	}


	/**
	 * This method is to update the Limit Used of account
	 * @param
	 * @throws TPlusException
	 */

	public int updatePreAuth(String accountId, double purchaseAmount, double cashAmount, String sign)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updateLimitUsed");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;

		double totAmt = purchaseAmount+cashAmount;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("update customer_account ca set ca.limit_used =  ca.limit_used + " + totAmt + " ");
			sbfDTDVal.append(", ca.preauth_amt =  ca.preauth_amt " + sign + " " + purchaseAmount + " ");
			sbfDTDVal.append(", ca.purchase_used =  ca.purchase_used " + sign + " " + purchaseAmount + " ");
			sbfDTDVal.append("where ca.account_id='"+accountId+"' ");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			rec = objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return rec;
	}

	public int updatePreAuthTranx(String tranxLogId, String status)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updateLimitUsed");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;

		try
		{

			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("update preauth_tranx set COMPLETED =  '"+status+"' ");
			sbfDTDVal.append("where TRANXLOGID="+tranxLogId+" ");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			rec = objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return rec;
	}

	public int updatePreAuthComplete(String accountId, double amount, String sign)throws TPlusException
	{

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:updateLimitUsed");


		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute=false;
		int  rec = 0;

		try
		{
			StringBuffer sbfDTDVal = new StringBuffer();

			sbfDTDVal.append("update customer_account ca set ");
			sbfDTDVal.append("ca.preauth_amt =  ca.preauth_amt " + sign + " " + amount + " ");
			sbfDTDVal.append("where ca.account_id='"+accountId+"' ");

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" mySQL="+ sbfDTDVal.toString());
			rec = objDBMan.executeUpdate(sbfDTDVal.toString());


		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Error: while retrieving the Working Key for terminal"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return rec;
	}



	public CoreBankReqResBean getCBReqRes(CoreBankReqResBean objBankReqResBean) throws Exception{

		DBManagerCB objDbManager = new DBManagerCB();

		Connection con=null;
		CallableStatement callableStatement=null;

		try{

			if(con==null){
				con=objDbManager.getConnection();
			}

			String cbReqRes = "{call ATM_CASH_WD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

			callableStatement = con.prepareCall(cbReqRes);

			callableStatement.setLong(1,Long.valueOf(objBankReqResBean.getSourceId()));
			callableStatement.setString(2, objBankReqResBean.getAtmpos());
			callableStatement.setLong(3, Long.valueOf(objBankReqResBean.getAcqId()));
			callableStatement.setLong(4, Long.valueOf(objBankReqResBean.getCurrCode()));
			callableStatement.setString(5, objBankReqResBean.getAcctNo());
			callableStatement.setLong(6, Long.valueOf(objBankReqResBean.getTraceNo()));
			callableStatement.setDouble(7, Double.valueOf(objBankReqResBean.getAmt()));
			callableStatement.setString(8, objBankReqResBean.getSale());
			callableStatement.setString(9, objBankReqResBean.getDateTime());

			callableStatement.registerOutParameter(10, OracleTypes.DOUBLE);
			callableStatement.registerOutParameter(11, OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(12, OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(13, OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(14, OracleTypes.DOUBLE);
			callableStatement.registerOutParameter(15, OracleTypes.DOUBLE);
			callableStatement.registerOutParameter(16, OracleTypes.VARCHAR);

			// execute CardLimitUsed store procedure
			callableStatement.execute();

			objBankReqResBean.setAppCode(callableStatement.getString(11));
			objBankReqResBean.setResCode(callableStatement.getString(12));


		}catch (Exception e) {
			throw e;
		}finally{

			if(callableStatement!=null){
				callableStatement.close();
				callableStatement=null;
			}

			if(con!=null){
				objDbManager.closeConnection(con);
				con=null;
			}
		}

		return objBankReqResBean;
	}
	
	//For Reversal
		public CoreBankReqResBean getCBReqResFromRevsal(CoreBankReqResBean objBankReqResBean) throws Exception{

			DBManagerCB objDbManager = new DBManagerCB();

			Connection con=null;
			CallableStatement callableStatement=null;

			try{

				if(con==null){
					con=objDbManager.getConnection();
				}

				String cbReqRes = "{call ATM_CASH_WD_REVRSAL(?,?,?,?,?,?,?,?,?,?,?,?)}";

				callableStatement = con.prepareCall(cbReqRes);

				callableStatement.setLong(1,Long.valueOf(objBankReqResBean.getSourceId()));
				callableStatement.setString(2, objBankReqResBean.getAcctNo());
				callableStatement.setLong(3, Long.valueOf(objBankReqResBean.getTraceNo()));
				callableStatement.setDouble(4, Double.valueOf(objBankReqResBean.getAmt()));
				callableStatement.setString(5, objBankReqResBean.getDateTime());

				callableStatement.registerOutParameter(6, OracleTypes.DOUBLE);
				callableStatement.registerOutParameter(7, OracleTypes.VARCHAR);
				callableStatement.registerOutParameter(8, OracleTypes.VARCHAR);
				callableStatement.registerOutParameter(9, OracleTypes.VARCHAR);
				callableStatement.registerOutParameter(10, OracleTypes.DOUBLE);
				callableStatement.registerOutParameter(11, OracleTypes.VARCHAR);
				callableStatement.registerOutParameter(12, OracleTypes.DOUBLE);

				// execute CardLimitUsed store procedure
				callableStatement.execute();

				objBankReqResBean.setAppCode(callableStatement.getString(7));
				objBankReqResBean.setResCode(callableStatement.getString(8));


			}catch (Exception e) {
				throw e;
			}finally{

				if(callableStatement!=null){
					callableStatement.close();
					callableStatement=null;
				}

				if(con!=null){
					objDbManager.closeConnection(con);
					con=null;
				}
			}

			return objBankReqResBean;
		}
		
		public CoreBankReqResBean getCBReqResFromBalanceEnquiry(CoreBankReqResBean objBankReqResBean) throws Exception{

			DBManagerCB objDbManager = new DBManagerCB();

			Connection con=null;
			CallableStatement callableStatement=null;

			try{

				if(con==null){
					con=objDbManager.getConnection();
				}

				String cbReqRes = "{call BALANCE_ENQUIRY(?,?,?,?)}";

				callableStatement = con.prepareCall(cbReqRes);

				callableStatement.setString(1, objBankReqResBean.getAcctNo());

				callableStatement.registerOutParameter(2, OracleTypes.DOUBLE);
				callableStatement.registerOutParameter(3, OracleTypes.VARCHAR);
				callableStatement.registerOutParameter(4, OracleTypes.VARCHAR);

				// execute CardLimitUsed store procedure
				callableStatement.execute();

				objBankReqResBean.setAppCode(callableStatement.getString(2));
				objBankReqResBean.setBalance(callableStatement.getString(3));
				objBankReqResBean.setResCode(callableStatement.getString(4));


			}catch (Exception e) {
				throw e;
			}finally{

				if(callableStatement!=null){
					callableStatement.close();
					callableStatement=null;
				}

				if(con!=null){
					objDbManager.closeConnection(con);
					con=null;
				}
			}

			return objBankReqResBean;
		}

	/*
	public void testInsert(com.transinfo.tplus.log.TransactionDataBean objTransactionDataBean)throws TPlusException
	{

		System.out.println("Inside test insert");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("Inside test insert");

		DBManager objDbManager = new DBManager();
		PreparedStatement objPrepStatement=null;
		Connection con=null;
		try
		{

			if(con==null)
			{
				con=objDbManager.getConnection();
			}

			StringBuffer strQuery=new StringBuffer("INSERT INTO TEST_INSERT (TINSERT_COULMN,INSERT_DATE) VALUES (?,?)");
			System.out.println("Test SQL="+strQuery.toString());

			if(objPrepStatement==null)
			{
				objPrepStatement=con.prepareStatement(strQuery.toString());
			}

			objPrepStatement.setString(1,"BCEL TEST");
			Long time=objTransactionDataBean.getTimeStamp();
			objPrepStatement.setTimestamp(2,new Timestamp(time.longValue()));

			objPrepStatement.executeUpdate();

			System.out.println("testInsert Success");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("testInsert Success");

		}catch(Exception objExcep){
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("DB Insert Exception :: "+ objExcep.toString());
			throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,objExcep.getMessage());
		}finally{

			try
			{
				if(objPrepStatement!=null)
				{
					objPrepStatement.close();
					objPrepStatement=null;
				}
			}
			catch(Exception objExcep)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("testInsert:Error in closing prepared statement.");
				throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,
						objExcep.getMessage());
			}
			finally
			{
				try
				{
					if(con!=null)
					{
						objDbManager.closeConnection(con);
						con=null;
					}
				}
				catch(Exception Exp)
				{
					throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,Exp.getMessage());
				}
			}

		}

	}*/


}