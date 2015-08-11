package com.transinfo.tplus.messaging.validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.TPlusUtility;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.CardInfo;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.util.DateUtil;

public class CardValidator implements BaseValidator
{
	public CardValidator()
	{
	}

	public boolean process(IParser objISO) throws OnlineException
	{
		System.out.println(" In CardValidator process...");

		String cardNumber = objISO.getCardNumber();

		try
		{

			System.out.println("objCardInfo");
			TransactionDB objTranxDB = new TransactionDB();

			CardInfo objCardInfo = objTranxDB.getCardInfo(new Long(objISO.getCardNumber()).longValue());

			if(objCardInfo == null)
			{
				System.out.println("This Card Not Exist ");
				throw new OnlineException("14","A06371","Invalid Card Number: " + cardNumber);
			}

			objISO.setCardDataBean(objCardInfo);

			String cardScheme = objCardInfo.getCardScheme();

			// assign the card scheme
			objISO.setCardProduct(cardScheme);

			String cardExpiredDate = objISO.getExpiryDate();
			System.out.println(cardExpiredDate+"   "+objCardInfo.getExpDate());

			/*if(!objCardInfo.getExpDate().equals(cardExpiredDate))
			{
				System.out.println("Invalid Card Expiry  ");
				throw new OnlineException("14","A06371","Invalid Card Expiry Date: " + cardNumber);
			}*/

			if(cardExpiredDate != null) {

				SimpleDateFormat df = new SimpleDateFormat("yyMM");
				Date now = new Date(System.currentTimeMillis());
				String dateTime = df.format(now);

				System.out.println(Integer.parseInt(cardExpiredDate)+"   "+Integer.parseInt(dateTime));

				if(Integer.parseInt(cardExpiredDate) < Integer.parseInt(dateTime)){
					throw new OnlineException("54", "022734","Card number "+cardNumber+" has expired");
				}

			}

			System.out.println("1");

			System.out.println("objISO.getMTI() :: " + objISO.getMTI() + " objISO.getValue(3) :: " + objISO.getValue(3));
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("objISO.getMTI() :: " + objISO.getMTI() + " objISO.getValue(3) :: " + objISO.getValue(3));

			// if request reversal return here
			if(objISO.getMTI().equals("0400") || objISO.getMTI().equals("0420") || objISO.getMTI().equals("0421") || objISO.getMTI().equals("0401"))
			{
				return true;
			}

			// ACQ country code validate and assign since JCB will not support F19
			String acqCountryCode = objISO.getValue(19);

			// JCB F19 is NOT Support. So getting from F61
			if(objISO.getValue(19) == null && ("JC".equals(objISO.getCardProduct()))){

				if(objISO.getValue(61) != null){

					acqCountryCode = objISO.getValue(61).substring(3,6);

					// JCB ACQ country code assign
					objISO.getTransactionDataBean().setAcqCountryCode(acqCountryCode);

				}
			}

			if("CU".equals(cardScheme)){

				String expDateDb = objCardInfo.getExpDate().substring(2,4)+objCardInfo.getExpDate().substring(0,2);

				String f14ExpDate = objISO.getValue(14);
				System.out.println("f14ExpDate :: " + f14ExpDate);

				if(f14ExpDate != null && !"".equals(f14ExpDate)){

					// e com validation
					String f60 = objISO.getValue(60);
					System.out.println("f60 :: " + f60);

					if(f60 != null && !"".equals(f60)){

						String eci = f60.substring(12,14);
						System.out.println("eci :: " + eci);

						if("10".equals(eci)){
							if("0000".equals(f14ExpDate)){
								throw new OnlineException("54", "022734","eCom Tranx No Proper F14");
							}
						}
					}

					if(!"0000".equals(f14ExpDate)){

						if(!expDateDb.equals(f14ExpDate)){
							throw new OnlineException("54", "022734","Card number "+cardNumber+" wrong exp date on F14");
						}

					}

				}else{

					System.out.println("objISO.getMTI() :: " + objISO.getMTI() + " objISO.getValue(3) :: " + objISO.getValue(3));
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("objISO.getMTI() :: " + objISO.getMTI() + " objISO.getValue(3) :: " + objISO.getValue(3));

					if("0800".equals(objISO.getMTI()) || "0420".equals(objISO.getMTI()) || "20".equals(objISO.getValue(3).substring(0,2))){
						// void or reversal
						System.out.println("VOID / REVERSAL / PIN Change");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("VOID / REVERSAL / PIN Change");

					}else{

						String f60 = objISO.getValue(60);
						if(f60 != null && !"".equals(f60)){

							String eci = f60.substring(12,14);
							if("10".equals(eci)){
								throw new OnlineException("40", "022734","eCom Tranx No F14");

							}
						}

					}

				}

			}

			System.out.println("2"+objCardInfo.getStatus());

			// commented here since created new validator card status

			/*if(!objCardInfo.getStatus().equals("A"))
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
			 */

			// end here

			System.out.println("2"+objCardInfo.isPinBlocked());

			if(objCardInfo.isPinBlocked().equals("N") && objISO.hasField(52))
			{

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardValidator :: objISO.getValue(3).substring(0,2) :: " + objISO.getValue(3).substring(0,2));
				// if PIN change request on next day NOT accept
				if("91".equals(objISO.getValue(3).substring(0,2))){
					System.out.println("This Card PIN is blocked :: PIN change request");
					throw new OnlineException("75","A06376","This Card PIN is blocked " + cardNumber);
				}

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

			}

			// e com validation
			if("CU".equals(cardScheme)){

				String f60 = objISO.getValue(60);
				if(f60 != null && !"".equals(f60)){

					String eci = f60.substring(12,14);
					if("10".equals(eci)){

						System.out.println("eCom Tranx");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardValidator :: eCom Tranx :: " + objISO.getValue(25));
						objISO.setEComTranx(true);

						String f14ExpDate = objISO.getValue(14);
						if(f14ExpDate == null || "".equals(f14ExpDate)){
							objISO.setValue(14, "0000");
							throw new OnlineException("40", "022734","eCom Tranx No F14");
						}

					}else{
						System.out.println("NOT eCom Tranx");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardValidator :: NOT eCom Tranx :: " + objISO.getValue(25));
						objISO.setEComTranx(false);
					}

				}

			}else if("JC".equals(cardScheme)){

				String f22eci = objISO.getValue(22).substring(0, 2);
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardValidator :: eCom Tranx :: " + f22eci);

				if("81".equals(f22eci)){
					System.out.println("eCom Tranx");
					objISO.setEComTranx(true);

					// data validations
					String f14ExpDate = objISO.getValue(14);
					if(f14ExpDate == null || "".equals(f14ExpDate)){
						throw new OnlineException("05", "022734","eCom Tranx No F14");
					}

					String f48AD = objISO.getValue(48);
					System.out.println("f48AD :: " + f48AD);

					/*if(f48AD == null || "".equals(f48AD)){
						throw new OnlineException("05", "022734","F48 must to JCB EC");
					}*/

					// check AVS available
					if(objISO.getValue(48) != null){

						String AVS = TPlusUtility.parseJCBF48New(objISO.getValue(48)).get("03");

						if(AVS != null && !"".equals(AVS)){

							String strF44 = objISO.getStrJcbF44();

							if(strF44.length() < 1){
								strF44 = " " + "I";
							}else{
								strF44 = strF44.substring(0, 1)+"I";
							}

							// assign back to field
							objISO.setStrJcbF44(strF44);

						}

					}

					/*
					String tag3Id = "F0F3F1F4";

					if(objISO.getValue(48).contains(tag3Id)){

						String strF44 = objISO.getStrJcbF44();
						//strF44 = strF44.substring(0, 2)+"I";
						strF44 = strF44.substring(0, 1)+"I";

						// assign back to field
						objISO.setStrJcbF44(strF44);

					}*/

				}else{
					System.out.println("NOT eCom Tranx");
					objISO.setEComTranx(false);
				}

			}else{

				if(objISO.hasField(25) && "59".equals(objISO.getValue(25))){
					System.out.println("eCom Tranx");
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardValidator :: eCom Tranx :: " + objISO.getValue(25));
					objISO.setEComTranx(true);
				}else{
					System.out.println("NOT eCom Tranx");
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("CardValidator :: NOT eCom Tranx :: " + objISO.getValue(25));
					objISO.setEComTranx(false);
				}

			}

			if(objISO.isEComTranx()){

				// assign transaction code sub type
				objISO.getTransactionDataBean().setTranxCodeSubType("ECOM");

				if("N".equals(objCardInfo.geteComEnable())){
					System.out.println("eCommerce NOT enabled to this card");
					throw new OnlineException("57","A06385","This Card is NOT enabled to eCom " + cardNumber);
				}
			}

			// JCB F60 MTI 0120 & 0121 request validate
			if("JC".equals(cardScheme)){

				String f60 = objISO.getValue(60);

				if(f60 != null && !"".equals(f60)){

					String f60Asc = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(f60));

					// parse the F60  and get tags
					Map<String, String> f60Tags = TPlusUtility.parseJCBF60(f60);

					// get the tag 3
					String tag3 = f60Tags.get("03");

					if(tag3 != null && !"00".equals(tag3)){

						// get the approval code from request and assign to response
						String reqF39 = objISO.getValue(39);

						//if(reqF39 != null && !"".equals(reqF39) && !"00".equals(reqF39)){
						if(!"00".equals(reqF39)){
							System.out.println("STIP request was NOT approved by JCB");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("STIP request was NOT approved by JCB. STIP F60 " + f60Asc + ", F39 " + reqF39);
							throw new OnlineException(reqF39,"A06385","STIP request was NOT approved by JCB. STIP F60 " + f60Asc + ", F39 " + reqF39);
						}

					}else{

						// get the approval code from request and assign to response
						String reqF39 = objISO.getValue(39);

						//if(reqF39 != null && !"".equals(reqF39) && !"00".equals(reqF39)){
						if(!"00".equals(reqF39)){
							System.out.println("STIP request was NOT approved by JCB");
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("STIP request was NOT approved by JCB. STIP F60 " + f60Asc + ", F39 " + reqF39);
							throw new OnlineException(reqF39,"A06385","STIP request was NOT approved by JCB. STIP F60 " + f60Asc + ", F39 " + reqF39);
						}

					}

				}

			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write(">>> CardValidator is Successful.....");

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