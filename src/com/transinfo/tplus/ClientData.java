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


package com.transinfo.tplus;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.debug.DebugWriter;

public class ClientData{

	public StringBuffer strISODump = new StringBuffer();

	//private StringBuffer strISODump = new StringBuffer();


	public void addISOPart(String data) {

		if(data!=null && !data.equals(""))
		strISODump.append(data);


	}


	public synchronized String getNextISO(int intmsg)
	{

		String nextISO=null;

		try
		{
				String data =  strISODump.substring(4);
				strISODump.replace(0,data.length()+4, "");
				return data;
			
			/*String data =  strISODump.substring(0);
			strISODump.replace(0,data.length(), "");
			return data;*/
			
			/*if (DebugWriter.boolDebugEnabled) DebugWriter.write("ClientData:getNextISO length "+strISODump.length());
			if(strISODump.length() > 4)
			{
				 String strlen = strISODump.substring(0,4);

				 int intLen =0;

				 if(TPlusConfig.isNumeric(strlen))
				 {
					intLen = Integer.parseInt(strlen)*2;

					 //intLen = Integer.parseInt(strlen,16);

					if(strISODump.length() >= intLen)
					{
						nextISO = strISODump.substring(4,intLen+4);
						strISODump.replace(0,intLen+4, "");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("ClientData: Remaining Size Data="+strISODump.length()+"    "+strISODump);
						return nextISO;
					}
				}
				else
				{

					if (DebugWriter.boolDebugEnabled) DebugWriter.write("ClientData: Incomplete Message "+strISODump.length()+"  "+"strLen="+strlen);
					System.out.println("ClientData: Incomplete Message "+strISODump.length()+"  "+"strLen="+strlen);
				}
			}*/

		}
		catch(Exception exp)
		{
			strISODump.setLength(0);
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("ClientData",TPlusCodes.MSG_EXT_FAIL,exp.getMessage());
			//elServer.addLogItem("TPLUSSERVER","267",TPlusException.getErrorType(0),"Thread pool exception  occurred when adding request to the request queue. Error:"+tp.getMessage());
			TPlusPrintOutput.printErrorMessage("ClientData",TPlusCodes.MSG_EXT_FAIL,exp.toString());
	 	}

	 	return nextISO;
	 }

public static String convertHexToString(String hex){

	  StringBuilder sb = new StringBuilder();
	  StringBuilder temp = new StringBuilder();

	  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
	  for( int i=0; i<hex.length()-1; i+=2 ){

	      //grab the hex in pairs
	      String output = hex.substring(i, (i + 2));
	      //convert hex to decimal
	      int decimal = Integer.parseInt(output, 16);
	      //convert the decimal to character
	      sb.append((char)decimal);

	      temp.append(decimal);
	  }
	  System.out.println("Decimal : " + temp.toString());

	  return sb.toString();
  }



	 public static void main(String s[])
	 {


		 byte[] b = ISOUtil.hex2byte("0205600000000002003020078020C00204000000000000000100A0100100520001000000374541822001564124D060120101234226100000303030303030313130303030303030303030303030313101185F2A02070282025C008407A00000000310109A030401269C01009F02060000000001009F03060000000000009F0902008C9F1A0207029F2608BB67B3EF39BAA0CE9F2701809F3303E020C89F3501229F3602001B9F3704BB5D5F445F3401019F34031E03009F100706990A03A0A1009505400000800000063030303030320205600000000002003020078020C002");
		 ClientData cdata = new ClientData();
		try
		 {

		 cdata.addISOPart("0205600000000002003020078020C00204000000000000000100A0100100520001000000374541822001564124D060120101234226100000303030303030313130303030303030303030303030313101185F2A02070282025C008407A00000000310109A030401269C01009F02060000000001009F03060000000000009F0902008C9F1A0207029F2608BB67B3EF39BAA0CE9F2701809F3303E020C89F3501229F3602001B9F3704BB5D5F445F3401019F34031E03009F100706990A03A0A1009505400000800000063030303030320205600000000002003020078020C002");
		 String str=cdata.getNextISO(1);
		 System.out.println("String ="+str);
		/* System.out.println(str == null ? 0 : str.length());
		 System.out.println(cdata.strISODump.length());
		 cdata.dumpString(str);
		 System.out.println("***************************");
		 b = ISOUtil.hex2byte("400000800000063030303030320205600000000002003020078020C002");
		 cdata.addISOPart(new String(b));
		 str=cdata.getNextISO();
		 System.out.println(str == null ? 0 : str.length());
		 System.out.println(cdata.strISODump.length());
		 cdata.dumpString(str);
		 System.out.println("***************************");
		 b = ISOUtil.hex2byte("04000000000000000100A0100100520001000000374541822001564124D060120101234226100000303030303030313130303030303030303030303030313101185F2A02070282025C008407A00000000310109A030401269C01009F02060000000001009F03060000000000009F0902008C9F1A0207029F2608BB67B3EF39BAA0CE9F2701809F3303E020C89F3501229F3602001B9F3704BB5D5F445F3401019F34031E03009F100706990A03A0A100950540000080000006303030303032");
		 cdata.addISOPart(new String(b));
		 str=cdata.getNextISO();
		 System.out.println(str == null ? 0 : str.length());
		 System.out.println(cdata.strISODump.length());
		 cdata.dumpString(str);
		 System.out.println("3");*/

	    }
	    catch(Exception e){System.out.println(e);}
 	}

 	public void dumpString(String str)
 	{
		try{
			 if(str!=null)
			System.out.println(ISOUtil.hexString(str.getBytes()));
	  }catch(Exception e){System.out.println(e);}
   }
}


