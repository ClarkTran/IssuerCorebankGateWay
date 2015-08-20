/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore. is prohibited.
 * File Name          : WriteLogDB.java
 * Author             : I.T. Solutions (Inida) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : WriteLogDB with the update methods
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 * 29-12-2001	   1.01				Rohini R			  Updated all methods so as to
 *														  form prepared stmts if needed
 *														  only.
 *
 * 26/03/2002 	   1.02				RajeeV				  Added SPA functionality
 * 06-July-2002	   1.03				Kabilan A.N			  For UAT bug fix - Removed SOPs.
 * 28-May-2003	   1.04				Rajeev				  SecureCode Changes.
 */

package com.transinfo.tplus.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.log.ErrorDataBean;
import com.transinfo.tplus.log.ErrorLog;
import com.transinfo.tplus.log.SystemDataBean;
import com.transinfo.tplus.log.SystemLog;
import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;


/**
 * This class has methods to update the logs to the database.
 * The methods of this class are called by the WriteLogSB.
 */

@SuppressWarnings({ "unused", "unchecked", "serial" })
public class WriteLogDB implements java.io.Serializable
{

	/**
	 * This method inserts all the elements in the ArrayList
	 * to the TRANSACTION_LOG table using  prepared statement.
	 * @param   objTransLog that is to be added to Database
	 * @throws TPlusException
	 */

	public static void updateLog(com.transinfo.tplus.log.TransactionDataBean objTransDataBean)throws TPlusException
	{
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB: Updating Transaction Log");

		String strTranxType = "";
		String strOrgMTI    = "";
		double dblAmt 		= 0;

		DBManager objDbManager = new DBManager();
		PreparedStatement objPrepStatement=null;
		Connection con=null;

		try
		{

			if(con==null)
			{
				con=objDbManager.getConnection();
			}

			StringBuffer strQuery=new StringBuffer("INSERT INTO TRANXLOG("+
					"TRANXLOGID,ISSUER_ID,DATETIME,TERMINALID, TRANXCODE,CARDNUMBER, AMOUNT, CURRCODE, TRACENO, MCC,"+
					"REFNO,APPROVALCODE, RESPONSECODE,DELETED,ACQID, TRACENO2, TRANX_FEE,MTI,F90,REMARKS)"+
			" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			System.out.println("SQL="+strQuery.toString());

			if(objPrepStatement==null)
			{
				objPrepStatement=con.prepareStatement(strQuery.toString());
			}

			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT SEQ_TRANXLOG.NEXTVAL AS TRANXID FROM DUAL");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			boolean bolExecute = objDbManager.executeSQL(sbfDTDVal.toString());
			TPlusResultSet objRs = objDbManager.getResultSet();
			String strTransId ="";

			if (objRs.next())
			{
				strTransId = objRs.getString("TRANXID");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB: TRAXCODE:"+objTransDataBean.getMTI()+"  "+objTransDataBean.getProcessingCode());

			System.out.println("1 : TransactionId  "+strTransId);
			objTransDataBean.setTransactionId(strTransId);
			objPrepStatement.setString(1,strTransId);
			
			System.out.println("2 -Issuer Id : "+objTransDataBean.getIssuerId());
			objPrepStatement.setString(2,objTransDataBean.getIssuerId());
			
			Long time=objTransDataBean.getTimeStamp();
			System.out.println("3 - time :  "+time.longValue());
			objPrepStatement.setTimestamp(3,new Timestamp(time.longValue()));
			
			objPrepStatement.setString(4,isNull(objTransDataBean.getTerminalId()));
			System.out.println("4 - TerminalId  "+objTransDataBean.getTerminalId());
			
			objPrepStatement.setString(5,objTransDataBean.getTranxCode());
			System.out.println("5 - TranxCode "+objTransDataBean.getTranxCode());
			
			objPrepStatement.setString(6,isNull(objTransDataBean.getCardNo()));
			System.out.println("6 - CardNo "+objTransDataBean.getCardNo());

			if(objTransDataBean.getTranxAmt() ==null || objTransDataBean.getTranxAmt().trim().equals(""))
			{
				objTransDataBean.setTranxAmt("0");
			}

			objPrepStatement.setDouble(7,(Double.valueOf(objTransDataBean.getTranxAmt()).doubleValue())/Integer.valueOf(objTransDataBean.getCurrDecimalPoint()));
			System.out.println("7 - Amount  "+Double.valueOf(objTransDataBean.getTranxAmt()).doubleValue()/Integer.valueOf(objTransDataBean.getCurrDecimalPoint()));
			
			objPrepStatement.setString(8,isNull(objTransDataBean.getTranxCurrCode()));
			System.out.println("8 - TranxCurrCode "+objTransDataBean.getTranxCurrCode());
			
			objPrepStatement.setString(9,isNull(objTransDataBean.getTraceNo()));
			System.out.println("9 - TraceNo "+objTransDataBean.getTraceNo());
			
			objPrepStatement.setString(10,isNull(objTransDataBean.getMCC()));
			System.out.println("10 - MCC "+objTransDataBean.getMCC());

			objPrepStatement.setString(11,isNull(objTransDataBean.getRefNo()));
			System.out.println("11 - RefNo "+objTransDataBean.getRefNo());
			
			objPrepStatement.setString(12,isNull(objTransDataBean.getApprovalCode()));
			System.out.println("12 - ApprovalCode "+objTransDataBean.getApprovalCode());
			
			objPrepStatement.setString(13,isNull(objTransDataBean.getResponseCode()));
			System.out.println("13 - ResponseCode "+objTransDataBean.getResponseCode());

			objPrepStatement.setString(14,isNull(objTransDataBean.getDeleted()));
			System.out.println("14 - Deleted "+objTransDataBean.getDeleted());

			objPrepStatement.setString(15,isNull(objTransDataBean.getAcqInstId()));
			System.out.println("15 "+objTransDataBean.getAcqInstId());
			
			objPrepStatement.setString(16,isNull(objTransDataBean.getTraceNo2()));
			System.out.println("16 -TraceNo2 :  "+objTransDataBean.getTraceNo2());

			objPrepStatement.setString(17,isNull(objTransDataBean.getTranxFee()));
			System.out.println("17 - TranxFee :  "+objTransDataBean.getTranxFee());
			
			objPrepStatement.setString(18,isNull(objTransDataBean.getMTI()));
			System.out.println("18 - MTI:  "+objTransDataBean.getMTI());
			
			objPrepStatement.setString(19,isNull(objTransDataBean.getF90()));
			System.out.println("19 - F90 :  "+objTransDataBean.getF90());
			
			objPrepStatement.setString(20,isNull(objTransDataBean.getRemarks()));
			System.out.println("20 - Remarks :  "+objTransDataBean.getRemarks());
			
			objPrepStatement.executeUpdate();

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Sucessfully updated TransLog");
		}
		catch(TPlusException objTPlusExcep)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in updating TransactionLog."+
					objTPlusExcep.toString());
			throw objTPlusExcep;
		}
		catch(Exception objExcep)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in updating TransactionLog."+
					objExcep.toString());
			throw new TPlusException
			(TPlusCodes.SQL_QUERY_ERR,objExcep.getMessage());
		}
		finally
		{
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
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in closing prepared statement.");
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

	}


	public static void insertPreAuthTranx(com.transinfo.tplus.log.TransactionDataBean objTransDataBean)throws TPlusException
	{
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB: Updating Transaction Log");

		String strTranxType = "";
		String strOrgMTI    = "";
		double dblAmt 		= 0;

		DBManager objDbManager = new DBManager();
		PreparedStatement objPrepStatement=null;
		Connection con=null;

		try
		{

			if(con==null)
			{
				con=objDbManager.getConnection();
			}

			StringBuffer strQuery=new StringBuffer("INSERT INTO PREAUTH_TRANX("+
					"AUTHID,TRANXLOGID,COMPLETED,EXPIRY_DATE,CREATE_DATE )"	+
			" VALUES(?,?,?,?,?)");
			System.out.println("SQL="+strQuery.toString());

			if(objPrepStatement==null)
			{
				objPrepStatement=con.prepareStatement(strQuery.toString());
			}




			StringBuffer sbfDTDVal = new StringBuffer();
			sbfDTDVal.append("SELECT SEQ_PREAUTH_ID.NEXTVAL AS PREAUTHID FROM DUAL");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionDB:"+sbfDTDVal.toString());
			System.out.println(" SQL="+ sbfDTDVal.toString());
			boolean bolExecute = objDbManager.executeSQL(sbfDTDVal.toString());
			TPlusResultSet objRs = objDbManager.getResultSet();
			String preAuthId ="";

			if (objRs.next())
			{
				preAuthId = objRs.getString("PREAUTHID");

			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB: TRAXCODE:"+objTransDataBean.getMTI()+"  "+objTransDataBean.getProcessingCode());

			//String strTranxCode = TPlusTransType.getTranxType(objTransDataBean.getMTI(),objTransDataBean.getProcessCode());
			//objPrepStatement.setString(1,objTransLog.getTransactionID());

			System.out.println("1 : PREAUTH Id  "+preAuthId);
			objPrepStatement.setString(1,preAuthId);

			System.out.println("2 -TranxLog Id : "+objTransDataBean.getTransactionId());
			objPrepStatement.setString(2,objTransDataBean.getTransactionId());

			System.out.println("3 - Completed  :"+"N");
			objPrepStatement.setString(3,"N");

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, 7);
			Date expDate = calendar.getTime();

			System.out.println("4 - Expiry Date :"+expDate);
			objPrepStatement.setTimestamp(4,new Timestamp(expDate.getTime()));

			Long time=objTransDataBean.getTimeStamp();
			System.out.println("5 - time :  "+time.longValue());
			objPrepStatement.setTimestamp(5,new Timestamp(time.longValue()));

			objPrepStatement.executeUpdate();

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Sucessfully updated TransLog");
		}
		catch(TPlusException objTPlusExcep)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in updating TransactionLog."+
					objTPlusExcep.toString());
			throw objTPlusExcep;
		}
		catch(Exception objExcep)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in updating TransactionLog."+
					objExcep.toString());
			throw new TPlusException
			(TPlusCodes.SQL_QUERY_ERR,objExcep.getMessage());
		}
		finally
		{
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
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in closing prepared statement.");
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

	}


	//EOA by Rajeev on 26/03/2002.

	/**
	 * This method inserts all the elements in the ArrayList
	 * to the ERROR_LOG table using  prepared statement.
	 * @param   objErrorLog that is to be added to Database
	 */

	public static void updateLog(ErrorLog objErrorLog)throws TPlusException
	{

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB: Updating Error Log");
		//Added By Rohini
		ArrayList objArlLog=objErrorLog.getLog();
		if (objArlLog.size() > 0)
		{
			//Added By Rohini

			ErrorDataBean objErrorDataBean;
			DBManager objDbManager = new DBManager();
			Connection con=null;
			PreparedStatement objPrepStatement=null;
			try
			{
				if(con==null)
				{
					con=objDbManager.getConnection();
				}

				StringBuffer strQuery=new StringBuffer("INSERT INTO TRANX_ERRORLOG(ERROR_ID,ISSUER_ID,REQUEST_TYPE,"+
						"ERROR_CODE,ERROR_TYPE,ERROR_SRC,MERCHANT_ID,TERMINAL_ID,DESCRIPTION,DATETIME) "+
				"VALUES(?,?,?,?,?,?,?,?,?,?)");

				System.out.println("Error Log="+strQuery.toString());

				if(objPrepStatement==null)
				{
					objPrepStatement=con.prepareStatement(strQuery.toString());
				}

				for(int i=0;i<objArlLog.size();i++)
				{

					StringBuffer sbfDTDVal = new StringBuffer();
					sbfDTDVal.append("SELECT SEQ_ERRORLOG.NEXTVAL AS ERRORID FROM DUAL");
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("ERRORDB:"+sbfDTDVal.toString());
					System.out.println(" SQL="+ sbfDTDVal.toString());
					boolean bolExecute = objDbManager.executeSQL(sbfDTDVal.toString());
					TPlusResultSet objRs = objDbManager.getResultSet();
					String strErrorId ="";

					if (objRs.next())
					{
						strErrorId = objRs.getString("ERRORID");

					}

					System.out.println("ERROR ID="+strErrorId);

					objErrorDataBean=(ErrorDataBean)objArlLog.get(i);
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+strErrorId);
					objPrepStatement.setString(1,isNull(strErrorId));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+objErrorDataBean.getIssuerId());
					objPrepStatement.setString(2,isNull(objErrorDataBean.getIssuerId()));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+objErrorDataBean.getRequestType());
					objPrepStatement.setString(3,isNull(objErrorDataBean.getRequestType()));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+objErrorDataBean.getErrorCode());
					objPrepStatement.setString(4,isNull(objErrorDataBean.getErrorCode()));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+objErrorDataBean.getErrorType());
					objPrepStatement.setString(5,isNull(objErrorDataBean.getErrorType()));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+objErrorDataBean.getErrorSrc());
					objPrepStatement.setString(6,isNull(objErrorDataBean.getErrorSrc()));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+objErrorDataBean.getMerchantId());
					objPrepStatement.setString(7,isNull(objErrorDataBean.getMerchantId()));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+objErrorDataBean.getTerminalId());
					objPrepStatement.setString(8,isNull(objErrorDataBean.getTerminalId()));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:"+objErrorDataBean.getErrorDesc());
					objPrepStatement.setString(9,isNull(objErrorDataBean.getErrorDesc()));
					Long time=objErrorDataBean.getTimeStamp();
					objPrepStatement.setTimestamp(10,new Timestamp(time.longValue()));

					objPrepStatement.executeUpdate();

				}
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Successfully updated ErrorLog.");

			}
			catch(TPlusException objTPlusExcep)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in updating ErrorLog"+
						objTPlusExcep.toString());
				throw objTPlusExcep;
			}
			catch(Exception objExcep)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in updating ErrorLog"+
						objExcep.toString());
				throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,objExcep.getMessage());
			}
			finally
			{
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
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in closing prepared statement.");
					//throw new TPlusException(TPlusCodes.SQL_QUERY_ERR,objExcep.getMessage());
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
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in closing DB Connection...");
					}
				}
			}
		}
	}

	/**
	 * This method inserts all the elements in the ArrayList
	 * to the PA_LOG table using  prepared statement.
	 * @param   objCacheLog that is to be added to Database
	 */




	/**
	 * This method inserts all the elements in the ArrayList
	 * to the SYSTEMLOG table using  prepared statement.
	 * @param   objSysLog that is to be added to Database
	 */
	public static void updateLog(SystemLog objSysLog)throws TPlusException
	{

		ArrayList objArlLog=objSysLog.getLog();
		if (objArlLog.size() > 0)
		{

			SystemDataBean objSysDataBean;
			DBManager objDbManager = new DBManager();
			Connection con=null;
			PreparedStatement objPrepStatement=null;
			try
			{
				if(con==null)
				{
					con=objDbManager.getConnection();
				}

				StringBuffer strQuery=new StringBuffer("INSERT INTO SYSTEMLOG("+
				"MODULE_ID,ERROR_CODE,ERROR_TYPE,DESCRIPTION,TIME_STAMP) VALUES(?,?,?,?,?)");

				if(objPrepStatement==null)
				{
					objPrepStatement=con.prepareStatement(strQuery.toString());
				}

				for(int i=0;i<objArlLog.size();i++)
				{
					objSysDataBean=(SystemDataBean)objArlLog.get(i);
					objPrepStatement.setString(1,objSysDataBean.getModuleCode());
					objPrepStatement.setString(2,objSysDataBean.getErrorCode());
					objPrepStatement.setString(3,objSysDataBean.getErrorType());
					objPrepStatement.setString(4,objSysDataBean.getErrorDescription());
					Long time=objSysDataBean.getTimeStamp();
					objPrepStatement.setTimestamp(5,new Timestamp(time.longValue()));
					objPrepStatement.executeUpdate();
				}
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Successfully updated SYSTEMLOG.");

			}
			catch(TPlusException objTPlusExcep)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB: Error in updating SYSTEMLOG "+
						objTPlusExcep.toString());
				throw objTPlusExcep;
			}
			catch(Exception objExcep)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB: Error in updating SYSTEMLOG "+
						objExcep.toString());
				throw new TPlusException
				(TPlusCodes.SQL_QUERY_ERR,"Error in updating SYSTEMLOG"+objExcep.getMessage());
			}
			finally
			{
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
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriteLogDB:Error in closing prepared statement.");
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
						System.out.println("100 ");
						throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,Exp.getMessage());
					}
				}
			}
		}
	}

	// TO BE REMOVED
	/**
	 * Method check for value is null
	 * @param String
	 * @return String
	 */

	public static String isNull(String strValue)
	{
		return strValue == null ? " " : strValue;
	}


}