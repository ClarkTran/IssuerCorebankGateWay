package com.transinfo.tplus.messaging.mux;


import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;

import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOMUX;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISORequest;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.NACChannel;
import org.jpos.iso.header.BASE1Header;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.tlv.TLVList;
import org.jpos.tlv.TLVMsg;
import org.jpos.util.LogEvent;
import org.jpos.util.Logger;
import org.jpos.util.NameRegistrar;
import org.jpos.util.SimpleLogListener;

import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.messaging.TIVNThread;
import com.transinfo.tplus.messaging.parser.IParser;


public class CBMux implements IMux  {
	public static final String VISA_CHANNEL_REALM = "visa-channel";
	public static final String VISA_MUX_REALM = "visa-mux";
	public static boolean INITIALIZED = false;
	public static boolean FirstTimeConn = true;

	static ISOMUX cbMux;
	static BASE1Header header;
	static BaseChannel channel;
	int visaTimeOut1 = 30;
	int visaTimeOut2 = 5;
	static String prevF55Res = null;
	private TIVNThread VisaMuxThread = null;

	public CBMux() {
	}

	public static CBMux getCBMux(String name){
		System.out.println("in getCBMux");
		CBMux mux;
		String key = CBMux.class.getName() + "." + name;
		try {
			mux = (CBMux)NameRegistrar.get(key);
		} catch (NameRegistrar.NotFoundException e) {
			mux = new CBMux();
			NameRegistrar.register(key, mux);
		}
		return mux;
	}

	public ISOMsg process(ISOMsg isomsg){

		ISOMsg requestMsg = isomsg;
		requestMsg.setHeader(header);
		requestMsg.setDirection(requestMsg.OUTGOING);
		ISORequest request;
		ISOMsg response=null;
		ISOPackager packager = null;

		try
		{

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("VisaMux: In Visa MUX..");

			LogEvent evt = new LogEvent (channel, "send");
			evt.addMessage(requestMsg);

			System.out.println("VISA- C Mux Connected.."+cbMux.isConnected());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("VisaMux:VISA Connected.."+cbMux.isConnected());


			System.out.println("Af 55"+ requestMsg.getString(11)+"   "+requestMsg.getString(63));
			// requestMsg.unset(35);

			// packager = new GenericPackager("D:\\WorkSpace_CACIS\\BCELISS_TESTING\\bin\\parser\\base1_cb.xml");
			packager = new GenericPackager(TPlusConfig.getISOIssuerConfig());

			requestMsg.setPackager(packager);
			System.out.println("Send to VISA="+ ISOUtil.hexString(requestMsg.pack()));
			request = new ISORequest(requestMsg);

			//Dump the transaction to System.out to see all the edited fields
			System.out.println("Sending request to CB - CB Mux");
			request.dump(System.out,"");
			DebugWriter.writeMsgDump(requestMsg);



			//End Test. Remember to uncomment the below section afterward

			cbMux.queue(request);
			response = request.getResponse(visaTimeOut1*1000);

			System.out.println("Waiting for Response");
			System.out.println("Request transmited: ++++++++++"+ request.isTransmitted());
			if (response!=null && request.isTransmitted())
			{
				DebugWriter.writeMsgDump(response);
				System.out.println("Receiving response from VISA - CB Mux...");
				System.out.println("Is Transmitted "+request.isTransmitted());
				//System.out.println(request.getTransmitException());
				System.out.println("Received response from VISA -CB Mux="+ ISOUtil.hexString(response.pack()));
				response.dump(System.out,"");
			}
			else
			{
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("VisaMux: Error.. Connection lost with Visa Host.."+request.isTransmitted());

				if(!request.isTransmitted())
				{
					packager = new VSDCPackager();
					isomsg.setPackager(packager);
					isomsg.set(39, "91");
					return isomsg;
					//return null;
				}

				if (response!=null)
				{
					response.dump(System.out,"");
					DebugWriter.writeMsgDump(response);
				}

			}


		} catch (Exception e)
		{
			e.printStackTrace();
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("CB/VisaMux: Error.. Exception while sending request to Visa..."+e);
			//ISOMsg responseMsg = (ISOMsg)requestMsg;
			try
			{
				//responseMsg.setResponseMTI();
				packager = new VSDCPackager();
				isomsg.setPackager(packager);
				isomsg.set(39, "96");
				return isomsg;
			}catch(Exception ignore){ System.out.println("In Visa Mux"+ignore);}

		}

		return response;
	}

	public void initialize(IParser objISO){

		if(objISO.getValue(55) != null)
		{
			objISO.setF55Req(objISO.getValue(55));
		}

		prevF55Res = objISO.getF55Res();

		// System.out.println("prevF55="+prevF55Res);
		System.out.println(INITIALIZED+"  "+objISO.getIssuerHost()+" "+objISO.getIssuerPort()+"  "+objISO.getStationId()+"  "+objISO.getSignOnNeeded());
		if(DebugWriter.boolDebugEnabled) DebugWriter.write(INITIALIZED+"  "+objISO.getIssuerHost()+" "+objISO.getIssuerPort()+"  "+objISO.getStationId()+"  "+objISO.getSignOnNeeded());

		synchronized(this)
		{

			if(INITIALIZED)
			{
				return;
			}

			System.out.println(INITIALIZED);
			DebugWriter.write("TransactionDB: Visa Connection is Re-establishing");

			FirstTimeConn=true;
			String visaHost = objISO.getIssuerHost();
			int visaPort = -1;
			try
			{
				visaPort = new Integer(objISO.getIssuerPort()).intValue();

			}catch(Exception e)
			{
				e.printStackTrace();
				return;
			}

			try
			{
				String visaStationId = objISO.getStationId();

				//ISOPackager packager = new GenericPackager("D:\\WorkSpace_CACIS\\BCELISS_TESTING\\bin\\parser\\base1_cb.xml");
				ISOPackager packager = new GenericPackager(TPlusConfig.getISOIssuerConfig());
				channel = new NACChannel(visaHost, visaPort, packager,"".getBytes());
				cbMux = new ISOMUX(channel);
				new Thread(cbMux).start();
				cbMux.setISORequestListener(new VisaRequestListener());

				INITIALIZED = true;

				Logger logger = new Logger();
				logger.addListener(new SimpleLogListener(System.out));
				channel.setLogger(logger,"CACIS");

				Logger fileLogger = new Logger();


				System.out.println("*********************************************File Logger");
				FileOutputStream fwDebug = new FileOutputStream("/opt/cacis_be/issuer_engine/debug/ISOCBLog.txt",true);
				//FileOutputStream fwDebug = new FileOutputStream("C:\\CACISEngine\\NewIssuing\\debug\\ISOLog.txt",true);
				PrintStream   pwDebug = new PrintStream(fwDebug);
				fileLogger.addListener(new SimpleLogListener(pwDebug));
				channel.setLogger(fileLogger,"ISOCACIS");
			}
			catch(Exception exp)
			{
				System.out.println(exp);
			}
		}
		System.out.println("New Mux Started");
		try{ Thread.sleep(2000);}catch(Exception exp){}
		if(cbMux.isConnected())
		{
			System.out.println("Is connected");
			//MonitorLogListener.getMonitorLogListener(8005).sendSystemLog("Visa connection is up",MonitorLogListener.BLUE);
		}
		else
		{
			System.out.println("Is not connected");
			//MonitorLogListener.getMonitorLogListener(8005).sendSystemLog(" Visa connection not up" ,MonitorLogListener.RED);
		}

	}

	public String removeOptTag(String f55)
	{

		try
		{
			TLVList tlv = new TLVList();

			tlv.unpack(ISOUtil.hex2byte(f55));

			byte[] iad = null;
			int tag;
			Enumeration enume = tlv.elements();
			while(enume.hasMoreElements())
			{
				TLVMsg tlvq = (TLVMsg)enume.nextElement();
				iad = tlvq.getValue();
				tag = tlvq.getTag();
				System.out.println(Integer.toHexString(tag) + " : " +ISOUtil.hexString(iad));
				if(Integer.toHexString(tag).equals("5f34") ||
						Integer.toHexString(tag).equals("84")   ||
						Integer.toHexString(tag).equals("9f09") ||
						Integer.toHexString(tag).equals("9f1e") ||
						Integer.toHexString(tag).equals("9f27") ||
						Integer.toHexString(tag).equals("9f34") ||
						Integer.toHexString(tag).equals("9f35") ||
						Integer.toHexString(tag).equals("9f41"))
				{

					tlv.deleteByTag(tag);
				}

				int tag9f10 =	Integer.parseInt("9f10",16);
				TLVMsg iad9f10 = tlv.find(tag9f10);
				if(iad9f10 !=null)
				{
					String strValue = ISOUtil.hexString(iad9f10.getValue());

					System.out.println("strValue="+strValue);

					if(strValue.startsWith("61"))
					{
						iad9f10.setValue(ISOUtil.hex2byte("21"));

					}
					else if(strValue.startsWith("62112233445566778899AABBCCDDEEFFFFEEDDCCBBAA99887766554433221100"))
					{
						iad9f10.setValue(ISOUtil.hex2byte("22112233445566778899AABBCCDDEEFFFFEEDDCCBBAA99887766554433221100"));
					}

					System.out.println("CCD AUTH VALUE="+strValue);

				}

			}

			f55 = ISOUtil.hexString(tlv.pack());

		}catch(Exception exp)
		{
			System.out.println(exp);
		}

		return f55;

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
		System.out.println("tag9f10 "+tag9f10);
		TLVMsg iad9f10 = tlv.find(tag9f10);
		System.out.println("tag9f101 "+tag9f10);
		if(iad9f10 != null)
		{
			System.out.println("iad9f10.getValue() "+iad9f10.getValue());
			System.out.println("9f10="+ISOUtil.hexString(iad9f10.getValue()));
			str9f10 = ISOUtil.hexString(iad9f10.getValue());
		}
		System.out.println("1");

		int tag95 =	Integer.parseInt("95",16);
		TLVMsg iad95 = tlv.find(tag95);
		if(iad95 != null)
		{
			System.out.println("95="+ISOUtil.hexString(iad95.getValue()));
			str95 = ISOUtil.hexString(iad95.getValue());
		}

		int tag9f5b =	Integer.parseInt("9f5b",16);
		TLVMsg iad9f5b = tlv.find(tag9f5b);


		if(!checkIssuerAuthFail(str95,str9f10))
		{
			System.out.println("Iss Auth Failed");
			revTLV.append(iad9f10);
			revTLV.append(iad95);
		}
		System.out.println("3");
		if(iad9f5b != null && checkIssuerScript())
		{
			System.out.println("Script Count > 1");
			revTLV.append(iad9f5b);
		}

		System.out.println("Final Reversal Req="+ISOUtil.hexString(revTLV.pack()));
		return ISOUtil.hexString(revTLV.pack());

	}



	public static boolean checkIssuerAuthFail(String tag95,String tag9f10)
	{
		System.out.println("checkIssuerAuthFail"+tag95+"   "+tag9f10);
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
		System.out.println("checkIssuerAuthFail9F10");

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


	public static boolean checkIssuerScript()throws Exception
	{
		System.out.println("checkIssuerScript");
		System.out.println("PREV F55 RES"+prevF55Res);

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
		tlv.unpack(ISOUtil.hex2byte(prevF55Res));

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