/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore is prohibited.
 * File Name          : PALog.java
 * Author             : I.T. Solutions (Inida) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : Extends BaseLog. Creates PALogDataBean and adds to the BaseLog arraylist
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 *
 */

package com.transinfo.tplus.log;

/**
 * This class extends the BaseLog and has an overloaded method to add the
 * PALogDataBean to the ArrayList.
 */

public class SignOnLog extends BaseLog implements java.io.Serializable
{

   /**
    * Creates an PADataBean and adds this to the arrayList
    *
    * strConnName - It holds the Connection Name.
    * strResCode - It holds the Response Code.
    * strDesc - It holds the Description.
    *
    */

	public void addLogItem(String strConnName,String strResCode,String strDesc)
 	{
		SignOnDataBean objSingOnBean = new SignOnDataBean(strConnName,strResCode,strDesc);
		super.addLogItem (objSingOnBean);
	}


 }
