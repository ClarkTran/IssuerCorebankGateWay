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

import java.io.PrintStream;
import java.util.Vector;

import org.jpos.iso.ISOUtil;

import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.switching.IConnectionHandler;



class WriterThread extends Thread
{

    WriterThread(PrintStream out,IConnectionHandler handler)
    {
        super("SwitchManager$WriterThread");
        keepGoing = true;
        _out = out;
        outgoingMessages = new Vector();
        _connectionHandler = handler;
    }

    public synchronized void run()
    {
        try
        {
            while(keepGoing)
            {
if (DebugWriter.boolDebugEnabled) DebugWriter.write("flushing out");
                 flushOutputQueue();
                 if(outgoingMessages.size() == 0)
                 wait();
            }
            TPlusPrintOutput.printMessage("WriterThread", "stopping gracefully.");
            if (DebugWriter.boolDebugEnabled) DebugWriter.write("Stopping gracefully.");
        }
        catch(InterruptedException e)
        {
            System.err.println("ConnectionHandlerLocal$WriterThread.run(): Interrupted!");
        }
    }

    void flushOutputQueue()
    {

        for(; outgoingMessages.size() > 0;)
        {
			String message = (String)outgoingMessages.elementAt(0);
			if (DebugWriter.boolDebugEnabled) DebugWriter.write("Data Written to Switch '"+_connectionHandler.getSwitchName() +"' "+ISOUtil.hexString(message.getBytes())+ "length="+message.length());
			//if (DebugWriter.boolDebugEnabled) DebugWriter.write("WriterThread: Data Written to Switch "+ ISOUtil.hexString(message.getBytes()));
			_out.print(message);
			_out.flush();
			outgoingMessages.removeElementAt(0);
            System.out.println("Queue Size="+outgoingMessages.size());
            //ConnectionHandlerLocal.DEBUG("> " + message);
        }

    }

    synchronized void queueMessage(String s)
    {
if (DebugWriter.boolDebugEnabled) DebugWriter.write("queueMsg"+outgoingMessages.size());
		outgoingMessages.addElement(s);
        notify();
    }

    void pleaseStop()
    {
        keepGoing = false;
    }

    private boolean keepGoing;
    private Vector outgoingMessages;
    private PrintStream _out;
    private IConnectionHandler _connectionHandler;
}
