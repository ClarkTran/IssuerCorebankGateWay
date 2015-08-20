/*
 * Copyright (c) 2001-2002 OneEmpower Pte Ltd. Singapore. All Rights Reserved.
 * This work contains trade secrets and confidential
 * material of  OneEmpower Pte Ltd. Singapore and its use of
 * disclosure in whole or in part without express written permission of
 * OneEmpower Pte Ltd. Singapore is prohibited.
 * File Name          : TransactionDataBean.java
 * Author             : I.T. Solutions (India) Pvt Ltd.
 * Date of Creation   : October 10, 2001
 * Description        : TransactionDataBean with get and set methods
 * Version Number     : 1.0
 * Modification History:
 * Date 		 Version No.		Modified By  	 	  Modification Details.
 * 28-May-2003	   1.01				Rajeev				  SecureCode Changes.
 *
 */

package com.transinfo.tplus.log;

import org.jpos.iso.ISOMsg;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;

/**
 * This class holds a Transaction Log entry.
 * It has set and get  methods for all the member variables.
 * It is derived from LogDataBean. This class implements java.io.Serializable.
 */

@SuppressWarnings({ "unused", "serial"})
public class TransactionDataBean extends LogDataBean
implements java.io.Serializable
{
	private String strMTI= "";
	private String strProcessCode= "";
	private String strIssuerId="";
	private String strTranxCode="";
	private String strMerchantId="";
	private String strMerchantName="";
	private String strTerminalId="";
	private String strCardNo="";
	private String strExpDate="";
	private double dblTranxAmount=0.0;
	private String strTranxCurrCode="";
	private double dblOrgAmt=0.0;
	private String strOrgCurr="";
	private String strTraceNo="";
	private String strMCC="";
	private String strTCC="";
	private String strPOSEntryMode="";
	private String strPOSCondCode="";
	private String strTrack2Data="";
	private String strRefNo="";
	private String strResponseCode="";
	private String strApprovalCode="";
	private String strPinData="";
	private String strTraceNo2="";
	private String strDeleted="N";

	private String strTranxAmount = "";
	private String strTranxSettledAmt = "";
	private String strTranxCurr = "";
	private String strTranxFee = "";
	private String strTranxSettledCurr = "";
	private String strTranxCHAmt = "";
	private String strTanxFee = "";
	private String isAuthComplete = "N";
	private String strVisaCutOff = "";
	private String strF90 = "";
	private String strAcqInstId="";
	private String strAcqBinType = "";
	private String strRemarks = "";
	private String strRecon = "";

	private String strTranxDateTime=null;
	private String strTranxTime=null;
	private String strTranxDate=null;
	private String strAutoSettled="N";
	private String strVisaEnable="N";
	private String strCardTypeId="";
	private String strTranxId="";
	private String strGLType="";

	private String f55Exist="N";
	private String f55="";
	private String f55Res="";
	private String isAuthComVoid="N";

	private String currConRate="";
	private String acqCountryCode="";
	private String debitAccNo="";

	private String limitUsed="0";
	private String tranxCurrConvAmt="0.0";

	private String tranxIdentifier="";
	private String partialAmt="0";

	private String currDecimalPoint="100";

	private String tranxCodeSubType="SALE";

	public TransactionDataBean ()
	{	}

	public TransactionDataBean(String strIssuerId,String strMTI,String strTranxCode,String strProcessCode,String strMerchantId,String strMerchantName,
			String strTerminalId,String strCardNo,String strExpDate,String strAmount,
			String strCurrCode,String strOrgAmt,String strOrgCurr,String strTraceNo,
			String strMCC,String strTCC,String strPOSEntryMode,String strTrack2Data,
			String strRefNo,String strResponseCode,String strApprovalCode,String strPinData,
			String strTraceNo2,String strDeleted,
			String strTranxAmount,String strTranxCurr,String strTranxSettledAmt,String strTranxCHAmt,String strTanxFee,String isAuthComplete,
			String strVisaCutOff,String strF90,String strAcqInstId,String strAcqBinType,String strRemarks,
			String strTranxDateTime, String strTranxTime,String strTranxDate, String strAutoSettled, String strVisaEnable)throws TPlusException
			{

		super("",strIssuerId,strMerchantId,strTerminalId);
		try
		{

			this.strMTI = strMTI;
			this.strProcessCode = strProcessCode;
			this.strTranxCode=strTranxCode;
			this.strCardNo=strCardNo;
			this.strExpDate=strExpDate;
			this.dblTranxAmount=new Double(strAmount).doubleValue();
			this.strTranxCurrCode=strCurrCode;
			this.strTraceNo=strTraceNo;
			this.strMCC = strMCC;
			this.strTCC = strTCC;
			this.strTrack2Data=strTrack2Data;
			this.strRefNo=strRefNo;
			this.strResponseCode=strResponseCode;
			this.strApprovalCode = strApprovalCode;
			this.strPinData=strPinData;
			this.strDeleted=strDeleted;
			this.strPOSEntryMode=strPOSEntryMode;
			this.strTraceNo2 = strTraceNo2;
			this.dblOrgAmt = new Double(strOrgAmt).doubleValue();
			this.strOrgCurr = strOrgCurr;

		}
		catch(Exception objExcp)
		{
			throw new TPlusException(TPlusCodes.APPL_ERR,
					objExcp.getMessage());
		}
			}


	/** Getter for property MTI.
	 * @return Value of property MTI.
	 *
	 */
	public java.lang.String getGLType() {
		return strGLType;
	}

	/** Setter for property MTI.
	 * @param MTI New value of property MTI.
	 *
	 */
	public void setGLType(java.lang.String strGLType) {
		this.strGLType = strGLType;
	}



	/** Getter for property MTI.
	 * @return Value of property MTI.
	 *
	 */
	public java.lang.String getTransactionId() {
		return strTranxId;
	}

	/** Setter for property MTI.
	 * @param MTI New value of property MTI.
	 *
	 */
	public void setTransactionId(java.lang.String strTranxId) {
		this.strTranxId = strTranxId;
	}


	/** Getter for property MTI.
	 * @return Value of property MTI.
	 *
	 */
	public java.lang.String getMTI() {
		return strMTI;
	}

	/** Setter for property MTI.
	 * @param MTI New value of property MTI.
	 *
	 */
	public void setMTI(java.lang.String MTI) {
		this.strMTI = MTI;
	}


	/** Getter for property ProcessCode.
	 * @return Value of property ProcessCode.
	 *
	 */
	public java.lang.String getProcessingCode() {
		return strProcessCode;
	}

	/** Setter for property MTI.
	 * @param MTI New value of property MTI.
	 *
	 */
	public void setProcessingCode(java.lang.String ProcessCode) {
		this.strProcessCode = ProcessCode;
	}



	/** Getter for property Amount.
	 * @return Value of property Amount.
	 *
	 */
	public double getAmount() {
		return dblTranxAmount;
	}

	/** Setter for property Amount.
	 * @param Amount New value of property Amount.
	 *
	 */
	public void setAmount(double Amount) {
		this.dblTranxAmount = Amount;
	}




	/** Getter for property CardNo.
	 * @return Value of property CardNo.
	 *
	 */
	public java.lang.String getCardNo() {
		return strCardNo;
	}

	/** Setter for property CardNo.
	 * @param CardNo New value of property CardNo.
	 *
	 */
	public void setCardNo(java.lang.String CardNo) {
		this.strCardNo = CardNo;
	}

	/** Getter for property CurrCode.
	 * @return Value of property CurrCode.
	 *
	 */
	public java.lang.String getTranxCurrCode() {
		return strTranxCurrCode;
	}

	/** Setter for property CurrCode.
	 * @param CurrCode New value of property CurrCode.
	 *
	 */
	public void setTranxCurrCode(java.lang.String CurrCode) {
		this.strTranxCurrCode = CurrCode;
	}


	/** Getter for property Deleted.
	 * @return Value of property Deleted.
	 *
	 */
	public java.lang.String getDeleted() {
		return strDeleted;
	}

	/** Setter for property Deleted.
	 * @param Deleted New value of property Deleted.
	 *
	 */
	public void setDeleted(java.lang.String Deleted) {
		this.strDeleted = Deleted;
	}

	/** Getter for property ExpDate.
	 * @return Value of property ExpDate.
	 *
	 */
	public java.lang.String getExpDate() {
		return strExpDate;
	}

	/** Setter for property ExpDate.
	 * @param ExpDate New value of property ExpDate.
	 *
	 */
	public void setExpDate(java.lang.String ExpDate) {
		this.strExpDate = ExpDate;
	}


	/** Getter for property MCC.
	 * @return Value of property MCC.
	 *
	 */
	public java.lang.String getMCC() {
		return strMCC;
	}

	/** Setter for property MCC.
	 * @param MCC New value of property MCC.
	 *
	 */
	public void setMCC(java.lang.String MCC) {
		this.strMCC = MCC;
	}

	/** Getter for property PINData.
	 * @return Value of property PINData.
	 *
	 */
	public java.lang.String getPINData() {
		return strPinData;
	}

	/** Setter for property PINData.
	 * @param PINData New value of property PINData.
	 *
	 */
	public void setPINData(java.lang.String PinData) {
		this.strPinData = PinData;
	}

	/** Getter for property POSEntryMode.
	 * @return Value of property POSEntryMode.
	 *
	 */
	public java.lang.String getPOSEntryMode() {
		return strPOSEntryMode;
	}

	/** Setter for property POSEntryMode.
	 * @param POSEntryMode New value of property POSEntryMode.
	 *
	 */
	public void setPOSEntryMode(java.lang.String POSEntryMode) {
		this.strPOSEntryMode = POSEntryMode;
	}


	/** Getter for property POSEntryMode.
	 * @return Value of property POSEntryMode.
	 *
	 */
	public java.lang.String getPOSCondCode() {
		return strPOSCondCode;
	}

	/** Setter for property POSEntryMode.
	 * @param POSEntryMode New value of property POSEntryMode.
	 *
	 */
	public void setPOSCondCode(java.lang.String strPOSCondCode) {
		this.strPOSCondCode = strPOSCondCode;
	}

	/** Getter for property RefNo.
	 * @return Value of property RefNo.
	 *
	 */
	public java.lang.String getRefNo() {
		return strRefNo;
	}

	/** Setter for property RefNo.
	 * @param RefNo New value of property RefNo.
	 *
	 */
	public void setRefNo(java.lang.String RefNo) {
		this.strRefNo = RefNo;
	}


	/** Getter for property ResponseCode.
	 * @return Value of property ResponseCode.
	 *
	 */
	public java.lang.String getResponseCode() {
		return strResponseCode;
	}

	/** Setter for property ResponseCode.
	 * @param ResponseCode New value of property ResponseCode.
	 *
	 */
	public void setResponseCode(java.lang.String ResponseCode) {
		this.strResponseCode = ResponseCode;
	}

	/** Getter for property ApprovalCode.
	 * @return Value of property ApprovalCode.
	 *
	 */
	public java.lang.String getApprovalCode() {
		return strApprovalCode;
	}

	/** Setter for property ApprovalCode.
	 * @param ApprovalCode New value of property ApprovalCode.
	 *
	 */
	public void setApprovalCode(java.lang.String ApprovalCode) {
		this.strApprovalCode = ApprovalCode;
	}

	/** Getter for property TCC.
	 * @return Value of property TCC.
	 *
	 */
	public java.lang.String getTCC() {
		return strTCC;
	}

	/** Setter for property TCC.
	 * @param TCC New value of property TCC.
	 *
	 */
	public void setTCC(java.lang.String TCC) {
		this.strTCC = TCC;
	}

	/** Getter for property TraceNo.
	 * @return Value of property TraceNo.
	 *
	 */
	public java.lang.String getTraceNo() {
		return strTraceNo;
	}

	/** Setter for property TraceNo.
	 * @param TraceNo New value of property TraceNo.
	 *
	 */
	public void setTraceNo(java.lang.String TraceNo) {
		this.strTraceNo = TraceNo;
	}

	/** Getter for property Track2Data.
	 * @return Value of property Track2Data.
	 *
	 */
	public java.lang.String getTrack2Data() {
		return strTrack2Data;
	}

	/** Setter for property Track2Data.
	 * @param Track2Data New value of property Track2Data.
	 *
	 */
	public void setTrack2Data(java.lang.String Track2Data) {
		this.strTrack2Data = Track2Data;
	}


	/** Getter for property TranxCode.
	 * @return Value of property TranxCode.
	 *
	 */
	public java.lang.String getTranxCode() {
		return strTranxCode;
	}

	/** Setter for property TranxCode.
	 * @param TranxCode New value of property TranxCode.
	 *
	 */
	public void setTranxCode(java.lang.String TranxCode) {
		this.strTranxCode = TranxCode;
	}

	/** Getter for property TraceNo2.
	 * @return Value of property TraceNo2.
	 *
	 */
	public java.lang.String getTraceNo2() {
		return strTraceNo2;
	}

	/** Setter for property TraceNo2.
	 * @param TraceNo2 New value of property TraceNo2.
	 *
	 */
	public void setTraceNo2(java.lang.String TraceNo2) {
		this.strTraceNo2 = TraceNo2;
	}


	/** Getter for property OrgAmt.
	 * @return Value of property OrgAmt.
	 *
	 */
	public double getOrgAmt() {
		return dblOrgAmt;
	}

	/** Setter for property OrgAmt.
	 * @param TraceNo2 New value of property OrgAmt.
	 *
	 */
	public void setOrgAmt(double dblOrgAmt) {
		this.dblOrgAmt = dblOrgAmt;
	}


	/** Getter for property OrgCurrCode.
	 * @return Value of property OrgCurrCode.
	 *
	 */
	public java.lang.String getOrgCurrCode() {
		return strOrgCurr;
	}

	/** Setter for property OrgCurrCode.
	 * @param TraceNo2 New value of property OrgCurrCode.
	 *
	 */
	public void setOrgCurrCode(java.lang.String strOrgCurr) {
		this.strOrgCurr = strOrgCurr;
	}

	/** Getter for property merchantName.
	 * @return Value of property merchantName.
	 *
	 */
	public java.lang.String getMerchantName() {
		return strMerchantName;
	}

	/** Setter for property OrgCurrCode.
	 * @param TraceNo2 New value of property OrgCurrCode.
	 *
	 */
	public void setMerchantName(java.lang.String strMerchantName) {
		this.strMerchantName = strMerchantName;
	}



	public String getVisaCutOff() {
		return strVisaCutOff;
	}
	public void setVisaCutOff(String strVisaCutOff) {
		this.strVisaCutOff= strVisaCutOff;
	}

	public String getF90() {
		return strF90;
	}
	public void setF90(String strF90) {
		this.strF90= strF90;
	}


	public void setTranxAmt(String strTranxAmount)
	{
		this.strTranxAmount = strTranxAmount;
	}

	public String getTranxAmt()
	{
		return strTranxAmount;
	}

	/*public void setTranxCurr(String tranxCurrency)
	{
		this.strTranxCurrency = strTranxCurrency;
	}

	public String getTranxCurr()
	{
		return strTranxCurrency;
	}*/

	public String getTranxSettledAmt() {
		return strTranxSettledAmt;
	}
	public void setTranxSettledAmt(String strTranxSettledCurr) {
		this.strTranxSettledAmt = strTranxSettledCurr;
	}

	public String getTranxSettledCurr() {
		return strTranxSettledCurr;
	}
	public void setTranxSettledCurr(String strTranxSettledCurr) {
		this.strTranxSettledCurr = strTranxSettledCurr;
	}
	public String getTranxCHAmt() {
		return strTranxCHAmt;
	}
	public void setTranxCHAmt(String strTranxCHAmt) {
		this.strTranxCHAmt = strTranxCHAmt;
	}
	public String getTranxFee() {
		return strTranxFee;
	}
	public void setTranxFee(String strTranxFee) {
		this.strTranxFee = strTranxFee;
	}



	public String getIsAuthComplete() {
		return isAuthComplete;
	}
	public void setIsAuthComplete(String isAuthComplete) {
		this.isAuthComplete = isAuthComplete;
	}

	public String getAcqInstId() {
		return strAcqInstId;
	}
	public void setAcqInstId(String strAcqInstId) {
		this.strAcqInstId = strAcqInstId;
	}

	public String getAcqBinType() {
		return strAcqBinType;
	}
	public void setAcqBinType(String strAcqBinType) {
		this.strAcqBinType = strAcqBinType;
	}

	public String getRemarks() {
		return strRemarks;
	}
	public void setRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}

	public String getRecon()
	{
		return strRecon;
	}

	public void setRecon(String strRecon)
	{
		this.strRecon = strRecon;
	}

	public String getTransmissionDateTime()
	{
		return strTranxDateTime;
	}

	public void setTransmissionDateTime(String strTranxDateTime)
	{
		this.strTranxDateTime = strTranxDateTime;
	}


	public String getTranxTime()
	{
		return strTranxTime;
	}

	public void setTranxTime(String strTranxTime)
	{
		this.strTranxTime = strTranxTime;
	}


	public String getTranxDate()
	{
		return strTranxDate;
	}

	public void setTranxDate(String strTranxDate)
	{
		this.strTranxDate = strTranxDate;
	}

	public String getCardTypeId()
	{
		return strCardTypeId;
	}

	public void setCardTypeId(String strCardTypeId)
	{
		this.strCardTypeId = strCardTypeId;
	}

	public void populateData(IParser objISO)throws OnlineException
	{

		try
		{
			this.setIssuerId(TPlusConfig.ISSUER_ID);
			this.setMTI(objISO.getMTI());
			this.setProcessingCode(objISO.getValue(3));
			this.setTranxCode(objISO.getTranxType());
			System.out.println(this.getTranxCode()+"   "+objISO.getTranxType());
			this.setTransmissionDateTime(objISO.getValue(7));
			this.setTraceNo(objISO.getValue(11));
			this.setMCC(objISO.getValue(18));
			this.setPOSEntryMode(objISO.getValue(22));
			this.setPOSCondCode(objISO.getValue(25));
			this.setAcqInstId(objISO.getValue(32));
			this.setTrack2Data(objISO.getValue(35));
			this.setRefNo(objISO.getValue(37));

			this.setTerminalId(objISO.getValue(41));
			this.setMerchantId(objISO.getValue(42));
			this.setMerchantName(objISO.getValue(43));
			this.setCardNo(objISO.getCardNumber());
			this.setExpDate(objISO.getValue(14));

			this.setRecon("N");
			this.setVisaCutOff("N");
			//if(objISO.getValue(90) != null){
			this.setF90(objISO.getValue(90));
			//}
			this.setMTI(objISO.getMTI());

			// trace no 2 will be used to void & Reversal
			this.setTraceNo2(objISO.getValue(11));

			//if(objISO.getValue(32).equals("437734"))
			//if(objISO.getValue(32).equals("437733"))
			
			String mti = objISO.getMTI(); // 0200 - CASH, SALE : 0400 - REVERSL : 0220 - AUTHCOMPLETE
			
			/*if((("437733".equals(objISO.getValue(32))) 
					&& ("0200".equals(mti) 
							|| "0220".equals(mti) 
							|| "0400".equals(mti) 
							|| ("0100".equals(mti) && "38".equals(objISO.getValue(3).substring(0,2))))) 
				|| "0800".equals(mti))
			{
				this.setAcqBinType("ONUS");
			}
			else{
				this.setAcqBinType("OFFUS");
			}*/

			this.setAcqCountryCode(objISO.getValue(19));
			this.setCurrConRate(objISO.getValue(10));

			if(objISO.hasField(55)){
				this.setF55Exist("Y");
				this.setF55(objISO.getValue(55));
			}

			// it should be inserted all the transactions
			this.setTranxCurrCode(objISO.getValue(49));

			//this.setAcqBinType("OFFUS");
			//this.setTransactionId(generateTransactionId());
			//System.out.println("TransactionId : "+ transactionId);

			// set transaction identifier
			if(objISO.hasField(62))
			{
				System.out.println("Inside F62");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Inside F62");

				if(objISO.getISOObject(62) instanceof ISOMsg)
				{
					ISOMsg F62 = (ISOMsg)objISO.getISOObject(62);
					if(F62.hasField(2))
					{
						System.out.println("Inside F62.2 " + F62.getString(2));
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Inside F62.2 " + F62.getString(2));

						this.setTranxIdentifier(F62.getString(2));

					}
				}
			}

		}
		catch(Exception exp)
		{
			throw new OnlineException("09","A05374","Problem Populating Data...");
		}

	}

	public String getF55Exist() {
		return f55Exist;
	}

	public void setF55Exist(String f55Exist) {
		this.f55Exist = f55Exist;
	}

	public String getF55() {
		return f55;
	}

	public void setF55(String f55) {
		this.f55 = f55;
	}

	public String getIsAuthComVoid() {
		return isAuthComVoid;
	}

	public void setIsAuthComVoid(String isAuthComVoid) {
		this.isAuthComVoid = isAuthComVoid;
	}

	public String getCurrConRate() {
		return currConRate;
	}

	public void setCurrConRate(String currConRate) {
		this.currConRate = currConRate;
	}

	public String getAcqCountryCode() {
		return acqCountryCode;
	}

	public void setAcqCountryCode(String acqCountryCode) {
		this.acqCountryCode = acqCountryCode;
	}

	public String getDebitAccNo() {
		return debitAccNo;
	}

	public void setDebitAccNo(String debitAccNo) {
		this.debitAccNo = debitAccNo;
	}

	public String getF55Res() {
		return f55Res;
	}

	public void setF55Res(String f55Res) {
		this.f55Res = f55Res;
	}

	public String getLimitUsed() {
		return limitUsed;
	}

	public void setLimitUsed(String limitUsed) {
		this.limitUsed = limitUsed;
	}

	public String getTranxIdentifier() {
		return tranxIdentifier;
	}

	public void setTranxIdentifier(String tranxIdentifier) {
		this.tranxIdentifier = tranxIdentifier;
	}

	public String getPartialAmt() {
		return partialAmt;
	}

	public void setPartialAmt(String partialAmt) {
		this.partialAmt = partialAmt;
	}

	public String getTranxCurrConvAmt() {
		return tranxCurrConvAmt;
	}

	public void setTranxCurrConvAmt(String tranxCurrConvAmt) {
		this.tranxCurrConvAmt = tranxCurrConvAmt;
	}

	public String getCurrDecimalPoint() {
		return currDecimalPoint;
	}

	public void setCurrDecimalPoint(String currDecimalPoint) {
		this.currDecimalPoint = currDecimalPoint;
	}

	public String getTranxCodeSubType() {
		return tranxCodeSubType;
	}

	public void setTranxCodeSubType(String tranxCodeSubType) {
		this.tranxCodeSubType = tranxCodeSubType;
	}
}