package com.transinfo.tplus.messaging.credit;

import org.jpos.iso.ISOMsg;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.db.WriteLogDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.CardInfo;
import com.transinfo.tplus.log.TransactionDataBean;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.RequestBaseHandler;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;

public class TransferRequest extends RequestBaseHandler {

    IParser objISO = null;
    public TransferRequest(){}

    public IParser execute(IParser objISO)throws TPlusException {
        
        boolean TranxValidation =true;
        TransactionDataBean objTranxBean=null;
        CardInfo objCardInfo=null;


        if (DebugWriter.boolDebugEnabled) DebugWriter.write("TransferRequest Start Processing :");

        try
        {

            ISOMsg cloneISO = objISO.clone();
            objISO.setCloneISO(cloneISO);
            TransactionDB objTranxDB = new TransactionDB();

			try
			{
				objTranxBean = objISO.getTransactionDataBean();
		    	
			}
			catch(OnlineException exp)
			{
				System.out.println("Exception=="+exp);
				throw exp;
			}

            System.out.println(" Card Validation is Successful");
            // Sale transaction

			objCardInfo = objISO.getCardDataBean();

            String savingsAccount = objCardInfo.getSavingAcct();
            String checkingAccount = objCardInfo.getCheckingAcct();

            DebugWriter.write("savingsAccount >> " + savingsAccount);
            DebugWriter.write("checkingAccount >> " + checkingAccount);


            int cLen = 0;
            int sLen = 0;
            if(objCardInfo.getSavingAcct() != null) sLen = savingsAccount.length();
            if(objCardInfo.getCheckingAcct() != null) cLen = checkingAccount.length();

            if(objISO.getValue(3).equals("400000") || objISO.getValue(3).equals("401020") || objISO.getValue(3).equals("402010")){
				 if(savingsAccount ==null || savingsAccount.trim().equals(""))
				{
					throw new OnlineException("53","G0001","No Saving Account");
				}

				if(checkingAccount == null || checkingAccount.trim().equals(""))
				{
					throw new OnlineException("52","G0001","No Checking Account");
				}
                if(cLen > 0 && sLen > 0){
                    if(objISO.getValue(3).equals("400000")) {
                        objISO.setValue(102,savingsAccount);
                        objISO.setValue(103,checkingAccount);
                        objISO.setValue(3,"401020");
                    }else if(objISO.getValue(3).equals("401020")) {
                        objISO.setValue(102,savingsAccount);
                        objISO.setValue(103,checkingAccount);
                    }else if(objISO.getValue(3).equals("402010")) {
                        objISO.setValue(102,checkingAccount);
                        objISO.setValue(103,savingsAccount);
                    }
                }else{
                    TranxValidation=false;
                }
            }else if(objISO.getValue(3).equals("401000")) {
                if(sLen > 0) objISO.setValue(102,savingsAccount);
                else TranxValidation=false;
            }else if(objISO.getValue(3).equals("402000")) {
                if(cLen > 0) objISO.setValue(102,checkingAccount);
                else TranxValidation=false;
            }else {
                TranxValidation=false;
            }


            String accountOne = objISO.getValue(102);
            String accountTwo = objISO.getValue(103);

            if(accountOne.trim().equals(accountTwo.trim())){ //if both the accounts are same, reject
                TranxValidation=false;
            }

            if(!TranxValidation)
            {
                objISO.setValue(TPlusISOCode.RESPONSE_CODE,TPlusCodes.DO_NOT_HONOUR);
                return objISO;
            }

            ISOMsg objRes = sendAndReceiveDestination(objISO);
            if(objRes!=null) {
                objISO.setMsgObject(cloneISO);
                objISO= updateResponse(objISO,objRes);
                //objISO.setValue(39,objRes.getString(39));
                //objISO.setValue(TPlusISOCode.ADDITIONAL_DATA,objRes.getString(TPlusISOCode.ADDITIONAL_DATA));


				try
				{
					WriteLogDB objWriteLogDb = new WriteLogDB();
					objWriteLogDb.updateLog(objISO.getTransactionDataBean());
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("Transaction Inserted....");
					System.out.println("Transaction Inserted");
					if(objISO.getValue(39).equals("00"))
					{
						objISO.getTransactionDataBean().setGLType("D");
						objTranxDB.insertDebitGL(objISO.getTransactionDataBean());
						System.out.println("Transaction Inserted into Debit GL");
					}


				}
				catch(Exception e)
				{
					 e.printStackTrace();
					 if (DebugWriter.boolDebugEnabled) DebugWriter.write("Error Inserting Transaction...");
					 System.out.println("Error Inserting Transaction...");
					 throw new OnlineException("96","G0001","System Error");
				}


                if(objISO.getValue(22) != null){ //--added by sai
                    if(objISO.getValue(22).equals("021")) {
                        System.out.println("unset");
                        objISO.unset(54);
                    }
                }
            }
            else
            {
				return null;
			}
        }
        catch(OnlineException cex) {
            System.out.println("Exception while execute TransferRequest Request.."+cex);
           // objISO.setValue(TPlusISOCode.RESPONSE_CODE,tplusExp.getErrorCode());
           throw new OnlineException(cex);

        }catch(Exception exp) {
			 exp.printStackTrace();
	        throw new OnlineException("96","G0001","System Error");
        }

        return objISO;

    }


}