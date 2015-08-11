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

import java.io.IOException;
import java.io.InputStream;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.ClientData;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.switching.IConnectionHandler;
import com.transinfo.tplus.switching.IConnectionListener;



class ReaderThread extends Thread
{

    ReaderThread(InputStream in, IConnectionHandler handler)
    {
        super("SwitchManager$ReaderThread");
        keepGoing = true;
        _in = in;
        _connectionHandler = handler;
    }

    public void setListener(IConnectionListener listener)
    {
        _listener = listener;
    }

    public void run()
    {
        try
        {
            String newLine="";


            while(keepGoing )
            {


					ClientData objCliData = new ClientData();
					byte[] readBuffer = new byte[1096];

					int size=0;
					String strRequest = null;
					while ((size = _in.read(readBuffer)) != -1)
					{

						if (DebugWriter.boolDebugEnabled) DebugWriter.write("Data len="+size);
						try
						{
							objCliData.addISOPart(ISOUtil.hexString(readBuffer, 0, size));
							strRequest = objCliData.getNextISO(1);

					   	}
					   	catch(Exception exp)
					   	{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write(exp.getMessage());
							TPlusPrintOutput.printMessage("ReaderThread",exp.getMessage());
						}


						if(_listener != null && strRequest != null)
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("Data Got From Switch '"+_connectionHandler.getSwitchName()+"' "+ISOUtil.hexString(strRequest.getBytes()));
							_listener.incomingMessage(strRequest);
						}
						else
						{
							if (DebugWriter.boolDebugEnabled) DebugWriter.write("Data Got From Switch but data not arrived completly" );
						}

					}

			}
				TPlusPrintOutput.printMessage("ReaderThread","stopping gracefully."+_connectionHandler.getSwitchName());
				if (DebugWriter.boolDebugEnabled) DebugWriter.write(" stopping gracefully."+_connectionHandler.getSwitchName());
        }
        catch(IOException e)
        {
            keepGoing = false;
        }

        _connectionHandler.shutdown(false);
    }

    void pleaseStop()
    {
        keepGoing = false;
    }

    private boolean keepGoing;
    private InputStream _in;
    private IConnectionListener _listener;
    private IConnectionHandler _connectionHandler;
}


