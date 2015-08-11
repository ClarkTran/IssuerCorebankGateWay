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

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;

public class VoiceProcessor
{

public VoiceProcessor(){}


public String  process(String voicemsg)throws TPlusException
{

	String voiceReq = "";

   		try
   		{


			if(voicemsg!= null)
			{
				String voicedata[] = voicemsg.split("&");

				System.out.println("VoiceReq="+voicemsg+"   "+voicedata[7]);

				ISOMsg msg = new ISOMsg();
				msg.setPackager(new GenericPackager("file:\\c:\\acquirer\\atm\\atmswitch\\parser\\base1.xml"));

				//msg.setHeader("6080000001");
				if(voicedata[0].equals("VOISALE"))
				{
					msg.set(0,"0200");
					msg.set(3,"005000");
				}
				if(voicedata[0].equals("VOIREVERSAL"))
				{
					msg.set(0,"0200");
					msg.set(3,"006000");
				}
				msg.set(2,voicedata[3]);

				msg.set(4,voicedata[6]);
				msg.set(11,voicedata[7]);
				msg.set(12,"070727");
				msg.set(13,"0703");
				msg.set(14,voicedata[4]);
				msg.set(22,"012");
				msg.set(23,"001");
				msg.set(25,"00");
				msg.set(37,voicedata[9]);
				msg.set(41,voicedata[2]);
				msg.set(42,voicedata[1]);
				msg.set(49,voicedata[8]);


				msg.dump(new PrintStream(System.out),"0");

				byte[] req = createResponse(msg.pack());

				msg.unpack(msg.pack());

				msg.dump(new PrintStream(System.out),"0");


				System.out.println(ISOUtil.hexString(req));

				voiceReq = ISOUtil.hexString(req);



			}
			else
			{
				throw new TPlusException(TPlusCodes.NO_REQ_HANDLER,". Error: Issuer-MTI-ProcessingCode  ");
			}

   		}
  		catch(Exception exp)
   		{
	  		 throw new TPlusException(TPlusCodes.OBJ_CREATION_FAIL, "Error in VoiceProcessor:Unable to create Voice request handler "+exp.getMessage());
   		}


		return voiceReq;
}


public byte[] createResponse(byte[] isoBytes) throws TPlusException
{
   	byte strResponseMsg[] = null;

		try
		{
			if(isoBytes != null)
			{
				String strHeader = "6080000001";
				strResponseMsg= this.appendBytes(ISOUtil.hex2byte(strHeader),isoBytes);
				/*String strTempStatus = "0000" + isoBytes.length;
				byte hexMsgLen[] = ISOUtil.str2bcd(strTempStatus.substring(strTempStatus.length()-4),true);
				strResponseMsg = this.appendBytes(hexMsgLen,isoBytes);*/
			}
		}
		catch(Exception e)
		{
			//TPlusPrintOutput.printMessage("TPlus8583Parser","Error in generating the Error Response");
			//throw new TPlusException(TPlusCodes.APPL_ERR," createResponse Error: "+ e.getMessage());
		}

   	return strResponseMsg;
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
	VoiceProcessor vp = new VoiceProcessor();
	System.out.println(vp.process("VSALE&123456&000001050265165&65500011&4565982010129243&0109&123&000000099999"));


}

}