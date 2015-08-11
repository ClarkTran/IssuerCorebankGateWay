/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore is prohibited.
 * File Name          : PADataBean.java
 * Author             : I.T. Solutions (India) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : PADataBean with get and set methods
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 *
 */

package com.transinfo.tplus.log;

/**
 * This class holds an entry for PA Log.
 * It has get and set methods for all the member variables.
 * It is derived from LogDataBean. This class implements java.io.Serializable.
 */

public class SystemDataBean extends LogDataBean
	implements java.io.Serializable
	{

	private String strModuleCode;
	private String strErrorCode;
	private String strErrorType;
	private String strErrorDescription;


   /*
	* Constructor
	*/
	public SystemDataBean()
	{
	}

   /*
	*Overloaded Constructor
	*/
	public SystemDataBean(String strModuleCode,String strErrorCode,
 		String strErrorType,String strErrorDescription)
	{
		//super("","","","");
		this.strModuleCode=strModuleCode;
		this.strErrorCode=strErrorCode;
		this.strErrorType=strErrorType;
		this.strErrorDescription=strErrorDescription;

	}


   /**
	* Access method for the strModuleCode property.
	* @return the current value of the strModuleCode property
    */
    public String getModuleCode()
    {
		return strModuleCode;
	}

   /**
	* Sets the value of the setModuleCode property.
	* @param setModuleCode the new value of the setModuleCode property
	*/
	public void setModuleCode(String strModuleCode)
	{
		this.strModuleCode=strModuleCode;
	}

   /**
	* Access method for the getErrorCode property.
	* @return the current value of the getErrorCode property
    */
    public String getErrorCode()
    {
		return strErrorCode;
	}

   /**
	* Sets the value of the setErrorCode property.
	* @param setErrorCode the new value of the setErrorCode property
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


	/**
		* Access method for the strErrorDescription property.
		* @return the current value of the strErrorDescription property
	    */
	    public String getErrorDescription()
	    {
			return strErrorDescription;
		}

	   /**
		* Sets the value of the strErrorDescription property.
		* @param strErrorDescription the new value of the strErrorDescription property
		*/
		public void setErrorDescription(String strErrorDescription)
		{
			this.strErrorDescription=strErrorDescription;
	}



}