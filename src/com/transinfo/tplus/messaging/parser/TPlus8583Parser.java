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

import java.io.PrintStream;
import java.util.HashMap;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.debug.DebugWriter;



public class TPlus8583Parser extends IParser
{


	private static GenericPackager objGenPac =null;
	private HashMap objFldMap = new HashMap();
	private ISOMsg objISO = null;
	private String strHeader="";
	private String strISOString="";


	public static void main(String s[]) throws TPlusException
	{
		TPlus8583Parser parser = new TPlus8583Parser();

		String val = "00B200000093930700000000000000000000000200723C658128E09000104665310000000118301000000000000000072303373401932711373407232108581204180510001002064377331B04665310000000118D2108201717F0F7F2F3F0F0F0F0F0F0F0F1F2F3F4F0F0F0F2F0F6F6F8F8F9F9F5F8F1F2F0F0F0F0F1D9C1D4C2D640D9C5E2E3C1E4D9C1D5E3404040404040404040D3E4C1D5C7D5C1D4E3C8C14040D3C10840F2BEAE58C728432D";
		parser.parse(val);
		System.out.println(ISOUtil.bcd2str("0203".getBytes(),0,"0203".length(),true));
		int i =203;
		System.out.println(Integer.toHexString(203));




	}


	/**
	 * It parse incoming ISO message and parse against the configXML.
	 * @param raw ISO msg
	 * @return none
	 */


	public void parse(String strISOString) throws TPlusException
	{

		try
		{
			// Strip any trailling white space

			this.strISOString = strISOString;

			TPlusPrintOutput.printMessage("TPlus8583Parser","PARSING ISO MESSAGE "+strISOString);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlus8583Parser:PARSING ISO MESSAGE "+strISOString);

			byte[] hexbytes = strISOString.getBytes();
			int nbytes =hexbytes.length;
			byte byteISO[] = ISOUtil.hex2byte (hexbytes,0,nbytes/2);

			// And upack it into an ISOMsg
			objISO = new ISOMsg();
			System.out.println("TPlusConfig="+TPlusConfig.getISOConfig());
			objISO.setPackager (new GenericPackager(TPlusConfig.getISOConfig()));
			//objISO.setPackager ( new com.transinfo.tplus.messaging.mux.VSDCPackager());
			int val = objISO.unpack(byteISO);
			objISO.dump (new PrintStream(System.out), "0");
			DebugWriter.writeMsgDump(objISO);


			System.out.println("1");
			this.setMsgObject(objISO);
			System.out.println("2");
			this.setParse(true);
			System.out.println("3");
			this.setMsgType("ISO");
			System.out.println("4");
			this.setHeader(strHeader);
			System.out.println("5");

		}
		catch(ISOException isoExp)
		{
			System.out.println(isoExp);
			isoExp.printStackTrace ();
			String strExceptionMessage = isoExp.getMessage();
			throw new TPlusException(TPlusCodes.ISO_PARSING_FATAL," Error:while Unpacking "+isoExp.getMessage());
		}
		catch (Exception exp)
		{
			System.out.println(exp);
			throw new TPlusException(TPlusCodes.REQ_PARSE_FAIL," Error:while Parsing Req-> Single Arg "+exp.toString());
		}

	}

	/**
	 * repack the unpack ISO message and return in bytes
	 * @param none
	 * @returns byte[]
	 * @throws TPlusException
	 */


	public byte[] repack() throws TPlusException
	{

		try
		{

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlus8583Parser:REPACK");
			ISOMsg objISO = this.getMsgObject();

			DebugWriter.writeMsgDump(objISO);
			objISO.dump (new PrintStream(System.out), "0");

			return createResponse(objISO.pack(),this.getHeader());
		}
		catch(ISOException isoExp)
		{
			isoExp.printStackTrace ();
			String strExceptionMessage = isoExp.getMessage();
			throw new TPlusException(TPlusCodes.ISO_PARSING_FATAL,TPlusCodes.getErrorDesc(TPlusCodes.ISO_PARSING_FATAL)+". Error:while packing"+isoExp);
		}
		catch (Exception exp)
		{
			throw new TPlusException(TPlusCodes.ISO_IOEXCEPTION,TPlusCodes.getErrorDesc(TPlusCodes.ISO_IOEXCEPTION)+". Error:while 1packing"+exp);
		}

	}




	/**
	 * repack the unpack ISO message and return in bytes
	 * @param none
	 * @returns byte[]
	 * @throws TPlusException
	 */


	public ISOMsg muxObj(byte[] muxBytes) throws TPlusException
	{

		try
		{
			TPlusPrintOutput.printMessage("TPlus8583Parser","muxObj String...."+ISOUtil.hexString(muxBytes));
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlus8583Parser:muxObj String ..."+ISOUtil.hexString(muxBytes));

			this.strISOString = ISOUtil.hexString(muxBytes);

			if(strISOString.length()<12)
			{
				throw new TPlusException(TPlusCodes.INVALID_REQUEST," Error: Request = "+strISOString );
			}
			else
			{

				strISOString = strISOString.substring(4);
			}


			ISOMsg objISO = new ISOMsg();
			objISO.setPackager (new GenericPackager(TPlusConfig.getISOConfig()));
			objISO.unpack(ISOUtil.hex2byte(strISOString));


			return objISO;
		}
		catch(ISOException isoExp)
		{
			isoExp.printStackTrace();
			String strExceptionMessage = isoExp.getMessage();
			throw new TPlusException(TPlusCodes.ISO_PARSING_FATAL,TPlusCodes.getErrorDesc(TPlusCodes.ISO_PARSING_FATAL)+". Error:while packing"+isoExp.getMessage(),isoExp.getMessage());
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
			throw new TPlusException(TPlusCodes.ISO_IOEXCEPTION,TPlusCodes.getErrorDesc(TPlusCodes.ISO_IOEXCEPTION)+". Error:while 1packing"+exp.getMessage(),exp.getMessage());
		}

	}

	public byte[] createResponse(byte[] isoBytes,String strHeader) throws TPlusException {
		byte strResponseMsg[] = null;

		try {

			if(DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusResponse:Creating Response .... :");
			if(isoBytes == null)
				if(DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusResponse:Response Bytes is null .... :");
			//strHeader = "6080000001";
			isoBytes= this.appendBytes(ISOUtil.hex2byte(strHeader),isoBytes);
			String strTempStatus = "0000" + isoBytes.length;
			byte hexMsgLen[] = ISOUtil.str2bcd(strTempStatus.substring(strTempStatus.length()-4),true);
			strResponseMsg = this.appendBytes(hexMsgLen,isoBytes);

		}
		catch(Exception e) {
			TPlusPrintOutput.printMessage("TPlus8583Parser","Error in generating the Error Response");
			throw new TPlusException(TPlusCodes.APPL_ERR," createResponse Error: "+ e.getMessage());
		}

		return strResponseMsg;
	}


	// For Visa
	/*public byte[] createResponse(byte[] isoBytes,String strHeader) throws TPlusException
{
   	byte strResponseMsg[] = null;

		try
		{

			if(DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusResponse:Creating Response .... :");
			if(isoBytes == null)
			if(DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusResponse:Response Bytes is null .... :");

			String hexlen = convertStringToHex((ISOUtil.hex2byte(strHeader).length-2)+isoBytes.length);
			strHeader = "000016010200"+hexlen+"4337330000000000000000000000000000";

			isoBytes= this.appendBytes(ISOUtil.hex2byte(strHeader),isoBytes);

			String strTempStatus = "0000" + hexlen;
			byte hexMsgLen[] = ISOUtil.hex2byte(strTempStatus.substring(strTempStatus.length()-4));
			//byte hexMsgLen1[]= ISOUtil.hex2byte(convertStringToHex(ISOUtil.hexString(hexMsgLen)));
			//byte hexMsgLen[]=ISOUtil.hex2byte("00C4");
			strResponseMsg = this.appendBytes(hexMsgLen,isoBytes);

		}
		catch(Exception e)
		{
			TPlusPrintOutput.printMessage("TPlus8583Parser","Error in generating the Error Response");
			throw new TPlusException(TPlusCodes.APPL_ERR," createResponse Error: "+ e.getMessage());
		}

   	  return strResponseMsg;
   }*/



	public static String convertStringToHex(int str){

		System.out.println("Hex Len="+str);

		String hex = Integer.toHexString(str);
		System.out.println("After Hex Len="+hex);
		return hex;


	}



	/**
	 * create new response ISO object by populating data with the actual ISO object.
	 * @param TPlusISOParser
	 * @returns TPlusISOParser (new Object)
	 * @throws TPlusException,Exception
	 */

	//TO BE CHECK AGAIN

	public String createResponseISO(IParser objISO) throws TPlusException,Exception
	{
		return "CHECK AGAIN";

	}

	/*
	public TPlusISOParser createResponseISO(TPlusISOParser objISO) throws TPlusException,Exception
	{


				if(DebugWriter.boolDebugEnabled) DebugWriter.write("TPlus8583Parser:createResponseISO");

				TPlus8583Parser resISO = new TPlus8583Parser();       // 2) create ISOMsg
				resISO.setPackager (TPlusConfig.getGenericPackager());

				// 4) populate ISOMsg
				resISO.set (TPlusISOCode.MTI,  objISO.getValue(TPlusISOCode.MTI));
				resISO.set (TPlusISOCode.PROCESSING_CODE,  objISO.getOrginalProcessingCode());
				resISO.set (TPlusISOCode.TRANSACTION_AMOUNT,  objISO.getValue(TPlusISOCode.TRANSACTION_AMOUNT));
				resISO.set (TPlusISOCode.SYSTEM_TRACE_NO,  objISO.getValue(TPlusISOCode.SYSTEM_TRACE_NO));
				resISO.set (TPlusISOCode.TRANSMISSION_TIME,  objISO.getValue(TPlusISOCode.TRANSMISSION_TIME));
				resISO.set (TPlusISOCode.TRANSMISSION_DATE,  objISO.getValue(TPlusISOCode.TRANSMISSION_DATE));
				resISO.set (TPlusISOCode.ACQ_COUNTRY_CODE,  objISO.getValue(TPlusISOCode.ACQ_COUNTRY_CODE));
				resISO.set (TPlusISOCode.CH_COUNTRY_CODE,  objISO.getValue(TPlusISOCode.CH_COUNTRY_CODE));
				resISO.set (TPlusISOCode.POS_CONDITION_CODE,  objISO.getValue(TPlusISOCode.POS_CONDITION_CODE));
				resISO.set (TPlusISOCode.ACQ_INST_IDEN_CODE,  objISO.getValue(TPlusISOCode.ACQ_INST_IDEN_CODE));
				resISO.set (TPlusISOCode.REF_NO,  objISO.getValue(TPlusISOCode.REF_NO));
				if(!objISO.getTranxType().equals("") && !objISO.getTranxType().equals("REVERSAL"))
				{
					resISO.set (TPlusISOCode.APPROVAL_CODE,  objISO.getValue(TPlusISOCode.APPROVAL_CODE));
				}
				resISO.set (TPlusISOCode.RESPONSE_CODE,  objISO.getValue(TPlusISOCode.RESPONSE_CODE));
				resISO.set (TPlusISOCode.TERMINAL_ID,  objISO.getValue(TPlusISOCode.TERMINAL_ID));
				resISO.set (TPlusISOCode.MERCHANT_ID,  objISO.getValue(TPlusISOCode.MERCHANT_ID));

				resISO.set (TPlusISOCode.ADDT_RES_DATA,  objISO.getValue(TPlusISOCode.ADDT_RES_DATA));
				resISO.set (TPlusISOCode.ADDITIONAL_DATA_PRIVATE,  objISO.getValue(TPlusISOCode.ADDITIONAL_DATA_PRIVATE));
				resISO.set (TPlusISOCode.TRANSACTION_CURRENCY,  objISO.getValue(TPlusISOCode.TRANSACTION_CURRENCY));

				resISO.set (TPlusISOCode.EMV,  objISO.getValue(TPlusISOCode.EMV));
				resISO.set (TPlusISOCode.PRIVATE_60,  objISO.getValue(TPlusISOCode.PRIVATE_60));
				resISO.set (TPlusISOCode.INVOICE_NO,  objISO.getValue(TPlusISOCode.INVOICE_NO));
				resISO.set (TPlusISOCode.ORG_DATA_ELEMENT,  objISO.getValue(TPlusISOCode.ORG_DATA_ELEMENT));
				resISO.set (TPlusISOCode.REPLACEMENT_AMT,  objISO.getValue(TPlusISOCode.REPLACEMENT_AMT));


				return resISO;
	}
	 */




}

