package com.transinfo.tplus.messaging.validator;

import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;

public class BlackListCardValidator implements BaseValidator {
	public BlackListCardValidator() {
	}

	public boolean process(IParser objISO) throws OnlineException {
		
		System.out.println(" In BlackListCardValidator process...");

		String cardNumber = objISO.getCardNumber();

		try {

			TransactionDB objTranxDB = new TransactionDB();

			boolean isBlackList = objTranxDB.isBlaclListCard(new Long(objISO.getCardNumber()).longValue());
			if (isBlackList) {
				System.out.println("This Card is Black Listed ");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Black Listed Card : " + cardNumber);
				
				String rc = "34";
				if("CU".equals(objISO.getCardProduct())){
					rc = "62";
				}
				
				throw new OnlineException(rc, "B00001", "Black Listed Card Number: " + cardNumber);
			}

			if (DebugWriter.boolDebugEnabled)
				DebugWriter.write(">>> BlackListCardValidator is Successful.....");

		} catch (OnlineException e) {
			throw new OnlineException(e);
		} catch (Exception e) {
			throw new OnlineException("96", "G0001", "System Error");
		}
		return true;
	}

}