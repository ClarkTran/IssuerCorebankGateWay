
/**
 * Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential material of
 * Trans-Info Pte Ltd. Singapore and its use of disclosure in whole
 * or in part without express written permission of
 * Trans-Info Pte Ltd. Singapore. is prohibited.
 * Date of Creation   : Feb 25, 2008
 * Version Number     : 1.0
 *                   Modification History:
 * Date          Version No.         Modified By           Modification Details.
 */

package com.transinfo.tplus.messaging;

import java.io.PrintStream;
import java.util.Date;
import java.util.Random;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.debug.DebugMsgWriter;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.TranxInfo;
import com.transinfo.tplus.log.TransactionDataBean;
import com.transinfo.tplus.messaging.mux.IMux;
import com.transinfo.tplus.messaging.mux.MuxFactory;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.TPlusISOCode;

@SuppressWarnings("unused")
public abstract class RequestBaseHandler
{


	private static int N=0;
	Date now = new Date(System.currentTimeMillis());

	public RequestBaseHandler(){}

	public abstract IParser execute(IParser objISO)throws TPlusException;


	public ISOMsg sendAndReceiveDestination(IParser objISO)throws TPlusException
	{
		byte[] resByte=null;
		boolean TranxValidation =true;
		ISOMsg objResISO = null;

		if (DebugWriter.boolDebugEnabled) DebugWriter.write("Send and Receive Destination Processing Started.....:");
		TPlusPrintOutput.printMessage("RequestBaseHandler","Send and Receive Destination Processing Started.......");

		try
		{
			System.out.println(objISO.getValue(3));

			ISOMsg muxiso = objISO.clone();

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("MTI :: " + muxiso.getMTI() + ", Processing Code :: " + muxiso.getString(3));
			if(muxiso.getMTI().equals("0100") && (muxiso.getString(3).substring(0,2).equals("30") || muxiso.getString(3).substring(0,2).equals("31")))
			{
				muxiso.setMTI("0200");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Assigned MTI :: " + muxiso.getMTI());
			}else if(muxiso.getMTI().equals("0200") && (muxiso.getString(3).substring(0,2).equals("02") || muxiso.getString(3).substring(0,2).equals("22")))
			{

				TranxInfo objTranxInfo = objISO.getObjTranxInfo();

				if(objTranxInfo != null){

					if(objTranxInfo.getTranxCode() != null){

						muxiso.setMTI("0400");

						String processingCode = muxiso.getString(3);

						if("CASH".equals(objTranxInfo.getTranxCode())){
							processingCode = "01"+muxiso.getString(3).substring(2,6);
						}else{
							processingCode = "00"+muxiso.getString(3).substring(2,6);
						}

						muxiso.set(3, processingCode);

						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Assigned MTI :: " + muxiso.getMTI() + ", Processing Code :: " + processingCode);

					}else{
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("objTranxInfo.getTranxCode() is NULL. NOT change any field values ");
					}

				}else{
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("Original Tranx NOT found NOT change any field values ");
				}
			}

			/*if(("0400".equals(muxiso.getMTI()) || "0401".equals(muxiso.getMTI())) && !muxiso.hasField(41)){
				if (DebugWriter.boolDebugEnabled) DebugWriter.write(" ReversalRequest: NO Terminal ID F41. Assign zeros and send to CB");
				System.out.println(" ReversalRequest: NO Terminal ID F41. Assign zeros and send to CB");
				muxiso.set(41, "00000000");
			}

			System.out.println("muxiso.getValue(41) :: '"+muxiso.getValue(41)+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("muxiso.getValue(41) :: '"+muxiso.getValue(41)+"'");
			if(muxiso.hasField(41) && (muxiso.getValue(41) != null) && ("".equals(muxiso.getString(41).trim()))){
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Spaces on Terminal ID. Replace it by zeros");
				System.out.println("Spaces on Terminal ID. Replace it by zeros");
				muxiso.set(41, "00000000");
			}*/

			System.out.println("muxiso.getValue(41) :: '"+muxiso.getValue(41)+"'");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("muxiso.getValue(41) :: '"+muxiso.getValue(41)+"'");
			if(!muxiso.hasField(41) || (muxiso.hasField(41) && (muxiso.getValue(41) != null) && ("".equals(muxiso.getString(41).trim())))){
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Sending zeros on F41 to CB");
				System.out.println("Sending zeros on F41 to CB");
				muxiso.set(41, "00000000");
			}

			/*if(muxiso.getMTI().equals("0100") && !muxiso.getString(3).substring(0,2).equals("30") && !muxiso.getString(3).substring(0,2).equals("31"))
			{
				muxiso.setMTI("0200");
			}

			if(muxiso.getMTI().equals("0420"))
			{
				muxiso.setMTI("0400");
			}*/
			/*
			if(muxiso.hasField(6))
			{
				muxiso.set(4,muxiso.getString(6));

				muxiso.set(49,muxiso.getString(51));

			}
			 */

			//muxiso.unset(7);
			muxiso.unset(5);
			//muxiso.unset(6);
			muxiso.unset(10);
			muxiso.unset(12);
			muxiso.unset(13);
			muxiso.unset(14);
			//muxiso.unset(18);
			muxiso.unset(19);
			muxiso.unset(22);
			muxiso.unset(24);
			muxiso.unset(25);
			muxiso.unset(26);
			muxiso.unset(28);
			//muxiso.unset(32);
			muxiso.unset(33);
			muxiso.unset(35);
			//muxiso.unset(42);
			//muxiso.unset(43);
			muxiso.unset(44);
			muxiso.unset(45);
			muxiso.unset(48);
			muxiso.unset(50);
			//muxiso.unset(51);
			muxiso.unset(52);
			muxiso.unset(53);
			muxiso.unset(59);
			muxiso.unset(60);
			muxiso.unset(61);
			muxiso.unset(62);
			muxiso.unset(63);
			muxiso.unset(90);
			muxiso.unset(95);
			muxiso.unset(126);
			/*
			for(int z=50;z<129;z++){
				if(!(z==102)){
				muxiso.unset(z);
				}
			}
			 */

			GenericPackager pp=  new GenericPackager(TPlusConfig.getISOIssuerConfig());
			muxiso.setPackager(pp);
			System.out.println("Before printing the messge");
			System.out.println("Data to Core Banking="+ISOUtil.hexString(muxiso.pack()));

			int port =0;
			if(objISO.getIssuerPort()!=null)
				port = Integer.parseInt(objISO.getIssuerPort());
			else
				throw new TPlusException(TPlusCodes.APPL_ERR,TPlusCodes.getErrorDesc(TPlusCodes.APPL_ERR)+". Error: Invalid CoreBanking Port");

			DebugWriter.write("******** REQUEST To Core Banking ***********\n");
			DebugWriter.write("Host Name="+objISO.getIssuer()+" Host IP Address="+objISO.getIssuerHost() +"Host Port="+objISO.getIssuerPort()+"\n");
			DebugWriter.write(" Request Send to CoreBanking... ");
			System.out.println(" Core Banking Connection "+objISO.getIssuerHost()+":"+objISO.getIssuerPort());

			muxiso.dump (new PrintStream(System.out), "0");
			DebugWriter.writeMsgDump(muxiso);

			System.out.println("88888888888"+objISO.getConnectionName());
			IMux  mux = MuxFactory.getMux("CB");
			mux.initialize(objISO);
			objResISO = mux.process(muxiso);

			System.out.println("************************************RECEIVED CB RESPONSE**************************************");

			if(objResISO != null)
			{
				String issuerMsg = ISOUtil.hexString(objResISO.pack());
				System.out.println("++++++++++ F54 in RBH ObjRes ++++++"+objResISO.getString(54));

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("RequestBaseHandler:Issuer Response= "+issuerMsg);
				TPlusPrintOutput.printMessage("RequestBaseHandler"," ISSUER RESPONSE="+ issuerMsg);
				TPlusPrintOutput.printMessage("\n\n************************* ISSUER RESPONSE ********************************\n\n");
				DebugMsgWriter.write("******** RESPONSE FROM HOST ***********\n");

				TPlusPrintOutput.printMessage("\n\n************************* ISSUER RESPONSE ********************************\n\n");
				TPlusPrintOutput.printMessage("RequestBaseHandler","Returned Issuer Response Object..");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("sendAndReceiveDestination:Returned Issuer Response Object..");
			}else{

				System.out.println("************************************RECEIVED NULL RESPONSE**************************************");
				DebugMsgWriter.write("************************************RECEIVED NULL RESPONSE**************************************");
			}


		}catch(TPlusException tplusExp)
		{
			throw tplusExp;
		}
		catch(Exception exp)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,TPlusCodes.getErrorDesc(TPlusCodes.APPL_ERR)+". Error: in RequestBaseHandler:"+exp.toString());
		}

		return objResISO;
	}


	public String getApprovalCode()
	{
		String randNo="";
		Random rand = new Random();
		for(int i=0;i<6;i++)
		{
			randNo = randNo + rand.nextInt(10);
		}

		if(randNo.length()<6)
		{
			for(int i =0;i<(randNo.length()-6);i++)
			{
				randNo = "0"+randNo;
			}
		}

		return randNo;

	}

	public IParser updateResponse(IParser objISO,ISOMsg issuerResObj)throws TPlusException
	{

		try
		{
			//if(objISO.getMTI().equals("0800"))
			if(objISO.getMTI().equals("0800") || objISO.getMTI().equals("0820") || objISO.getMTI().equals("0620") || objISO.getMTI().equals("0302"))
			{
				System.out.println("1");
				if(issuerResObj.hasField(39))
					objISO.setValue(TPlusISOCode.RESPONSE_CODE,issuerResObj.getString(TPlusISOCode.RESPONSE_CODE));
			}
			else
			{

				System.out.println("3");
				if(objISO.hasField(22)) //--added by sai
				{
					if(objISO.getValue(22).equals("021"))
					{
						System.out.println("unset");
						//objISO.unset(54);
					}
				}
				System.out.println("4");
				if(objISO.getF44() !=null)
				{
					objISO.setValue(44,objISO.getF44());
				}


				System.out.println("5");
				TransactionDataBean objTranxBean= objISO.getTransactionDataBean();
				if(issuerResObj.hasField(39))
				{
					objISO.setValue(TPlusISOCode.RESPONSE_CODE,issuerResObj.getString(TPlusISOCode.RESPONSE_CODE));

					if(objTranxBean != null){

						objTranxBean.setResponseCode(objISO.getValue(39));
						if(objISO.getValue(39).equals("00"))
						{
							objTranxBean.setRemarks("Tranx Approved");
						}else{
							objTranxBean.setRemarks("This response Sent by Core Bank");
						}

					}


				}

				System.out.println("6");
				if(!objISO.getMTI().equals("0400") && !objISO.getMTI().equals("0401"))
				{
					if(issuerResObj.hasField(38)){
						objISO.setValue(TPlusISOCode.APPROVAL_CODE,issuerResObj.getString(TPlusISOCode.APPROVAL_CODE));

						if(objTranxBean != null){
							objTranxBean.setApprovalCode(issuerResObj.getString(38));
						}
					}
				}

				if(issuerResObj.hasField(54))
				{
					objISO.setValue(54,issuerResObj.getString(54));
				}

			}

			return 	objISO;

		}
		catch(TPlusException tplusExp)
		{
			System.out.println("Exception while updating Issuer Response.."+tplusExp.getMessage());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exception while updating Issuer Response.."+tplusExp.getMessage());
			objISO.setValue(TPlusISOCode.RESPONSE_CODE,tplusExp.getErrorCode());
			throw tplusExp;

		}catch(Exception exp)
		{
			System.out.println("Exception while updating Issuer Response.."+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exception while updating Issuer Response.."+exp.getMessage());
			throw new TPlusException(TPlusCodes.APPL_ERR," Error: in Response Update: "+exp.getMessage());
		}

	}




	public IParser updateErrResponse(IParser objISO,ISOMsg cloneObj)throws TPlusException
	{

		try
		{
			System.out.println("11");
			if(objISO.getValue(TPlusISOCode.TRANSACTION_CURRENCY)!=null)
				objISO.setTranxCurr(objISO.getValue(TPlusISOCode.TRANSACTION_CURRENCY));

			String strRefNo = objISO.getValue(TPlusISOCode.REF_NO);
			String strOrgTrace = objISO.getValue(TPlusISOCode.SYSTEM_TRACE_NO);
			objISO.setMsgObject(cloneObj);
			objISO.setValue(TPlusISOCode.REF_NO,strRefNo);
			objISO.setOrgTraceNo(strOrgTrace);


			return 	objISO;

		}
		catch(TPlusException tplusExp)
		{
			System.out.println("Exception while updating Error Issuer Response.."+tplusExp.getMessage());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exception while updating Issuer Response.."+tplusExp.getMessage());
			objISO.setValue(TPlusISOCode.RESPONSE_CODE,tplusExp.getErrorCode());
			throw tplusExp;

		}catch(Exception exp)
		{
			System.out.println("Exception while updating Error Issuer Response.."+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exception while updating Issuer Response.."+exp.getMessage());
			throw new TPlusException(TPlusCodes.APPL_ERR," Error: in Err Response Update: "+exp.getMessage());
		}

	}


	public String visaF54(String strF54,String strProcessingCode)
	{
		System.out.println("strProcessingCode"+strProcessingCode);

		String strActType = strProcessingCode.substring(2,4);
		System.out.println(strActType);
		String strAmtType = strF54.substring(2,4);
		System.out.println(strAmtType);
		String strCurrCode = strF54.substring(5,8);
		String strAmtSign="C";
		if(strF54.substring(8,9).equals("1"))
			strAmtSign = "C";
		else
			strAmtSign = "D";

		String strAmt = strF54.substring(9,21);

		return strActType+strAmtType+strCurrCode+strAmtSign+strAmt ;

	}

	public IParser setProcessingCode(IParser objISO)throws Exception
	{
		String strTranxType="00";
		String strFromType="01";
		String strToType="00";
		String strSavingAcct=objISO.getCardDataBean().getSavingAcct();
		String strCheckingAcct=objISO.getCardDataBean().getCheckingAcct();

		strTranxType = objISO.getValue(3).substring(0,2);
		strFromType = objISO.getValue(3).substring(2,4);
		strToType =objISO.getValue(3).substring(4,6);

		System.out.println("Set processing code inside RBH : "+"  "+objISO.getCardDataBean().getSavingAcct()+"  "+objISO.getCardDataBean().getCheckingAcct());

		// assign debit account no
		TransactionDataBean objTransactionDataBean = objISO.getTransactionDataBean();

		// General
		System.out.println("In General");
		if(strFromType.equals("10"))
		{
			if(!(objISO.getCardDataBean().getSavingAcct().length() > 0))
			{
				throw new OnlineException("53","G0001","Customer Core Banking Saving Account not Found");
			}
			else
			{
				objTransactionDataBean.setDebitAccNo(objISO.getCardDataBean().getSavingAcct());
				objISO.setValue(102,objISO.getCardDataBean().getSavingAcct());
			}
		}

		if(strFromType.equals("20"))
		{
			if(!(objISO.getCardDataBean().getCheckingAcct().length() > 0))
			{
				throw new OnlineException("52","G0001","Customer Core Banking Checking Account not Found");
			}
			else
			{
				objTransactionDataBean.setDebitAccNo(objISO.getCardDataBean().getCheckingAcct());
				objISO.setValue(102,objISO.getCardDataBean().getCheckingAcct());
			}
		}


		if(strFromType.equals("30") || strFromType.equals("40") || strFromType.equals("00"))
		{
			objTransactionDataBean.setDebitAccNo(objISO.getCardDataBean().getSavingAcct());
			objISO.setValue(102,objISO.getCardDataBean().getSavingAcct());
			//throw new OnlineException("57","G0001","Customer Core Banking Credit/Universal Account not Found");
		}

		return objISO;

	}


	public String getCurrencyCode(IParser objISO)
	{

		if(objISO.getCurrency()!=null && !objISO.getCurrency().equals(""))
		{
			return objISO.getCurrency();
		}

		return objISO.getCardDataBean().getCurrencyCode();
	}

	public IParser updateData(IParser objISO)throws TPlusException
	{

		try
		{

			TransactionDataBean objTranxBean= objISO.getTransactionDataBean();
			if(objISO.hasField(55))
			{
				objTranxBean.setF55Exist("Y");
				objTranxBean.setF55(objISO.getValue(55));
			}else{
				objTranxBean.setF55Exist("N");
			}

			return 	objISO;

		}
		catch(Exception exp)
		{
			System.out.println("Exception while updating Issuer Response.."+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Exception while updating Issuer Response.."+exp.getMessage());
			throw new TPlusException(TPlusCodes.APPL_ERR," Error: in Response Update: "+exp.getMessage());
		}

	}


}