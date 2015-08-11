package com.transinfo.tplus.log;

//Java specific imports
import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.alert.AlertManager;
import com.transinfo.tplus.db.WriteLogDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.RequestInfo;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;

@SuppressWarnings("unused")
public class LogWriter
{

	/** Update the logs to the file.
	 */
	public void updateLogs(IParser objISOParser,RequestInfo objReqInfo)
	{


		//PALog objPALog = null;
		ErrorLog objErrLog = null;
		//TransactionDataBean objTranxDataBean = null;
		TransactionDataBean objTranxDataBean = objISOParser.getTransactionDataBean();

		try
		{

			boolean boolReqError =objReqInfo.isReqError();
			int intReturn =objReqInfo.getReturnTo();
			String strErrCode =objReqInfo.getReqErrCode();
			String strErrMsg =objReqInfo.getReqErrMsg();
			String strErrSrc =objReqInfo.getErrorSrc();
			String strRequest =objReqInfo.getRequest();

			//String strTranxType = TPlusTransType.getTranxType(objISOParser.getValue(TPlusISOCode.MTI),objISOParser.getValue(TPlusISOCode.PROCESSING_CODE));
			String strTranxType =objISOParser.getTranxType();
			//if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:TRACK2DATA=="+ objISOParser.getValue(TPlusISOCode.TRACK2DATA));
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:REQUEST ERROR=="+boolReqError);

			if(objISOParser != null && !objISOParser.equals("") && objISOParser.isParse())
			{

				/*objPALog = new PALog();
					objPALog.addLogItem(
						strSessionId,"ACQ123",ISOUtil.hexString(strRequest.getBytes()),"CR",
						objISOParser.getValue(TPlusISOCode.MERCHANT_ID),
						objISOParser.getValue(TPlusISOCode.TERMINAL_ID));
	if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest:PA Log Created");*/

				System.out.println("***********333*************boolReqError****************************"+boolReqError);
				if(boolReqError)
				{
					objErrLog = new ErrorLog();
					objErrLog.addLogItem(strErrCode,"Issuer1",
							objISOParser.getValue(TPlusISOCode.MERCHANT_ID),
							objISOParser.getValue(TPlusISOCode.TERMINAL_ID),
							strErrMsg,strTranxType,strErrSrc);

				}


			}
			else
			{

				if(boolReqError)
				{
					objErrLog = new ErrorLog();
					objErrLog.addLogItem(strErrCode,"Issuer1",
							"",
							"",
							strErrMsg,strTranxType,strErrSrc);
				}


				//objTranLog = new TransactionLog();
				//objTranLog.setTransactionID(TPlusConfig.getSystemParam().getTransactionID());
				//objTranLog.addLogItem(strReqErrCode,strReqExp);

			}




		}
		catch(Exception e)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest: Error in creating the Error/Transaction Log:"+e);
			System.out.println("Error in creating the Error/Transaction Log Transaction"+e);
		}
		finally
		{
			try
			{

				/*if(objPALog != null)
				  WriteLogDB.updateLog(objPALog);*/
				System.out.println("*********** 8888 ************* ReqError ****************************");
				if(objErrLog != null)
					WriteLogDB.updateLog(objErrLog);

				/*if(objTranxDataBean != null)
				  System.out.println("Writing in Tranx Data Bean...");
				  WriteLogDB.updateLog(objTranxDataBean);*/
			}
			catch(TPlusException tplusExp)
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusRequest: Error in Logging the Error/Transaction Log:"+tplusExp.getMessage());
				AlertManager.RaiseAlert(""+TPlusCodes.ENGINE_ERROR,"LOGGING ERROR:"+tplusExp.getMessage());
			}

		}
	}

}