package com.transinfo.tplus.messaging.validator;

import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.CardInfo;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;

public class CardStatusValidator implements BaseValidator
{
	public CardStatusValidator()
	{
	}

	public boolean process(IParser objISO) throws OnlineException
	{
		System.out.println(" In CardStatusValidator process...");

		String cardNumber = objISO.getCardNumber();

		try
		{

			TransactionDB objTranxDB = new TransactionDB();

			System.out.println("objCardInfo");
			CardInfo objCardInfo = objISO.getCardDataBean();

			if(!objCardInfo.getStatus().equals("A"))
			{
				System.out.println("This Card is Inactive ");
				throw new OnlineException("16","A06372","This Card Is InActive: " + cardNumber);
			}

			System.out.println("2"+objCardInfo.getCardstatusId());

			if(objCardInfo.getCardstatusId()!=null && Integer.parseInt(objCardInfo.getCardstatusId())>0)
			{
				if(objISO.getValue(3).substring(0,2).equals("95"))
				{
					System.out.println("Card Activation");

				}
				else
				{
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("RequestBaseHandler:Error Card not in Active state "+objISO.getCardNumber());
					System.out.println("Error Card not in Active state "+objISO.getCardNumber());
					String strResCode = objTranxDB.getCardResponseCode(objCardInfo.getCardstatusId());
					throw new OnlineException(strResCode,"A06372","This Card Is InActive: " + cardNumber);
				}
			}

			/*System.out.println("2"+objCardInfo.isPinBlocked());

			if(objCardInfo.isPinBlocked().equals("N") && objISO.hasField(52))
			{
				// check when it is blocked
				String pinBlockDate = objCardInfo.getPinBlockDate();
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardValidator :: pinBlockDate :: " + pinBlockDate);

				if(pinBlockDate != null && !"".equals(pinBlockDate)){

					long daysDiff = DateUtil.daysBetween(pinBlockDate, DateUtil.getTodayDate(), "dd/MM/yyyy");

					if(daysDiff == 0){
						System.out.println("This Card PIN is blocked ");
						throw new OnlineException("75","A06376","This Card PIN is blocked " + cardNumber);
					}

				}

			}*/

			if (DebugWriter.boolDebugEnabled) DebugWriter.write(">>> CardStatusValidator is Successful.....");

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