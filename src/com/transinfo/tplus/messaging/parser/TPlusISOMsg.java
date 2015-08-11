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

package com.transinfo.tplus.messaging.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusUtility;
import com.transinfo.tplus.javabean.CardInfo;
import com.transinfo.tplus.javabean.TranxInfo;
import com.transinfo.tplus.log.TransactionDataBean;
import com.transinfo.tplus.util.StringUtil;


@SuppressWarnings({"unused","unchecked"})
public class TPlusISOMsg
{

	private ISOMsg objISO = null;
	private ISOMsg objCloneISO = null;
	private String strISOString = null;
	private String strTag;
	private String strHeader;
	private String strOrgProcessCode;
	private String strOrgMTI;
	private String strIssuerMTI;
	private int intResTo;
	private String strMTI2;
	private String strAuthComplete = "Y";
	private String strCapture = "Y";
	private String strDeleted = "N";
	private String strAdjusted = "N";
	private String strSettled = "N";
	private String strReconciliation = "=";
	private String strEMVExists = "";
	private String strTC = "";
	private boolean boolParseStatus = false;
	private boolean boolTranxLog = true;

	private HashMap objSubFieldMap = new HashMap();
	private HashMap objFieldMap = new HashMap();
	private String strMsgType="";
	private String strIssuer = null;

	private String strTranxType = null;
	private String strTranxCurr = null;
	private String strReqHandler = null;
	private String strSource = null;
	private String strDestination = null;
	private String strSourceBits = null;
	private String strDestinationBits = null;
	private String strIssuerHost = null;
	private String strIssuerPort = null;
	private String strConnName = null;
	private String strClass=null;
	private String strSignOnClass = null;
	private String strSignOffClass = null;
	private String strSignOnNeeded =null;
	private String strCurrCode = null;
	private String strStationId = null;
	private String strRefNo = null;
	private String strOrgTraceNo = null;
	private String strAcqInstId = null;
	private String strMerchantRefno=null;
	private String strMerchantName=null;
	private String strTerminalRefno=null;
	private String strBase2Code=null;
	private String strMCC = null;
	private String strDestAmt=null;
	private String strDestCurr=null;
	private String strCardTypeId=null;
	private String strF55=null;
	private String strF55Res=null;
	private String strF55Exists=null;

	private String strConnectionId=null;
	private String strTranxDateTime=null;
	private String strTranxTime=null;
	private String strTranxDate=null;
	private String strAutoSettled="N";
	private String strVisaEnable="N";

	private CardInfo objCardInfoBean=null;
	private String strTopUpTranxId = null;
	private TransactionDataBean objTransactionDataBean = null;

	private String tranxAmount = "";
	private String tranxCurrency = "";
	private String tranxSettledAmt = "";
	private String tranxCurr = "";
	private String tranxSettledCurr = "";
	private String tranxCHAmt = "";
	private String tranxFee = "";
	private String track2Data = "";
	private String deleted = "";
	private String settled = "";

	private String isAuthComplete = "";
	private String visaCutOff = "";
	private String F90 = "";
	private String acqBinType = "";
	private String description = "";
	private String strF44="5           ";

	private String strJcbF44=""; // '  U' value will be assigned on validation places

	TranxInfo objTranxInfo = null;

	private String strCardProduct="JC";
	private String strEMVKey="";
	private String strTranxCnt="";
	private String strARQC="";

	private String strF61="000000000000000000000033   00033";

	private String strF60="";

	private boolean isEComTranx = false;
	private boolean isAccountVerification = false;

	public TPlusISOMsg()
	{

		//objISO = new ISOMsg();
	}


	public ISOMsg clone()
	{
		return (ISOMsg)objISO.clone();

	}

	public ISOMsg getCloneISO()
	{
		return objCloneISO;
	}

	public void setCloneISO(ISOMsg objCloneISO)
	{
		this.objCloneISO = objCloneISO;
	}

	public Object getISOObject(int fld)throws ISOException
	{
		System.out.println("returning  getISOObject(int fld)   ");
		return objISO.getValue(fld);
	}

	public void setMsgObject(ISOMsg objISO)
	{

		this.objISO = objISO;
	}


	public ISOMsg getMsgObject()
	{
		return objISO;
	}

	public void setMsgType(String strMsgType)
	{
		this.strMsgType = strMsgType;
	}


	public String getMsgType()
	{
		return strMsgType;
	}


	public TransactionDataBean getTransactionDataBean()
	{
		return objTransactionDataBean;
	}

	public void setTransactionDataBean(TransactionDataBean objTransactionDataBean)
	{
		this.objTransactionDataBean= objTransactionDataBean;
	}

	public void setMCC(String strMCC)
	{
		this.strMCC = strMCC;
	}


	public String getMCC()
	{
		return strMCC;
	}


	public String getDestAmt()
	{
		return strDestAmt;
	}

	public void setDestAmt(String strDestAmt)
	{
		this.strDestAmt = strDestAmt;
	}

	public String getDestCurr()
	{
		return strDestCurr;
	}

	public void setDestCurr(String strDestCurr)
	{
		this.strDestCurr = strDestCurr;
	}


	public String getTranxDateTime()
	{
		return strTranxDateTime;
	}

	public void setTranxDateTime(String strTranxDateTime)
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

	public void setBase2Code(String strBase2Code)
	{
		this.strBase2Code = strBase2Code;
	}


	public String getBase2Code()
	{
		return strBase2Code;
	}

	public String getAcqInstId()
	{
		return strAcqInstId;
	}

	public void setAcqInstId(String strAcqInstId)
	{
		this.strAcqInstId = strAcqInstId;
	}

	public String getMerchantRefNo()
	{
		return strMerchantRefno;
	}

	public void setMerchantRefNo(String strMerchantRefno)
	{
		this.strMerchantRefno = strMerchantRefno;
	}

	public String getMerchantName()
	{
		return strMerchantName;
	}

	public void setMerchantName(String strMerchantName)
	{
		this.strMerchantName = strMerchantName;
	}


	public String getF55Req()
	{
		return strF55;
	}

	public void setF55Req(String strF55)
	{
		this.strF55 = strF55;
	}



	public String getF55Res()
	{
		return strF55Res;
	}

	public void setF55Res(String strF55Res)
	{
		this.strF55Res = strF55Res;

	}


	public String getTerminalRefNo()
	{
		return strTerminalRefno;
	}

	public void setTerminalRefNo(String strTerminalRefno)
	{
		this.strTerminalRefno = strTerminalRefno;
	}


	public void setIssuer(String strIssuer)
	{
		this.strIssuer = strIssuer;
	}


	public String getIssuer()
	{

		return strIssuer;
	}

	public void setTranxCurr(String strTranxCurr)
	{
		this.strTranxCurr = strTranxCurr;
	}


	public String getTranxCurr()
	{
		return strTranxCurr;
	}


	public void setTranxType(String strTranxType)
	{
		this.strTranxType = strTranxType;
	}


	public String getTranxType()
	{
		return strTranxType;
	}

	public void setReqHandler(String strReqHandler)
	{
		this.strReqHandler = strReqHandler;
	}


	public String getReqHandler()
	{
		return strReqHandler;
	}

	public void setSource(String strSource)
	{
		this.strSource = strSource;
	}


	public String getSource()
	{

		return strSource;
	}


	public void setDestination(String strDestination)
	{
		this.strDestination = strDestination;
	}


	public String getDestination()
	{
		return strDestination;
	}

	public void setSourceBits(String strSourceBits)
	{
		this.strSourceBits = strSourceBits;
	}


	public String getSourceBits()
	{
		return strSourceBits;
	}

	public void setDestinationBits(String strDestinationBits)
	{
		this.strDestinationBits = strDestinationBits;
	}


	public String getDestinationBits()
	{

		return strDestinationBits;
	}

	public void setIssuerHost(String strIssuerHost)
	{
		this.strIssuerHost = strIssuerHost;
	}


	public String getIssuerHost()
	{
		return strIssuerHost;
	}

	public void setIssuerPort(String strIssuerPort)
	{
		this.strIssuerPort = strIssuerPort;
	}


	public String getIssuerPort()
	{
		return strIssuerPort;
	}



	public String getAutoSettled()
	{
		return strAutoSettled;
	}

	public void setAutoSettled(String strAutoSettled)
	{
		this.strAutoSettled = strAutoSettled;
	}


	public String getVisaEnable()
	{
		return strVisaEnable;
	}

	public void setVisaEnable(String strVisaEnable)
	{
		this.strVisaEnable = strVisaEnable;
	}


	public void setConnectionId(String strConnectionId)
	{
		this.strConnectionId = strConnectionId;
	}


	public String getConnectionId()
	{
		return strConnectionId;
	}


	public void setConnectionName(String strConnName)
	{
		this.strConnName = strConnName;
	}


	public String getConnectionName()
	{
		return strConnName;
	}

	public void setClassName(String strClass)
	{
		this.strClass = strClass;
	}


	public String getClassName()
	{
		return strClass;
	}


	public void setSignOnClass(String strClass)
	{
		this.strSignOnClass = strClass;
	}


	public String getSignOnClass()
	{
		return strSignOnClass;
	}


	public void setSignOffClass(String strClass)
	{
		this.strSignOffClass = strClass;
	}


	public String getSignOffClass()
	{
		return strSignOffClass;
	}


	public void setSignOnNeeded(String strSignOnNeeded)
	{
		this.strSignOnNeeded = strSignOnNeeded;
	}


	public String getSignOnNeeded()
	{
		return strSignOnNeeded;
	}


	public void setCurrCode(String strCurrCode)
	{
		this.strCurrCode = strCurrCode;
	}


	public String getCurrCode()
	{
		return strCurrCode;
	}


	public void setStationId(String strStationId)
	{
		this.strStationId = strStationId;
	}


	public String getStationId()
	{
		return strStationId;
	}



	/**
	 * set MTI value to the ISO message
	 * @param strMTI
	 * @returns none
	 */

	public void setMTI(String strMTI) throws TPlusException
	{

		try
		{

			objISO.setMTI(strMTI);
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}
	}

	/**
	 * get MTI value of the ISO message
	 * @param none
	 * @returns String
	 */

	public String getMTI()throws TPlusException
	{
		try
		{
			return objISO.getMTI();

		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}
	}


	/**
	 * set MTI value to the ISO message
	 * @param strMTI
	 * @returns none
	 */

	public void setResponseMTI(String strResMTI) throws TPlusException
	{

		try
		{

			objISO.setMTI(strResMTI);
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR, " Error:"+isoExp.getMessage(),isoExp.getMessage());
		}
	}

	/**
	 * get MTI value of the ISO message
	 * @param none
	 * @returns String
	 */

	public void setResponseMTI()throws TPlusException
	{
		try
		{

			objISO.setResponseMTI();
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}
	}



	/**
	 * set a value to the ISO message Field
	 * @param strFiledNo,strValue Binary Value
	 * @returns none
	 */

	public void setBinaryValue(int intFieldNo,String strValue)throws TPlusException
	{

		try
		{
			objISO.set(intFieldNo,hex2byte(strValue));
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}

	/**
	 * set a value to the ISO message Field
	 * @param strFiledNo,strValue Binary Value
	 * @returns none
	 */

	public void setBinaryValue(int intFieldNo,byte[] strValue)throws TPlusException
	{

		try
		{

			objISO.set(intFieldNo,strValue);
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}


	/**
	 * set a value to the ISO message Field
	 * @param strFiledNo,strValue
	 * @returns none
	 */

	public void setValue(int intFieldNo,String strValue) throws TPlusException
	{

		try
		{
			objISO.set(intFieldNo,strValue);
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}

	/**
	 * return a value to the associated specified Field
	 * @param strFiledNo
	 * @returns String
	 */

	public String getValue(int intFieldNo)
	{

		return objISO.getString(intFieldNo);
	}

	public byte[] getBinaryValue(int intFieldNo)
	{
		return objISO.getBytes(intFieldNo);

	}


	/**
	 * set a value to the ISO message Field
	 * @param strFiledNo,strValue
	 * @returns none
	 */

	public byte[] getBinaryValue(String strValue)throws TPlusException
	{

		try
		{
			return ISOUtil.hex2byte(strValue);
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}


	public int getFieldType(int intFieldNo)
	{
		try
		{
			if(objISO.getValue(intFieldNo) == null)
				return 0;
			if(objISO.getValue(intFieldNo) instanceof byte[])
				return 1;

		}
		catch(ISOException isoexp)
		{
			System.out.println("Error at getFieldType="+isoexp);
		}
		return 2;
	}


	public void setCardDataBean(CardInfo objCardInfoBean)
	{
		this.objCardInfoBean = objCardInfoBean;
	}

	public CardInfo getCardDataBean()
	{
		return objCardInfoBean;
	}



	/**
	 * unset a ISOComponent to the ISO Field
	 * @param strFiledNo,objComponent
	 * @returns none
	 */

	public void unset(int fld)throws TPlusException
	{

		try
		{
			objISO.unset(fld);
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}


	/**
	 * unset a ISOComponent to the ISO Field
	 * @param strFiledNo,objComponent
	 * @returns none
	 */

	public void unset(int fld[])throws TPlusException
	{

		try
		{
			objISO.unset(fld);
		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}


	public void unsetFieldsOnResponse(IParser objISOParser)throws TPlusException
	{

		try
		{

			String cardScheme = getCardScheme();

			if("CU".equals(cardScheme))
			{

				if(objISOParser.getValue(3).substring(0,2).equals("30") || objISOParser.getValue(3).substring(0,2).equals("31")){
					objISO.unset(38);
					objISO.unset(44);
				}

				int unsetFld[] = {5,6,9,10,16,22,26,35,36,43,44,45,48,50,51,52,53,90,122};
				objISO.unset(unsetFld);

			} else if("JC".equals(cardScheme)) {

				if(("0420".equals(objISOParser.getMTI())) || ("0421".equals(objISOParser.getMTI()))){

					objISO.unset(38);
					//objISO.unset(44);

				}else{

					objISO.unset(95);

					/*if(("0200".equals(objISOParser.getMTI())) || ("0220".equals(objISOParser.getMTI()))){

						objISO.unset(6);
						objISO.unset(44);

					}

					if(objISOParser.getValue(3).substring(0,2).equals("30")){
						objISO.unset(44);
					}*/

				}

				int unsetFld[] = {5,6,9,10,16,18,22,23,25,26,35,36,41,43,45,48,50,51,52,53,60,61,122};
				objISO.unset(unsetFld);

			} else {

				int unsetFld[] = {6,10,14,18,22,26,28,45,51,52,53,61,62,126};
				objISO.unset(unsetFld);

			}

		}
		catch(Exception isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}


	/**
	 * set the header ISO message
	 * @param strHeader
	 * @returns none
	 */

	public void setHeader(String strHeader)
	{
		this.strHeader = strHeader;
	}

	/**
	 * get the header ISO message
	 * @param none
	 * @returns byte[]
	 */

	public String getHeader()
	{
		return strHeader;
	}



	/**
	 * get the header ISO message
	 * @param none
	 * @returns byte[]
	 */

	public String getResponseHeader()
	{
		strHeader = strHeader.substring(0,2)+strHeader.substring(6,10)+strHeader.substring(2,6);
		return strHeader;
	}


	/**
	 * check the input fields are available in the ISO message
	 * @param none
	 * @returns boolean
	 */

	public boolean hasFields(int[] fields)
	{
		return objISO.hasFields(fields);
	}


	/**
	 * check the input fields are available in the ISO message
	 * @param none
	 * @returns boolean
	 */

	public boolean hasField(int field)
	{
		return objISO.hasField(field);
	}

	/**
	 * move the value of one field to another field in the ISO message
	 * @param oldField,newField
	 * @returns void
	 */

	public void move(int oldField,int newField)throws TPlusException
	{
		try
		{
			objISO.move(oldField,newField);
		}
		catch(ISOException isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}


	/**
	 * Check whether the message is a Retransmission message
	 * @param none
	 * @returns boolean
	 */

	public boolean isRetransmission()throws TPlusException
	{
		try
		{
			return objISO.isRetransmission();
		}
		catch(ISOException isoExp)
		{
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}
	}

	/**
	 * This method returns the cardnumber from Field[2] or TrackData of the message.
	 * If the field[2] of the ISO message is not present then get the data from the track2data or
	 * track1data elements
	 * @param none
	 * @return String.If cardnumber not found then it return "".
	 */

	public String getCardNumber()
	{

		if (getValue(TPlusISOCode.CARD_NUMBER) !=null && getValue(TPlusISOCode.CARD_NUMBER) != "")
			return getValue(TPlusISOCode.CARD_NUMBER);

		int pos =0;

		String track2Data = getValue(TPlusISOCode.TRACK2DATA);		// find in Track 2

		if(track2Data != null)
		{
			track2Data.replace('D','=');

			pos = track2Data != null ? track2Data.indexOf("=") : 0;

			if (pos > 0)
			{
				String cardno = track2Data.substring(0, pos);
				return cardno;
			}
		}


		String track1Data = getValue(TPlusISOCode.TRACK1DATA);		// find in Track 1
		if(track1Data != null)
		{
			track1Data.replace('D','=');
			pos = track1Data != null ? track1Data.indexOf("^") : 0;

			if (pos > 0)
				//return track1Data.substring(0, pos);
				return track1Data.substring(pos-16, pos);
		}

		return null;
	}

	public void getF60Value() throws TPlusException
	{

		try {

			// check card scheme and assign f60
			String cardScheme = getCardScheme();

			// ONUS or OFFUS
			String tranxType = getTransactionDataBean().getAcqBinType();

			if("OFFUS".equals(tranxType)){

				if("CU".equals(cardScheme)){

					String f60 = objISO.getString(60);;

					if(f60 != null){

						if(f60.length() != 30){

							f60 = f60+"000000000000000";
							f60 = f60.substring(0,27);

							// assign 60.3.9 & 60.3.10
							f60 = f60+"000";

						}else{
							f60 = f60.substring(0,27);

							// assign 60.3.9 & 60.3.10
							f60 = f60+"000";
						}
					}else{
						f60 = StringUtil.getZero(27);

						// assign 60.3.9 & 60.3.10
						f60 = f60+"000";
					}

					objISO.set(60, f60);

					// to assign when get exception
					setStrF60(f60);

				}

			}

		} catch (Exception isoExp) {
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}

	public void getF61Value() throws TPlusException
	{

		try {

			// check card scheme and assign f60
			String cardScheme = getCardScheme();

			if("CU".equals(cardScheme)){

				String reqF61 = objISO.getString(61);;

				if(reqF61 != null && !"".equals(reqF61)){
					String secValue = "";
					if(reqF61.length()>32){
						secValue = reqF61.substring(32, reqF61.length());
					}
					strF61 = strF61+secValue;
				}

				objISO.set(61, strF61);

				// to assign when get exception
				setStrF61(strF61);

			}

		} catch (Exception isoExp) {
			throw new TPlusException(TPlusCodes.PARAM_SET_ERR,TPlusCodes.getErrorDesc(TPlusCodes.PARAM_SET_ERR)+". Error:"+isoExp.getMessage(),isoExp.getMessage());
		}

	}

	public String getCardScheme() throws TPlusException
	{

		List visaBins = new ArrayList();
		visaBins.add("470530");
		visaBins.add("470531");
		visaBins.add("470532");

		List cupBins = new ArrayList();
		cupBins.add("624352");
		cupBins.add("624353");
		cupBins.add("624354");

		List jcbBins = new ArrayList();
		jcbBins.add("356970");
		jcbBins.add("356971");

		String cardScheme = getCardProduct();

		if(cardScheme == null || "".equals(cardScheme)){

			String cardNumber = getCardNumber();

			String cardBIN = cardNumber.substring(0, 6);
			if(visaBins.contains(cardBIN)){
				cardScheme = "VI";
			}else if(cupBins.contains(cardBIN)){
				cardScheme = "CU";
			}else if(jcbBins.contains(cardBIN)){
				cardScheme = "JC";
			}

		}

		return cardScheme;

	}

	/**
	 * This method returns the card ExpiryDate from Field[14] or TrackData of the message.
	 * If the field[14] of the ISO message is not present then get the data from the track1data or
	 * track2data elements
	 * @param none
	 * @return String.If ExpiryData not found then it return "".
	 */

	public String getExpiryDate() throws Exception
	{

		int pos =0;

		// expire date get from F35
		String track2Data = getValue(TPlusISOCode.TRACK2DATA);		// find in Track 2
		if(track2Data!=null && !track2Data.equals(""))
		{
			track2Data.replace('D','=');
			pos = track2Data != null ? track2Data.indexOf("=") : 0;

			if (pos > 0)
			{
				if(track2Data.length() < pos+5)
					return "";
				else
					return track2Data.substring(pos+1,pos+5);

			}
		}

		// expire date get from F14
		if (getValue(TPlusISOCode.EXPIRY_DATE) != null && ! getValue(TPlusISOCode.EXPIRY_DATE).equals(""))
			return getValue(TPlusISOCode.EXPIRY_DATE);


		// expire date get from F45
		String track1Data = getValue(TPlusISOCode.TRACK1DATA);		// find in Track 1
		if(track1Data!=null && !track2Data.equals(""))
		{

			track1Data.replace('D','=');

			pos = track1Data.lastIndexOf('^');
			if (pos > 0)
			{
				return track1Data.substring(pos+1, pos+5);
			}
		}

		return null;

	}


	/**
	 * This method returns the Card Type(Chip or MagStripe) from F35 of the message.
	 * @param none
	 * @return String.If ExpiryData not found then it return "".
	 */

	public String getCardType() throws Exception
	{

		int pos =0;

		String cardType = getValue(TPlusISOCode.TRACK2DATA);		// find in Track 2
		if(cardType != null)
		{
			cardType.replace('D','=');
			pos = cardType != null ? cardType.indexOf("=") : 0;

			if (pos > 0)
			{
				if(cardType.length() < pos+6)
					return "";
				else
					return cardType.substring(pos+5,pos+6);

			}

		}

		return "";

	}

	/**
	 * This method Format the Card Acceptor Name/Loc.
	 * @param Merchant Name, City ,Country
	 * @return String.
	 */

	public String getCardAccpt(String strMerName,String strCity,String strCntry)
	{

		int merLen = strMerName.length();
		int cityLen = strCity.length();

		if(merLen>25)
			strMerName = strMerName.substring(0,25);

		if(cityLen>13)
			strCity  = strCity.substring(0,13);

		for(int i=0;i<25-merLen;i++)
			strMerName = strMerName+" ";

		for(int i=0;i<13-cityLen;i++)
			strCity = strCity+" ";


		return strMerName+strCity+strCntry;

	}


	public String getCUPCardAccpt(String strMerName,String strCity,String strCntry)
	{
		int merLen = strMerName.length();
		int cityLen = strCity.length();

		if(merLen>25)
			strMerName = strMerName.substring(0,25);

		if(cityLen>12)
			strCity  = strCity.substring(0,12);

		for(int i=0;i<25-merLen;i++)
			strMerName = strMerName+" ";

		for(int i=0;i<12-cityLen;i++)
			strCity = strCity+" ";


		return strMerName+strCity+strCntry;

	}


	/**
	 * This method returns the service code of the message .
	 * The serive code is available in the Track2Data element.
	 * @param none
	 * @return String.If service code not found then it return "".
	 */

	public String getServiceCode()
	{
		String track2Data = getValue(TPlusISOCode.TRACK2DATA);		// find in Track 2
		int pos =  track2Data.indexOf("=");
		if(pos > 0)
		{
			return track2Data.substring(pos+5, pos+8);
		}

		return "";
	}


	/**
	 * This method is used to get the amount from the ISO message
	 * It check whether multiplecurrency participants is exists,If not then it returns
	 * Non-Multiple participants.
	 * @param none
	 * @return String.
	 */

	public String getAmount()
	{
		if (getValue(TPlusISOCode.TRANSACTION_AMOUNT) != null && getValue(TPlusISOCode.TRANSACTION_AMOUNT) != "")		// for Multicurrency Participants
		{
			return getValue(TPlusISOCode.TRANSACTION_AMOUNT);
		}
		return getValue(TPlusISOCode.BILLING_AMOUNT);			// for Non-Multicurrency Participants
	}


	/**
	 * This method is used to get the currency from the ISO message
	 * It check whether multiplecurrency participants is exists,If not then it returns
	 * Non-Multiple participants.
	 * @param none
	 * @return String.
	 */

	public String getCurrency()
	{
		if (getValue(TPlusISOCode.TRANSACTION_CURRENCY) != null && getValue(TPlusISOCode.TRANSACTION_CURRENCY) != "")		// for Multicurrency Participants
		{
			return getValue(TPlusISOCode.TRANSACTION_CURRENCY);
		}
		return getValue(TPlusISOCode.BILLING_CURRENCY);			// for Non-Multicurrency Participants
	}


	/** Getter for property capture.
	 * @return Value of property capture.
	 *
	 */
	public java.lang.String getAuthComplete() {
		return strAuthComplete;
	}

	/** Setter for property capture.
	 * @param capture New value of property capture.
	 *
	 */
	public void setAuthComplete(java.lang.String strAuthComplete) {
		this.strAuthComplete = strAuthComplete;
	}

	/** Getter for property capture.
	 * @return Value of property capture.
	 *
	 */
	public java.lang.String getCapture() {
		return strCapture;
	}

	/** Setter for property capture.
	 * @param capture New value of property capture.
	 *
	 */
	public void setCapture(java.lang.String capture) {
		this.strCapture = capture;
	}

	/** Getter for property deleted.
	 * @return Value of property deleted.
	 *
	 */
	public java.lang.String getDeleted() {
		return strDeleted;
	}

	/** Setter for property deleted.
	 * @param deleted New value of property deleted.
	 *
	 */
	public void setDeleted(java.lang.String deleted) {
		this.strDeleted = deleted;
	}

	/** Getter for property Adjusted.
	 * @return Value of property Deleted.
	 *
	 */
	public java.lang.String getAdjusted() {
		return strAdjusted;
	}

	/** Setter for property Adjusted.
	 * @param Deleted New value of property Adjusted.
	 *
	 */
	public void setAdjusted(java.lang.String strAdjusted) {
		this.strAdjusted = strAdjusted;
	}


	/** Getter for property Reconciliation.
	 * @return Value of property Reconciliation.
	 *
	 */
	public java.lang.String getReconciliation() {
		return strReconciliation;
	}

	/** Setter for property Reconciliation.
	 * @param capture New value of property Reconciliation.
	 *
	 */
	public void setReconciliation(java.lang.String strReconciliation) {
		this.strReconciliation = strReconciliation;
	}


	/** Getter for property OrgTraceNo.
	 * @return Value of property OrgTraceNo.
	 *
	 */
	public java.lang.String getOrgTraceNo() {
		return strOrgTraceNo;
	}

	/** Setter for property OrgTraceNo.
	 * @param deleted New value of property OrgTraceNo.
	 *
	 */
	public void setOrgTraceNo(java.lang.String strOrgTraceNo) {
		this.strOrgTraceNo = strOrgTraceNo;
	}



	/** Getter for property RefNo.
	 * @return Value of property RefNo.
	 *
	 */
	public java.lang.String getRefNo() {
		return strRefNo;
	}

	/** Setter for property RefNo.
	 * @param deleted New value of property RefNo.
	 *
	 */
	public void setRefNo(java.lang.String strRefNo) {
		this.strRefNo = strRefNo;
	}



	/** Getter for property EMVExists.
	 * @return Value of property EMVExists.
	 *
	 */
	public java.lang.String getEMVExists() {
		return strEMVExists;
	}

	/** Setter for property EMVExists.
	 * @param EMVExists New value of property EMVExists.
	 *
	 */
	public void setEMVExists(java.lang.String EMVExists) {
		this.strEMVExists = EMVExists;
	}

	/** Getter for property settled.
	 * @return Value of property settled.
	 *
	 */
	public java.lang.String getSettled() {
		return strSettled;
	}

	/** Setter for property settled.
	 * @param settled New value of property settled.
	 *
	 */
	public void setSettled(java.lang.String settled) {
		this.strSettled = settled;
	}


	/** Getter for property strTC.
	 * @return Value of property strTC.
	 *
	 */
	public java.lang.String getTC() {
		return strTC;
	}

	/** Setter for property strTC.
	 * @param strTC New value of property settled.
	 *
	 */
	public void setTC(java.lang.String settled) {
		this.strTC = settled;
	}


	/** Getter for property boolTranxLog.
	 * @return Value of property boolTranxLog.
	 *
	 */
	public boolean isTranxLog() {
		return boolTranxLog;
	}

	/** Setter for property boolTranxLog.
	 * @param boolTranxLog To log Tranx record.
	 *
	 */
	public void setTranxLog(boolean boolTranxLog) {
		this.boolTranxLog = boolTranxLog;
	}


	/**
	 *This method is used to return intResTo
	 *
	 * @param none
	 * @returns String
	 */

	public int getSendTo()
	{
		return intResTo;


	}


	/**
	 *This method is used to set intResTo
	 *
	 * @param string
	 * @returns none
	 */

	public void setSendTo(int intResTo)
	{
		this.intResTo = intResTo;


	}

	/**
	 *This method is used to return strOrgMTI
	 *
	 * @param none
	 * @returns String
	 */

	public String getOrgMTI()
	{
		return strOrgMTI;


	}


	/**
	 *This method is used to set strMTI2
	 *
	 * @param string
	 * @returns none
	 */

	public void setOrgMTI(String strOrgMTI)
	{
		this.strOrgMTI = strOrgMTI;


	}


	/**
	 *This method is used to return strMTI2
	 *
	 * @param none
	 * @returns String
	 */

	public String getMTI2()
	{
		return strMTI2;


	}


	/**
	 *This method is used to set strMTI2
	 *
	 * @param string
	 * @returns none
	 */

	public void setMTI2(String strMTI2)
	{
		this.strMTI2 = strMTI2;


	}

	/**
	 *This method is used to return strOrgProcessCode
	 *
	 * @param none
	 * @returns String
	 */

	public String getOrginalProcessingCode()
	{
		return strOrgProcessCode;
	}


	/**
	 *This method is used to set strOrgProcessCode
	 *
	 * @param string
	 * @returns none
	 */

	public void setOrginalProcessingCode(String strOrgProcessCode)
	{
		this.strOrgProcessCode = strOrgProcessCode;
	}



	/**
	 *This method is used to fill the blank space to the right of the string
	 *
	 * @param String,length
	 * @returns String
	 */

	public String fillRightSpace(String strString,int len)
	{
		return ISOUtil.strpad(strString,len);
	}

	/**
	 * This method is used to convert the String to HEX
	 *
	 * @param String
	 * @returns String
	 */

	public String hex2Str(String strString)
	{
		if(strString !=null)
			return ISOUtil.hexString(strString.getBytes());
		else
			return strString;
	}


	/**
	 * This method is used to convert the Hex to Byte
	 *
	 * @param String
	 * @returns String
	 */

	public byte[] hex2byte(String strHexString)
	{
		//if(strHexString !=null)
		return ISOUtil.hex2byte(strHexString);

	}


	/**
	 *This method is used to convert the String to Integer
	 *
	 * @param  String
	 * @returns int
	 */

	public int parseInt(String strNumber)throws NumberFormatException
	{
		return ISOUtil.parseInt(strNumber);
	}


	/**
	 * This method is used to convert the String to Long
	 *
	 * @param  String
	 * @returns int
	 */

	public long parseLong(String strNumber)throws NumberFormatException
	{
		return (new Long(strNumber)).longValue();

	}

	/**
	 * This method is used to convert the String to Double
	 *
	 * @param  String
	 * @returns int
	 */

	public double parseDouble(String strNumber)throws NumberFormatException
	{
		return (new Double(strNumber)).doubleValue();

	}


	/**
	 *This method is used to convert the BCD to ASC Format
	 *
	 * @param EBCDIC String
	 * @returns ASCII String
	 */

	public String ebcdic2Ascii(String strEBCDString)
	{
		if(strEBCDString !=null)
			return ISOUtil.ebcdicToAscii(strEBCDString.getBytes());
		else
			return strEBCDString	;
	}

	/**
	 *This method is used to convert the String to BCD Format
	 *
	 * @param String
	 * @returns BCD String
	 */

	public String Str2BCD(String strLen)
	{
		if(strLen !=null && !strLen.equals(""))
			return new String(ISOUtil.str2bcd(strLen,true));
		else
			return strLen	;
	}


	/**
	 * This method is used to make the string for the required length
	 *
	 * @param string ,string len
	 * @returns new string
	 */

	public String replicate(String str, int len)
	{
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<len; i++)
		{
			sb.append(str);

		}
		return sb.toString();
	}

	/**
	 * This method to provide the status of the message is parsed succesfully or not
	 */

	public boolean isParse()
	{
		return boolParseStatus;
	}

	public void setParse(boolean boolParseStatus)
	{
		this.boolParseStatus = boolParseStatus;
	}


	/**
	 * Covert the ISO message to log
	 * @param none
	 * @returns String
	 */

	public String toString()
	{
		return objISO.toString();
	}





	public String getVisaCutOff() {
		return visaCutOff;
	}
	public void setVisaCutOff(String visaCutOff) {
		this.visaCutOff= visaCutOff;
	}

	public String getF90() {
		return F90;
	}
	public void setF90(String F90) {
		this.F90= F90;
	}


	public String getF44() {
		return strF44;
	}
	public void setF44(String strF44) {
		this.strF44= strF44;
	}

	public String getCardProduct() {
		return strCardProduct;
	}
	public void setCardProduct(String strCardProduct) {
		this.strCardProduct= strCardProduct;
	}

	public String getEMVKey() {
		return strEMVKey;
	}
	public void setEMVKey(String strEMVKey) {
		this.strEMVKey= strEMVKey;
	}

	public String getTranxCnt() {
		return strTranxCnt;
	}
	public void setTranxCnt(String strTranxCnt) {
		this.strTranxCnt= strTranxCnt;
	}

	public String getARQC() {
		return strARQC;
	}
	public void setARQC(String strARQC) {
		this.strARQC= strARQC;
	}


	public void setTranxAmt(String tranxAmount)
	{
		this.tranxAmount = tranxAmount;
	}

	public String getTranxAmt()
	{
		return tranxAmount;
	}

	/*public void setTranxCurr(String tranxCurrency)
	{
		this.tranxCurrency = tranxCurrency;
	}

	public String getTranxCurr()
	{
		return tranxCurrency;
	}*/

	public String getTranxSettledAmt() {
		return tranxSettledAmt;
	}
	public void setTranxSettledAmt(String tranxSettledAmt) {
		this.tranxSettledAmt = tranxSettledAmt;
	}

	public String getTranxSettledCurr() {
		return tranxSettledCurr;
	}
	public void setTranxSettledCurr(String tranxSettledCurr) {
		this.tranxSettledCurr = tranxSettledCurr;
	}
	public String getTranxCHAmt() {
		return tranxCHAmt;
	}
	public void setTranxCHAmt(String tranxCHAmt) {
		this.tranxCHAmt = tranxCHAmt;
	}
	public String getTranxFee() {
		return tranxFee;
	}
	public void setTranxFee(String tranxFee) {
		this.tranxFee = tranxFee;
	}



	public String getIsAuthComplete() {
		return isAuthComplete;
	}
	public void setIsAuthComplete(String isAuthComplete) {
		this.isAuthComplete = isAuthComplete;
	}


	public String getAcqBinType() {
		return acqBinType;
	}
	public void setAcqBinType(String acqBinType) {
		this.acqBinType = acqBinType;
	}

	public String getRemarks() {
		return description;
	}
	public void setRemarks(String description) {
		this.description = description;
	}




	/**
	 * Appends two bytes array into one.
	 *
	 * @param a A byte[].
	 * @param b A byte[].
	 * @return A byte[].
	 */
	public static byte[] appendBytes(byte[] a, byte[] b) {
		byte[] z = new byte[a.length + b.length];
		System.arraycopy(a, 0, z, 0, a.length);
		System.arraycopy(b, 0, z, a.length, b.length);
		return z;
	}


	public static void main(String s[])throws Exception
	{
		TPlusISOMsg msg = new TPlusISOMsg();
		System.out.println(msg.getCardType());
	}

	public static String getRandomApprovalCode(){
		int i = (int)(Math.random() * 1000000);
		String ret = "" + i;
		while (ret.length() < 6){
			ret = "0" + ret;
		}
		return ret;
	}

	/*public static String getRandomApprovalCode(){
		int i = (int)(Math.random() * 100000);
		String ret = "" + i;
		while (ret.length() < 5){
			ret = "0" + ret;
		}
		return "T"+ret;
	}*/


	public String getStrF55Exists() {
		return strF55Exists;
	}


	public void setStrF55Exists(String strF55Exists) {
		this.strF55Exists = strF55Exists;
	}


	public TranxInfo getObjTranxInfo() {
		return objTranxInfo;
	}


	public void setObjTranxInfo(TranxInfo objTranxInfo) {
		this.objTranxInfo = objTranxInfo;
	}


	public String getStrF61() {
		return strF61;
	}


	public void setStrF61(String strF61) {
		this.strF61 = strF61;
	}


	public boolean isEComTranx() {
		return isEComTranx;
	}


	public void setEComTranx(boolean isEComTranx) {
		this.isEComTranx = isEComTranx;
	}


	public boolean isAccountVerification() {
		return isAccountVerification;
	}


	public void setAccountVerification(boolean isAccountVerification) {
		this.isAccountVerification = isAccountVerification;
	}


	public String getStrF60() {
		return strF60;
	}


	public void setStrF60(String strF60) {
		this.strF60 = strF60;
	}


	public String getStrJcbF44() {
		return strJcbF44;
	}


	public void setStrJcbF44(String strJcbF44) {
		this.strJcbF44 = strJcbF44;
	}

}

