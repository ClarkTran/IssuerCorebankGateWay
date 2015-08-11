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

class SocketFactory
    implements ISocketFactory
{

    public SocketFactory(String host, int port)
    {
        _host = host;
        _port = port;
    }

    public Socket makeSocket()
        throws IOException
    {
		return new Socket(_host, _port);
    }

    String _host;
    int _port;
}
