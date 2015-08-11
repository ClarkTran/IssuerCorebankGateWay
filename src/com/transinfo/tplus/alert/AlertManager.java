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

 import java.util.ArrayList;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.AlertInfo;
import com.transinfo.tplus.javabean.RequestInfo;
import com.transinfo.tplus.log.ErrorLog;
import com.transinfo.tplus.log.LogWriter;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.util.DBManager;

 /**
  * This class has methods to send Email when an alert is raised.
  */


 public class AlertManager implements java.io.Serializable
 {

	private static SMTPClient smtpClient = null;

/**
 * This method is to initiate SMTPClient for sending raised alert msg via mail.
 * @ param none
 * @ param none
 * @ throws TPlusException:if the SMTPClient unable to connect to mail server.
 *
 */

 public static void initAlert()
 {

		smtpClient = new SMTPClient(
							 TPlusConfig.strSMTPServer,TPlusConfig.strSMTPPort,
						 	"",TPlusConfig.strMailSubject,
						 	"",	TPlusConfig.strSMTPFrom);
		smtpClient.start();
 }




	/**
	 * Description: This method sends an email when an alert is raised.
	 * It calls the getMailServerParameters() method in the ConfigDB class
	 * to get the mail server parameters from the database.
	 * @ param alertCode: Alert code
	 * @ param objConfig : The Request which caused the alert
	 * @ throws TPlusException
	 */

	public static void RaiseAlert(String alertCode,String strErrorMsg)
	{
		if(smtpClient == null)
		initAlert();


		RaiseAlert(alertCode,"",strErrorMsg);
	}

	/**
	 *
	 * Description: This method sends an email when an alert is raised.
	 * It calls the getMailServerParameters() method in the ConfigDB class
	 * to get the mail server parameters from the database.
	 * @ param alertCode: Alert code
	 * @ param objConfig : The Request which caused the alert
	 * @ param strErrorMsg: The Java Error Msg
	 * @ throws TPlusException
	 *
	 */

	public static void RaiseAlert(String alertCode,String strTransactionId,String strErrorMsg)
	{
		System.out.println("************************1****************************");

		if(smtpClient == null)
		{
			System.out.println("smtpClient is NULL");
			initAlert();
		}


		AlertInfo objAlertDataBean=new AlertInfo();
		ArrayList arlToMailId=new ArrayList();//To Mail Ids
		IAlertHandler objAlertHandler=null;
		String strMessageBody = "";


		TPlusPrintOutput.printMessage("AlertHandler","Raising alert. Error is " + strErrorMsg);

		try
		{
			 DBManager objDBManager=new DBManager();
			 objAlertDataBean.setAlertCode(alertCode);

			 if(objAlertHandler==null)
			 {
				 objAlertHandler= new AlertHandlerImpl();
			 }

			 objAlertDataBean=objAlertHandler.getUsers(objAlertDataBean);

			 ArrayList arlToMail;
			 int intNumberofUser=0;

			 //Fetching the "To" Mail Id's

			 arlToMailId=objAlertDataBean.getUsers();
			// String strMessageBody = "Time : "+ TPlusUtility.getTPlusDate()+"\n";

			 if (strErrorMsg != null)
			 {
				String strErrorLabel ="Error";

				strMessageBody = strMessageBody+objAlertDataBean.getEmailBody()+"\n"+strErrorLabel+" : "+strErrorMsg+"\n\n\n"+objAlertDataBean.getEmailSign();
			 }
			 else
			 {
				strMessageBody = strMessageBody+objAlertDataBean.getEmailBody()+"\n\n\n"+objAlertDataBean.getEmailSign();
			 }

			System.out.println(TPlusConfig.strSMTPServer+"  "+TPlusConfig.strSMTPPort+"  "+TPlusConfig.strMailSubject+"  "+TPlusConfig.strSMTPFrom+"  "+objAlertDataBean.getStatus());

			ArrayList objMsgArr = new ArrayList();
			objMsgArr.add(strMessageBody);
			objMsgArr.add(arlToMailId);

			 if((objAlertDataBean.getStatus()).equals("A") && (arlToMailId.size()>0))
			 {

				smtpClient.queueMessage(objMsgArr);

		 	}

	  	 }
	  	 catch(TPlusException objTPlusExcp)
	  	 {
			 if (DebugWriter.boolDebugEnabled) DebugWriter.write("AlertHandler:Error in sending email using DB values:"+
	  	 		objTPlusExcp.toString());
				 int code=0;
				 try
				 {
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("AlertHandler:Sending Email using default values.");
					 sendMail();

					 if(objTPlusExcp.getMessageId()!=TPlusCodes.DATABASE_CONN_ERR)
					 {

						//updateErrorLog(objTPlusExcp,strTransactionId);
				 	 }

					if (DebugWriter.boolDebugEnabled) DebugWriter.write("AlertHandler:Email sent using default values.");
				 }

				 catch(Exception objExcp)
				 {
					 if (DebugWriter.boolDebugEnabled) DebugWriter.write("AlertHandler:Error in sending email using default values:"+
						objExcp.toString());
					 TPlusPrintOutput.printMessage("AlertHandler","Exception in sending mail:"+objExcp.getMessage());
				 }
		  }
		 catch(Exception objExcp)
		 {
			 if (DebugWriter.boolDebugEnabled) DebugWriter.write("AlertHandler:Error in sending email using DB values:"+
	  	 		objExcp.toString());

			/* try
			 {
			 	 updateErrorLog(objExcp,strTransactionId);

			 }
			 catch(Exception objExcp1)
			 {
			 	 if (DebugWriter.boolDebugEnabled) DebugWriter.write("AlertHandler:Error in sending email using default values:"+
				 	objExcp1.toString());
			 	    TPlusPrintOutput.printMessage("AlertHandler","Exception in sending mail:"+objExcp1.getMessage());
			 }*/
		 }


	}

	/**
	 * This method is used to send Email for alerts when the data cannot be
	 * retrieved from database. This method gets all the mail server parameters
	 * from the Config File.
	 * @ return int: The status of sending the Email . '0' if mail is
	 *   successfully sent.
	 * @ throws Exception.
	 */

	public static void sendMail() throws Exception
	{
		SMTPClient smtpClient = new SMTPClient(TPlusConfig.strSMTPServer,
			TPlusConfig.strSMTPPort,TPlusConfig.strSMTPTo,TPlusConfig.strMailSubject,
			TPlusConfig.strMailBody,TPlusConfig.strSMTPFrom);

		     smtpClient.sendMail();
	}

	/**
	 * This method is used to send Email for alerts when the data cannot be
	 * retrieved from database. This method gets all the mail server parameters
	 * from the Config File and it also sends the error included in the message.
	 * @param String : The error message to be sent along.
	 * @ return int: The status of sending the Email . '0' if mail is
	 *   successfully sent.
	 * @ throws Exception.
	 */
	public static void sendMail(String strErrorMsg) throws Exception
	{
		SMTPClient smtpClient = new SMTPClient(TPlusConfig.strSMTPServer,
			TPlusConfig.strSMTPPort,TPlusConfig.strSMTPTo,TPlusConfig.strMailSubject,
			TPlusConfig.strMailBody+"\n Error: "+strErrorMsg,TPlusConfig.strSMTPFrom);

			smtpClient.sendMail();
	}


	/**
	 * This method is used to send Email alerts for the designated email address.
	 * This method gets all the mail server parameters
	 * from the Config File and it also sends the error included in the message.
	 * @param String : The error message to be sent along.
	 * @ return int: The status of sending the Email . '0' if mail is
	 *   successfully sent.
	 * @ throws Exception.
	 */
	/*public static void sendMail(String strErrorMsg,String emailId) throws Exception
	{
		SMTPClient smtpClient = new SMTPClient(TPlusConfig.strSMTPServer,
			TPlusConfig.strSMTPPort,emailId,TPlusConfig.strMailSubject,
			TPlusConfig.strMailBody+"\n Error: "+strErrorMsg,TPlusConfig.strSMTPFrom);

			smtpClient.sendMail();
	}*/


	/**
	* This method is used to update the Error Log if an error occurs in sending mail
	* using DB values.
	* @param none.
	* @return none
	* @throws TPlusException.
    */

	private static void updateErrorLog(Exception exp,String strTransactionId)
	{
		ErrorLog objErrorLog=new ErrorLog();
		IParser objISOParser = null;

		try
		{
			System.out.println("************************updateErrorLog****************************");

			LogWriter objLogWriter = new LogWriter();
			RequestInfo objReqInfo = new RequestInfo();
			objReqInfo.setReqError(true);
			objReqInfo.setReturnTo(1);
			objReqInfo.setReqErrCode(TPlusCodes.SMTP_MAIL_ERR);
			objReqInfo.setReqErrMsg(exp.getMessage());
			objReqInfo.setSessionId(strTransactionId);
			objReqInfo.setRequest("");

			objLogWriter.updateLogs(null,objReqInfo);
			System.out.println("************************updateErrorLog1****************************");
		}
		catch(Exception exptn)
		{
			System.out.println(exptn);
			if (DebugWriter.boolDebugEnabled)
			{
				DebugWriter.write("AlertHandler:Error while updating Error Log:"+exptn.toString());
			}
		}


	}


 }