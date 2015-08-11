package com.transinfo.tplus.messaging.validator;


import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.CardInfo;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;


/*import com.tivn.contactless.online.BaseProcessor;
import com.tivn.contactless.online.ISOFunctions;
import com.tivn.contactless.online.OnlineException;
import com.tivn.contactless.online.TIVNContext;

import com.tivn.contactless.online.util.Config;
import com.tivn.contactless.online.util.ProcessCode;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;*/

public class RiskValidator implements BaseValidator
{
	boolean TranxValidation =true;
  public RiskValidator()
  {
  }

  public boolean process(IParser objISO) throws OnlineException
  {
	  System.out.println(" In DataValidator process...");
	try
    {

			CardInfo objCardInfo = objISO.getCardDataBean();
			TransactionDB objTranxDB = new TransactionDB();



            System.out.println("***Checking for Cardholder Limit***");

            /*****************************************
             *	Checking for Cardholder Limit
             *****************************************/

            if(!objTranxDB.withdrawalLimitCheck(objCardInfo,objISO.getValue(TPlusISOCode.MERCHANT_TYPE),
            objISO.getTranxType(),Integer.parseInt(objISO.getAmount())/100,objISO.getValue(49),objCardInfo.getIssuerId(), true)) {
                System.out.println("*** Cardholder withdrawalLimitCheck Failed");
                if (DebugWriter.boolDebugEnabled) DebugWriter.write("WithDrawalRequest:Cardholder withdrawalLimitCheck Failed.....");
                objISO.setValue(TPlusISOCode.RESPONSE_CODE,TPlusCodes.EXEED_WITHDRAW_LIMIT);
                TranxValidation=false;
            }

            //objISO.setValue(TPlusISOCode.RESERVED_59,objCardInfo.getCustomerId());

            System.out.println("***Checking for Withdrawal Limit Rules for tranx MCC NEW **"+TranxValidation);

            /***************************************************
             *	Checking for Withdrawal Limit Rules for Tranx MCC
             **************************************************/

            //if(TranxValidation && objISO.getValue(TPlusISOCode.MERCHANT_TYPE)!=null && !objISO.getValue(TPlusISOCode.MERCHANT_TYPE).equals("")){

                System.out.println("*** CHECKING FOR MCC VALIDATION  *****");

				// No Merchant Type will be send in the Tranx so hardcoded with '0000'

                if(!objTranxDB.withdrawRulesLimitCheck(objCardInfo.getCardnumber(),objCardInfo.getCardProductId(),
                objCardInfo.getCustomerTypeId(),"0000",
                objISO.getTranxType(),Integer.parseInt(objISO.getAmount())/100,objISO.getValue(49),objCardInfo.getIssuerId())) {
                    System.out.println("*** Withdrawal Limit Rules Validation Failed");
                    if (DebugWriter.boolDebugEnabled) DebugWriter.write("WithDrawalRequest:Withdrawal Limit Rules Validation Failed.....");

                    objISO.setValue(TPlusISOCode.RESPONSE_CODE,TPlusCodes.EXEED_WITHDRAW_LIMIT);
                    TranxValidation=false;
                }
            //}

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