package com.transinfo.tplus.messaging;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.messaging.parser.IParser;


public class PINTranslation
{

  public boolean pinTranslation(IParser objISO)throws TPlusException
  {
	  String PINblock = null;
	  String NewPINblock = null;

	try
	{

			if(objISO.getValue(52) != null && !objISO.getValue(52).equals(""))
			{
				PINblock = objISO.getValue(52);
			}
			else
			{
				 return true;
		 	}

			if(objISO.getValue(53) != null && !objISO.getValue(53).equals(""))
			{
				NewPINblock = objISO.getValue(53);


			}

			TransactionDB objTranx = new TransactionDB();
			String TerminalMasterKey = objTranx.getTerminalMK(objISO.getValue(41));
			String TerminalWorkingKey = objTranx.getTerminalWK(objISO.getValue(41));

			String issuerWorkingKey = objTranx.getLogonIssuerWK();

			String issKey24 = issuerWorkingKey + issuerWorkingKey.substring(0,16);

			System.out.println(TerminalMasterKey+"  "+TerminalWorkingKey+"  "+issKey24);

				  byte[] translatedPINblock = new byte[8];

				  if (PINblock != null && !PINblock.equals(""))
				  {
					if (TerminalMasterKey.length()>0)
					{
					  try
					  {
						//translatedPINblock = ISOUtil.hex2byte(JFHSMUtil.TranslatePIN(requestMsg.getString(2), TerminalWorkingKey, PINblock));
						byte[] decryptedWorkingKey = OnlineUtil.decrypt(ISOUtil.hex2byte(TerminalMasterKey), ISOUtil.hex2byte(TerminalWorkingKey));
						byte[] decryptedPinBlock = OnlineUtil.decrypt(decryptedWorkingKey, ISOUtil.hex2byte(PINblock));
						translatedPINblock = OnlineUtil.encrypt3DES(ISOUtil.hex2byte(issKey24),decryptedPinBlock);
						objISO.setBinaryValue(52,translatedPINblock);
						System.out.println(ISOUtil.hexString(translatedPINblock));
					  }catch(Exception exp)
					  {
						  System.out.println("PIN Translation Error="+exp);
						//throw new TPlusException(TPlusCodes.APPL_ERR," Error: PIN Translation: "+exp.getMessage());
						return false;
					  }

					}
				 }

System.out.println("Sent True");
			return true;

	}
	catch(Exception exp)
	{
		System.out.println("PIN Translation Error="+exp);
		//throw new TPlusException(TPlusCodes.APPL_ERR," Error: PIN Translation Key Retrive: "+exp.getMessage());
		return false;
	}

  }

}