package com.transinfo.tplus.messaging.credit;

import java.io.PrintStream;

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

public class MiniStatementRequest extends RequestBaseHandler {

	IParser objISO = null;
	TransactionDataBean objTranxBean=null;

	public MiniStatementRequest(){}

	public IParser execute(IParser objISO)throws TPlusException {
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("MiniStatementRequest: Start Processing.....");

		try
		{
			System.out.println("In Cash Request");

			ISOMsg cloneISO = objISO.clone();
			objISO.setCloneISO(cloneISO);
			cloneISO.dump(new PrintStream(System.out), "0");

			try
			{
				objTranxBean = objISO.getTransactionDataBean();

				// assign transaction code sub type
				objTranxBean.setTranxCodeSubType("MINISTAT");

			}
			catch(OnlineException exp)
			{
				System.out.println("Exception=="+exp);
				throw new OnlineException(exp);
			}

			objTranxBean.setTraceNo2(objISO.getValue(11));
			try{

				// here call the CB MINISTATEMENT
				TransactionDB objTransactionDB = new TransactionDB();

				CoreBankReqResBean objBankReqResBean = new CoreBankReqResBean();
				objBankReqResBean.setSourceId(objISO.getValue(41));
				objBankReqResBean.setAcctNo(objISO.getValue(102));
				objBankReqResBean.setTraceNo(objISO.getValue(11));
				
				System.out.println("Calling Store procedure MINI_STATEMENT..  ");
				objBankReqResBean = objTransactionDB.getCBReqResFromMiniStatement(objBankReqResBean);

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

		}catch(OnlineException cex){
			System.out.println("exp online");
			throw new OnlineException(cex);
		}catch(Exception ge){
			System.out.println("exp");
			ge.printStackTrace();
			throw new OnlineException("96","G0001","System Error");
		}
		System.out.println("*** Returning OBJISO ****");
		return objISO;
	}


}