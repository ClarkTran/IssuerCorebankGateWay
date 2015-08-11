package com.transinfo.tplus.messaging.validator;

import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;

public class BlackListMerchantValidator implements BaseValidator {
	
	public BlackListMerchantValidator() {
	}

	public boolean process(IParser objISO) throws OnlineException {

		System.out.println(" In BlackListMerchantIdValidator process...");

		String merchantId = objISO.getValue(42);

		try {

			TransactionDB objTranxDB = new TransactionDB();

			boolean isBlackList = objTranxDB.isBlaclListMerchant(merchantId);
			
			if (isBlackList) {
				System.out.println("This Merchant Id is Black Listed ");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Black Listed Merchant Id: " + merchantId);

				String rc = "59";
				if("CU".equals(objISO.getCardProduct())){
					rc = "62";
				}
								
				throw new OnlineException(rc, "B00001", "Black Listed Merchant Id: " + merchantId);
			}

			if (DebugWriter.boolDebugEnabled)
				DebugWriter.write(">>> BlackListMerchantIdValidator is Successful.....");

		} catch (OnlineException e) {
			throw new OnlineException(e);
		} catch (Exception e) {
			throw new OnlineException("96", "G0001", "System Error");
		}
		
		return true;
	}

}