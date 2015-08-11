/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore is prohibited.
 * File Name          : ErrorDataBean.java
 * Author             : I.T. Solutions (Inida) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : ErrorDataBean with get and set methods
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 * 26/03/2002 	 1.01				RajeeV				  Added SPA UpdateSPALog method declaration for SPA.
 *
 */

package com.transinfo.tplus.log;


/**
 *	This class holds an entry for Error Log.
 *  It has set and get method for all the member variables.
 *  It is derived from LogDataBean This class implements java.io.Serializable.
 */

public class ErrorDataBean extends LogDataBean
	implements java.io.Serializable
{

	private String strErrorCode;
	private String strErrorType;
	private String strErrorDescription;
	private String strErrorSrc;

	//Added by Rajeev on 26/03/2002
	private String strRequestType;
	//Added by Rajeev on 26/03/2002

   /*
	* Constructor
   	*/
   	public ErrorDataBean( )
   	{


	}

   /*
	* Overloaded Constructor
   	*/
   	public ErrorDataBean(String strTransId, String strErrorCode,String strIssuerID,
   		 String strMerchantId,String strTerminalId,String strErrorDescription,
   		String strErrorType,String strRequestType,String strErrorSrc)
   	{
		super(strTransId,strIssuerID,strMerchantId,strTerminalId);
		this.strErrorCode=strErrorCode;
		this.strErrorDescription=strErrorDescription;
		this.strErrorType=strErrorType;
		this.strRequestType=strRequestType;
		this.strErrorSrc=strErrorSrc;
	}


   /**
	* Access method for the strErrorCode property.
	* @return the current value of the strErrorCode property
    */
    public String getErrorCode()
    {
		return strErrorCode;
	}

   /**
	* Sets the value of the strErrorCode property.
	* @param strErrorCode the new value of the strErrorCode property
	*/
	public void setErrorCode(String strErrorCode)
	{
		this.strErrorCode=strErrorCode;
	}

   /**
	* Access method for the strErrorType property.
	* @return the current value of the strErrorType property
	*/
	public String getErrorType()
	{
		return strErrorType;
	}

   /**
	* Sets the value of the strErrorType property.
	* @param strErrorType the new value of the strErrorType property
	*/
	public void setErrorType(String strErrorType)
	{
		this.strErrorType=strErrorType;
	}

    /** Setter for property ReqError.
     * @param ReqError New value of property ReqError.
     *
     */
    public void setErrorSrc(String strErrorSrc) {
        this.strErrorSrc = strErrorSrc;
    }

    /** Getter for property Request.
     * @return Value of property Request.
     *
     */
    public java.lang.String getErrorSrc() {
        return strErrorSrc;
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

   /**
	* Access method for the strErrorDescription property.
	* @return the current value of the strErrorDescription property
	*/
	public String getErrorDesc()
	{
		return strErrorDescription;
	}

   /**
	* Sets the value of the strErrorDescription property.
	* @param strErrorDescription the new value of the strErrorDescription property
	*/
	public void setErrorDesc(String strErrorDescription)
	{
		this.strErrorDescription=strErrorDescription;
	}


	public String toString()
	{
		return "ErrorDataBean-"+strErrorCode+";"+strErrorType+";"+strErrorDescription;
	}

}