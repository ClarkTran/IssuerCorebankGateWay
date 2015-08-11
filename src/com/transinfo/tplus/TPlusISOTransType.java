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

package com.transinfo.tplus;



public class TPlusISOTransType extends Object
{

	public static final String SALE	 	= "SALE";
	public static final String CASH		= "CASH";
	public static final String VOID		= "VOID";
	public static final String VERIFY 	= "VERIFY";
	public static final String OSALE  	= "OSALE";
	public static final String OCASH  	= "OCASH";
	public static final String UCASH  	= "UCASH";
	public static final String ADJUST 	= "ADJUST";
	public static final String USALE  	= "USALE";
	public static final String SETTLE 	= "SETTLE";
	public static final String TEST		= "TEST";
	public static final String QUASI_CASH= "QUASI_CASH";
	public static final String REFUND 	 = "REFUND";
	public static final String REVERSAL  = "REVERSAL";
	public static final String PIN		 = "PIN";
	public static final String ADVICE 	 = "ADVICE";
	public static final String RADVICE   = "RADVICE";
	public static final String MANUAL 	 = "MANUAL";
	public static final String AUTH		 = "AUTH";
	public static final String VSALE     = "VSALE";
	public static final String VCASH     = "VCASH";
	public static final String TRANSFER  = "TRANSFER";
	public static final String BALANCE   = "BALANCE";
	public static final String LOGON     = "LOGON";
	public static final String LOGOFF    = "LOGOFF";
	public static final String LOADKEY 	 = "LOAD KEY";
	public static final String UNKNOWN   = "UNKNOWN";

	public static String getTranxType(String MTI, String ProcessingCode)
	{
		if (MTI.equals("0320") || MTI.equals("0330"))
			return "USALE";

		String type = MTI + ProcessingCode.substring(0, 2);

		if (type.equals("010031") || type.equals("011031"))
			return "BALANCE";
		if (type.equals("020003") || type.equals("021003"))
			return "TRANSFER";
		if (type.equals("020094") || type.equals("021094"))
			return "PIN";
		if (type.equals("010000") || type.equals("011000"))
			return "VERIFY";
		if (type.equals("010001") || type.equals("011001"))
			return "VERIFY";
		if (type.equals("010038") || type.equals("011038"))
			return "VERIFY";
		if (type.equals("020000") || type.equals("021000"))
			return "SALE";
		if (type.equals("020001") || type.equals("021001"))
			return "CASH";
		if (type.equals("020002") || type.equals("021002"))
			return "VOID";
		if (type.equals("020004") || type.equals("021004"))
			return "DREFUND";
		if (type.equals("020020") || type.equals("021020"))
			return "REFUND";
		if (type.equals("022000") || type.equals("023000"))
			return "OSALE";
		if (type.equals("022001") || type.equals("023001"))
			return "OCASH";
		if (type.equals("022002") || type.equals("023002"))
			return "ADJUST";
		if (type.equals("032000") || type.equals("033000"))
			return "USALE";
		if (type.equals("032001") || type.equals("033001"))
			return "UCASH";
		if (type.equals("040000") || type.equals("041000") ||
				type.equals("040001") || type.equals("041001") ||
				type.equals("040002") || type.equals("041002") ||
				type.equals("040038") || type.equals("041038"))
			return "REVERSAL";
		if (type.equals("050092") || type.equals("051092") ||
				type.equals("050096") || type.equals("051096"))
			return "SETTLE";
		if (type.equals("080090") || type.equals("081090"))
			return "LOAD KEY";
		if (type.equals("080099") || type.equals("081099"))
			return "TEST";
		return "UNKNOWN";



	}

	public static String TranxType2(String POSEntryMode, String POSConditionCode)
	{
		String Tranx;

		Tranx = POSEntryMode.substring(0, 2);

		if (Tranx == "01")
		{
			return MANUAL;
		}

		if (POSConditionCode == "06")
		{
			return AUTH;
		}

		return "";
	}

}

