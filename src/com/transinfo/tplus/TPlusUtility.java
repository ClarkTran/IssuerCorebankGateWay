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

/**
 *This is in the MPIPackage
 */
package com.transinfo.tplus;

//Java specific imports
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.debug.DebugWriter;

import cryptix.util.core.Hex;


/**
 * This class contains all reusable functions..
 */
@SuppressWarnings({ "unused", "static-access" })
public class TPlusUtility
{
	static Random ranKey = new Random();

	/**
	 * This method checks the length of the parameter passed and creates a string of the particular length by stuffing characters.
	 * @param strData The data whose length has to be corrected.
	 * @param intLength The required length
	 * @param strStuff The stuff char or string
	 * @param boolPrefix Flag which shows if the stuffed characters are to be prefixed or postfixed
	 * @return String - The string with the corrected length
	 */

	public static String setLength(String strData, int intLength,String strStuff,boolean boolPrefix)
	{
		StringBuffer sbfData = new StringBuffer();

		sbfData.append(strData);

		while (sbfData.length()<intLength)
		{
			if (boolPrefix)
			{
				sbfData.insert(0,strStuff);
			}
			else {
				sbfData.append(strStuff);
			}
		}

		return sbfData.toString();
	}

	/**
	 * This method checks the length of the parameter passed and creates a string of the particular
	 * length by stuffing " " at the end of the string.
	 * @param strData The data whose length has to be corrected.
	 * @param intLength The required length
	 * @return String - The string with the corrected length
	 */
	public  static String setLength(String strData, int intLength)
	{
		return setLength(strData, intLength," ",false);
	}

	/**
	 * This method checks the length of the parameter passed and creates a string of the particular length by stuffing " ".
	 * @param strData The data whose length has to be corrected.
	 * @param intLength The required length
	 * @param boolPrefix Flag which shows if the stuffed characters are to be prefixed or postfixed
	 * @return String - The string with the corrected length
	 */
	public  static String setLength(String strData, int intLength,boolean boolPrefix)
	{
		return setLength(strData, intLength," ",boolPrefix);
	}

	/**
	 * This method checks if the date of the log file is the same as todays's date
	 * @param intDate The date at which the logfile was last created.
	 * @return  boolean - True if both the dates are same, false other wise
	 */
	public static boolean validateFileDate(int intDate)
	{
		Calendar calNow = Calendar.getInstance();
		int intToday = Integer.parseInt(calNow.get(Calendar.YEAR)+TPlusUtility.setLength(""+(calNow.get(Calendar.MONTH)+1),2,"0",true)+TPlusUtility.setLength(""+calNow.get(Calendar.DAY_OF_MONTH),2,"0",true));
		if (intToday != intDate)
			return false;
		return true;

	}

	/**
	 * This method encodes the input string to Base64 encoded and returns the encoded string.
	 * @param stringInput The string need to be Base64 encoded,
	 * @return String. Base64 encoded String.
	 * @throws TPlusException.
	 */
	public static String encodeBase64(String stringInput) throws TPlusException
	{
		String strEncoded = "";
		try
		{
			byte[] byteArray	=	stringInput.getBytes();
			strEncoded	=	new String(org.apache.xerces.utils.Base64.encode(byteArray));
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.EN_DEC_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.EN_DEC_FAIL)+" Error: " + e.getMessage());
		}

		return strEncoded;
	}

	/**
	 * This method encodes the input string to Base64 encoded and returns the encoded string.
	 * @param stringInput The string need to be Base64 encoded,
	 * @return String. Base64 encoded String.
	 * @throws TPlusException.
	 */
	public static String encodeBase64(byte[] bytearrInput) throws TPlusException
	{
		String strEncoded = "";
		try
		{
			strEncoded	=	new String(org.apache.xerces.utils.Base64.encode(bytearrInput));
		}
		catch (Exception e)
		{
			throw new TPlusException(TPlusCodes.EN_DEC_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.EN_DEC_FAIL)+" Error: " + e.getMessage());
		}

		return strEncoded;
	}
	/**
	 * This method decodes the input string to Base64 encoded and returns the encoded string.
	 * @param stringInput The string need to be decoded from Base64,
	 * @return String. ASCII String decode from base 64.
	 * @throws TPlusException.
	 */
	public static String decodeBase64(String stringInput) throws TPlusException
	{

		String strEncoded = "";
		try
		{
			byte[] byteArray	=	stringInput.getBytes();
			org.apache.xerces.utils.Base64 base64	=	new  org.apache.xerces.utils.Base64();
			strEncoded	=	new String(base64.decode(byteArray));

		}
		catch (Exception e)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusUtility: Decode failed. Error Data:"+strEncoded);
			throw new TPlusException(TPlusCodes.EN_DEC_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.EN_DEC_FAIL)+" Error: " + e.getMessage());
		}
		return strEncoded;
	}


	public static String generateSNO()
	{
		int intRanSNo = ranKey.nextInt(99999);
		return setLength(Integer.toString(intRanSNo), 5,"0",true);
	}


	/*
    public static String getDecodedandInflated(String strSignedXML) throws TPlusException
    {
		String result=null;
		     byte[] decode=null;
		      //decode
		      try{
		      org.apache.xerces.utils.Base64 base64=new  org.apache.xerces.utils.Base64();
		      decode=base64.decode(strSignedXML.getBytes());
		      }catch(Exception e){
		         return "11";
		      }
		      try{
		    //decompression
		    java.io.ByteArrayInputStream is = new java.io.ByteArrayInputStream(decode);
		    java.io.Reader reader = new java.io.InputStreamReader(new java.util.zip.InflaterInputStream(is,new java.util.zip.Inflater(true)));

		    char[] charData = new char[decode.length*3];
		    int numRead = reader.read(charData,0,decode.length*3);

		    result = new String(charData).trim();
		    reader.close();
		   }catch(Exception e){
		       return "12";
     	  }

     	  return result;
	}
	 */

	/** Function to format the an date to the xml date format
	 * @param String
	 * @return String
	 */
	public static String formatXMLDate(String strDate)
	{
		String strResult = "";
		strResult = strDate.substring(0,8);
		strResult += " ";
		strResult +=strDate.substring(8,10);
		strResult += ":";
		strResult +=strDate.substring(10,12);
		strResult += ":";
		strResult +=strDate.substring(12);

		return strResult;



	}


	/**
	 * This method converts a local time to GMT time
	 * @param long, the data in long
	 */
	public static Date convertTimeToGMT(long lngDate) {
		// get the Timezone of the host default
		String strGMTOffset = "";
		Date dtExp = new Date(lngDate);

		Calendar calExp = Calendar.getInstance();
		calExp.clear();
		calExp.setTime(dtExp);

		String strMerGMTOffset = TPlusConfig.strMerchantGMTOffset;
		strGMTOffset = strMerGMTOffset.replace('+',' ');
		strGMTOffset = strGMTOffset.replace('-',' ');
		strGMTOffset = strGMTOffset.trim();
		String strAdd = strMerGMTOffset.substring(0,1);
		if(strAdd.equals("-")){
			//Add offset
			int intHrs = Integer.parseInt(strGMTOffset.substring(0, strGMTOffset.indexOf(":")));
			int intMin = Integer.parseInt(strGMTOffset.substring(strGMTOffset.indexOf(":")+1));
			calExp.add(calExp.HOUR,intHrs);
			calExp.add(calExp.MINUTE,intMin);
		}else{
			//Substract offset
			int intHrs = Integer.parseInt(strGMTOffset.substring(0, strGMTOffset.indexOf(":")));
			int intMin = Integer.parseInt(strGMTOffset.substring(strGMTOffset.indexOf(":")+1));
			calExp.add(calExp.HOUR,-intHrs);
			calExp.add(calExp.MINUTE,-intMin);

		}


		/* Not required as the GMT can be obtained from Config
	    // create a calendar
	    Calendar calExp = Calendar.getInstance();
	    calExp.clear();
	    calExp.setTime(dtExp);
		int gmtOffset = tz.getOffset(calExp.get(calExp.ERA),calExp.get(calExp.YEAR),
						calExp.get(calExp.MONTH),calExp.get(calExp.DATE),calExp.get(calExp.DAY_OF_WEEK),
							calExp.get(calExp.MILLISECOND));
	    calExp.add(calExp.MILLISECOND,gmtOffset);
		 */
		Date newTime = calExp.getTime();
		return newTime;
	}

	/**
	 * This method converts a a given date into 3D Secure format YYYYMMDD HH:MI:SS
	 * @param long, the data in long
	 */
	public static String convertDateTo3DSecureFormat(Date dt){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = sdf.format(dt);
		return formatXMLDate(strDate);
	}


	/**
	 * This method to generate the license key
	 * @param strLicense - the license information
	 * @param strLicenseEncryptionKey - The key for encryption
	 * @return String - the license key
	 * @throws Exception if there is any exception.
	 */

	/*public static String getLicenseKey(String strLicence, String strLicenseEncryptionKey) throws Exception
	{
		//Encrypt the LicenseKey License Key = SLNO(15 digits) + IP (upto 15 digits)
		String strEncrLicen = MPIEncryptUtility.encrypt(strLicence,strLicenseEncryptionKey);

		//Get the digest value of the key.
		java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA1");

		byte[] res = md.digest(strEncrLicen.getBytes());

		//Convert the digest value into Hex
		String strLicenHex = Hex.toString(res);

		if( strLicenHex.length() < 16)
		{
			throw new Exception("Invalid Digest Value");
		}


		//Perform DES Encryption with the first 16 digits as the KEY and the remaining digits as value to be encrypted.
		String strLocalKey = strLicenHex.substring(0,16);

		String strLocalValue = strLicenHex.substring(16);

		String strFinalEnc = MPIEncryptUtility.encryptDES(strLocalValue,strLocalKey);

		//Add the Key and Encrypted Value
		String strAddedKeyEnc = strLocalKey+strFinalEnc;

		//Split the String into two and get the value of Split1 XOR Split2
		int intSize = strAddedKeyEnc.length()/2;
		String strSplit1 = strAddedKeyEnc.substring(0,intSize);
		String strSplit2 = strAddedKeyEnc.substring(intSize);
		String strResult= stringXOR(strSplit1,strSplit2);

		//Convert the 32 char key to 16 char key.
		strResult = trimLicenseKey(strResult);

		return strResult;
	}
	 */

	/**
	 * Method to get the xor value of two strings.
	 */
	private static String stringXOR(String strFirst,String strSec)
	{
		//Convert the string to byte array and take the xor value of each byte pair.
		int intFirst;
		int intSecond;
		byte[] arrayByte1 = Hex.fromString(strFirst);
		byte[] arrayByte2 = Hex.fromString(strSec);
		byte[] res = new byte[arrayByte1.length];

		for (int intCount=0; intCount < arrayByte1.length;intCount++)
		{
			res[intCount] = (byte)(arrayByte1[intCount]^arrayByte2[intCount]);

		}

		return Hex.toString(res);

	}

	/**
	 * Method to get the 16 char license key from 32 char key.
	 */
	private static String trimLicenseKey(String strLicense) {

		// Take the first 2 digits and leave the next two and continue this logic to get 16 char key
		StringBuffer sbrTrimLicense= new StringBuffer();
		for (int intCount=0;intCount<32;intCount=intCount+4)
		{
			sbrTrimLicense.append(strLicense.substring(intCount,intCount+2));

		}
		return sbrTrimLicense.toString();


	}


	/**
	 * Method to get the Message Digest using SHA-1 algorithm
	 * Changes are done by Satya for PhoneNo Hash.
	 */

	public static String getSHADigest(String phoneNo)
	{

		String phDigest=" ";
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA1");

			byte[] res = md.digest(phoneNo.getBytes());

			phDigest= new String (res);

		}catch(Exception e){System.out.println("Expection in getSHADigest"+e);}


		return phDigest;


	}


	public static synchronized Object createObject(String strMsgParser)throws TPlusException
	{

		try
		{
			Class classObj = Class.forName(strMsgParser);
			return classObj.newInstance();
		}
		catch(InstantiationException exp)
		{
			throw new TPlusException(TPlusCodes.OBJ_CREATION_FAIL," Object Class Instantiation Exception"+exp.toString());
		}
		catch(ClassNotFoundException exp)
		{
			throw new TPlusException(TPlusCodes.OBJ_CREATION_FAIL," Object Class Not Found Exception"+exp.toString());
		}
		catch(Exception exp)
		{
			throw new TPlusException(TPlusCodes.OBJ_CREATION_FAIL," Error while genarating Object class"+exp.toString());
		}

	}


	/*public static void currConversion(String amount)
{
	double rate = 8463;
	//TransactionDB objDB = new TransactionDB();
	//objDB.getCurRate();

	Double l = new Double(amount);
	double ll = l.doubleValue()/100;
	System.out.println("Double"+ll);
	double currCon = ll/rate;
	System.out.println("AFT RATE="+currCon+"  "+currCon*100);
	long con = new Double(currCon*100).longValue();
	System.out.println("CONN="+con);
	//String con1=new Long(con).toString();
	String pad="000000000000"+con;
	System.out.println(pad.substring((new Long(con).toString()).length()));
}*/


	public static String currConversion(String amount,double rate)
	{
		//double rate = 8463;
		DecimalFormat df = new DecimalFormat("000000000000");

		//amount =  amount.substring(0,10);
		double lamount = new Double(amount).doubleValue()/100;
		double convAmt = (lamount*rate)*10;

		return df.format(convAmt);
	}

	public static String rateConversion(double rate)
	{

		NumberFormat formatter = new DecimalFormat("0000000");
		double lrate = rate*10000000;
		String strrate =formatter.format(lrate);
		String str = strrate.substring(0,7);
		double llrate = new Double(str).doubleValue();

		long val = new Double(llrate/rate).longValue();
		System.out.println(val);
		return (new Long(val).toString()).length()-1+str;
	}




	public static final double roundDouble(double d, int places)
	{
		return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10,(double) places);
	}


	public static String lpad (String src, String filler, int length)
	{
		while (src.length()<length)
			src = filler + src;
		return src;
	}

	public static String rpad (String src, String filler, int length)
	{
		while (src.length()<length)
			src += filler;
		return src;
	}

	public static String getApprovalCode()
	{

		Random randomGenerator = new Random();
		String approvalCode="";

		for (int idx = 1; idx <= 6; ++idx)
		{
			int randomInt = randomGenerator.nextInt(10);
			approvalCode=approvalCode+randomInt;
		}

		return approvalCode;

	}



	public static String strXor(String s1,String s2)
	{
		System.out.println("strXor="+s1+"  "+s2);
		BigInteger one = new BigInteger(s1, 16);
		BigInteger two = new BigInteger(s2, 16);
		BigInteger three = one.xor(two);
		String s3 = three.toString(16);
		System.out.println(s3);
		return s3;
	}


	public static String isOddParity( byte[] hexStr )
	{


		int bb = 0;
		int bitCount = 0;
		String finalVal="";
		// Process bits right to left, shifting each bit in turn into the lsb position.
		for( int j=0 ;j< hexStr.length;j++)
		{
			bitCount=0;
			bb = hexStr[j];
			System.out.println("bb="+bb);
			for ( int i = 0; i < 8; i++, bb >>>= 1 )
			{
				if ( ( bb & 1 ) != 0 )
				{
					bitCount++;
				}
			}
			if(( bitCount & 1 ) == 0)
			{
				System.out.println(bytesToHexString((byte)(hexStr[j]^1)));
				finalVal = finalVal+bytesToHexString((byte)(hexStr[j]^1));
			}
			else
			{
				System.out.println("Odd Parity");
				finalVal = finalVal+bytesToHexString((byte)(hexStr[j]));
			}
		}

		return finalVal;
	}

	public static String bytesToHexString(byte b)
	{
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format("%02x", b);
		return sb.toString();
	}

	/*public String getCardScheme() throws TPlusException
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

		TPlusISOMsg objTPlusISOMsg = new TPlusISOMsg();

		String cardScheme = objTPlusISOMsg.getCardProduct();

		if(cardScheme == null || "".equals(cardScheme)){

			String cardNumber = objTPlusISOMsg.getCardNumber();

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

	}*/

	public static void main(String s[])
	{
		/*double rate =1.41234;

		NumberFormat formatter = new DecimalFormat("0000000");*/

		/*DecimalFormat df = new DecimalFormat("0.00");
	System.out.println("amt="+currConversion("000000846300"));
	double cAmt = new Double(currConversion("000000846300")).doubleValue()*100;
	System.out.println(new Double(cAmt).longValue());

	String strRate = TPlusUtility.rpad(new Integer(rate).toString(),"0",7);
	strRate  = (7-(new Integer(rate).toString()).length())+strRate;
	System.out.println(strRate);*/

		/*double lrate = rate*10000000;
		String strrate =formatter.format(lrate);
		String str = strrate.substring(0,7);
		System.out.println(new Double(lrate).toString()+"  "+str);
		double llrate = new Double(str).doubleValue();
		System.out.println(llrate);
		long val = new Double(llrate/rate).longValue();
		System.out.println(val);
		System.out.println((new Long(val).toString()).length()-1+str);*/

		String s1 = "6FAA73C4517B5672";
		String s2 = "3030000000000000";
		
		String arpc = strXor(s1, s2);
		System.out.println("arpc :: " + arpc);



	}

	public static Map<String, String> parseJCBF48(String tlv) {

		if (tlv == null || tlv.length()%2!=0) {
			throw new RuntimeException("Invalid tlv, null or odd length");
		}

		HashMap<String, String> hashMap = new HashMap<String, String>();

		for (int i=0; i<tlv.length();) {

			try {

				String tag = tlv.substring(i, i=i+4);
				String tagAsc = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tag));

				String tagLen = tlv.substring(i, i=i+4);
				String tagLenVal = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tagLen));

				String tagValue = tlv.substring(i, i=i+Integer.valueOf(tagLenVal).intValue()*2);
				String tagValueAsc = "";

				if("01".equals(tagAsc)){
					tagValueAsc = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tagValue));					
				}else if("02".equals(tagAsc)){					
					tagValueAsc = tagValue;					
				}else if("03".equals(tagAsc)){					
					tagValueAsc = new String(ISOUtil.hex2byte(tagValue));					
				}else if("04".equals(tagAsc)){					
					tagValueAsc = new String(ISOUtil.hex2byte(tagValue));					
				}

				hashMap.put(tagAsc, tagValueAsc);

			} catch (NumberFormatException e) {
				throw new RuntimeException("Error parsing number",e);
			} catch (IndexOutOfBoundsException e) {
				throw new RuntimeException("Error processing field",e);
			}
		}

		return hashMap;
	}

	public static Map<String, String> parseJCBF48New(String tlv) {

		if (tlv == null) {
			throw new RuntimeException("Invalid tlv, null");
		}

		HashMap<String, String> hashMap = new HashMap<String, String>();

		for (int i=0; i<tlv.length();) {

			try {

				String tag = tlv.substring(i, i=i+2);
				//String tagAsc = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tag));

				String tagLen = tlv.substring(i, i=i+2);
				//String tagLenVal = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tagLen));

				String tagValue = tlv.substring(i, i=i+Integer.valueOf(tagLen).intValue());
				//String tagValueAsc = "";

				/*if("01".equals(tag)){
					tagValueAsc = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tagValue));					
				}else if("02".equals(tag)){					
					tagValueAsc = tagValue;					
				}else if("03".equals(tag)){					
					tagValueAsc = new String(ISOUtil.hex2byte(tagValue));					
				}else if("04".equals(tag)){					
					tagValueAsc = new String(ISOUtil.hex2byte(tagValue));					
				}*/

				hashMap.put(tag, tagValue);

			} catch (NumberFormatException e) {
				throw new RuntimeException("Error parsing number",e);
			} catch (IndexOutOfBoundsException e) {
				throw new RuntimeException("Error processing field",e);
			}
		}

		return hashMap;
	}

	public static Map<String, String> parseJCBF60(String f60) {

		if (f60 == null || f60.length()%2!=0) {
			throw new RuntimeException("Invalid f60, null or odd length");
		}

		HashMap<String, String> hashMap = new HashMap<String, String>();

		for (int i=0; i<f60.length();) {

			try {

				String tag = f60.substring(i, i=i+4);
				String tagAsc = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tag));

				String tagLen = f60.substring(i, i=i+4);
				String tagLenVal = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tagLen));

				String tagValue = f60.substring(i, i=i+Integer.valueOf(tagLenVal).intValue()*2);
				String tagValueAsc = ISOUtil.ebcdicToAscii(ISOUtil.hex2byte(tagValue));

				hashMap.put(tagAsc, tagValueAsc);

			} catch (NumberFormatException e) {
				throw new RuntimeException("Error parsing number",e);
			} catch (IndexOutOfBoundsException e) {
				throw new RuntimeException("Error processing field",e);
			}

		}

		return hashMap;
	}

}