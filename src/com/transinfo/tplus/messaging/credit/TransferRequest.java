package com.transinfo.tplus.messaging.credit;

import org.jpos.iso.ISOMsg;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.db.WriteLogDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.CardInfo;
import com.transinfo.tplus.javabean.CoreBankReqResBean;
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
				// assign transaction code sub type
		    	objTranxBean.setTranxCodeSubType("TRANSFER");
			}
			catch(OnlineException exp)
			{
				System.out.println("Exception=="+exp);
				throw exp;
			}
			
			objTranxBean.setTraceNo2(objISO.getValue(11));
			
			try{
				// here call the CB FUND TRANSFER
				TransactionDB objTransactionDB = new TransactionDB();

				CoreBankReqResBean objBankReqResBean = new CoreBankReqResBean();
				objBankReqResBean.setSourceId(objISO.getValue(41));
				objBankReqResBean.setAtmpos(objISO.getValue(18).equals("6011")?"Y":"N");
				objBankReqResBean.setAcqId(objISO.getValue(32));
				objBankReqResBean.setCurrCode("2");
				objBankReqResBean.setAcctNo(objISO.getValue(102));
				objBankReqResBean.setTraceNo(objISO.getValue(11));
				objBankReqResBean.setAmt(Double.valueOf(objISO.getValue(4))/100);
				objBankReqResBean.setDateTime(objISO.getValue(7));
				objBankReqResBean.setTranxFee(0);
				objBankReqResBean.setToAcctNo(objISO.getValue(103));
				
				System.out.println("Calling Store procedure ATM_POS_FUNDTRANSFER..  ");
				objBankReqResBean = objTransactionDB.getCBReqResFromFundTransfer(objBankReqResBean);

				if(objBankReqResBean.getResCode() != null && !"".equals(objBankReqResBean.getResCode())){

					String resCode = objBankReqResBean.getResCode();

					if("00".equals(resCode)){

						String approvalCode = objBankReqResBean.getAppCode();

						objTranxBean.setResponseCode(resCode);
						objTranxBean.setRemarks("Tranx Approved");
						objTranxBean.setApprovalCode(approvalCode);

						objISO.setValue(38, approvalCode);
						objISO.setValue(39, resCode);

						WriteLogDB objWriteLogDb = new WriteLogDB();
						objWriteLogDb.updateLog(objISO.getTransactionDataBean());

						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Transaction Inserted....");
						System.out.println("Transaction Inserted");

					}else{
						throw new TPlusException(resCode, "01", "Response Code From CB");
					}				

				}else{
					throw new TPlusException(TPlusCodes.DO_NOT_HONOUR, "01", "Tranx Do NOT honour");
				}
			}catch(TPlusException tpexe){
				System.out.println("TPlusException.....");
				throw new OnlineException(tpexe.getErrorCode(), tpexe.getStrReasonCode(), tpexe.getMessage());
			}catch(Exception ge){
				System.out.println("exp");
				ge.printStackTrace();
				throw new OnlineException("96","G0001","System Error");
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