package com.transinfo.tplus.messaging.validator;


import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;


public class TrackValidator implements BaseValidator
{
	public TrackValidator()
	{
	}

	public boolean process(IParser objISO) throws OnlineException
	{

		String POSEntryMode = objISO.getValue(22);
		if ("012".equals(POSEntryMode))
			return true;
		
		String track2 = objISO.getValue(35);
		
		if(POSEntryMode.substring(0,2).equals("90"))
		{
			String track1 = objISO.getValue(45);
			
			String cardnumber = objISO.getValue(2);
			if (((track1 == null)&&(track2 == null))) //no track
			{
				throw new OnlineException("14", "A12888", "No track data for card number "+cardnumber);
			}
		}

		String cardScheme = objISO.getCardProduct();
		if("JC".equals(cardScheme)){
			
			String pos12 = POSEntryMode.substring(0, 2);
			String posCondCode = objISO.getValue(25);
			
			if(("02".equals(pos12) || "05".equals(pos12) || "97".equals(pos12)) && ("02".equals(posCondCode))){
				
				if (track2 == null) //no track
				{
					throw new OnlineException("05", "A12888", "No track data for JCB Cardmember Operated Terminal Transaction");
				}
				
			}
			
		}

		if (DebugWriter.boolDebugEnabled) DebugWriter.write(">>> TrackValidator is Successful.....");
		return true;
		
	}
	
}