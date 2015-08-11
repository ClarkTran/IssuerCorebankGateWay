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

//Java specific imports.
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.transinfo.tplus.db.TransactionDB;
import com.transinfo.tplus.db.WriteLogDB;
import com.transinfo.tplus.debug.DebugMsgWriter;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.MessageHandler;
import com.transinfo.tplus.log.SystemLog;
import com.transinfo.tplus.messaging.RequestBaseHandler;
import com.transinfo.tplus.messaging.parser.IParser;
import com.transinfo.tplus.monitor.MonitorLogListener;
import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.DBManagerCB;

@SuppressWarnings("unchecked")
public class TPlusAdminDaemon
{
	private ServerSocket ssocAdminServer = null; // The admin server socket.
	private static SystemLog elTPlusSystem = null; // The System log
	private static TPlusAdminDaemon TPlusAdminDaemon= null;// A reference to this class
	private static boolean boolShutdownDone = false; // Flag to check if the server is shutdown
	private HashMap socketMap=null;




	/**
	 *Flag which is used to denote if the server is shutting down.
	 */
	public static boolean boolShutdown = false;


	/**
	 *  This is the constructor.
	 */

	public TPlusAdminDaemon(){}

	/**
	 * It will load the TPlusConfig class and It opens the log files to be written.
	 */

	public void initServer() throws TPlusException,Exception
	{
		boolean boolError = false;
		TPlusAdminDaemon = this;

		//Initializing ATM Switch

		try
		{

			TPlusConfig.loadProperties();
			TPlusPrintOutput.printMessage("TPlusAdminDaemon"," Properties Loaded from Config file");


			if (TPlusConfig.boolDebug)
			{
				DebugWriter.setFileNameAndFormat(TPlusConfig.strDebugFile,TPlusConfig.strDateFormatDebug);
				DebugWriter.startDebugging();
				TPlusPrintOutput.printMessage("TPlusAdminDaemon","Debug Enabled Successfully...");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Debug Enabled Successfully...");

				DebugMsgWriter.setFileNameAndFormat(TPlusConfig.strDebugMsgFile,TPlusConfig.strDateFormatDebug);
				DebugMsgWriter.startDebugging();
				TPlusPrintOutput.printMessage("TPlusAdminDaemon","Debug Message Dump File Enabled Successfully...");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Debug Message Dump File Enabled Successfully...");

			}

			DBManager.initDBPool();
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","DB Pooled Created Successfully...");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:DB Pooled Created Successfully...");

			DBManagerCB.initDBPool();
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","DB CB Pooled Created Successfully...");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:DB CB Pooled Created Successfully...");

			TPlusConfig.loadConfig();
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","Message Config Loaded Successfully...");
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Message Config Loaded Successfully...");

		}
		catch(TPlusException TPlusExp)
		{
			// if there is an error then update the error log and exit
			boolError = true;
			boolShutdown = true;
			updateAndCloseLog();
			throw TPlusExp;
		}
		catch(Exception expExp)
		{
			// if there is an error then update the error log and exit
			boolError = true;
			boolShutdown = true;
			updateAndCloseLog();
			throw expExp;
		}


		// Creating the System Log instance

		elTPlusSystem = new SystemLog();

	}


	/**
	 * This is called when the system needs to be loaded. It creates the TPlusServer
	 * daemon and also starts listening for admin requests.
	 * @throws TPlusException if there is any error while starting the socket servers.
	 */

	public void startup() throws TPlusException
	{
		TPlusPrintOutput.printMessage("TPlusAdminDaemon","Server is starting up. Please wait.");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Server is starting up. Please wait.");
		Socket clientSocket = null;
		// creating the socket servers
		try
		{
			TPlusPrintOutput.printMessage("TPlusAdminDaemon"," Starting Admin Daemon ..."+TPlusConfig.getAdminPort());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon: Starting Admin Daemon ...");
			ssocAdminServer = new ServerSocket(new Integer(TPlusConfig.getAdminPort()).intValue());

			ArrayList messageList = (ArrayList)TPlusConfig.getMessageHandler();
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","Message Handler Size="+messageList.size());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Server daemon started successfully. Listening to port "+messageList.size());

			if(messageList.size() > 0)
			{
				socketMap = new HashMap();
				for(int i=0;i<messageList.size();i++)
				{
					MessageHandler msgHandler = (MessageHandler)messageList.get(i);

					try
					{
						ServerSocket ssocTPlusServer = new ServerSocket(Integer.parseInt(msgHandler.getPort()));
						TPlusServerDaemon TPlussdServer = new TPlusServerDaemon(ssocTPlusServer,this,msgHandler);
						TPlussdServer.startServer();
						TPlusPrintOutput.printMessage("TPlusAdminDaemon","Server daemon started successfully. Listening to port......."+msgHandler.getPort());
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Server daemon started successfully. Listening to port "+msgHandler.getPort());
						socketMap.put(msgHandler.getPort(),TPlussdServer);

					}
					catch (TPlusException TPlusExp)
					{
						elTPlusSystem.addLogItem("TPlusAdminDaemon",TPlusCodes.SVR_SOCK_FAIL,TPlusException.getErrorType(0),TPlusCodes.getErrorDesc(TPlusCodes.SVR_START_FAIL)+" Error: "+TPlusExp.getMessage());
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Error while starting TPlusServer daemon for Message Port"+msgHandler.getPort());
						TPlusPrintOutput.printMessage("TPlusAdminDaemon","Error while starting the TPlus Server for Message Port"+msgHandler.getPort()+" Error:\n"+ TPlusExp.toString());
						//Stop the servers

						stopServer();
						throw TPlusExp;
					}

				}

			}
			else
			{
				TPlusPrintOutput.printMessage("No Message Handlers are configured for Request Processing");
				TPlusPrintOutput.printMessage(" Server Shuting Down........ ");
				stopServer();
			}

		}
		catch(IOException e)
		{

			//update log and close the sockets and the log files.
			elTPlusSystem.addLogItem("TPLUSSERVER",TPlusCodes.ADM_SOCK_FAIL,TPlusException.getErrorType(0),TPlusCodes.getErrorDesc(TPlusCodes.ADM_SOCK_FAIL)+" Error: "+e.getMessage());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Unable to create Server Sockets. Error: "+e.getMessage());
			closeSockets();
			updateAndCloseLog();
			throw new TPlusException(TPlusCodes.ADM_SOCK_FAIL,TPlusCodes.getErrorDesc(TPlusCodes.ADM_SOCK_FAIL)+" Error: "+e.getMessage());
		}

		//Start the TPlusServer

		if(DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon: Admin Daemon started successfully. Listening to port... "+TPlusConfig.getAdminPort());
		TPlusPrintOutput.printMessage("TPlusAdminDaemon","Admin daemon started successfully. Listening to port.... "+TPlusConfig.getAdminPort());



		try
		{
			System.out.println("Starting the Monitor Server");
			MonitorLogListener monitorLogListener = MonitorLogListener.getMonitorLogListener(8005);
			monitorLogListener.sendSystemLog("CACIS is UP", MonitorLogListener.BLUE);
		}
		catch(Exception exp)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon: Error occured while starting Transaction Monitor"+exp);
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","Error occured while starting Transaction Monitor"+exp);
		}

		/*try
		{
			System.out.println("***************************Singon Request Admin Daemon");
			SignOnRequest();
		}
		catch(Exception exp)
		{
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","Error occur while SignOn Request"+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Error occur while SignOn Request"+exp );
		}*/

		//Admin server starts listening

		while (!boolShutdown)
		{

			try
			{
				//Wait for client strInput
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Admin Daemon is waiting to service requests");
				clientSocket = ssocAdminServer.accept();
				TPlusPrintOutput.printMessage("TPlusAdminDaemon","Accepted Admin Request");
				processRequest(clientSocket);
			}
			catch(SocketException e)
			{

				TPlusPrintOutput.printMessage("TPlusAdminDaemon","Socket Exception occur while processing admin request"+e);
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Error occur while processing admin request" + e.getMessage());
			}
			catch (IOException e)
			{
				TPlusPrintOutput.printMessage("TPlusAdminDaemon","Error occur while reading admin request"+e);
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("Fatal IOError from TPlusAdminDaemon" + e.getMessage());
			}

		}


		if ((boolShutdown)&&(!boolShutdownDone))
		{

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Stopping the TPlusAdmin server");
			stopServer();
		}

	}

	/**
	 * This method is initiate all signon message for different cards
	 *
	 */

	private void SignOnRequest()
	{

		try
		{

			TransactionDB objTranx = new TransactionDB();
			Map signMap = objTranx.getSignOnList();
			Iterator iterator =signMap.values().iterator();

			while(iterator.hasNext())
			{
				try
				{

					IParser objISO = (IParser)iterator.next();
					System.out.println("Connection Name="+objISO.getConnectionName()+" Class Name="+objISO.getSignOnClass());
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:SignOn Connection Name="+objISO.getConnectionName()+" Class Name="+objISO.getSignOnClass());

					RequestBaseHandler objSignReq = (RequestBaseHandler)TPlusUtility.createObject(objISO.getSignOnClass());
					IParser resISO = objSignReq.execute(objISO);
					if(resISO != null && resISO.getValue(39).equals("00"))
					{
						System.out.println("SignOn Successful");
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:SingOn Successful " + objISO.getConnectionName());
						System.out.println("TPlusAdminDaemon:SingOn Successful " + objISO.getConnectionName());
						TPlusConfig.signonMap.put(objISO.getConnectionName(),"1");
						/* if(objISO.getConnectionName().equals("VISA"))
						 {

						    SignOnSchedularRequest signonSchReq = new SignOnSchedularRequest();
						 	Thread signonThread = new Thread(signonSchReq);
						 	//signonThread.start();

							System.out.println("TPlusAdminDaemon:inside");
						 	EchoSchedularRequest echoSchReq = new EchoSchedularRequest(signonSchReq);
						 	Thread echoThread = new Thread(echoSchReq);
						 	//echoThread.start();
					 	 }*/


					}
					else
					{
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:SingOn Successful " + objISO.getConnectionName());
						TPlusConfig.signonMap.put(objISO.getConnectionName(),"0");
					}

				}catch(Exception exp){System.out.println(exp);}
				System.out.println("next while");
			}





		}
		catch(Exception exp)
		{
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","Error occur while SignOn Request"+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Error occur while SignOn Request"+exp );
		}

	}


	/**
	 * This method is initiate all signon message for different cards
	 *
	 */

	private static void SignOffRequest()
	{

		try
		{

			System.out.println("In SignOffRequest");
			TransactionDB objTranx = new TransactionDB();
			Map signMap = objTranx.getSignOnList();
			Iterator iterator =signMap.values().iterator();


			while(iterator.hasNext())
			{
				try
				{
					IParser objISO = (IParser)iterator.next();
					System.out.println("Connection Name="+objISO.getConnectionName());
					System.out.println("Class Name="+objISO.getSignOffClass());
					RequestBaseHandler objSignReq = (RequestBaseHandler)TPlusUtility.createObject(objISO.getSignOffClass());
					System.out.println("SignOffReq Created");

					IParser resISO = objSignReq.execute(objISO);
					if(resISO != null && resISO.getValue(39).equals("00"))
					{
						TPlusPrintOutput.printMessage("TPlusAdminDaemon:SingOff Successful " + objISO.getConnectionName());
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:SingOff Successful " + objISO.getConnectionName());
						TPlusConfig.signonMap.put(objISO.getConnectionName(),"0");
					}


				}catch(Exception exp){System.out.println(exp);}
			}



		}
		catch(Exception exp)
		{
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","Error occur while SignOff Request"+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Error occur while SignOff Request"+exp );
		}

	}




	/**
	 * This method is resposible for processing the request. It calls the refreshCache() or the stopServer() method t
	 * to service the request.
	 * @param sockClient The client socket which has requested for an admin service.
	 */

	@SuppressWarnings("unused")
	private void processRequest(Socket sockClient)
	{
		PrintWriter out = null;
		//BufferedReader in = null;
		InputStream in = null;
		boolean isVoice =false;

		try
		{
			System.out.println("Before Processsing Request");

			out = new PrintWriter(sockClient.getOutputStream(), true);
			in = sockClient.getInputStream();
			String strInput = new String();
			byte[] readBuffer = new byte[512];
			int size = in.read(readBuffer);
			if(size >0)
			{
				strInput = new String(readBuffer,0,size);
			}

			System.out.println("Admin Request="+strInput+"  "+strInput.trim().length());
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon: Admin Request="+strInput+"  "+strInput.trim().length());
			String strStatus = "";


			if (strInput==null)
			{

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Invalid Request Received....");
				strInput ="Invalid Request";
				// break;
			}

			else if (strInput.trim().equals("SHUTDOWN"))
			{
				System.out.println("In Shutdown");
				stopServer(out);
				strStatus = "Server successfully shutdown";
				try
				{
					Thread.sleep(10000);
				}
				catch(Exception exp){}

			}
			else if (strInput.startsWith("PORT"))
			{
				String strInputArr[] = strInput.split("=");
				stopPort(out,strInputArr[1]);
				strStatus = "Server successfully shutdown on port strInputArr[1]";

				try
				{
					Thread.sleep(10000);
				}
				catch(Exception exp){}

			}
			else if (strInput.equals("SERVERSTATUS"))
			{

				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Status request detected");
				strStatus = "TPlus server running";
			}
			else
			{
				System.out.println("InValid Request");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon: InValid Request ");
				strInput ="Invalid Request";

			}

			//out.println(strStatus);

		}
		catch(IOException ioExp)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusAdminDaemon",TPlusCodes.IO_ERR_INPUT," while servicing admin request. :"+ioExp.getMessage());
			TPlusPrintOutput.printErrorMessage("TPlusAdminDaemon",TPlusCodes.IO_ERR_INPUT,ioExp.getMessage());
			elTPlusSystem.addLogItem("TPLUSSERVER",TPlusCodes.IO_ERR_INPUT,TPlusException.getErrorType(0),"Error while servicing admin request. Error :"+ioExp.getMessage());
			out.println("Error while servicing admin request. Error :"+ioExp.getMessage());
		}
		catch(Exception exp)
		{
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusAdminDaemon",TPlusCodes.IO_ERR_INPUT," while servicing admin request. "+exp.getMessage());
			TPlusPrintOutput.printErrorMessage("TPlusAdminDaemon",TPlusCodes.IO_ERR_INPUT,exp.getMessage());
			elTPlusSystem.addLogItem("TPLUSSERVER",TPlusCodes.ERR_ADM_REQ,TPlusException.getErrorType(0),"Unidentified Error while servicing admin request. Error :"+exp.getMessage());
			out.println("Unidentified Error while servicing admin request. Error :"+exp.getMessage());
		}
		finally
		{

			/*try
            {
				    if(in != null)
					in.close();
					if(out != null)
					out.close();
					if(sockClient != null)
					sockClient.close();
					out = null;
					WriteLogDB.updateLog(elTPlusSystem);
            }
            catch(Exception exp)
            {
                if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Error while closing Admin connection :"+exp.getMessage());
            }*/
		}

	}


	private void stopServer()
	{
		stopServer(null);
	}

	/**
	 * This method is called when the system needs to be shut down.It sets the Shutting down flag to true, closes the
	 * admin server socket and requests the TPlusServer daemon to shutdown.
	 */
	private void stopServer(PrintWriter pwAdminSock)
	{

		TPlusPrintOutput.printMessage("TPlusAdminDaemon","Shutting down the TPlus server...please wait");
		if (pwAdminSock != null) pwAdminSock.println(TPlusPrintOutput.formatMessage("TPlusAdminDaemon","Begining Shutdown routine"));

		SignOffRequest();

		boolShutdown = true;

		//Closing the Admin serversocket
		closeSockets();
		if (pwAdminSock != null) pwAdminSock.println(TPlusPrintOutput.formatMessage("TPlusAdminDaemon","Closing the Admin and TPlusServer server sockets"));


		//Shutting down the Thread pool. if the thread poll is started.
		if (TPlusConfig.tpServer != null)
		{
			TPlusConfig.tpServer.closePool();
			TPlusPrintOutput.printMessage("TPlusServerDaemon","Thread pool shutdown requested");

			//Check and wait till the threads are shutdown.
			if ((TPlusConfig.tpServer.getActiveCount() > 0) &&(TPlusConfig.tpServer.checkStatus()))
			{
				try
				{
					TPlusPrintOutput.printMessage("TPlusServerDaemon","Waiting for threads to finish processing");
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:Waiting for the threads to finish processing");
					Thread.sleep(5000);

				}
				catch(InterruptedException iexp)
				{
				}

			}

			//If there are any active threads then destroy the pool
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:Count before destroying the thread pool. Count: "+TPlusConfig.tpServer.getActiveCount());
			if (TPlusConfig.tpServer.getActiveCount() > 0)
			{
				TPlusConfig.tpServer.destroyPool();
			}

			//Clear the request queue after logging it
			Vector vectRequestQueue = TPlusConfig.tpServer.getRequestQueue();

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:Number of objects to be removed from the queue = "+vectRequestQueue.size());
			for (int intCount=0;intCount < vectRequestQueue.size();intCount++)
			{
				((TPlusRequest)vectRequestQueue.elementAt(intCount)).kill();
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:Removed object from Queue "+intCount);
			}

			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:No of active threads after destroying the thread pool = "+TPlusConfig.tpServer.getActiveCount());
		}
		TPlusPrintOutput.printMessage("TPlusServerDaemon","Thread pool shutdown successfully");

		// updating the error log and close the logfiles.
		updateAndCloseLog();

		TPlusPrintOutput.printMessage("TPlusAdminDaemon","Updated and closed the log files");
		if (pwAdminSock != null) pwAdminSock.println(TPlusPrintOutput.formatMessage("TPlusAdminDaemon","Updated and Closed the Log files"));

		boolShutdownDone = true;

		if (pwAdminSock != null) pwAdminSock.println(TPlusPrintOutput.formatMessage("TPlusAdminDaemon","Admin Deamon is shutdown successfully"));

		TPlusPrintOutput.printMessage("TPlusAdminDaemon","Admin daemon has shutdown successfully");
		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Admin and Server Daemon is shutdown successfully");

		if (pwAdminSock != null) pwAdminSock.println(TPlusPrintOutput.formatMessage("TPlusAdminDaemon","Server successfully shutdown"));

		if (pwAdminSock != null)
		{
			pwAdminSock.close();
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Admin Request is serviced");
		}


	}


	/**
	 * This method is called when the system needs to be shut down the particular port daemon.
	 */
	private void stopPort(PrintWriter pwAdminSock,String strPort){

		String strActionMessage = "";

		TPlusPrintOutput.printMessage("TPlusAdminDaemon","Please wait shutting down the TPlus server on port..."+strPort);


		if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Closing the TPlusServer server sockets on Port"+strPort);

		try {

			TPlusServerDaemon tPlusServer = (TPlusServerDaemon) socketMap.get(strPort.trim());
			if (tPlusServer != null)
			{
				tPlusServer.shutdown();
				strActionMessage = " Server daemon running on the port "+strPort+" successfully shutdown";
			}
			else
			{
				strActionMessage = " No Server running on the port "+strPort+" Please check whether port no is correct....";
			}


		}
		catch (Exception exp)
		{
			System.out.println("CloseSocket="+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Error while closing the TPlusServer server port Error:"+exp.getMessage());
			elTPlusSystem.addLogItem("TPLUSSERVER",TPlusCodes.ERR_SVR_SOCK,TPlusException.getErrorType(0),exp.getMessage());
			strActionMessage = " Exception while closing the port "+ exp;
		}


		TPlusPrintOutput.printMessage("TPlusAdminDaemon","Admin daemon has shutdown successfully");
		//if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:TPlusAdmin Daemon is shutdown successfully");

		if (pwAdminSock != null) pwAdminSock.println(TPlusPrintOutput.formatMessage("TPlusAdminDaemon",strActionMessage));
		if (pwAdminSock != null)
		{
			pwAdminSock.close();
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:"+strActionMessage);
		}

		//System.exit(0);

	}


	/**
	 * This is called when the system is shutting down.. It will close both the sockets.
	 */

	private void closeSockets()
	{
		try
		{
			if (ssocAdminServer != null)
				ssocAdminServer.close();
		}
		catch (IOException ioExp)
		{
			TPlusPrintOutput.printErrorMessage("TPlusServerDaemon",TPlusCodes.ERR_ADM_SOCK, " Error: "+ioExp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusServerDaemon",TPlusCodes.ERR_ADM_SOCK, " Error: "+ioExp);
			elTPlusSystem.addLogItem("TPLUSSERVER",TPlusCodes.ERR_ADM_SOCK,TPlusException.getErrorType(0),ioExp.getMessage());
		}

		try
		{
			Collection portCollection =   socketMap.values();
			Iterator iterator = portCollection.iterator();
			while(iterator.hasNext())
			{
				TPlusServerDaemon tPlusServer = (TPlusServerDaemon)iterator.next();
				if (tPlusServer != null)
					tPlusServer.shutdown();
			}
		}
		catch (Exception exp)
		{
			TPlusPrintOutput.printErrorMessage("TPlusAdminDaemon",TPlusCodes.APPL_ERR, " Error: Closing Server Port "+exp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusServerDaemon",TPlusCodes.ERR_ADM_SOCK, " Error: Closing Server Port "+exp);

			elTPlusSystem.addLogItem("TPLUSSERVER",TPlusCodes.ERR_SVR_SOCK,TPlusException.getErrorType(0),"while shutting down TPlus server socket: "+exp.getMessage());

		}


	}


	/**
	 * This method is used to close all the log files when the system is shutting down.
	 */
	private void closeLogFiles()
	{

		if (TPlusConfig.boolDebug) {
			DebugWriter.stopDebugging();
		}


	}

	/**
	 * This method is used to close all the log files after updating the error log.
	 */
	private void updateAndCloseLog()
	{
		//WriteLogDB.updateLog(elTPlusSystem);

		if(DBManager.getPoolStatus() && elTPlusSystem!=null)
		{
			try
			{
				WriteLogDB.updateLog(elTPlusSystem);
				DBManager.closeDBPool();

			}
			catch(Exception exp)
			{
				TPlusPrintOutput.printMessage("TPlusAdminDaemon","Error while logging/closing the DB. Error: "+exp.getMessage());
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusAdminDaemon:Error while logging/closing the DB. Error: "+exp.getMessage());
			}

		}

		closeLogFiles();

	}

	/**
	 * This method will be called when there is an error while writing log files.
	 */
	public static void shutDownServer()
	{
		boolShutdown = true;
		TPlusAdminDaemon.closeSockets();

	}



	/**
	 * This is the main method which iscalled to start the TPlusServer.
	 * @param args[] : This string array contains all the parameters which were entered while starting this class.
	 */
	public static void main(String args[])
	{
		System.out.println("Start...");
		try {

			/*TPlusConfig.getEncrKey();
			if (!TPlusLicenseDialog.checkLicense())
			{
				throw new Exception("Invalid License number");
			}*/

			TPlusAdminDaemon TPlusadServer = new TPlusAdminDaemon();
			TPlusadServer.initServer();
			TPlusadServer.startup();

		}
		catch(TPlusException exception)
		{
			TPlusPrintOutput.printErrorMessage("TPlusAdminDaemon",exception.getErrorCode(),exception.getMessage());
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","Server could not started successfully: ");

		}
		catch(Exception exception)
		{
			TPlusPrintOutput.printErrorMessage("TPlusAdminDaemon",TPlusCodes.APPL_ERR,exception.toString());
			TPlusPrintOutput.printMessage("TPlusAdminDaemon","Server could not started successfully: ");

		}



	}


}
