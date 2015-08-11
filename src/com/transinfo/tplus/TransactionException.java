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

import com.transinfo.tplus.debug.DebugWriter;

/**
*
* This class is a customized exception class for the TPlus application.
*
*/

public class TransactionException extends Exception
{
	private String strErrorCode; //The Error code
	private String strErrorMessage = "TPlus Exception."; //The Error Message
	private String strAuxErrorMessage = ""; //The Aux Error Msg.
    private static String strErrorType[] = {"ERROR","WARRING"};

	private TPlusConnection TPlusconn = null;

	/**
	*
	* This constructor sets the error code creates the TPlusException.
	* @param strErrorCode  Error code for the TPlusExceptions.
	*
	*/
	public TransactionException(String strErrorCode)
	{
		this.strErrorCode = strErrorCode;
	}

	/**
	*
	*  This constructor sets the error code, auxillary message and creates the TPlusException.
	*
	*  @param strErrorCode  Error code for the TPlusExceptions.
	*  @param strAuxMessage Addition message to overwrite the TPlus Exception message.
	*
	*/
	public TransactionException(String strErrorCode, String strErrMessage)
	{

		this.strErrorCode  = strErrorCode;
		this.strErrorMessage = strErrMessage;
	}


	/**
	*
	*  This constructor sets the error code, auxillary message and creates the TPlusException.
	*
	*  @param strErrorCode  Error code for the TPlusExceptions.
	*  @param strErrMessage Error message to overwrite the TPlus Exception message.
	*  @param strAuxMessage Addition message to overwrite the TPlus Aux Exception message.
	*
	*/
	public TransactionException(String strErrorCode,String strErrMessage, String strAuxMessage)
	{
		this.strErrorCode  = strErrorCode;
		this.strErrorMessage = strErrMessage;
		this.strAuxErrorMessage = getErrorMsg(strAuxMessage);
if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionException:strErrorCode="+strErrorCode);
if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionException:strErrorMessage="+strErrorMessage);
if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransactionException:strAuxErrorMessage="+strAuxErrorMessage);


	}


	/**
	 *
	 *  This method returns the Exception Id.
	 *
	 *  @returns the Exception Id
	 *
	 */
	public String getMessageId()
	{
		return strErrorCode;
	}

	/**
	 *
	 *  This method returns the Exception Message. If no message set it will return the default.
	 *
	 *  @returns the Exception Message
	 *
	 */
	public String getMessage()
	{
		return strErrorMessage;
	}

	/**
	 *
	 *  This method returns the Auxillary Exception Message. If no message set it will return the default.
	 *
	 *  @returns the Auxillary Message
	 *
	 */
	public String getAuxillaryMessage()
	{
		return strAuxErrorMessage;
	}


	/**
	*  This method returns the string representation of the object.
	*/
	public String toString()
	{
		StringBuffer	sbrErrorMessage = new StringBuffer();
		sbrErrorMessage.append(strErrorCode);
		sbrErrorMessage.append(".  ");

		if (strErrorMessage != null)
		{
			sbrErrorMessage.append(strErrorMessage);

		}
		sbrErrorMessage.append(".  ");
		if (strAuxErrorMessage != null)
		{
			sbrErrorMessage.append(strAuxErrorMessage);

		}

		return sbrErrorMessage.toString();
	}

	/**
	 * This method sets the TPlusConnection object which is the source of the exception.
	 * @param TPlusConnection - The TPlusconnection object
	 */
	public void setTPlusConnection(TPlusConnection TPlusconn)
	{
		this.TPlusconn = TPlusconn;
	}

	/**
	 * This method returns the TPlusConnection which is the source of the exception.
	 * @return TPlusConnection - The TPlusconnection object
	 */
	public TPlusConnection getTPlusConnection ()
	{
		return TPlusconn;
	}

	/**
	 * This method is used to get the ErrorMsg from the Error String.
	 * @param strExp - The Error String
	 * @return String - The Error element.
	 */
	private static String getErrorMsg(String strExp)
	{
		int intIndex = strExp.indexOf("\"");
		try {
			if (intIndex >=0)
				return strExp.substring(intIndex+1, strExp.indexOf("\"",intIndex+1));
		}
		catch(Exception exp1)
		{
		}
		return strExp;

	}

	public static String getErrorType(int mode)
	{
		if(mode==1)
		return strErrorType[1];
		else
		return strErrorType[0];
	}


  }