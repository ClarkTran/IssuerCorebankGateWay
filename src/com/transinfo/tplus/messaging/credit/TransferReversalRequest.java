package com.transinfo.tplus.messaging.credit;

import org.jpos.iso.ISOMsg;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.db.WriteLogDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.CardCashPurseDataBean;
import com.transinfo.tplus.javabean.CoreBankReqResBean;
import com.transinfo.tplus.javabean.TranxInfo;
import com.transinfo.tplus.log.TransactionDataBean;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.RequestBaseHandler;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.validator.CardNumberValidator;
import com.transinfo.tplus.messaging.validator.CardValidator;
import com.transinfo.tplus.messaging.validator.Validator;
import com.transinfo.tplus.util.DateUtil;

@SuppressWarnings("static-access")
public class TransferReversalRequest extends RequestBaseHandler {

	public TransferReversalRequest() {
	}

	public IParser execute(IParser objISO) throws TPlusException {
		TransactionDataBean objTranxBean=null;

		if (DebugWriter.boolDebugEnabled)
			DebugWriter.write("Transfer Reversal Start Processing :");
		System.out.println("Transfer Reversal Start Processing :");

		boolean isDeleteRec = true;

		try {

			CardCashPurseDataBean objCardCashPurseDataBean = new CardCashPurseDataBean();
			TransactionDB objTransactionDB = new TransactionDB();

			ISOMsg cloneISO = objISO.clone();
			objISO.setCloneISO(cloneISO);

			objTranxBean = objISO.getTransactionDataBean();
			// assign transaction code sub type
			objTranxBean.setTranxCodeSubType("TRANSFER_R");


			try {
				TranxInfo objTranxInfo = null;
				if(null == (objTranxInfo = objTransactionDB.recordExistsReversal(objISO))){
					System.out.println("Record NOT FOUND FOR REVERSAL");
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("Reversal: Record Not Found for Reversal...");
					throw new OnlineException("00","G0001","Record Not Found for Reversal");
				}
				
				objTranxBean.setTraceNo2(objISO.getValue(11));
				
				// here call the CB Transfer Reversal
				CoreBankReqResBean objBankReqResBean = new CoreBankReqResBean();
				objBankReqResBean.setSourceId(objISO.getValue(41));
				objBankReqResBean.setAcqId(objISO.getValue(90).substring(20, 31));
				objBankReqResBean.setCurrCode("2");
				objBankReqResBean.setAcctNo(objISO.getValue(102));
				objBankReqResBean.setTraceNo(objISO.getValue(90).substring(4,10));
				objBankReqResBean.setAmt(Double.valueOf(objISO.getValue(4))/100);
				objBankReqResBean.setDateTime(objISO.getValue(90).substring(10, 20));
				objBankReqResBean.setTranxFee(0);
				objBankReqResBean.setToAcctNo(objISO.getValue(103));

				System.out.println("Calling Store procedure ATM_POS_FT_REV..");
				objBankReqResBean = objTransactionDB.getCBReqResFromFTRevsal(objBankReqResBean);

				if(objBankReqResBean.getResCode() != null && !"".equals(objBankReqResBean.getResCode())){

					String resCode = objBankReqResBean.getResCode();

					if("00".equals(resCode)){

						String approvalCode = objBankReqResBean.getAppCode();

						objTranxBean.setResponseCode(resCode);
						objTranxBean.setRemarks("Tranx Approved");
						objTranxBean.setApprovalCode(approvalCode);

						objISO.setValue(38, approvalCode);
						objISO.setValue(39, resCode);

						if("JC".equals(objISO.getCardProduct())){
							
							String reqF39 = objISO.getValue(39);
							
							// over write
							objTranxBean.setRemarks("Tranx Approved_Req F39 " + reqF39);
							
						}
						
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
				
				if (DebugWriter.boolDebugEnabled)
					DebugWriter.write("Transaction Inserted....");
				System.out.println("Transaction Inserted");

			} catch(TPlusException tpexe){
					System.out.println("TPlusException.....");
					throw new OnlineException(tpexe.getErrorCode(), tpexe.getStrReasonCode(), tpexe.getMessage());	
			} catch (Exception e) {
				if (DebugWriter.boolDebugEnabled)
					DebugWriter.write("Error Inserting Transaction...");
				System.out.println("Error Inserting Transaction...");
				throw new OnlineException("05", "G0001", "System Error");

			}

		} catch (OnlineException cex) {
			System.out.println("exp online");
			throw new OnlineException(cex);
		}catch(TPlusException tpexe){
			System.out.println("TPlusException.....");
			throw new OnlineException(tpexe.getErrorCode(), tpexe.getStrReasonCode(), tpexe.getMessage());
		}catch (Exception exp) {
			throw new OnlineException("96", "G0001", "System Error while processing Reversal " + exp.getMessage());
		}
		System.out.println("Request Finished..");
		return objISO;

	}

}