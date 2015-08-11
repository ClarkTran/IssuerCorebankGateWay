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

package com.transinfo.tplus.messaging;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.messaging.parser.IParser;

public abstract class IRequestProcessor
{

protected static final String PACKAGE = "com.transinfo.tplus.messaging";

public abstract IParser processRequest(IParser objISO)throws TPlusException;

public static Object getIssuerInstance(String strCardNumber) throws TPlusException
{
		Object objIssuer = null;
		String strIssuer = null;

		try
		{

			if(strCardNumber != null)
			{
				TransactionDB objTranx = new TransactionDB();
				//strIssuer = objTranx.getIssuer(strCardNumber);

				if (strIssuer ==null)
				{
					throw new TPlusException("1001", "No Issuer Available for the Cardno "+strCardNumber);
				}
			}
			else
			{

				strIssuer = "common";
			}

			if(strIssuer !=null)
			{

				Class classObj =  Class.forName(PACKAGE+"."+strIssuer);
				objIssuer = classObj.newInstance();
			}
			else
			{

				throw new TPlusException("1001", "Issuer could not create as Issuer is NULL");

			}
		}
		catch (Exception exp)
		{

			throw new TPlusException(TPlusCodes.APPL_ERR, TPlusCodes.getErrorDesc(TPlusCodes.APPL_ERR) + " Error while trying to instantiate Issuer Class: Error:"+exp.getMessage());
		}

		return objIssuer;


	}




}