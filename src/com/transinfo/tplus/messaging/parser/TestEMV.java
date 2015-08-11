package com.transinfo.tplus.messaging.parser;

import org.jpos.iso.ISOUtil;
import org.jpos.tlv.TLVList;
import org.jpos.tlv.TLVMsg;


public class TestEMV
{

	public static void main(String s[])
	{


		try
		{
			System.out.println(Integer.parseInt("9f10",16));
			System.out.println(Integer.toHexString(Integer.parseInt("9f10",16))+"  "+"06990A03A0A010".substring(8,10));
			System.out.println(ISOUtil.hexString("5".getBytes()));


			System.out.println(getReversalMsg("7102070282025C008407A00000000310109A030402209C01009F02060000000000059F03060000000000009F0902008C9F1A0207029F26084CB9C2891C2A434A9F2701809F3303E020C89F3501229F360200C99F37049732D85A5F3401019F34031E03009F100706990A030AA01095050000008000"));

			/*TLVList newTLV = new TLVList();

		   			TLVList tlv = new TLVList();
                    tlv.unpack(ISOUtil.hex2byte("5F2A02070282025C008407A00000000310109A030402209C01009F02060000000000059F03060000000000009F0902008C9F1A0207029F26084CB9C2891C2A434A9F2701809F3303E020C89F3501229F360200C99F37049732D85A5F3401019F34031E03009F100706990A030AA01095050000008000"));

                    byte[] iad = null;
                    int tag;
                    Enumeration enume = tlv.elements();
                    while(enume.hasMoreElements())
                    {
						TLVMsg tlvq = (TLVMsg)enume.nextElement();
						iad = tlvq.getValue();
						tag = tlvq.getTag();
						System.out.println(tag+"  "+Integer.toHexString(tag) + " : " +ISOUtil.hexString(iad));

						/*if(Integer.toHexString(tag).equals("9f36"))
						{
							System.out.println("In ");
							newTLV.append(tlvq);
					 	}

					 	if(Integer.toHexString(tag).equals("9f10"))
						{
							System.out.println("IssuerAuthFail="+checkIssuerAuthFail(ISOUtil.hexString(iad));
					 	}*/




			/*if(Integer.toHexString(tag).equals("5f34") ||
						   Integer.toHexString(tag).equals("84")   ||
						   Integer.toHexString(tag).equals("9f09") ||
						   Integer.toHexString(tag).equals("9f1e") ||
						   Integer.toHexString(tag).equals("9f27") ||
						   Integer.toHexString(tag).equals("9f34") ||
						   Integer.toHexString(tag).equals("9f35") ||
						   Integer.toHexString(tag).equals("9f41"))
						{
							System.out.println("11");
							tlv.deleteByTag(tag);
						}*/
			//}
			//tlv.deleteByTag("9F34");
			//tlv.deleteByTag("9F35");
			//System.out.println(ISOUtil.hexString(tlv.pack()));


			//System.out.println(ISOUtil.hexString(newTLV.pack()));



			//Date now = new Date(System.currentTimeMillis());
			//SimpleDateFormat df = new SimpleDateFormat("yyddd");
			//System.out.println("Refno="+df.format(now));
			//String dateTime = TPlusCalendar.getGMTTime();
			//System.out.println("f7"+dateTime);


			/*GregorianCalendar cal = new GregorianCalendar();
cal.setGregorianChange(new Date(Long.MAX_VALUE)); // setting the calendar to act as a pure Julian calendar.

// cal.set(Calendar.DATE, new Date().getDate()); // seting the current date
// Date todayJD = cal.getGregorianChange(); // getting the changed date after the setGregorianChange
Date todayJD = cal.getTime(); // getting the calculated time of today's Julian date
SimpleDateFormat sdfJulianDate = new SimpleDateFormat("yyDDDHHmmss");
SimpleDateFormat sdfJuliandayOfYear = new SimpleDateFormat("DDD");
System.out.println("today Date = " + new Date());
System.out.println("Today as julian date = " +
Long.parseLong(sdfJulianDate.format(todayJD))+""+60);
System.out.println("Today as day of year = " +
sdfJuliandayOfYear.format(todayJD));*/






		}catch(Exception exp)
		{

			System.out.println(exp);
		}

	}


	public static String getReversalMsg(String f55)throws Exception
	{
		String str9f10=null;
		String str95=null;



		TLVList tlv = new TLVList();
		tlv.unpack(ISOUtil.hex2byte(f55));

		TLVList revTLV = new TLVList();

		int tag9f36 =	Integer.parseInt("9f36",16);
		TLVMsg iad9f36 = tlv.find(tag9f36);
		if(iad9f36 !=null)
		{
			System.out.println("9f36"+ISOUtil.hexString(iad9f36.getValue()));
			revTLV.append(iad9f36);
		}

		System.out.println("9f36 appended");

		int tag9f10 =	Integer.parseInt("9f10",16);
		TLVMsg iad9f10 = tlv.find(tag9f10);
		if(iad9f10 != null)
		{
			System.out.println("9f10="+ISOUtil.hexString(iad9f10.getValue()));
			str9f10 = ISOUtil.hexString(iad9f10.getValue());
		}

		int tag95 =	Integer.parseInt("95",16);
		TLVMsg iad95 = tlv.find(tag95);
		if(iad95 != null)
		{
			System.out.println("95="+ISOUtil.hexString(iad95.getValue()));
			str95 = ISOUtil.hexString(iad95.getValue());
		}

		int tag71 =	Integer.parseInt("71",16);
		TLVMsg iad71 = tlv.find(tag71);


		if(! checkIssuerAuthFail(str95,str9f10))
		{
			System.out.println("Iss Auth Failed");
			revTLV.append(iad9f10);
			revTLV.append(iad95);
		}

		if(checkIssuerScript(f55))
		{
			System.out.println("Script Count > 1");
			revTLV.append(iad71);
		}

		System.out.println(ISOUtil.hexString(revTLV.pack()));
		return null;

	}



	public static boolean checkIssuerAuthFail(String tag95,String tag9f10)
	{
		if(tag9f10 != null && !checkIssuerAuthFail9F10(tag9f10))
		{
			return false;
		}
		if(tag95 != null &&!checkIssuerAuthFail95(tag95))
		{
			return false;
		}

		return true;

	}

	public static boolean checkIssuerAuthFail9F10(String str)
	{


		if(str.startsWith("06"))
		{
			String strCVR = str.substring(8,10);
			System.out.println("strCVR="+strCVR);
			String strByn = Hex2Bin(strCVR);
			if(strByn.length()>4 && strByn.charAt(4)=='1')
			{
				return false;
			}
		}
		else if(str.startsWith("61"))
		{
			return false;
		}
		else if(str.substring(2).equals("62112233445566778899AABBCCDDEEFFFFEEDDCCBBAA99887766554433221100"))
		{
			return false;
		}

		return true;
	}


	public static boolean checkIssuerAuthFail95(String str)
	{

		String strTVR = str.substring(9,10);
		System.out.println(strTVR);
		String strByn = Hex2Bin(strTVR);
		if( strByn.charAt(2)=='1')
		{
			return false;
		}

		return true;
	}


	public static boolean checkIssuerScript(String f55)throws Exception
	{
		/*if(tag9f10!=null)
		{
			String byte4 = Hex2Bin(tag9f10.substring(12,14));
			String scriptCnt = byte4.substring(1,4);
			System.out.println(scriptCnt);

			if( Integer.parseInt(scriptCnt)>0)
			{
				return true;
			}
	 	}*/

		TLVList tlv = new TLVList();
		tlv.unpack(ISOUtil.hex2byte(f55));

		if(tlv.find(Integer.parseInt("71",16))!=null)
		{
			return true;
		}
		if(tlv.find(Integer.parseInt("72",16))!=null)
		{
			return true;
		}

		return false;

	}


	public static String Hex2Bin(String byte2){

		System.out.println(byte2);
		int i = Integer.parseInt(byte2,16);
		String by = Integer.toBinaryString(i);
		System.out.println("This is Binary: " + lpad(by,"0",8));

		return lpad(by,"0",8);


	}


	public static String lpad (String src, String filler, int length)
	{
		while (src.length()<length)
			src = filler + src;
		return src;
	}




}


