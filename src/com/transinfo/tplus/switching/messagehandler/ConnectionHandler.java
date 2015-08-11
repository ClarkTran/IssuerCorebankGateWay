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
package com.transinfo.tplus.switching.messagehandler;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.ClientData;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.switching.IConnectionHandler;
import com.transinfo.tplus.switching.IConnectionListener;




public class ConnectionHandler
    implements IConnectionHandler
{

    public ConnectionHandler(Socket s,String switchName)
        throws IOException
    {
    	this.switchName = switchName;
    	_socket = s;
        _out = new BufferedOutputStream(s.getOutputStream());
        _in = s.getInputStream();

    }

    public void setListener(IConnectionListener listener)
    {
        _listener = listener;
        _reader.setListener(listener);
        _reader.start();
    }

    public void queueMessage(String message)
    {
        _writer.queueMessage(message);
    }

    public String sendAndRead(byte[] message)throws IOException,TPlusException
    {
		System.out.println("Data Send to Issuer Host"+new String(message));
		System.out.println("Data Send to Issuer Host1"+ISOUtil.hexString(message));
        _out.write(message,0,message.length);
        _out.flush();
        if (DebugWriter.boolDebugEnabled) DebugWriter.write("ConnectionHandler: Data Send to Issuer Host");
        TPlusPrintOutput.printMessage("ConnectionHandler: Data Send to Issuer Host");
        return readData();

    }


   public String readData()throws TPlusException
    {
		String strResponse="";
        try
        {

				ClientData objCliData = new ClientData();
				byte[] readBuffer = new byte[2000];

				int size=0;
				/*size = _in.read(readBuffer);
				strResponse = ISOUtil.hexString(readBuffer, 2, size);
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("ConnectionHandler: Data Received from Issuer Host "+strResponse);
				closeSocket();*/



				while ((size = _in.read(readBuffer)) != -1)
				{
					System.out.println("Data Len="+ size);
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("Data len="+size);
					try
					{
						objCliData.addISOPart(ISOUtil.hexString(readBuffer, 0, size));
						strResponse = objCliData.getNextISO(1);
						if(strResponse != null)
						 break;
					}
					catch(Exception exp)
					{
						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Error while Reading Data from Issuer= "+exp.getMessage());
						TPlusPrintOutput.printMessage("ConnectionHandler","Error Reading Data from Issuer="+exp.getMessage());
						throw new TPlusException("1001","Error while Reading Data from Switch socket"+exp);
					}

				}

				if(strResponse == null ||strResponse.equals(""))
				{
					if (DebugWriter.boolDebugEnabled) DebugWriter.write("ConnectionHandler:No Data Received from Host/ may be connection might got closed");
					TPlusPrintOutput.printMessage("ConnectionHandler","No Data Received from Host/ may be connection might got closed");
				}



        }
        catch(SocketTimeoutException socExp)
		{
		    keepGoing = false;
		    //throw new TPlusException(TPlusCodes.CONN_TIME_OUT,socExp.toString());
        }
        catch(Exception e)
        {
            keepGoing = false;
            //throw new TPlusException(TPlusCodes.IO_ERR_INPUT,"Error from Issuer socket"+e);
        }
        finally
        {
			closeSocket();
		}

		return strResponse;

    }


    public void shutdown(boolean notify)
    {
        _reader.pleaseStop();
        _writer.pleaseStop();
        _writer.flushOutputQueue();
        try
        {
            _socket.close();
        }
        catch(IOException e)
        {
            System.err.println("Error closing socket.");
            e.printStackTrace();
        }

        _listener.socketClosed(switchName);
    }

	public void closeSocket()
	{

       try
        {
            _socket.close();
        }
        catch(IOException e)
        {
            System.err.println("Error closing socket.");
            if (DebugWriter.boolDebugEnabled) DebugWriter.write("ConnectionHandler: Error Closing Socket Connection"+e);
            e.printStackTrace();
        }

	}





	/**
	 * Checks if the client is still conected.
	 * @exception SocketException if Socket is not open.
	 */
	public boolean isConnected()
	{
		if(_socket==null || _socket.isConnected()==false || _socket.isClosed()==true)
			return false;
		else
			return true;
	}

    static final void DEBUG(String s1)
    {
    }

    public String getSwitchName()
    {
		return switchName;
	}

	protected boolean keepGoing=true;
	protected String switchName;
    protected BufferedOutputStream _out;
    protected Socket _socket;
    protected InputStream _in;
    protected IConnectionListener _listener;
    protected ReaderThread _reader;
    protected WriterThread _writer;
    static final boolean DEBUG = false;
}
