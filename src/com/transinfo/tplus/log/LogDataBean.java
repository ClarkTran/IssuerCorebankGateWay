/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore is prohibited.
 * File Name          : LogDataBean.java
 * Author             : I.T. Solutions (India) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : LogDataBean with get and set methods
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 *
 */

package com.transinfo.tplus.log;


import java.util.Date;

import com.transinfo.tplus.TPlusCalendar;
import com.transinfo.tplus.db.ConfigDB;
/** This is base class holds the common data LogDataBean.
 *  It has set and get methods all the private member variables.
 *  Each specific LogDataBean inherits from this class.
 *  This class implements java.io.Serializable.
 */

public class LogDataBean implements java.io.Serializable{

	private String strIssuerId;
	private String strTransId;
	private String strMerchantId;
	private String strTerminalId;
	private String strSessionId;
	private Long lngTimeStamp;
	private Date dteTimeStamp;


	public LogDataBean()
    {
		TPlusCalendar TPluscal = new TPlusCalendar();
		TPluscal.setTime(ConfigDB.getTPlusCalender());
		this.lngTimeStamp=new Long(TPluscal.getTimeInMillis());

	}



   /*
    * Overloaded constructor
    */
    public LogDataBean(String strTransId,String strIssuerId,String strMerchantId,String strTerminalId)
    {
		this.strIssuerId = strIssuerId;
		this.strTransId=strTransId;
		this.strMerchantId=strMerchantId;
		this.strTerminalId=strTerminalId;
		TPlusCalendar TPluscal = new TPlusCalendar();
		TPluscal.setTime(ConfigDB.getTPlusCalender());
		this.lngTimeStamp=new Long(TPluscal.getTimeInMillis());
	}


   /**
	* Access method for the strTransId property.
	* @return   the current value of the strTransId property
    */

	public String getTransId()
	{
		return 	strTransId;
	}

   /**
	* Sets the value of the strTransId property.
	* @param strTransId the new value of the strTransId property
	*/

	public void setTransId(String strTransId)
	{
		this.strTransId=strTransId;
	}


	/** Getter for property AcqId.
	 * @return Value of property AcqId.
	 *
	 */
	public java.lang.String getIssuerId() {
		return strIssuerId;
	}

	/** Setter for property AcqId.
	 * @param AcqId New value of property AcqId.
	 *
	 */
	public void setIssuerId(java.lang.String strIssuerId) {
		this.strIssuerId = strIssuerId;
	}


   /**
   	* Access method for the strMerchantId property.
   	* @return the current value of the strMerchantId property
    */

	public String getMerchantId()
	{
		return 	strMerchantId;
	}

   /**
	* Sets the value of the strMerchantId property.
	* @param strMerchantId the new value of the strMerchantId property
	*/

	public void setMerchantId(String strMerchantId)
	{
		this.strMerchantId=strMerchantId;
	}

/**
   	* Access method for the strTerminalId property.
   	* @return the current value of the strTerminalId property
    */

	public String getTerminalId()
	{
		return 	strTerminalId;
	}

   /**
	* Sets the value of the strTerminalId property.
	* @param strTerminalId the new value of the strMerchantId property
	*/

	public void setTerminalId(String strTerminalId)
	{
		this.strTerminalId=strTerminalId;
	}


   /**
    * Access method for the strSessionId property.
    * @return the current value of the strSessionId property
    */
	public String getSessionId()
	{
		return 	strSessionId;
	}

   /**
	* Sets the value of the strSessionId property.
	* @param strSessionId the new value of the strSessionId property
	*/

	public void setSessionId(String strSessionId)
	{
		this.strSessionId=strSessionId;
	}


   /**
    * Access method for the lngTimeStamp property.
    * @return the current value of the lngTimeStamp property
    */

	public Long getTimeStamp()
	{
		return lngTimeStamp;
	}
	/**
	* Sets the value of the lngTimeStamp property.
	* @param lngTimeStamp the new value of the lngTimeStamp property
	*/

	public void setTimeStamp(Long lngTimeStamp)
	{
		this.lngTimeStamp=lngTimeStamp;
	}

	/**
	* Access method for the dteTimeStamp property.
	* @return the current value of the dteTimeStamp property
	*/
	public Date getDateTimeStamp()
	{
		return this.dteTimeStamp;
	}

   /**
	* Sets the value of the dteTimeStamp property.
	* @param dteTimeStamp the new value of the dteTimeStamp property
	*/
	public void setDateTimeStamp(Date dteTimeStamp)
	{
		this.dteTimeStamp=dteTimeStamp;
	}




 }