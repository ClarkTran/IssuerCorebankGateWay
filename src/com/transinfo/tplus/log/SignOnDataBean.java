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
 * This class holds an entry for SignOnDataBean.
 * It has get and set methods for all the member variables.
 * It is derived from SignOnDataBean. This class implements java.io.Serializable.
 */

public class SignOnDataBean extends LogDataBean
	implements java.io.Serializable
	{

	private String strConnName;
	private String strResCode;
	private String strDesc;



   /*
	* Constructor
	*/
	public SignOnDataBean()
	{
	}

   /*
	*Overloaded Constructor
	*/
	public SignOnDataBean(String strConnName,String strResCode,String strDesc)
	{
		this.strConnName=strConnName;
		this.strResCode=strResCode;
		this.strDesc=strDesc;
	}


   /**
	* Access method for the strConnName property.
	* @return the current value of the strConnName property
    */
    public String getConnName()
    {
		return strConnName;
	}

   /**
	* Sets the value of the strConnName property.
	* @param strConnName the new value of the strConnName property
	*/
	public void setConnName(String strConnName)
	{
		this.strConnName=strConnName;
	}

   /**
	* Access method for the strResCode property.
	* @return the current value of the strResCode property
    */
    public String getResCode()
    {
		return strResCode;
	}

   /**
	* Sets the value of the strResCode property.
	* @param strResCode the new value of the strPAType property
	*/
	public void setResCode(String strResCode)
	{
		this.strResCode=strResCode;
	}

   /**
	* Access method for the strDesc property.
	* @return the current value of the strDesc property
    */
    public String getDesc()
    {
		return strDesc;
	}

   /**
	* Sets the value of the strDesc property.
	* @param strDesc the new value of the strDesc property
	*/
	public void setDesc(String strDesc)
	{
		this.strDesc=strDesc;
	}



}