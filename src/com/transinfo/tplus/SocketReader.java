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

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.jpos.iso.ISOUtil;

import com.transinfo.threadpool.ThreadPool;
import com.transinfo.threadpool.ThreadPoolException;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.MessageHandler;

public class SocketReader implements ISocketReader
{

	private boolean keepGoing;
	private ThreadPool tpServer  =  null;
    private Socket clientConnection  =  null;
    private InputStream isClient =  null;
    private static int reqCount =1;
    private MessageHandler objMsgHandler = null;

   public  SocketReader(){}

   public  SocketReader(Socket clientConnection,ThreadPool tpServer,MessageHandler objMsgHandler)
    {

        keepGoing = true;
        this.clientConnection = clientConnection;
        this.tpServer = tpServer;
        this.objMsgHandler = objMsgHandler;
    }




	public void readData()
	{
		System.out.println("Start Reading From Socket");
		Thread thread = new Thread(this);
		thread.start();

	}


    /**
     *  This Method reads the data from the NAC socket connection and update the variables in this class.
     *  @throws TPlusException This exception will be thrown if there is an error reading the input.
     */


    public void run()
    {
        try
        {

			isClient = clientConnection.getInputStream();
			byte[] readBuffer = new byte[1024];
			ClientData objCliData = new ClientData();
			int socReadCont = 0;

			 do
             {

					int size=0;
					String strRequest = null;
					while ((size = isClient.read(readBuffer)) != -1)
					{

						if (DebugWriter.boolDebugEnabled) DebugWriter.write("SocketReader:Data Arrived from Socket Channel= "+size);
						try
						{
							socReadCont = 0;
							//byte data[] = new byte[size];
							//System.arraycopy(readBuffer,0,data,0,size);
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("SocketReader:Data Read from Buffer " + ISOUtil.hexString(new String(readBuffer,0,size).getBytes()));

							TPlusPrintOutput.printMessage("SocketReader:Data Read from Buffer " + ISOUtil.hexString(readBuffer,0,size));


							objCliData.addISOPart(ISOUtil.hexString(readBuffer,0,size));
							while((strRequest = objCliData.getNextISO(1)) !=null)
							{
								if (DebugWriter.boolDebugEnabled) DebugWriter.write("SocketReader: Current Request Count... "+reqCount++);
								TPlusRequest reqClient = new TPlusRequest(clientConnection,strRequest,objMsgHandler);
								tpServer.addRequest(reqClient);
							}


					   	}
						catch(ThreadPoolException tp)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("SocketReader",TPlusCodes.THREAD_EXP," while reading Terminal request. "+tp.getMessage());
							//elServer.addLogItem("TPLUSSERVER","267",TPlusException.getErrorType(0),"Thread pool exception  occurred when adding request to the request queue. Error:"+tp.getMessage());
							TPlusPrintOutput.printErrorMessage("TPlusAdminDaemon",TPlusCodes.THREAD_EXP,"while reading Terminal request. "+tp.getMessage());

            			}
				        catch (Exception exp)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("SocketReader",TPlusCodes.IO_ERR_INPUT," while reading Terminal request. "+exp.getMessage());
							//elServer.addLogItem("TPLUSSERVER","267",TPlusException.getErrorType(0),"Thread pool exception  occurred when adding request to the request queue. Error:"+tp.getMessage());
							TPlusPrintOutput.printErrorMessage("TPlusAdminDaemon",TPlusCodes.IO_ERR_INPUT,"while reading Terminal request. "+exp.getMessage());

        				}

						if(strRequest == null)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("SocketReader:NULL Request arrived. ");
						}

					}


					if(socReadCont == 100)
					{
						keepGoing = false;
					}
					else
					{
						socReadCont = socReadCont+1;
				 	}

			  }while(keepGoing && !clientConnection.isClosed() && !TPlusConfig.isMultipleSocket());

				TPlusPrintOutput.printMessage("SocketReader","stopping gracefully.");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("SocketReader: stopping gracefully.");
        }
        catch(IOException e)
        {
            keepGoing = false;
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("SocketReader",TPlusCodes.IO_ERR_INPUT," while reading Terminal request. "+e.getMessage());
			//elServer.addLogItem("TPLUSSERVER","267",TPlusException.getErrorType(0),"Thread pool exception  occurred when adding request to the request queue. Error:"+tp.getMessage());
			TPlusPrintOutput.printErrorMessage("SocketReader",TPlusCodes.IO_ERR_INPUT,"while reading Terminal request. "+e.getMessage());


        }


    }



  public boolean stopReading()
  {
	  boolean status = false;
	  try{

		 keepGoing = false;
		 if(isClient != null)
		 isClient.close();
		 if(clientConnection!=null && !clientConnection.isClosed())
		 clientConnection.close();
		 status = true;
	 	}
	 	catch(IOException ioExp)
	 	{
			TPlusPrintOutput.printErrorMessage("TPlusServerDaemon",TPlusCodes.ERR_SVR_SOCK, " Error: "+ioExp);
			if (DebugWriter.boolDebugEnabled) DebugWriter.writeError("TPlusServerDaemon",TPlusCodes.ERR_SVR_SOCK, " Error: "+ioExp);


		}

		return status;

  }


}





