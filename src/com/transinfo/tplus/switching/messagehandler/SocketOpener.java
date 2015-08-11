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
import java.net.Socket;


public class SocketOpener
{

    public SocketOpener(String host, int port)
    {
        socketFactory = new SocketFactory(host, port);
    }

    public Socket makeSocket(int timeout)
        throws IOException
    {

       try
       {
       	 socket = socketFactory.makeSocket();
       	 socket.setSoTimeout(timeout*1000);

       }catch(IOException ioexception)
       {
			throw ioexception;
	   }

            return socket;
    }

	ISocketFactory socketFactory;
    Socket socket;

}
