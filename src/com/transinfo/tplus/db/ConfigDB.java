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
package com.transinfo.tplus.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.javabean.MessageHandler;
import com.transinfo.tplus.javabean.RiskControlRule;
import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;

/**
 *
 * @author  Owner
 */


public class ConfigDB {

    /** Creates a new instance of TransactionDB */
    public ConfigDB() {
    }


/**
	* This method is used to get TPlus Message Config Info
	* @param none
	* @returns MessageHandler
	* @throws TPlusException
 	*/

	public static ArrayList getMessageConfig()
		throws TPlusException
	{

		String strValue ="";
		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute = false;
		ArrayList messageList=new ArrayList();

		try
		{

				StringBuffer sbfDTDVal = new StringBuffer();
				sbfDTDVal.append("SELECT PORTNO,MESSAGE_TYPE,MESSAGE_HANDLER,MESSAGE_PROCESSOR  FROM MESSAGE_CONFIG ");
				System.out.println(" SQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

				if (bolExecute)
					objRs = objDBMan.getResultSet();
				else
					throw new TPlusException(TPlusCodes.NO_DATA_AVAILABLE,
						"No Message Config Data Found. ");
				while (objRs.next())
				{
					MessageHandler objMsgHandler = new MessageHandler();
					objMsgHandler.setPort(objRs.getString("PORTNO"));
					objMsgHandler.setMessageType(objRs.getString("MESSAGE_TYPE"));
					objMsgHandler.setMessageHandler(objRs.getString("MESSAGE_HANDLER"));
					objMsgHandler.setMessageProcessor(objRs.getString("MESSAGE_PROCESSOR"));
					messageList.add(objMsgHandler);


				}
		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR," Error while retreiving the Message Config info"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return messageList;
   }


/**
	* This method is used to get TPlus Message Config Info
	* @param none
	* @returns MessageHandler
	* @throws TPlusException
 	*/

	public static MessageHandler getMessageConfig(String msgType)
		throws TPlusException
	{

		String strValue ="";
		DBManager objDBMan = new DBManager();
		TPlusResultSet objRs = null;
		boolean bolExecute = false;
		MessageHandler objMsgHandler = new MessageHandler();

		try
		{

				StringBuffer sbfDTDVal = new StringBuffer();
				sbfDTDVal.append("SELECT PORTNO,MESSAGE_TYPE,MESSAGE_HANDLER,MESSAGE_PROCESSOR  FROM MESSAGE_CONFIG ");
				sbfDTDVal.append(" WHERE MESSAGE_TYPE='"+msgType+"'");
				System.out.println(" SQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());

				if (bolExecute)
					objRs = objDBMan.getResultSet();
				else
					throw new TPlusException(TPlusCodes.NO_DATA_AVAILABLE,
						"No Message Config Data Found. ");
				while (objRs.next())
				{

					objMsgHandler.setPort(objRs.getString("PORTNO"));
					objMsgHandler.setMessageType(objRs.getString("MESSAGE_TYPE"));
					objMsgHandler.setMessageHandler(objRs.getString("MESSAGE_HANDLER"));
					objMsgHandler.setMessageProcessor(objRs.getString("MESSAGE_PROCESSOR"));



				}
		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR," Error while retreiving the Message Config info"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objMsgHandler;
   }

	public static Calendar getTPlusCalender()
	{
		String strInput = "";
		Calendar valCal= Calendar.getInstance();
		System.out.println("Config sys date "+valCal);
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat (TPlusConfig.REQUEST_DATE_FORMAT);
			//Query to fetch SYSDATE
			StringBuffer sbfDate = new StringBuffer();
			sbfDate.append("SELECT TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') SYSTEM_DATE FROM DUAL ");


			DBManager objDBManager = new DBManager();
			boolean boolResult = objDBManager.executeSQL(sbfDate.toString());
			TPlusResultSet objRs = objDBManager.getResultSet();


			while (objRs.next())
			{
				strInput= objRs.getString("SYSTEM_DATE");
			}


			if (strInput.length() > 14)
			{
				return Calendar.getInstance();
			}

			int intYear = Integer.parseInt(strInput.substring(0,4));
			int intMonth = Integer.parseInt(strInput.substring(4,6));
			intMonth = intMonth - 1;
			int intDay = Integer.parseInt(strInput.substring(6,8));
			int intHour = Integer.parseInt(strInput.substring(8,10));
			int intMin = Integer.parseInt(strInput.substring(10,12));
			int intSec = Integer.parseInt(strInput.substring(12,14));
			//Calendar valCal = Calendar.getInstance();
			valCal.clear();
			valCal.set(intYear,intMonth,intDay,intHour,intMin,intSec);


			if (intYear != valCal.get(Calendar.YEAR) ||
				intMonth != valCal.get(Calendar.MONTH) ||
					intDay != valCal.get(Calendar.DAY_OF_MONTH) ||
						intHour != valCal.get(Calendar.HOUR_OF_DAY) ||
							intMin != valCal.get(Calendar.MINUTE) ||
									intSec != valCal.get(Calendar.SECOND))
			{
				return Calendar.getInstance();
			}


		}
		catch (TPlusException mpiexp)
		{
			//throw mpiexp;
		}
		catch (Exception e)
		{
			//throw new TPlusException(TPlusCodes.APPLICATION_ERROR,e.getMessage());
		}
		finally
		{
		}
		return valCal;
	}

	/**
	 * This method is used to get the date.
	 * @param none
	 * @returns Date
	 * @throws TPlusException
	 */


	public static Date getTPlusDate()
	{
		String strInput = "";
		Date sysDate = new Date();
		System.out.println("Config sys date "+sysDate);
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat (TPlusConfig.REQUEST_DATE_FORMAT);
			//Query to fetch SYSDATE
			StringBuffer sbfDate = new StringBuffer();
			sbfDate.append("SELECT TO_CHAR(SYSDATE,'YYYYMMDDHH24MISS') SYSTEM_DATE FROM DUAL ");


			DBManager objDBManager = new DBManager();
			boolean boolResult = objDBManager.executeSQL(sbfDate.toString());
			TPlusResultSet objRs = objDBManager.getResultSet();


			while (objRs.next())
			{
				strInput= objRs.getString("SYSTEM_DATE");
			}


			if (strInput.length() > 14)
			{
				return sysDate;
			}

			int intYear = Integer.parseInt(strInput.substring(0,4));
			int intMonth = Integer.parseInt(strInput.substring(4,6));
			intMonth = intMonth - 1;
			int intDay = Integer.parseInt(strInput.substring(6,8));
			int intHour = Integer.parseInt(strInput.substring(8,10));
			int intMin = Integer.parseInt(strInput.substring(10,12));
			int intSec = Integer.parseInt(strInput.substring(12,14));
			Calendar valCal = Calendar.getInstance();
			valCal.clear();
			valCal.set(intYear,intMonth,intDay,intHour,intMin,intSec);


			if (intYear != valCal.get(Calendar.YEAR) ||
				intMonth != valCal.get(Calendar.MONTH) ||
					intDay != valCal.get(Calendar.DAY_OF_MONTH) ||
						intHour != valCal.get(Calendar.HOUR_OF_DAY) ||
							intMin != valCal.get(Calendar.MINUTE) ||
									intSec != valCal.get(Calendar.SECOND))
			{
				return sysDate;
			}

			sysDate = valCal.getTime();

		}
		catch (TPlusException mpiexp)
		{
			//throw mpiexp;
		}
		catch (Exception e)
		{
			//throw new TPlusException(TPlusCodes.APPLICATION_ERROR,e.getMessage());
		}
		finally
		{
		}
		return sysDate;
	}


/**
 * This method is used to get TPlus RiskControlRules Info
 * @param none
 * @returns RiskControlRule
 * @throws TPlusException
 */

	public static ArrayList getTPlusRiskRulesInfo()
		throws TPlusException
	{

		String strValue ="";
		DBManager objDBMan = new DBManager();
		ArrayList objArryRisk = new ArrayList();
		TPlusResultSet objRs = null;
		boolean bolExecute = false;

		try
		{
				StringBuffer sbfDTDVal = new StringBuffer();
				sbfDTDVal.append("SELECT * FROM RISKCONTROLRULES");

				System.out.println(" SQL="+ sbfDTDVal.toString());
				bolExecute = objDBMan.executeSQL(sbfDTDVal.toString());
				objRs = objDBMan.getResultSet();

				while (objRs.next())
				{
					RiskControlRule objRiskRuleInfo = new RiskControlRule();
					String ruleId = objRs.getString("RULEID");
					objRiskRuleInfo.setType(objRs.getString("TYPE"));
					objRiskRuleInfo.setScope(objRs.getString("SCOPE"));
					objRiskRuleInfo.setMCC(objRs.getString("MCC"));
					objRiskRuleInfo.setMerchantRefno(objRs.getString("MERCHANT_REFNO"));
					objRiskRuleInfo.setCurrCode(objRs.getString("CURRCODE"));
					objRiskRuleInfo.setResponse(objRs.getString("RESPONSE"));

					if(!TPlusConfig.isNumeric(objRs.getString("LASTRECENTDAYS")))
				    throw new TPlusException(TPlusCodes.INVALID_PARAM,"LAST Recent Days value should be numeric value for rule ("+ruleId+")");
					else
				    objRiskRuleInfo.setLastRecentDays(new Integer(objRs.getString("LASTRECENTDAYS")).intValue());

					if(!TPlusConfig.isNumeric(objRs.getString("COUNT")))
				    throw new TPlusException(TPlusCodes.INVALID_PARAM,"RiskControl Count value should be numeric value for rule("+ruleId+")");
					else
				    objRiskRuleInfo.setCount(new Integer(objRs.getString("COUNT")).intValue());

				    if(!TPlusConfig.isDouble(objRs.getString("AMOUNT")))
					throw new TPlusException(TPlusCodes.INVALID_PARAM,"RiskControl Amount value should be numeric value for rule("+ruleId+")");
					else
				    objRiskRuleInfo.setAmount(new Double(objRs.getString("AMOUNT")).doubleValue());

					objArryRisk.add(objRiskRuleInfo);

				}
		}
		catch (TPlusException tplusexp)
		{
			throw tplusexp;
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,"Retrieving the Tag info"+e.getMessage());
		}
		finally
		{
		} // End of the Main Try Block

		return objArryRisk;
   }



}
