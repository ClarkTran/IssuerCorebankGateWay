/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore. is prohibited.
 * File Name          : TransactionLog.java
 * Author             : I.T. Solutions (India) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : Extends BaseLog. Creates Transaction Bean.
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 * 28-May-2003	   1.01				Rajeev				  SecureCode Changes.
 *
 */

package com.transinfo.tplus.log;

import com.transinfo.tplus.TPlusException;
/**
 * This class extends the BaseLog and has an overloaded method
 * to add the data to the Arraylist.
 * It creates a TransactionDataBean and adds to the Arraylist.
 */

public class TransactionLog extends BaseLog implements java.io.Serializable
{

	private String strTXSOSIP;



   /**
    * Creates an TransDataBean and adds this to the arrayList
    * strMTI	- It hold the MTI of the request
    * strTransID - It holds the Transaction Id.
    * strTransCode - It holds the Transaction Code.
    * strSessionID - It holds the session Id.
    * strEncrPan - It holds the enrypted PAN.
    * strStatus - It holds the status of the transaction sucess or failure
    * strErrorDesc - It holds the Error Description if an error has occured.
    * strVendorCode - It holds the vendor code.
    * strTransEncrXID - It holds the encrypted Transaction ID.
    * strStatusCode - It holds the status code.
    * strTXDetail - It holds the transaction detail.
    * strTXECI - It holds the Ecom Indicator.
    * strMerchantID - It holds the Merchant Id.
    * strTXPOSMode - It holds the POS MODE received from the ACS.
    * strTXTCC - It holds the TCC received from the ACS.
    * strTXSTAIN - It holds the STAIN received from the ACS.
    * strTXSOSIP - It holds the clients IP where the request is
    */

	public void addLogItem(String strIssuerId,String strMTI,String strTranxCode,
			String strProcessCode,String strMerchantId,String strMerchantName,
			String strTerminalId,String strCardNo,String strExpDate,String strAmount,
			String strCurrCode,String strOrgAmt,String strOrgCurr,String strTraceNo,
			String strMCC,String strTCC,String strPOSEntryMode,String strTrack2Data,
			String strRefNo,String strResponseCode,String strApprovalCode,String strPinData,
			String strTraceNo2,String strDeleted)throws TPlusException
 		{

			/*TransactionDataBean objTransactionbean = new TransactionDataBean
		    (strIssuerId,strMTI,strTranxCode,strProcessCode,strMerchantId,strMerchantName,strTerminalId,
		    strCardNo,strExpDate,strAmount,strCurrCode,strOrgAmt,strOrgCurr,strTraceNo,
			strMCC,strTCC,strPOSEntryMode,strTrack2Data,strRefNo,strResponseCode,
			strApprovalCode,strPinData,strTraceNo2,strDeleted);*/

			//super.addLogItem (objTransactionbean);

		}


   /**
	* Access method for the strTXSOSIP property.
	* @return the current value of the strTXSOSIP property
	*/
	public String getTXSOSIP()
	{
		return strTXSOSIP;
	}

   /**
	* Sets the value of the strTXSOSIP property.
	* @param strTXSOSIP the new value of the strTXSOSIP property
	*/
	public void setTXSOSIP(String strTXSOSIP)
	{
		this.strTXSOSIP=strTXSOSIP;
	}

 }
