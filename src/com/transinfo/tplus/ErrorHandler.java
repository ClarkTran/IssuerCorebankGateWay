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


//TPlus specific imports
import com.transinfo.mail.SMTPClient;
import com.transinfo.tplus.debug.DebugWriter;

/**
 * This class is used handle errors.
 */
public class ErrorHandler
{
    /**
     * This method is used to handle critical errors.
     */
    public static void raiseDBAlert(String strErrCode, Exception exp) {
        if (!TPlusAdminDaemon.boolShutdown)
        {
            System.out.println("***********ATTENTION: Error occured while "+TPlusCodes.getErrorDesc(strErrCode)+". Error:"+exp.getMessage());
            System.out.println("***********ATTENTION: Sending Email to Admin");
            if (DebugWriter.boolDebugEnabled) DebugWriter.write("***********ATTENTION: Error occured while "+TPlusCodes.getErrorDesc(strErrCode)+". Error:"+exp.getMessage());
            if (DebugWriter.boolDebugEnabled) DebugWriter.write("***********ATTENTION: Sending Email to Admin");

            //send the mail to the administrator..
            String strMailMessage = "";
            //int intIndex = TPlusConfig.strErrorMailMsg.indexOf("<DYNERR>");
            strMailMessage = "***********ATTENTION:****************\n Error occured while "+TPlusCodes.getErrorDesc(strErrCode)+". Error:"+exp.getMessage();
            System.out.println(strMailMessage);
            try{
	            SMTPClient smtpClient = new SMTPClient();
	            int intReturn = -1;
	            if (!smtpClient.sendMail("DB Error",strMailMessage))
	            {
	
	              System.out.println("***********ATTENTION: Mail could not be sent to the Administrator. Return code:");
	              if (DebugWriter.boolDebugEnabled) DebugWriter.write("***********ATTENTION:Mail could not be sent to the Administrator. Return code:");
	            }
	            else
	            {
	            	if (DebugWriter.boolDebugEnabled) DebugWriter.write("***********ATTENTION:Mail Sent***************");
	            	System.out.println("***********ATTENTION:Mail Sent***************");
	            }
            
            }catch(TPlusException tplusExp){System.out.println("In Error Handler"+tplusExp);}
             System.out.println("****************************"); 
        }
    }

    /**
     * This method is used to handle not critical file close errors.
     */
    public static void handleCloseError(String strErrorFile, Exception exp)
    {
        if (DebugWriter.boolDebugEnabled) DebugWriter.write("*********ATTENTION: Error while closing "+strErrorFile+" file. Error: "+exp.getMessage());
        System.out.println("*********ATTENTION: Error while closing "+strErrorFile+" file. Error: "+exp.getMessage());
    }


}