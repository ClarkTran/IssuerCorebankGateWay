package com.transinfo.tplus.monitor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.util.LogEvent;
import org.jpos.util.LogListener;
import org.jpos.util.NameRegistrar;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.messaging.parser.IParser;


@SuppressWarnings("unchecked")
public class MonitorLogListener implements LogListener {
	public static final int BLUE = 0x00FF0000;
	public static final int RED = 0x000000FF;
	private static final int INTERNAL_MONITOR_PORT = 5555;
	private ClientThread logThread;
	static Hashtable requests = new Hashtable();

	class ClientThread extends Thread{
		private static final String SYSTEM_LOG = "SYSTEM LOG";
		private static final String TRANX_LOG =  "TRANX  LOG";
		Socket socket;
		InputStream in;
		PrintStream out;
		public ClientThread(Socket socket)
		{
			this.socket = socket;
			try
			{
				out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
				in = new BufferedInputStream(socket.getInputStream());
			}catch(Exception e){}
		}

		public void run()
		{
			try{

				while (in.read() > 0){
				}
			}catch(Exception e){
			}finally{
				try{
					socket.close();
				}catch(Exception ignore){}
			}
		}

		private String getString(ISOMsg msg, int field)
		{
			if (msg.hasField(field))
			{
				return msg.getString(field);
			}
			return " ";
		}

		public void sendTransactionLog(ISOMsg msg, int c, LogEvent ev) throws IOException, ISOException{
			String mti = msg.getMTI();
			// System.out.println("MTI=="+mti);
			if ("0810".equals(mti)){
				if ("LOGIN".equals("Login"))
					sendSystemLog("Login" + " from "+msg.getString(41), BLUE);
				else
					//sendSystemLog("Not Login" + " message is processed successfully.", BLUE);
					return;
			}

			// String key = getString(msg, 41) + getString(msg, 42);
			//debug
			//System.out.println("Looking for msg: "+key);
			//ISOMsg originalRequest = (ISOMsg)requests.get(key);
			//requests.remove(key);
			//if (originalRequest==null)
			System.out.println("Request not found");
			System.out.println("getString(msg, 39)"+getString(msg, 39));
			if (getString(msg, 39).equals("00")){
				c = BLUE;
			}else{
				c = RED;
			}
			Date now = new Date(System.currentTimeMillis());
			SimpleDateFormat df = new SimpleDateFormat("dd-MM HH:mm:ss");
			String s = TRANX_LOG + "|" + c + "|" + df.format(now);

			// tranx type
			/*
      if (ev.getSource() instanceof VAPChannel){
        s += "|" + ISOFunctions.getVisaTranxType(msg);
      }else{
        s += "|" + ISOFunctions.getPosTranxType(msg);
      }
			 */
			s += "|" + "Login";
			//String cardNumber = ISOFunctions.getCardNumber(msg);
			String cardNumber = "4565982010129243";
			if (cardNumber == null){
				cardNumber = " ";
			}
			//String expDate = ISOFunctions.getExpiryDate(msg);
			String expDate = "0909";
			if (expDate == null){
				expDate = " ";
			}
			s += "|" + getString(msg, 42)  // merchant id
			+ "|" + getString(msg, 41)  // terminal id
			+ "|" + cardNumber  // card number
			+ "|" + expDate  // exp date
			+ "|" + getString(msg, 4)  // amount
			+ "|" + "840"  // currency code
			+ "|" + getString(msg, 39)  // response code
			+ "|" + getString(msg, 38)  // approval code
			;

			/*
      if ((h.get(key)!=null)&&(!"".equals(h.get(key))))
        s = (String)h.get(key);
      else
        h.put(key, s);
			 */
			//msg.unset(new int[]{35,45,49});
			out.println(s);
			out.flush();
		}


		public void sendTransactionLog(IParser objISOParser, int c) throws IOException, ISOException,Exception{


			ISOMsg msg = objISOParser.getMsgObject();

			String mti = msg.getMTI();
			System.out.println("MTI=="+mti);
			if ("0810".equals(mti)){
				if ("LOGON".equals(objISOParser.getTranxType()))
					sendSystemLog("Logon" + " from "+msg.getString(41), BLUE);
				else
					sendSystemLog(objISOParser.getTranxType() + " message is processed successfully.", BLUE);
				return;
			}

			// String key = getString(msg, 41) + getString(msg, 42);
			//debug
			//System.out.println("Looking for msg: "+key);
			//ISOMsg originalRequest = (ISOMsg)requests.get(key);
			//requests.remove(key);
			//if (originalRequest==null)
			System.out.println("Request not found");
			System.out.println("getString1(msg, 39)"+getString(msg, 39));
			if (getString(msg, 39).equals("00")){
				c = BLUE;
			}else{
				c = RED;
			}
			Date now = new Date(System.currentTimeMillis());
			SimpleDateFormat df = new SimpleDateFormat("dd-MM HH:mm:ss");
			String s = TRANX_LOG + "|" + c + "|" + df.format(now);
			System.out.println(s);
			// tranx type
			/*
      if (ev.getSource() instanceof VAPChannel){
        s += "|" + ISOFunctions.getVisaTranxType(msg);
      }else{
        s += "|" + ISOFunctions.getPosTranxType(msg);
      }
			 */


			s += "|" + objISOParser.getTranxType();
			//String cardNumber = ISOFunctions.getCardNumber(msg);
			String cardNumber = objISOParser.getCardNumber();
			if (cardNumber == null){
				cardNumber = " ";
			}
			//String expDate = ISOFunctions.getExpiryDate(msg);
			String expDate = objISOParser.getExpiryDate();
			if (expDate == null){
				expDate = " ";
			}

			String strTranxType="";

			if(objISOParser.getValue(22).equals("012"))
			{
				strTranxType="M";
			}
			else if(objISOParser.getValue(24).equals("008"))
			{
				strTranxType="C";
			}
			else
			{
				strTranxType="I";
			}

			System.out.println(s);
			s +="|" + objISOParser.getMerchantName()  // merchant id
			+ "|" + getString(msg, 42)
			+ "|" + getString(msg, 41)  // terminal id
			+ "|" + cardNumber  // card number
			+ "|" + expDate  // exp date
			+ "|" + getString(msg, 4)  // amount
			+ "|" + objISOParser.getTranxCurr()  // currency code
			+ "|" + getString(msg, 39)  // response code
			+ "|" + getString(msg, 38)  // approval code
			+ "|" + TPlusCodes.getErrorDesc(getString(msg, 39))  // Error Desc
			+ "|" + strTranxType;
			;
			System.out.println(s);
			/*
      if ((h.get(key)!=null)&&(!"".equals(h.get(key))))
        s = (String)h.get(key);
      else
        h.put(key, s);
			 */
			//msg.unset(new int[]{35,45,49});
			System.out.println("Monitor Data="+s);
			out.println(s);
			out.flush();
		}

		public void sendSystemLog(String s, int c) throws IOException
		{
			Date now = new Date(System.currentTimeMillis());
			SimpleDateFormat df = new SimpleDateFormat("dd-MM HH:mm:ss");
			out.println(SYSTEM_LOG + "|" + c + "|" + df.format(now) + "|" + s);
			out.flush();
			System.out.println("Send data to sendSystemLog==");
		}
	}



	class AcceptConnectionThread extends Thread{
		public void run(){
			ServerSocket server;
			try{
				server = new ServerSocket(port);
			}catch(IOException e){
				return;
			}
			while (true){
				try
				{
					Socket socket = server.accept();
					MonitorLogListener.ClientThread thread = new MonitorLogListener.ClientThread(socket);
					thread.start();
					clientThreadList.add(thread);
				}catch(IOException e){}
			}
		}
	}

	int port;
	List clientThreadList = new Vector();
	Map hostMap = new Hashtable();
	/* Old log MonitorLogListener
  public MonitorLogListener(int port) {
    this.port = port;

    new MonitorLogListener.AcceptConnectionThread().start();

    TICMSAppModule am = AppModuleUtil.getTICMSAppModule();
    ViewObject view = am.findViewObject("OnlineOutgoingConnectionView");
    view.executeQuery();
    while (view.hasNext()){
      Row row = view.next();
      String connName = (String)row.getAttribute("ConnectionName");
      String remoteHost = (String)row.getAttribute("Host");
      Integer remotePort = (Integer)row.getAttribute("Port");
      hostMap.put(remoteHost + ":" + remotePort.intValue(), connName);
    }
    AppModuleUtil.releaseTICMSAppModule(am);
    ISOUtil.sleep(2000);
  }
	 */

	//New MonitorLogListener
	public MonitorLogListener(int port)
	{
		try
		{
			MonitorServer ms = new MonitorServer(port, INTERNAL_MONITOR_PORT);
			ms.start();
			logThread = new ClientThread(new Socket("localhost", INTERNAL_MONITOR_PORT));
			System.out.println("Connected to INTERNAL_MONITOR_PORT "+INTERNAL_MONITOR_PORT);
			logThread.start();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized LogEvent log(LogEvent ev)
	{
		/*
    if (ev.getSource() instanceof ISOChannel){
      String name = "Connection ";
      if (ev.payLoad.size() > 0){
        name = (String)hostMap.get(ev.payLoad.get(0));
      }
      if ("connect".equals(ev.tag)){
        sendSystemLog("Connection to " + name + " is UP", BLUE);
      }
      if ("disconnect".equals(ev.tag)){
        sendSystemLog("Connection to " + name + " is DOWN", RED);
      }
    }*/

		Vector payLoad = ev.getPayLoad();
		// System.out.println("PayLoad"+payLoad.size());
		int size = payLoad.size();
		for (int i=0; i<size; i++) {
			Object obj = payLoad.elementAt(i);

			if (obj instanceof ISOMsg) {
				ISOMsg msg = (ISOMsg)((ISOMsg) obj).clone();
				try{
					if (msg.isRequest())
					{
						System.out.println("Receive msg : "+ getKey(msg));
						requests.put(getKey(msg), msg);
					}
					if (msg.isOutgoing() && msg.isResponse()){
						sendTransactionLog(msg, ev);
					}
				}catch(Exception e){ System.out.println(e);}
			}
		}
		return ev;
	}


	public synchronized boolean logMonitor(IParser objISOParser)
	{



		try{

			ISOMsg msg = objISOParser.getMsgObject();
			System.out.println("IN LogMonitor"+msg.isOutgoing() +"  "+msg.isResponse()+"  "+msg.isRequest());
			if (msg.isRequest())
			{
				System.out.println("Receive msg : "+ getKey(msg));
				requests.put(getKey(msg), msg);
			}
			if (msg.isOutgoing() && msg.isResponse()){
				sendTransactionLog(objISOParser);
			}
		}catch(Exception e){ System.out.println(e);}

		return true;
	}


	private String getKey(ISOMsg msg)
	{
		return msg.getString(41) + msg.getString(42);
	}

	/* Old methods
  private void sendTransactionLog(ISOMsg msg, LogEvent ev){
    for (int j=0; j<clientThreadList.size(); j++){
      MonitorLogListener.ClientThread thread = (ClientThread)clientThreadList.get(j);
      try{
        thread.sendTransactionLog(msg, BLUE, ev);
      }catch(Exception e){
        clientThreadList.remove(thread);
      }
    }
    requests.remove(getKey(msg));
  }

  public void sendSystemLog(String s, int c){
    for (int j=0; j<clientThreadList.size(); j++){
      MonitorLogListener.ClientThread thread = (ClientThread)clientThreadList.get(j);
      try{
        thread.sendSystemLog(s, c);
      }catch(IOException e){
        clientThreadList.remove(thread);
      }
    }
  }
	 */

	//New methods
	private void sendTransactionLog(ISOMsg msg, LogEvent ev){
		try{
			logThread.sendTransactionLog(msg, BLUE, ev);
		}catch(Exception e){
		}
		requests.remove(getKey(msg));
	}

	private void sendTransactionLog(IParser objISOParser){
		try{
			logThread.sendTransactionLog(objISOParser, BLUE);
		}catch(Exception e){
		}
		// Check This
		//requests.remove(getKey(msg));
	}

	public void sendSystemLog(String s, int c){
		try
		{
			logThread.sendSystemLog(s, c);
		}catch(IOException e){  }
	}

	public void addRequest (ISOMsg msg)
	{
		requests.put(getKey(msg), msg);
	}

	public static MonitorLogListener getMonitorLogListener(int port){
		MonitorLogListener monitor;
		String key = MonitorLogListener.class.getName() + "._" + port;

		try {
			monitor = (MonitorLogListener)NameRegistrar.get(key);
		} catch (NameRegistrar.NotFoundException e) {
			monitor = new MonitorLogListener(port);
			NameRegistrar.register(key, monitor);
		}
		return monitor;
	}
}
