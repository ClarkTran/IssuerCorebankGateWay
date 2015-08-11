
package com.transinfo.tplus;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.jpos.iso.ISOUtil;

import com.transinfo.threadpool.ThreadPool;
import com.transinfo.threadpool.ThreadPoolException;
import com.transinfo.tplus.debug.DebugWriter;

class NDCSocketReader extends Thread
{

	private boolean keepGoing;
	private ThreadPool tpServer  =  null;
    private Socket clientConnection  =  null;
    private InputStream isClient =  null;
    private static int reqCount =1;

    NDCSocketReader(Socket clientConnection,ThreadPool tpServer)
    {
        super("SocketReader");
        keepGoing = true;
        this.clientConnection = clientConnection;
        this.tpServer = tpServer;
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
			byte[] readBuffer = new byte[512];
			ClientData objCliData = new ClientData();

			// do
            // {

					int size=0;
					String strRequest = null;
					while ((size = isClient.read(readBuffer)) != -1)
					{

						if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader:Data Arrived from Socket Channel= "+size);
						try
						{
							//byte data[] = new byte[size];
							//System.arraycopy(readBuffer,0,data,0,size);
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader:Data Read from Buffer" + ISOUtil.hexString(new String(readBuffer,0,size).getBytes()));
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader:Data Read from Buffer" + ISOUtil.hexString(readBuffer,0,size));
							System.out.println("NDCSocketReader:Data Read from Buffer" + ISOUtil.hexString(readBuffer,0,size));

							/*String strHexISO = strRequest;

							if(strHexISO.equals(TPlusConfig.NAC_KEEP_ALIVE))
							{
								strResponse = strRequest.getBytes();
								writeToClient();
								return;
						 	}*/


							objCliData.addISOPart(ISOUtil.hexString(readBuffer,0,size));
							while((strRequest = objCliData.getNextISO(1)) !=null)
							{
								if (DebugWriter.boolDebugEnabled) DebugWriter.write("SocketReader:REQUEST COUNT"+reqCount++);
								TPlusRequest reqClient = new TPlusRequest(clientConnection,strRequest);
								tpServer.addRequest(reqClient);
							}


					   	}
						catch(ThreadPoolException tp)
						{
							//elServer.addLogItem("TPLUSSERVER","267",TPlusException.getErrorType(0),"Thread pool exception  occurred when adding request to the request queue. Error:"+tp.getMessage());
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader:Thread pool exception  occurred when adding request to the request queue. Error:"+tp.getMessage());

            			}
				        catch (Exception exp)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader:Error While reading the Data from socket"+exp.getMessage());
							TPlusPrintOutput.printMessage("NDCSocketReader:",exp.getMessage());


        				}

						if(strRequest == null)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader:NULL Request arrived. ");
						}

					}

			 // }while(keepGoing && !clientConnection.isClosed());

				TPlusPrintOutput.printMessage("NDCSocketReader","stopping gracefully.");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader: stopping gracefully.");
        }
        catch(IOException e)
        {
            keepGoing = false;
            if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader: Error in reading the Data "+e.getMessage());

        }

        Stop();
    }



  public void Stop()
  {
	  try{

		 keepGoing = false;
		 if(isClient != null)
		 isClient.close();
		 if(clientConnection!=null && !clientConnection.isClosed())
		 clientConnection.close();
	 	}
	 	catch(IOException exp)
	 	{
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("NDCSocketReader: Error while closing the Socket connection.."+exp.getMessage());
		}

  }


}





