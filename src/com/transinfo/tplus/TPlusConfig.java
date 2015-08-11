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

package com.transinfo.tplus;


import java.util.Map;

import org.jpos.iso.packager.GenericPackager;

import com.transinfo.threadpool.ThreadPool;
import com.transinfo.tplus.db.ConfigDB;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.util.DBManager;


/**
 *
 * @author  Owner
 */
@SuppressWarnings({"unchecked","unused"})
public class TPlusConfig extends BaseConfig {

	// TO Be Removed
	private static GenericPackager p =null;
	public static ThreadPool tpServer = null; // This is a reference to the Thread Pool.
	public static Map connectionMap = null;
	public static String ISSUER_ID="Issuer1";



	/** Creates a new instance of TPlusConfigTest */
	public TPlusConfig() {
		super();
	}

	public static void loadConfig() throws TPlusException
	{
		try
		{
			if(!boolReadINI)
			{
				throw new TPlusException(TPlusCodes.INI_FILE_ERR,TPlusCodes.getErrorDesc(TPlusCodes.INI_FILE_ERR));
			}


			BaseConfig.strServerId = getServerId();
			TPlusPrintOutput.printMessage("TPlusConfig","Loading Server parameters...");
			messageHandler = ConfigDB.getMessageConfig();
			//TransactionDB objTranx = new TransactionDB();
			//connectionMap = objTranx.getSignOnList();

		}
		catch(TPlusException tplusExp)
		{
			throw tplusExp;
		}
		catch(Exception e)
		{

			throw new TPlusException(TPlusCodes.APPL_ERR,e.getMessage());
		}

	}

	public static Map getConnectionMap()
	{
		return connectionMap;

	}


	public static String getServerId()
	{
		return "Server Started Time";
	}

	/**
	 * Method to set the running mode from the mode given in the SLNo.
	 */
	/*	private static void setRunningMode(String strSerialNo)
	{

		String strMode = strSerialNo.substring(10,12);
		int intMode = Integer.parseInt(strMode);
		intMode = 1;
		switch (intMode)
		{
			case 1:
			{
				BaseConfig.boolNormalEnabled = true;
				BaseConfig.boolEMVEnabled = true;
				BaseConfig.boolLoyaltyEnabled = true;
				break;
			}

		}

	}
	 */


	public static void main(String s[])
	{
		try
		{
			System.out.println("SMTP server ="+strSMTPServer);
			loadProperties();
			DBManager.initDBPool();
			TPlusConfig tplusCon = new TPlusConfig();
			tplusCon.loadConfig();
			//System.out.println(objMsgHandler.getPort());
			//System.out.println(objMsgHandler.getMessageType());
			/* System.out.println(tplusCon.getSystemParam().getTransactionID());
		 ErrorLog objErrorLog = new ErrorLog();

		 objErrorLog.setTransactionID(tplusCon.getSystemParam().getTransactionID());
		 objErrorLog.addLogItem("701","seson123","mer123","ter123","errordesc");
		 WriteLogDB wdb = new WriteLogDB();
		 wdb.updateLog(objErrorLog);

		 TransactionLog tranLog = new TransactionLog();
		 tranLog.setTransactionID(tplusCon.getSystemParam().getTransactionID());
		 tranLog.addLogItem("","Merid","ter123","434","40000000000002","0908",
		 			"123.90","720","Tra123","MCC","tcc","Track2Data","invNo12",
		 			"ref123","re","app123","pin123","C","D",
		 			"010101","000000","pos1","S","R","track2",
		 			"ref123","0101010000","aac","F","Q","P",
					"S","55","strDesc");
		wdb.updateLog(tranLog);
		System.out.println("Transaction log updated");
		SystemLog sysLog = new SystemLog();
		sysLog.addLogItem("TPLUSSERVER","107","WARRINING","TESTING");
		wdb.updateLog(sysLog);*/
		}
		catch(Exception e)
		{
			TPlusPrintOutput.printMessage("TPlusConfig","TPlus Initlization Failed " + e.getMessage());
		}

	}


}
