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
package com.transinfo.tplus.switching;

import java.io.IOException;
import java.net.Socket;

import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusConfig;
import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.TPlusPrintOutput;
import com.transinfo.tplus.debug.DebugWriter;
import com.transinfo.tplus.javabean.SwitchInfo;
import com.transinfo.tplus.switching.messagehandler.ConnectionHandler;
import com.transinfo.tplus.switching.messagehandler.SocketOpener;



public class SwitchManager
    implements ISwitch
{

    private SwitchManager()
    {
        _connected = false;
        //clientList = new HashMap();
        //inactiveClientList = new HashMap();
    }



public String sendMessage(SwitchInfo objSwitch,byte[] ISOMsg)throws TPlusException
{
		String strResponse=null;

		try
		{
				TPlusPrintOutput.printMessage("SwitchManager"," Connecting to Issuer "+objSwitch.getSwitchName()+" IP "+objSwitch.getIPAddress()+" Port "+objSwitch.getPort());
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("SwitchManager: Connecting to Issuer '"+objSwitch.getSwitchName()+"' IP "+objSwitch.getIPAddress()+" Port "+objSwitch.getPort());

				IConnectionHandler objConnHandler = connect(objSwitch.getSwitchName(),objSwitch.getIPAddress(),objSwitch.getPort());
				  if(objConnHandler != null && objConnHandler.isConnected())
				  {
					   strResponse =objConnHandler.sendAndRead(ISOMsg);
				  }
				  else
				  {
					throw new TPlusException(TPlusCodes.CONN_NOT_ACTIVE,"Switch Connection is not active ");
				  }

				TPlusPrintOutput.printMessage("SwitchManager"," Connected Successfully ");
				if (DebugWriter.boolDebugEnabled) DebugWriter.write("SwitchManager: Connected Successfully ");
				_connected = true;

		}catch(TPlusException exp)
		{
			throw exp;
		}
		catch(Exception exp)
		{
			 throw new TPlusException(TPlusCodes.IO_ERR_INPUT,"Error in sending message to issuer "+exp);
		}

		return strResponse;
}


public static ISwitch getInstance()
{
	return new SwitchManager();

}





public String info()
{
	if(_connected)
		return "SwitchManager is running";
	else
		return "SwitchManager is not running";
}


public int getServiceState()
{
	if(_connected)
		return RUNNING;
	else
		return STOPPED;
}



	protected IConnectionHandler connect(String switchName,String _host,int _port)throws TPlusException
	{

		Socket s = null;
		try
		{

			_socketOpener = new SocketOpener(_host,_port);
			s = _socketOpener.makeSocket(TPlusConfig.getSocketTimeout());
			_connectionHandler = new ConnectionHandler(s,switchName);
			_connected = true;

		}
		catch(IOException e)
		{
			System.out.println("Error in connecting to Issuer Host "+e.getMessage());
			throw new TPlusException(TPlusCodes.SOCK_ISS_FAIL,e.toString());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			throw new TPlusException(TPlusCodes.CONN_ISS_FAIL,e.toString());
		}


		return _connectionHandler ;
	}



    protected boolean _connected;
    protected IConnectionHandler _connectionHandler;
    protected SocketOpener _socketOpener;





}
