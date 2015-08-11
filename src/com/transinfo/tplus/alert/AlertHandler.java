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

//Package structure.
package com.transinfo.tplus.alert;

//Java specific imports
import java.util.ArrayList;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.javabean.AlertInfo;
import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;

public class AlertHandler implements IAlertHandler
{
	private static String strActive = "A";
	/*
	* This method returns the values pertaining to the alert code.
	* @param	objAlertDataBean	instance of AlertHandlerDataBean
	* @return   AlertHandlerDataBean
	* @throws 	OEException
	*/
	public AlertInfo getUsers(AlertInfo objAlertDataBean)
		throws TPlusException
	{

		DBManager		objDBManager	= new DBManager();
		StringBuffer	sbfQuery		= new StringBuffer();
		TPlusResultSet	objrsResult		= null;
		ArrayList		arlUsers		= new ArrayList();
		ArrayList		arlInput		= new ArrayList();
		ArrayList		arlOutput		= new ArrayList();
		int				intQueryCount	= 0;

		try
		{
			//First Query to fetch the alert details
			sbfQuery.append(" SELECT alert_code, " );
			sbfQuery.append("        alert_description, ");
			sbfQuery.append("        email_subject, ");
			sbfQuery.append("        email_body, ");
			sbfQuery.append("        email_signature, ");
			sbfQuery.append("        status ");
			sbfQuery.append(" FROM   alert_master " ) ;
			sbfQuery.append(" WHERE  alert_code='" + objAlertDataBean.getAlertCode() + "' ");
			System.out.println("Alert SQL1="+sbfQuery.toString());
			arlInput.add(sbfQuery.toString());

			//Second Query to fetch the Email Id of the user
			sbfQuery = new StringBuffer() ;
			sbfQuery.append(" SELECT	a.email_id ");
			sbfQuery.append(" FROM		user_master a, alert_admin_user b ");
			sbfQuery.append(" WHERE		a.user_id = b.user_id");
			sbfQuery.append(" AND		b.ALERT_CODE = '" + objAlertDataBean.getAlertCode() + "'");
			sbfQuery.append(" ORDER BY	a.user_id ");
			System.out.println("Alert SQL2="+sbfQuery.toString());
			arlInput.add(sbfQuery.toString());


			//Execute the queries
			arlOutput=objDBManager.executeMultipleSelect(arlInput);

			//Fetch the data corresponding to the first query
			objrsResult=(TPlusResultSet)arlOutput.get(intQueryCount);
			intQueryCount++;

			while(objrsResult.next())
			{

				objAlertDataBean.setAlertName(objrsResult.getString("alert_description"));
				objAlertDataBean.setEmailBody(objrsResult.getString("email_body"));
				objAlertDataBean.setEmailSub(objrsResult.getString("email_subject"));
				objAlertDataBean.setEmailSign(objrsResult.getString("email_signature"));
				objAlertDataBean.setStatus(objrsResult.getString("status"));

			}

			//Fetch the data corresponding to the second query
			objrsResult=(TPlusResultSet)arlOutput.get(intQueryCount);
			intQueryCount++;

			arlUsers.clear() ;
			while(objrsResult.next())
			{
				arlUsers.add(objrsResult.getString("email_id"));
			}


			objAlertDataBean.setUsers(arlUsers) ;

		}
		catch (Exception expGeneral)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR, "Error: "+expGeneral.getMessage());
		}

		return objAlertDataBean;
	}


}