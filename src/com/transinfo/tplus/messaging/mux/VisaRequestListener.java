package com.transinfo.tplus.messaging.mux;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.iso.header.BASE1Header;

import com.transinfo.tplus.TPlusOffUsRequest;
import com.transinfo.tplus.debug.DebugMsgWriter;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.messaging.parser.ParserFactory;

@SuppressWarnings("static-access")
public class VisaRequestListener implements ISORequestListener
{
	public VisaRequestListener()
	{
	}

	protected void initialize() {
		// super.initialize();
	}

	public boolean process(ISOSource source, ISOMsg m)
	{

		try
		{
			System.out.println("Received VISA request: "+ m.getMTI());
			DebugMsgWriter.write("Received VISA Request:"+m.getMTI());
			//m.dump(System.out,"");
			DebugWriter.write("----------------  Received Request from Visa "+m.getMTI()+" ------------------");
			DebugWriter.write("       ");
			DebugWriter.writeMsgDump(m);
			//Echo
			ISOMsg res = (ISOMsg)m.clone();
			if ("0800".equals(m.getMTI()))
			{

				res.setResponseMTI();
				//swap source and destination
				BASE1Header hd = (BASE1Header)res.getISOHeader();

				String src = hd.getSource();
				String dest = hd.getDestination();
				hd.setDestination(src);
				hd.setSource(dest);
				res.setHeader(hd);
				res.setDirection(res.OUTGOING);
				res.set(39,"00");

				System.out.println("Sending Echo response");
				res.dump(System.out,"");
				source.send(res);
			}
			else if("0110".equals(m.getMTI()) || "0410".equals(m.getMTI()))
			{
				DebugMsgWriter.write("Error in Response Msg");
				System.out.println("Error in Response Msg");
				return true;
			}

			else
			{

				IParser objISOParser = (IParser)ParserFactory.createPraserObject("com.transinfo.tplus.messaging.parser.TPlus8583Parser");
				objISOParser.setMsgObject(m);
				objISOParser.setParse(true);
				objISOParser.setMsgType("ISO");

				TPlusOffUsRequest tplusOffUsRequest = new TPlusOffUsRequest();
				objISOParser = tplusOffUsRequest.processRequest(objISOParser);
				ISOMsg objResMsg = objISOParser.getMsgObject();

				if(objResMsg !=null)
				{
					objResMsg.setResponseMTI();
					res.setDirection(res.OUTGOING);

					DebugWriter.write("       ");
					System.out.println("--------------------- Response To Visa -------------------");
					DebugWriter.writeMsgDump(objResMsg);
					DebugWriter.write("       ");
					objResMsg.dump(System.out,"");
					DebugWriter.write("--------------------- Response To Visa -------------------");

					source.send(objResMsg);
				}


				return true;
			}


		}catch (Exception e)
		{
			System.out.println("Message rejected");
			DebugMsgWriter.write("Visa Request Listener Err Msg="+e);
			e.printStackTrace();
		}
		return true;
	}
}