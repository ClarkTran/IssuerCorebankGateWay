package com.transinfo.tplus.messaging.mux;

import java.util.HashMap;

import org.jpos.util.NameRegistrar;

import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusUtility;

@SuppressWarnings("unchecked")
public class MuxFactory
{


	public static HashMap muxMap = new HashMap();

	static
	{
		muxMap.put("VISA","com.transinfo.tplus.messaging.mux.VisaMux");
		muxMap.put("CADENCIE","com.transinfo.tplus.messaging.mux.CadencieMux");
		muxMap.put("VCB","com.transinfo.tplus.messaging.mux.VCBMux");
		muxMap.put("NARADA","com.transinfo.tplus.messaging.mux.NaradaMux");
		muxMap.put("CAB","com.transinfo.tplus.messaging.mux.NaradaMux");
		muxMap.put("CB","com.transinfo.tplus.messaging.mux.CBMux");

	}


	public static IMux getMux(String key)throws TPlusException
	{

		IMux mux;

		try
		{
			mux = (IMux)NameRegistrar.get(key);
		} catch (NameRegistrar.NotFoundException e)
		{
			String muxClass = (String)muxMap.get(key);
			mux = (IMux)TPlusUtility.createObject(muxClass);
			NameRegistrar.register(key, mux);
		}
		return mux;
	}

}