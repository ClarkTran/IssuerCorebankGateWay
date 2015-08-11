/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore is prohibited.
 * File Name          : BaseLog.java
 * Author             : I.T. Solutions (India) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : BaseLog with get and set methods
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 *
 */
package com.transinfo.tplus.log;

import java.util.ArrayList;

/**
 *  This is Base Class to hold all the logs. It contains an ArrayList of Objects.
 *  The ArrayList contains various log data beans.
 */

public class BaseLog implements java.io.Serializable{

	private ArrayList arlLogs =new ArrayList();
	private String strTransactionID = "";


    /** Creates a new instance of BaseLog */
    public BaseLog() {

    }

   /**
	* Access method for the arlLogs property.
	* @return the current value of the arlLogs property
	*/
	public ArrayList getLog(){
		return arlLogs;
	}

   /**
	* Sets the value of the arlLogs property.
	* @param arlLogs the new value of the arlLogs property
	*/
	public void setLog(ArrayList arlLogs){
		this.arlLogs=arlLogs;
	}

   /**
	* Adds the input object to the ArrayList.
	* @param objLog the value to be added to arlLogs
	*/
	public void addLogItem(Object objLog){
		arlLogs.add(objLog);
	}

	/**
	* Gets the Transaction ID for the Log
	*/
	public String getTransactionID()
	{
		return strTransactionID;
	}

	/**
	* Sets the Transaction ID for the Log
	*/
	public void setTransactionID(String strTransactionID)
	{
		this.strTransactionID = strTransactionID;
	}


}
