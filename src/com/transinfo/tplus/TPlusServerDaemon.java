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

//Java specific imports
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;

import com.transinfo.threadpool.ThreadPool;
import com.transinfo.threadpool.ThreadPoolException;
import com.transinfo.tplus.db.WriteLogDB;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.MessageHandler;
import com.transinfo.tplus.log.SystemLog;

/**
 *  This class is the TPlusServerDaemon. This class listens for Requests top be serviced
 */
public class TPlusServerDaemon extends Thread
{

    private TPlusAdminDaemon TPlusadServer = null; //This is a reference to TPlusAdminDaemon.
    private ServerSocket ssocTPlusServer = null; //This is the reference to the TPlusServerDaemon serversocket.
    private boolean boolShutDown; //Flag which shows if the system is shutting down.
    private SystemLog elServer = null; //The System log.
	private int reqCount =0;
	private String messageHandler = "";
	private MessageHandler objMsgHandler = null;


    /**
     *  This is the constructor. It will load the TPlusSever class and it initializes the thread pool.
     */
    public TPlusServerDaemon(ServerSocket ssocTPlusServer,TPlusAdminDaemon TPlusadServer,MessageHandler objMsgHandler) throws TPlusException
    {
        super("TPlusServerDaemon");
        this.ssocTPlusServer = ssocTPlusServer;
        this.TPlusadServer = TPlusadServer;
        this.objMsgHandler = objMsgHandler;
    }


    public void startServer() throws TPlusException{

        boolShutDown = false;
        elServer = new SystemLog();

        try
        {

            TPlusPrintOutput.printMessage("TPlusServerDaemon","TPlus Server Session started");

            //Initializing the ThreadPool

            if(TPlusConfig.tpServer == null)
            {

				TPlusConfig.tpServer = new ThreadPool(new Integer(TPlusConfig.getWorkerPoolThreads()).intValue());
				TPlusPrintOutput.printMessage("TPlusServerDaemon","Thread pool started. No of active threads:"+TPlusConfig.tpServer.getActiveCount());

			}
			else
			{
				 TPlusPrintOutput.printMessage("TPlusServerDaemon","Already Connections Initialised");
				 if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:Already Connections Initialised");
		    }


         }
        catch(Exception exp)
        {
            //if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:Unknown error occurred while starting the TPlusServer Daemon. Error: " + exp.getMessage());
            throw new TPlusException(TPlusCodes.SVR_START_FAIL,exp.getMessage());
        }

        //starting to listen for TPlus requests.
        start();
    }


    /**
     * This method will be runing in an infinite loop which adds the new requests to the request queue
     * to be servicd.
     */
    public void run()
    {
        startListening();
    }


    /**
     * This method starts listening for new requests and when they come will create a request object and add
     * them to the request queue.
     */
    private void startListening() {

    	//This loop will be running till the shutdown flag becomes true.
        while (!boolShutDown) {
            // Adds the request to the request queue.
            try
            {
                Socket sockClient = ssocTPlusServer.accept(); //Gets the client connection
                if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:REQUEST CONNECTION="+ ++reqCount);
                TPlusPrintOutput.printMessage("TPlusServerDaemon","MessageType="+objMsgHandler.getMessageType()+"  "+objMsgHandler.getMessageHandler());


                if(objMsgHandler!= null)
                {

					Class classObj = Class.forName(objMsgHandler.getMessageHandler());
					Class[] types = { java.net.Socket.class,com.transinfo.threadpool.ThreadPool.class,com.transinfo.tplus.javabean.MessageHandler.class };
					Constructor conObj=null;
					try
					{
						conObj = classObj.getConstructor(types);
						Object arrObj[] = new Object[3];
						arrObj[0] = sockClient;
						arrObj[1] = TPlusConfig.tpServer;
						arrObj[2] = objMsgHandler;
						com.transinfo.tplus.ISocketReader sr = (com.transinfo.tplus.ISocketReader)conObj.newInstance(arrObj);
						sr.readData();
					}
					catch(Exception exp)
					{
						if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusServerDaemon",TPlusCodes.OBJ_CREATION_FAIL,"MessageHandler Creation Faile"+exp);
						TPlusPrintOutput.printErrorMessage("TPlusServerDaemon",TPlusCodes.OBJ_CREATION_FAIL,"MessageHandler Creation Faile"+exp);
						new TPlusException(TPlusCodes.OBJ_CREATION_FAIL," MessageHandler Creation Fail "+exp.toString());
					}

				}
				else
				{
					TPlusPrintOutput.printMessage("TPlusServerDaemon","Invalid Message Handler "+objMsgHandler.getMessageHandler());
					boolShutDown =false;
			 	}


			}
            catch (Exception e)
            {
				TPlusPrintOutput.printErrorMessage("TPlusServerDaemon",TPlusCodes.IO_EXP_SVR, " Error:"+e);
				if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusServerDaemon",TPlusCodes.IO_EXP_SVR, " Error:"+e);
                elServer.addLogItem("TPLUSSERVER",TPlusCodes.IO_EXP_SVR,TPlusException.getErrorType(0),"IO Exception occurred when listening to the server port. Error:"+e.getMessage());

            }
            finally
            {
				try
				{
                  WriteLogDB.updateLog(elServer);
			 	}catch(Exception exp){}
            }
        }
    }

    /**
     * This method is called by TPlusAdminDaemon when it is shutting down.
     */
    public void shutdown()
    {
        //Sets the shutdown flag to true.
        boolShutDown = true;

        //Closing the socket server... this can be done from the TPlusServerDaemon
        try {

            ssocTPlusServer.close();
            if (DebugWriter.boolDebugEnabled) DebugWriter.write("TPlusServerDaemon:TPlusServer Daemon is shutdown successfully");
            System.out.println(" Server daemon is shutdown sucessfully");
        }
        catch(IOException ioExp)
        {
			TPlusPrintOutput.printErrorMessage("TPlusServerDaemon",TPlusCodes.ERR_SVR_SOCK, " Error: "+ioExp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusServerDaemon",TPlusCodes.ERR_SVR_SOCK, " Error: "+ioExp);
            elServer.addLogItem("TPLUSSERVER",TPlusCodes.ERR_SVR_SOCK,TPlusException.getErrorType(0),"while shutting down TPlus server socket: "+ioExp.getMessage());

		}

    }



	/**
     * This method is used to add to queue
     * @param TPlusRequest .
     */
    public void addRequest(TPlusRequest objRequest)throws ThreadPoolException
    {

		TPlusConfig.tpServer.addRequest(objRequest);
	}


}
