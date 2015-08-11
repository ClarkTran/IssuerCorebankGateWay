package com.transinfo.tplus.messaging.validator;


import java.util.Map;

import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;

public class SignOnValidator implements BaseValidator
{
	public SignOnValidator()
	{
	}

	public boolean process(IParser objISO) throws OnlineException
	{
		System.out.println(" In SignOnValidator process...");

		String cardNumber = objISO.getCardNumber();

		try
		{
			String tranxType = objISO.getTransactionDataBean().getAcqBinType();

			if("OFFUS".equals(tranxType)){

				String scheme = "VISA";

				String cardScheme = objISO.getCardDataBean().getCardScheme();
				if("CU".equals(cardScheme)){
					scheme = "CUP";
				}else if("JC".equals(cardScheme)){
					scheme = "JCB";
				}else{
					scheme = "VISA";
				}

				Map connlist = TPlusConfig.connectionMap;
				IParser parser = (IParser)connlist.get(scheme);

				if("N".equals(parser.getSignOnNeeded())){
					System.out.println("Connection Sign Offed");
					throw new OnlineException("91","A06371","Connection Sign Offed :: " + cardNumber);
				}

			}else{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("ONUS transaction no SIGNON validation");
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write(">>> SignOnValidator is Successful.....");

		}catch (OnlineException e){
			e.printStackTrace();
			throw new OnlineException(e);
		}catch (Exception e){
			e.printStackTrace();
			throw new OnlineException("96","G0001","System Error");
		}

		return true;
	}

}