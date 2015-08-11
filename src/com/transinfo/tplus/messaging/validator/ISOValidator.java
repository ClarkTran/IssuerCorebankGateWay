
package com.transinfo.tplus.messaging.validator;


import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;

public class ISOValidator
{


	public boolean validateCard(IParser objISO)throws TPlusException
	{

		String strCardNo =  objISO.getCardNumber();
		if( strCardNo== null || strCardNo.equals(""))
			return false;
		//objISO.setValue(39,"14");
		//throw new TPlusException(TPlusCodes.INVALID_CARDNUMBER,TPlusCodes.getErrorDesc(TPlusCodes.INVALID_CARDNUMBER)+". Error:Invalid Card No from Terminal:"+objISO.getValue(TPlusISOCode.TERMINAL_ID));
		return true;


	}

	public boolean validateExpiry(IParser objISO )throws TPlusException,Exception
	{

		String strExpDate = objISO.getExpiryDate();
		if(strExpDate == null || strExpDate.equals(""))
			return false;
		//return objISO.setValue(39,"14");
		//throw new TPlusException(TPlusCodes.INVALID_CARDNUMBER,TPlusCodes.getErrorDesc(TPlusCodes.INVALID_CARDNUMBER)+". Error:Invalid Card Expiry Date from Terminal:"+objISO.getValue(TPlusISOCode.TERMINAL_ID));
		return true;


	}


	public boolean validate(IParser objISO )throws TPlusException
	{
		String strSYSTraceNo =	objISO.getValue(TPlusISOCode.SYSTEM_TRACE_NO);
		if( strSYSTraceNo == null || strSYSTraceNo.equals(""))
			return false;
		//return objISO.setValue(39,"05");
		//throw new TPlusException(TPlusCodes.DO_NOT_HONOUR,TPlusCodes.getErrorDesc(TPlusCodes.DO_NOT_HONOUR)+". Error:System Trace No is null or invalid from terminal:"+objISO.getValue(TPlusISOCode.TERMINAL_ID));
		return true;


	}

	/*
public boolean validateMerchantTerminal(IParser objISO )throws TPlusException,Exception
{
	String Header = objISO.getHeader();
	TransactionDB objTranxDB = new TransactionDB();
	if(Header.substring(0,2).equals("80")) // VOICE Request
	{
		System.out.println("5");

		System.out.println("Merchant ID="+objISO.getValue(TPlusISOCode.MERCHANT_ID));
		MerchTermInfo objMerchTermInfo = objTranxDB.getMerchTermInfo("",objISO.getValue(TPlusISOCode.MERCHANT_ID));
		if(objMerchTermInfo == null)
		{
			//AlertManager.RaiseAlert(TPlusCodes.ALR_UNAUTH_ACS,"Not a valid merchant "+objISO.getValue(TPlusISOCode.MERCHANT_ID));
			//throw new TPlusException(TPlusCodes.INVALID_MERCHANT,TPlusCodes.getErrorDesc(TPlusCodes.INVALID_MERCHANT)+". Error:Invalid MerchantID from Terminal:"+objISO.getValue(TPlusISOCode.TERMINAL_ID));
			return false;
			//return objISO.setValue(39,"03");
		}
	}
	else // ATM and POS Request
	{
		System.out.println("6666");
		MerchTermInfo objMerchTermInfo = objTranxDB.getMerchTermInfo(objISO.getValue(TPlusISOCode.TERMINAL_ID),"");

		if(objMerchTermInfo == null)
		{
			System.out.println("INVALID TERMINAL");
			return false;
			//AlertManager.RaiseAlert(TPlusCodes.ALR_UNAUTH_ACS,"Not a valid Terminal ID "+objISO.getValue(TPlusISOCode.TERMINAL_ID));
			//throw new TPlusException(TPlusCodes.INVALID_MERCHANT,TPlusCodes.getErrorDesc(TPlusCodes.INVALID_MERCHANT)+". Error:Invalid TerminalID");
			//return objISO.setValue(39,"03");
		}


		if ((objISO.getValue(TPlusISOCode.MERCHANT_ID) ==null ||objISO.getValue(TPlusISOCode.MERCHANT_ID).equals("")) && (!objISO.getValue(TPlusISOCode.MERCHANT_ID).equals(objMerchTermInfo.getMerchantID())))
		{
			System.out.println("Invalid MerchantID from terminal");
			//AlertManager.RaiseAlert(TPlusCodes.ALR_UNAUTH_ACS,"Not a valid Terminal ID "+objISO.getValue(TPlusISOCode.TERMINAL_ID));
			//throw new TPlusException(TPlusCodes.INVALID_MERCHANT,TPlusCodes.getErrorDesc(TPlusCodes.INVALID_MERCHANT)+". Error:Invalid MerchantID from terminal"+objISO.getValue(TPlusISOCode.TERMINAL_ID));
			//return objISO.setValue(39,"03");
			return false;

		}


	}
	return true;
}*/


}