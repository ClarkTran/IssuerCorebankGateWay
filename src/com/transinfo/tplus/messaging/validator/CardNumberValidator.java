package com.transinfo.tplus.messaging.validator;


import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.OnlineUtil;
import com.transinfo.tplus.messaging.parser.IParser;


public class CardNumberValidator implements BaseValidator {
	public CardNumberValidator() {
	}

	public boolean process(IParser objISO) throws OnlineException {

		System.out.println("In CardNumberValidator="+objISO.getCardNumber());
		try{

			if ((objISO.getCardNumber()==null)||(objISO.getCardNumber().equals("")))
			{
				throw new OnlineException("14","A06741","CardNumber not Available");
			}

			/*if ((objISO.getExpiryDate()==null)||(objISO.getExpiryDate().equals("")))
    		{
    		  throw new OnlineException("14","A06748","Invalid Expiry Date");
    		}*/
			
			// assign the field 60 value
			objISO.getF60Value();

			// assign the field 61 value
			// following validation will be done on each stage and update correct value
			//objISO.getF61Value();

			String cardNumber = objISO.getCardNumber();
			if (!OnlineUtil.checkLuhn(cardNumber))
			{
				System.out.println("In CheckLuhn..");
				throw new OnlineException("14","A06744","Card Luhn check Failed - "+cardNumber);

			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write(">>> CardNumberValidator is Successful.....");
			return true;
			
		}catch(OnlineException onlineExp)
		{
			throw onlineExp;

		}catch(Exception exp)
		{
			System.out.println("In Exception");
			throw new OnlineException("14","A06744","Exception while checking the CardNumber "+exp.getMessage());
		}
	}
}
