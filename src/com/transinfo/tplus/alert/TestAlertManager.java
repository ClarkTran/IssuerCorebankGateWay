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

package com.transinfo.tplus.alert;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.AlertInfo;
import com.transinfo.tplus.util.DBManager;

public class TestAlertManager
{

	public static void main(String s[])throws Exception
	{

		TestAlertManager objAlertHandler = new TestAlertManager();
		objAlertHandler.initServer();
		AlertInfo objAlertDataBean = new AlertInfo();
		objAlertDataBean.setAlertCode("RISK");
		//AlertManager objAlert = new AlertManager();
		AlertManager.initAlert();
		AlertManager.RaiseAlert("RISK","12345","TEST ERROR");

		System.out.println("*********** FINISH *************");

		/*

			objAlertDataBean = objAlert.getUsers(objAlertDataBean);
			System.out.println(objAlertDataBean.getAlertName());
			System.out.println(objAlertDataBean.getAlertCode());
			System.out.println(objAlertDataBean.getEmailBody());
			System.out.println(objAlertDataBean.getEmailSign());
			ArrayList objArr = objAlertDataBean.getUsers();
			System.out.println("User Len"+objArr.size());
			System.out.println((String)objArr.get(0));
			System.out.println((String)objArr.get(1));

		*/

	}

public void initServer() throws TPlusException
{
        boolean boolError = false;

        //TPlusAdminDaemon = this;
        //Load the TPlus Properties data

        try
        {
			TPlusConfig.loadProperties();

        }
        catch(Exception exp)
        {

            // if there is an error then update the error log and exit
            boolError = true;

            //elTPlusAdmin.addLogItem(TPlusExp.getMessageId(),TPlusExp.getMessage());
            //ErrorLog.updateLog(elTPlusAdmin);
            //throw TPlusExp;
        }

       // Setting the Debug Log file

        DebugWriter.setFileNameAndFormat(TPlusConfig.strDebugFile,TPlusConfig.strDateFormat);

        try
        {
            if (TPlusConfig.boolDebug)
       	        DebugWriter.startDebugging();

        }
        catch(Exception ioExp)
        {

            //Update the error log and exit.
            boolError = true;
            //boolShutdown = true;
            throw new TPlusException(TPlusCodes.LOG_FILE_ERR,TPlusCodes.getErrorDesc(TPlusCodes.LOG_FILE_ERR)+" Error in Opening Debug Log: "+ioExp.getMessage());
        }


		//Initializing DB Pool
		try
		{
			DBManager.initDBPool();
		}
		catch(Exception exp)
		{
			boolError = true;
            //boolShutdown = true;
            throw new TPlusException(TPlusCodes.DATABASE_CONN_ERR,TPlusCodes.getErrorDesc(TPlusCodes.DATABASE_CONN_ERR)+". Error in Creating DB pool:"+exp.getMessage());

			//RAISE ALERT

	 	}

		// Creating the System Log instance
		//elTPlusSystem = new SystemLog();

		//Load the TPlus Config data
        try {

            TPlusConfig.loadConfig();
        }
        catch(TPlusException TPlusExp)
        {
            // if there is an error then update the error log and exit
            boolError = true;
            //boolShutdown = true;
            //elTPlusSystem.addLogItem("TPLUSSERVER",TPlusExp.getMessageId(),TPlusExp.getErrorType(0),"Error in loding Server Config Data from DB:--"+TPlusExp.getMessage());
            //updateAndCloseLog();
            throw TPlusExp;
        }


        //Load the TPlus configuration limits
        /*try {
            TPlusParamValidations.loadConfigLimits();
        }
        catch(TPlusException TPlusExp)
        {

            // if there is an error then update the error log and exit
            boolError = true;

            //boolShutdown = true;
            elTPlusAdmin.addLogItem(TPlusExp.getMessageId(),TPlusExp.getMessage());

            ErrorLog.updateLog(elTPlusAdmin);

            LogWriter.stopErrorLogging();

            throw TPlusExp;
        }*/


    }
}