package com.transinfo.tplus.messaging;

import java.util.HashMap;

import org.jpos.iso.ISOUtil;

@SuppressWarnings("unchecked")
public class HeaderUtil
{


	static HashMap map = new HashMap();
	static{
		map.put("0","30");
		map.put("1","31");
		map.put("2","32");
		map.put("3","33");
		map.put("4","34");
		map.put("5","35");
		map.put("6","36");
		map.put("7","37");
		map.put("8","38");
		map.put("9","39");
	}
	//3030303535383430202020
	public static String getReqHeader(int msgLen)
	{
		/*String strLen = new Integer(msgLen+92).toString();
	    System.out.println("BCD"+Integer.parseInt(strLen,16) +"   "+ISOUtil.hexString(ISOUtil.str2bcd(strLen,true)));

		String TotMsgLen = ISOUtil.hexString(ISOUtil.str2bcd(strLen,true));

		System.out.println("TotMsgLen"+TotMsgLen);*/
		System.out.println("Hex"+Integer.parseInt(""+msgLen,16));

		String TotMsgLen = new Integer(msgLen+46).toString();
		System.out.println("TotMsgLen"+TotMsgLen);
		// return "2E01"+asciiLen(TotMsgLen)+"30303031303334342020203030303135383430202020000000003030303030303030003030303030";

		//return "2E82"+asciiLen(TotMsgLen)+"30303031303334342020203237333830343138202020000000013130303030303030003030303030"; // IIN 27380418
		//return "2E82"+asciiLen(TotMsgLen)+"30303031303334342020203330343730343831202020000000013130303030303030003030303030"; // IIN 30470481
		return "2E82"+asciiLen(TotMsgLen)+"30303031303334342020203330343730343138202020000000013130303030303030003030303030"; // IIN 30470418
	}

	public static String getResHeader(String strHeader,int msgLen)
	{
		String TotMsgLen = new Integer(msgLen+46).toString();
		return strHeader.substring(0,4)+asciiLen(TotMsgLen)+strHeader.substring(34,56)+strHeader.substring(12,34)+strHeader.substring(56);
	}

	public static String asciiLen(String str)
	{
		String strLen="";
		for(int i=0;i<str.length();i++)
		{
			strLen=strLen+(String)map.get(str.substring(i,i+1));
		}
		System.out.println(strLen);
		strLen = ("30303030"+strLen).substring(strLen.length());
		System.out.println(strLen);
		return strLen;
	}

	public static String getJCBReqHeader(int msgLen)
	{

		String len = Integer.toHexString(msgLen);
		String str = ("0000"+Integer.toHexString(msgLen)).substring(len.length());

		String val = str+"0000";

		return val;
	}


	public static void main(String ss[])
	{
		System.out.println(getJCBReqHeader(500));
	}

}