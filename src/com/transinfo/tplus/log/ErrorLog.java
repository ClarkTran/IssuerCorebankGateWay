/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore is prohibited.
 * File Name          : ErrorLog.java
 * Author             : I.T. Solutions (Inida) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : ErrorLog with get and set methods
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 * 26/03/2002 	 1.01				RajeeV				  Added SPA UpdateSPALog method declaration for SPA.
 *
 */

package com.transinfo.tplus.log;


/**
 *	This class extends the BaseLog and has method to add
 *  the ErrorDataBean to the ArrayList.
 */

@SuppressWarnings("serial")
public class ErrorLog extends BaseLog implements java.io.Serializable
{

//Added by Rajeev on 26/03/2002
private String strRequestType = "";
//EOA by Rajeev on 26/03/2002

/**
 * Creates an ErrorDataBean and adds this to the arrayList
 * @param strTransId to set the strTransId property in the ErrorDataBean
 * @param strErrorCode to set strErrorCode property in the ErrorDataBean
 * @param strSessionId to set strSessionId property in the ErrorDataBean
 * @param strMerchantId to set strMerchantId property in the ErrorDataBean
 * @param strErrorDesc to set the strErrorDesc of the ErrorDataBean
 */

 public void addLogItem(String strErrorCode,String strIssuerID,
 	String strMerchantId,String strTerminalId, String strErrorDesc,String strRequestType,String strErrorSrc)
 	{
		String strErrorType = "ERROR";
		ErrorDataBean objErrorBean=new ErrorDataBean("",strErrorCode,strIssuerID,
			strMerchantId,strTerminalId,strErrorDesc,strErrorType, strRequestType,strErrorSrc);

		super.addLogItem(objErrorBean);

	}
	/**
	 * Creates an ErrorDataBean and adds this to the arrayList
	 * @param strTransId to set the strTransId property in the ErrorDataBean
	 * @param strErrorCode to set strErrorCode property in the ErrorDataBean
	 * @param strSessionId to set strSessionId property in the ErrorDataBean
	 * @param strMerchantId to set strMerchantId property in the ErrorDataBean
	 * @param strErrorDesc to set the strErrorDesc of the ErrorDataBean
	 */
	public void addLogItemWarning(String strErrorCode,String strIssuerID,
		String strMerchantId,String strTerminalId, String strErrorDesc,String strRequestType,String strErrorSrc)
	{
		String strErrorType = "WARNING";
		ErrorDataBean objErrorBean=new ErrorDataBean("",strErrorCode,strIssuerID,
		strMerchantId,strTerminalId,strErrorDesc,strErrorType,strRequestType,strErrorSrc);
		super.addLogItem(objErrorBean);
	}

	//Added by Rajeev on 26 Mar 2002 for SPA integration
	/**
	* Access method for the strRequestType property.
	* @return the current value of the strRequestType property
	*/
	public String getRequestType()
	{
		return strRequestType;
	}

   /**
	* Sets the value of the strRequestType property.
	* @param strErrorType the new value of the strRequestType property
	*/
	public void setRequestType(String strRequestType)
	{
		this.strRequestType=strRequestType;
	}

	//EOA by Rajeev on 26 Mar 2002
	
}