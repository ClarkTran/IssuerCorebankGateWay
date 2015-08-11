package com.transinfo.tplus.messaging.validator;

import com.transinfo.tplus.javabean.TransactionDataBean;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.util.Config;

@SuppressWarnings("unused")
public class TranxValidator implements BaseValidator
{
	public TranxValidator()
	{
	}

	public boolean process(IParser objISO) throws OnlineException
	{

		try
		{

			TransactionDataBean transaction = new TransactionDataBean();

			transaction.setTerminalNo(objISO.getValue(41));
			transaction.setMerchantNo(objISO.getValue(42));

			if(objISO.getCardNumber()!=null)
			{
				transaction.setCardNo(objISO.getCardNumber());
			}else
			{
				transaction.setCardNo("");
			}

			transaction.setTransactionStatus(Config.ORIGINAL);
			transaction.setResponseCode("00");

			/*if(ISOFunctions.getPosTranxType(objISO).equals("VOID SALE") || ISOFunctions.getPosTranxType(objISO).equals("VOID CASH")){
          transaction.setSTAN(objISO.getValue(62).split("#")[0]);
      }else{
          transaction.setSTAN(objISO.getValue(11));
      }*/

			transaction.setSTAN(objISO.getValue(11));
			transaction.setRRN(objISO.getValue(37));

			/*String transactionType = ISOFunctions.getPosTranxType(objISO);

      if(transactionType.startsWith("VOID")){
          transaction.setTransRecordType(Config.VOID);
      }else if(transactionType.startsWith("REVERSAL")){
          transaction.setTransRecordType(Config.REVERSAL);
          if(ProcessCode.CONTACTLESS_PREPAID_PRE_TOPUP.equals(objISO.getValue(3))){
              transaction.setCardNo(objISO.getValue(62));
          }
      }

      if(ISOFunctions.getPosTranxType(objISO).equals("VOID SALE") || ISOFunctions.getPosTranxType(objISO).equals("VOID CASH")){
          transaction.setTransRecordType(Config.REVERSAL);
      }*/

			transaction.execute();

			/* if(!transaction.isRecordExist()){
         if(ISOFunctions.getPosTranxType(objISO).equals("VOID SALE") || ISOFunctions.getPosTranxType(objISO).equals("VOID CASH")){
            throw new OnlineException("29","733864","Original transaction not found for void");
         }else{
            throw new OnlineException("00","733864","Original transaction not found for reversal");
         }
      }

      context.setTransactionDataBean(transaction);*/

		}catch (OnlineException e)
		{
			throw new OnlineException(e);
		}catch (Exception e)
		{
			e.printStackTrace();
			throw new OnlineException("96","A99999","System error");
		}
		return true;
	}

	private String lpad (String src, int len, String padder)
	{
		if (src == null)
			src = "";
		if (len <= src.length())
		{
			return src.substring(0,len);
		}
		else
		{
			while (src.length() < len){
				src = padder + src;
			}
		}
		return src;
	}

	public static void main (String args[])
	{
		byte [] b = new byte [] {0x30,0x30,0x30,0x30,0x37,0x39};
		System.out.println(new String(b));
	}
}